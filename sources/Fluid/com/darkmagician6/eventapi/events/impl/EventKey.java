// 
// Decompiled by Procyon v0.6.0
// 

package com.darkmagician6.eventapi.events.impl;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventKey extends EventCancellable
{
    public int key;
    
    public EventKey(final int key) {
        this.key = key;
    }
}
