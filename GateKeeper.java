package psl.habitats;
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import siena.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

// Alpa : need to replace the siena Notification with a generalised service descritpion
// Alpa: need to add someway of removing treaty when recieved BYE

public class GateKeeper implements Notifiable {
	static private Habitat masterHabitat;
	static private Hashtable summits_list; 
	static private Hashtable currServiceReqList; 
	static private String sienaMaster = "senp://canal.psl.cs.columbia.edu:31331";  static private HierarchicalDispatcher hd = null;

	public GateKeeper(Habitat h) {
		masterHabitat = h;
		summits_list = new Hashtable();
		// connect to siena
		// Alpa : will read senp host from properties file
		hd = new HierarchicalDispatcher();
    try {   
      hd.setMaster(sienaMaster);
      System.out.println(": master is " + sienaMaster);
    } catch (siena.InvalidSenderException ihe) {
      ihe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    Filter f1 = new Filter();
    f1.addConstraint("habitatCategory", masterHabitat.getCategory());
    f1.addConstraint("habitatName", masterHabitat.getName());

    FilterThread t = new FilterThread(hd, f1, this);
    t.start();
  }
  public void notify(Notification n) {
    AttributeValue rav = n.getAttribute("EventType");
    if (rav == null) {
			System.out.println("Received a null attribute for: EventType");
			return;
		}
		String av = rav.stringValue();
		if(av == "ServiceRequest");
			processServiceRequest(n);
		if(av == "Response2ServiceRequest");
			processResponse2ServiceRequest(n);
	}
  public void notify(Notification[] e) {}

	public void broadcast_req(String serName, siena.Notification n) {
		n.putAttribute("source", masterHabitat.getName());
		n.putAttribute("requestingService", serName);     
		try {
			hd.publish(n);
		} catch(siena.SienaException se) {
			se.printStackTrace();
		}
		currServiceReqList.put(serName, n);
	}    private boolean allowed(String _sName, String _sDesc) {
    return true;  }

	private void create_treaty(String src, String dest, int ID, java.lang.Object ip_param, java.lang.Object ret_param, int numIP) {
		Treaty curr_treaty = new Treaty(src, dest, ID, numIP);
		if (ip_param instanceof Vector) {
			curr_treaty.add_ip_list((Vector)ip_param);
		}
		else curr_treaty.add_ip_param(ip_param);
		summits_list.put(new Integer(ID),curr_treaty);
	}
	private int getNextID() {
		return 0;
	}

	public void processServiceRequest(Notification n_request) {
		// do some simple security clearance
		// assign an ID number
		// create a new treaty and add it to the list of sumits
		// if security cleared create a proxy of the service object

		String srcHabitat = null, srcService = null;
		int totalIP = 0, totalOP = 0;
		Notification res_notif = new Notification();
		AttributeValue av = n_request.getAttribute("source");
		if (av != null) 
			srcHabitat = av.stringValue();
		av = n_request.getAttribute("requestingService");
		if (av != null) srcService = av.stringValue();
		av = n_request.getAttribute("totalPpparams");
		if (av != null) totalIP = av.intValue();						// total imput parameters
		av = n_request.getAttribute("totaOPpparams");
		if (av != null)	
			totalOP = av.intValue(); 	// total output parameters 
		if(allowed(srcHabitat,srcService)) {
			// send a +ve response
			int ID = getNextID();
			res_notif.putAttribute("habitatName", srcHabitat);
			res_notif.putAttribute("EventType", "Response2ServiceRequest");
			res_notif.putAttribute("Access", "Allowed");			// could have used ID only but just in case
			res_notif.putAttribute("Treaty", "AcceptedAll");	
			/* AcceptAll = all the ip_params and op_params sent in the 
			*  request were allowed. OR
			*  New Treaty = the new treaty description follows thsi event 
			*  that gives a new description of the ip_params and the op_params
			*  currently only AcceptAll implemented
			*/
			res_notif.putAttribute("requestingService" , srcService);
			res_notif.putAttribute("IDnumber", ID); 
			
			Vector ip = new Vector();
			Vector op = new Vector();
			int ind;
			for (ind =1; ind <= totalIP; ind++) {
				String att = "param";
				att += ind;
				av = n_request.getAttribute(att);
				if (av != null) {
					ip.add(av.stringValue());
				}
			}
			for (ind =1; ind <= totalOP; ind++) {
				String att = "retParam";
				att += ind;
				av = n_request.getAttribute(att);
				if (av != null) {
					op.add(av.stringValue());
				}
			}
			create_treaty(srcHabitat, masterHabitat.getName(), ID, ip, op,(1+totalIP));
			// create a new treaty and a new proxy for the requested service
		}
		else {
			// send a -ve response
			res_notif.putAttribute("habitatName", srcHabitat);
			res_notif.putAttribute("EventType", "Response2ServiceRequest");
			res_notif.putAttribute("Access", "Denied");
		}
		try {
			hd.publish(res_notif);
		} catch (siena.SienaException se) {
			se.printStackTrace();
		}
	}
	private Vector get_IP_params_from_Notification(Notification n) {
		return (new Vector());
	}
	public void processResponse2ServiceRequest(Notification n) {
		AttributeValue av = n.getAttribute("Access");
		int ID;
		if (av != null) {
			if ((av.stringValue()).equals("Allowed")) {
				av = n.getAttribute("ID");
				if (av != null) {
					ID = av.intValue();          String serName = n.getAttribute("RequestingService").stringValue();
					Notification serv_old_notif = (Notification) currServiceReqList.get(serName);
					Vector ip = get_IP_params_from_Notification(serv_old_notif);
					Vector op = get_IP_params_from_Notification(serv_old_notif);
					String srcHabitat = serv_old_notif.getAttribute("habitatName").stringValue();
					create_treaty(srcHabitat, masterHabitat.getName(), ID, ip, op, ip.size());
				// masterHabitat.notifyService(); .. wake up the service waiting for this
					Filter f1 = new Filter();
					f1.addConstraint("CurrentSummitID", ID);
					FilterThread f_summit = new FilterThread(hd, f1, new SummitHandler(ID, this));
					}
			}
			else {
				System.out.println("Access Denied");
				// masterHabitat.notifyService(); .. wake up the service waiting for this failure notification
			}
		// response to the service request
		// create a treaty with the params 
		// save the ID number recieved by the remote GK
		// notify the service to start using the service
	}
}
	
	class SummitHandler implements Notifiable {
	  int ID;
		GateKeeper masterGK;
		public SummitHandler(int _ID, GateKeeper _gk) {
			ID = _ID;
			masterGK = _gk;
		}
  	public void notify(Notification n[]) {}
  	public void notify(Notification n) {
			Notification send_notif = new Notification();
			send_notif.putAttribute("currentSummitID", ID);
			Treaty currTreaty = (Treaty) masterGK.summits_list.get(new Integer(ID)); 
			// check for termination by peer 
			if (send_notif.size() != currTreaty.get_IP_size()) {
				send_notif.putAttribute("Terminate Summit" , "Invalid number of params");
				return;
			} else { 
				Vector ip = (Vector) currTreaty.getIPList();
			  Hashtable forService = new Hashtable();
			  for(int i =0; i < ip.size(); i++) {
			  	String ip_par = (String) ip.elementAt(i);
			  	AttributeValue av = send_notif.getAttribute(ip_par);
			  	if (av!= null) {
			  		forService.put(ip_par, av.stringValue()); // need to pass the list to the params
			  	}
			  }

				// this must be replaced by a lookup on a corba object
				ServiceInterface _service = masterHabitat.getService(currTreaty.servUsed);
				Vector result2Bsent = _service.performService(forService);

				for (int _v = 0; _v < result2Bsent.size(); _v++) {
					MyAttributeValuePair _avp = (MyAttributeValuePair) result2Bsent.elementAt(_v);
					if (currTreaty.valid_op_param(_avp.getValue())) {
						send_notif.putAttribute(_avp.getAttribute(), _avp.getValue());
					}
					else {
						send_notif.clear();
						send_notif.putAttribute("Terminate Summit" , "Invalid number of params");
						// break;
					}
				}
			}
		}
	}
}
