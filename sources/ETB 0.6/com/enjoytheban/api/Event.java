package com.enjoytheban.api;

public abstract class Event {

	//Creating variables
	private boolean cancelled;
	public byte type;

	/**
	 * Basically just checking if an event is cancelled or not
	 */
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * Set if the event should be cancelled or not
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * Gets the type of event
	 */
	public byte getType() {
		return this.type;
	}

	/**
	 * Set the event type according to values set in the EventType class
	 */
	public void setType(byte type) {
		this.type = type;
	}
}
