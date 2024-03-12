// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.player;

import dev.tenacity.event.Event;

public class JumpFixEvent extends Event
{
    private float yaw;
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public JumpFixEvent(final float yaw) {
        this.yaw = yaw;
    }
}
