package psl.habitats;

import siena.*;

import java.util.Vector;
import java.util.Hashtable;

public interface ServiceInterface {
  public void initialize(Habitat _h);
  public String getDescription();
	public void startDisplay();
  public Hashtable performService(String _senderService, Hashtable ipList);
	// public void startDisplay(Graphics g){} 
}
