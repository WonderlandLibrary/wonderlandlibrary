package dev.tenacity.event;

import java.util.List;
import java.util.ArrayList;

import dev.tenacity.event.pool.EventSubscriberPool;

public final class EventBus {
        private List<IEventListener<?>> listeners = new ArrayList<>();

        public <T> void register(IEventListener<T> listener) {
            listeners.add(listener);
        }

    public <T> void dispatch(T event) {
            for (IEventListener<?> listener : listeners) {
                if (listener instanceof IEventListener<?>) {
                    ((IEventListener<T>) listener).invoke(event);
                }
            }
        }
    private final EventSubscriberPool eventSubscriberPool;

    public EventBus() {
        this.eventSubscriberPool = new EventSubscriberPool();
    }

    public void subscribe(final Object subscriber) {
        eventSubscriberPool.subscribe(subscriber);
    }

    public void unsubscribe(final Object subscriber) {
        eventSubscriberPool.unsubscribe(subscriber);
    }

    public void dispatch(final Event event) {
        eventSubscriberPool.dispatch(event);
    }

}
