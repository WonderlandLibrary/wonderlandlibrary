package client.event;

import client.Client;
import client.util.MinecraftInstance;
import client.util.liquidbounce.InventoryUtils;
import lombok.AllArgsConstructor;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public final class EventBus implements Bus<Event>, MinecraftInstance {

    private final Map<Type, List<CallSite<Event>>> callSiteMap;
    private final Map<Type, List<Listener<Event>>> listenerCache;

    public EventBus() {
        callSiteMap = new HashMap<>();
        listenerCache = new HashMap<>();
        register(new InventoryUtils());
    }

    @Override
    public void register(final Object subscriber) {
        try {
            for (final Field field : subscriber.getClass().getDeclaredFields()) {
                final EventLink annotation = field.getAnnotation(EventLink.class);
                if (annotation != null) {
                    final Type eventType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    if (!field.isAccessible())
                        field.setAccessible(true);
                    try {
                        final Listener<Event> listener = (Listener<Event>) MethodHandles.lookup().unreflectGetter(field).invokeWithArguments(subscriber);
                        final byte priority = annotation.value();
                        final List<CallSite<Event>> callSites;
                        final CallSite<Event> callSite = new CallSite<>(subscriber, listener, priority);
                        if (callSiteMap.containsKey(eventType)) {
                            callSites = callSiteMap.get(eventType);
                            callSites.add(callSite);
                            callSites.sort(Comparator.comparingInt(o -> o.priority));
                        } else {
                            callSites = new ArrayList<>(1);
                            callSites.add(callSite);
                            callSiteMap.put(eventType, callSites);
                        }
                    } catch (Throwable e) {
                        if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
                    }
                }
            }
            populateListenerCache();
        } catch (Exception e) {
            if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
        }
    }

    @Override
    public void unregister(final Object subscriber) {
        for (List<CallSite<Event>> callSites : callSiteMap.values()) callSites.removeIf(eventCallSite -> eventCallSite.owner == subscriber);
        populateListenerCache();
    }

    @Override
    public void handle(final Event event) {
        if (mc.theWorld == null) return;
        final List<Listener<Event>> listeners = listenerCache.getOrDefault(event.getClass(), Collections.emptyList());
        int i = 0;
        final int listenersSize = listeners.size();
        while (i < listenersSize) listeners.get(i++).call(event);
    }

    private void populateListenerCache() {
        final Map<Type, List<CallSite<Event>>> callSiteMap = this.callSiteMap;
        final Map<Type, List<Listener<Event>>> listenerCache = this.listenerCache;
        for (final Type type : callSiteMap.keySet()) {
            final List<CallSite<Event>> callSites = callSiteMap.get(type);
            final int size = callSites.size();
            final List<Listener<Event>> listeners = new ArrayList<>(size);
            for (CallSite<Event> callSite : callSites) listeners.add(callSite.listener);
            listenerCache.put(type, listeners);
        }
    }

    public void handle(final Event event, final Object... listeners) {
        int i = 0;
        final int listenersSize = listeners.length;
        final List<Object> list = Arrays.asList(listeners);
        while (i < listenersSize) ((Listener) list.get(i++)).call(event);
    }

    @AllArgsConstructor
    private static class CallSite<Event> {
        private final Object owner;
        private final Listener<Event> listener;
        private final byte priority;
    }
}
