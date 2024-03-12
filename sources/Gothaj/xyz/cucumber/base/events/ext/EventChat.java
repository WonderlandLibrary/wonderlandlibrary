package xyz.cucumber.base.events.ext;

import xyz.cucumber.base.events.Event;

public class EventChat extends Event{
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public EventChat(String message) {
		this.message = message;
	}

	
}