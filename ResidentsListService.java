package psl.habitats;

import java.util.Vector;
import java.util.Hashtable;

public class ResidentsListService {
  String myDescription = "TexasResidentInfo";
  public void initialize(){}
  public String getName(){
    return myDescription;
  }
	public void startDisplay(){}
  public Hashtable performService(String _senderService, Hashtable ipList) {
    Hashtable result = new Hashtable();
    if (ipList.containsKey(key1) && ipList.containsKey(key2)) {
      result.put(key5, lookup(key5, masterHabitat.getName()));
      result.put(key6, lookup(key6, masterHabitat.getName()));                                                                           
      result.put(key7, lookup(key7, masterHabitat.getName()));                                                                           
      result.put(key8, lookup(key8, masterHabitat.getName()));                                                                           
      result.put(key9, lookup(key9, masterHabitat.getName()));                                                                           
    } else 
      return null;
    return result;
  }

  private static final String key1 = "First_Name";
  private static final String key2 = "Last_Name";
  private static final String key5 = "Address";
  private static final String key6 = "Telephone";
  private static final String key7 = "SSN";
  private static final String key8 = "Image";
  private static final String key9 = "Age";

  private String lookup(String key, String forHab) {
    if(forHab == HABITAT_NAME) {
      if (key == key5)
        return ("608 CEPSR");
      else if (key == key6)
        return "7184";
      else if (key == key7)
        return "987-65-4321";
      else if (key == key8)
        return "http://www.web.com/picture.jpg";
      else if (key == key9)
        return "17";
      else
        return null;
    } else 
      return null;
  }
  }
}
