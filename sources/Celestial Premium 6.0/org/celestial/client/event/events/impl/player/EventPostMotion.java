/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.player;

import org.celestial.client.event.events.callables.EventCancellable;

public class EventPostMotion
extends EventCancellable {
    public float pitch;
    private float yaw;

    public EventPostMotion(float yaw, float pitch) {
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

