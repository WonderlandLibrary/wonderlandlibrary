/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.AbstractLinkedDeque;
import org.checkerframework.checker.nullness.qual.Nullable;

final class WriteOrderDeque<E extends WriteOrder<E>>
extends AbstractLinkedDeque<E> {
    WriteOrderDeque() {
    }

    @Override
    public boolean contains(Object o) {
        return o instanceof WriteOrder && this.contains((WriteOrder)o);
    }

    boolean contains(WriteOrder<?> e) {
        return e.getPreviousInWriteOrder() != null || e.getNextInWriteOrder() != null || e == this.first;
    }

    @Override
    public boolean remove(Object o) {
        return o instanceof WriteOrder && this.remove((E)((WriteOrder)o));
    }

    @Override
    public boolean remove(E e) {
        if (this.contains((WriteOrder<?>)e)) {
            this.unlink(e);
            return true;
        }
        return false;
    }

    @Override
    public @Nullable E getPrevious(E e) {
        return (E)e.getPreviousInWriteOrder();
    }

    @Override
    public void setPrevious(E e, @Nullable E prev) {
        e.setPreviousInWriteOrder(prev);
    }

    @Override
    public @Nullable E getNext(E e) {
        return (E)e.getNextInWriteOrder();
    }

    @Override
    public void setNext(E e, @Nullable E next) {
        e.setNextInWriteOrder(next);
    }

    static interface WriteOrder<T extends WriteOrder<T>> {
        public @Nullable T getPreviousInWriteOrder();

        public void setPreviousInWriteOrder(@Nullable T var1);

        public @Nullable T getNextInWriteOrder();

        public void setNextInWriteOrder(@Nullable T var1);
    }
}

