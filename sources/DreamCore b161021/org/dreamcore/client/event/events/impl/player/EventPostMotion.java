package org.dreamcore.client.event.events.impl.player;

import org.dreamcore.client.event.events.callables.EventCancellable;

public class EventPostMotion extends EventCancellable {

    public float pitch;
    private float yaw;

    public EventPostMotion(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
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
}