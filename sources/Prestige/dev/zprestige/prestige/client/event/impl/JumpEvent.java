package dev.zprestige.prestige.client.event.impl;

import dev.zprestige.prestige.client.event.Event;

public class JumpEvent extends Event {
    public float yaw;

    public JumpEvent(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
