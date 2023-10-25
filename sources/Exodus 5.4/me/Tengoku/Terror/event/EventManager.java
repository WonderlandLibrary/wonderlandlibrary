/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import me.Tengoku.Terror.event.ArrayHelper;
import me.Tengoku.Terror.event.Data;
import me.Tengoku.Terror.event.Event;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.Priority;

public class EventManager {
    private static final Map<Class<? extends Event>, ArrayHelper<Data>> REGISTRY_MAP = new HashMap<Class<? extends Event>, ArrayHelper<Data>>();

    public static void cleanMap(boolean bl) {
        Iterator<Map.Entry<Class<? extends Event>, ArrayHelper<Data>>> iterator = REGISTRY_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            if (bl && !iterator.next().getValue().isEmpty()) continue;
            iterator.remove();
        }
    }

    public static void register(Object object, Class<? extends Event> clazz) {
        Method[] methodArray = object.getClass().getDeclaredMethods();
        int n = methodArray.length;
        int n2 = 0;
        while (n2 < n) {
            Method method = methodArray[n2];
            if (!EventManager.isMethodBad(method, clazz)) {
                EventManager.register(method, object);
            }
            ++n2;
        }
    }

    public static void unregister(Object object) {
        for (ArrayHelper<Data> arrayHelper : REGISTRY_MAP.values()) {
            for (Data data : arrayHelper) {
                if (!data.source.equals(object)) continue;
                arrayHelper.remove(data);
            }
        }
        EventManager.cleanMap(true);
    }

    private static boolean isMethodBad(Method method, Class<? extends Event> clazz) {
        return EventManager.isMethodBad(method) || method.getParameterTypes()[0].equals(clazz);
    }

    private static void register(Method method, Object object) {
        Class<?> clazz = method.getParameterTypes()[0];
        Data data = new Data(object, method, method.getAnnotation(EventTarget.class).value());
        if (!data.target.isAccessible()) {
            data.target.setAccessible(true);
        }
        if (REGISTRY_MAP.containsKey(clazz)) {
            if (!REGISTRY_MAP.get(clazz).contains(data)) {
                REGISTRY_MAP.get(clazz).add(data);
                EventManager.sortListValue(clazz);
            }
        } else {
            REGISTRY_MAP.put(clazz, new ArrayHelper<Data>(data){
                {
                    this.add(data);
                }
            });
        }
    }

    public static void removeEnty(Class<? extends Event> clazz) {
        Iterator<Map.Entry<Class<? extends Event>, ArrayHelper<Data>>> iterator = REGISTRY_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().getKey().equals(clazz)) continue;
            iterator.remove();
            break;
        }
    }

    public static void register(Object object) {
        Method[] methodArray = object.getClass().getDeclaredMethods();
        int n = methodArray.length;
        int n2 = 0;
        while (n2 < n) {
            Method method = methodArray[n2];
            if (!EventManager.isMethodBad(method)) {
                EventManager.register(method, object);
            }
            ++n2;
        }
    }

    private static boolean isMethodBad(Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
    }

    private static void sortListValue(Class<? extends Event> clazz) {
        ArrayHelper<Data> arrayHelper = new ArrayHelper<Data>();
        byte[] byArray = Priority.VALUE_ARRAY;
        int n = Priority.VALUE_ARRAY.length;
        int n2 = 0;
        while (n2 < n) {
            byte by = byArray[n2];
            for (Data data : REGISTRY_MAP.get(clazz)) {
                if (data.priority != by) continue;
                arrayHelper.add(data);
            }
            ++n2;
        }
        REGISTRY_MAP.put(clazz, arrayHelper);
    }

    public static void shutdown() {
        REGISTRY_MAP.clear();
    }

    public static ArrayHelper<Data> get(Class<? extends Event> clazz) {
        return REGISTRY_MAP.get(clazz);
    }

    public static void unregister(Object object, Class<? extends Event> clazz) {
        if (REGISTRY_MAP.containsKey(clazz)) {
            for (Data data : REGISTRY_MAP.get(clazz)) {
                if (!data.source.equals(object)) continue;
                REGISTRY_MAP.get(clazz).remove(data);
            }
            EventManager.cleanMap(true);
        }
    }
}

