import java.lang.*;
import siena.*;

class FilterThread extends Thread {
	public Filter f1;
	public Notifiable interestedParty;
	public Siena s = null;

	public FilterThread(Siena sie, Filter f, Notifiable c) {
		f1 = f;
		interestedParty = c;
		s = sie;
	}

	public void run() {
		if ((interestedParty == null) || (f1 == null)) { 
			// Alpa: need to do something like exit 
		}
		try {
			s.subscribe(f1,interestedParty);
		} catch (siena.SienaException se) {
			se.printStackTrace();
		}
	}
}
