package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;
import me.aquavit.liquidsense.event.EventState;

public class MotionEvent extends Event {
    private EventState eventState;

    public MotionEvent(EventState eventState) {
        this.eventState = eventState;
    }

    public EventState getEventState() {
        return this.eventState;
    }
}
