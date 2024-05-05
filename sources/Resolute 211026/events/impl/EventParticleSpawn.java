// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import vip.Resolute.events.Event;

public class EventParticleSpawn extends Event<EventParticleSpawn>
{
    private final int type;
    private int multiplier;
    
    public EventParticleSpawn(final int type, final int multiplier) {
        this.type = type;
        this.multiplier = multiplier;
    }
    
    public int getEventType() {
        return this.type;
    }
    
    public int getMultiplier() {
        return this.multiplier;
    }
    
    public void setMultiplier(final int multiplier) {
        this.multiplier = multiplier;
    }
}
