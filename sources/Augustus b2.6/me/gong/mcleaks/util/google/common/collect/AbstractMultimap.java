// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.AbstractCollection;
import java.util.Spliterators;
import java.util.Spliterator;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.Map;
import java.util.Collection;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class AbstractMultimap<K, V> implements Multimap<K, V>
{
    private transient Collection<Map.Entry<K, V>> entries;
    private transient Set<K> keySet;
    private transient Multiset<K> keys;
    private transient Collection<V> values;
    private transient Map<K, Collection<V>> asMap;
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
        for (final Collection<V> collection : this.asMap().values()) {
            if (collection.contains(value)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsEntry(@Nullable final Object key, @Nullable final Object value) {
        final Collection<V> collection = this.asMap().get(key);
        return collection != null && collection.contains(value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean remove(@Nullable final Object key, @Nullable final Object value) {
        final Collection<V> collection = this.asMap().get(key);
        return collection != null && collection.remove(value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean put(@Nullable final K key, @Nullable final V value) {
        return this.get(key).add(value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean putAll(@Nullable final K key, final Iterable<? extends V> values) {
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
    public Collection<V> replaceValues(@Nullable final K key, final Iterable<? extends V> values) {
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
    
    Collection<Map.Entry<K, V>> createEntries() {
        if (this instanceof SetMultimap) {
            return (Collection<Map.Entry<K, V>>)new EntrySet();
        }
        return (Collection<Map.Entry<K, V>>)new Entries();
    }
    
    abstract Iterator<Map.Entry<K, V>> entryIterator();
    
    Spliterator<Map.Entry<K, V>> entrySpliterator() {
        return Spliterators.spliterator((Iterator<? extends Map.Entry<K, V>>)this.entryIterator(), (long)this.size(), (int)((this instanceof SetMultimap) ? 1 : 0));
    }
    
    @Override
    public Set<K> keySet() {
        final Set<K> result = this.keySet;
        return (result == null) ? (this.keySet = this.createKeySet()) : result;
    }
    
    Set<K> createKeySet() {
        return (Set<K>)new Maps.KeySet((Map<Object, Object>)this.asMap());
    }
    
    @Override
    public Multiset<K> keys() {
        final Multiset<K> result = this.keys;
        return (result == null) ? (this.keys = this.createKeys()) : result;
    }
    
    Multiset<K> createKeys() {
        return (Multiset<K>)new Multimaps.Keys((Multimap<Object, Object>)this);
    }
    
    @Override
    public Collection<V> values() {
        final Collection<V> result = this.values;
        return (result == null) ? (this.values = this.createValues()) : result;
    }
    
    Collection<V> createValues() {
        return new Values();
    }
    
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
    public boolean equals(@Nullable final Object object) {
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
    
    private class Entries extends Multimaps.Entries<K, V>
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
    
    private class EntrySet extends Entries implements Set<Map.Entry<K, V>>
    {
        @Override
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
        
        @Override
        public boolean equals(@Nullable final Object obj) {
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
        public boolean contains(@Nullable final Object o) {
            return AbstractMultimap.this.containsValue(o);
        }
        
        @Override
        public void clear() {
            AbstractMultimap.this.clear();
        }
    }
}
