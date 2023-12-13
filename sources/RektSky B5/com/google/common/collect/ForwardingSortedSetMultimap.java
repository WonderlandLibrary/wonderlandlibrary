/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ForwardingSetMultimap;
import com.google.common.collect.SortedSetMultimap;
import java.util.Comparator;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingSortedSetMultimap<K, V>
extends ForwardingSetMultimap<K, V>
implements SortedSetMultimap<K, V> {
    protected ForwardingSortedSetMultimap() {
    }

    @Override
    protected abstract SortedSetMultimap<K, V> delegate();

    @Override
    public SortedSet<V> get(@Nullable K key) {
        return this.delegate().get(key);
    }

    @Override
    public SortedSet<V> removeAll(@Nullable Object key) {
        return this.delegate().removeAll(key);
    }

    @Override
    public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
        return this.delegate().replaceValues(key, values);
    }

    @Override
    public Comparator<? super V> valueComparator() {
        return this.delegate().valueComparator();
    }
}

