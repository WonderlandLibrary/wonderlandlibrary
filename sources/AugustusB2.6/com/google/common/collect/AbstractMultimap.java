// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.AbstractCollection;
import java.util.Spliterators;
import java.util.Spliterator;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Map;
import java.util.Collection;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractMultimap<K, V> implements Multimap<K, V>
{
    @LazyInit
    @CheckForNull
    private transient Collection<Map.Entry<K, V>> entries;
    @LazyInit
    @CheckForNull
    private transient Set<K> keySet;
    @LazyInit
    @CheckForNull
    private transient Multiset<K> keys;
    @LazyInit
    @CheckForNull
    private transient Collection<V> values;
    @LazyInit
    @CheckForNull
    private transient Map<K, Collection<V>> asMap;
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public boolean containsValue(@CheckForNull final Object value) {
        for (final Collection<V> collection : this.asMap().values()) {
            if (collection.contains(value)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsEntry(@CheckForNull final Object key, @CheckForNull final Object value) {
        final Collection<V> collection = this.asMap().get(key);
        return collection != null && collection.contains(value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean remove(@CheckForNull final Object key, @CheckForNull final Object value) {
        final Collection<V> collection = this.asMap().get(key);
        return collection != null && collection.remove(value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean put(@ParametricNullness final K key, @ParametricNullness final V value) {
        return this.get(key).add(value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean putAll(@ParametricNullness final K key, final Iterable<? extends V> values) {
        Preconditions.checkNotNull(values);
        if (values instanceof Collection) {
            final Collection<? extends V> valueCollection = (Collection<? extends V>)(Collection)values;
            return !valueCollection.isEmpty() && this.get(key).addAll(valueCollection);
        }
        final Iterator<? extends V> valueItr = values.iterator();
        return valueItr.hasNext() && Iterators.addAll(this.get(key), valueItr);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
        boolean changed = false;
        for (final Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
            changed |= this.put(entry.getKey(), entry.getValue());
        }
        return changed;
    }
    
    @CanIgnoreReturnValue
    @Override
    public Collection<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
        Preconditions.checkNotNull(values);
        final Collection<V> result = this.removeAll(key);
        this.putAll(key, values);
        return result;
    }
    
    @Override
    public Collection<Map.Entry<K, V>> entries() {
        final Collection<Map.Entry<K, V>> result = this.entries;
        return (result == null) ? (this.entries = this.createEntries()) : result;
    }
    
    abstract Collection<Map.Entry<K, V>> createEntries();
    
    abstract Iterator<Map.Entry<K, V>> entryIterator();
    
    Spliterator<Map.Entry<K, V>> entrySpliterator() {
        return Spliterators.spliterator((Iterator<? extends Map.Entry<K, V>>)this.entryIterator(), (long)this.size(), (int)((this instanceof SetMultimap) ? 1 : 0));
    }
    
    @Override
    public Set<K> keySet() {
        final Set<K> result = this.keySet;
        return (result == null) ? (this.keySet = this.createKeySet()) : result;
    }
    
    abstract Set<K> createKeySet();
    
    @Override
    public Multiset<K> keys() {
        final Multiset<K> result = this.keys;
        return (result == null) ? (this.keys = this.createKeys()) : result;
    }
    
    abstract Multiset<K> createKeys();
    
    @Override
    public Collection<V> values() {
        final Collection<V> result = this.values;
        return (result == null) ? (this.values = this.createValues()) : result;
    }
    
    abstract Collection<V> createValues();
    
    Iterator<V> valueIterator() {
        return Maps.valueIterator(this.entries().iterator());
    }
    
    Spliterator<V> valueSpliterator() {
        return Spliterators.spliterator(this.valueIterator(), (long)this.size(), 0);
    }
    
    @Override
    public Map<K, Collection<V>> asMap() {
        final Map<K, Collection<V>> result = this.asMap;
        return (result == null) ? (this.asMap = this.createAsMap()) : result;
    }
    
    abstract Map<K, Collection<V>> createAsMap();
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        return Multimaps.equalsImpl(this, object);
    }
    
    @Override
    public int hashCode() {
        return this.asMap().hashCode();
    }
    
    @Override
    public String toString() {
        return this.asMap().toString();
    }
    
    class Entries extends Multimaps.Entries<K, V>
    {
        @Override
        Multimap<K, V> multimap() {
            return (Multimap<K, V>)AbstractMultimap.this;
        }
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return AbstractMultimap.this.entryIterator();
        }
        
        @Override
        public Spliterator<Map.Entry<K, V>> spliterator() {
            return AbstractMultimap.this.entrySpliterator();
        }
    }
    
    class EntrySet extends Entries implements Set<Map.Entry<K, V>>
    {
        EntrySet(final AbstractMultimap this$0) {
            this$0.super();
        }
        
        @Override
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
        
        @Override
        public boolean equals(@CheckForNull final Object obj) {
            return Sets.equalsImpl(this, obj);
        }
    }
    
    class Values extends AbstractCollection<V>
    {
        @Override
        public Iterator<V> iterator() {
            return AbstractMultimap.this.valueIterator();
        }
        
        @Override
        public Spliterator<V> spliterator() {
            return AbstractMultimap.this.valueSpliterator();
        }
        
        @Override
        public int size() {
            return AbstractMultimap.this.size();
        }
        
        @Override
        public boolean contains(@CheckForNull final Object o) {
            return AbstractMultimap.this.containsValue(o);
        }
        
        @Override
        public void clear() {
            AbstractMultimap.this.clear();
        }
    }
}
