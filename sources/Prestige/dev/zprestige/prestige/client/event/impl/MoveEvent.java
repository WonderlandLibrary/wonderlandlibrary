package dev.zprestige.prestige.client.event.impl;

import dev.zprestige.prestige.client.event.Event;
import dev.zprestige.prestige.client.event.Phase;

public class MoveEvent extends Event {
    public float yaw;
    public float pitch;

    public MoveEvent(Phase phase, float yaw, float pitch) {
        super(phase);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
