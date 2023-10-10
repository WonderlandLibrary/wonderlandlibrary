// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.objects.Reference2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntFunction;
import com.viaversion.viaversion.libs.fastutil.doubles.Double2IntFunction;
import com.viaversion.viaversion.libs.fastutil.floats.Float2IntFunction;
import com.viaversion.viaversion.libs.fastutil.chars.Char2IntFunction;
import com.viaversion.viaversion.libs.fastutil.longs.Long2IntFunction;
import com.viaversion.viaversion.libs.fastutil.shorts.Short2IntFunction;
import com.viaversion.viaversion.libs.fastutil.bytes.Byte2IntFunction;
import java.util.function.IntUnaryOperator;
import com.viaversion.viaversion.libs.fastutil.Function;

@FunctionalInterface
public interface Int2IntFunction extends Function<Integer, Integer>, IntUnaryOperator
{
    default int applyAsInt(final int operand) {
        return this.get(operand);
    }
    
    default int put(final int key, final int value) {
        throw new UnsupportedOperationException();
    }
    
    int get(final int p0);
    
    default int getOrDefault(final int key, final int defaultValue) {
        final int v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default int remove(final int key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Integer put(final Integer key, final Integer value) {
        final int k = key;
        final boolean containsKey = this.containsKey(k);
        final int v = this.put(k, (int)value);
        return containsKey ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer get(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        final int v;
        return ((v = this.get(k)) != this.defaultReturnValue() || this.containsKey(k)) ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer getOrDefault(final Object key, final Integer defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        final int k = (int)key;
        final int v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Integer.valueOf(v) : defaultValue;
    }
    
    @Deprecated
    default Integer remove(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        return this.containsKey(k) ? Integer.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final int key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((int)key);
    }
    
    default void defaultReturnValue(final int rv) {
        throw new UnsupportedOperationException();
    }
    
    default int defaultReturnValue() {
        return 0;
    }
    
    default Int2IntFunction identity() {
        return k -> k;
    }
    
    @Deprecated
    default <T> java.util.function.Function<T, Integer> compose(final java.util.function.Function<? super T, ? extends Integer> before) {
        return super.compose((java.util.function.Function<? super T, ?>)before);
    }
    
    @Deprecated
    default <T> java.util.function.Function<Integer, T> andThen(final java.util.function.Function<? super Integer, ? extends T> after) {
        return super.andThen((java.util.function.Function<? super Object, ? extends T>)after);
    }
    
    default Int2ByteFunction andThenByte(final Int2ByteFunction after) {
        return k -> after.get(this.get(k));
    }
    
    default Byte2IntFunction composeByte(final Byte2IntFunction before) {
        return k -> this.get(before.get(k));
    }
    
    default Int2ShortFunction andThenShort(final Int2ShortFunction after) {
        return k -> after.get(this.get(k));
    }
    
    default Short2IntFunction composeShort(final Short2IntFunction before) {
        return k -> this.get(before.get(k));
    }
    
    default Int2IntFunction andThenInt(final Int2IntFunction after) {
        return k -> after.get(this.get(k));
    }
    
    default Int2IntFunction composeInt(final Int2IntFunction before) {
        return k -> this.get(before.get(k));
    }
    
    default Int2LongFunction andThenLong(final Int2LongFunction after) {
        return k -> after.get(this.get(k));
    }
    
    default Long2IntFunction composeLong(final Long2IntFunction before) {
        return k -> this.get(before.get(k));
    }
    
    default Int2CharFunction andThenChar(final Int2CharFunction after) {
        return k -> after.get(this.get(k));
    }
    
    default Char2IntFunction composeChar(final Char2IntFunction before) {
        return k -> this.get(before.get(k));
    }
    
    default Int2FloatFunction andThenFloat(final Int2FloatFunction after) {
        return k -> after.get(this.get(k));
    }
    
    default Float2IntFunction composeFloat(final Float2IntFunction before) {
        return k -> this.get(before.get(k));
    }
    
    default Int2DoubleFunction andThenDouble(final Int2DoubleFunction after) {
        return k -> after.get(this.get(k));
    }
    
    default Double2IntFunction composeDouble(final Double2IntFunction before) {
        return k -> this.get(before.get(k));
    }
    
    default <T> Int2ObjectFunction<T> andThenObject(final Int2ObjectFunction<? extends T> after) {
        return (Int2ObjectFunction<T>)(k -> after.get(this.get(k)));
    }
    
    default <T> Object2IntFunction<T> composeObject(final Object2IntFunction<? super T> before) {
        return k -> this.get(before.getInt(k));
    }
    
    default <T> Int2ReferenceFunction<T> andThenReference(final Int2ReferenceFunction<? extends T> after) {
        return (Int2ReferenceFunction<T>)(k -> after.get(this.get(k)));
    }
    
    default <T> Reference2IntFunction<T> composeReference(final Reference2IntFunction<? super T> before) {
        return (Reference2IntFunction<T>)(k -> this.get(before.getInt(k)));
    }
}
