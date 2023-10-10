// 
// Decompiled by Procyon v0.5.36
// 

package net.lenni0451.eventapi.manager;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import net.lenni0451.eventapi.listener.IEventListener;
import java.util.List;
import net.lenni0451.eventapi.events.IEvent;
import java.util.Map;

public class MinimalEventManager
{
    private static final Map<Class<? extends IEvent>, List<IEventListener>> EVENT_LISTENER;
    
    static {
        EVENT_LISTENER = new ConcurrentHashMap<Class<? extends IEvent>, List<IEventListener>>();
    }
    
    public static void call(final IEvent event) {
        if (MinimalEventManager.EVENT_LISTENER.containsKey(event.getClass())) {
            MinimalEventManager.EVENT_LISTENER.get(event.getClass()).forEach(l -> {
                try {
                    l.onEvent(event);
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            });
        }
    }
    
    public static <T extends IEventListener> void register(final Class<? extends IEvent> eventType, final T listener) {
        MinimalEventManager.EVENT_LISTENER.computeIfAbsent(eventType, c -> new CopyOnWriteArrayList()).add(listener);
    }
    
    public static <T extends IEventListener> void unregister(final T listener) {
        MinimalEventManager.EVENT_LISTENER.entrySet().forEach(entry -> entry.getValue().removeIf(l -> l.equals(listener)));
    }
}
