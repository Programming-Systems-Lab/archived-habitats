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
	static private String sienaMaster = "senp://canal.psl.cs.columbia.edu:31331";

	public GateKeeper(Habitat h) {
		masterHabitat = h;
		summits_list = new Hashtable();
		// connect to siena
		// Alpa : will read senp host from properties file
		HierarchicalDispatcher hd = new HierarchicalDispatcher();
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
		if(av == "currentSummit");
			processSummitEvent(n);
	}
  public void notify(Notification[] e) {}

	public void broadcast_req(siena.Notification n) {
		n.putAttribute("source", masterHabitat);
		try {
			n.publish();
		} catch(siena.InvalidHandleException se) {
			se.printStackTrace();
		}
	}

	private void create_treaty(String src, String dest, int ID, java.lang.Object ip_param, java.lang.Object ret_param) {
		Treaty curr_treaty = new Treaty(src, dest, ID);
		if (ip_param instanceof Vector) {
			curr_treaty.add_ip_list(ip_param);
		}
		else 
			curr_treaty.add_ip_param(ip_param);
		summits_list.put(ID,curr_treaty);
	}
	private boolean valid_ip(int ID, Object param) {
		Treaty curr_treaty = (Treaty) summits_list.get(ID);
    if (param instanceof Vector) {
      return (curr_treaty.valid_ip_list(param));
    }
    else 
      return (curr_treaty.valid_ip_param(param));
  }
	public void forwardMessage(int ID, siena.Notification n) {
		// service will use this to talk to remote services in a current session i.e. after security clearance
		// check if param are valid o/p params
		// and then publish 
	}

	public void processServiceRequest(Notification n) {
		// do some simple security clearance
		// assign an ID number
		// create a new treaty and add it to the list of sumits
		// if security cleared create a proxy of the service object
	}
	public void processSummitEvent(Notification n) {
		// get the ID 
		// gk contacts the summitlist to check if there if it should process it.
		// check if the i/p param are valid
		// contact the service and call forward mesg with results.
	}

}
