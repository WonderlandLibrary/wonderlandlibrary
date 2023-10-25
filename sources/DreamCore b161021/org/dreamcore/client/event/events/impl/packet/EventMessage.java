package org.dreamcore.client.event.events.impl.packet;

import org.dreamcore.client.event.events.Event;
import org.dreamcore.client.event.events.callables.EventCancellable;

public class EventMessage extends EventCancellable implements Event {

    public String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
