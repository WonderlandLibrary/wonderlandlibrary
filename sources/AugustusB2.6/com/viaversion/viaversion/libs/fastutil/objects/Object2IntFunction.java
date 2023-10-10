// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.ints.Int2ReferenceFunction;
import com.viaversion.viaversion.libs.fastutil.doubles.Double2IntFunction;
import com.viaversion.viaversion.libs.fastutil.doubles.Double2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2DoubleFunction;
import com.viaversion.viaversion.libs.fastutil.floats.Float2IntFunction;
import com.viaversion.viaversion.libs.fastutil.floats.Float2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2FloatFunction;
import com.viaversion.viaversion.libs.fastutil.chars.Char2IntFunction;
import com.viaversion.viaversion.libs.fastutil.chars.Char2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2CharFunction;
import com.viaversion.viaversion.libs.fastutil.longs.Long2IntFunction;
import com.viaversion.viaversion.libs.fastutil.longs.Long2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2LongFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntFunction;
import com.viaversion.viaversion.libs.fastutil.shorts.Short2IntFunction;
import com.viaversion.viaversion.libs.fastutil.shorts.Short2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ShortFunction;
import com.viaversion.viaversion.libs.fastutil.bytes.Byte2IntFunction;
import com.viaversion.viaversion.libs.fastutil.bytes.Byte2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ByteFunction;
import java.util.function.ToIntFunction;
import com.viaversion.viaversion.libs.fastutil.Function;

@FunctionalInterface
public interface Object2IntFunction<K> extends Function<K, Integer>, ToIntFunction<K>
{
    default int applyAsInt(final K operand) {
        return this.getInt(operand);
    }
    
    default int put(final K key, final int value) {
        throw new UnsupportedOperationException();
    }
    
    int getInt(final Object p0);
    
    default int getOrDefault(final Object key, final int defaultValue) {
        final int v;
        return ((v = this.getInt(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default int removeInt(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Integer put(final K key, final Integer value) {
        final K k = key;
        final boolean containsKey = this.containsKey(k);
        final int v = this.put(k, (int)value);
        return containsKey ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer get(final Object key) {
        final Object k = key;
        final int v;
        return ((v = this.getInt(k)) != this.defaultReturnValue() || this.containsKey(k)) ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer getOrDefault(final Object key, final Integer defaultValue) {
        final Object k = key;
        final int v = this.getInt(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Integer.valueOf(v) : defaultValue;
    }
    
    @Deprecated
    default Integer remove(final Object key) {
        final Object k = key;
        return this.containsKey(k) ? Integer.valueOf(this.removeInt(k)) : null;
    }
    
    default void defaultReturnValue(final int rv) {
        throw new UnsupportedOperationException();
    }
    
    default int defaultReturnValue() {
        return 0;
    }
    
    @Deprecated
    default <T> java.util.function.Function<K, T> andThen(final java.util.function.Function<? super Integer, ? extends T> after) {
        return super.andThen((java.util.function.Function<? super Object, ? extends T>)after);
    }
    
    default Object2ByteFunction<K> andThenByte(final Int2ByteFunction after) {
        return (Object2ByteFunction<K>)(k -> after.get(this.getInt(k)));
    }
    
    default Byte2IntFunction composeByte(final Byte2ObjectFunction<K> before) {
        return k -> this.getInt(before.get(k));
    }
    
    default Object2ShortFunction<K> andThenShort(final Int2ShortFunction after) {
        return (Object2ShortFunction<K>)(k -> after.get(this.getInt(k)));
    }
    
    default Short2IntFunction composeShort(final Short2ObjectFunction<K> before) {
        return k -> this.getInt(before.get(k));
    }
    
    default Object2IntFunction<K> andThenInt(final Int2IntFunction after) {
        return k -> after.get(this.getInt(k));
    }
    
    default Int2IntFunction composeInt(final Int2ObjectFunction<K> before) {
        return k -> this.getInt(before.get(k));
    }
    
    default Object2LongFunction<K> andThenLong(final Int2LongFunction after) {
        return (Object2LongFunction<K>)(k -> after.get(this.getInt(k)));
    }
    
    default Long2IntFunction composeLong(final Long2ObjectFunction<K> before) {
        return k -> this.getInt(before.get(k));
    }
    
    default Object2CharFunction<K> andThenChar(final Int2CharFunction after) {
        return (Object2CharFunction<K>)(k -> after.get(this.getInt(k)));
    }
    
    default Char2IntFunction composeChar(final Char2ObjectFunction<K> before) {
        return k -> this.getInt(before.get(k));
    }
    
    default Object2FloatFunction<K> andThenFloat(final Int2FloatFunction after) {
        return (Object2FloatFunction<K>)(k -> after.get(this.getInt(k)));
    }
    
    default Float2IntFunction composeFloat(final Float2ObjectFunction<K> before) {
        return k -> this.getInt(before.get(k));
    }
    
    default Object2DoubleFunction<K> andThenDouble(final Int2DoubleFunction after) {
        return (Object2DoubleFunction<K>)(k -> after.get(this.getInt(k)));
    }
    
    default Double2IntFunction composeDouble(final Double2ObjectFunction<K> before) {
        return k -> this.getInt(before.get(k));
    }
    
    default <T> Object2ObjectFunction<K, T> andThenObject(final Int2ObjectFunction<? extends T> after) {
        return (Object2ObjectFunction<K, T>)(k -> after.get(this.getInt(k)));
    }
    
    default <T> Object2IntFunction<T> composeObject(final Object2ObjectFunction<? super T, ? extends K> before) {
        return k -> this.getInt(before.get(k));
    }
    
    default <T> Object2ReferenceFunction<K, T> andThenReference(final Int2ReferenceFunction<? extends T> after) {
        return (Object2ReferenceFunction<K, T>)(k -> after.get(this.getInt(k)));
    }
    
    default <T> Reference2IntFunction<T> composeReference(final Reference2ObjectFunction<? super T, ? extends K> before) {
        return (Reference2IntFunction<T>)(k -> this.getInt(before.get(k)));
    }
}
