package psl.habitats;

import javax.swing.JPanel;
import java.util.Hashtable;

public interface ServiceInterface {
  public void initialize(Habitat _h);
  public String getDescription();
	public JPanel startDisplay();
  public String getDetailDescription();
  public Hashtable performService(String _senderService, Hashtable ipList);
	// public void startDisplay(Graphics g){}
}
