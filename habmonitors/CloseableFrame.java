package psl.habitats;

import java.awt.*;
import java.awt.event.*;

public class CloseableFrame extends Frame {
  public CloseableFrame() {
    addWindowListener(new WindowAdapter() {public void 
        windowClosing(WindowEvent e ) {System.exit(0); }} );
    setSize(300,200);
		setLocation(300,200);
  }
	public void sTitle(String name) {
    setTitle(name);
	}
  public static void main(String[] args) {
    CloseableFrame c = new CloseableFrame();
    c.show();
  }
}



