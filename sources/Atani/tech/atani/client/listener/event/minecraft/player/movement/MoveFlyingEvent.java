package tech.atani.client.listener.event.minecraft.player.movement;

import tech.atani.client.listener.event.Event;

public final class MoveFlyingEvent
extends Event {
    private float strafe;
    private float forward;
    private float friction;

    public MoveFlyingEvent(float f, float f2, float f3) {
        this.strafe = f;
        this.forward = f2;
        this.friction = f3;
    }

    public final float getStrafe() {
        return this.strafe;
    }

    public final void setStrafe(float f) {
        this.strafe = f;
    }

    public final float getForward() {
        return this.forward;
    }

    public final void setForward(float f) {
        this.forward = f;
    }

    public final float getFriction() {
        return this.friction;
    }

    public final void setFriction(float f) {
        this.friction = f;
    }

}
