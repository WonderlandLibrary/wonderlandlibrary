package de.tired.base.event.events;

import de.tired.base.event.Event;

public class EventKey extends Event
{
	public int key;

	public EventKey(final int key) {
		this.key = key;
	}

	public int getKey() {
		return this.key;
	}
}
