// 
// Decompiled by Procyon v0.6.0
// 

package com.darkmagician6.eventapi.events.impl;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventConnect extends EventCancellable
{
    public String ip;
    
    public EventConnect(final String ip) {
        this.ip = ip;
    }
}
