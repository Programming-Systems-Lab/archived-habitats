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

	masterHabitat.svclog(myDescription, myDescription + ": performService for " + _senderService);

	Hashtable result = new Hashtable();
	if(ipList.containsKey(key1) || ipList.containsKey(key2)){
	    result.put(key1, lookup("" + ipList.get(key1), key1, masterHabitat.getName()));
	    result.put(key2, lookup("" + ipList.get(key1), key2, masterHabitat.getName()));
	    result.put(key3, lookup("" + ipList.get(key1), key3, masterHabitat.getName()));
	    result.put(key4, lookup("" + ipList.get(key1), key4, masterHabitat.getName()));
	} else 
	    result = null;

	masterHabitat.svclog(myDescription, myDescription + ": returning requested Data ");

	return result;
    }
    
    private static final String HABITAT_NAME = "AmTrak";
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
	gailHash.put(key3, "Texas");
	gailHash.put(key4, "Friday morning");
	
	johnHash.put(key1, "John");
	johnHash.put(key2, "Salasin");
	johnHash.put(key3, "Texas");
	johnHash.put(key4, "Yesterday");
    }
}
