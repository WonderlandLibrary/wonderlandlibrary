// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Collection;
import com.viaversion.viaversion.libs.fastutil.Size64;
import java.util.Set;

public interface IntSet extends IntCollection, Set<Integer>
{
    IntIterator iterator();
    
    default IntSpliterator spliterator() {
        return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this), 321);
    }
    
    boolean remove(final int p0);
    
    @Deprecated
    default boolean remove(final Object o) {
        return super.remove(o);
    }
    
    @Deprecated
    default boolean add(final Integer o) {
        return super.add(o);
    }
    
    @Deprecated
    default boolean contains(final Object o) {
        return super.contains(o);
    }
    
    @Deprecated
    default boolean rem(final int k) {
        return this.remove(k);
    }
    
    default IntSet of() {
        return IntSets.UNMODIFIABLE_EMPTY_SET;
    }
    
    default IntSet of(final int e) {
        return IntSets.singleton(e);
    }
    
    default IntSet of(final int e0, final int e1) {
        final IntArraySet innerSet = new IntArraySet(2);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        return IntSets.unmodifiable(innerSet);
    }
    
    default IntSet of(final int e0, final int e1, final int e2) {
        final IntArraySet innerSet = new IntArraySet(3);
        innerSet.add(e0);
        if (!innerSet.add(e1)) {
            throw new IllegalArgumentException("Duplicate element: " + e1);
        }
        if (!innerSet.add(e2)) {
            throw new IllegalArgumentException("Duplicate element: " + e2);
        }
        return IntSets.unmodifiable(innerSet);
    }
    
    default IntSet of(final int... a) {
        switch (a.length) {
            case 0: {
                return of();
            }
            case 1: {
                return of(a[0]);
            }
            case 2: {
                return of(a[0], a[1]);
            }
            case 3: {
                return of(a[0], a[1], a[2]);
            }
            default: {
                final IntSet innerSet = (IntSet)((a.length <= 4) ? new IntArraySet(a.length) : new IntOpenHashSet(a.length));
                for (final int element : a) {
                    if (!innerSet.add(element)) {
                        throw new IllegalArgumentException("Duplicate element: " + element);
                    }
                }
                return IntSets.unmodifiable(innerSet);
            }
        }
    }
}
