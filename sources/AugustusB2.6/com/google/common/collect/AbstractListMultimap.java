// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.Collections;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractListMultimap<K, V> extends AbstractMapBasedMultimap<K, V> implements ListMultimap<K, V>
{
    private static final long serialVersionUID = 6588350623831699109L;
    
    protected AbstractListMultimap(final Map<K, Collection<V>> map) {
        super(map);
    }
    
    @Override
    abstract List<V> createCollection();
    
    @Override
    List<V> createUnmodifiableEmptyCollection() {
        return Collections.emptyList();
    }
    
    @Override
     <E> Collection<E> unmodifiableCollectionSubclass(final Collection<E> collection) {
        return (Collection<E>)Collections.unmodifiableList((List<?>)(List)collection);
    }
    
    @Override
    Collection<V> wrapCollection(@ParametricNullness final K key, final Collection<V> collection) {
        return this.wrapList(key, (List)collection, null);
    }
    
    @Override
    public List<V> get(@ParametricNullness final K key) {
        return (List<V>)(List)super.get(key);
    }
    
    @CanIgnoreReturnValue
    @Override
    public List<V> removeAll(@CheckForNull final Object key) {
        return (List<V>)(List)super.removeAll(key);
    }
    
    @CanIgnoreReturnValue
    @Override
    public List<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
        return (List<V>)(List)super.replaceValues(key, values);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean put(@ParametricNullness final K key, @ParametricNullness final V value) {
        return super.put(key, value);
    }
    
    @Override
    public Map<K, Collection<V>> asMap() {
        return super.asMap();
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        return super.equals(object);
    }
}
