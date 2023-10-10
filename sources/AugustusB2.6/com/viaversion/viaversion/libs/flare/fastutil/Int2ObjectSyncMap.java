// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.flare.fastutil;

import java.util.function.BiFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSet;
import java.util.function.IntFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectOpenHashMap;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectMap;

public interface Int2ObjectSyncMap<V> extends Int2ObjectMap<V>
{
    default <V> Int2ObjectSyncMap<V> hashmap() {
        return of((IntFunction<Int2ObjectMap<ExpungingEntry<V>>>)Int2ObjectOpenHashMap::new, 16);
    }
    
    default <V> Int2ObjectSyncMap<V> hashmap(final int initialCapacity) {
        return of((IntFunction<Int2ObjectMap<ExpungingEntry<V>>>)Int2ObjectOpenHashMap::new, initialCapacity);
    }
    
    default IntSet hashset() {
        return setOf((IntFunction<Int2ObjectMap<ExpungingEntry<Boolean>>>)Int2ObjectOpenHashMap::new, 16);
    }
    
    default IntSet hashset(final int initialCapacity) {
        return setOf((IntFunction<Int2ObjectMap<ExpungingEntry<Boolean>>>)Int2ObjectOpenHashMap::new, initialCapacity);
    }
    
    default <V> Int2ObjectSyncMap<V> of(final IntFunction<Int2ObjectMap<ExpungingEntry<V>>> function, final int initialCapacity) {
        return new Int2ObjectSyncMapImpl<V>(function, initialCapacity);
    }
    
    default IntSet setOf(final IntFunction<Int2ObjectMap<ExpungingEntry<Boolean>>> function, final int initialCapacity) {
        return new Int2ObjectSyncMapSet(new Int2ObjectSyncMapImpl<Boolean>(function, initialCapacity));
    }
    
    ObjectSet<Entry<V>> int2ObjectEntrySet();
    
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
        
        InsertionResult<V> computeIfAbsent(final int key, final IntFunction<? extends V> function);
        
        InsertionResult<V> computeIfAbsentPrimitive(final int key, final Int2ObjectFunction<? extends V> function);
        
        InsertionResult<V> computeIfPresent(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction);
        
        InsertionResult<V> compute(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction);
        
        void set(final V value);
        
        boolean replace(final Object compare, final V value);
        
        V clear();
        
        boolean trySet(final V value);
        
        V tryReplace(final V value);
        
        boolean tryExpunge();
        
        boolean tryUnexpungeAndSet(final V value);
        
        boolean tryUnexpungeAndCompute(final int key, final IntFunction<? extends V> function);
        
        boolean tryUnexpungeAndComputePrimitive(final int key, final Int2ObjectFunction<? extends V> function);
        
        boolean tryUnexpungeAndCompute(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction);
    }
}
