package psl.habitats.services;
import psl.habitats.*;

import java.awt.*;
import java.awt.event.*;import javax.swing.*;

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
  private static final String key8 = "Age";
  private static final String key9 = "Image";

  public void initialize(Habitat _h){
    masterHabitat = _h;
  }
  public String getDescription() {
    return myDescription;
  }
  public String getDetailDescription(){
    StringBuffer _stringBuffer = new StringBuffer();
    _stringBuffer.setLength(0);
    _stringBuffer.append("<html><body>");
    _stringBuffer.append("<h1>" + getDescription() + "</h1>");
    _stringBuffer.append("<table border=4>");
    _stringBuffer.append("<tr><td>Purpose</td><td>Receive and Update Events </td></tr>");
    _stringBuffer.append("<tr><td>Availablity</td><td>Local</td></tr>");
    _stringBuffer.append("<tr><td>Monitor</td><td>True</td></tr>");
    _stringBuffer.append("<tr><td>Monitor-Mode</td><td>Text</td></tr>");
    _stringBuffer.append("</table>");
    _stringBuffer.append("</body></html>");
    return _stringBuffer + "";
  }

  private String phaseOneData;
	public JPanel startDisplay(){
    // start the GUI here
    final JPanel panel = new JPanel(new GridLayout(20, 1));    
    final JLabel label = new JLabel("Enter name: ");
    final JTextField text = new JTextField(20);    JButton button = new JButton("Action!");    panel.add(new JLabel("FOR USE BY AUTHORISED PERSONNEL!"));
    {JPanel p = new JPanel(new GridLayout(1, 2));p.add(new JLabel("Type of Event: "));p.add(new JTextField(10));panel.add(p);}    {JPanel p = new JPanel(new GridLayout(1, 2));p.add(new JLabel("Available Data: "));p.add(new JTextField(10));panel.add(p);}
    {JPanel p = new JPanel(new GridLayout(1, 2));p.add(new JLabel("Description: "));p.add(new JTextField(10));panel.add(p);}
    {JPanel p = new JPanel(new GridLayout(1, 2));p.add(label);p.add(text);panel.add(p);}
    panel.add(button);

    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {        phaseOneData = text.getText();        phaseOne();
      }    });    return panel;
  }
  
  private static final String PHASE_ONE_PEER = "NYC_Passengers_Query";
  private static final String PHASE_TWO_PEER = "TexasResidentInfo";
  private static final String PHASE_THREE_PEER = "AlertService";
  
  int phaseOneProgress = 0, phaseTwoProgress = 0, phaseThreeProgress = 0;

  private String hashtableListing(Hashtable h) {
    StringBuffer sb = new StringBuffer("");
    for (Enumeration e = h.keys(); e.hasMoreElements(); ) {
      String key = "" + e.nextElement();
      sb.append("key: " + key + ", val: " + h.get(key));
    }
    return "" + sb;
  }
  public Hashtable performService(String _senderService, Hashtable ipList) {
    Hashtable result = new Hashtable();
    if(_senderService.equals(PHASE_ONE_PEER)) {
      masterHabitat.log(myDescription + " :: PHASE_ONE_PEER " + PHASE_ONE_PEER);
      switch (phaseOneProgress) {
      case 0:
        result.put(key1, phaseOneData);
        phaseOneProgress++;
        break;
        
      case 1:
        for (Enumeration e=ipList.keys(); e.hasMoreElements(); ) {
          String key = e.nextElement().toString();
          informationCatalogue.put(key, ipList.get(key));
        }
        masterHabitat.log(myDescription + ":: Terminating Session with " + PHASE_TWO_PEER);
        result.put("Terminate_Session", "SESSION_ENDED");
        phaseOneProgress = 0;
        
        // initiate phase-II
        phaseTwo();
        break;

      }
      
    } else if(_senderService.equals(PHASE_TWO_PEER)) {
      masterHabitat.log(myDescription + ":: PHASE_TWO_PEER " + PHASE_TWO_PEER);
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
				masterHabitat.log(myDescription + " Terminating Session with " + PHASE_TWO_PEER);
        phaseTwoProgress = 0;

        // initiate phase-III
        phaseThree();
        break;

      }
    } else if(_senderService.equals(PHASE_THREE_PEER)) {
      masterHabitat.log(myDescription + ":: PHASE_THREE_PEER" + PHASE_THREE_PEER);
      switch (phaseThreeProgress) {
      case 0:
        for (Enumeration e=informationCatalogue.keys(); e.hasMoreElements(); ) {
          String key = e.nextElement().toString();
          if (key.equals(key3) || key.equals(key4)) continue;
          result.put(key, informationCatalogue.get(key));
          // masterHabitat.log("$$$$$$$$Sending this " + key);
        }
        phaseThreeProgress++;
        break;
        
      case 1:
        phaseThreeProgress = 0;
				masterHabitat.log(myDescription + " Terminating Session with " + PHASE_THREE_PEER);
        result.put("Terminate_Session", "SESSION_ENDED");
        break;
      }
    }

    masterHabitat.log(myDescription + ": performService, returning: " + hashtableListing(result));
    return result;
  } 
  private void phaseOne() {
		masterHabitat.log(myDescription + " Received some new Info - processing it .. ");
		masterHabitat.log(myDescription + " Number of Steps Suggested -> 3 "); 
    masterHabitat.log(myDescription + " Entering Phase One ");
		masterHabitat.log(myDescription + " Need to gather information on 'NYC Passengers'" ); 
		masterHabitat.log(myDescription + " Contact Habitat -> 'NYCTransitHabitat'");
		masterHabitat.log(myDescription + " Contact Service -> 'NYC_Passengers_Query'");
		
    // invoked by user entering chor's name
    // some intelligent workflow suggested that service to be contacted
    String useService = "NYC_Passengers_Query";
    if(!Habitat.localService(useService)) {
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
      
      masterHabitat.gk.broadcast_req(myDescription, useService, ipList);
			masterHabitat.log(myDescription + " Requesting Gatekeeper to contact Remote Service");
    }
    masterHabitat.log(myDescription + ": phaseOne, returned");
  }
  
  private void phaseTwo() {
    masterHabitat.log(myDescription + ": phaseTwo");
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
      ipList.put("retvals1", key5);
      ipList.put("retvals2", key6);
      ipList.put("retvals3", key7);
      ipList.put("retvals4", key8);
      ipList.put("retvals5", key9);
      
      masterHabitat.gk.broadcast_req(myDescription, useService, ipList);
    }
    masterHabitat.log(myDescription + ": phaseTwo, returned");
  }
  
  private void phaseThree() {  
    masterHabitat.log(myDescription + ": phaseThree");
    String useService = "AlertService";
    if (!Habitat.localService(useService)) {
      Hashtable ipList = new Hashtable();
      ipList.put("habitatCategory","NYCTransitHabitat");
      ipList.put("EventType","ServiceRequest");
      ipList.put("Request4Service", useService);
      ipList.put("totalParams","7");
      ipList.put("params1", key1);
      ipList.put("params2", key2);
      ipList.put("params3", key5);
      ipList.put("params4", key6);
      ipList.put("params5", key7);
      ipList.put("params6", key8);
      ipList.put("params7", key9);
      ipList.put("totalRetvals", "0");
      
      masterHabitat.gk.broadcast_req(myDescription, useService, ipList);
    }
    masterHabitat.log(myDescription + ": phaseThree, returned");
  }

}
