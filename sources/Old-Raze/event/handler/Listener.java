package markgg.event.handler;

import markgg.event.Event;

public interface Listener<T extends Event> {
    void call(final T event);
}
