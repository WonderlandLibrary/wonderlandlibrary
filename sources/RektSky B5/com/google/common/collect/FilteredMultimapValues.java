/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FilteredMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
final class FilteredMultimapValues<K, V>
extends AbstractCollection<V> {
    private final FilteredMultimap<K, V> multimap;

    FilteredMultimapValues(FilteredMultimap<K, V> multimap) {
        this.multimap = Preconditions.checkNotNull(multimap);
    }

    @Override
    public Iterator<V> iterator() {
        return Maps.valueIterator(this.multimap.entries().iterator());
    }

    @Override
    public boolean contains(@Nullable Object o2) {
        return this.multimap.containsValue(o2);
    }

    @Override
    public int size() {
        return this.multimap.size();
    }

    @Override
    public boolean remove(@Nullable Object o2) {
        Predicate<Map.Entry<K, V>> entryPredicate = this.multimap.entryPredicate();
        Iterator<Map.Entry<K, V>> unfilteredItr = this.multimap.unfiltered().entries().iterator();
        while (unfilteredItr.hasNext()) {
            Map.Entry<K, V> entry = unfilteredItr.next();
            if (!entryPredicate.apply(entry) || !Objects.equal(entry.getValue(), o2)) continue;
            unfilteredItr.remove();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c2) {
        return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.in(c2))));
    }

    @Override
    public boolean retainAll(Collection<?> c2) {
        return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c2)))));
    }

    @Override
    public void clear() {
        this.multimap.clear();
    }
}

