// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.events.callables.EventCancellable;

public class EventMotion extends EventCancellable implements Event
{
    private float yaw;
    private float pitch;
    private double posX;
    private double posY;
    private double posZ;
    private boolean onGround;
    
    public EventMotion(final float yaw, final float pitch, final double posX, final double posY, final double posZ, final boolean onGround) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.onGround = onGround;
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
    
    public double getPosX() {
        return this.posX;
    }
    
    public void setPosX(final double posX) {
        this.posX = posX;
    }
    
    public double getPosY() {
        return this.posY;
    }
    
    public void setPosY(final double posY) {
        this.posY = posY;
    }
    
    public double getPosZ() {
        return this.posZ;
    }
    
    public void setPosZ(final double posZ) {
        this.posZ = posZ;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
}
