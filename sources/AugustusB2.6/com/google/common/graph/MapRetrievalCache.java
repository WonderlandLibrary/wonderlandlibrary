// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.base.Preconditions;
import java.util.Map;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
final class MapRetrievalCache<K, V> extends MapIteratorCache<K, V>
{
    @CheckForNull
    private transient volatile CacheEntry<K, V> cacheEntry1;
    @CheckForNull
    private transient volatile CacheEntry<K, V> cacheEntry2;
    
    MapRetrievalCache(final Map<K, V> backingMap) {
        super(backingMap);
    }
    
    @CheckForNull
    @Override
    V get(final Object key) {
        Preconditions.checkNotNull(key);
        V value = this.getIfCached(key);
        if (value != null) {
            return value;
        }
        value = this.getWithoutCaching(key);
        if (value != null) {
            this.addToCache(key, value);
        }
        return value;
    }
    
    @CheckForNull
    @Override
    V getIfCached(@CheckForNull final Object key) {
        final V value = super.getIfCached(key);
        if (value != null) {
            return value;
        }
        CacheEntry<K, V> entry = this.cacheEntry1;
        if (entry != null && entry.key == key) {
            return entry.value;
        }
        entry = this.cacheEntry2;
        if (entry != null && entry.key == key) {
            this.addToCache(entry);
            return entry.value;
        }
        return null;
    }
    
    @Override
    void clearCache() {
        super.clearCache();
        this.cacheEntry1 = null;
        this.cacheEntry2 = null;
    }
    
    private void addToCache(final K key, final V value) {
        this.addToCache(new CacheEntry<K, V>(key, value));
    }
    
    private void addToCache(final CacheEntry<K, V> entry) {
        this.cacheEntry2 = this.cacheEntry1;
        this.cacheEntry1 = entry;
    }
    
    private static final class CacheEntry<K, V>
    {
        final K key;
        final V value;
        
        CacheEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
    }
}
