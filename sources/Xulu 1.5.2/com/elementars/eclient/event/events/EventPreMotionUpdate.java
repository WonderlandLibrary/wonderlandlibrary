// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import com.elementars.eclient.event.Event;

public class EventPreMotionUpdate extends Event
{
    private float yaw;
    private float pitch;
    private boolean ground;
    public double x;
    public double y;
    public double z;
    
    public EventPreMotionUpdate(final float yaw, final float pitch, final boolean ground, final double x, final double y, final double z) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public boolean onGround() {
        return this.ground;
    }
    
    public void setGround(final boolean ground) {
        this.ground = ground;
    }
}
