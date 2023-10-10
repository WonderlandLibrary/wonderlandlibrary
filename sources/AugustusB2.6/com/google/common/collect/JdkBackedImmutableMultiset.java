// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import com.google.common.primitives.Ints;
import java.util.List;
import com.google.common.base.Preconditions;
import java.util.Collection;
import javax.annotation.CheckForNull;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class JdkBackedImmutableMultiset<E> extends ImmutableMultiset<E>
{
    private final Map<E, Integer> delegateMap;
    private final ImmutableList<Multiset.Entry<E>> entries;
    private final long size;
    @CheckForNull
    private transient ImmutableSet<E> elementSet;
    
    static <E> ImmutableMultiset<E> create(final Collection<? extends Multiset.Entry<? extends E>> entries) {
        final Multiset.Entry<E>[] entriesArray = entries.toArray(new Multiset.Entry[0]);
        final Map<E, Integer> delegateMap = (Map<E, Integer>)Maps.newHashMapWithExpectedSize(entriesArray.length);
        long size = 0L;
        for (int i = 0; i < entriesArray.length; ++i) {
            final Multiset.Entry<E> entry = entriesArray[i];
            final int count = entry.getCount();
            size += count;
            final E element = Preconditions.checkNotNull(entry.getElement());
            delegateMap.put(element, count);
            if (!(entry instanceof Multisets.ImmutableEntry)) {
                entriesArray[i] = Multisets.immutableEntry(element, count);
            }
        }
        return new JdkBackedImmutableMultiset<E>(delegateMap, ImmutableList.asImmutableList(entriesArray), size);
    }
    
    private JdkBackedImmutableMultiset(final Map<E, Integer> delegateMap, final ImmutableList<Multiset.Entry<E>> entries, final long size) {
        this.delegateMap = delegateMap;
        this.entries = entries;
        this.size = size;
    }
    
    @Override
    public int count(@CheckForNull final Object element) {
        return this.delegateMap.getOrDefault(element, 0);
    }
    
    @Override
    public ImmutableSet<E> elementSet() {
        final ImmutableSet<E> result = this.elementSet;
        return (result == null) ? (this.elementSet = new ElementSet<E>(this.entries, this)) : result;
    }
    
    @Override
    Multiset.Entry<E> getEntry(final int index) {
        return this.entries.get(index);
    }
    
    @Override
    boolean isPartialView() {
        return false;
    }
    
    @Override
    public int size() {
        return Ints.saturatedCast(this.size);
    }
}
