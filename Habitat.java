package psl.habitats;

import java.io.*;
import java.util.*;


public class Habitat {
  String roleName, category;
  Hashtable serviceObjects;
    
  public Habitat(String name, String c) {
		roleName = name;
		category = c;
		serviceObjects = new Hashtable();
		FileIO fileRW = new FileIO();
		Hashtable serviceList;
		GateKeeper gk;
		FrontEnd fe;
		Console ad = new Console();

		// register with HRC
		fileRW.writeFile(".HRCList", roleName);
	
	  ad.cf_show("Admin Frame");
		ad.cf_show("Admin Console");

		// read the services 
		serviceList = fileRW.readFile(".serviceList", ","); 
	
		// initialize the services
		for (Enumeration e = serviceList.elements() ; e.hasMoreElements() ;) {
	    try {
				Class t = Class.forName((String)e.nextElement());
				ServiceInterface s = (ServiceInterface)t.newInstance();
				serviceObjects.put(s.getName(),s);
				s.initialize();
				ad.cf_add("Started sevice " + s.getName()+ "\n");
	    } catch (Exception ex) {
				ex.printStackTrace();
	    }
		}
	
		gk = new GateKeeper();
		fe = new FrontEnd();
  }
		public String getName() {
			return roleName;
		}
		public String getCategory() {
			return category;
		}

  public static void main(String arg[]) {
	  Habitat myFirstHabitat = new Habitat("AlpaChristy", "general");
		System.out.println("Done");
	}
}

	
    
	   
