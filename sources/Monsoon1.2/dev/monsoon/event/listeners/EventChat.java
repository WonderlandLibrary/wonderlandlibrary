package dev.monsoon.event.listeners;

import dev.monsoon.event.Event;

public class EventChat extends Event<EventChat> {

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
