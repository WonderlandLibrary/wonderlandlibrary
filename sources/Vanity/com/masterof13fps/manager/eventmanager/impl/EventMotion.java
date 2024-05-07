package com.masterof13fps.manager.eventmanager.impl;

import com.masterof13fps.manager.eventmanager.Event;

public class EventMotion extends Event {

    float yaw, pitch;
    Type type;

    public EventMotion(Type type, float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.type = type;
    }

    public Type getType() {
        return type;
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

    public enum Type {
        PRE, POST;
    }
}
