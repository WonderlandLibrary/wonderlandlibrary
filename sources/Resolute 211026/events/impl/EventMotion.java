// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import vip.Resolute.events.Event;

public class EventMotion extends Event<EventMotion>
{
    public boolean onGround;
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    private boolean rotating;
    private float prevYaw;
    private float prevPitch;
    private final double prevPosX;
    private final double prevPosY;
    private final double prevPosZ;
    
    public EventMotion(final float prevYaw, final float prevPitch, final double x, final double y, final double z, final double prevPosX, final double prevPosY, final double prevPosZ, final float yaw, final float pitch, final boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.prevPosX = prevPosX;
        this.prevPosY = prevPosY;
        this.prevPosZ = prevPosZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.prevPitch = prevPitch;
        this.prevYaw = prevYaw;
    }
    
    public boolean isRotating() {
        return this.rotating;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
        this.rotating = true;
    }
    
    public double getPrevPosX() {
        return this.prevPosX;
    }
    
    public double getPrevPosY() {
        return this.prevPosY;
    }
    
    public double getPrevPosZ() {
        return this.prevPosZ;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
        this.rotating = true;
    }
    
    public float getPrevPitch() {
        return this.prevPitch;
    }
    
    public float getPrevYaw() {
        return this.prevYaw;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
}
