// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Collections;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import java.util.function.BiConsumer;
import java.util.function.LongBinaryOperator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongUnaryOperator;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible
public final class AtomicLongMap<K> implements Serializable
{
    private final ConcurrentHashMap<K, Long> map;
    private transient Map<K, Long> asMap;
    
    private AtomicLongMap(final ConcurrentHashMap<K, Long> map) {
        this.map = Preconditions.checkNotNull(map);
    }
    
    public static <K> AtomicLongMap<K> create() {
        return new AtomicLongMap<K>(new ConcurrentHashMap<K, Long>());
    }
    
    public static <K> AtomicLongMap<K> create(final Map<? extends K, ? extends Long> m) {
        final AtomicLongMap<K> result = create();
        result.putAll(m);
        return result;
    }
    
    public long get(final K key) {
        return this.map.getOrDefault(key, 0L);
    }
    
    @CanIgnoreReturnValue
    public long incrementAndGet(final K key) {
        return this.addAndGet(key, 1L);
    }
    
    @CanIgnoreReturnValue
    public long decrementAndGet(final K key) {
        return this.addAndGet(key, -1L);
    }
    
    @CanIgnoreReturnValue
    public long addAndGet(final K key, final long delta) {
        return this.accumulateAndGet(key, delta, Long::sum);
    }
    
    @CanIgnoreReturnValue
    public long getAndIncrement(final K key) {
        return this.getAndAdd(key, 1L);
    }
    
    @CanIgnoreReturnValue
    public long getAndDecrement(final K key) {
        return this.getAndAdd(key, -1L);
    }
    
    @CanIgnoreReturnValue
    public long getAndAdd(final K key, final long delta) {
        return this.getAndAccumulate(key, delta, Long::sum);
    }
    
    @CanIgnoreReturnValue
    public long updateAndGet(final K key, final LongUnaryOperator updaterFunction) {
        Preconditions.checkNotNull(updaterFunction);
        return this.map.compute(key, (k, value) -> updaterFunction.applyAsLong((value == null) ? 0L : ((long)value)));
    }
    
    @CanIgnoreReturnValue
    public long getAndUpdate(final K key, final LongUnaryOperator updaterFunction) {
        Preconditions.checkNotNull(updaterFunction);
        final AtomicLong holder = new AtomicLong();
        final long oldValue;
        final AtomicLong atomicLong;
        this.map.compute(key, (k, value) -> {
            oldValue = ((value == null) ? 0L : value);
            atomicLong.set(oldValue);
            return updaterFunction.applyAsLong(oldValue);
        });
        return holder.get();
    }
    
    @CanIgnoreReturnValue
    public long accumulateAndGet(final K key, final long x, final LongBinaryOperator accumulatorFunction) {
        Preconditions.checkNotNull(accumulatorFunction);
        return this.updateAndGet(key, oldValue -> accumulatorFunction.applyAsLong(oldValue, x));
    }
    
    @CanIgnoreReturnValue
    public long getAndAccumulate(final K key, final long x, final LongBinaryOperator accumulatorFunction) {
        Preconditions.checkNotNull(accumulatorFunction);
        return this.getAndUpdate(key, oldValue -> accumulatorFunction.applyAsLong(oldValue, x));
    }
    
    @CanIgnoreReturnValue
    public long put(final K key, final long newValue) {
        return this.getAndUpdate(key, x -> newValue);
    }
    
    public void putAll(final Map<? extends K, ? extends Long> m) {
        m.forEach(this::put);
    }
    
    @CanIgnoreReturnValue
    public long remove(final K key) {
        final Long result = this.map.remove(key);
        return (result == null) ? 0L : result;
    }
    
    @Beta
    @CanIgnoreReturnValue
    public boolean removeIfZero(final K key) {
        return this.remove(key, 0L);
    }
    
    public void removeAllZeros() {
        this.map.values().removeIf(x -> x == 0L);
    }
    
    public long sum() {
        return this.map.values().stream().mapToLong(Long::longValue).sum();
    }
    
    public Map<K, Long> asMap() {
        final Map<K, Long> result = this.asMap;
        return (result == null) ? (this.asMap = this.createAsMap()) : result;
    }
    
    private Map<K, Long> createAsMap() {
        return Collections.unmodifiableMap((Map<? extends K, ? extends Long>)this.map);
    }
    
    public boolean containsKey(final Object key) {
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
    
    @Override
    public String toString() {
        return this.map.toString();
    }
    
    long putIfAbsent(final K key, final long newValue) {
        final AtomicBoolean noValue = new AtomicBoolean(false);
        final AtomicBoolean atomicBoolean;
        final Long result = this.map.compute(key, (k, oldValue) -> {
            if (oldValue == null || oldValue == 0L) {
                atomicBoolean.set(true);
                return newValue;
            }
            else {
                return oldValue;
            }
        });
        return noValue.get() ? 0L : result;
    }
    
    boolean replace(final K key, final long expectedOldValue, final long newValue) {
        if (expectedOldValue == 0L) {
            return this.putIfAbsent(key, newValue) == 0L;
        }
        return this.map.replace(key, expectedOldValue, newValue);
    }
    
    boolean remove(final K key, final long value) {
        return this.map.remove(key, value);
    }
}
