// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.player;

import ru.fluger.client.event.events.callables.EventCancellable;

public class EventPlayerState extends EventCancellable
{
    private final boolean isPre;
    private float yaw;
    private float pitch;
    private double x;
    private double y;
    private double z;
    private boolean onGround;
    
    public EventPlayerState(final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround) {
        this.y = y;
        this.x = x;
        this.z = z;
        this.isPre = true;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }
    
    public EventPlayerState() {
        this.isPre = false;
    }
    
    public boolean isPre() {
        return this.isPre;
    }
    
    public boolean isPost() {
        return !this.isPre;
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
