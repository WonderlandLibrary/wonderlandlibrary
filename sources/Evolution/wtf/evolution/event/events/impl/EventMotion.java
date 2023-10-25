package wtf.evolution.event.events.impl;


import wtf.evolution.event.events.Event;

public class EventMotion implements Event {

private float yaw, pitch;
private double posX, posY, posZ;
private boolean onGround;

public EventMotion(float yaw, float pitch, double posX, double posY, double posZ, boolean onGround) {
    this.yaw = yaw;
    this.pitch = pitch;
    this.posX = posX;
    this.posY = posY;
    this.posZ = posZ;
    this.onGround = onGround;
}

public float getYaw() {
    return yaw;
}

public void setYaw(float yaw) {
    this.yaw = yaw;
}

public float getPitch() {
    return pitch;
}

public void setPitch(float pitch) {
    this.pitch = pitch;
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

public boolean isOnGround() {
    return onGround;
}

public void setOnGround(boolean onGround) {
    this.onGround = onGround;
}
}
