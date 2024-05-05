package tech.atani.client.listener.event.minecraft.player.movement;

import tech.atani.client.listener.event.Event;

public class RidingEntityEvent extends Event {
    double motionX, motionZ, motionY;
    boolean onGround;

    public RidingEntityEvent(double motionX, double motionZ, double motionY, boolean onGround) {
        this.motionX = motionX;
        this.motionZ = motionZ;
        this.motionY = motionY;
        this.onGround = onGround;
    }

    public double getMotionX() {
        return motionX;
    }

    public double getMotionZ() {
        return motionZ;
    }

    public double getMotionY() {
        return motionY;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setMotionX(double motionX) {
        this.motionX = motionX;
    }

    public void setMotionZ(double motionZ) {
        this.motionZ = motionZ;
    }

    public void setMotionY(double motionY) {
        this.motionY = motionY;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}