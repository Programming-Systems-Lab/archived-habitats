package psl.habitats.portal;

import psl.habitats.ServiceInterface;

import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.*;

import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public class HabitatMonitor extends JFrame {
  private static final int WIDTH = 800;
  private static final int HEIGHT = 400;
  private static final int CTRL_WIDTH = 200;
  
  private final Hashtable _portals;

  private final JPanel _mainPanel;
    private final JPanel _logPanel;
    private final JPanel _svcPanel;
    private final JPanel _svcPane;
    private final JLabel _logLabel;
    private final JLabel _svcLabel;
    //  private final JPanel _ctrlPanel;
    //  private final JList _servicesList;
    //  private final JLabel _dataLabel;
    //  private final JPanel _portalPanel;
  private final JTextArea _logArea;
  private final JScrollPane _logPane;

    Hashtable monitors = new Hashtable();
    Hashtable messages = new Hashtable();
  
    //  private final CardLayout _cardLayout;
  
  public HabitatMonitor(String habitatName) { 
      //, Enumeration services, 
      //		HabitatPortal hp) {
    
    // Data struRctures ///////////////////////////////////////////////////
    _portals = new Hashtable();
    
    // GUI stuff /////////////////////////////////////////////////////////
    //    Point p = hp.getLocation();
    //    System.out.println("christy:  location: " + p.x + " " + p.y);
    //    Dimension d = hp.getSize();

    setTitle(habitatName + " Monitor");
    setSize(HabitatMonitor.WIDTH, HabitatMonitor.HEIGHT);
    //    setLocation(p.x, p.y+(HabitatMonitor.HEIGHT*6+50));

    //    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    
    //CKL
    _mainPanel = new JPanel(new GridLayout(0,1));
    //_mainPanel = new JPanel(new BorderLayout());
    //    _ctrlPanel = new JPanel(new GridLayout(2, 1, 5, 5));
    //    _dataLabel = new JLabel("");
    //    _portalPanel = new JPanel(_cardLayout = new CardLayout());
    
    _logPanel = new JPanel(new BorderLayout());
    _logLabel = new JLabel(habitatName + " Habitat Monitor");
    _logPane = new JScrollPane(_logArea = new JTextArea("", 8, 10));
    _logArea.setEditable(false);

    _svcPanel = new JPanel(new BorderLayout());
    _svcPane = new JPanel(new GridLayout(0,2));
    _svcLabel = new JLabel("Service Monitors");
    //    _svcPane = new JScrollPane(_logArea = new JTextArea("some text", 8, 10));
    //    _svcArea.setEditable(false);

    _logPanel.add(_logLabel, BorderLayout.NORTH);
    _logPanel.add(_logPane, BorderLayout.CENTER);

    _svcPanel.add(_svcLabel, BorderLayout.NORTH);
    _svcPanel.add(_svcPane, BorderLayout.CENTER);
 

    //    _mainPanel.add(_ctrlPanel, BorderLayout.WEST);
    //    _mainPanel.add(_portalPanel, BorderLayout.CENTER);
    _mainPanel.add(_logPanel, BorderLayout.NORTH);
    _mainPanel.add(_svcPanel, BorderLayout.CENTER);
    //    _mainPanel.add(_logPane);
    
    //    _ctrlPanel.add(_servicesList);
    //    _ctrlPanel.add(new JScrollPane(_dataLabel));
    //    _ctrlPanel.setMinimumSize(new Dimension(CTRL_WIDTH, HEIGHT));
    
    getContentPane().add(_mainPanel);

    // getContentPane().add(_logPanel);

    //    getContentPane().add(_svcPanel);


    //    show();
  }
    
    public void fix(HabitatPortal hap) {
	//public void fix(Point p) {
	Point p = hap.getLocation();
	setLocation(p.x, p.y+(HabitatMonitor.HEIGHT+100));
	show();
    }



    public void addmonitor(String name) {
	//	String name = _portals.get(service);

	if (monitors.get(name) != null) { return; }
	monitors.put(name, new TextAreaPanel(name));
	//(f.getContentPane()).add((TextAreaPanel)monitors.get(name));
	_svcPane.add((TextAreaPanel)monitors.get(name));
	rewritemonitor(name);
	//	f.validate();
	/*	if (noframe) {
	    (f.getContentPane()).setLayout(new GridLayout(0,2));
	    f.setSize(500, 500);
	    f.setVisible(true);
	    noframe = false;
	    }*/
    }

    public void writemonitor(String str, String svcname) {
	try {
	    //new -- save messages
	    String output = (str + "\n"); //>");
	    //	    messages.add(output);
	    if (messages.get(svcname) == null) { 
		messages.put(svcname, new Vector());
	    }
	    ((Vector)(messages.get(svcname))).add(output);

	    ((TextAreaPanel)
	     (monitors.get(svcname))).h_textArea.append(output);
	} catch (Exception e) {
	}
    }

    void rewritemonitor(String svcname) {
	try {
	    for (Enumeration e = 
		     ((Vector)(messages.get(svcname))).elements(); 
		 e.hasMoreElements() ;) {	

		//		System.out.println("christy: want to write enumeration: " +
		//				   ((String) e.nextElement()));
		((TextAreaPanel)
		 (monitors.get(svcname)))
		    .h_textArea.append((String) e.nextElement());
	    }
	} catch (Exception e) {
	}  
    }

    /*    void writemonitor(String str, String svcname) {
	try {
	    ((TextAreaPanel)(monitors.get(svcname))).h_textArea.append(str+"\n>");
	} catch (Exception e) {
	}
	}*/

    /////// Inner classes /////
    class TextAreaPanel extends JPanel
    {
	JTextArea h_textArea = new JTextArea(8, 20);

	public TextAreaPanel(String name)
	{
	    setLayout( new BorderLayout() );
	    //	    h_textArea.setText(">");
	    JScrollPane scrollPane = new JScrollPane(h_textArea);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    add(scrollPane, BorderLayout.CENTER);
	    add(new JLabel(name), BorderLayout.SOUTH);
	}
    }



  public void updatePortal(String key) {
      //    _cardLayout.show(_portalPanel, key);
  }
  public void log(String msg) {
    _logArea.append(msg + "\n");
  }
  
  private String generateHTML(Object o) {
    if (o instanceof ServiceInterface) 
      return ((ServiceInterface) o).getDetailDescription();
    else return "INVALID";
  }

  public static void main(String args[]) {
      new HabitatMonitor("HabitatMonitor"); //, null, null);
  }
}
