// 
// Decompiled by Procyon v0.6.0
// 

package net.lenni0451.eventapi.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.lang.reflect.Method;
import net.lenni0451.eventapi.reflection.ReflectedEventListener;
import java.lang.annotation.Annotation;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.lenni0451.eventapi.listener.IEventListener;
import java.util.Iterator;
import net.lenni0451.eventapi.events.types.IStoppable;
import java.util.Collection;
import java.util.ArrayList;
import net.lenni0451.eventapi.listener.IErrorListener;
import java.util.List;
import net.lenni0451.eventapi.events.IEvent;
import java.util.Map;

public class EventManager
{
    private static final Map<Class<? extends IEvent>, List<EventExecutor>> EVENT_LISTENER;
    private static final List<IErrorListener> ERROR_LISTENER;
    
    public static void call(final IEvent event) {
        if (event != null) {
            final List<EventExecutor> eventListener = new ArrayList<EventExecutor>();
            if (EventManager.EVENT_LISTENER.containsKey(event.getClass())) {
                eventListener.addAll(EventManager.EVENT_LISTENER.get(event.getClass()));
            }
            if (EventManager.EVENT_LISTENER.containsKey(IEvent.class)) {
                eventListener.addAll(EventManager.EVENT_LISTENER.get(IEvent.class));
            }
            for (final EventExecutor listener : eventListener) {
                try {
                    listener.getEventListener().onEvent(event);
                }
                catch (final Throwable var5) {
                    if (EventManager.ERROR_LISTENER.isEmpty()) {
                        throw new RuntimeException(var5);
                    }
                    EventManager.ERROR_LISTENER.forEach(errorListener -> errorListener.catchException(var5));
                }
                if (event instanceof IStoppable && ((IStoppable)event).isStopped()) {
                    break;
                }
            }
        }
    }
    
    public static <T extends IEventListener> void register(final T listener) {
        register(IEvent.class, (byte)2, listener);
    }
    
    public static void register(final Object listener) {
        final Method[] methods;
        final Method[] var4 = methods = listener.getClass().getMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(EventTarget.class)) {
                final EventTarget anno = method.getAnnotation(EventTarget.class);
                final Class[] methodArguments = method.getParameterTypes();
                if (methodArguments.length == 1 && IEvent.class.isAssignableFrom(methodArguments[0])) {
                    final ReflectedEventListener eventListener = new ReflectedEventListener(listener, methodArguments[0], method);
                    if (methodArguments[0].equals(IEvent.class)) {
                        register(anno.priority(), eventListener);
                    }
                    else {
                        register(methodArguments[0], anno.priority(), eventListener);
                    }
                }
            }
        }
    }
    
    public static <T extends IEventListener> void register(final Class<? extends IEvent> eventType, final T listener) {
        register(eventType, (byte)2, listener);
    }
    
    public static <T extends IEventListener> void register(final byte eventPriority, final T listener) {
        register(IEvent.class, eventPriority, listener);
    }
    
    public static <T extends IEventListener> void register(final Class<? extends IEvent> eventType, final byte eventPriority, final T listener) {
        final List<EventExecutor> eventListener = EventManager.EVENT_LISTENER.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList());
        eventListener.add(new EventExecutor(listener, eventPriority));
        for (final Map.Entry<Class<? extends IEvent>, List<EventExecutor>> entry : EventManager.EVENT_LISTENER.entrySet()) {
            final List<EventExecutor> eventExecutor = entry.getValue();
            Collections.sort(eventExecutor, new Comparator<EventExecutor>() {
                @Override
                public int compare(final EventExecutor o1, final EventExecutor o2) {
                    return Integer.compare(o2.getPriority(), o1.getPriority());
                }
            });
        }
    }
    
    public static void unregister(final Object listener) {
        for (final Map.Entry<Class<? extends IEvent>, List<EventExecutor>> entry : EventManager.EVENT_LISTENER.entrySet()) {
            entry.getValue().removeIf(eventExecutor -> eventExecutor.getEventListener().equals(listener) || (eventExecutor.getEventListener() instanceof ReflectedEventListener && ((ReflectedEventListener)eventExecutor.getEventListener()).getCallInstance().equals(listener)));
        }
    }
    
    public static void addErrorListener(final IErrorListener errorListener) {
        if (!EventManager.ERROR_LISTENER.contains(errorListener)) {
            EventManager.ERROR_LISTENER.add(errorListener);
        }
    }
    
    public static boolean removeErrorListener(final IErrorListener errorListener) {
        return EventManager.ERROR_LISTENER.remove(errorListener);
    }
    
    static {
        EVENT_LISTENER = new ConcurrentHashMap<Class<? extends IEvent>, List<EventExecutor>>();
        ERROR_LISTENER = new CopyOnWriteArrayList<IErrorListener>();
    }
}
