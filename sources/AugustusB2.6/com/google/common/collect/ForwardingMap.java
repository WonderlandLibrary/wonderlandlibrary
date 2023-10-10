// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.util.Iterator;
import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class ForwardingMap<K, V> extends ForwardingObject implements Map<K, V>
{
    protected ForwardingMap() {
    }
    
    @Override
    protected abstract Map<K, V> delegate();
    
    @Override
    public int size() {
        return this.delegate().size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.delegate().isEmpty();
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V remove(@CheckForNull final Object key) {
        return this.delegate().remove(key);
    }
    
    @Override
    public void clear() {
        this.delegate().clear();
    }
    
    @Override
    public boolean containsKey(@CheckForNull final Object key) {
        return this.delegate().containsKey(key);
    }
    
    @Override
    public boolean containsValue(@CheckForNull final Object value) {
        return this.delegate().containsValue(value);
    }
    
    @CheckForNull
    @Override
    public V get(@CheckForNull final Object key) {
        return this.delegate().get(key);
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    @Override
    public V put(@ParametricNullness final K key, @ParametricNullness final V value) {
        return this.delegate().put(key, value);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        this.delegate().putAll(map);
    }
    
    @Override
    public Set<K> keySet() {
        return this.delegate().keySet();
    }
    
    @Override
    public Collection<V> values() {
        return this.delegate().values();
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.delegate().entrySet();
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        return object == this || this.delegate().equals(object);
    }
    
    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }
    
    protected void standardPutAll(final Map<? extends K, ? extends V> map) {
        Maps.putAllImpl((Map<Object, Object>)this, map);
    }
    
    @CheckForNull
    @Beta
    protected V standardRemove(@CheckForNull final Object key) {
        final Iterator<Entry<K, V>> entryIterator = this.entrySet().iterator();
        while (entryIterator.hasNext()) {
            final Entry<K, V> entry = entryIterator.next();
            if (Objects.equal(entry.getKey(), key)) {
                final V value = entry.getValue();
                entryIterator.remove();
                return value;
            }
        }
        return null;
    }
    
    protected void standardClear() {
        Iterators.clear(this.entrySet().iterator());
    }
    
    @Beta
    protected boolean standardContainsKey(@CheckForNull final Object key) {
        return Maps.containsKeyImpl(this, key);
    }
    
    protected boolean standardContainsValue(@CheckForNull final Object value) {
        return Maps.containsValueImpl(this, value);
    }
    
    protected boolean standardIsEmpty() {
        return !this.entrySet().iterator().hasNext();
    }
    
    protected boolean standardEquals(@CheckForNull final Object object) {
        return Maps.equalsImpl(this, object);
    }
    
    protected int standardHashCode() {
        return Sets.hashCodeImpl(this.entrySet());
    }
    
    protected String standardToString() {
        return Maps.toStringImpl(this);
    }
    
    @Beta
    protected class StandardKeySet extends Maps.KeySet<K, V>
    {
        public StandardKeySet(final ForwardingMap this$0) {
            super(this$0);
        }
    }
    
    @Beta
    protected class StandardValues extends Maps.Values<K, V>
    {
        public StandardValues(final ForwardingMap this$0) {
            super(this$0);
        }
    }
    
    @Beta
    protected abstract class StandardEntrySet extends Maps.EntrySet<K, V>
    {
        public StandardEntrySet() {
        }
        
        @Override
        Map<K, V> map() {
            return (Map<K, V>)ForwardingMap.this;
        }
    }
}
