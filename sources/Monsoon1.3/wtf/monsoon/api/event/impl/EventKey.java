package wtf.monsoon.api.event.impl;

import wtf.monsoon.api.event.Event;

public class EventKey extends Event {
	private int key;
	
	public EventKey(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

}
