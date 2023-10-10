/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event.impl;

import tk.rektsky.event.Event;

public final class MotionUpdateEvent
extends Event {
    private double posX;
    private double lastPosX;
    private double posY;
    private double lastPosY;
    private double posZ;
    private double lastPosZ;
    public float yaw;
    private float lastYaw;
    public float pitch;
    private float lastPitch;
    private boolean onGround;
    private Type type;
    private boolean cancelled = false;
    private boolean sneaking;
    private boolean sprinting;

    public MotionUpdateEvent(double posX, double posY, double posZ, float yaw, float pitch, boolean onGround, boolean sprinting, boolean sneaking, Type type) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.type = type;
        this.sneaking = sneaking;
        this.sprinting = sprinting;
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public MotionUpdateEvent setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
        return this;
    }

    public MotionUpdateEvent setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
        return this;
    }

    public boolean isPre() {
        return this.type == Type.PRE;
    }

    public double getPosX() {
        return this.posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getLastPosX() {
        return this.lastPosX;
    }

    public void setLastPosX(double lastPosX) {
        this.lastPosX = lastPosX;
    }

    public double getPosY() {
        return this.posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getLastPosY() {
        return this.lastPosY;
    }

    public void setLastPosY(double lastPosY) {
        this.lastPosY = lastPosY;
    }

    public double getPosZ() {
        return this.posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public double getLastPosZ() {
        return this.lastPosZ;
    }

    public void setLastPosZ(double lastPosZ) {
        this.lastPosZ = lastPosZ;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getLastYaw() {
        return this.lastYaw;
    }

    public void setLastYaw(float lastYaw) {
        this.lastYaw = lastYaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getLastPitch() {
        return this.lastPitch;
    }

    public void setLastPitch(float lastPitch) {
        this.lastPitch = lastPitch;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean newState) {
        this.cancelled = newState;
    }

    public static enum Type {
        PRE,
        POST;

    }
}

