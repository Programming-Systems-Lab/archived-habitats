package psl.habitats;

import java.util.Vector;
import java.util.Hashtable;

public class NYCPassListService implements ServiceInterface {
  String myDescription = "NYCPassListService";
  Habitat masterHabitat = null;
  public void initialize(){}
  public String getDescription(){
    return NYCPassListService;
  }
  public void startDisplay(){}

  public Hashtable performService(Hashtable ipList){
    Hashtable opParams = new Hashtable();
    if(ipList.containsKey("First_Name") 
       && ipList.containsKey("Last_Name")
       && ipList.containsKey("Boarding_City")){ 
      opParams.put("First_Name",lookup("First_Name", masterHabitat.getName()));
      opParams.put("Last_Name",lookup("Last_Name", masterHabitat.getName()));
      opParams.put("Boarding_City", lookup("Boarding_City", masterHabitat.getName()));
    } else 
      return null;
    return opParams;
  }
  
  private String lookup(String key, String forHab) {
    if(forHab == "Amtrak_Habitat") {
      if (key == "First_Name")
        return ("Alpa");
      else if (key == "Last_Name")
        return ("Shah");
      else if(key == "Boarding_City")
        return ("Texas");
      else 
        return null;
    } else 
      return null;
  }
}
