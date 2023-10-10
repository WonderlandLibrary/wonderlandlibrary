// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.examination;

import java.util.function.IntFunction;
import java.util.AbstractMap;
import java.util.function.Function;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import java.util.stream.BaseStream;
import java.util.Map;
import java.util.Collection;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractExaminer<R> implements Examiner<R>
{
    @NotNull
    @Override
    public R examine(@Nullable final Object value) {
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
            return this.collection((Collection<Object>)value);
        }
        if (value instanceof Map) {
            return this.map((Map<Object, Object>)value);
        }
        if (value.getClass().isArray()) {
            final Class<?> type = value.getClass().getComponentType();
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
            return this.examine((boolean)value);
        }
        if (value instanceof Character) {
            return this.examine((char)value);
        }
        if (value instanceof Number) {
            if (value instanceof Byte) {
                return this.examine((byte)value);
            }
            if (value instanceof Double) {
                return this.examine((double)value);
            }
            if (value instanceof Float) {
                return this.examine((float)value);
            }
            if (value instanceof Integer) {
                return this.examine((int)value);
            }
            if (value instanceof Long) {
                return this.examine((long)value);
            }
            if (value instanceof Short) {
                return this.examine((short)value);
            }
        }
        else if (value instanceof BaseStream) {
            if (value instanceof Stream) {
                return this.stream((Stream<Object>)value);
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
    
    @NotNull
    private <E> R array(final E[] array) {
        return this.array(array, Arrays.stream(array).map((Function<? super E, ? extends R>)this::examine));
    }
    
    @NotNull
    protected abstract <E> R array(final E[] array, @NotNull final Stream<R> elements);
    
    @NotNull
    private <E> R collection(@NotNull final Collection<E> collection) {
        return this.collection(collection, collection.stream().map((Function<? super E, ? extends R>)this::examine));
    }
    
    @NotNull
    protected abstract <E> R collection(@NotNull final Collection<E> collection, @NotNull final Stream<R> elements);
    
    @NotNull
    @Override
    public R examine(@NotNull final String name, @NotNull final Stream<? extends ExaminableProperty> properties) {
        return this.examinable(name, properties.map(property -> new AbstractMap.SimpleImmutableEntry(property.name(), property.examine((Examiner<?>)this))));
    }
    
    @NotNull
    protected abstract R examinable(@NotNull final String name, @NotNull final Stream<Map.Entry<String, R>> properties);
    
    @NotNull
    private <K, V> R map(@NotNull final Map<K, V> map) {
        return this.map(map, map.entrySet().stream().map(entry -> new AbstractMap.SimpleImmutableEntry(this.examine(entry.getKey()), this.examine(entry.getValue()))));
    }
    
    @NotNull
    protected abstract <K, V> R map(@NotNull final Map<K, V> map, @NotNull final Stream<Map.Entry<R, R>> entries);
    
    @NotNull
    protected abstract R nil();
    
    @NotNull
    protected abstract R scalar(@NotNull final Object value);
    
    @NotNull
    protected abstract <T> R stream(@NotNull final Stream<T> stream);
    
    @NotNull
    protected abstract R stream(@NotNull final DoubleStream stream);
    
    @NotNull
    protected abstract R stream(@NotNull final IntStream stream);
    
    @NotNull
    protected abstract R stream(@NotNull final LongStream stream);
    
    @NotNull
    @Override
    public R examine(final boolean[] values) {
        if (values == null) {
            return this.nil();
        }
        return (R)this.array(values.length, index -> this.examine(values[index]));
    }
    
    @NotNull
    @Override
    public R examine(final byte[] values) {
        if (values == null) {
            return this.nil();
        }
        return (R)this.array(values.length, index -> this.examine(values[index]));
    }
    
    @NotNull
    @Override
    public R examine(final char[] values) {
        if (values == null) {
            return this.nil();
        }
        return (R)this.array(values.length, index -> this.examine(values[index]));
    }
    
    @NotNull
    @Override
    public R examine(final double[] values) {
        if (values == null) {
            return this.nil();
        }
        return (R)this.array(values.length, index -> this.examine(values[index]));
    }
    
    @NotNull
    @Override
    public R examine(final float[] values) {
        if (values == null) {
            return this.nil();
        }
        return (R)this.array(values.length, index -> this.examine(values[index]));
    }
    
    @NotNull
    @Override
    public R examine(final int[] values) {
        if (values == null) {
            return this.nil();
        }
        return (R)this.array(values.length, index -> this.examine(values[index]));
    }
    
    @NotNull
    @Override
    public R examine(final long[] values) {
        if (values == null) {
            return this.nil();
        }
        return (R)this.array(values.length, index -> this.examine(values[index]));
    }
    
    @NotNull
    @Override
    public R examine(final short[] values) {
        if (values == null) {
            return this.nil();
        }
        return (R)this.array(values.length, index -> this.examine(values[index]));
    }
    
    @NotNull
    protected abstract R array(final int length, final IntFunction<R> value);
}
