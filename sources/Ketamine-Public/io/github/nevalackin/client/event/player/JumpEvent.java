package io.github.nevalackin.client.event.player;

import io.github.nevalackin.client.event.CancellableEvent;

public final class JumpEvent extends CancellableEvent {

    private float yaw;
    private double motion;

    public JumpEvent(double motion, float yaw) {
        this.yaw = yaw;
        this.motion = motion;
    }

    public double getMotion() {
        return motion;
    }

    public void setMotion(double motion) {
        this.motion = motion;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
