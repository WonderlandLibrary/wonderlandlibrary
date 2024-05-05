package client.event;

public interface Bus<Event> {
    void register(final Object subscriber);
    void unregister(final Object subscriber);
    void handle(final Event event);
}
