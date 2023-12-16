package wtf.expensive.events.impl.player;

import wtf.expensive.events.Event;

public class EventStep extends Event {

    public float stepHeight;

    public EventStep(float stepHeight) {
        this.stepHeight = stepHeight;
    }

}
