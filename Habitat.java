import java.io.*;
import java.util.*;


public class Habitat {
    String roleName;
    Hashtable serviceObjects;
    
    public Habitat(String name) {
	roleName = name;
	serviceObjects = new Hashtable();
	FileIO fileRW = new FileIO();
	Hashtable serviceList;
	GateKeeper gk;
	FrontEnd fe;
	
	// register with HRC
	fileRW.writeFile(".HRCList", roleName);
	
	// read the services 
	serviceList = fileRW.readFile(".serviceList", ","); 
	
	// initialize the services
	for (Enumeration e = serviceList.elements() ; e.hasMoreElements() ;) {
	    try {
		Class t = Class.forName((String)e.nextElement());
		ServiceInterface s = (ServiceInterface)t.newInstance();
		serviceObjects.put(s.getName(),s);
		s.initialize();
		System.out.println("Started sevice " + s.getName());
		
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	
	gk = new GateKeeper();
	fe = new FrontEnd();
    }
    
  public static void main(String arg[]) {
	  Habitat myFirstHabitat = new Habitat("AlpaChristy");
		System.out.println("Done");
	}
}

	
    
	   
