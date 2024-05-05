package com.initial.events.impl;

import com.initial.events.*;

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
