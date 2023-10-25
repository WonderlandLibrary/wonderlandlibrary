/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl.locale;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class LocaleObjectCache<K, V> {
    private ConcurrentHashMap<K, CacheEntry<K, V>> _map;
    private ReferenceQueue<V> _queue = new ReferenceQueue();

    public LocaleObjectCache() {
        this(16, 0.75f, 16);
    }

    public LocaleObjectCache(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this._map = new ConcurrentHashMap(initialCapacity, loadFactor, concurrencyLevel);
    }

    public V get(K key) {
        V value = null;
        this.cleanStaleEntries();
        CacheEntry<K, V> entry = this._map.get(key);
        if (entry != null) {
            value = (V)entry.get();
        }
        if (value == null) {
            key = this.normalizeKey(key);
            V newVal = this.createObject(key);
            if (key == null || newVal == null) {
                return null;
            }
            CacheEntry<K, V> newEntry = new CacheEntry<K, V>(key, newVal, this._queue);
            while (value == null) {
                this.cleanStaleEntries();
                entry = this._map.putIfAbsent(key, newEntry);
                if (entry == null) {
                    value = newVal;
                    break;
                }
                value = (V)entry.get();
            }
        }
        return value;
    }

    private void cleanStaleEntries() {
        CacheEntry entry;
        while ((entry = (CacheEntry)this._queue.poll()) != null) {
            this._map.remove(entry.getKey());
        }
    }

    protected abstract V createObject(K var1);

    protected K normalizeKey(K key) {
        return key;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class CacheEntry<K, V>
    extends SoftReference<V> {
        private K _key;

        CacheEntry(K key, V value, ReferenceQueue<V> queue) {
            super(value, queue);
            this._key = key;
        }

        K getKey() {
            return this._key;
        }
    }
}

