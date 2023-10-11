package com.initial.events.impl;

import com.initial.events.*;
import net.minecraft.client.*;

public class EventPreMotionUpdate extends Event
{
    private float yaw;
    private float pitch;
    private boolean ground;
    public double x;
    public double y;
    public double z;
    State state;
    float lastYaw;
    float lastPitch;
    
    public EventPreMotionUpdate(final float yaw, final float pitch, final float lastYaw, final float lastPitch, final boolean ground, final double x, final double y, final double z) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.x = x;
        this.y = y;
        this.z = z;
        this.lastYaw = lastYaw;
        this.lastPitch = lastPitch;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
        Minecraft.getMinecraft().thePlayer.rotationYawHead = yaw;
        Minecraft.getMinecraft().thePlayer.renderYawOffset = yaw;
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
    
    public float getLastYaw() {
        return this.lastYaw;
    }
    
    public void setLastYaw(final float lastYaw) {
        this.lastYaw = lastYaw;
    }
    
    public float getLastPitch() {
        return this.lastPitch;
    }
    
    public void setLastPitch(final float lastPitch) {
        this.lastPitch = lastPitch;
    }
    
    public boolean isOnGround() {
        return this.ground;
    }
    
    public State getState() {
        return this.state;
    }
    
    public void setState(final State state) {
        this.state = state;
    }
}
