
/**
 * ControlFrontEndFrame.java
 *
 *
 * Created: Thu Nov 15 09:26:14 2001
 *
 * @author Christy Lauridsen
 * @version
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ControlFrontEndFrame extends JFrame
{
    ControlFrontEndPanel cfep = new ControlFrontEndPanel();

    public ControlFrontEndFrame()
    {
	setTitle("Habitats Front End");
	setLocation(100, 100);
	setSize(500, 500);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	Container contentPane = getContentPane();
	//	contentPane.add(new ControlFrontEndPanel());
	contentPane.add(cfep);
    }

    void msgToMon(String servname, String message) {
	cfep.msgToMon(servname, message);
    }

    void addMe(String servname) {
	cfep.addService(servname);
    }

} // ControlFrontEndFrame

