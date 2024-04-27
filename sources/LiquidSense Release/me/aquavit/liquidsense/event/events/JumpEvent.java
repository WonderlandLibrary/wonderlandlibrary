package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.CancellableEvent;

public class JumpEvent extends CancellableEvent {
    private float motion;

    public JumpEvent(float motion) {
        this.motion = motion;
    }

    public void setMotion(float motion) {
        this.motion = motion;
    }

    public float getMotion() {
        return this.motion;
    }
}
