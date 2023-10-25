/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import sun.misc.Unsafe;

final class UnsafeAtomicIntegerFieldUpdater<T>
extends AtomicIntegerFieldUpdater<T> {
    private final Unsafe unsafe;
    private final long offset;

    @Override
    public void lazySet(T t, int n) {
        this.unsafe.putOrderedInt(t, this.offset, n);
    }

    @Override
    public void set(T t, int n) {
        this.unsafe.putIntVolatile(t, this.offset, n);
    }

    @Override
    public boolean weakCompareAndSet(T t, int n, int n2) {
        return this.unsafe.compareAndSwapInt(t, this.offset, n, n2);
    }

    @Override
    public boolean compareAndSet(T t, int n, int n2) {
        return this.unsafe.compareAndSwapInt(t, this.offset, n, n2);
    }

    @Override
    public int get(T t) {
        return this.unsafe.getIntVolatile(t, this.offset);
    }

    UnsafeAtomicIntegerFieldUpdater(Unsafe unsafe, Class<?> clazz, String string) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(string);
        if (!Modifier.isVolatile(field.getModifiers())) {
            throw new IllegalArgumentException("Must be volatile");
        }
        this.unsafe = unsafe;
        this.offset = unsafe.objectFieldOffset(field);
    }
}

