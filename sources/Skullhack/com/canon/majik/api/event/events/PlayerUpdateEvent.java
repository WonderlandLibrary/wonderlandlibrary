package com.canon.majik.api.event.events;

import com.canon.majik.api.event.eventBus.Event;

public class PlayerUpdateEvent extends Event {
    private double x, y,  z;
    private final double prevX, prevY, prevZ;
    private float rotationYaw;
    private float rotationPitch;
    private final float prevYaw, prevPitch;
    private boolean onGround;
    private final boolean prevOnGround;

    public PlayerUpdateEvent(PlayerUpdateEvent event){
        this(event.getX(), event.getY(), event.getZ(), event.getRotationYaw(), event.getRotationPitch(), event.isOnGround());
    }

    public PlayerUpdateEvent(double x, double y, double z, float rotationYaw, float rotationPitch, boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.onGround = onGround;
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.prevYaw = rotationYaw;
        this.prevPitch = rotationPitch;
        this.prevOnGround = onGround;
    }


    public double getPrevX() {
        return prevX;
    }

    public double getPrevY() {
        return prevY;
    }

    public double getPrevZ() {
        return prevZ;
    }

    public float getPrevYaw() {
        return prevYaw;
    }

    public float getPrevPitch() {
        return prevPitch;
    }

    public boolean isPrevOnGround() {
        return prevOnGround;
    }

    public float getRotationYaw() {
        return rotationYaw;
    }

    public float getRotationPitch() {
        return rotationPitch;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return rotationYaw;
    }

    public void setYaw(float rotationYaw) {
        this.rotationYaw = rotationYaw;
    }

    public float getPitch() {
        return rotationPitch;
    }

    public void setPitch(float rotationPitch) {
        this.rotationPitch = rotationPitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
