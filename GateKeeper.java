package psl.habitats;
import java.utils.Vector;
import java.utils.Hashtable;
import siena.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

// Alpa : need to replace the siena Notification with a generalised service descritpion

public class GateKeeper implements Notifiable {
	static private Habitat masterHabitat;
	static private Hashtable summits_list; 
	static private String sienaMaster = "senp://canal.psl.cs.columbia.edu:31331";

	public GateKeeper(Habitat h) {
		masterHabitat = h;
		summits_list = new Hashtable();
		// connect to siena
		// Alpa : will read senp host from properties file
		HierarchicalDispatcher h = new HierarchicalDispatcher();
    try {   
      h.setMaster(sienaMaster);
      System.out.println(me + ": master is " + sienaMaster);
    } catch (siena.InvalidSenderException ihe) {
      ihe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    Filter f1 = new Filter();
    f1.addConstraint("habitatCategory", masterHabitat.getCategory());
    f1.addConstraint("habitatName", masterHabitat.getName());

    FilterThread t = new FilterThread(this);
    t.start();
  }
  public void notify(Notification n) {
    AttributeValue av = n.getAttribute("EventType");
    if (av != null) {
			if(av == "ServiceRequest");
				processServiceRequest(n);
  try {
    s.publish(n);
  } catch (siena.SienaException se) {
    se.printStackTrace();
  }
  System.out.println(me + " published " + n);
      }

    } else {
      System.out.println(me + " Error: Oracle lookup without key");
    }
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

	private void create_treaty(String src, String dest, int ID, Object ip_param, Object ret_param) {
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
    if (ip_param instanceof Vector) {
      return (curr_treaty.valid_ip_list(ip_param));
    }
    else 
      return (curr_treaty.valid_ip_param(ip_param));
  }
	public void forwardMessage(int ID, siena.Notification n)
}
