package psl.habitats;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class Console extends CloseableFrame {
	TextArea log;
	Panel cf_panel;

public Console() {
  log = new TextArea();
  cf_panel = new Panel();
  cf_panel.add(log);
}

  public void cf_show(String name) {
		setTitle(name);
		add(cf_panel);
		show();
  }
	
  public void cf_add(String mesg) {
		log.append(mesg);
  }
}
