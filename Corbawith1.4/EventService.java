package psl.habitats.services;
import psl.habitats.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public class EventService implements ServiceInterface {
  String myDescription = "Event_Manager";
  Habitat masterHabitat = null;
  
  private final Hashtable informationCatalogue = new Hashtable();
  
  private static final String key1 = "First_Name";
  private static final String key2 = "Last_Name";
  private static final String key3 = "Boarding_City";
  private static final String key4 = "Boarding_Date";
  private static final String key5 = "Address";
  private static final String key6 = "Telephone";
  private static final String key7 = "SSN";
  private static final String key8 = "Image";
  private static final String key9 = "Age";

  public void initialize(Habitat _h){
    masterHabitat = _h;
  }
  public String getDescription(){
    return myDescription;
  }
	public void startDisplay(){
    // start the GUI here    
  }
  
  private static final String PHASE_ONE_PEER = "NYC_Passengers_Query";
  private static final String PHASE_TWO_PEER = "ResidentsListService";
  private static final String PHASE_THREE_PEER = "AlertServcie";
  
  int phaseOneProgress = 0, phaseTwoProgress = 0, phaseThreeProgress = 0;

  public Hashtable performService(String _senderService, Hashtable ipList) {
    Hashtable result = new Hashtable();
    if(_senderService.equals(PHASE_ONE_PEER)) {
      switch (phaseOneProgress) {
      case 0:
        result.put(key1, "Alpa");
        phaseOneProgress++;
        break;
        
      case 1:
        for (Enumeration e=ipList.keys(); e.hasMoreElements(); ) {
          String key = e.nextElement().toString();
          informationCatalogue.put(key, ipList.get(key));
        }
        result.put("Terminate_Session", "SESSION_ENDED");
        phaseOneProgress = 0;
        
        // initiate phase-II
        phaseTwo();
        break;

      }
      
    } else if(_senderService.equals(PHASE_TWO_PEER)) {
      switch (phaseTwoProgress) {
      case 0:
        result.put(key1, informationCatalogue.get(key1));
        result.put(key2, informationCatalogue.get(key2));
        phaseTwoProgress++;
        break;
        
      case 1:
        for (Enumeration e=ipList.keys(); e.hasMoreElements(); ) {
          String key = e.nextElement().toString();
          informationCatalogue.put(key, ipList.get(key));
        }
        result.put("Terminate_Session", "SESSION_ENDED");
        phaseTwoProgress = 0;

        // initiate phase-III
        phaseThree();
        break;

      }
    } else if(_senderService.equals(PHASE_THREE_PEER)) {
      switch (phaseThreeProgress) {
      case 0:
        for (Enumeration e=informationCatalogue.keys(); e.hasMoreElements(); ) {
          String key = e.nextElement().toString();
          if (key.equals(key3) || key.equals(key4)) continue;
          result.put(key, informationCatalogue.get(key));
        }
        phaseThreeProgress++;
        break;
        
      case 1:
        phaseThreeProgress = 0;
        result.put("Terminate_Session", "SESSION_ENDED");
        break;
        
      }
    }
    
    return result;
  } 
  private void phaseOne(String name) {
    // invoked by user entering chor's name
    // some intelligent workflow suggested that service to be contacted
    String useService = "NYC_Passengers_Query";
    if (!Habitat.localService(useService)) {
      Hashtable ipList = new Hashtable();
      ipList.put("habitatCategory","NYCTransitHabitat");
      ipList.put("EventType","ServiceRequest");
      ipList.put("Request4Service", useService);
      ipList.put("totalParams","1");
      ipList.put("params1", key1);
      ipList.put("totalRetvals", "4");
      ipList.put("retvals1", key1);
      ipList.put("retvals2", key2);
      ipList.put("retvals3", key3);
      ipList.put("retvals4", key4);
      
      masterHabitat.gk.broadcast_req("NYCTransitHabitat", ipList);
    }
  }
  
  private void phaseTwo() {
    String useService = "TexasResidentInfo";
    if (!Habitat.localService(useService)) {
      Hashtable ipList = new Hashtable();
      ipList.put("habitatCategory","TexasStateHabitat");
      ipList.put("EventType","ServiceRequest");
      ipList.put("Request4Service", useService);
      ipList.put("totalParams","2");
      ipList.put("params1", key1);
      ipList.put("params2", key2);
      ipList.put("totalRetvals", "5");
      ipList.put("retvals3", key5);
      ipList.put("retvals4", key6);
      ipList.put("retvals5", key7);
      ipList.put("retvals6", key8);
      ipList.put("retvals7", key9);
      
      masterHabitat.gk.broadcast_req("TexasStateHabitat", ipList);
    }
  }
  
  private void phaseThree() {  
    String useService = "AlertService";
    if (!Habitat.localService(useService)) {
      Hashtable ipList = new Hashtable();
      ipList.put("habitatCategory","NYCTransitHabitat");
      ipList.put("EventType","ServiceRequest");
      ipList.put("Request4Service", useService);
      ipList.put("totalParams","2");
      ipList.put("params1", key1);
      ipList.put("params2", key2);
      ipList.put("params3", key5);
      ipList.put("params4", key6);
      ipList.put("params5", key7);
      ipList.put("params6", key8);
      ipList.put("params7", key9);
      ipList.put("totalRetvals", "0");
      
      masterHabitat.gk.broadcast_req("NYCTransitHabitat", ipList);
    }
  }

}
