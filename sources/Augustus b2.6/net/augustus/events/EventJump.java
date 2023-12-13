// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

public class EventJump extends Event
{
    private float yaw;
    
    public EventJump(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
}
