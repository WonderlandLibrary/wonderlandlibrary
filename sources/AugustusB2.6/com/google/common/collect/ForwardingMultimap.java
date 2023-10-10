// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;
import javax.annotation.CheckForNull;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingMultimap<K, V> extends ForwardingObject implements Multimap<K, V>
{
    protected ForwardingMultimap() {
    }
    
    @Override
    protected abstract Multimap<K, V> delegate();
    
    @Override
    public Map<K, Collection<V>> asMap() {
        return this.delegate().asMap();
    }
    
    @Override
    public void clear() {
        this.delegate().clear();
    }
    
    @Override
    public boolean containsEntry(@CheckForNull final Object key, @CheckForNull final Object value) {
        return this.delegate().containsEntry(key, value);
    }
    
    @Override
    public boolean containsKey(@CheckForNull final Object key) {
        return this.delegate().containsKey(key);
    }
    
    @Override
    public boolean containsValue(@CheckForNull final Object value) {
        return this.delegate().containsValue(value);
    }
    
    @Override
    public Collection<Map.Entry<K, V>> entries() {
        return this.delegate().entries();
    }
    
    @Override
    public Collection<V> get(@ParametricNullness final K key) {
        return this.delegate().get(key);
    }
    
    @Override
    public boolean isEmpty() {
        return this.delegate().isEmpty();
    }
    
    @Override
    public Multiset<K> keys() {
        return this.delegate().keys();
    }
    
    @Override
    public Set<K> keySet() {
        return this.delegate().keySet();
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean put(@ParametricNullness final K key, @ParametricNullness final V value) {
        return this.delegate().put(key, value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean putAll(@ParametricNullness final K key, final Iterable<? extends V> values) {
        return this.delegate().putAll(key, values);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
        return this.delegate().putAll(multimap);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean remove(@CheckForNull final Object key, @CheckForNull final Object value) {
        return this.delegate().remove(key, value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public Collection<V> removeAll(@CheckForNull final Object key) {
        return this.delegate().removeAll(key);
    }
    
    @CanIgnoreReturnValue
    @Override
    public Collection<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
        return this.delegate().replaceValues(key, values);
    }
    
    @Override
    public int size() {
        return this.delegate().size();
    }
    
    @Override
    public Collection<V> values() {
        return this.delegate().values();
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        return object == this || this.delegate().equals(object);
    }
    
    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }
}
