// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import vip.Resolute.events.Event;

public class EventStrafe extends Event<EventStrafe>
{
    public float yaw;
    public float strafe;
    public float forward;
    public float friction;
    
    public EventStrafe(final float yaw, final float strafe, final float forward, final float friction) {
        this.yaw = yaw;
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getStrafe() {
        return this.strafe;
    }
    
    public void setStrafe(final float strafe) {
        this.strafe = strafe;
    }
    
    public float getForward() {
        return this.forward;
    }
    
    public void setForward(final float forward) {
        this.forward = forward;
    }
    
    public float getFriction() {
        return this.friction;
    }
    
    public void setFriction(final float friction) {
        this.friction = friction;
    }
}
