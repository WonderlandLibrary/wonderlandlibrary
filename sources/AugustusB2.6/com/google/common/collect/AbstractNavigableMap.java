// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableMap;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
abstract class AbstractNavigableMap<K, V> extends Maps.IteratorBasedAbstractMap<K, V> implements NavigableMap<K, V>
{
    @CheckForNull
    @Override
    public abstract V get(@CheckForNull final Object p0);
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> firstEntry() {
        return Iterators.getNext(this.entryIterator(), (Map.Entry<K, V>)null);
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> lastEntry() {
        return Iterators.getNext(this.descendingEntryIterator(), (Map.Entry<K, V>)null);
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> pollFirstEntry() {
        return Iterators.pollNext(this.entryIterator());
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> pollLastEntry() {
        return Iterators.pollNext(this.descendingEntryIterator());
    }
    
    @ParametricNullness
    @Override
    public K firstKey() {
        final Map.Entry<K, V> entry = this.firstEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getKey();
    }
    
    @ParametricNullness
    @Override
    public K lastKey() {
        final Map.Entry<K, V> entry = this.lastEntry();
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getKey();
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> lowerEntry(@ParametricNullness final K key) {
        return this.headMap(key, false).lastEntry();
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> floorEntry(@ParametricNullness final K key) {
        return this.headMap(key, true).lastEntry();
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> ceilingEntry(@ParametricNullness final K key) {
        return this.tailMap(key, true).firstEntry();
    }
    
    @CheckForNull
    @Override
    public Map.Entry<K, V> higherEntry(@ParametricNullness final K key) {
        return this.tailMap(key, false).firstEntry();
    }
    
    @CheckForNull
    @Override
    public K lowerKey(@ParametricNullness final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.lowerEntry((K)key));
    }
    
    @CheckForNull
    @Override
    public K floorKey(@ParametricNullness final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.floorEntry((K)key));
    }
    
    @CheckForNull
    @Override
    public K ceilingKey(@ParametricNullness final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.ceilingEntry((K)key));
    }
    
    @CheckForNull
    @Override
    public K higherKey(@ParametricNullness final K key) {
        return Maps.keyOrNull((Map.Entry<K, ?>)this.higherEntry((K)key));
    }
    
    abstract Iterator<Map.Entry<K, V>> descendingEntryIterator();
    
    @Override
    public SortedMap<K, V> subMap(@ParametricNullness final K fromKey, @ParametricNullness final K toKey) {
        return (SortedMap<K, V>)this.subMap(fromKey, true, toKey, false);
    }
    
    @Override
    public SortedMap<K, V> headMap(@ParametricNullness final K toKey) {
        return (SortedMap<K, V>)this.headMap(toKey, false);
    }
    
    @Override
    public SortedMap<K, V> tailMap(@ParametricNullness final K fromKey) {
        return (SortedMap<K, V>)this.tailMap(fromKey, true);
    }
    
    @Override
    public NavigableSet<K> navigableKeySet() {
        return new Maps.NavigableKeySet<K, Object>(this);
    }
    
    @Override
    public Set<K> keySet() {
        return this.navigableKeySet();
    }
    
    @Override
    public NavigableSet<K> descendingKeySet() {
        return this.descendingMap().navigableKeySet();
    }
    
    @Override
    public NavigableMap<K, V> descendingMap() {
        return new DescendingMap();
    }
    
    private final class DescendingMap extends Maps.DescendingMap<K, V>
    {
        @Override
        NavigableMap<K, V> forward() {
            return (NavigableMap<K, V>)AbstractNavigableMap.this;
        }
        
        @Override
        Iterator<Map.Entry<K, V>> entryIterator() {
            return AbstractNavigableMap.this.descendingEntryIterator();
        }
    }
}
