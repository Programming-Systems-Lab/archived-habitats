package psl.habitats;

import java.util.Vector;
import java.util.Hashtable;

public interface ServiceInterface {
  public void initialize();
  public String getDescription();
	public void startDisplay();
  public Hashtable performService(String _senderService, Hashtable ipList);
	// public void startDisplay(Graphics g){} 
}
