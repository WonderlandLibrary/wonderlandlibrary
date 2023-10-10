/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.Size64;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectImmutableList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectListIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectLists;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectSpliterators;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public interface ObjectList<K>
extends List<K>,
Comparable<List<? extends K>>,
ObjectCollection<K> {
    @Override
    public ObjectListIterator<K> iterator();

    @Override
    default public ObjectSpliterator<K> spliterator() {
        if (this instanceof RandomAccess) {
            return new AbstractObjectList.IndexBasedSpliterator(this, 0);
        }
        return ObjectSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this), 16464);
    }

    @Override
    public ObjectListIterator<K> listIterator();

    @Override
    public ObjectListIterator<K> listIterator(int var1);

    @Override
    public ObjectList<K> subList(int var1, int var2);

    public void size(int var1);

    public void getElements(int var1, Object[] var2, int var3, int var4);

    public void removeElements(int var1, int var2);

    public void addElements(int var1, K[] var2);

    public void addElements(int var1, K[] var2, int var3, int var4);

    default public void setElements(K[] a2) {
        this.setElements(0, a2);
    }

    default public void setElements(int index, K[] a2) {
        this.setElements(index, a2, 0, a2.length);
    }

    default public void setElements(int index, K[] a2, int offset, int length) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > this.size()) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size() + ")");
        }
        ObjectArrays.ensureOffsetLength(a2, offset, length);
        if (index + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size() + ")");
        }
        ListIterator iter = this.listIterator(index);
        int i2 = 0;
        while (i2 < length) {
            iter.next();
            iter.set(a2[offset + i2++]);
        }
    }

    @Override
    default public boolean addAll(int index, ObjectList<? extends K> l2) {
        return this.addAll(index, l2);
    }

    @Override
    default public boolean addAll(ObjectList<? extends K> l2) {
        return this.addAll(this.size(), l2);
    }

    public static <K> ObjectList<K> of() {
        return ObjectImmutableList.of();
    }

    public static <K> ObjectList<K> of(K e2) {
        return ObjectLists.singleton(e2);
    }

    public static <K> ObjectList<K> of(K e0, K e1) {
        return ObjectImmutableList.of(new Object[]{e0, e1});
    }

    public static <K> ObjectList<K> of(K e0, K e1, K e2) {
        return ObjectImmutableList.of(new Object[]{e0, e1, e2});
    }

    @SafeVarargs
    public static <K> ObjectList<K> of(K ... a2) {
        switch (a2.length) {
            case 0: {
                return ObjectList.of();
            }
            case 1: {
                return ObjectList.of(a2[0]);
            }
        }
        return ObjectImmutableList.of(a2);
    }

    @Override
    default public void sort(Comparator<? super K> comparator) {
        Object[] elements = this.toArray();
        if (comparator == null) {
            ObjectArrays.stableSort(elements);
        } else {
            ObjectArrays.stableSort(elements, comparator);
        }
        this.setElements(elements);
    }

    default public void unstableSort(Comparator<? super K> comparator) {
        Object[] elements = this.toArray();
        if (comparator == null) {
            ObjectArrays.unstableSort(elements);
        } else {
            ObjectArrays.unstableSort(elements, comparator);
        }
        this.setElements(elements);
    }
}

