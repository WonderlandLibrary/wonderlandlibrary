/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.CacheBase;
import com.ibm.icu.impl.CacheValue;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SoftCache<K, V, D>
extends CacheBase<K, V, D> {
    private ConcurrentHashMap<K, Object> map = new ConcurrentHashMap();

    @Override
    public final V getInstance(K key, D data) {
        CacheValue mapValue = this.map.get(key);
        if (mapValue != null) {
            if (!(mapValue instanceof CacheValue)) {
                return (V)mapValue;
            }
            CacheValue cv = mapValue;
            if (cv.isNull()) {
                return null;
            }
            Object value = cv.get();
            if (value != null) {
                return value;
            }
            value = this.createInstance(key, data);
            return cv.resetIfCleared(value);
        }
        Object value = this.createInstance(key, data);
        mapValue = value != null && CacheValue.futureInstancesWillBeStrong() ? value : CacheValue.getInstance(value);
        if ((mapValue = this.map.putIfAbsent(key, mapValue)) == null) {
            return value;
        }
        if (!(mapValue instanceof CacheValue)) {
            return (V)mapValue;
        }
        CacheValue cv = mapValue;
        return cv.resetIfCleared(value);
    }
}

