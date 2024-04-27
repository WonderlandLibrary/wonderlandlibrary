package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;

public class StepConfirmEvent extends Event {
    private float stepHeight;

    public float getStepHeight() {
        return this.stepHeight;
    }

    public void setStepHeight(float stepHeight) {
        this.stepHeight = stepHeight;
    }

    public StepConfirmEvent(float stepHeight) {
        this.stepHeight = stepHeight;
    }
}
