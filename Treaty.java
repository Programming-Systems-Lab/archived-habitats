package psl.habitats;
import java.util.Vector;
import java.util.Enumeration;

class Treaty {
	final int ID;
	final String clientHabitat;
	final String serverHabitat;  final String clientHabService;
	final String serverHabService;
  final int numParameters;
  final int numReturnValues;
  final Vector allowed_params;
	final Vector allowed_retvals;

	public Treaty(String serSvc, String cliSvc, int id, 
                String s, String c, Vector params, Vector retvals) {    ID = id;    clientHabitat = c;    serverHabitat = s;    clientHabService = cliSvc;
    serverHabService = serSvc;        allowed_params = new Vector(params);    allowed_retvals = new Vector(retvals);        numParameters = allowed_params.size();    numReturnValues = allowed_retvals.size();
	}

	//////////////////////////////////////////////////////
	// 				general getter functions
	//////////////////////////////////////////////////////
	String getServiceUsed() { return serverHabService; }
	int    getID()          { return ID;               }
	int    get_IP_size()    { return numParameters;    }
	  //////////////////////////////////////////////////////
	// 				functions for input params 
	//////////////////////////////////////////////////////

	public void add_ip_param(Object param) {
		allowed_params.add(param);
	}
	public void add_ip_list(Vector param_list) {
		allowed_params.addAll(param_list);
	}
	public boolean valid_ip_param(Object param) {
		return (allowed_params.contains(param));
	}
	public boolean valid_ip_list(Vector param_list) {
		return (allowed_params.containsAll(param_list));
	}
	public Vector getIPList() {
		return (allowed_params);
	}

	//////////////////////////////////////////////////////
	// 				functions for return types
	//////////////////////////////////////////////////////
	
	public void add_op_param(Object param) {
		allowed_retvals.add(param);
	}
	public void add_op_list(Vector param_list) {
		allowed_retvals.addAll(param_list);
	}
	public boolean valid_op_param(Object param) {
		return (allowed_retvals.contains(param));
	}
	public boolean valid_op_list(Vector param_list) {
		return (allowed_retvals.containsAll(param_list));
	}
  //////////////////////////////////////////////////////
	// 				functions for printing
	//////////////////////////////////////////////////////  public void printIPList() {
    System.out.println(" Printing IP List ==> ");
    for (Enumeration e = allowed_params.elements(); e.hasMoreElements(); )
      System.out.println(" .. " + e.nextElement());  }
  public void printOPList() {    System.out.println(" Printing OP List ==> ");
    for (Enumeration e = allowed_retvals.elements(); e.hasMoreElements(); )
      System.out.println(" .. " + e.nextElement());  }
	
}
