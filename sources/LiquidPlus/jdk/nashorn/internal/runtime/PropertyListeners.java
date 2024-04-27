/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.LongAdder;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

public class PropertyListeners {
    private Map<String, WeakPropertyMapSet> listeners;
    private static LongAdder listenersAdded;
    private static LongAdder listenersRemoved;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    PropertyListeners(PropertyListeners listener) {
        if (listener != null && listener.listeners != null) {
            this.listeners = new WeakHashMap<String, WeakPropertyMapSet>();
            PropertyListeners propertyListeners = listener;
            synchronized (propertyListeners) {
                for (Map.Entry<String, WeakPropertyMapSet> entry : listener.listeners.entrySet()) {
                    this.listeners.put(entry.getKey(), new WeakPropertyMapSet(entry.getValue()));
                }
            }
        }
    }

    public static long getListenersAdded() {
        return listenersAdded.longValue();
    }

    public static long getListenersRemoved() {
        return listenersRemoved.longValue();
    }

    public static int getListenerCount(ScriptObject obj) {
        return obj.getMap().getListenerCount();
    }

    public int getListenerCount() {
        return this.listeners == null ? 0 : this.listeners.size();
    }

    public static PropertyListeners addListener(PropertyListeners listeners, String key, PropertyMap propertyMap) {
        if (listeners == null || !listeners.containsListener(key, propertyMap)) {
            PropertyListeners newListeners = new PropertyListeners(listeners);
            newListeners.addListener(key, propertyMap);
            return newListeners;
        }
        return listeners;
    }

    synchronized boolean containsListener(String key, PropertyMap propertyMap) {
        if (this.listeners == null) {
            return false;
        }
        WeakPropertyMapSet set = this.listeners.get(key);
        return set != null && set.contains(propertyMap);
    }

    final synchronized void addListener(String key, PropertyMap propertyMap) {
        WeakPropertyMapSet set;
        if (Context.DEBUG) {
            listenersAdded.increment();
        }
        if (this.listeners == null) {
            this.listeners = new WeakHashMap<String, WeakPropertyMapSet>();
        }
        if ((set = this.listeners.get(key)) == null) {
            set = new WeakPropertyMapSet();
            this.listeners.put(key, set);
        }
        if (!set.contains(propertyMap)) {
            set.add(propertyMap);
        }
    }

    public synchronized void propertyAdded(Property prop) {
        WeakPropertyMapSet set;
        if (this.listeners != null && (set = this.listeners.get(prop.getKey())) != null) {
            for (PropertyMap propertyMap : set.elements()) {
                propertyMap.propertyAdded(prop, false);
            }
            this.listeners.remove(prop.getKey());
            if (Context.DEBUG) {
                listenersRemoved.increment();
            }
        }
    }

    public synchronized void propertyDeleted(Property prop) {
        WeakPropertyMapSet set;
        if (this.listeners != null && (set = this.listeners.get(prop.getKey())) != null) {
            for (PropertyMap propertyMap : set.elements()) {
                propertyMap.propertyDeleted(prop, false);
            }
            this.listeners.remove(prop.getKey());
            if (Context.DEBUG) {
                listenersRemoved.increment();
            }
        }
    }

    public synchronized void propertyModified(Property oldProp, Property newProp) {
        WeakPropertyMapSet set;
        if (this.listeners != null && (set = this.listeners.get(oldProp.getKey())) != null) {
            for (PropertyMap propertyMap : set.elements()) {
                propertyMap.propertyModified(oldProp, newProp, false);
            }
            this.listeners.remove(oldProp.getKey());
            if (Context.DEBUG) {
                listenersRemoved.increment();
            }
        }
    }

    public synchronized void protoChanged() {
        if (this.listeners != null) {
            for (WeakPropertyMapSet set : this.listeners.values()) {
                for (PropertyMap propertyMap : set.elements()) {
                    propertyMap.protoChanged(false);
                }
            }
            this.listeners.clear();
        }
    }

    static {
        if (Context.DEBUG) {
            listenersAdded = new LongAdder();
            listenersRemoved = new LongAdder();
        }
    }

    private static class WeakPropertyMapSet {
        private final WeakHashMap<PropertyMap, Boolean> map;

        WeakPropertyMapSet() {
            this.map = new WeakHashMap();
        }

        WeakPropertyMapSet(WeakPropertyMapSet set) {
            this.map = new WeakHashMap<PropertyMap, Boolean>(set.map);
        }

        void add(PropertyMap propertyMap) {
            this.map.put(propertyMap, Boolean.TRUE);
        }

        boolean contains(PropertyMap propertyMap) {
            return this.map.containsKey(propertyMap);
        }

        Set<PropertyMap> elements() {
            return this.map.keySet();
        }
    }
}

