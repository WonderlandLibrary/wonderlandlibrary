package graphite.api.event;

@FunctionalInterface
public interface Listener<Event> {
    void invoke(Event event);
}