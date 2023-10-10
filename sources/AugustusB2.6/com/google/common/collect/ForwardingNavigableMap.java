// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.function.BiFunction;
import java.util.SortedMap;
import com.google.common.annotations.Beta;
import java.util.NavigableSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;
import java.util.Map;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableMap;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class ForwardingNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>
{
    protected ForwardingNavigableMap() {
    }
    
    @Override
    protected abstract NavigableMap<K, V> delegate();
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> lowerEntry(@ParametricNullness final K key) {
        return this.delegate().lowerEntry(key);
    }
    
    @CheckForNull
    protected Map.Entry<K, V> standardLowerEntry(@ParametricNullness final K key) {
        return this.headMap(key, false).lastEntry();
    }
    
    @CheckForNull
    @Override
    public K lowerKey(@ParametricNullness final K key) {
        return this.delegate().lowerKey(key);
    }
    
    @CheckForNull
    protected K standardLowerKey(@ParametricNullness final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.lowerEntry((K)key));
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> floorEntry(@ParametricNullness final K key) {
        return this.delegate().floorEntry(key);
    }
    
    @CheckForNull
    protected Map.Entry<K, V> standardFloorEntry(@ParametricNullness final K key) {
        return this.headMap(key, true).lastEntry();
    }
    
    @CheckForNull
    @Override
    public K floorKey(@ParametricNullness final K key) {
        return this.delegate().floorKey(key);
    }
    
    @CheckForNull
    protected K standardFloorKey(@ParametricNullness final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.floorEntry((K)key));
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> ceilingEntry(@ParametricNullness final K key) {
        return this.delegate().ceilingEntry(key);
    }
    
    @CheckForNull
    protected Map.Entry<K, V> standardCeilingEntry(@ParametricNullness final K key) {
        return this.tailMap(key, true).firstEntry();
    }
    
    @CheckForNull
    @Override
    public K ceilingKey(@ParametricNullness final K key) {
        return this.delegate().ceilingKey(key);
    }
    
    @CheckForNull
    protected K standardCeilingKey(@ParametricNullness final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.ceilingEntry((K)key));
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> higherEntry(@ParametricNullness final K key) {
        return this.delegate().higherEntry(key);
    }
    
    @CheckForNull
    protected Map.Entry<K, V> standardHigherEntry(@ParametricNullness final K key) {
        return this.tailMap(key, false).firstEntry();
    }
    
    @CheckForNull
    @Override
    public K higherKey(@ParametricNullness final K key) {
        return this.delegate().higherKey(key);
    }
    
    @CheckForNull
    protected K standardHigherKey(@ParametricNullness final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.higherEntry((K)key));
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> firstEntry() {
        return this.delegate().firstEntry();
    }
    
    @CheckForNull
    protected Map.Entry<K, V> standardFirstEntry() {
        return Iterables.getFirst(this.entrySet(), (Map.Entry<K, V>)null);
    }
    
    protected K standardFirstKey() {
        final Map.Entry<K, V> entry = this.firstEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getKey();
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> lastEntry() {
        return this.delegate().lastEntry();
    }
    
    @CheckForNull
    protected Map.Entry<K, V> standardLastEntry() {
        return Iterables.getFirst((Iterable<? extends Map.Entry<K, V>>)this.descendingMap().entrySet(), (Map.Entry<K, V>)null);
    }
    
    protected K standardLastKey() {
        final Map.Entry<K, V> entry = this.lastEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getKey();
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> pollFirstEntry() {
        return this.delegate().pollFirstEntry();
    }
    
    @CheckForNull
    protected Map.Entry<K, V> standardPollFirstEntry() {
        return Iterators.pollNext(this.entrySet().iterator());
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> pollLastEntry() {
        return this.delegate().pollLastEntry();
    }
    
    @CheckForNull
    protected Map.Entry<K, V> standardPollLastEntry() {
        return Iterators.pollNext((Iterator<Map.Entry<K, V>>)this.descendingMap().entrySet().iterator());
    }
    
    @Override
    public NavigableMap<K, V> descendingMap() {
        return this.delegate().descendingMap();
    }
    
    @Override
    public NavigableSet<K> navigableKeySet() {
        return this.delegate().navigableKeySet();
    }
    
    @Override
    public NavigableSet<K> descendingKeySet() {
        return this.delegate().descendingKeySet();
    }
    
    @Beta
    protected NavigableSet<K> standardDescendingKeySet() {
        return this.descendingMap().navigableKeySet();
    }
    
    @Override
    protected SortedMap<K, V> standardSubMap(@ParametricNullness final K fromKey, @ParametricNullness final K toKey) {
        return this.subMap(fromKey, true, toKey, false);
    }
    
    @Override
    public NavigableMap<K, V> subMap(@ParametricNullness final K fromKey, final boolean fromInclusive, @ParametricNullness final K toKey, final boolean toInclusive) {
        return this.delegate().subMap(fromKey, fromInclusive, toKey, toInclusive);
    }
    
    @Override
    public NavigableMap<K, V> headMap(@ParametricNullness final K toKey, final boolean inclusive) {
        return this.delegate().headMap(toKey, inclusive);
    }
    
    @Override
    public NavigableMap<K, V> tailMap(@ParametricNullness final K fromKey, final boolean inclusive) {
        return this.delegate().tailMap(fromKey, inclusive);
    }
    
    protected SortedMap<K, V> standardHeadMap(@ParametricNullness final K toKey) {
        return this.headMap(toKey, false);
    }
    
    protected SortedMap<K, V> standardTailMap(@ParametricNullness final K fromKey) {
        return this.tailMap(fromKey, true);
    }
    
    @Beta
    protected class StandardDescendingMap extends Maps.DescendingMap<K, V>
    {
        public StandardDescendingMap() {
        }
        
        @Override
        NavigableMap<K, V> forward() {
            return (NavigableMap<K, V>)ForwardingNavigableMap.this;
        }
        
        @Override
        public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
            this.forward().replaceAll((BiFunction<? super Object, ? super Object, ?>)function);
        }
        
        protected Iterator<Map.Entry<K, V>> entryIterator() {
            return new Iterator<Map.Entry<K, V>>() {
                @CheckForNull
                private Map.Entry<K, V> toRemove = null;
                @CheckForNull
                private Map.Entry<K, V> nextOrNull = StandardDescendingMap.this.forward().lastEntry();
                
                @Override
                public boolean hasNext() {
                    return this.nextOrNull != null;
                }
                
                @Override
                public Map.Entry<K, V> next() {
                    if (this.nextOrNull == null) {
                        throw new NoSuchElementException();
                    }
                    try {
                        return this.nextOrNull;
                    }
                    finally {
                        this.toRemove = this.nextOrNull;
                        this.nextOrNull = StandardDescendingMap.this.forward().lowerEntry(this.nextOrNull.getKey());
                    }
                }
                
                @Override
                public void remove() {
                    if (this.toRemove == null) {
                        throw new IllegalStateException("no calls to next() since the last call to remove()");
                    }
                    StandardDescendingMap.this.forward().remove(this.toRemove.getKey());
                    this.toRemove = null;
                }
            };
        }
    }
    
    @Beta
    protected class StandardNavigableKeySet extends Maps.NavigableKeySet<K, V>
    {
        public StandardNavigableKeySet(final ForwardingNavigableMap this$0) {
            super(this$0);
        }
    }
}
