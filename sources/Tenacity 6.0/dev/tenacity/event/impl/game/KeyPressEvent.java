// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.game;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.event.Event;

public class KeyPressEvent extends Event
{
    private final int key;
    
    public KeyPressEvent(final int key) {
        this.key = key;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public int getKey() {
        return this.key;
    }
}
