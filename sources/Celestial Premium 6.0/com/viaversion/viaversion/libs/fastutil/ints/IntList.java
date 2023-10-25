/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntComparator;
import com.viaversion.viaversion.libs.fastutil.ints.IntComparators;
import com.viaversion.viaversion.libs.fastutil.ints.IntListIterator;
import java.util.Comparator;
import java.util.List;

public interface IntList
extends List<Integer>,
Comparable<List<? extends Integer>>,
IntCollection {
    @Override
    public IntListIterator iterator();

    public IntListIterator listIterator();

    public IntListIterator listIterator(int var1);

    public IntList subList(int var1, int var2);

    public void size(int var1);

    public void getElements(int var1, int[] var2, int var3, int var4);

    public void removeElements(int var1, int var2);

    public void addElements(int var1, int[] var2);

    public void addElements(int var1, int[] var2, int var3, int var4);

    default public void setElements(int[] a) {
        this.setElements(0, a);
    }

    default public void setElements(int index, int[] a) {
        this.setElements(index, a, 0, a.length);
    }

    default public void setElements(int index, int[] a, int offset, int length) {
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
        IntListIterator iter = this.listIterator(index);
        int i = 0;
        while (i < length) {
            iter.nextInt();
            iter.set(a[offset + i++]);
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

    public boolean addAll(int var1, IntList var2);

    public boolean addAll(IntList var1);

    @Override
    public int set(int var1, int var2);

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
    default public int indexOf(Object o) {
        return this.indexOf((Integer)o);
    }

    @Override
    @Deprecated
    default public int lastIndexOf(Object o) {
        return this.lastIndexOf((Integer)o);
    }

    @Override
    @Deprecated
    default public boolean add(Integer k) {
        return this.add((int)k);
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
    default public Integer set(int index, Integer k) {
        return this.set(index, (int)k);
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

