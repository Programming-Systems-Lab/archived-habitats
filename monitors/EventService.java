
/**
 * EventService.java
 *
 *
 * Created: Sun Nov 18 06:05:15 2001
 *
 * @author Christina K. Lauridsen
 * @version
 */

public class EventService  {
    boolean needwrite = false;
    String mstr;

    public EventService() {
    }

    void sendMessages() {
	needwrite = true;
	mstr = "hello i'm the event service.";
    }

    boolean wantwrite() {
	return needwrite;
    }

    String writeToMonitor() {
	needwrite = false;
	return mstr;
    }

    String myName() {
	return "EventService";
    }

} // EventService
