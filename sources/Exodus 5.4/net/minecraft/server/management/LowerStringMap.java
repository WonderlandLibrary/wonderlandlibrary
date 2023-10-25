/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.server.management;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class LowerStringMap<V>
implements Map<String, V> {
    private final Map<String, V> internalMap = Maps.newLinkedHashMap();

    @Override
    public V put(String string, V v) {
        return this.internalMap.put(string.toLowerCase(), v);
    }

    @Override
    public Set<String> keySet() {
        return this.internalMap.keySet();
    }

    @Override
    public V remove(Object object) {
        return this.internalMap.remove(object.toString().toLowerCase());
    }

    @Override
    public V get(Object object) {
        return this.internalMap.get(object.toString().toLowerCase());
    }

    @Override
    public Set<Map.Entry<String, V>> entrySet() {
        return this.internalMap.entrySet();
    }

    @Override
    public void clear() {
        this.internalMap.clear();
    }

    @Override
    public boolean containsValue(Object object) {
        return this.internalMap.containsKey(object);
    }

    @Override
    public int size() {
        return this.internalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.internalMap.isEmpty();
    }

    @Override
    public Collection<V> values() {
        return this.internalMap.values();
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> map) {
        for (Map.Entry<String, V> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean containsKey(Object object) {
        return this.internalMap.containsKey(object.toString().toLowerCase());
    }
}

