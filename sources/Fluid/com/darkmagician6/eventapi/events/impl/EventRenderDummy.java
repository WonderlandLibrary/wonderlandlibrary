// 
// Decompiled by Procyon v0.6.0
// 

package com.darkmagician6.eventapi.events.impl;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventRenderDummy extends EventCancellable
{
    public int x;
    public int y;
    
    public EventRenderDummy(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
}
