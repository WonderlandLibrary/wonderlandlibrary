package vip.radium.event.impl.player;

import vip.radium.event.CancellableEvent;

public final class UpdatePositionEvent extends CancellableEvent {

    private final float prevYaw, prevPitch;
    private final double prevPosX, prevPosY, prevPosZ;
    private double posX, posY, posZ;
    private float yaw, pitch;
    private boolean ground;
    private boolean pre;
    private boolean rotating;
    public UpdatePositionEvent(float prevYaw, float prevPitch,
                               double posX, double posY, double posZ,
                               double prevPosX, double prevPosY, double prevPosZ,
                               float yaw, float pitch,
                               boolean ground) {
        this.prevYaw = prevYaw;
        this.prevPitch = prevPitch;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.prevPosX = prevPosX;
        this.prevPosY = prevPosY;
        this.prevPosZ = prevPosZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.pre = true;
    }

    public double getPrevPosX() {
        return prevPosX;
    }

    public double getPrevPosY() {
        return prevPosY;
    }

    public double getPrevPosZ() {
        return prevPosZ;
    }

    public boolean isRotating() {
        return rotating;
    }

    public float getPrevYaw() {
        return prevYaw;
    }

    public float getPrevPitch() {
        return prevPitch;
    }

    public boolean isPre() {
        return pre;
    }

    public void setPost() {
        this.pre = false;
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

    public float getYaw() {
        return yaw;
    }

    public boolean hasMoved() {
        final double xDif = prevPosX - posX;
        final double yDif = prevPosY - posY;
        final double zDif = prevPosZ - posZ;

        return Math.sqrt(xDif * xDif + yDif * yDif + zDif * zDif) > 1.0E-5D;
    }

    public void setYaw(float yaw) {
        this.rotating = this.yaw - yaw != 0.0F;
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.rotating = this.pitch - pitch != 0.0F;
        this.pitch = pitch;
    }

    public boolean isOnGround() {
        return ground;
    }

    public void setOnGround(boolean ground) {
        this.ground = ground;
    }
}
