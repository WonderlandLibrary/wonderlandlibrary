package me.nyan.flush.event.impl;

import me.nyan.flush.event.Event;

public class EventChat extends Event {
    public String message;

    public EventChat(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
