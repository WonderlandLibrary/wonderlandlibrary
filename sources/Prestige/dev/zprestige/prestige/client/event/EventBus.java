package dev.zprestige.prestige.client.event;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventBus {
    private final CopyOnWriteArrayList<Listener> listeners;

    public EventBus() {
        listeners = new CopyOnWriteArrayList<>();
    }

    public void registerListener(final Object object) {
        listeners(object);
    }

    public void unregisterListener(final Object object) {
        listeners.removeIf(listener -> listener.getObject().equals(object));
    }

    private void listeners(final Object object) {
        final Class<?> c = object.getClass();
        Arrays.stream(c.getDeclaredMethods()).forEach(method -> {
            if (method.isAnnotationPresent(EventListener.class)) {
                final Class<?>[] parameterTypes = method.getParameterTypes();
                listeners.add(new Listener(method, object, parameterTypes[0], method.getAnnotation(EventListener.class).getPriority().getPriority()));
            }
        });
    }

    public boolean invoke(final Event event) {
        new CopyOnWriteArrayList<>(listeners).stream().filter(listener -> listener.getEvent().equals(event.getClass())).forEach(listener -> {
            try {
                listener.getMethod().invoke(listener.getObject(), event);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        return event.isCancelled();
    }
}