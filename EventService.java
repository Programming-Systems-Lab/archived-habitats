package psl.habitats;

import java.util.Vector;
import java.util.Hashtable;
import siena.*;

public class EventService implements ServiceInterface {
  String myDescription = "Event_Manager";
  Siena siena = null;
  Habitat masterHabitat = null;
  public void initialize(Siena _s, Habitat _h){
    siena = _s;
    masterHabitat = _h;
  }
  public String getDescription(){
    return myDescription;
  }
	public void startDisplay(){
    // start the GUI here    
  }
	public Hashtable performService(Hashtable ipList){
  }
  
  private Notification getNotificationONE(){
    // some intelligent workflow suggested that service to be contacted
    String useService = "NYC_Passengers_Query";
    if(!Habitat.localService()) {
      Notification step1 = new Notification();
      step1.putAttribute("habitatCategory","NYCTransitHabitat");
      step1.putAttribute("EventType","ServiceRequest");
      step1.putAttribute("Request4Service", useService);
      step1.putAttribute("totaIPpparams","1");
      step1.putAttribute("param1", "First_Name");
      step1.putAttribute("totaOPpparams", "2");
      step1.putAttribute("param1", "Full_Name");
      step1.putAttribute("param2", "Boarding City"); // get the correct TERM
    
      Filter f = new Filter();   
      f.addConstraint("wakeUpService", myDescription);
      f.addConstraint("availableService", useService);
      FilterThread f_sleep = new FilterThread(siena,f, new WakeUpOnService(siena,useService));
    }
    return (step1);
  }
  
  class WakeupService implements Notifiable {
    Siena si = null;
    String sleepingOnService;
    public WakeupService(Siena _s,String _serv) {
      si = _s;
      sleepingOnService = _serv;
		}
  	public void notify(Notification n[]) {}
    public void notify(Notification gk_notif) {
      // got access to remote service so use it'
      Notification toBSent = new Notification();
      int ID;
      AttributeValue av = gk_notif.getAttribute("ID");
      toBSent.putAttribute("First_Name","Alpa");
    
      Filter f = new Filter();
      f.addConstraint("localGK", masterHabitat.getName());
      f.addConstraint("ID", ID);
    }
  }    
  private Notification getNotificationTWO(){
  }
  private Notification getNotificationTHREE(){
  }

}
