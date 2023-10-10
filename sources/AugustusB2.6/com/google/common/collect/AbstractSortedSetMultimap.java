// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.Collections;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractSortedSetMultimap<K, V> extends AbstractSetMultimap<K, V> implements SortedSetMultimap<K, V>
{
    private static final long serialVersionUID = 430848587173315748L;
    
    protected AbstractSortedSetMultimap(final Map<K, Collection<V>> map) {
        super(map);
    }
    
    @Override
    abstract SortedSet<V> createCollection();
    
    @Override
    SortedSet<V> createUnmodifiableEmptyCollection() {
        return this.unmodifiableCollectionSubclass(this.createCollection());
    }
    
    @Override
     <E> SortedSet<E> unmodifiableCollectionSubclass(final Collection<E> collection) {
        if (collection instanceof NavigableSet) {
            return (SortedSet<E>)Sets.unmodifiableNavigableSet((NavigableSet<Object>)(NavigableSet)collection);
        }
        return Collections.unmodifiableSortedSet((SortedSet<E>)(SortedSet)collection);
    }
    
    @Override
    Collection<V> wrapCollection(@ParametricNullness final K key, final Collection<V> collection) {
        if (collection instanceof NavigableSet) {
            return (Collection<V>)new WrappedNavigableSet((K)key, (NavigableSet)collection, null);
        }
        return (Collection<V>)new WrappedSortedSet((K)key, (SortedSet)collection, null);
    }
    
    @Override
    public SortedSet<V> get(@ParametricNullness final K key) {
        return (SortedSet<V>)(SortedSet)super.get(key);
    }
    
    @CanIgnoreReturnValue
    @Override
    public SortedSet<V> removeAll(@CheckForNull final Object key) {
        return (SortedSet<V>)(SortedSet)super.removeAll(key);
    }
    
    @CanIgnoreReturnValue
    @Override
    public SortedSet<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
        return (SortedSet<V>)(SortedSet)super.replaceValues(key, values);
    }
    
    @Override
    public Map<K, Collection<V>> asMap() {
        return super.asMap();
    }
    
    @Override
    public Collection<V> values() {
        return super.values();
    }
}
