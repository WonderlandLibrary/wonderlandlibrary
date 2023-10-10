// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import java.util.Objects;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.StringMap;

class CopyOnWriteSortedArrayThreadContextMap implements ReadOnlyThreadContextMap, ObjectThreadContextMap, CopyOnWrite
{
    public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
    protected static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected static final String PROPERTY_NAME_INITIAL_CAPACITY = "log4j2.ThreadContext.initial.capacity";
    private static final StringMap EMPTY_CONTEXT_DATA;
    private static volatile int initialCapacity;
    private static volatile boolean inheritableMap;
    private final ThreadLocal<StringMap> localMap;
    
    static void init() {
        final PropertiesUtil properties = PropertiesUtil.getProperties();
        CopyOnWriteSortedArrayThreadContextMap.initialCapacity = properties.getIntegerProperty("log4j2.ThreadContext.initial.capacity", 16);
        CopyOnWriteSortedArrayThreadContextMap.inheritableMap = properties.getBooleanProperty("isThreadContextMapInheritable");
    }
    
    public CopyOnWriteSortedArrayThreadContextMap() {
        this.localMap = this.createThreadLocalMap();
    }
    
    private ThreadLocal<StringMap> createThreadLocalMap() {
        if (CopyOnWriteSortedArrayThreadContextMap.inheritableMap) {
            return new InheritableThreadLocal<StringMap>() {
                @Override
                protected StringMap childValue(final StringMap parentValue) {
                    if (parentValue == null) {
                        return null;
                    }
                    final StringMap stringMap = CopyOnWriteSortedArrayThreadContextMap.this.createStringMap(parentValue);
                    stringMap.freeze();
                    return stringMap;
                }
            };
        }
        return new ThreadLocal<StringMap>();
    }
    
    protected StringMap createStringMap() {
        return new SortedArrayStringMap(CopyOnWriteSortedArrayThreadContextMap.initialCapacity);
    }
    
    protected StringMap createStringMap(final ReadOnlyStringMap original) {
        return new SortedArrayStringMap(original);
    }
    
    @Override
    public void put(final String key, final String value) {
        this.putValue(key, value);
    }
    
    @Override
    public void putValue(final String key, final Object value) {
        StringMap map = this.localMap.get();
        map = ((map == null) ? this.createStringMap() : this.createStringMap(map));
        map.putValue(key, value);
        map.freeze();
        this.localMap.set(map);
    }
    
    @Override
    public void putAll(final Map<String, String> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        StringMap map = this.localMap.get();
        map = ((map == null) ? this.createStringMap() : this.createStringMap(map));
        for (final Map.Entry<String, String> entry : values.entrySet()) {
            map.putValue(entry.getKey(), entry.getValue());
        }
        map.freeze();
        this.localMap.set(map);
    }
    
    @Override
    public <V> void putAllValues(final Map<String, V> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        StringMap map = this.localMap.get();
        map = ((map == null) ? this.createStringMap() : this.createStringMap(map));
        for (final Map.Entry<String, V> entry : values.entrySet()) {
            map.putValue(entry.getKey(), entry.getValue());
        }
        map.freeze();
        this.localMap.set(map);
    }
    
    @Override
    public String get(final String key) {
        return this.getValue(key);
    }
    
    @Override
    public <V> V getValue(final String key) {
        final StringMap map = this.localMap.get();
        return (map == null) ? null : map.getValue(key);
    }
    
    @Override
    public void remove(final String key) {
        final StringMap map = this.localMap.get();
        if (map != null) {
            final StringMap copy = this.createStringMap(map);
            copy.remove(key);
            copy.freeze();
            this.localMap.set(copy);
        }
    }
    
    @Override
    public void removeAll(final Iterable<String> keys) {
        final StringMap map = this.localMap.get();
        if (map != null) {
            final StringMap copy = this.createStringMap(map);
            for (final String key : keys) {
                copy.remove(key);
            }
            copy.freeze();
            this.localMap.set(copy);
        }
    }
    
    @Override
    public void clear() {
        this.localMap.remove();
    }
    
    @Override
    public boolean containsKey(final String key) {
        final StringMap map = this.localMap.get();
        return map != null && map.containsKey(key);
    }
    
    @Override
    public Map<String, String> getCopy() {
        final StringMap map = this.localMap.get();
        return (map == null) ? new HashMap<String, String>() : map.toMap();
    }
    
    @Override
    public StringMap getReadOnlyContextData() {
        final StringMap map = this.localMap.get();
        return (map == null) ? CopyOnWriteSortedArrayThreadContextMap.EMPTY_CONTEXT_DATA : map;
    }
    
    @Override
    public Map<String, String> getImmutableMapOrNull() {
        final StringMap map = this.localMap.get();
        return (map == null) ? null : Collections.unmodifiableMap((Map<? extends String, ? extends String>)map.toMap());
    }
    
    @Override
    public boolean isEmpty() {
        final StringMap map = this.localMap.get();
        return map == null || map.isEmpty();
    }
    
    @Override
    public String toString() {
        final StringMap map = this.localMap.get();
        return (map == null) ? "{}" : map.toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        final StringMap map = this.localMap.get();
        result = 31 * result + ((map == null) ? 0 : map.hashCode());
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
        if (!(obj instanceof ThreadContextMap)) {
            return false;
        }
        final ThreadContextMap other = (ThreadContextMap)obj;
        final Map<String, String> map = this.getImmutableMapOrNull();
        final Map<String, String> otherMap = other.getImmutableMapOrNull();
        return Objects.equals(map, otherMap);
    }
    
    static {
        (EMPTY_CONTEXT_DATA = new SortedArrayStringMap(1)).freeze();
        init();
    }
}
