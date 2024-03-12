// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.game;

import dev.tenacity.event.Event;

public class RenderTickEvent extends StateEvent
{
    private final float ticks;
    
    public RenderTickEvent(final float ticks) {
        this.ticks = ticks;
    }
    
    public float getTicks() {
        return this.ticks;
    }
}
