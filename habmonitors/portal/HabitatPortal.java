package psl.habitats.portal;

import psl.habitats.ServiceInterface;

import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;

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

public class HabitatPortal extends JFrame {
  private static final int WIDTH = 800;
  private static final int HEIGHT = 450;
  private static final int CTRL_WIDTH = 200;
  
  private final Hashtable _portals;

  private final JPanel _mainPanel;
  private final JPanel _ctrlPanel;
  private final JList _servicesList;
  private final JLabel _dataLabel;
  private final JPanel _portalPanel;
    //  private final JTextArea _logArea;
    //  private final JScrollPane _logPane;
  
  private final CardLayout _cardLayout;
  
  public HabitatPortal(String habitatName, Enumeration services) {
    
    // Data struRctures ///////////////////////////////////////////////////
    _portals = new Hashtable();
    
    // GUI stuff /////////////////////////////////////////////////////////

    // for demo
    if (habitatName.charAt(0) == 'N') {
	setLocation(100, 50);
    } else if (habitatName.charAt(0) == 'A') {
	setLocation(200, 100);
    } else if (habitatName.charAt(0) == 'T') {
	setLocation(300, 150);
    }

    setTitle(habitatName);
    setSize(HabitatPortal.WIDTH, HabitatPortal.HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    _mainPanel = new JPanel(new BorderLayout());
    _ctrlPanel = new JPanel(new GridLayout(2, 1, 5, 5));
    _dataLabel = new JLabel("");
    _portalPanel = new JPanel(_cardLayout = new CardLayout());
    //    _logPane = new JScrollPane(_logArea = new JTextArea("some text", 8, 10));
    //    _logArea.setEditable(false);



    if (services != null) {
      Vector servicesListing = new Vector();
      while (services.hasMoreElements()) {
        ServiceInterface si = (ServiceInterface) services.nextElement();
        String serviceDesc = si.getDescription();
        servicesListing.add(serviceDesc);
        _portalPanel.add(si.startDisplay(), serviceDesc);
        
        _portals.put(serviceDesc, si);
      }
      _servicesList = new JList(servicesListing.toArray());
    } else _servicesList = new JList(new String[] { "hello", "there" });
    
    _servicesList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent lse) {
        String service = "" + _servicesList.getSelectedValue();
        try {
          _dataLabel.setText(generateHTML(_portals.get(service)));
          _cardLayout.show(_portalPanel, service);
        } catch (Exception e) { }
      }
    });
    
    _mainPanel.add(_ctrlPanel, BorderLayout.WEST);
    _mainPanel.add(_portalPanel, BorderLayout.CENTER);
    //    _mainPanel.add(_logPane, BorderLayout.SOUTH);
    
    _ctrlPanel.add(_servicesList);
    _ctrlPanel.add(new JScrollPane(_dataLabel));
    _ctrlPanel.setMinimumSize(new Dimension(CTRL_WIDTH, HEIGHT));
    
    getContentPane().add(_mainPanel);
    show();
  }
  
  public void updatePortal(String key) {
    _cardLayout.show(_portalPanel, key);
  }
  public void log(String msg) {
      //    _logArea.append(msg + "\n");
  }
  
  private String generateHTML(Object o) {
    if (o instanceof ServiceInterface) 
      return ((ServiceInterface) o).getDetailDescription();
    else return "INVALID";
  }

  public static void main(String args[]) {
    new HabitatPortal("HabitatPortal", null);
  }
}
