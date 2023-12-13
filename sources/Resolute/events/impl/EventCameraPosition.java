// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import vip.Resolute.events.Event;

public class EventCameraPosition extends Event<EventCameraPosition>
{
    public double y;
    
    public EventCameraPosition(final double y) {
        this.y = y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public double getY() {
        return this.y;
    }
}
