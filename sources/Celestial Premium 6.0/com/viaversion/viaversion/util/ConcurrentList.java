/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

@Deprecated
public class ConcurrentList<E>
extends ArrayList<E> {
    private final Object lock = new Object();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean add(E e) {
        Object object = this.lock;
        synchronized (object) {
            return super.add(e);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void add(int index, E element) {
        Object object = this.lock;
        synchronized (object) {
            super.add(index, element);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object object = this.lock;
        synchronized (object) {
            return super.addAll(c);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        Object object = this.lock;
        synchronized (object) {
            return super.addAll(index, c);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void clear() {
        Object object = this.lock;
        synchronized (object) {
            super.clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Object clone() {
        Object object = this.lock;
        synchronized (object) {
            return super.clone();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean contains(Object o) {
        Object object = this.lock;
        synchronized (object) {
            return super.contains(o);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void ensureCapacity(int minCapacity) {
        Object object = this.lock;
        synchronized (object) {
            super.ensureCapacity(minCapacity);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public E get(int index) {
        Object object = this.lock;
        synchronized (object) {
            return super.get(index);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int indexOf(Object o) {
        Object object = this.lock;
        synchronized (object) {
            return super.indexOf(o);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int lastIndexOf(Object o) {
        Object object = this.lock;
        synchronized (object) {
            return super.lastIndexOf(o);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public E remove(int index) {
        Object object = this.lock;
        synchronized (object) {
            return super.remove(index);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean remove(Object o) {
        Object object = this.lock;
        synchronized (object) {
            return super.remove(o);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        Object object = this.lock;
        synchronized (object) {
            return super.removeAll(c);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        Object object = this.lock;
        synchronized (object) {
            return super.retainAll(c);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public E set(int index, E element) {
        Object object = this.lock;
        synchronized (object) {
            return super.set(index, element);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        Object object = this.lock;
        synchronized (object) {
            return super.subList(fromIndex, toIndex);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Object[] toArray() {
        Object object = this.lock;
        synchronized (object) {
            return super.toArray();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <T> T[] toArray(T[] a) {
        Object object = this.lock;
        synchronized (object) {
            return super.toArray(a);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void trimToSize() {
        Object object = this.lock;
        synchronized (object) {
            super.trimToSize();
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    public class ListItr
    extends Itr
    implements ListIterator<E> {
        ListItr(int index) {
            this.cursor = index;
        }

        @Override
        public boolean hasPrevious() {
            return this.cursor > 0;
        }

        @Override
        public int nextIndex() {
            return this.cursor;
        }

        @Override
        public int previousIndex() {
            return this.cursor - 1;
        }

        @Override
        public E previous() {
            int i = this.cursor - 1;
            if (i < 0) {
                throw new NoSuchElementException();
            }
            this.cursor = i;
            this.lastRet = i;
            return this.l.get(this.lastRet);
        }

        @Override
        public void set(E e) {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            this.l.set(this.lastRet, e);
            ConcurrentList.this.set(this.lastRet, e);
        }

        @Override
        public void add(E e) {
            int i = this.cursor;
            this.l.add(i, e);
            ConcurrentList.this.add(i, e);
            this.cursor = i + 1;
            this.lastRet = -1;
        }
    }

    private class Itr
    implements Iterator<E> {
        protected int cursor = 0;
        protected int lastRet = -1;
        final ConcurrentList l;

        public Itr() {
            this.l = (ConcurrentList)ConcurrentList.this.clone();
        }

        @Override
        public boolean hasNext() {
            return this.cursor < this.l.size();
        }

        @Override
        public E next() {
            int i = this.cursor;
            if (i >= this.l.size()) {
                throw new NoSuchElementException();
            }
            this.cursor = i + 1;
            this.lastRet = i;
            return this.l.get(this.lastRet);
        }

        @Override
        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            this.l.remove(this.lastRet);
            ConcurrentList.this.remove(this.lastRet);
            this.cursor = this.lastRet;
            this.lastRet = -1;
        }
    }
}

