// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.function.IntFunction;
import java.util.Objects;
import java.util.Spliterator;
import com.google.common.annotations.GwtIncompatible;
import javax.annotation.CheckForNull;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
final class ImmutableSortedAsList<E> extends RegularImmutableAsList<E> implements SortedIterable<E>
{
    ImmutableSortedAsList(final ImmutableSortedSet<E> backingSet, final ImmutableList<E> backingList) {
        super(backingSet, (ImmutableList<? extends E>)backingList);
    }
    
    @Override
    ImmutableSortedSet<E> delegateCollection() {
        return (ImmutableSortedSet<E>)(ImmutableSortedSet)super.delegateCollection();
    }
    
    @Override
    public Comparator<? super E> comparator() {
        return this.delegateCollection().comparator();
    }
    
    @GwtIncompatible
    @Override
    public int indexOf(@CheckForNull final Object target) {
        final int index = this.delegateCollection().indexOf(target);
        return (index >= 0 && this.get(index).equals(target)) ? index : -1;
    }
    
    @GwtIncompatible
    @Override
    public int lastIndexOf(@CheckForNull final Object target) {
        return this.indexOf(target);
    }
    
    @Override
    public boolean contains(@CheckForNull final Object target) {
        return this.indexOf(target) >= 0;
    }
    
    @GwtIncompatible
    @Override
    ImmutableList<E> subListUnchecked(final int fromIndex, final int toIndex) {
        final ImmutableList<E> parentSubList = super.subListUnchecked(fromIndex, toIndex);
        return (ImmutableList<E>)new RegularImmutableSortedSet<Object>((ImmutableList<Object>)parentSubList, (Comparator<? super Object>)this.comparator()).asList();
    }
    
    @Override
    public Spliterator<E> spliterator() {
        final int size = this.size();
        final int extraCharacteristics = 1301;
        final ImmutableList<? extends E> delegateList = this.delegateList();
        Objects.requireNonNull(delegateList);
        return CollectSpliterators.indexed(size, extraCharacteristics, (IntFunction<E>)delegateList::get, this.comparator());
    }
}
