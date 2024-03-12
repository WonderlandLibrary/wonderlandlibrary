// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import dev.tenacity.event.Event;

public class ClickEvent extends Event
{
    boolean fake;
    
    public ClickEvent(final boolean fake) {
        this.fake = fake;
    }
    
    public boolean isFake() {
        return this.fake;
    }
}
