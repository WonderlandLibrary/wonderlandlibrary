package wtf.monsoon.api.event.impl;

import wtf.monsoon.api.event.Event;

public class EventChat extends Event {
	
	public String message;

    public EventChat(String text) {
        this.message = text;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
