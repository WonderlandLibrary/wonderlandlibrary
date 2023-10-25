package fun.rich.client.event.events.impl.packet;

import fun.rich.client.event.events.Event;
import fun.rich.client.event.events.callables.EventCancellable;
public class EventMessage extends EventCancellable implements Event {

    public String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
