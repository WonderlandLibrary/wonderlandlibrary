// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Comparator;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.Size64;
import java.util.RandomAccess;
import java.util.List;

public interface ObjectList<K> extends List<K>, Comparable<List<? extends K>>, ObjectCollection<K>
{
    ObjectListIterator<K> iterator();
    
    default ObjectSpliterator<K> spliterator() {
        if (this instanceof RandomAccess) {
            return new AbstractObjectList.IndexBasedSpliterator<K>(this, 0);
        }
        return ObjectSpliterators.asSpliterator((ObjectIterator<? extends K>)this.iterator(), Size64.sizeOf(this), 16464);
    }
    
    ObjectListIterator<K> listIterator();
    
    ObjectListIterator<K> listIterator(final int p0);
    
    ObjectList<K> subList(final int p0, final int p1);
    
    void size(final int p0);
    
    void getElements(final int p0, final Object[] p1, final int p2, final int p3);
    
    void removeElements(final int p0, final int p1);
    
    void addElements(final int p0, final K[] p1);
    
    void addElements(final int p0, final K[] p1, final int p2, final int p3);
    
    default void setElements(final K[] a) {
        this.setElements(0, a);
    }
    
    default void setElements(final int index, final K[] a) {
        this.setElements(index, a, 0, a.length);
    }
    
    default void setElements(final int index, final K[] a, final int offset, final int length) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size() + ")");
        }
        ObjectArrays.ensureOffsetLength(a, offset, length);
        if (index + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size() + ")");
        }
        final ObjectListIterator<K> iter = this.listIterator(index);
        int i = 0;
        while (i < length) {
            iter.next();
            iter.set(a[offset + i++]);
        }
    }
    
    default boolean addAll(final int index, final ObjectList<? extends K> l) {
        return this.addAll(index, l);
    }
    
    default boolean addAll(final ObjectList<? extends K> l) {
        return this.addAll(this.size(), l);
    }
    
    default <K> ObjectList<K> of() {
        return (ObjectList<K>)ObjectImmutableList.of();
    }
    
    default <K> ObjectList<K> of(final K e) {
        return ObjectLists.singleton(e);
    }
    
    default <K> ObjectList<K> of(final K e0, final K e1) {
        return (ObjectList<K>)ObjectImmutableList.of(new Object[] { e0, e1 });
    }
    
    default <K> ObjectList<K> of(final K e0, final K e1, final K e2) {
        return (ObjectList<K>)ObjectImmutableList.of(new Object[] { e0, e1, e2 });
    }
    
    @SafeVarargs
    default <K> ObjectList<K> of(final K... a) {
        switch (a.length) {
            case 0: {
                return of();
            }
            case 1: {
                return of(a[0]);
            }
            default: {
                return ObjectImmutableList.of(a);
            }
        }
    }
    
    default void sort(final Comparator<? super K> comparator) {
        final K[] elements = (K[])this.toArray();
        if (comparator == null) {
            ObjectArrays.stableSort(elements);
        }
        else {
            ObjectArrays.stableSort(elements, (Comparator<K>)comparator);
        }
        this.setElements(elements);
    }
    
    default void unstableSort(final Comparator<? super K> comparator) {
        final K[] elements = (K[])this.toArray();
        if (comparator == null) {
            ObjectArrays.unstableSort(elements);
        }
        else {
            ObjectArrays.unstableSort(elements, (Comparator<K>)comparator);
        }
        this.setElements(elements);
    }
}
