// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.graph;

import java.util.Iterator;
import me.gong.mcleaks.util.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Set;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.Map;

class MapIteratorCache<K, V>
{
    private final Map<K, V> backingMap;
    @Nullable
    private transient Map.Entry<K, V> entrySetCache;
    
    MapIteratorCache(final Map<K, V> backingMap) {
        this.backingMap = Preconditions.checkNotNull(backingMap);
    }
    
    @CanIgnoreReturnValue
    public V put(@Nullable final K key, @Nullable final V value) {
        this.clearCache();
        return this.backingMap.put(key, value);
    }
    
    @CanIgnoreReturnValue
    public V remove(@Nullable final Object key) {
        this.clearCache();
        return this.backingMap.remove(key);
    }
    
    public void clear() {
        this.clearCache();
        this.backingMap.clear();
    }
    
    public V get(@Nullable final Object key) {
        final V value = this.getIfCached(key);
        return (value != null) ? value : this.getWithoutCaching(key);
    }
    
    public final V getWithoutCaching(@Nullable final Object key) {
        return this.backingMap.get(key);
    }
    
    public final boolean containsKey(@Nullable final Object key) {
        return this.getIfCached(key) != null || this.backingMap.containsKey(key);
    }
    
    public final Set<K> unmodifiableKeySet() {
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
                        MapIteratorCache.this.entrySetCache = entry;
                        return entry.getKey();
                    }
                };
            }
            
            @Override
            public int size() {
                return MapIteratorCache.this.backingMap.size();
            }
            
            @Override
            public boolean contains(@Nullable final Object key) {
                return MapIteratorCache.this.containsKey(key);
            }
        };
    }
    
    protected V getIfCached(@Nullable final Object key) {
        final Map.Entry<K, V> entry = this.entrySetCache;
        if (entry != null && entry.getKey() == key) {
            return entry.getValue();
        }
        return null;
    }
    
    protected void clearCache() {
        this.entrySetCache = null;
    }
}
