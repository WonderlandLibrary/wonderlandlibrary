package com.initial.events.impl;

import com.initial.events.*;

public class EventRender3D extends Event
{
    private float partialTicks;
    
    public EventRender3D(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
