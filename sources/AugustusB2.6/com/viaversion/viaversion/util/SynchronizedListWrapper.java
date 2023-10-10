// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.util;

import java.util.function.Predicate;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.List;

public final class SynchronizedListWrapper<E> implements List<E>
{
    private final List<E> list;
    private final Consumer<E> addHandler;
    
    public SynchronizedListWrapper(final List<E> inputList, final Consumer<E> addHandler) {
        this.list = inputList;
        this.addHandler = addHandler;
    }
    
    public List<E> originalList() {
        return this.list;
    }
    
    private void handleAdd(final E o) {
        this.addHandler.accept(o);
    }
    
    @Override
    public int size() {
        synchronized (this) {
            return this.list.size();
        }
    }
    
    @Override
    public boolean isEmpty() {
        synchronized (this) {
            return this.list.isEmpty();
        }
    }
    
    @Override
    public boolean contains(final Object o) {
        synchronized (this) {
            return this.list.contains(o);
        }
    }
    
    @Override
    public Iterator<E> iterator() {
        return this.listIterator();
    }
    
    @Override
    public Object[] toArray() {
        synchronized (this) {
            return this.list.toArray();
        }
    }
    
    @Override
    public boolean add(final E o) {
        synchronized (this) {
            this.handleAdd(o);
            return this.list.add(o);
        }
    }
    
    @Override
    public boolean remove(final Object o) {
        synchronized (this) {
            return this.list.remove(o);
        }
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        synchronized (this) {
            for (final E o : c) {
                this.handleAdd(o);
            }
            return this.list.addAll(c);
        }
    }
    
    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        synchronized (this) {
            for (final E o : c) {
                this.handleAdd(o);
            }
            return this.list.addAll(index, c);
        }
    }
    
    @Override
    public void clear() {
        synchronized (this) {
            this.list.clear();
        }
    }
    
    @Override
    public E get(final int index) {
        synchronized (this) {
            return this.list.get(index);
        }
    }
    
    @Override
    public E set(final int index, final E element) {
        synchronized (this) {
            return this.list.set(index, element);
        }
    }
    
    @Override
    public void add(final int index, final E element) {
        synchronized (this) {
            this.list.add(index, element);
        }
    }
    
    @Override
    public E remove(final int index) {
        synchronized (this) {
            return this.list.remove(index);
        }
    }
    
    @Override
    public int indexOf(final Object o) {
        synchronized (this) {
            return this.list.indexOf(o);
        }
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        synchronized (this) {
            return this.list.lastIndexOf(o);
        }
    }
    
    @Override
    public ListIterator<E> listIterator() {
        return this.list.listIterator();
    }
    
    @Override
    public ListIterator<E> listIterator(final int index) {
        return this.list.listIterator(index);
    }
    
    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        synchronized (this) {
            return this.list.subList(fromIndex, toIndex);
        }
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        synchronized (this) {
            return this.list.retainAll(c);
        }
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        synchronized (this) {
            return this.list.removeAll(c);
        }
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        synchronized (this) {
            return this.list.containsAll(c);
        }
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        synchronized (this) {
            return this.list.toArray(a);
        }
    }
    
    @Override
    public void sort(final Comparator<? super E> c) {
        synchronized (this) {
            this.list.sort(c);
        }
    }
    
    @Override
    public void forEach(final Consumer<? super E> consumer) {
        synchronized (this) {
            this.list.forEach(consumer);
        }
    }
    
    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        synchronized (this) {
            return this.list.removeIf(filter);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        synchronized (this) {
            return this.list.equals(o);
        }
    }
    
    @Override
    public int hashCode() {
        synchronized (this) {
            return this.list.hashCode();
        }
    }
    
    @Override
    public String toString() {
        synchronized (this) {
            return this.list.toString();
        }
    }
}
