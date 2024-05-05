package com.initial.events.impl;

import com.initial.events.*;
import net.minecraft.client.*;

public class EventMotionUpdate extends Event
{
    double x;
    double y;
    double z;
    float yaw;
    float pitch;
    float lastYaw;
    float lastPitch;
    State state;
    boolean onGround;
    
    public EventMotionUpdate(final double x, final double y, final double z, final float yaw, final float pitch, final float lastYaw, final float lastPitch, final boolean onGround, final State state) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.lastYaw = lastYaw;
        this.lastPitch = lastPitch;
        this.state = state;
        this.onGround = onGround;
    }
    
    @Override
    public boolean isPre() {
        return this.state == State.PRE;
    }
    
    public boolean onGround() {
        return this.onGround;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
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
        Minecraft.getMinecraft().thePlayer.rotationPitch = pitch;
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
    
    public State getState() {
        return this.state;
    }
    
    public void setState(final State state) {
        this.state = state;
    }
}
