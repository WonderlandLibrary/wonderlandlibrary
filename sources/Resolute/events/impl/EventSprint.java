// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import vip.Resolute.events.Event;

public class EventSprint extends Event<EventSprint>
{
    private boolean sprinting;
    
    public EventSprint(final boolean sprinting) {
        this.sprinting = sprinting;
    }
    
    public boolean isSprinting() {
        return this.sprinting;
    }
    
    public void setSprinting(final boolean sprinting) {
        this.sprinting = sprinting;
    }
}
