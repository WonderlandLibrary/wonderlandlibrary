// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.List;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;
import java.util.Collections;
import javax.annotation.CheckForNull;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
class FilteredKeyMultimap<K, V> extends AbstractMultimap<K, V> implements FilteredMultimap<K, V>
{
    final Multimap<K, V> unfiltered;
    final Predicate<? super K> keyPredicate;
    
    FilteredKeyMultimap(final Multimap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        this.unfiltered = Preconditions.checkNotNull(unfiltered);
        this.keyPredicate = Preconditions.checkNotNull(keyPredicate);
    }
    
    @Override
    public Multimap<K, V> unfiltered() {
        return this.unfiltered;
    }
    
    @Override
    public Predicate<? super Map.Entry<K, V>> entryPredicate() {
        return (Predicate<? super Map.Entry<K, V>>)Maps.keyPredicateOnEntries((Predicate<? super Object>)this.keyPredicate);
    }
    
    @Override
    public int size() {
        int size = 0;
        for (final Collection<V> collection : this.asMap().values()) {
            size += collection.size();
        }
        return size;
    }
    
    @Override
    public boolean containsKey(@CheckForNull final Object key) {
        if (this.unfiltered.containsKey(key)) {
            final K k = (K)key;
            return this.keyPredicate.apply((Object)k);
        }
        return false;
    }
    
    @Override
    public Collection<V> removeAll(@CheckForNull final Object key) {
        return this.containsKey(key) ? this.unfiltered.removeAll(key) : this.unmodifiableEmptyCollection();
    }
    
    Collection<V> unmodifiableEmptyCollection() {
        if (this.unfiltered instanceof SetMultimap) {
            return (Collection<V>)Collections.emptySet();
        }
        return (Collection<V>)Collections.emptyList();
    }
    
    @Override
    public void clear() {
        this.keySet().clear();
    }
    
    @Override
    Set<K> createKeySet() {
        return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
    }
    
    @Override
    public Collection<V> get(@ParametricNullness final K key) {
        if (this.keyPredicate.apply((Object)key)) {
            return this.unfiltered.get(key);
        }
        if (this.unfiltered instanceof SetMultimap) {
            return (Collection<V>)new AddRejectingSet(key);
        }
        return (Collection<V>)new AddRejectingList(key);
    }
    
    @Override
    Iterator<Map.Entry<K, V>> entryIterator() {
        throw new AssertionError((Object)"should never be called");
    }
    
    @Override
    Collection<Map.Entry<K, V>> createEntries() {
        return new Entries();
    }
    
    @Override
    Collection<V> createValues() {
        return (Collection<V>)new FilteredMultimapValues((FilteredMultimap<Object, Object>)this);
    }
    
    @Override
    Map<K, Collection<V>> createAsMap() {
        return Maps.filterKeys(this.unfiltered.asMap(), this.keyPredicate);
    }
    
    @Override
    Multiset<K> createKeys() {
        return Multisets.filter(this.unfiltered.keys(), this.keyPredicate);
    }
    
    static class AddRejectingSet<K, V> extends ForwardingSet<V>
    {
        @ParametricNullness
        final K key;
        
        AddRejectingSet(@ParametricNullness final K key) {
            this.key = key;
        }
        
        @Override
        public boolean add(@ParametricNullness final V element) {
            final String value = String.valueOf(this.key);
            throw new IllegalArgumentException(new StringBuilder(32 + String.valueOf(value).length()).append("Key does not satisfy predicate: ").append(value).toString());
        }
        
        @Override
        public boolean addAll(final Collection<? extends V> collection) {
            Preconditions.checkNotNull(collection);
            final String value = String.valueOf(this.key);
            throw new IllegalArgumentException(new StringBuilder(32 + String.valueOf(value).length()).append("Key does not satisfy predicate: ").append(value).toString());
        }
        
        @Override
        protected Set<V> delegate() {
            return Collections.emptySet();
        }
    }
    
    static class AddRejectingList<K, V> extends ForwardingList<V>
    {
        @ParametricNullness
        final K key;
        
        AddRejectingList(@ParametricNullness final K key) {
            this.key = key;
        }
        
        @Override
        public boolean add(@ParametricNullness final V v) {
            this.add(0, v);
            return true;
        }
        
        @Override
        public void add(final int index, @ParametricNullness final V element) {
            Preconditions.checkPositionIndex(index, 0);
            final String value = String.valueOf(this.key);
            throw new IllegalArgumentException(new StringBuilder(32 + String.valueOf(value).length()).append("Key does not satisfy predicate: ").append(value).toString());
        }
        
        @Override
        public boolean addAll(final Collection<? extends V> collection) {
            this.addAll(0, collection);
            return true;
        }
        
        @CanIgnoreReturnValue
        @Override
        public boolean addAll(final int index, final Collection<? extends V> elements) {
            Preconditions.checkNotNull(elements);
            Preconditions.checkPositionIndex(index, 0);
            final String value = String.valueOf(this.key);
            throw new IllegalArgumentException(new StringBuilder(32 + String.valueOf(value).length()).append("Key does not satisfy predicate: ").append(value).toString());
        }
        
        @Override
        protected List<V> delegate() {
            return Collections.emptyList();
        }
    }
    
    class Entries extends ForwardingCollection<Map.Entry<K, V>>
    {
        @Override
        protected Collection<Map.Entry<K, V>> delegate() {
            return Collections2.filter(FilteredKeyMultimap.this.unfiltered.entries(), FilteredKeyMultimap.this.entryPredicate());
        }
        
        @Override
        public boolean remove(@CheckForNull final Object o) {
            if (o instanceof Map.Entry) {
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
                if (FilteredKeyMultimap.this.unfiltered.containsKey(entry.getKey()) && FilteredKeyMultimap.this.keyPredicate.apply((Object)entry.getKey())) {
                    return FilteredKeyMultimap.this.unfiltered.remove(entry.getKey(), entry.getValue());
                }
            }
            return false;
        }
    }
}
