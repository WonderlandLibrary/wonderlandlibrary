package tech.atani.client.listener.radbus;

import java.util.function.Consumer;

/**
 * @author nevalackin
 * @since 1.0.0
 */
public interface PubSub<Event> {

    static <Event> PubSub<Event> newInstance(Consumer<String> errorLogger) {
        return new PubSubImpl<>(errorLogger);
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