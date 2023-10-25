package org.dreamcore.client.event.events.impl.motion;

import org.dreamcore.client.event.events.Event;
import org.dreamcore.client.event.events.callables.EventCancellable;

public class EventJump extends EventCancellable implements Event {

    private float yaw;

    public EventJump(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}