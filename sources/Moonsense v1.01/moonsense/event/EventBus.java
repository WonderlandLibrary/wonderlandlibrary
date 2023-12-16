// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

public class EventBus
{
    private static final Map<Class<? extends SCEvent>, ArrayList<EventData>> REGISTRY_MAP;
    
    static {
        REGISTRY_MAP = new HashMap<Class<? extends SCEvent>, ArrayList<EventData>>();
    }
    
    private static void sortListValue(final Class<? extends SCEvent> clazz) {
        final ArrayList<EventData> flexableArray = new ArrayList<EventData>();
        byte[] priorities;
        for (int length = (priorities = EventPriority.PRIORITIES).length, i = 0; i < length; ++i) {
            final byte b = priorities[i];
            for (final EventData methodData : EventBus.REGISTRY_MAP.get(clazz)) {
                if (methodData.priority == b) {
                    flexableArray.add(methodData);
                }
            }
        }
        EventBus.REGISTRY_MAP.put(clazz, flexableArray);
    }
    
    private static boolean isMethodBad(final Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(SubscribeEvent.class);
    }
    
    private static boolean isMethodBad(final Method method, final Class<? extends SCEvent> clazz) {
        return isMethodBad(method) || method.getParameterTypes()[0].equals(clazz);
    }
    
    public static ArrayList<EventData> get(final Class<? extends SCEvent> clazz) {
        return EventBus.REGISTRY_MAP.get(clazz);
    }
    
    public static void cleanMap(final boolean removeOnlyEmptyValues) {
        final Iterator<Map.Entry<Class<? extends SCEvent>, ArrayList<EventData>>> iterator = EventBus.REGISTRY_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            if (!removeOnlyEmptyValues || iterator.next().getValue().isEmpty()) {
                iterator.remove();
            }
        }
    }
    
    public static void unregister(final Object o, final Class<? extends SCEvent> clazz) {
        if (EventBus.REGISTRY_MAP.containsKey(clazz)) {
            for (final EventData methodData : EventBus.REGISTRY_MAP.get(clazz)) {
                if (methodData.source.equals(o)) {
                    EventBus.REGISTRY_MAP.get(clazz).remove(methodData);
                }
            }
        }
        cleanMap(true);
    }
    
    public static void unregister(final Object o) {
        for (final ArrayList<EventData> flexableArray : EventBus.REGISTRY_MAP.values()) {
            for (int i = flexableArray.size() - 1; i >= 0; --i) {
                if (flexableArray.get(i).source.equals(o)) {
                    flexableArray.remove(i);
                }
            }
        }
        cleanMap(true);
    }
    
    public static void register(final Method method, final Object o) {
        final Class<?> clazz = method.getParameterTypes()[0];
        final EventData methodData = new EventData(o, method, method.getAnnotation(SubscribeEvent.class).value());
        if (!methodData.target.isAccessible()) {
            methodData.target.setAccessible(true);
        }
        if (EventBus.REGISTRY_MAP.containsKey(clazz)) {
            if (!EventBus.REGISTRY_MAP.get(clazz).contains(methodData)) {
                EventBus.REGISTRY_MAP.get(clazz).add(methodData);
                sortListValue((Class<? extends SCEvent>)clazz);
            }
        }
        else {
            EventBus.REGISTRY_MAP.put((Class<? extends SCEvent>)clazz, new ArrayList<EventData>(methodData) {
                {
                    this.add(e);
                }
            });
        }
    }
    
    public static void register(final Object o, final Class<? extends SCEvent> clazz) {
        Method[] methods;
        for (int length = (methods = o.getClass().getMethods()).length, i = 0; i < length; ++i) {
            final Method method = methods[i];
            if (!isMethodBad(method, clazz)) {
                register(method, o);
            }
        }
    }
    
    public static void register(final Object o) {
        Method[] methods;
        for (int length = (methods = o.getClass().getMethods()).length, i = 0; i < length; ++i) {
            final Method method = methods[i];
            if (!isMethodBad(method)) {
                register(method, o);
            }
        }
    }
}
