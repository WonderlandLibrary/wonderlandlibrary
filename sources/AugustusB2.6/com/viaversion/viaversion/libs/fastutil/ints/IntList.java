// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.Size64;
import java.util.RandomAccess;
import java.util.List;

public interface IntList extends List<Integer>, Comparable<List<? extends Integer>>, IntCollection
{
    IntListIterator iterator();
    
    default IntSpliterator spliterator() {
        if (this instanceof RandomAccess) {
            return new AbstractIntList.IndexBasedSpliterator(this, 0);
        }
        return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this), 16720);
    }
    
    IntListIterator listIterator();
    
    IntListIterator listIterator(final int p0);
    
    IntList subList(final int p0, final int p1);
    
    void size(final int p0);
    
    void getElements(final int p0, final int[] p1, final int p2, final int p3);
    
    void removeElements(final int p0, final int p1);
    
    void addElements(final int p0, final int[] p1);
    
    void addElements(final int p0, final int[] p1, final int p2, final int p3);
    
    default void setElements(final int[] a) {
        this.setElements(0, a);
    }
    
    default void setElements(final int index, final int[] a) {
        this.setElements(index, a, 0, a.length);
    }
    
    default void setElements(final int index, final int[] a, final int offset, final int length) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size() + ")");
        }
        IntArrays.ensureOffsetLength(a, offset, length);
        if (index + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size() + ")");
        }
        final IntListIterator iter = this.listIterator(index);
        int i = 0;
        while (i < length) {
            iter.nextInt();
            iter.set(a[offset + i++]);
        }
    }
    
    boolean add(final int p0);
    
    void add(final int p0, final int p1);
    
    @Deprecated
    default void add(final int index, final Integer key) {
        this.add(index, (int)key);
    }
    
    boolean addAll(final int p0, final IntCollection p1);
    
    int set(final int p0, final int p1);
    
    default void replaceAll(final IntUnaryOperator operator) {
        final IntListIterator iter = this.listIterator();
        while (iter.hasNext()) {
            iter.set(operator.applyAsInt(iter.nextInt()));
        }
    }
    
    default void replaceAll(final com.viaversion.viaversion.libs.fastutil.ints.IntUnaryOperator operator) {
        this.replaceAll((IntUnaryOperator)operator);
    }
    
    @Deprecated
    default void replaceAll(final UnaryOperator<Integer> operator) {
        Objects.requireNonNull(operator);
        IntUnaryOperator operator2;
        if (operator instanceof IntUnaryOperator) {
            operator2 = (IntUnaryOperator)operator;
        }
        else {
            Objects.requireNonNull(operator);
            operator2 = operator::apply;
        }
        this.replaceAll(operator2);
    }
    
    int getInt(final int p0);
    
    int indexOf(final int p0);
    
    int lastIndexOf(final int p0);
    
    @Deprecated
    default boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    default Integer get(final int index) {
        return this.getInt(index);
    }
    
    @Deprecated
    default int indexOf(final Object o) {
        return this.indexOf((int)o);
    }
    
    @Deprecated
    default int lastIndexOf(final Object o) {
        return this.lastIndexOf((int)o);
    }
    
    @Deprecated
    default boolean add(final Integer k) {
        return this.add((int)k);
    }
    
    int removeInt(final int p0);
    
    @Deprecated
    default boolean remove(final Object key) {
        return super.remove(key);
    }
    
    @Deprecated
    default Integer remove(final int index) {
        return this.removeInt(index);
    }
    
    @Deprecated
    default Integer set(final int index, final Integer k) {
        return this.set(index, (int)k);
    }
    
    default boolean addAll(final int index, final IntList l) {
        return this.addAll(index, (IntCollection)l);
    }
    
    default boolean addAll(final IntList l) {
        return this.addAll(this.size(), l);
    }
    
    default IntList of() {
        return IntImmutableList.of();
    }
    
    default IntList of(final int e) {
        return IntLists.singleton(e);
    }
    
    default IntList of(final int e0, final int e1) {
        return IntImmutableList.of(new int[] { e0, e1 });
    }
    
    default IntList of(final int e0, final int e1, final int e2) {
        return IntImmutableList.of(new int[] { e0, e1, e2 });
    }
    
    default IntList of(final int... a) {
        switch (a.length) {
            case 0: {
                return of();
            }
            case 1: {
                return of(a[0]);
            }
            default: {
                return IntImmutableList.of(a);
            }
        }
    }
    
    @Deprecated
    default void sort(final Comparator<? super Integer> comparator) {
        this.sort(IntComparators.asIntComparator(comparator));
    }
    
    default void sort(final IntComparator comparator) {
        if (comparator == null) {
            this.unstableSort(comparator);
        }
        else {
            final int[] elements = this.toIntArray();
            IntArrays.stableSort(elements, comparator);
            this.setElements(elements);
        }
    }
    
    @Deprecated
    default void unstableSort(final Comparator<? super Integer> comparator) {
        this.unstableSort(IntComparators.asIntComparator(comparator));
    }
    
    default void unstableSort(final IntComparator comparator) {
        final int[] elements = this.toIntArray();
        if (comparator == null) {
            IntArrays.unstableSort(elements);
        }
        else {
            IntArrays.unstableSort(elements, comparator);
        }
        this.setElements(elements);
    }
}
