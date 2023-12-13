// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

public class Rotation
{
    private float yaw;
    private float pitch;
    private boolean active;
    
    public Rotation(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public Rotation add(final float yaw, final float pitch) {
        this.yaw += yaw;
        this.pitch += pitch;
        return this;
    }
    
    public Rotation subtract(final float yaw, final float pitch) {
        this.yaw -= yaw;
        this.pitch -= pitch;
        return this;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public Rotation setYaw(final float yaw) {
        this.active = true;
        this.yaw = yaw;
        return this;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public Rotation setPitch(final float pitch) {
        this.active = true;
        this.pitch = pitch;
        return this;
    }
    
    public boolean isActive() {
        return this.active;
    }
}
