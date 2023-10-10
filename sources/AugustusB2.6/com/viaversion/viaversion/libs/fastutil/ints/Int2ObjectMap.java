// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Int2ObjectMap<V> extends Int2ObjectFunction<V>, Map<Integer, V>
{
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final V p0);
    
    V defaultReturnValue();
    
    ObjectSet<Entry<V>> int2ObjectEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Integer, V>> entrySet() {
        return (ObjectSet<Map.Entry<Integer, V>>)this.int2ObjectEntrySet();
    }
    
    @Deprecated
    default V put(final Integer key, final V value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default V get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default V remove(final Object key) {
        return super.remove(key);
    }
    
    IntSet keySet();
    
    ObjectCollection<V> values();
    
    boolean containsKey(final int p0);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    default void forEach(final BiConsumer<? super Integer, ? super V> consumer) {
        final ObjectSet<Entry<V>> entrySet = this.int2ObjectEntrySet();
        final Consumer<Entry<V>> wrappingConsumer = entry -> consumer.accept(((Entry)entry).getIntKey(), entry.getValue());
        if (entrySet instanceof FastEntrySet) {
            ((FastEntrySet)entrySet).fastForEach(wrappingConsumer);
        }
        else {
            entrySet.forEach(wrappingConsumer);
        }
    }
    
    default V getOrDefault(final int key, final V defaultValue) {
        final V v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    @Deprecated
    default V getOrDefault(final Object key, final V defaultValue) {
        return super.getOrDefault(key, defaultValue);
    }
    
    default V putIfAbsent(final int key, final V value) {
        final V v = this.get(key);
        final V drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final int key, final Object value) {
        final V curValue = this.get(key);
        if (!Objects.equals(curValue, value) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final int key, final V oldValue, final V newValue) {
        final V curValue = this.get(key);
        if (!Objects.equals(curValue, oldValue) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default V replace(final int key, final V value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default V computeIfAbsent(final int key, final IntFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final V v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final V newValue = (V)mappingFunction.apply(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default V computeIfAbsent(final int key, final Int2ObjectFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final V v = this.get(key);
        final V drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final V newValue = (V)mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    @Deprecated
    default V computeIfAbsentPartial(final int key, final Int2ObjectFunction<? extends V> mappingFunction) {
        return this.computeIfAbsent(key, mappingFunction);
    }
    
    default V computeIfPresent(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final V oldValue = this.get(key);
        final V drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final V newValue = (V)remappingFunction.apply(Integer.valueOf(key), (Object)oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        this.put(key, newValue);
        return newValue;
    }
    
    default V compute(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final V oldValue = this.get(key);
        final V drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final V newValue = (V)remappingFunction.apply(Integer.valueOf(key), (Object)(contained ? oldValue : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        this.put(key, newValue);
        return newValue;
    }
    
    default V merge(final int key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        final V oldValue = this.get(key);
        final V drv = this.defaultReturnValue();
        V newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final V mergedValue = (V)remappingFunction.apply((Object)oldValue, (Object)value);
            if (mergedValue == null) {
                this.remove(key);
                return drv;
            }
            newValue = mergedValue;
        }
        else {
            newValue = value;
        }
        this.put(key, newValue);
        return newValue;
    }
    
    public interface FastEntrySet<V> extends ObjectSet<Entry<V>>
    {
        ObjectIterator<Entry<V>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<V>> consumer) {
            this.forEach(consumer);
        }
    }
    
    public interface Entry<V> extends Map.Entry<Integer, V>
    {
        int getIntKey();
        
        @Deprecated
        default Integer getKey() {
            return this.getIntKey();
        }
    }
}
