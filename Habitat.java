package psl.habitats;

import java.io.*;
import java.util.*;


public class Habitat {
  String roleName, category;
  Hashtable serviceObjects;
	public final GateKeeper gk;
  public Habitat(String name, String c, String file, String sienaMaster) {
		roleName = name;
		category = c;
		serviceObjects = new Hashtable();
		FileIO fileRW = new FileIO();
		Hashtable serviceList;
		FrontEnd fe;
		Console ad = new Console();

	  ad.cf_show("Admin Frame");
		ad.cf_show("Admin Console");

		// read the services 
    if (file == null) file = ".serviceList";
		serviceList = fileRW.readFile(file, ",");
    
    // set the sienaMaster
    if (sienaMaster == null) 
      sienaMaster = "senp://canal.psl.cs.columbia.edu:31331";
	
		gk = new GateKeeper(this, sienaMaster);
		fe = new FrontEnd();

    // initialize the services
		for (Enumeration e = serviceList.elements() ; e.hasMoreElements() ;) {
	    try {
				Class t = Class.forName((String)e.nextElement());
				ServiceInterface s = (ServiceInterface)t.newInstance();
				serviceObjects.put(s.getDescription(), s);
				s.initialize(this);
				ad.cf_add("Started sevice " + s.getDescription()+ "\n");
	    } catch (Exception ex) {
				ex.printStackTrace();
	    }
		}
	
  }
	public String getName() {
		return roleName;
	}
	public String getCategory() {
		return category;
	}
	public ServiceInterface getService(String _svdes) {
		return((ServiceInterface)serviceObjects.get(_svdes));
	}
  static public boolean localService(String servDes) {
    return false;
  }
  
  private static void usage() {
    System.out.println("java psl.habitats.Habitat <habName> <habCategory> [-sl <file>] [-sm <uri>]");
    System.out.println(" <habName>     : habitat name");
    System.out.println(" <habCategory> : habitat category");
    System.out.println(" [-sl <file>]  : initial serviceList file [default: .serviceList]");
    System.out.println(" [-sm <uri>]   : siena master             [default: senp://canal:31331/");
    System.exit(0);
  }
  public static void main(String args[]) {
    if (args.length < 2) usage();
    
    String habName = args[0];
    String habCategory = args[1];
    String file = null, sm = null;
    
    int i = 2;
    while (i<args.length) {
      if (args[i].equals("-sl")) file = args[++i];
      else if (args[i].equals("-sm")) sm = args[++i];
      i++;
    }

	  new Habitat(habName, habCategory, file, sm);
	}
}

	
    
	   
