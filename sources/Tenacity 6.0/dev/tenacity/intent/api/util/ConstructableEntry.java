// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.intent.api.util;

import java.util.Map;

public class ConstructableEntry<K, V> implements Map.Entry<K, V>
{
    private final K key;
    private V value;
    
    public ConstructableEntry(final K key, final V value) {
        this.key = key;
        this.value = value;
    }
    
    @Override
    public K getKey() {
        return this.key;
    }
    
    @Override
    public V getValue() {
        return this.value;
    }
    
    @Override
    public V setValue(final V value) {
        final V old = this.value;
        this.value = value;
        return old;
    }
}
