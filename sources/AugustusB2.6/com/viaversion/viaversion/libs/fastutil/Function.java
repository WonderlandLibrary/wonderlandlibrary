// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil;

@FunctionalInterface
public interface Function<K, V> extends java.util.function.Function<K, V>
{
    default V apply(final K key) {
        return this.get(key);
    }
    
    default V put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    V get(final Object p0);
    
    default V getOrDefault(final Object key, final V defaultValue) {
        final V value = this.get(key);
        return (value != null || this.containsKey(key)) ? value : defaultValue;
    }
    
    default boolean containsKey(final Object key) {
        return true;
    }
    
    default V remove(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    default int size() {
        return -1;
    }
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
}
