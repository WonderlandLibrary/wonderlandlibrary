// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.render;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.event.Event;

public class Render3DEvent extends Event
{
    private float ticks;
    
    public Render3DEvent(final float ticks) {
        this.ticks = ticks;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public float getTicks() {
        return this.ticks;
    }
    
    public void setTicks(final float ticks) {
        this.ticks = ticks;
    }
}
