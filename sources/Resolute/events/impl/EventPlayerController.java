// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import vip.Resolute.events.Event;

public class EventPlayerController extends Event<EventPlayerController>
{
    private float yaw;
    private double motion;
    private boolean sprinting;
    
    public EventPlayerController(final float yaw, final double motion, final boolean sprinting) {
        this.yaw = yaw;
        this.motion = motion;
        this.sprinting = sprinting;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public double getMotion() {
        return this.motion;
    }
    
    public void setMotion(final double motion) {
        this.motion = motion;
    }
    
    public boolean isSprinting() {
        return this.sprinting;
    }
    
    public void setSprinting(final boolean sprinting) {
        this.sprinting = sprinting;
    }
}
