/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.Event;
import org.celestial.client.event.events.EventStoppable;
import org.celestial.client.event.types.Priority;

public class EventManager {
    private static final Map<Class<? extends Event>, List<MethodData>> REGISTRY_MAP = new HashMap<Class<? extends Event>, List<MethodData>>();

    public static void register(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (EventManager.isMethodBad(method)) continue;
            EventManager.register(method, object);
        }
    }

    public static void unregister(Object object) {
        for (List<MethodData> dataList : REGISTRY_MAP.values()) {
            dataList.removeIf(data -> data.getSource().equals(object));
        }
        EventManager.cleanMap(true);
    }

    private static void register(Method method, Object object) {
        Class<?> indexClass = method.getParameterTypes()[0];
        final MethodData data = new MethodData(object, method, method.getAnnotation(EventTarget.class).value());
        if (!data.getTarget().isAccessible()) {
            data.getTarget().setAccessible(true);
        }
        if (REGISTRY_MAP.containsKey(indexClass)) {
            if (!REGISTRY_MAP.get(indexClass).contains(data)) {
                REGISTRY_MAP.get(indexClass).add(data);
                EventManager.sortListValue(indexClass);
            }
        } else {
            REGISTRY_MAP.put(indexClass, (List<MethodData>)new CopyOnWriteArrayList<MethodData>(){
                private static final long serialVersionUID = 666L;
                {
                    this.add(data);
                }
            });
        }
    }

    public static void cleanMap(boolean onlyEmptyEntries) {
        Iterator<Map.Entry<Class<? extends Event>, List<MethodData>>> mapIterator = REGISTRY_MAP.entrySet().iterator();
        while (mapIterator.hasNext()) {
            if (onlyEmptyEntries && !mapIterator.next().getValue().isEmpty()) continue;
            mapIterator.remove();
        }
    }

    private static void sortListValue(Class<? extends Event> indexClass) {
        CopyOnWriteArrayList<MethodData> sortedList = new CopyOnWriteArrayList<MethodData>();
        for (byte priority : Priority.VALUE_ARRAY) {
            for (MethodData data : REGISTRY_MAP.get(indexClass)) {
                if (data.getPriority() != priority) continue;
                sortedList.add(data);
            }
        }
        REGISTRY_MAP.put(indexClass, sortedList);
    }

    private static boolean isMethodBad(Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
    }

    private static boolean isMethodBad(Method method, Class<? extends Event> eventClass) {
        return EventManager.isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
    }

    public static Event call(Event event) {
        block4: {
            List<MethodData> dataList = REGISTRY_MAP.get(event.getClass());
            if (dataList == null) break block4;
            if (event instanceof EventStoppable) {
                EventStoppable stoppable = (EventStoppable)event;
                for (MethodData data : dataList) {
                    EventManager.invoke(data, event);
                    if (!stoppable.isStopped()) continue;
                    break;
                }
            } else {
                for (MethodData data : dataList) {
                    EventManager.invoke(data, event);
                }
            }
        }
        return event;
    }

    private static void invoke(MethodData data, Event argument) {
        try {
            data.getTarget().invoke(data.getSource(), argument);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            // empty catch block
        }
    }

    private static final class MethodData {
        private final Object source;
        private final Method target;
        private final byte priority;

        public MethodData(Object source, Method target, byte priority) {
            this.source = source;
            this.target = target;
            this.priority = priority;
        }

        public Object getSource() {
            return this.source;
        }

        public Method getTarget() {
            return this.target;
        }

        public byte getPriority() {
            return this.priority;
        }
    }
}

