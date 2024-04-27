package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.CancellableEvent;

public class StrafeEvent extends CancellableEvent {
    private float strafe;
    private float forward;
    private float friction;

    public float getStrafe() {
        return this.strafe;
    }

    public float getForward() {
        return this.forward;
    }

    public float getFriction() {
        return this.friction;
    }

    public StrafeEvent(float strafe, float forward, float friction) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
    }
}
