// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.game;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.event.Event;

public class TickEvent extends StateEvent
{
    private final int ticks;
    
    public TickEvent(final int ticks) {
        this.ticks = ticks;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public int getTicks() {
        return this.ticks;
    }
}
