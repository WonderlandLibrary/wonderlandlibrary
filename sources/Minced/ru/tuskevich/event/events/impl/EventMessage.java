// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import ru.tuskevich.event.events.callables.EventCancellable;

public class EventMessage extends EventCancellable
{
    private boolean canceled;
    private String message;
    
    public EventMessage(final String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
}
