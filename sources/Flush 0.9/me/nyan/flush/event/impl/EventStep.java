package me.nyan.flush.event.impl;

import me.nyan.flush.event.Event;

public class EventStep extends Event {
    private float stepHeight;

    public EventStep(float stepHeight) {
        this.stepHeight = stepHeight;
    }

    public float getStepHeight() {
        return stepHeight;
    }

    public void setStepHeight(float stepHeight) {
        this.stepHeight = stepHeight;
    }
}
