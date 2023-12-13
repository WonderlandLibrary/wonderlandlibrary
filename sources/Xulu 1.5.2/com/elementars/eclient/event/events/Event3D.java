// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import com.elementars.eclient.event.Event;

public class Event3D extends Event
{
    private float partialTicks;
    
    public Event3D(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    @Override
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
