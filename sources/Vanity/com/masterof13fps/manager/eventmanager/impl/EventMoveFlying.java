package com.masterof13fps.manager.eventmanager.impl;

import com.masterof13fps.manager.eventmanager.Event;

public class EventMoveFlying extends Event {

    float yaw;

    public EventMoveFlying(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
