/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

final class ChainBuilder<E> {
    private final LinkedList<E> list = new LinkedList();
    private final Map<Class<?>, E> uniqueClasses = new HashMap();

    private void ensureUnique(E e2) {
        E previous = this.uniqueClasses.remove(e2.getClass());
        if (previous != null) {
            this.list.remove(previous);
        }
        this.uniqueClasses.put(e2.getClass(), e2);
    }

    public ChainBuilder<E> addFirst(E e2) {
        if (e2 == null) {
            return this;
        }
        this.ensureUnique(e2);
        this.list.addFirst(e2);
        return this;
    }

    public ChainBuilder<E> addLast(E e2) {
        if (e2 == null) {
            return this;
        }
        this.ensureUnique(e2);
        this.list.addLast(e2);
        return this;
    }

    public ChainBuilder<E> addAllFirst(Collection<E> c2) {
        if (c2 == null) {
            return this;
        }
        for (E e2 : c2) {
            this.addFirst(e2);
        }
        return this;
    }

    public ChainBuilder<E> addAllFirst(E ... c2) {
        if (c2 == null) {
            return this;
        }
        for (E e2 : c2) {
            this.addFirst(e2);
        }
        return this;
    }

    public ChainBuilder<E> addAllLast(Collection<E> c2) {
        if (c2 == null) {
            return this;
        }
        for (E e2 : c2) {
            this.addLast(e2);
        }
        return this;
    }

    public ChainBuilder<E> addAllLast(E ... c2) {
        if (c2 == null) {
            return this;
        }
        for (E e2 : c2) {
            this.addLast(e2);
        }
        return this;
    }

    public LinkedList<E> build() {
        return new LinkedList<E>(this.list);
    }
}

