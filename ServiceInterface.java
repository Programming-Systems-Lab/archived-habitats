package psl.habitats;

public class ServiceInterface {
  public String roleName;

    public ServiceInterface() {}

    public void initialize() {}

    public String getName() {
	    return roleName; 
    }
		
		public void startDisplay() {}
		public Vector performService(Hashtable ipList) {}

		// public void startDisplay(Graphics g){} 
}
