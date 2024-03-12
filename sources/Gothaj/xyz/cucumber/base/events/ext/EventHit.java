package xyz.cucumber.base.events.ext;

import xyz.cucumber.base.events.Event;

public class EventHit extends Event
{
	boolean sprint = false;

	public void setSprint(boolean sprint) {
		this.sprint = sprint;
	}

	public boolean isSprint() {
		return sprint;
	}
	
	public EventHit(boolean sprint) {
		this.sprint = sprint;
	}
}
