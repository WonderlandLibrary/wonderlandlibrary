/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.bytes.Byte2IntFunction
 *  com.viaversion.viaversion.libs.fastutil.bytes.Byte2ObjectFunction
 *  com.viaversion.viaversion.libs.fastutil.chars.Char2IntFunction
 *  com.viaversion.viaversion.libs.fastutil.chars.Char2ObjectFunction
 *  com.viaversion.viaversion.libs.fastutil.doubles.Double2IntFunction
 *  com.viaversion.viaversion.libs.fastutil.doubles.Double2ObjectFunction
 *  com.viaversion.viaversion.libs.fastutil.floats.Float2IntFunction
 *  com.viaversion.viaversion.libs.fastutil.floats.Float2ObjectFunction
 *  com.viaversion.viaversion.libs.fastutil.ints.Int2ByteFunction
 *  com.viaversion.viaversion.libs.fastutil.ints.Int2CharFunction
 *  com.viaversion.viaversion.libs.fastutil.ints.Int2DoubleFunction
 *  com.viaversion.viaversion.libs.fastutil.ints.Int2FloatFunction
 *  com.viaversion.viaversion.libs.fastutil.ints.Int2LongFunction
 *  com.viaversion.viaversion.libs.fastutil.ints.Int2ReferenceFunction
 *  com.viaversion.viaversion.libs.fastutil.ints.Int2ShortFunction
 *  com.viaversion.viaversion.libs.fastutil.longs.Long2IntFunction
 *  com.viaversion.viaversion.libs.fastutil.longs.Long2ObjectFunction
 *  com.viaversion.viaversion.libs.fastutil.objects.Object2ByteFunction
 *  com.viaversion.viaversion.libs.fastutil.objects.Object2CharFunction
 *  com.viaversion.viaversion.libs.fastutil.objects.Object2DoubleFunction
 *  com.viaversion.viaversion.libs.fastutil.objects.Object2FloatFunction
 *  com.viaversion.viaversion.libs.fastutil.objects.Object2LongFunction
 *  com.viaversion.viaversion.libs.fastutil.objects.Object2ReferenceFunction
 *  com.viaversion.viaversion.libs.fastutil.objects.Object2ShortFunction
 *  com.viaversion.viaversion.libs.fastutil.objects.Reference2IntFunction
 *  com.viaversion.viaversion.libs.fastutil.objects.Reference2ObjectFunction
 *  com.viaversion.viaversion.libs.fastutil.shorts.Short2IntFunction
 *  com.viaversion.viaversion.libs.fastutil.shorts.Short2ObjectFunction
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Function;
import com.viaversion.viaversion.libs.fastutil.bytes.Byte2IntFunction;
import com.viaversion.viaversion.libs.fastutil.bytes.Byte2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.chars.Char2IntFunction;
import com.viaversion.viaversion.libs.fastutil.chars.Char2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.doubles.Double2IntFunction;
import com.viaversion.viaversion.libs.fastutil.doubles.Double2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.floats.Float2IntFunction;
import com.viaversion.viaversion.libs.fastutil.floats.Float2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ByteFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2CharFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2DoubleFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2FloatFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2LongFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ReferenceFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2ShortFunction;
import com.viaversion.viaversion.libs.fastutil.longs.Long2IntFunction;
import com.viaversion.viaversion.libs.fastutil.longs.Long2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2ByteFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2CharFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2DoubleFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2FloatFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2LongFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2ReferenceFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2ShortFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Reference2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Reference2ObjectFunction;
import com.viaversion.viaversion.libs.fastutil.shorts.Short2IntFunction;
import com.viaversion.viaversion.libs.fastutil.shorts.Short2ObjectFunction;
import java.util.function.IntFunction;

@FunctionalInterface
public interface Int2ObjectFunction<V>
extends Function<Integer, V>,
IntFunction<V> {
    @Override
    default public V apply(int operand) {
        return this.get(operand);
    }

    @Override
    default public V put(int key, V value) {
        throw new UnsupportedOperationException();
    }

    public V get(int var1);

    default public V getOrDefault(int key, V defaultValue) {
        V v2 = this.get(key);
        return v2 != this.defaultReturnValue() || this.containsKey(key) ? v2 : defaultValue;
    }

    default public V remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public V put(Integer key, V value) {
        int k2 = key;
        boolean containsKey = this.containsKey(k2);
        V v2 = this.put(k2, value);
        return (V)(containsKey ? v2 : null);
    }

    @Override
    @Deprecated
    default public V get(Object key) {
        if (key == null) {
            return null;
        }
        int k2 = (Integer)key;
        V v2 = this.get(k2);
        return (V)(v2 != this.defaultReturnValue() || this.containsKey(k2) ? v2 : null);
    }

    @Override
    @Deprecated
    default public V getOrDefault(Object key, V defaultValue) {
        if (key == null) {
            return defaultValue;
        }
        int k2 = (Integer)key;
        V v2 = this.get(k2);
        return v2 != this.defaultReturnValue() || this.containsKey(k2) ? v2 : defaultValue;
    }

    @Override
    @Deprecated
    default public V remove(Object key) {
        if (key == null) {
            return null;
        }
        int k2 = (Integer)key;
        return this.containsKey(k2) ? (V)this.remove(k2) : null;
    }

    default public boolean containsKey(int key) {
        return true;
    }

    @Override
    @Deprecated
    default public boolean containsKey(Object key) {
        return key == null ? false : this.containsKey((Integer)key);
    }

    default public void defaultReturnValue(V rv) {
        throw new UnsupportedOperationException();
    }

    default public V defaultReturnValue() {
        return null;
    }

    @Override
    @Deprecated
    default public <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends Integer> before) {
        return Function.super.compose(before);
    }

    default public Int2ByteFunction andThenByte(Object2ByteFunction<V> after) {
        return k2 -> after.getByte(this.get(k2));
    }

    default public Byte2ObjectFunction<V> composeByte(Byte2IntFunction before) {
        return k2 -> this.get(before.get(k2));
    }

    default public Int2ShortFunction andThenShort(Object2ShortFunction<V> after) {
        return k2 -> after.getShort(this.get(k2));
    }

    default public Short2ObjectFunction<V> composeShort(Short2IntFunction before) {
        return k2 -> this.get(before.get(k2));
    }

    default public Int2IntFunction andThenInt(Object2IntFunction<V> after) {
        return k2 -> after.getInt(this.get(k2));
    }

    default public Int2ObjectFunction<V> composeInt(Int2IntFunction before) {
        return k2 -> this.get(before.get(k2));
    }

    default public Int2LongFunction andThenLong(Object2LongFunction<V> after) {
        return k2 -> after.getLong(this.get(k2));
    }

    default public Long2ObjectFunction<V> composeLong(Long2IntFunction before) {
        return k2 -> this.get(before.get(k2));
    }

    default public Int2CharFunction andThenChar(Object2CharFunction<V> after) {
        return k2 -> after.getChar(this.get(k2));
    }

    default public Char2ObjectFunction<V> composeChar(Char2IntFunction before) {
        return k2 -> this.get(before.get(k2));
    }

    default public Int2FloatFunction andThenFloat(Object2FloatFunction<V> after) {
        return k2 -> after.getFloat(this.get(k2));
    }

    default public Float2ObjectFunction<V> composeFloat(Float2IntFunction before) {
        return k2 -> this.get(before.get(k2));
    }

    default public Int2DoubleFunction andThenDouble(Object2DoubleFunction<V> after) {
        return k2 -> after.getDouble(this.get(k2));
    }

    default public Double2ObjectFunction<V> composeDouble(Double2IntFunction before) {
        return k2 -> this.get(before.get(k2));
    }

    default public <T> Int2ObjectFunction<T> andThenObject(Object2ObjectFunction<? super V, ? extends T> after) {
        return k2 -> after.get(this.get(k2));
    }

    default public <T> Object2ObjectFunction<T, V> composeObject(Object2IntFunction<? super T> before) {
        return k2 -> this.get(before.getInt(k2));
    }

    default public <T> Int2ReferenceFunction<T> andThenReference(Object2ReferenceFunction<? super V, ? extends T> after) {
        return k2 -> after.get(this.get(k2));
    }

    default public <T> Reference2ObjectFunction<T, V> composeReference(Reference2IntFunction<? super T> before) {
        return k2 -> this.get(before.getInt(k2));
    }
}

