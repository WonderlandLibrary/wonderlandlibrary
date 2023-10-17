package de.tired.base.event.events;

import de.tired.base.event.Event;

public class GetLookEvent extends Event {

    float pitch, yaw, prevPitch, prevYaw;


    public GetLookEvent(float yaw, float prevYaw, float pitch, float prevPitch) {
        this.yaw = yaw;
        this.prevYaw = prevYaw;
        this.pitch = pitch;
        this.prevPitch = prevPitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPrevPitch() {
        return prevPitch;
    }

    public void setPrevPitch(float prevPitch) {
        this.prevPitch = prevPitch;
    }

    public float getPrevYaw() {
        return prevYaw;
    }

    public void setPrevYaw(float prevYaw) {
        this.prevYaw = prevYaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
