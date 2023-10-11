package com.initial.events.impl;

import com.initial.events.*;

public class EntityRenderEvent extends Event
{
    private final float partialTicks;
    
    public EntityRenderEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
