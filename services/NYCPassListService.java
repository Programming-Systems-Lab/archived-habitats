package psl.habitats.services;
import psl.habitats.*;

import javax.swing.*;

import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;

public class NYCPassListService implements ServiceInterface {
  private static final String myDescription = "NYC_Passengers_Query";
  Habitat masterHabitat = null;
  public void initialize(Habitat _h) {
    masterHabitat = _h;
  }
  public String getDescription(){
    return myDescription;
  }
  public JPanel startDisplay() {
    return new JPanel();
  }

  public String getDetailDescription(){
    StringBuffer _stringBuffer = new StringBuffer();
    _stringBuffer.setLength(0);
    _stringBuffer.append("<html><body>");
    _stringBuffer.append("<h1>" + getDescription() + "</h1>");
    _stringBuffer.append("<table border=4>");
    _stringBuffer.append("<tr><td>Purpose     </td><td>AmTrak Passenger List</td></tr>");
    _stringBuffer.append("<tr><td>Availablity </td><td>Local/Remote         </td></tr>");
    _stringBuffer.append("<tr><td>Monitor     </td><td>True                 </td></tr>");
    _stringBuffer.append("<tr><td>Monitor-Mode</td><td>Text                 </td></tr>");
    _stringBuffer.append("</table>");
    _stringBuffer.append("</body></html>");
    return _stringBuffer + "";
  }

  private static final String key1 = "First_Name";
  private static final String key2 = "Last_Name";
  private static final String key3 = "Boarding_City";
  private static final String key4 = "Boarding_Date";

  public Hashtable performService(String _senderService, Hashtable ipList) {
    masterHabitat.log(myDescription + ": performService for " + _senderService);
    Hashtable result = new Hashtable();
    if(ipList.containsKey(key1) || ipList.containsKey(key2)){
      result.put(key1, lookup(key1, masterHabitat.getName()));
      result.put(key2, lookup(key2, masterHabitat.getName()));
      result.put(key3, lookup(key3, masterHabitat.getName()));
      result.put(key4, lookup(key4, masterHabitat.getName()));
    } else 
      result = null;
    masterHabitat.log(myDescription + ": returning requested Data ");
    return result;
  }
  
  private static final String HABITAT_NAME = "AmTrak";
  private String lookup(String key, String forHab) {
    if (forHab.equals(HABITAT_NAME)) {
      if (key.equals(key1))
        return ("John");
      else if (key.equals(key2))
        return ("Salasin");
      else if(key.equals(key3))
        return ("Texas");
      else if (key.equals(key4))
        return new Date().toString();
      else
        return null;
    } else 
      return null;
  }
}
