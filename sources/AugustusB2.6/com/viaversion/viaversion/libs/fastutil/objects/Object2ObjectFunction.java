// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.doubles.Double2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.floats.Float2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.chars.Char2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.longs.Long2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.shorts.Short2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.bytes.Byte2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.Function;

@FunctionalInterface
public interface Object2ObjectFunction<K, V> extends Function<K, V>
{
    default V put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    V get(final Object p0);
    
    default V getOrDefault(final Object key, final V defaultValue) {
        final V v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default V remove(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    default void defaultReturnValue(final V rv) {
        throw new UnsupportedOperationException();
    }
    
    default V defaultReturnValue() {
        return null;
    }
    
    default Object2ByteFunction<K> andThenByte(final Object2ByteFunction<V> after) {
        return (Object2ByteFunction<K>)(k -> after.getByte(this.get(k)));
    }
    
    default Byte2ObjectFunction<V> composeByte(final Byte2ObjectFunction<K> before) {
        return (Byte2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default Object2ShortFunction<K> andThenShort(final Object2ShortFunction<V> after) {
        return (Object2ShortFunction<K>)(k -> after.getShort(this.get(k)));
    }
    
    default Short2ObjectFunction<V> composeShort(final Short2ObjectFunction<K> before) {
        return (Short2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default Object2IntFunction<K> andThenInt(final Object2IntFunction<V> after) {
        return k -> after.getInt(this.get(k));
    }
    
    default Int2ObjectFunction<V> composeInt(final Int2ObjectFunction<K> before) {
        return (Int2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default Object2LongFunction<K> andThenLong(final Object2LongFunction<V> after) {
        return (Object2LongFunction<K>)(k -> after.getLong(this.get(k)));
    }
    
    default Long2ObjectFunction<V> composeLong(final Long2ObjectFunction<K> before) {
        return (Long2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default Object2CharFunction<K> andThenChar(final Object2CharFunction<V> after) {
        return (Object2CharFunction<K>)(k -> after.getChar(this.get(k)));
    }
    
    default Char2ObjectFunction<V> composeChar(final Char2ObjectFunction<K> before) {
        return (Char2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default Object2FloatFunction<K> andThenFloat(final Object2FloatFunction<V> after) {
        return (Object2FloatFunction<K>)(k -> after.getFloat(this.get(k)));
    }
    
    default Float2ObjectFunction<V> composeFloat(final Float2ObjectFunction<K> before) {
        return (Float2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default Object2DoubleFunction<K> andThenDouble(final Object2DoubleFunction<V> after) {
        return (Object2DoubleFunction<K>)(k -> after.getDouble(this.get(k)));
    }
    
    default Double2ObjectFunction<V> composeDouble(final Double2ObjectFunction<K> before) {
        return (Double2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default <T> Object2ObjectFunction<K, T> andThenObject(final Object2ObjectFunction<? super V, ? extends T> after) {
        return (Object2ObjectFunction<K, T>)(k -> after.get(this.get(k)));
    }
    
    default <T> Object2ObjectFunction<T, V> composeObject(final Object2ObjectFunction<? super T, ? extends K> before) {
        return k -> this.get(before.get(k));
    }
    
    default <T> Object2ReferenceFunction<K, T> andThenReference(final Object2ReferenceFunction<? super V, ? extends T> after) {
        return (Object2ReferenceFunction<K, T>)(k -> after.get(this.get(k)));
    }
    
    default <T> Reference2ObjectFunction<T, V> composeReference(final Reference2ObjectFunction<? super T, ? extends K> before) {
        return (Reference2ObjectFunction<T, V>)(k -> this.get(before.get(k)));
    }
}
