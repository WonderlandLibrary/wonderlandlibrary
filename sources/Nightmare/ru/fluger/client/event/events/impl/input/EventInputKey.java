// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.input;

import ru.fluger.client.event.events.Event;

public class EventInputKey implements Event
{
    private int key;
    
    public EventInputKey(final int key) {
        this.key = key;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setKey(final int key) {
        this.key = key;
    }
}
