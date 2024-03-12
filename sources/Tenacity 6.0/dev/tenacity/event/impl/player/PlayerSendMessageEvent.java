// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.event.Event;

public class PlayerSendMessageEvent extends Event
{
    private final String message;
    
    public PlayerSendMessageEvent(final String message) {
        this.message = message;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public String getMessage() {
        return this.message;
    }
}
