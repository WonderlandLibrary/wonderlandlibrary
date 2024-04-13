package com.canon.majik.api.event.eventBus;

import com.canon.majik.api.thread.ThreadedHandler;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private final CopyOnWriteArrayList<Listener> listeners;

    public EventBus() {
        listeners = new CopyOnWriteArrayList<>();
    }

    public void registerListener(Object object) {
        listeners(object);
    }

    public void unregisterListener(Object object) {
        listeners.removeIf(listener -> listener.getParent() == object);
    }

    private void listeners(Object object) {
        Class<?> c = object.getClass();
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                listeners.add(new Listener(method, object, parameterTypes[0], method.getAnnotation(EventListener.class).getPriority().getPriority()));
            }
        }
        ThreadedHandler.queue(() -> listeners.sort(Comparator.comparing(Listener::getPriority)));
    }

    public boolean invoke(Event event) {
        for (Listener listener : listeners) {
            if (listener.getEvent().equals(event.getClass())) {
                try {
                    listener.getMethod().invoke(listener.getParent(), event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return event.isCancelled();
    }
}
