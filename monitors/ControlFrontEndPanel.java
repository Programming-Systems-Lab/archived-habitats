/**
 * ControlFrontEndPanel
 *
 *
 * Created: Thu Nov 15 09:23:42 2001
 *
 * @author Christy Lauridsen
 * @version
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class ControlFrontEndPanel extends JPanel
{
    Monitor m = new Monitor();

    public ControlFrontEndPanel()
    {
	setLayout(new GridLayout(0,1));
	
	for (int i = 0; i < 3; i++) {
	    add(new ServiceOptionsPanel("Service"+i));
	}
    }

    void addService(String sname) {
	add(new ServiceOptionsPanel(sname));
    }

    void msgToMon(String servname, String message) {
	m.writemonitor(message, servname);
    }

    class ServiceOptionsPanel extends JPanel
    {
	String mname;
	JTextField mtext = new JTextField(10);

	public ServiceOptionsPanel(String name)
	{
	    mname = name;
	    JButton monitor = new JButton("Monitor");
	    JButton write = new JButton("Write");
	    JButton remove = new JButton("Remove");
	    add(new JLabel(mname));
	    add(monitor);
	    add(mtext);
	    add(write);
	    add(remove);
	    monitor.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent ev)
		    {
			m.addmonitor(mname);
		    }
		});
	    write.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent ev)
		    {
			m.writemonitor(mtext.getText(), mname);
		    }
		});
	    remove.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent ev)
		    {
			m.removemonitor(mname);
		    }
		});
	}

    }

} // ControlFrontEndPanel
