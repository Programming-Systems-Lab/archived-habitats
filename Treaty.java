package psl.habitats;
import java.util.Vector;

class Treaty {
	String src;
	String dest;
	String servUsed;
	int ID;
	int numIPparams;
	Vector allowed_ip_param = new Vector();
	Vector allowed_op_param = new Vector();

	public Treaty (String s, String d, int i, int t) {
		src = s;
		dest = d;
		ID = i;
		numIPparams = t;
	}

	//////////////////////////////////////////////////////
	// 				general getter setter functions
	//////////////////////////////////////////////////////
	String getServUsed() {
		return servUsed;
	}
	int getID() {
		return ID;
	}

	int get_IP_size() {
		return numIPparams;
	}
	//////////////////////////////////////////////////////
	// 				functions for input params 
	//////////////////////////////////////////////////////

	public void add_ip_param(Object param) {
		allowed_ip_param.add(param);
	}
	public void add_ip_list(Vector param_list) {
		allowed_ip_param.addAll(param_list);
	}
	public boolean valid_ip_param(Object param) {
		return (allowed_ip_param.contains(param));
	}
	public boolean valid_ip_list(Vector param_list) {
		return (allowed_ip_param.containsAll(param_list));
	}
	public Vector getIPList() {
		return (allowed_ip_param);
	}

	//////////////////////////////////////////////////////
	// 				functions for return types
	//////////////////////////////////////////////////////
	
	public void add_op_param(Object param) {
		allowed_op_param.add(param);
	}
	public void add_op_list(Vector param_list) {
		allowed_op_param.addAll(param_list);
	}
	public boolean valid_op_param(Object param) {
		return (allowed_op_param.contains(param));
	}
	public boolean valid_op_list(Vector param_list) {
		return (allowed_op_param.containsAll(param_list));
	}
}
