package psl.habitats;

import java.util.Vector;
import java.util.Hashtable;
public interface ServiceInterface {
  public void initialize();
  public String getName();
	public void startDisplay();
	public Vector performService(Hashtable ipList);

	// public void startDisplay(Graphics g){} 
}
