package epsilon.botting.cmds;

import epsilon.events.Event;

public class EventCMD extends Event<EventCMD>{

	public String message;

	public EventCMD(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
