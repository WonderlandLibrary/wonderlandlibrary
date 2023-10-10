// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import javax.annotation.CheckForNull;
import java.util.SortedSet;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingSortedSetMultimap<K, V> extends ForwardingSetMultimap<K, V> implements SortedSetMultimap<K, V>
{
    protected ForwardingSortedSetMultimap() {
    }
    
    @Override
    protected abstract SortedSetMultimap<K, V> delegate();
    
    @Override
    public SortedSet<V> get(@ParametricNullness final K key) {
        return this.delegate().get(key);
    }
    
    @Override
    public SortedSet<V> removeAll(@CheckForNull final Object key) {
        return this.delegate().removeAll(key);
    }
    
    @Override
    public SortedSet<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
        return this.delegate().replaceValues(key, values);
    }
    
    @CheckForNull
    @Override
    public Comparator<? super V> valueComparator() {
        return this.delegate().valueComparator();
    }
}
