package Velo.impl.Event;

import Velo.api.Event.Event;

public class EventKey extends Event<EventKey> {
	
	public int code;
	
	public EventKey(int code) {
		this.code = code;
	}

	public int getKey() {
		return code;
	}

	public void setKey(int code) {
		this.code = code;
	}
	
}
