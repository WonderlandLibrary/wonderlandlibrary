// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.collect;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class AbstractSetMultimap<K, V> extends AbstractMapBasedMultimap<K, V> implements SetMultimap<K, V>
{
    private static final long serialVersionUID = 7431625294878419160L;
    
    protected AbstractSetMultimap(final Map<K, Collection<V>> map) {
        super(map);
    }
    
    @Override
    abstract Set<V> createCollection();
    
    @Override
    Set<V> createUnmodifiableEmptyCollection() {
        return (Set<V>)ImmutableSet.of();
    }
    
    @Override
    public Set<V> get(@Nullable final K key) {
        return (Set)super.get(key);
    }
    
    @Override
    public Set<Map.Entry<K, V>> entries() {
        return (Set)super.entries();
    }
    
    @Override
    public Set<V> removeAll(@Nullable final Object key) {
        return (Set)super.removeAll(key);
    }
    
    @Override
    public Set<V> replaceValues(@Nullable final K key, final Iterable<? extends V> values) {
        return (Set)super.replaceValues(key, values);
    }
    
    @Override
    public Map<K, Collection<V>> asMap() {
        return super.asMap();
    }
    
    @Override
    public boolean put(@Nullable final K key, @Nullable final V value) {
        return super.put(key, value);
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        return super.equals(object);
    }
}
