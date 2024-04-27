/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.AbstractLinkedDeque;
import org.checkerframework.checker.nullness.qual.Nullable;

final class AccessOrderDeque<E extends AccessOrder<E>>
extends AbstractLinkedDeque<E> {
    AccessOrderDeque() {
    }

    @Override
    public boolean contains(Object o) {
        return o instanceof AccessOrder && this.contains((AccessOrder)o);
    }

    boolean contains(AccessOrder<?> e) {
        return e.getPreviousInAccessOrder() != null || e.getNextInAccessOrder() != null || e == this.first;
    }

    @Override
    public boolean remove(Object o) {
        return o instanceof AccessOrder && this.remove((E)((AccessOrder)o));
    }

    @Override
    boolean remove(E e) {
        if (this.contains((AccessOrder<?>)e)) {
            this.unlink(e);
            return true;
        }
        return false;
    }

    @Override
    public @Nullable E getPrevious(E e) {
        return (E)e.getPreviousInAccessOrder();
    }

    @Override
    public void setPrevious(E e, @Nullable E prev) {
        e.setPreviousInAccessOrder(prev);
    }

    @Override
    public @Nullable E getNext(E e) {
        return (E)e.getNextInAccessOrder();
    }

    @Override
    public void setNext(E e, @Nullable E next) {
        e.setNextInAccessOrder(next);
    }

    static interface AccessOrder<T extends AccessOrder<T>> {
        public @Nullable T getPreviousInAccessOrder();

        public void setPreviousInAccessOrder(@Nullable T var1);

        public @Nullable T getNextInAccessOrder();

        public void setNextInAccessOrder(@Nullable T var1);
    }
}

