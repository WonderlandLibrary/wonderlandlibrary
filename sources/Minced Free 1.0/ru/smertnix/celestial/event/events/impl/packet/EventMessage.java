package ru.smertnix.celestial.event.events.impl.packet;

import ru.smertnix.celestial.event.events.Event;
import ru.smertnix.celestial.event.events.callables.EventCancellable;
public class EventMessage extends EventCancellable implements Event {

    public String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
