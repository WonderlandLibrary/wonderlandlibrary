/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.viaversion.viaversion.libs.kyori.examination;

import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractExaminer<R>
implements Examiner<R> {
    @Override
    public @NonNull R examine(@Nullable Object value) {
        if (value == null) {
            return this.nil();
        }
        if (value instanceof String) {
            return this.examine((String)value);
        }
        if (value instanceof Examinable) {
            return this.examine((Examinable)value);
        }
        if (value instanceof Collection) {
            return this.collection((Collection)value);
        }
        if (value instanceof Map) {
            return this.map((Map)value);
        }
        if (value.getClass().isArray()) {
            Class<?> type = value.getClass().getComponentType();
            if (type.isPrimitive()) {
                if (type == Boolean.TYPE) {
                    return this.examine((boolean[])value);
                }
                if (type == Byte.TYPE) {
                    return this.examine((byte[])value);
                }
                if (type == Character.TYPE) {
                    return this.examine((char[])value);
                }
                if (type == Double.TYPE) {
                    return this.examine((double[])value);
                }
                if (type == Float.TYPE) {
                    return this.examine((float[])value);
                }
                if (type == Integer.TYPE) {
                    return this.examine((int[])value);
                }
                if (type == Long.TYPE) {
                    return this.examine((long[])value);
                }
                if (type == Short.TYPE) {
                    return this.examine((short[])value);
                }
            }
            return this.array((Object[])value);
        }
        if (value instanceof Boolean) {
            return this.examine((Boolean)value);
        }
        if (value instanceof Character) {
            return this.examine(((Character)value).charValue());
        }
        if (value instanceof Number) {
            if (value instanceof Byte) {
                return this.examine((Byte)value);
            }
            if (value instanceof Double) {
                return this.examine((Double)value);
            }
            if (value instanceof Float) {
                return this.examine(((Float)value).floatValue());
            }
            if (value instanceof Integer) {
                return this.examine((Integer)value);
            }
            if (value instanceof Long) {
                return this.examine((Long)value);
            }
            if (value instanceof Short) {
                return this.examine((Short)value);
            }
        } else if (value instanceof BaseStream) {
            if (value instanceof Stream) {
                return this.stream((Stream)value);
            }
            if (value instanceof DoubleStream) {
                return this.stream((DoubleStream)value);
            }
            if (value instanceof IntStream) {
                return this.stream((IntStream)value);
            }
            if (value instanceof LongStream) {
                return this.stream((LongStream)value);
            }
        }
        return this.scalar(value);
    }

    private <E> @NonNull R array(@NonNull E[] array) {
        return (R)this.array(array, Arrays.stream(array).map(this::examine));
    }

    protected abstract <E> @NonNull R array(@NonNull E[] var1, @NonNull Stream<R> var2);

    private <E> @NonNull R collection(@NonNull Collection<E> collection) {
        return (R)this.collection(collection, collection.stream().map(this::examine));
    }

    protected abstract <E> @NonNull R collection(@NonNull Collection<E> var1, @NonNull Stream<R> var2);

    @Override
    public @NonNull R examine(@NonNull String name, @NonNull Stream<? extends ExaminableProperty> properties) {
        return this.examinable(name, properties.map((? super T property) -> new AbstractMap.SimpleImmutableEntry(property.name(), property.examine(this))));
    }

    protected abstract @NonNull R examinable(@NonNull String var1, @NonNull Stream<Map.Entry<String, R>> var2);

    private <K, V> @NonNull R map(@NonNull Map<K, V> map) {
        return this.map(map, map.entrySet().stream().map((? super T entry) -> new AbstractMap.SimpleImmutableEntry<R, R>(this.examine(entry.getKey()), this.examine(entry.getValue()))));
    }

    protected abstract <K, V> @NonNull R map(@NonNull Map<K, V> var1, @NonNull Stream<Map.Entry<R, R>> var2);

    protected abstract @NonNull R nil();

    protected abstract @NonNull R scalar(@NonNull Object var1);

    protected abstract <T> @NonNull R stream(@NonNull Stream<T> var1);

    protected abstract @NonNull R stream(@NonNull DoubleStream var1);

    protected abstract @NonNull R stream(@NonNull IntStream var1);

    protected abstract @NonNull R stream(@NonNull LongStream var1);
}

