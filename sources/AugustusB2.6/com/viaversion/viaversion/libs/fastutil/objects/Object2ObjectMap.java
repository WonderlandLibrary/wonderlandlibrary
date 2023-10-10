// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.Map;

public interface Object2ObjectMap<K, V> extends Object2ObjectFunction<K, V>, Map<K, V>
{
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final V p0);
    
    V defaultReturnValue();
    
    ObjectSet<Entry<K, V>> object2ObjectEntrySet();
    
    default ObjectSet<Map.Entry<K, V>> entrySet() {
        return (ObjectSet<Map.Entry<K, V>>)this.object2ObjectEntrySet();
    }
    
    default V put(final K key, final V value) {
        return super.put(key, value);
    }
    
    default V remove(final Object key) {
        return super.remove(key);
    }
    
    ObjectSet<K> keySet();
    
    ObjectCollection<V> values();
    
    boolean containsKey(final Object p0);
    
    default void forEach(final BiConsumer<? super K, ? super V> consumer) {
        final ObjectSet<Entry<K, V>> entrySet = this.object2ObjectEntrySet();
        final Consumer<Entry<K, V>> wrappingConsumer = entry -> consumer.accept((Object)entry.getKey(), entry.getValue());
        if (entrySet instanceof FastEntrySet) {
            ((FastEntrySet)entrySet).fastForEach(wrappingConsumer);
        }
        else {
            entrySet.forEach(wrappingConsumer);
        }
    }
    
    default V getOrDefault(final Object key, final V defaultValue) {
        final V v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default V putIfAbsent(final K key, final V value) {
        final V v = this.get(key);
        final V drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final Object key, final Object value) {
        final V curValue = this.get(key);
        if (!Objects.equals(curValue, value) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final K key, final V oldValue, final V newValue) {
        final V curValue = this.get(key);
        if (!Objects.equals(curValue, oldValue) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default V replace(final K key, final V value) {
        return (V)(this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue());
    }
    
    default V computeIfAbsent(final K key, final Object2ObjectFunction<? super K, ? extends V> mappingFunction) {
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
    default V computeObjectIfAbsentPartial(final K key, final Object2ObjectFunction<? super K, ? extends V> mappingFunction) {
        return this.computeIfAbsent(key, mappingFunction);
    }
    
    default V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final V oldValue = this.get(key);
        final V drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final V newValue = (V)remappingFunction.apply((Object)key, (Object)oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        this.put(key, newValue);
        return newValue;
    }
    
    default V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final V oldValue = this.get(key);
        final V drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final V newValue = (V)remappingFunction.apply((Object)key, (Object)(contained ? oldValue : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        this.put(key, newValue);
        return newValue;
    }
    
    default V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
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
    
    public interface FastEntrySet<K, V> extends ObjectSet<Entry<K, V>>
    {
        ObjectIterator<Entry<K, V>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<K, V>> consumer) {
            this.forEach(consumer);
        }
    }
    
    public interface Entry<K, V> extends Map.Entry<K, V>
    {
    }
}
