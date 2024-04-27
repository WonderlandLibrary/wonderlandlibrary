package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;

public class SlowDownEvent extends Event {
    private float strafe;
    private float forward;

    public float getStrafe() {
        return this.strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public float getForward() {
        return this.forward;
    }

    public void setForward(float forword) {
        this.forward = forword;
    }

    public SlowDownEvent(float strafe, float forward) {
        this.strafe = strafe;
        this.forward = forward;
    }
}
