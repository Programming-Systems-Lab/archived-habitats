package psl.habitats;

import java.io.*;
import java.util.*;

import psl.habitats.portal.HabitatPortal;
import psl.habitats.portal.HabitatMonitor;



public class Habitat {
    String roleName, category;
    Hashtable serviceObjects;
    HabitatPortal hp = null;
    HabitatMonitor hm = null;
    
    public final GateKeeper gk;
    public Habitat(String name, String c, String file, String sienaMaster) {
	roleName = name;
	category = c;
	serviceObjects = new Hashtable();
	FileIO fileRW = new FileIO();
	Hashtable serviceList;
	
	// read the services 
	if (file == null) file = ".serviceList";
	serviceList = fileRW.readFile(file, ",");
	
	// set the sienaMaster
	if (sienaMaster == null) 
	    sienaMaster = "senp://canal.psl.cs.columbia.edu:31331";
	
	gk = new GateKeeper(this, sienaMaster);
	hm = new HabitatMonitor(name);

	// initialize the services
	for (Enumeration e = serviceList.elements() ; e.hasMoreElements() ;) {
	    try {
		Class t = Class.forName((String) e.nextElement());
		ServiceInterface s = (ServiceInterface) t.newInstance();
		serviceObjects.put(s.getDescription(), s);
		s.initialize(this);
		System.out.println("Started sevice " + s.getDescription()+ "\n");

		// add a monitor for this service
		hm.addmonitor(s.getDescription());

		// log in habitat monitor
		log("Found service :" + s.getDescription());

		// log in service monitor
		svclog(s.getDescription(), "Started service :" + s.getDescription());

	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	
	hp = new HabitatPortal(name, serviceObjects.elements());	

	// display monitor frame relative to portal frame
	hm.relativedisplay(hp);
    }

    // send log messages to service monitors
    public void svclog(String servname, String message) {
	hm.writemonitor(message, servname);
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

    // send message to habitat monitor
    public void log(String msg) {
	hm.log(msg);
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

	
    
	   
