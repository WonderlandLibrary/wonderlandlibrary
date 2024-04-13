package dev.tenacity.event.pool;

import dev.tenacity.event.Event;
import dev.tenacity.event.IEventListener;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventSubscriberPool {

    private final Map<Type, List<EventSubscriber<?>>> subscriberCacheMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public void subscribe(final Object subscriber) {
        for(final Field field : subscriber.getClass().getDeclaredFields()) {
            if(field.getType().isAssignableFrom(IEventListener.class)) {
                if(!field.isAccessible())
                    field.setAccessible(true);

                try {
                    final IEventListener<Event> listener = (IEventListener<Event>) field.get(subscriber);

                    if(listener != null) {
                        final Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        subscriberCacheMap.computeIfAbsent(type, list -> new CopyOnWriteArrayList<>()).add(new EventSubscriber<>(subscriber, listener));
                    }
                } catch (final IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void unsubscribe(final Object subscriber) {
        subscriberCacheMap.values().forEach(eventSubscriberList -> eventSubscriberList.removeIf(eventSubscriber ->
                eventSubscriber.getSubscriber().equals(subscriber)));
    }

    @SuppressWarnings("unchecked")
    public void dispatch(final Event event) {
        final List<EventSubscriber<?>> eventSubscriberList = subscriberCacheMap.get(event.getClass());

        if(eventSubscriberList != null && !eventSubscriberList.isEmpty()) {
            eventSubscriberList.forEach(eventSubscriber -> {
                final EventSubscriber<Event> subscriber = (EventSubscriber<Event>) eventSubscriber;

                subscriber.getEventListener().invoke(event);
            });
        }
    }

}
