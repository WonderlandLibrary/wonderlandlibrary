/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@GwtCompatible
public final class AtomicLongMap<K> {
    private final ConcurrentHashMap<K, AtomicLong> map;
    private transient Map<K, Long> asMap;

    private AtomicLongMap(ConcurrentHashMap<K, AtomicLong> map) {
        this.map = Preconditions.checkNotNull(map);
    }

    public static <K> AtomicLongMap<K> create() {
        return new AtomicLongMap(new ConcurrentHashMap());
    }

    public static <K> AtomicLongMap<K> create(Map<? extends K, ? extends Long> m2) {
        AtomicLongMap<? extends K> result = AtomicLongMap.create();
        result.putAll(m2);
        return result;
    }

    public long get(K key) {
        AtomicLong atomic = this.map.get(key);
        return atomic == null ? 0L : atomic.get();
    }

    public long incrementAndGet(K key) {
        return this.addAndGet(key, 1L);
    }

    public long decrementAndGet(K key) {
        return this.addAndGet(key, -1L);
    }

    public long addAndGet(K key, long delta) {
        long newValue;
        block0: while (true) {
            long oldValue;
            AtomicLong atomic;
            if ((atomic = this.map.get(key)) == null && (atomic = this.map.putIfAbsent(key, new AtomicLong(delta))) == null) {
                return delta;
            }
            do {
                if ((oldValue = atomic.get()) != 0L) continue;
                if (!this.map.replace(key, atomic, new AtomicLong(delta))) continue block0;
                return delta;
            } while (!atomic.compareAndSet(oldValue, newValue = oldValue + delta));
            break;
        }
        return newValue;
    }

    public long getAndIncrement(K key) {
        return this.getAndAdd(key, 1L);
    }

    public long getAndDecrement(K key) {
        return this.getAndAdd(key, -1L);
    }

    public long getAndAdd(K key, long delta) {
        long oldValue;
        block0: while (true) {
            long newValue;
            AtomicLong atomic;
            if ((atomic = this.map.get(key)) == null && (atomic = this.map.putIfAbsent(key, new AtomicLong(delta))) == null) {
                return 0L;
            }
            do {
                if ((oldValue = atomic.get()) != 0L) continue;
                if (!this.map.replace(key, atomic, new AtomicLong(delta))) continue block0;
                return 0L;
            } while (!atomic.compareAndSet(oldValue, newValue = oldValue + delta));
            break;
        }
        return oldValue;
    }

    public long put(K key, long newValue) {
        long oldValue;
        block0: while (true) {
            AtomicLong atomic;
            if ((atomic = this.map.get(key)) == null && (atomic = this.map.putIfAbsent(key, new AtomicLong(newValue))) == null) {
                return 0L;
            }
            do {
                if ((oldValue = atomic.get()) != 0L) continue;
                if (!this.map.replace(key, atomic, new AtomicLong(newValue))) continue block0;
                return 0L;
            } while (!atomic.compareAndSet(oldValue, newValue));
            break;
        }
        return oldValue;
    }

    public void putAll(Map<? extends K, ? extends Long> m2) {
        for (Map.Entry<K, Long> entry : m2.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public long remove(K key) {
        long oldValue;
        AtomicLong atomic = this.map.get(key);
        if (atomic == null) {
            return 0L;
        }
        while ((oldValue = atomic.get()) != 0L && !atomic.compareAndSet(oldValue, 0L)) {
        }
        this.map.remove(key, atomic);
        return oldValue;
    }

    public void removeAllZeros() {
        for (Object key : this.map.keySet()) {
            AtomicLong atomic = this.map.get(key);
            if (atomic == null || atomic.get() != 0L) continue;
            this.map.remove(key, atomic);
        }
    }

    public long sum() {
        long sum = 0L;
        for (AtomicLong value : this.map.values()) {
            sum += value.get();
        }
        return sum;
    }

    public Map<K, Long> asMap() {
        Map<K, Long> result = this.asMap;
        return result == null ? (this.asMap = this.createAsMap()) : result;
    }

    private Map<K, Long> createAsMap() {
        return Collections.unmodifiableMap(Maps.transformValues(this.map, new Function<AtomicLong, Long>(){

            @Override
            public Long apply(AtomicLong atomic) {
                return atomic.get();
            }
        }));
    }

    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public void clear() {
        this.map.clear();
    }

    public String toString() {
        return this.map.toString();
    }

    long putIfAbsent(K key, long newValue) {
        long oldValue;
        block2: {
            AtomicLong atomic;
            do {
                if ((atomic = this.map.get(key)) == null && (atomic = this.map.putIfAbsent(key, new AtomicLong(newValue))) == null) {
                    return 0L;
                }
                oldValue = atomic.get();
                if (oldValue != 0L) break block2;
            } while (!this.map.replace(key, atomic, new AtomicLong(newValue)));
            return 0L;
        }
        return oldValue;
    }

    boolean replace(K key, long expectedOldValue, long newValue) {
        if (expectedOldValue == 0L) {
            return this.putIfAbsent(key, newValue) == 0L;
        }
        AtomicLong atomic = this.map.get(key);
        return atomic == null ? false : atomic.compareAndSet(expectedOldValue, newValue);
    }

    boolean remove(K key, long value) {
        AtomicLong atomic = this.map.get(key);
        if (atomic == null) {
            return false;
        }
        long oldValue = atomic.get();
        if (oldValue != value) {
            return false;
        }
        if (oldValue == 0L || atomic.compareAndSet(oldValue, 0L)) {
            this.map.remove(key, atomic);
            return true;
        }
        return false;
    }
}

