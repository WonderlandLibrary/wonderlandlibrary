package graphite.api.event;

public interface PubSub<Event> {

    static <Event> PubSub<Event> newInstance() {
        return new PubSubImpl<>();
    }

    void subscribe(Object subscriber);

    void unsubscribe(Object subscriber);

    <T extends Event> void subscribe(
            Class<T> event,
            Listener<T> listener
    );

    void publish(Event event);

    void clear();
}