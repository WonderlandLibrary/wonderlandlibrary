// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

public class EventMove extends Event
{
    private float yaw;
    private float friction;
    
    public EventMove(final float yaw, final float friction) {
        this.yaw = yaw;
        this.friction = friction;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getFriction() {
        return this.friction;
    }
    
    public void setFriction(final float friction) {
        this.friction = friction;
    }
}
