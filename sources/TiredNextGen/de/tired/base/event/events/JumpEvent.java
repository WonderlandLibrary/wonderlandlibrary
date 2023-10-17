package de.tired.base.event.events;

import de.tired.base.event.Event;

public class JumpEvent extends Event {

    double motion;
    float yaw;

    public JumpEvent(double motion, float yaw) {
        this.motion = motion;
        this.yaw = yaw;
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
