/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Size64;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntList;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntComparator;
import com.viaversion.viaversion.libs.fastutil.ints.IntComparators;
import com.viaversion.viaversion.libs.fastutil.ints.IntImmutableList;
import com.viaversion.viaversion.libs.fastutil.ints.IntListIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntLists;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import com.viaversion.viaversion.libs.fastutil.ints.IntUnaryOperator;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.UnaryOperator;

public interface IntList
extends List<Integer>,
Comparable<List<? extends Integer>>,
IntCollection {
    @Override
    public IntListIterator iterator();

    @Override
    default public IntSpliterator spliterator() {
        if (this instanceof RandomAccess) {
            return new AbstractIntList.IndexBasedSpliterator(this, 0);
        }
        return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this), 16720);
    }

    public IntListIterator listIterator();

    public IntListIterator listIterator(int var1);

    public IntList subList(int var1, int var2);

    public void size(int var1);

    public void getElements(int var1, int[] var2, int var3, int var4);

    public void removeElements(int var1, int var2);

    public void addElements(int var1, int[] var2);

    public void addElements(int var1, int[] var2, int var3, int var4);

    default public void setElements(int[] a2) {
        this.setElements(0, a2);
    }

    default public void setElements(int index, int[] a2) {
        this.setElements(index, a2, 0, a2.length);
    }

    default public void setElements(int index, int[] a2, int offset, int length) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size() + ")");
        }
        IntArrays.ensureOffsetLength(a2, offset, length);
        if (index + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size() + ")");
        }
        IntListIterator iter = this.listIterator(index);
        int i2 = 0;
        while (i2 < length) {
            iter.nextInt();
            iter.set(a2[offset + i2++]);
        }
    }

    @Override
    public boolean add(int var1);

    @Override
    public void add(int var1, int var2);

    @Override
    @Deprecated
    default public void add(int index, Integer key) {
        this.add(index, (int)key);
    }

    public boolean addAll(int var1, IntCollection var2);

    @Override
    public int set(int var1, int var2);

    default public void replaceAll(java.util.function.IntUnaryOperator operator) {
        IntListIterator iter = this.listIterator();
        while (iter.hasNext()) {
            iter.set(operator.applyAsInt(iter.nextInt()));
        }
    }

    default public void replaceAll(IntUnaryOperator operator) {
        this.replaceAll((java.util.function.IntUnaryOperator)operator);
    }

    @Override
    @Deprecated
    default public void replaceAll(UnaryOperator<Integer> operator) {
        Objects.requireNonNull(operator);
        this.replaceAll(operator instanceof java.util.function.IntUnaryOperator ? (java.util.function.IntUnaryOperator)((Object)operator) : operator::apply);
    }

    public int getInt(int var1);

    public int indexOf(int var1);

    public int lastIndexOf(int var1);

    @Override
    @Deprecated
    default public boolean contains(Object key) {
        return IntCollection.super.contains(key);
    }

    @Override
    @Deprecated
    default public Integer get(int index) {
        return this.getInt(index);
    }

    @Override
    @Deprecated
    default public int indexOf(Object o2) {
        return this.indexOf((Integer)o2);
    }

    @Override
    @Deprecated
    default public int lastIndexOf(Object o2) {
        return this.lastIndexOf((Integer)o2);
    }

    @Override
    @Deprecated
    default public boolean add(Integer k2) {
        return this.add((int)k2);
    }

    public int removeInt(int var1);

    @Override
    @Deprecated
    default public boolean remove(Object key) {
        return IntCollection.super.remove(key);
    }

    @Override
    @Deprecated
    default public Integer remove(int index) {
        return this.removeInt(index);
    }

    @Override
    @Deprecated
    default public Integer set(int index, Integer k2) {
        return this.set(index, (int)k2);
    }

    default public boolean addAll(int index, IntList l2) {
        return this.addAll(index, (IntCollection)l2);
    }

    default public boolean addAll(IntList l2) {
        return this.addAll(this.size(), l2);
    }

    public static IntList of() {
        return IntImmutableList.of();
    }

    public static IntList of(int e2) {
        return IntLists.singleton(e2);
    }

    public static IntList of(int e0, int e1) {
        return IntImmutableList.of(new int[]{e0, e1});
    }

    public static IntList of(int e0, int e1, int e2) {
        return IntImmutableList.of(new int[]{e0, e1, e2});
    }

    public static IntList of(int ... a2) {
        switch (a2.length) {
            case 0: {
                return IntList.of();
            }
            case 1: {
                return IntList.of(a2[0]);
            }
        }
        return IntImmutableList.of(a2);
    }

    @Override
    @Deprecated
    default public void sort(Comparator<? super Integer> comparator) {
        this.sort(IntComparators.asIntComparator(comparator));
    }

    default public void sort(IntComparator comparator) {
        if (comparator == null) {
            this.unstableSort(comparator);
        } else {
            int[] elements = this.toIntArray();
            IntArrays.stableSort(elements, comparator);
            this.setElements(elements);
        }
    }

    @Deprecated
    default public void unstableSort(Comparator<? super Integer> comparator) {
        this.unstableSort(IntComparators.asIntComparator(comparator));
    }

    default public void unstableSort(IntComparator comparator) {
        int[] elements = this.toIntArray();
        if (comparator == null) {
            IntArrays.unstableSort(elements);
        } else {
            IntArrays.unstableSort(elements, comparator);
        }
        this.setElements(elements);
    }
}

