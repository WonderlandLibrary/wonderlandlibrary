// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import vip.Resolute.events.Event;

public class EventMoveFlying extends Event<EventMoveFlying>
{
    private float yaw;
    
    public EventMoveFlying(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
}
