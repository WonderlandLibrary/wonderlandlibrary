// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import java.util.Map;
import javax.annotation.Nullable;

class MapRetrievalCache<K, V> extends MapIteratorCache<K, V>
{
    @Nullable
    private transient CacheEntry<K, V> cacheEntry1;
    @Nullable
    private transient CacheEntry<K, V> cacheEntry2;
    
    MapRetrievalCache(final Map<K, V> backingMap) {
        super(backingMap);
    }
    
    @Override
    public V get(@Nullable final Object key) {
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
    
    @Override
    protected V getIfCached(@Nullable final Object key) {
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
    protected void clearCache() {
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
