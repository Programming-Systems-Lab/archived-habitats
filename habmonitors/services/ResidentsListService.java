package psl.habitats.services;
import psl.habitats.*;

import javax.swing.*;

import java.util.Vector;
import java.util.Hashtable;

public class ResidentsListService implements ServiceInterface {
  String myDescription = "TexasResidentInfo";
  Habitat masterHabitat = null;

  public void initialize(Habitat _h){
    masterHabitat = _h;
  }
  public String getDescription() {
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
    _stringBuffer.append("<tr><td>Purpose</td><td>Texas Residents Credentials </td></tr>");
    _stringBuffer.append("<tr><td>Availablity</td><td>Local/Remote</td></tr>");
    _stringBuffer.append("<tr><td>Monitor</td><td>True</td></tr>");
    _stringBuffer.append("<tr><td>Monitor-Mode</td><td>Text</td></tr>");
    _stringBuffer.append("</table>");
    _stringBuffer.append("</body></html>");
    return _stringBuffer + "";
  }
  
  private static final String key1 = "First_Name";
  private static final String key2 = "Last_Name";
  private static final String key5 = "Address";
  private static final String key6 = "Telephone";
  private static final String key7 = "SSN";
  private static final String key8 = "Image";
  private static final String key9 = "Age";

  public Hashtable performService(String _senderService, Hashtable ipList) {
    masterHabitat.log(myDescription + ": performService for " + _senderService);
    Hashtable result = new Hashtable();
    if (ipList.containsKey(key1) && ipList.containsKey(key2)) {
      result.put(key5, lookup("" + ipList.get(key1), key5, masterHabitat.getName()));
      result.put(key6, lookup("" + ipList.get(key1), key6, masterHabitat.getName()));                                                                           
      result.put(key7, lookup("" + ipList.get(key1), key7, masterHabitat.getName()));                                                                           
      result.put(key8, lookup("" + ipList.get(key1), key8, masterHabitat.getName()));                                                                           
      result.put(key9, lookup("" + ipList.get(key1), key9, masterHabitat.getName()));                                                                           
    } else 
      result = null;
    masterHabitat.log(myDescription + ": returning requested data ");
    return result;
  }

  private static final String HABITAT_NAME = "Texas";
  private String lookup(String key1, String key, String forHab) {
    if (forHab.equals(HABITAT_NAME)) {
      if (key1.equalsIgnoreCase("john")) return "" + johnHash.get(key);
      if (key1.equalsIgnoreCase("gail")) return "" + gailHash.get(key);
    }
    return null;
  }
  private static final Hashtable gailHash = new Hashtable();
  private static final Hashtable johnHash = new Hashtable();
  static {
    gailHash.put(key1, "Gail");
    gailHash.put(key2, "Kaiser");
    gailHash.put(key5, "New Jersey");
    gailHash.put(key6, "(212) 939-7100");
    gailHash.put(key7, "123-45-6789");
    gailHash.put(key8, "http://www.psl.cs.columbia.edu/personnel/gail.jpg");
    gailHash.put(key9, "18");

    johnHash.put(key1, "John");
    johnHash.put(key2, "Salasin");
    johnHash.put(key5, "DARPA");
    johnHash.put(key6, "(800) SAL-ASIN");
    johnHash.put(key7, "987-65-4321");
    johnHash.put(key8, "http://www.cs.columbia.edu/~ajs248/intropic.jpg");
    johnHash.put(key9, "18");
  }
}
