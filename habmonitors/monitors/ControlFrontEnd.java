
/**
 * ControlFrontEnd.java
 *
 *
 * Created: Thu Nov 15 09:26:46 2001
 *
 * @author Christy Lauridsen
 * @version
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;

public class ControlFrontEnd
{
    public static void main(String[] args)
    {
	ControlFrontEndFrame frame = new ControlFrontEndFrame();
	frame.setVisible(true);

	// get services, set up...
	EventService eserv = new EventService();

	frame.addMe(eserv.myName());

	while (true) {
	    eserv.sendMessages();
		
	    if (eserv.wantwrite()) {
		frame.msgToMon(eserv.myName(), eserv.writeToMonitor());
	    }

	    int sleepTime = 10000;
	    try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
	}
    }
} // ControlFrontEnd



