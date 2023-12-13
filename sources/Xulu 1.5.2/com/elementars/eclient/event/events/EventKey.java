// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import com.elementars.eclient.event.Event;

public class EventKey extends Event
{
    private int key;
    
    public EventKey(final int key) {
        this.key = key;
    }
    
    public int getKey() {
        return this.key;
    }
}
