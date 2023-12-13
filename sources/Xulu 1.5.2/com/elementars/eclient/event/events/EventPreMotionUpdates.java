// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.event.Event;

public class EventPreMotionUpdates extends Event
{
    private boolean cancel;
    public float yaw;
    public float pitch;
    public double y;
    
    public EventPreMotionUpdates(final float yaw, final float pitch, final double y) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
    }
    
    public void setMotion(final double motionX, final double motionY, final double motionZ) {
        Wrapper.getMinecraft().player.motionX = motionX;
        Wrapper.getMinecraft().player.motionY = motionY;
        Wrapper.getMinecraft().player.motionZ = motionZ;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    @Override
    public void setCancelled(final boolean state) {
        this.cancel = state;
    }
}
