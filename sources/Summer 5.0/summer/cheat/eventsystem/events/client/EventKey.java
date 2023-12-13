package summer.cheat.eventsystem.events.client;

import summer.cheat.eventsystem.Event;

public class EventKey extends Event {
	
	private int key;
	
	public EventKey(int key) {
		this.key = key;
	}
	
	public int getKey() {
		return key;
	}

}
