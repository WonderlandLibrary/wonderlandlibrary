// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import java.util.Iterator;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;
import javax.annotation.CheckForNull;
import java.util.Map;

@ElementTypesAreNonnullByDefault
class MapIteratorCache<K, V>
{
    private final Map<K, V> backingMap;
    @CheckForNull
    private transient volatile Map.Entry<K, V> cacheEntry;
    
    MapIteratorCache(final Map<K, V> backingMap) {
        this.backingMap = Preconditions.checkNotNull(backingMap);
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    final V put(final K key, final V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        this.clearCache();
        return this.backingMap.put(key, value);
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    final V remove(final Object key) {
        Preconditions.checkNotNull(key);
        this.clearCache();
        return this.backingMap.remove(key);
    }
    
    final void clear() {
        this.clearCache();
        this.backingMap.clear();
    }
    
    @CheckForNull
    V get(final Object key) {
        Preconditions.checkNotNull(key);
        final V value = this.getIfCached(key);
        if (value == null) {
            return this.getWithoutCaching(key);
        }
        return value;
    }
    
    @CheckForNull
    final V getWithoutCaching(final Object key) {
        Preconditions.checkNotNull(key);
        return this.backingMap.get(key);
    }
    
    final boolean containsKey(@CheckForNull final Object key) {
        return this.getIfCached(key) != null || this.backingMap.containsKey(key);
    }
    
    final Set<K> unmodifiableKeySet() {
        return new AbstractSet<K>() {
            @Override
            public UnmodifiableIterator<K> iterator() {
                final Iterator<Map.Entry<K, V>> entryIterator = MapIteratorCache.this.backingMap.entrySet().iterator();
                return new UnmodifiableIterator<K>() {
                    @Override
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }
                    
                    @Override
                    public K next() {
                        final Map.Entry<K, V> entry = entryIterator.next();
                        MapIteratorCache.this.cacheEntry = entry;
                        return entry.getKey();
                    }
                };
            }
            
            @Override
            public int size() {
                return MapIteratorCache.this.backingMap.size();
            }
            
            @Override
            public boolean contains(@CheckForNull final Object key) {
                return MapIteratorCache.this.containsKey(key);
            }
        };
    }
    
    @CheckForNull
    V getIfCached(@CheckForNull final Object key) {
        final Map.Entry<K, V> entry = this.cacheEntry;
        if (entry != null && entry.getKey() == key) {
            return entry.getValue();
        }
        return null;
    }
    
    void clearCache() {
        this.cacheEntry = null;
    }
}
