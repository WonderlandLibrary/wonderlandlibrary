package de.tired.base.event.events;

import de.tired.base.event.Event;

public class SlowDownEvent extends Event {

    float strafeMultiplier, forwardMultiplier;

    public SlowDownEvent(float strafeMultiplier, float forwardMultiplier) {
        this.strafeMultiplier = strafeMultiplier;
        this.forwardMultiplier = forwardMultiplier;
    }

    public float getStrafeMultiplier() {
        return strafeMultiplier;
    }

    public void setStrafeMultiplier(float strafeMultiplier) {
        this.strafeMultiplier = strafeMultiplier;
    }

    public float getForwardMultiplier() {
        return forwardMultiplier;
    }

    public void setForwardMultiplier(float forwardMultiplier) {
        this.forwardMultiplier = forwardMultiplier;
    }
}
