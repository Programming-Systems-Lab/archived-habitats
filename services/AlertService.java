package psl.habitats.services;
import psl.habitats.*;

import javax.swing.*;

import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public class AlertService implements ServiceInterface {
  private static final String myDescription = "AlertService";
  Habitat masterHabitat = null;
  public void initialize(Habitat _h) {
    masterHabitat = _h;
  }
  public String getDescription(){
    return myDescription;
  }
  public String getDetailDescription(){
    StringBuffer _stringBuffer = new StringBuffer();
    _stringBuffer.setLength(0);
    _stringBuffer.append("<html><body>");
    _stringBuffer.append("<h1>" + getDescription() + "</h1>");
    _stringBuffer.append("<table border=4>");
    _stringBuffer.append("<tr><td>Purpose</td><td>Hit Panic Button</td></tr>");
    _stringBuffer.append("<tr><td>Availablity</td><td>Selected</td></tr>");
    _stringBuffer.append("<tr><td>Monitor</td><td>True</td></tr>");
    _stringBuffer.append("<tr><td>Monitor-Mode</td><td>Text</td></tr>");
    _stringBuffer.append("</table>");
    _stringBuffer.append("</body></html>");
    return _stringBuffer + "";
  }
  private JPanel panel;
  public JPanel startDisplay() {
    return (panel = new JPanel());
  }

  private static final String key1 = "First_Name";
  private static final String key2 = "Last_Name";
  private static final String key3 = "Boarding_City";
  private static final String key4 = "Boarding_Date";
  private static final String key5 = "Address";
  private static final String key6 = "Telephone";
  private static final String key7 = "SSN";
  private static final String key8 = "Age";
  private static final String key9 = "Image";

  public Hashtable performService(String _senderService, Hashtable ipList) {
    StringBuffer _stringBuffer = new StringBuffer();
    _stringBuffer.setLength(0);
    _stringBuffer.append("<html><body>\n");
    _stringBuffer.append("<h1>" + getDescription() + "</h1>\n");
    _stringBuffer.append("<table border=4>\n");
    for (Enumeration e=ipList.keys(); e.hasMoreElements(); ) {
      String key = e.nextElement().toString();
      if (key.equals(key9)) continue; 
      _stringBuffer.append("<tr><td>"+key+"</td><td>"+ipList.get(key)+"</td></tr>\n");
    }
    _stringBuffer.append("<tr><td colspan=2><img src="+ipList.get(key9)+"></td></tr>\n");
    _stringBuffer.append("</table>\n");
    _stringBuffer.append("</body></html>\n");

    JLabel label = new JLabel("" + _stringBuffer);
    JOptionPane.showMessageDialog(panel, label, "Alert Service", JOptionPane.WARNING_MESSAGE);
    masterHabitat.log("" + _stringBuffer);
    System.out.println("JUST LOGGED: " + _stringBuffer);

    return null;
  }
}
