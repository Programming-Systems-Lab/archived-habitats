package psl.habitats;

import java.util.Vector;
import java.util.Hashtable;

public class ResidentsListService {
  String myDescription = "TexasResidentList";
  public void initialize(){}
  public String getName(){
    return myDescription;
  }
	public void startDisplay(){}
	public Hashtable performService(Hashtable ipList){
    Hashtable opParams = new Hashtable();
    if(ipList.containsKey("First_Name") && ipList.containsKey("Last_Name")){
      opParams.put("First_Name",lookup("First_Name", masterHabitat.getName()));
      opParams.put("Last_Name",lookup("Last_Name", masterHabitat.getName()));
      opParams.put("Address", lookup("Address", masterHabitat.getName()));
      opParams.put("SSN",lookup("SSN", masterHabitat.getName()));                                                                           
      opParams.put("Age",lookup("Age", masterHabitat.getName()));                                                                           
      opParams.put("Image",lookup("Age", masterHabitat.getName()));                                                                           
      } else 
      return null;
    return opParams;
    }
      
  private String lookup(String key, String forHab) {
    
  }
}
