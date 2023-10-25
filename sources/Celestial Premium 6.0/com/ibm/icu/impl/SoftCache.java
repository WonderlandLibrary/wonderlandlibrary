/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.CacheBase;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class SoftCache<K, V, D>
extends CacheBase<K, V, D> {
    private ConcurrentHashMap<K, SettableSoftReference<V>> map = new ConcurrentHashMap();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final V getInstance(K key, D data) {
        SettableSoftReference<V> valueRef = this.map.get(key);
        if (valueRef != null) {
            SettableSoftReference<V> settableSoftReference = valueRef;
            synchronized (settableSoftReference) {
                Object value = ((SettableSoftReference)valueRef).ref.get();
                if (value != null) {
                    return (V)value;
                }
                value = this.createInstance(key, data);
                if (value != null) {
                    ((SettableSoftReference)valueRef).ref = new SoftReference(value);
                }
                return (V)value;
            }
        }
        Object value = this.createInstance(key, data);
        if (value == null) {
            return null;
        }
        valueRef = this.map.putIfAbsent(key, new SettableSoftReference(value));
        if (valueRef == null) {
            return value;
        }
        return (V)((SettableSoftReference)valueRef).setIfAbsent(value);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static final class SettableSoftReference<V> {
        private SoftReference<V> ref;

        private SettableSoftReference(V value) {
            this.ref = new SoftReference<V>(value);
        }

        private synchronized V setIfAbsent(V value) {
            V oldValue = this.ref.get();
            if (oldValue == null) {
                this.ref = new SoftReference<V>(value);
                return value;
            }
            return oldValue;
        }
    }
}

