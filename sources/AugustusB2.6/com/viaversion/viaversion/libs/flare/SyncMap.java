// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.flare;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

public interface SyncMap<K, V> extends ConcurrentMap<K, V>
{
    default <K, V> SyncMap<K, V> hashmap() {
        return of((IntFunction<Map<K, ExpungingEntry<V>>>)HashMap::new, 16);
    }
    
    default <K, V> SyncMap<K, V> hashmap(final int initialCapacity) {
        return of((IntFunction<Map<K, ExpungingEntry<V>>>)HashMap::new, initialCapacity);
    }
    
    default <K> Set<K> hashset() {
        return setOf((IntFunction<Map<K, ExpungingEntry<Boolean>>>)HashMap::new, 16);
    }
    
    default <K> Set<K> hashset(final int initialCapacity) {
        return setOf((IntFunction<Map<K, ExpungingEntry<Boolean>>>)HashMap::new, initialCapacity);
    }
    
    default <K, V> SyncMap<K, V> of(final IntFunction<Map<K, ExpungingEntry<V>>> function, final int initialCapacity) {
        return new SyncMapImpl<K, V>(function, initialCapacity);
    }
    
    default <K> Set<K> setOf(final IntFunction<Map<K, ExpungingEntry<Boolean>>> function, final int initialCapacity) {
        return Collections.newSetFromMap(new SyncMapImpl<K, Boolean>(function, initialCapacity));
    }
    
    Set<Map.Entry<K, V>> entrySet();
    
    int size();
    
    void clear();
    
    public interface InsertionResult<V>
    {
        byte operation();
        
        V previous();
        
        V current();
    }
    
    public interface ExpungingEntry<V>
    {
        boolean exists();
        
        V get();
        
        V getOr(final V other);
        
        InsertionResult<V> setIfAbsent(final V value);
        
         <K> InsertionResult<V> computeIfAbsent(final K key, final Function<? super K, ? extends V> function);
        
         <K> InsertionResult<V> computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction);
        
         <K> InsertionResult<V> compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction);
        
        void set(final V value);
        
        boolean replace(final Object compare, final V value);
        
        V clear();
        
        boolean trySet(final V value);
        
        V tryReplace(final V value);
        
        boolean tryExpunge();
        
        boolean tryUnexpungeAndSet(final V value);
        
         <K> boolean tryUnexpungeAndCompute(final K key, final Function<? super K, ? extends V> function);
        
         <K> boolean tryUnexpungeAndCompute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction);
    }
}
