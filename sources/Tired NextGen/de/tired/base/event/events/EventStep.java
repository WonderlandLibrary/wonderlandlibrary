package de.tired.base.event.events;

import de.tired.base.event.Event;

public class EventStep extends Event {
    float stepHeight;
    public EventStep(float stepHeight){
        this.stepHeight = stepHeight;
    }

    public float getStepHeight() {
        return stepHeight;
    }

    public void setStepHeight(float stepHeight) {
        this.stepHeight = stepHeight;
    }
}