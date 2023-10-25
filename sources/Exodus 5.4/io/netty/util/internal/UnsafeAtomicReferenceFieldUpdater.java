/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import sun.misc.Unsafe;

final class UnsafeAtomicReferenceFieldUpdater<U, M>
extends AtomicReferenceFieldUpdater<U, M> {
    private final Unsafe unsafe;
    private final long offset;

    @Override
    public M get(U u) {
        return (M)this.unsafe.getObjectVolatile(u, this.offset);
    }

    @Override
    public boolean compareAndSet(U u, M m, M m2) {
        return this.unsafe.compareAndSwapObject(u, this.offset, m, m2);
    }

    @Override
    public void lazySet(U u, M m) {
        this.unsafe.putOrderedObject(u, this.offset, m);
    }

    UnsafeAtomicReferenceFieldUpdater(Unsafe unsafe, Class<U> clazz, String string) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(string);
        if (!Modifier.isVolatile(field.getModifiers())) {
            throw new IllegalArgumentException("Must be volatile");
        }
        this.unsafe = unsafe;
        this.offset = unsafe.objectFieldOffset(field);
    }

    @Override
    public void set(U u, M m) {
        this.unsafe.putObjectVolatile(u, this.offset, m);
    }

    @Override
    public boolean weakCompareAndSet(U u, M m, M m2) {
        return this.unsafe.compareAndSwapObject(u, this.offset, m, m2);
    }
}

