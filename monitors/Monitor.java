
/**
 * Monitor.java
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
import java.util.*;

class Monitor //extends JFrame
{
    boolean noframe = true;
    JFrame f = new JFrame("Monitors");
    Hashtable monitors = new Hashtable();
    
    public Monitor() {}

    void addmonitor(String name) {

	if (monitors.get(name) != null) { return; }

	monitors.put(name, new TextAreaPanel(name));

	(f.getContentPane()).add((TextAreaPanel)monitors.get(name));

	f.validate();

	if (noframe) {
	    (f.getContentPane()).setLayout(new GridLayout(0,2));
	    f.setSize(500, 500);
	    f.setVisible(true);
	    noframe = false;
	}
    }

    void writemonitor(String str, String svcname) {
	try {
	    ((TextAreaPanel)(monitors.get(svcname))).h_textArea.append(str+"\n>");
	} catch (Exception e) {
	}
    }

    void removemonitor(String svcname) {
	try {
	    f.remove((Component)monitors.get(svcname));
	    f.validate();
	    monitors.remove(svcname);
	} catch (Exception e) {
	}
    }

    /////// Inner classes /////
    class TextAreaPanel extends JPanel
    {
	JTextArea h_textArea = new JTextArea(8, 20);

	public TextAreaPanel(String name)
	{
	    setLayout( new BorderLayout() );
	    h_textArea.setText(">");
	    JScrollPane scrollPane = new JScrollPane(h_textArea);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    add(scrollPane, BorderLayout.CENTER);
	    add(new JLabel(name), BorderLayout.SOUTH);
	}
    }
    

} // Monitor

	
