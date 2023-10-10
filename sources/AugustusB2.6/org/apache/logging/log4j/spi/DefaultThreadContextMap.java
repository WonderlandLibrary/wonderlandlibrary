// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import java.util.Objects;
import org.apache.logging.log4j.util.TriConsumer;
import org.apache.logging.log4j.util.BiConsumer;
import java.util.Iterator;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

public class DefaultThreadContextMap implements ThreadContextMap, ReadOnlyStringMap
{
    private static final long serialVersionUID = 8218007901108944053L;
    public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
    private final boolean useMap;
    private final ThreadLocal<Map<String, String>> localMap;
    private static boolean inheritableMap;
    
    static ThreadLocal<Map<String, String>> createThreadLocalMap(final boolean isMapEnabled) {
        if (DefaultThreadContextMap.inheritableMap) {
            return new InheritableThreadLocal<Map<String, String>>() {
                @Override
                protected Map<String, String> childValue(final Map<String, String> parentValue) {
                    return (parentValue != null && isMapEnabled) ? Collections.unmodifiableMap((Map<? extends String, ? extends String>)new HashMap<String, String>(parentValue)) : null;
                }
            };
        }
        return new ThreadLocal<Map<String, String>>();
    }
    
    static void init() {
        DefaultThreadContextMap.inheritableMap = PropertiesUtil.getProperties().getBooleanProperty("isThreadContextMapInheritable");
    }
    
    public DefaultThreadContextMap() {
        this(true);
    }
    
    public DefaultThreadContextMap(final boolean useMap) {
        this.useMap = useMap;
        this.localMap = createThreadLocalMap(useMap);
    }
    
    @Override
    public void put(final String key, final String value) {
        if (!this.useMap) {
            return;
        }
        Map<String, String> map = this.localMap.get();
        map = ((map == null) ? new HashMap<String, String>(1) : new HashMap<String, String>(map));
        map.put(key, value);
        this.localMap.set(Collections.unmodifiableMap((Map<? extends String, ? extends String>)map));
    }
    
    public void putAll(final Map<String, String> m) {
        if (!this.useMap) {
            return;
        }
        Map<String, String> map = this.localMap.get();
        map = ((map == null) ? new HashMap<String, String>(m.size()) : new HashMap<String, String>(map));
        for (final Map.Entry<String, String> e : m.entrySet()) {
            map.put(e.getKey(), e.getValue());
        }
        this.localMap.set(Collections.unmodifiableMap((Map<? extends String, ? extends String>)map));
    }
    
    @Override
    public String get(final String key) {
        final Map<String, String> map = this.localMap.get();
        return (map == null) ? null : map.get(key);
    }
    
    @Override
    public void remove(final String key) {
        final Map<String, String> map = this.localMap.get();
        if (map != null) {
            final Map<String, String> copy = new HashMap<String, String>(map);
            copy.remove(key);
            this.localMap.set(Collections.unmodifiableMap((Map<? extends String, ? extends String>)copy));
        }
    }
    
    public void removeAll(final Iterable<String> keys) {
        final Map<String, String> map = this.localMap.get();
        if (map != null) {
            final Map<String, String> copy = new HashMap<String, String>(map);
            for (final String key : keys) {
                copy.remove(key);
            }
            this.localMap.set(Collections.unmodifiableMap((Map<? extends String, ? extends String>)copy));
        }
    }
    
    @Override
    public void clear() {
        this.localMap.remove();
    }
    
    @Override
    public Map<String, String> toMap() {
        return this.getCopy();
    }
    
    @Override
    public boolean containsKey(final String key) {
        final Map<String, String> map = this.localMap.get();
        return map != null && map.containsKey(key);
    }
    
    @Override
    public <V> void forEach(final BiConsumer<String, ? super V> action) {
        final Map<String, String> map = this.localMap.get();
        if (map == null) {
            return;
        }
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            final V value = (V)entry.getValue();
            action.accept(entry.getKey(), (Object)value);
        }
    }
    
    @Override
    public <V, S> void forEach(final TriConsumer<String, ? super V, S> action, final S state) {
        final Map<String, String> map = this.localMap.get();
        if (map == null) {
            return;
        }
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            final V value = (V)entry.getValue();
            action.accept(entry.getKey(), (Object)value, state);
        }
    }
    
    @Override
    public <V> V getValue(final String key) {
        final Map<String, String> map = this.localMap.get();
        return (V)((map == null) ? null : map.get(key));
    }
    
    @Override
    public Map<String, String> getCopy() {
        final Map<String, String> map = this.localMap.get();
        return (map == null) ? new HashMap<String, String>() : new HashMap<String, String>(map);
    }
    
    @Override
    public Map<String, String> getImmutableMapOrNull() {
        return this.localMap.get();
    }
    
    @Override
    public boolean isEmpty() {
        final Map<String, String> map = this.localMap.get();
        return map == null || map.isEmpty();
    }
    
    @Override
    public int size() {
        final Map<String, String> map = this.localMap.get();
        return (map == null) ? 0 : map.size();
    }
    
    @Override
    public String toString() {
        final Map<String, String> map = this.localMap.get();
        return (map == null) ? "{}" : map.toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        final Map<String, String> map = this.localMap.get();
        result = 31 * result + ((map == null) ? 0 : map.hashCode());
        result = 31 * result + Boolean.valueOf(this.useMap).hashCode();
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof DefaultThreadContextMap) {
            final DefaultThreadContextMap other = (DefaultThreadContextMap)obj;
            if (this.useMap != other.useMap) {
                return false;
            }
        }
        if (!(obj instanceof ThreadContextMap)) {
            return false;
        }
        final ThreadContextMap other2 = (ThreadContextMap)obj;
        final Map<String, String> map = this.localMap.get();
        final Map<String, String> otherMap = other2.getImmutableMapOrNull();
        return Objects.equals(map, otherMap);
    }
    
    static {
        init();
    }
}
