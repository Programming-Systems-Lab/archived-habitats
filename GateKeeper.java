package psl.habitats;
import java.io.*;
import java.util.Iterator;
import java.util.Enumeration;
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
	static private final Hashtable currServiceReqList = new Hashtable(); 
	static private String sienaMaster = "senp://canal.psl.cs.columbia.edu:31331";
  static HierarchicalDispatcher hd = null;
  static String myDescription = "Gatekeeper";

	public GateKeeper(Habitat h, String sm) {
		masterHabitat = h;
		summits_list = new Hashtable();
		// connect to siena
    myDescription += "_" + masterHabitat.getName();
		hd = new HierarchicalDispatcher();
    try {
      if (sm != null) sienaMaster = sm;
      hd.setMaster(sienaMaster);
      // masterHabitat.log(": master is " + sienaMaster);
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
    masterHabitat.log("GateKeeper: recevied ");
    AttributeValue rav = n.getAttribute("EventType");
    if (rav == null) {
			masterHabitat.log("Received a null attribute for: EventType");
			return;
		}
    
		String av = rav.stringValue();
    
    if(av.equals("ServiceRequest")) {
			processServiceRequest(n);
    } else if(av.equals("Response2ServiceRequest")) {
			processResponse2ServiceRequest(n);
    }
	}

  private static final String SERVER_S = "ServerService";
  private static final String CLIENT_S = "ClientService";
  private static final String TOTALPARAMS = "totalParams";
  private static final String TOTALRETVALS = "totalRetvals";

	public void broadcast_req(String clientService, String serverService, Hashtable h) {
    masterHabitat.log("GateKeeper: broadcast_req for " + serverService);
    // maintain local reference to hashTable: h
    currServiceReqList.put(clientService, h);
    
    Notification n = new siena.Notification();
    for (Enumeration e=h.keys(); e.hasMoreElements(); ) {
      String key = e.nextElement().toString();
      n.putAttribute(key, "" + h.get(key));
    }
		n.putAttribute("source", masterHabitat.getName());
		n.putAttribute(SERVER_S, serverService);     
		n.putAttribute(CLIENT_S, clientService);     
    
		try {
      masterHabitat.log("GateKeeper: broadcasting: ");
      // send out event
			hd.publish(n);
		} catch (siena.SienaException se) {
			se.printStackTrace();
		}
	}

  private boolean allowed(String _sName, String _sDesc) {
    return true;
  }

	private int getNextID() {
		return (int) (Math.random() * 1000);
	}

  public void processServiceRequest(Notification n) {
    masterHabitat.log(myDescription + ":: processing a Service Request ");
		// do some simple security clearance
		// assign an ID number
		// create a new treaty and add it to the list of sumits
		// if security cleared create a proxy of the service object

		String clientHabitat = null, clientService = null, serverService = null;
		int totalParams = 0, totalRetvals = 0;
		Notification res_notif = new Notification();
    
		AttributeValue av = n.getAttribute("source");
		if (av != null) clientHabitat = av.stringValue();
		
    av = n.getAttribute(CLIENT_S);
		if (av != null) clientService = av.stringValue();
		
    av = n.getAttribute(SERVER_S);
		if (av != null) serverService = av.stringValue();
		
    av = n.getAttribute(TOTALPARAMS);
		if (av != null) totalParams = av.intValue();		// total parameters
		
    av = n.getAttribute(TOTALRETVALS);
		if (av != null)	totalRetvals = av.intValue(); 	// total return values 
		
    masterHabitat.log("Service Request for -> " + serverService + " from -> " + clientHabitat);
    masterHabitat.localService("Security Check on " + clientHabitat);
    if(allowed(clientHabitat, clientService)) {
      masterHabitat.localService("Security Check Cleared on " + clientHabitat);
			// send a +ve response
			int ID = getNextID();
			res_notif.putAttribute("habitatName", clientHabitat);
			res_notif.putAttribute("EventType", "Response2ServiceRequest");
			res_notif.putAttribute("Access", "Allowed");			// could have used ID only but just in case
			res_notif.putAttribute("Treaty", "AcceptedAll");	
			/* AcceptAll = all the ip_params and op_params sent in the 
			*  request were allowed. OR
			*  New Treaty = the new treaty description follows thsi event 
			*  that gives a new description of the ip_params and the op_params
			*  currently only AcceptAll implemented
			*/
			res_notif.putAttribute(CLIENT_S, clientService);
			res_notif.putAttribute(SERVER_S, serverService);
			res_notif.putAttribute("ID", ID); 
			
			Vector params = new Vector();
			Vector retvals = new Vector();

      for (int i=1; i<=totalParams; i++) {
				av = n.getAttribute("params" + i);
				if (av != null) params.add(av.stringValue());
			}
      
			for (int i=1; i<=totalRetvals; i++) {
				av = n.getAttribute("retvals" + i);
				if (av != null) retvals.add(av.stringValue());
			}

      masterHabitat.localService("Negotiating with " + clientHabitat);
      masterHabitat.localService("New Treaty and Summit ID created for " + clientHabitat);
      
      Treaty t = null;
      summits_list.put(new Integer(ID), 
                       t = new Treaty(serverService, clientService, ID, 
                                  clientHabitat, masterHabitat.getName(), 
                                  params, retvals));

      Filter f1 = new Filter();
      f1.addConstraint("CurrentSummitID", ID + "_" + serverService);
      new FilterThread(hd, f1, new SummitHandler(clientService, serverService, t, this)).start();

		}
		else {
			// send a -ve response
			res_notif.putAttribute("habitatName", clientHabitat);
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
    masterHabitat.log( myDescription + " :: Recieved Response2ServiceRequest");
    AttributeValue av = n.getAttribute("Access");
    int ID;
    if (av != null) {
      if ((av.stringValue()).equals("Allowed")) {
        av = n.getAttribute("ID");
        if (av != null) {
          ID = av.intValue();
          String clientHabService = n.getAttribute(CLIENT_S).stringValue();
          String serverHabService = n.getAttribute(SERVER_S).stringValue();
          masterHabitat.log( myDescription + " :: Allowed access to " + serverHabService);
          
          Hashtable oldHash = (Hashtable) currServiceReqList.get(clientHabService);
          String serverHabitat = oldHash.get("habitatCategory") + "";

          int totalParams = Integer.parseInt("" + oldHash.get("totalParams"));
          int totalRetvals = Integer.parseInt("" + oldHash.get("totalRetvals"));

          // figure out num params/retvals
          Vector params = new Vector(totalParams), retvals = new Vector(totalRetvals);
          for (int i=1; i<=totalParams; i++) params.add(oldHash.get("params" + i));
          for (int i=1; i<=totalRetvals; i++) retvals.add(oldHash.get("retvals" + i));

          masterHabitat.log( myDescription + " :: Creating New Treaty ");
          Treaty t = null; 
          summits_list.put(new Integer(ID), 
                           t = new Treaty(serverHabService, clientHabService, ID, 
                                      serverHabitat, masterHabitat.getName(),
                                      params, retvals));

          // t.printIPList(); t.printOPList();
          // treaty created, now notify requesting habService
          Hashtable newHash
            = masterHabitat.getService(clientHabService).performService(serverHabService, null);
          
          masterHabitat.log( myDescription + "Notifying Service " + clientHabService + "to start using Remote Service " + serverHabService);

          n = new siena.Notification();
          for (Enumeration e = newHash.keys(); e.hasMoreElements(); ) {
            String key = e.nextElement().toString();
            if (t.valid_ip_param(key) || t.valid_op_param(key))
              n.putAttribute(key, "" + newHash.get(key));
            else masterHabitat.log("Invalid IP");
          }
          n.putAttribute("CurrentSummitID", ID + "_" + serverHabService);
          
          Filter f1 = new Filter();
          f1.addConstraint("CurrentSummitID", ID + "_" + clientHabService);
          new FilterThread(hd, f1, new SummitHandler(serverHabService, clientHabService, t, this)).start();

          try {
            masterHabitat.log("GateKeeper: publishing: ");
            // send out to server 
            hd.publish(n);
          } catch (SienaException se) {
            se.printStackTrace();
          }
          
        } else { /* nothing */ }
      } else {
        masterHabitat.log("Access Denied");
        // masterHabitat.notifyService(); .. wake up the service waiting for this failure notification
      }
      // response to the service request
      // create a treaty with the params 
      // save the ID number recieved by the remote GK
      // notify the service to start using the service
    } else { /* nothing */ }
    masterHabitat.log("GateKeeper: processResponse2ServiceRequest ended: ");
  }
	
	class SummitHandler implements Notifiable {
    final Treaty treaty;
		final GateKeeper masterGK;
    final String targetService;
    final String localService;
		public SummitHandler(String ts, String ls, Treaty _t, GateKeeper _gk) {
      treaty = _t;
			masterGK = _gk;
      targetService = ts;
      localService = ls;
		}
  	public void notify(Notification n[]) {}
  	public void notify(Notification n) {
      masterHabitat.log("SummitHandler: notify ");
      // need validation in here
      
      Notification send_notif = new Notification();
      Hashtable retvals = new Hashtable();

      boolean terminateSummit = false;
      for (Iterator i = n.attributeNamesIterator(); i.hasNext(); ) {
        String key = "" + i.next();
        if (!key.equals("CurrentSummitID") && !key.equals("habitatCategory")) {
          if (treaty.valid_ip_param(key) || treaty.valid_op_param(key)) {
            retvals.put(key, n.getAttribute(key).stringValue());            
          } else {
            send_notif.clear();
            send_notif.putAttribute("Terminate Summit" , "Invalid number of params");
            terminateSummit = true;
		  		  break;
          }
        }
      }

      masterHabitat.log(treaty.printIPList());
      masterHabitat.log(treaty.printOPList());
      if (! terminateSummit) {
        ServiceInterface sInf = masterHabitat.getService(localService);
        Hashtable newHash = sInf.performService(treaty.serverHabService, retvals);
        
        send_notif = new siena.Notification();
        boolean isResultNull = false;

        if (newHash == null) {
          send_notif.clear();
          send_notif.putAttribute("Terminate Summit" , "Nothing to be returned");
        } else {
          for (Enumeration e = newHash.keys(); e.hasMoreElements(); ) {
            String key = e.nextElement().toString();
            if (treaty.valid_ip_param(key) || treaty.valid_op_param(key)) {
              send_notif.putAttribute(key, "" + newHash.get(key));
            } else {
            // masterHabitat.log("SummitHandler: here, terminating, " + key);
              send_notif.clear();
              send_notif.putAttribute("Terminate Summit" , "Invalid number of retvals");
			    	  break;
            }
          }
        }
      }
      send_notif.putAttribute("CurrentSummitID", treaty.getID() + "_" + targetService);
      try {
        // send out to server
        hd.publish(send_notif);
      } catch (SienaException se) {
        se.printStackTrace();
      }
      
      
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
