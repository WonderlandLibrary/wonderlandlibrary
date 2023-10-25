/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import sun.misc.Unsafe;

final class UnsafeAtomicLongFieldUpdater<T>
extends AtomicLongFieldUpdater<T> {
    private final long offset;
    private final Unsafe unsafe;

    @Override
    public boolean compareAndSet(T t, long l, long l2) {
        return this.unsafe.compareAndSwapLong(t, this.offset, l, l2);
    }

    @Override
    public void lazySet(T t, long l) {
        this.unsafe.putOrderedLong(t, this.offset, l);
    }

    @Override
    public void set(T t, long l) {
        this.unsafe.putLongVolatile(t, this.offset, l);
    }

    UnsafeAtomicLongFieldUpdater(Unsafe unsafe, Class<?> clazz, String string) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(string);
        if (!Modifier.isVolatile(field.getModifiers())) {
            throw new IllegalArgumentException("Must be volatile");
        }
        this.unsafe = unsafe;
        this.offset = unsafe.objectFieldOffset(field);
    }

    @Override
    public long get(T t) {
        return this.unsafe.getLongVolatile(t, this.offset);
    }

    @Override
    public boolean weakCompareAndSet(T t, long l, long l2) {
        return this.unsafe.compareAndSwapLong(t, this.offset, l, l2);
    }
}

