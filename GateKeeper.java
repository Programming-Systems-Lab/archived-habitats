package psl.habitats;
import java.io.*;
import java.util.Iterator;
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
	static private String sienaMaster = "senp://canal.psl.cs.columbia.edu:31331";
  static private HierarchicalDispatcher hd = null;

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
    Filter filter = null;

    filter = new Filter();
    filter.addConstraint("habitatCategory", masterHabitat.getCategory());
    new FilterThread(hd, filter, this).start();
    
    filter = new Filter();
    filter.addConstraint("habitatName", masterHabitat.getName());
    new FilterThread(hd, filter, this).start();
  }
  
  public void notify(Notification[] e) {}
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

	public void broadcast_req(String serName, Hashtable h) {
    // maintain local reference to hashTable: h
    currServiceReqList.put(serName, h);
    
    Notification n = new siena.Notification();
    for (Enumeration e=h.keys(); e.hasMoreElements(); ) {
      String key = e.nextElement().toString();
      n.putAttribute(key, h.get(key));
    }
		n.putAttribute("source", masterHabitat.getName());
		n.putAttribute("requestingService", serName);     
    
		try {
      // send out event
			hd.publish(n);
		} catch (siena.SienaException se) {
			se.printStackTrace();
		}
	}

  private boolean allowed(String _sName, String _sDesc) {
    return true;
  }

  /**
   * @deprecated
   */
	private void create_treaty(String src, String dest, int ID, 
                             java.lang.Object ip_param, java.lang.Object ret_param, 
                             int numIP, int numOp) {
		Treaty curr_treaty = new Treaty(src, dest, ID, numIP, numOP);
		if (ip_param instanceof Vector) {
			curr_treaty.add_ip_list((Vector)ip_param);
		}
		else curr_treaty.add_ip_param(ip_param);
		summits_list.put(new Integer(ID),curr_treaty);
	}
	private int getNextID() {
		return Math.random() * 1000;
	}

	public void processServiceRequest(Notification n_request) {
		// do some simple security clearance
		// assign an ID number
		// create a new treaty and add it to the list of sumits
		// if security cleared create a proxy of the service object

		String srcHabitat = null, srcServiceDes = null, req4serDes = null;
		int totalParams = 0, totalRetvals = 0;
		Notification res_notif = new Notification();
    
		AttributeValue av = n_request.getAttribute("source");
		if (av != null) srcHabitat = av.stringValue();
		
    av = n_request.getAttribute("requestingService");
		if (av != null) srcService = av.stringValue();
		
    av = n_request.getAttribute("Request4Service");
		if (av != null) req4serDes = av.stringValue();
		
    av = n_request.getAttribute("totalParams");
		if (av != null) totalParams = av.intValue();		// total parameters
		
    av = n_request.getAttribute("totalRetvals");
		if (av != null)	totalRetvals = av.intValue(); 	// total return values 
		
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
			
			Vector params = new Vector();
			Vector retvals = new Vector();

      for (int i=1; i<=totalParams; i++) {
				av = n_request.getAttribute("params" + i);
				if (av != null) params.add(av.stringValue());
			}
      
			for (int i=1; i<=totalRetvals; i++) {
				av = n_request.getAttribute("revals" + i);
				if (av != null) retvals.add(av.stringValue());
			}

      summits_list.put(new Integer(ID), 
                       new Treaty(req4serDes, srcServiceDes, ID, 
                                  srcHabitat, masterHabitat.getName(), 
                                  params, retvals));
			// create a new treaty and a new proxy for the requested service
			// create_treaty(srcHabitat, masterHabitat.getName(),srcService,req4serDes,ID, ip, op,(1+totalIP));
		}
		else {
			// send a -ve response
			res_notif.putAttribute("habitatName", srcHabitat);
			res_notif.putAttribute("EventType", "Response2ServiceRequest");
			res_notif.putAttribute("Access", "Denied");
		}
    
		try {
      // treaty created, now send reply to requesting habService
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
          ID = av.intValue();
          String clientHabService = n.getAttribute("RequestingService").stringValue();
          String serverHabService = n.getAttribute("Request4Service").stringValue();
          String serverHabitat = oldHash.get("habitatCategory") + "";
          
          Hashtable oldHash = (Hashtable) currServiceReqList.get(clientHabService);
          int totalParams = Integer.parseInt("" + oldHash.get("totalParams"));
          int totalRetvals = Integer.parseInt("" + oldHash.get("totalRetvals"));

          // figure out num params/retvals
          Vector params = new Vector(totalParams), retvals = new Vector(totalRetvals);
          for (int i=0; i<totalParams; i++) params.add(oldHash.get("params" + i));
          for (int i=0; i<totalRetvals; i++) retvals.add(oldHash.get("retvals" + i));

          Treaty t = null; 
          summits_list.put(new Integer(ID), 
                           t = new Treaty(serverHabService, clientHabService, ID, 
                                      serverHabitat, masterHabitat.getName(),
                                      params, retvals));

          // treaty created, now notify requesting habService
          Hashtable newHash
            = masterHabitat.getService(serName).performService(serverHabService, null);
          
          n = new siena.Notification();
          for (Enumeration e = newHash.keys(); e.hasMoreElements(); ) {
            String key = e.nextElement().toString();
            if (t.valid_ip_param(key)) n.putAttribute(key, newHash.get(key));
          }
          n.putAttribute("CurrentSummitID", "" + ID);
          n.putAttribute("habitatCategory", "" + masterHabitat.getName());
          
          Filter f1 = new Filter();
          f1.addConstraint("CurrentSummitID", ID);
          f1.addConstraint("habitatCategory", serverHabitat);
          
          new FilterThread(hd, f1, new SummitHandler(t, this)).start();
          
          // send out to server 
          hd.publish(n);
          
        } else { /* nothing */ }
      } else {
        System.out.println("Access Denied");
        // masterHabitat.notifyService(); .. wake up the service waiting for this failure notification
      }
      // response to the service request
      // create a treaty with the params 
      // save the ID number recieved by the remote GK
      // notify the service to start using the service
    } else { /* nothing */ }
  }
	
	class SummitHandler implements Notifiable {
    final Treaty treaty;
		final GateKeeper masterGK;
		public SummitHandler(Treaty _t, GateKeeper _gk) {
      treaty = _t;
			masterGK = _gk;
		}
  	public void notify(Notification n[]) {}
  	public void notify(Notification n) {
      
      // need validation in here
      
      Notification send_notif = new Notification();
      Hashtable retvals = new Hashtable();
      for (Iterator i = n.iterator(); i.hasNext(); ) {
        String key = "" + i.next();
        if (!key.equals("CurrentSummitID") && !key.equals("habitatCategory")) {
          if (treaty.valid_ip_param(key) || treaty.valid_op_param(key)) {
            retvals.put(key, n.getAttribute(key).stringValue());            
          } else {
            send_notif.clear();
            send_notif.putAttribute("Terminate Summit" , "Invalid number of params");
		  		  return;
          }
        }
      }
 
      ServiceInterface sInf = masterHabitat.getService(treaty.clientHabService);
      Hashtable newHash = sInf.performService(treaty.serverHabService, retvals);

      send_notif = new siena.Notification();
      for (Enumeration e = newHash.keys(); e.hasMoreElements(); ) {
        String key = e.nextElement().toString();
        if (t.valid_ip_param(key)) 
          send_notif.putAttribute(key, newHash.get(key));
        else {
          send_notif.clear();
          send_notif.putAttribute("Terminate Summit" , "Invalid number of params");
				  return;
        }
      }
      n.putAttribute("CurrentSummitID", "" + ID);
      n.putAttribute("habitatCategory", "" + masterHabitat.getName());
          
      // send out to server 
      hd.publish(send_notif);
      
      
      /* Notification send_notif = new Notification();
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

				// might be replaced by a lookup on a corba object
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
			}*/
		}
	}
}
