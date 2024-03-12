// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.event.Event;

public class SafeWalkEvent extends Event
{
    private boolean safe;
    
    public boolean isSafe() {
        return this.safe;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setSafe(final boolean safe) {
        this.safe = safe;
    }
}
