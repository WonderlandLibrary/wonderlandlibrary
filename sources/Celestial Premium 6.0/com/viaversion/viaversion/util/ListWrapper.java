/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Deprecated
public abstract class ListWrapper
implements List {
    private final List list;

    public ListWrapper(List inputList) {
        this.list = inputList;
    }

    public abstract void handleAdd(Object var1);

    public List getOriginalList() {
        return this.list;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int size() {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.size();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isEmpty() {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.isEmpty();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean contains(Object o) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.contains(o);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Iterator iterator() {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.listIterator();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Object[] toArray() {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.toArray();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean add(Object o) {
        this.handleAdd(o);
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.add(o);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean remove(Object o) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.remove(o);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean addAll(Collection c) {
        for (Object o : c) {
            this.handleAdd(o);
        }
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.addAll(c);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean addAll(int index, Collection c) {
        for (Object o : c) {
            this.handleAdd(o);
        }
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.addAll(index, c);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void clear() {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            this.list.clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Object get(int index) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.get(index);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Object set(int index, Object element) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.set(index, element);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void add(int index, Object element) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            this.list.add(index, element);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Object remove(int index) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.remove(index);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int indexOf(Object o) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.indexOf(o);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int lastIndexOf(Object o) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.lastIndexOf(o);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ListIterator listIterator() {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.listIterator();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ListIterator listIterator(int index) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.listIterator(index);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List subList(int fromIndex, int toIndex) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.subList(fromIndex, toIndex);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean retainAll(Collection c) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.retainAll(c);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean removeAll(Collection c) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.removeAll(c);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean containsAll(Collection c) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.containsAll(c);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Object[] toArray(Object[] a) {
        ListWrapper listWrapper = this;
        synchronized (listWrapper) {
            return this.list.toArray(a);
        }
    }
}

