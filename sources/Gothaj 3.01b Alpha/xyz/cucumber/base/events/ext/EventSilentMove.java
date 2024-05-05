package xyz.cucumber.base.events.ext;

import xyz.cucumber.base.events.Event;

public class EventSilentMove extends Event {
	boolean silent;

	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	public EventSilentMove(boolean silent) {
		this.silent = silent;
	}
}
