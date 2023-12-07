package Velo.impl.Event;

import Velo.api.Event.Event;

public class EventPreMotion extends Event<EventPreMotion> {

    public double x, y, z;
    public float yaw, pitch;
    public boolean onGround;
    private boolean rotating;
    private float prevYaw;
    private float prevPitch;

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
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.rotating = true;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        this.rotating = true;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getPrevPitch() {
        return prevPitch;
    }

    public float getPrevYaw() {
        return prevYaw;
    }

    public boolean isRotating() {
        return rotating;
    }

    public EventPreMotion(float prevYaw, float prevPitch, double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.prevPitch = prevPitch;
        this.prevYaw = prevYaw;
    }
}