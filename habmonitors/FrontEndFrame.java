package psl.habitats;
/**
 * FrontEndFrame.java
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

class FrontEndFrame extends JFrame
{
    public FrontEndFrame()
    {
	setTitle("Habitats Front End");
	setLocation(100, 100);
	setSize(500, 500);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	Container contentPane = getContentPane();
	contentPane.add(new FrontEndPanel());

    }
} // FrontEndFrame
