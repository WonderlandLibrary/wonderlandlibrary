/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.RegularImmutableAsList;
import com.google.common.collect.RegularImmutableSortedSet;
import com.google.common.collect.SortedIterable;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
final class ImmutableSortedAsList<E>
extends RegularImmutableAsList<E>
implements SortedIterable<E> {
    ImmutableSortedAsList(ImmutableSortedSet<E> backingSet, ImmutableList<E> backingList) {
        super(backingSet, backingList);
    }

    @Override
    ImmutableSortedSet<E> delegateCollection() {
        return (ImmutableSortedSet)super.delegateCollection();
    }

    @Override
    public Comparator<? super E> comparator() {
        return ((ImmutableSortedSet)this.delegateCollection()).comparator();
    }

    @Override
    @GwtIncompatible(value="ImmutableSortedSet.indexOf")
    public int indexOf(@Nullable Object target) {
        int index = ((ImmutableSortedSet)this.delegateCollection()).indexOf(target);
        return index >= 0 && this.get(index).equals(target) ? index : -1;
    }

    @Override
    @GwtIncompatible(value="ImmutableSortedSet.indexOf")
    public int lastIndexOf(@Nullable Object target) {
        return this.indexOf(target);
    }

    @Override
    public boolean contains(Object target) {
        return this.indexOf(target) >= 0;
    }

    @Override
    @GwtIncompatible(value="super.subListUnchecked does not exist; inherited subList is valid if slow")
    ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
        return new RegularImmutableSortedSet(super.subListUnchecked(fromIndex, toIndex), this.comparator()).asList();
    }
}

