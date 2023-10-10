// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.objects.Reference2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Reference2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2ReferenceFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.doubles.Double2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.doubles.Double2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2DoubleFunction;
import com.viaversion.viaversion.libs.fastutil.floats.Float2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.floats.Float2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2FloatFunction;
import com.viaversion.viaversion.libs.fastutil.chars.Char2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.chars.Char2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2CharFunction;
import com.viaversion.viaversion.libs.fastutil.longs.Long2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.longs.Long2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2LongFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntFunction;
import com.viaversion.viaversion.libs.fastutil.shorts.Short2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.shorts.Short2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2ShortFunction;
import com.viaversion.viaversion.libs.fastutil.bytes.Byte2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.bytes.Byte2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2ByteFunction;
import java.util.function.IntFunction;
import com.viaversion.viaversion.libs.fastutil.Function;

@FunctionalInterface
public interface Int2ObjectFunction<V> extends Function<Integer, V>, IntFunction<V>
{
    default V apply(final int operand) {
        return this.get(operand);
    }
    
    default V put(final int key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    V get(final int p0);
    
    default V getOrDefault(final int key, final V defaultValue) {
        final V v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default V remove(final int key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default V put(final Integer key, final V value) {
        final int k = key;
        final boolean containsKey = this.containsKey(k);
        final V v = this.put(k, value);
        return containsKey ? v : null;
    }
    
    @Deprecated
    default V get(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        final V v;
        return ((v = this.get(k)) != this.defaultReturnValue() || this.containsKey(k)) ? v : null;
    }
    
    @Deprecated
    default V getOrDefault(final Object key, final V defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        final int k = (int)key;
        final V v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? v : defaultValue;
    }
    
    @Deprecated
    default V remove(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        return this.containsKey(k) ? this.remove(k) : null;
    }
    
    default boolean containsKey(final int key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((int)key);
    }
    
    default void defaultReturnValue(final V rv) {
        throw new UnsupportedOperationException();
    }
    
    default V defaultReturnValue() {
        return null;
    }
    
    @Deprecated
    default <T> java.util.function.Function<T, V> compose(final java.util.function.Function<? super T, ? extends Integer> before) {
        return super.compose((java.util.function.Function<? super T, ?>)before);
    }
    
    default Int2ByteFunction andThenByte(final Object2ByteFunction<V> after) {
        return k -> after.getByte(this.get(k));
    }
    
    default Byte2ObjectFunction<V> composeByte(final Byte2IntFunction before) {
        return (Byte2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default Int2ShortFunction andThenShort(final Object2ShortFunction<V> after) {
        return k -> after.getShort(this.get(k));
    }
    
    default Short2ObjectFunction<V> composeShort(final Short2IntFunction before) {
        return (Short2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default Int2IntFunction andThenInt(final Object2IntFunction<V> after) {
        return k -> after.getInt(this.get(k));
    }
    
    default Int2ObjectFunction<V> composeInt(final Int2IntFunction before) {
        return k -> this.get(before.get(k));
    }
    
    default Int2LongFunction andThenLong(final Object2LongFunction<V> after) {
        return k -> after.getLong(this.get(k));
    }
    
    default Long2ObjectFunction<V> composeLong(final Long2IntFunction before) {
        return (Long2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default Int2CharFunction andThenChar(final Object2CharFunction<V> after) {
        return k -> after.getChar(this.get(k));
    }
    
    default Char2ObjectFunction<V> composeChar(final Char2IntFunction before) {
        return (Char2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default Int2FloatFunction andThenFloat(final Object2FloatFunction<V> after) {
        return k -> after.getFloat(this.get(k));
    }
    
    default Float2ObjectFunction<V> composeFloat(final Float2IntFunction before) {
        return (Float2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default Int2DoubleFunction andThenDouble(final Object2DoubleFunction<V> after) {
        return k -> after.getDouble(this.get(k));
    }
    
    default Double2ObjectFunction<V> composeDouble(final Double2IntFunction before) {
        return (Double2ObjectFunction<V>)(k -> this.get(before.get(k)));
    }
    
    default <T> Int2ObjectFunction<T> andThenObject(final Object2ObjectFunction<? super V, ? extends T> after) {
        return (Int2ObjectFunction<T>)(k -> after.get(this.get(k)));
    }
    
    default <T> Object2ObjectFunction<T, V> composeObject(final Object2IntFunction<? super T> before) {
        return (Object2ObjectFunction<T, V>)(k -> this.get(before.getInt(k)));
    }
    
    default <T> Int2ReferenceFunction<T> andThenReference(final Object2ReferenceFunction<? super V, ? extends T> after) {
        return (Int2ReferenceFunction<T>)(k -> after.get(this.get(k)));
    }
    
    default <T> Reference2ObjectFunction<T, V> composeReference(final Reference2IntFunction<? super T> before) {
        return (Reference2ObjectFunction<T, V>)(k -> this.get(before.getInt(k)));
    }
}
