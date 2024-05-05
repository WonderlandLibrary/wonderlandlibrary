package io.github.nevalackin.client.event.player;

import io.github.nevalackin.client.event.CancellableEvent;

public final class UpdatePositionEvent extends CancellableEvent {

    private double posX;
    private double posY;
    private double posZ;
    private final double lastTickPosX;
    private final double lastTickPosY;
    private final double lastTickPosZ;
    private float yaw;
    private float pitch;
    private float lastTickYaw;
    private float lastTickPitch;
    private boolean onGround;
    private final boolean wasOnGround;
    private boolean post;
    private boolean rotating;

    public UpdatePositionEvent(double posX, double posY, double posZ, double lastTickPosX, double lastTickPosY, double lastTickPosZ, float yaw, float pitch, float lastTickYaw, float lastTickPitch, boolean onGround, boolean wasOnGround) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.lastTickPosX = lastTickPosX;
        this.lastTickPosY = lastTickPosY;
        this.lastTickPosZ = lastTickPosZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.lastTickYaw = lastTickYaw;
        this.lastTickPitch = lastTickPitch;
        this.onGround = onGround;
        this.wasOnGround = wasOnGround;
    }

    public void setPost() {
        this.post = true;
    }

    public boolean isPre() {
        return !this.post;
    }

    public boolean isWasOnGround() {
        return wasOnGround;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public double getLastTickPosX() {
        return lastTickPosX;
    }

    public double getLastTickPosY() {
        return lastTickPosY;
    }

    public double getLastTickPosZ() {
        return lastTickPosZ;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        if (this.yaw - yaw != 0.0F) {
            this.rotating = true;
        }

        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        if (this.pitch - pitch != 0.0F) {
            this.rotating = true;
        }

        this.pitch = pitch;
    }

    public float getLastTickYaw() {
        return lastTickYaw;
    }

    public float getLastTickPitch() {
        return lastTickPitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isRotating() {
        return rotating;
    }

    public void setLastTickYaw(float lastTickYaw) {
        this.lastTickYaw = lastTickYaw;
    }

    public void setLastTickPitch(float lastTickPitch) {
        this.lastTickPitch = lastTickPitch;
    }
}
