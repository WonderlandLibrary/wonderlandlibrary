package com.masterof13fps.manager.eventmanager.impl;

import com.masterof13fps.manager.eventmanager.Event;

public class EventChat extends Event {

    String message;

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
