import java.lang.*;
import siena.*;

class FilterThread extends Thread {
	public Filter f;
	public Notifiable interestedParty;
	public Siena s = null;

	public FilterThread(Siena sie, Filter f, Notifiable c) {
		filter = f;
		interestedParty = c;
		s = sie;
	}

	public void run() {
		if ((!interestedParty) || (!filter)) { 
			// Alpa: need to do something like exit 
		}
		try {
			s.subscribe(f1,interestedParty);
		} catch (siena.SienaException se) {
			se.printStackTrace();
		}
	}
}
