/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUCache;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SimpleCache<K, V>
implements ICUCache<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private Reference<Map<K, V>> cacheRef = null;
    private int type = 0;
    private int capacity = 16;

    public SimpleCache() {
    }

    public SimpleCache(int cacheType) {
        this(cacheType, 16);
    }

    public SimpleCache(int cacheType, int initialCapacity) {
        if (cacheType == 1) {
            this.type = cacheType;
        }
        if (initialCapacity > 0) {
            this.capacity = initialCapacity;
        }
    }

    @Override
    public V get(Object key) {
        Map<K, V> map;
        Reference<Map<K, V>> ref = this.cacheRef;
        if (ref != null && (map = ref.get()) != null) {
            return map.get(key);
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        Reference ref = this.cacheRef;
        Map<K, V> map = null;
        if (ref != null) {
            map = ref.get();
        }
        if (map == null) {
            map = Collections.synchronizedMap(new HashMap(this.capacity));
            ref = this.type == 1 ? new WeakReference<Map<K, V>>(map) : new SoftReference<Map<K, V>>(map);
            this.cacheRef = ref;
        }
        map.put(key, value);
    }

    @Override
    public void clear() {
        this.cacheRef = null;
    }
}

