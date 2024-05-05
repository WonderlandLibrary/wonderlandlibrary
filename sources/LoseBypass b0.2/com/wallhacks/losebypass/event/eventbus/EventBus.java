/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.event.eventbus;

import com.wallhacks.losebypass.event.eventbus.Event;
import com.wallhacks.losebypass.event.eventbus.EventPriority;
import com.wallhacks.losebypass.event.eventbus.Listener;
import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventBus {
    private final CopyOnWriteArrayList<Listener> events = new CopyOnWriteArrayList();

    public final void register(Object object) throws IllegalArgumentException {
        this.getEvents(object);
    }

    public final void unregister(Object object) {
        this.events.removeIf(listener -> {
            if (listener.object != object) return false;
            return true;
        });
    }

    private void getEvents(Object object) {
        Class<?> clazz = object.getClass();
        Arrays.stream(clazz.getDeclaredMethods()).spliterator().forEachRemaining(method -> {
            if (!method.isAnnotationPresent(SubscribeEvent.class)) return;
            Class<?>[] prams = method.getParameterTypes();
            if (prams.length != 1) {
                throw new IllegalArgumentException("Method " + method + " doesnt have any event parameters");
            }
            if (!Event.class.isAssignableFrom(prams[0])) {
                throw new IllegalArgumentException("Method " + method + " doesnt have any event parameters only non event parameters");
            }
            this.events.add(new Listener((Method)method, object, prams[0], this.getPriority((Method)method)));
            this.events.sort(Comparator.comparing(o -> o.priority));
        });
    }

    public final boolean post(Event event) {
        this.events.spliterator().forEachRemaining(listener -> {
            if (listener.event != event.getClass()) return;
            listener.method.setAccessible(true);
            try {
                listener.method.invoke(listener.object, event);
                return;
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    private EventPriority getPriority(Method method) {
        return method.getAnnotation(SubscribeEvent.class).priority();
    }
}

