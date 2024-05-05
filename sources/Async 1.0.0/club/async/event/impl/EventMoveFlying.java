package club.async.event.impl;

import club.async.event.Event;

public class EventMoveFlying extends Event {

    private float yaw;

    public EventMoveFlying(float yaw) {
        this.yaw = yaw;
    }

    public final float getYaw() {
        return yaw;
    }

    public final void setYaw(float yaw) {
        this.yaw = yaw;
    }

}
