/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class CollectionSet<E>
implements Set<E> {
    private final Collection<E> data;

    public CollectionSet(Collection<E> data) {
        this.data = data;
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public boolean contains(Object o2) {
        return this.data.contains(o2);
    }

    @Override
    public Iterator<E> iterator() {
        return this.data.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.data.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a2) {
        return this.data.toArray(a2);
    }

    @Override
    public boolean add(E e2) {
        return this.data.add(e2);
    }

    @Override
    public boolean remove(Object o2) {
        return this.data.remove(o2);
    }

    @Override
    public boolean containsAll(Collection<?> c2) {
        return this.data.containsAll(c2);
    }

    @Override
    public boolean addAll(Collection<? extends E> c2) {
        return this.data.addAll(c2);
    }

    @Override
    public boolean retainAll(Collection<?> c2) {
        return this.data.retainAll(c2);
    }

    @Override
    public boolean removeAll(Collection<?> c2) {
        return this.data.removeAll(c2);
    }

    @Override
    public void clear() {
        this.data.clear();
    }
}

