// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.utils;

import java.util.NoSuchElementException;
import java.util.Iterator;

public final class Array<T> implements Iterable<T>
{
    public T[] items;
    public int size;
    private boolean ordered;
    private ArrayIterable iterable;
    
    public Array() {
        this(true, 16);
    }
    
    private Array(final boolean ordered, final int capacity) {
        this.ordered = true;
        this.items = (T[])new Object[16];
    }
    
    private Array(final boolean ordered, int capacity, final Class arrayType) {
        this.ordered = ordered;
        capacity = capacity;
        this.items = (T[])java.lang.reflect.Array.newInstance(arrayType, capacity);
    }
    
    public Array(final Array<? extends T> array) {
        this(array.ordered, array.size, array.items.getClass().getComponentType());
        this.size = array.size;
        System.arraycopy(array.items, 0, this.items, 0, this.size);
    }
    
    public Array(final T[] array) {
        this(true, array, 0, array.length);
    }
    
    private Array(final boolean ordered, final T[] array, final int start, final int count) {
        this(true, count, array.getClass().getComponentType());
        this.size = count;
        System.arraycopy(array, 0, this.items, 0, this.size);
    }
    
    public final void add(final T value) {
        T[] items = this.items;
        if (this.size == items.length) {
            final int max = Math.max(8, (int)(this.size * 1.75f));
            items = (T[])(Object)this;
            final Object[] items3;
            final Object[] items2 = (Object[])java.lang.reflect.Array.newInstance((items3 = this.items).getClass().getComponentType(), max);
            System.arraycopy(items3, 0, items2, 0, Math.min(((Array)(Object)items).size, items2.length));
            ((Array)(Object)items).items = (T[])items2;
            items = (T[])items2;
        }
        items[this.size++] = value;
    }
    
    public final T get(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size);
        }
        return this.items[index];
    }
    
    public final int indexOf(final T value, final boolean identity) {
        final T[] items = this.items;
        for (int i = 0, n = this.size; i < n; ++i) {
            if (items[i] == value) {
                return i;
            }
        }
        return -1;
    }
    
    public final T removeIndex(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size);
        }
        final T[] items;
        final T value = (items = this.items)[index];
        --this.size;
        if (this.ordered) {
            System.arraycopy(items, index + 1, items, index, this.size - index);
        }
        else {
            items[index] = items[this.size];
        }
        items[this.size] = null;
        return value;
    }
    
    public final void clear() {
        final T[] items = this.items;
        for (int i = 0, n = this.size; i < n; ++i) {
            items[i] = null;
        }
        this.size = 0;
    }
    
    public final void sort() {
        Sort.instance().sort(this.items, 0, this.size);
    }
    
    @Override
    public final Iterator<T> iterator() {
        if (this.iterable == null) {
            this.iterable = new ArrayIterable((Array<T>)this);
        }
        return this.iterable.iterator();
    }
    
    @Override
    public final int hashCode() {
        if (!this.ordered) {
            return super.hashCode();
        }
        final Object[] items = this.items;
        int h = 1;
        for (int i = 0, n = this.size; i < n; ++i) {
            h *= 31;
            final Object item;
            if ((item = items[i]) != null) {
                h += item.hashCode();
            }
        }
        return h;
    }
    
    @Override
    public final boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!this.ordered) {
            return false;
        }
        if (!(object instanceof Array)) {
            return false;
        }
        final Array array;
        if (!(array = (Array)object).ordered) {
            return false;
        }
        final int n;
        if ((n = this.size) != array.size) {
            return false;
        }
        final Object[] items1 = this.items;
        final Object[] items2 = array.items;
        for (int i = 0; i < n; ++i) {
            final Object o1 = items1[i];
            final Object o2 = items2[i];
            if (o1 == null) {
                if (o2 != null) {
                    return false;
                }
            }
            else if (!o1.equals(o2)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public final String toString() {
        if (this.size == 0) {
            return "[]";
        }
        final T[] items = this.items;
        final com.badlogic.gdx.utils.StringBuilder buffer;
        (buffer = new com.badlogic.gdx.utils.StringBuilder(32)).append('[');
        buffer.append(items[0]);
        for (int i = 1; i < this.size; ++i) {
            buffer.append(", ");
            buffer.append(items[i]);
        }
        buffer.append(']');
        return buffer.toString();
    }
    
    public static final class ArrayIterator<T> implements Iterable<T>, Iterator<T>
    {
        private final Array<T> array;
        private final boolean allowRemove;
        int index;
        boolean valid;
        
        public ArrayIterator(final Array<T> array, final boolean allowRemove) {
            this.valid = true;
            this.array = array;
            this.allowRemove = allowRemove;
        }
        
        @Override
        public final boolean hasNext() {
            if (!this.valid) {
                throw new GdxRuntimeException("#iterator() cannot be used nested.");
            }
            return this.index < this.array.size;
        }
        
        @Override
        public final T next() {
            if (this.index >= this.array.size) {
                throw new NoSuchElementException(String.valueOf(this.index));
            }
            if (!this.valid) {
                throw new GdxRuntimeException("#iterator() cannot be used nested.");
            }
            return this.array.items[this.index++];
        }
        
        @Override
        public final void remove() {
            if (!this.allowRemove) {
                throw new GdxRuntimeException("Remove not allowed.");
            }
            --this.index;
            this.array.removeIndex(this.index);
        }
        
        @Override
        public final Iterator<T> iterator() {
            return this;
        }
    }
    
    public static final class ArrayIterable<T> implements Iterable<T>
    {
        private final Array<T> array;
        private final boolean allowRemove;
        private ArrayIterator iterator1;
        private ArrayIterator iterator2;
        
        public ArrayIterable(final Array<T> array) {
            this(array, true);
        }
        
        private ArrayIterable(final Array<T> array, final boolean allowRemove) {
            this.array = array;
            this.allowRemove = true;
        }
        
        @Override
        public final Iterator<T> iterator() {
            if (this.iterator1 == null) {
                this.iterator1 = new ArrayIterator((Array<T>)this.array, this.allowRemove);
                this.iterator2 = new ArrayIterator((Array<T>)this.array, this.allowRemove);
            }
            if (!this.iterator1.valid) {
                this.iterator1.index = 0;
                this.iterator1.valid = true;
                this.iterator2.valid = false;
                return (Iterator<T>)this.iterator1;
            }
            this.iterator2.index = 0;
            this.iterator2.valid = true;
            this.iterator1.valid = false;
            return (Iterator<T>)this.iterator2;
        }
    }
}
