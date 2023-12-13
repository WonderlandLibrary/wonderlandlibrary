package io.github.nevalackin.client.event.player;

import io.github.nevalackin.client.event.Event;

public final class MoveFlyingEvent implements Event {

    private float yaw;

    public MoveFlyingEvent(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
