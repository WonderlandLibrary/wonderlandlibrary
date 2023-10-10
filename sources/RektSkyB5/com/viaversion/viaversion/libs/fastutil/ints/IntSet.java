/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Size64;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntArraySet;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSets;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntSpliterators;
import java.util.Set;

public interface IntSet
extends IntCollection,
Set<Integer> {
    @Override
    public IntIterator iterator();

    @Override
    default public IntSpliterator spliterator() {
        return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this), 321);
    }

    public boolean remove(int var1);

    @Override
    @Deprecated
    default public boolean remove(Object o2) {
        return IntCollection.super.remove(o2);
    }

    @Override
    @Deprecated
    default public boolean add(Integer o2) {
        return IntCollection.super.add(o2);
    }

    @Override
    @Deprecated
    default public boolean contains(Object o2) {
        return IntCollection.super.contains(o2);
    }

    @Override
    @Deprecated
    default public boolean rem(int k2) {
        return this.remove(k2);
    }

    public static IntSet of() {
        return IntSets.UNMODIFIABLE_EMPTY_SET;
    }

    public static IntSet of(int e2) {
        return IntSets.singleton(e2);
    }

    public static IntSet of(int e0, int e1) {
        IntArraySet innerSet = new IntArraySet(2);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return IntSets.unmodifiable(innerSet);
    }

    public static IntSet of(int e0, int e1, int e2) {
        IntArraySet innerSet = new IntArraySet(3);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!innerSet.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return IntSets.unmodifiable(innerSet);
    }

    public static IntSet of(int ... a2) {
        switch (a2.length) {
            case 0: {
                return IntSet.of();
            }
            case 1: {
                return IntSet.of(a2[0]);
            }
            case 2: {
                return IntSet.of(a2[0], a2[1]);
            }
            case 3: {
                return IntSet.of(a2[0], a2[1], a2[2]);
            }
        }
        AbstractIntSet innerSet = a2.length <= 4 ? new IntArraySet(a2.length) : new IntOpenHashSet(a2.length);
        for (int element : a2) {
            if (innerSet.add(element)) continue;
            throw new IllegalArgumentException("Duplicate element: " + element);
        }
        return IntSets.unmodifiable(innerSet);
    }
}

