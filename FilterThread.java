import java.lang.*;

class FilterThread extends Thread {
	public Filter f;
	public Notifiable interestedParty;

	class FilterThread(Filter f, Notifiable c) {
		filter = f;
		interestedParty = c;
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
