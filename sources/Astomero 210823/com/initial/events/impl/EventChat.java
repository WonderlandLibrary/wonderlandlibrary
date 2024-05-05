package com.initial.events.impl;

public class EventChat extends EventNigger<EventChat>
{
    public String message;
    
    public EventChat(final String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
}
