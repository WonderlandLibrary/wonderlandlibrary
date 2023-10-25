/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.ints.IntIterable;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import java.util.Collection;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public interface IntCollection
extends Collection<Integer>,
IntIterable {
    @Override
    public IntIterator iterator();

    @Override
    public boolean add(int var1);

    public boolean contains(int var1);

    public boolean rem(int var1);

    @Override
    @Deprecated
    default public boolean add(Integer key) {
        return this.add((int)key);
    }

    @Override
    @Deprecated
    default public boolean contains(Object key) {
        if (key == null) {
            return false;
        }
        return this.contains((Integer)key);
    }

    @Override
    @Deprecated
    default public boolean remove(Object key) {
        if (key == null) {
            return false;
        }
        return this.rem((Integer)key);
    }

    public int[] toIntArray();

    @Deprecated
    public int[] toIntArray(int[] var1);

    public int[] toArray(int[] var1);

    public boolean addAll(IntCollection var1);

    public boolean containsAll(IntCollection var1);

    public boolean removeAll(IntCollection var1);

    @Override
    @Deprecated
    default public boolean removeIf(Predicate<? super Integer> filter) {
        return this.removeIf((int key) -> filter.test(key));
    }

    default public boolean removeIf(IntPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        IntIterator each = this.iterator();
        while (each.hasNext()) {
            if (!filter.test(each.nextInt())) continue;
            each.remove();
            removed = true;
        }
        return removed;
    }

    public boolean retainAll(IntCollection var1);
}

