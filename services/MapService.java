package psl.habitats.services;

import psl.habitats.*;

import javax.swing.*;
import java.util.Hashtable;

public class MapService implements ServiceInterface {    private static String serviceDescription = "MapService";
  public MapService() {	}
  public void initialize(Habitat _h) { }
  public String getDescription() {
    return serviceDescription;
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
    _stringBuffer.append("<tr><td>Purpose</td><td>Maps Database</td></tr>");
    _stringBuffer.append("<tr><td>Availablity</td><td>Not available</td></tr>");
    _stringBuffer.append("<tr><td>Monitor</td><td>Probably</td></tr>");
    _stringBuffer.append("<tr><td>Monitor-Mode</td><td>NA</td></tr>");
    _stringBuffer.append("</table>");
    _stringBuffer.append("</body></html>");
  return _stringBuffer + "";
  }

  public Hashtable performService(String _senderService, Hashtable ipList) {
    return null;
  }
}
