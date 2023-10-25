/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.serialization;

import java.lang.ref.Reference;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

abstract class ReferenceMap<K, V>
implements Map<K, V> {
    private final Map<K, Reference<V>> delegate;

    @Override
    public int size() {
        return this.delegate.size();
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    @Override
    public V get(Object object) {
        return this.unfold(this.delegate.get(object));
    }

    protected ReferenceMap(Map<K, Reference<V>> map) {
        this.delegate = map;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            this.delegate.put(entry.getKey(), this.fold(entry.getValue()));
        }
    }

    @Override
    public Set<K> keySet() {
        return this.delegate.keySet();
    }

    @Override
    public V put(K k, V v) {
        return this.unfold(this.delegate.put(k, this.fold(v)));
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    private V unfold(Reference<V> reference) {
        if (reference == null) {
            return null;
        }
        return reference.get();
    }

    @Override
    public boolean containsValue(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(Object object) {
        return this.unfold(this.delegate.remove(object));
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    abstract Reference<V> fold(V var1);

    @Override
    public boolean containsKey(Object object) {
        return this.delegate.containsKey(object);
    }

    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }
}

