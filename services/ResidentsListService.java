package psl.habitats.services;
import psl.habitats.*;

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
	public void startDisplay(){}

  private static final String key1 = "First_Name";
  private static final String key2 = "Last_Name";
  private static final String key5 = "Address";
  private static final String key6 = "Telephone";
  private static final String key7 = "SSN";
  private static final String key8 = "Image";
  private static final String key9 = "Age";

  public Hashtable performService(String _senderService, Hashtable ipList) {
    System.out.println(myDescription + ": performService");
    Hashtable result = new Hashtable();
    if (ipList.containsKey(key1) && ipList.containsKey(key2)) {
      result.put(key5, lookup(key5, masterHabitat.getName()));
      result.put(key6, lookup(key6, masterHabitat.getName()));                                                                           
      result.put(key7, lookup(key7, masterHabitat.getName()));                                                                           
      result.put(key8, lookup(key8, masterHabitat.getName()));                                                                           
      result.put(key9, lookup(key9, masterHabitat.getName()));                                                                           
    } else 
      result = null;
    System.out.println(myDescription + ": performService, returning: " + result);
    return result;
  }

  private static final String HABITAT_NAME = "Texas";
  private String lookup(String key, String forHab) {
    if(forHab.equals(HABITAT_NAME)) {
      if (key.equals(key5))
        return ("608 CEPSR");
      else if (key.equals(key6))
        return "7184";
      else if (key.equals(key7))
        return "987-65-4321";
      else if (key.equals(key8))
        return "http://www.web.com/picture.jpg";
      else if (key.equals(key9))
        return "17";
      else
        return null;
    } else 
      return null;
  }
}
