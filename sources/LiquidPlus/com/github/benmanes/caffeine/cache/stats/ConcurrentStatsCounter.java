/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package com.github.benmanes.caffeine.cache.stats;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.github.benmanes.caffeine.cache.stats.StatsCounter;
import java.util.concurrent.atomic.LongAdder;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class ConcurrentStatsCounter
implements StatsCounter {
    private final LongAdder hitCount = new LongAdder();
    private final LongAdder missCount = new LongAdder();
    private final LongAdder loadSuccessCount = new LongAdder();
    private final LongAdder loadFailureCount = new LongAdder();
    private final LongAdder totalLoadTime = new LongAdder();
    private final LongAdder evictionCount = new LongAdder();
    private final LongAdder evictionWeight = new LongAdder();

    @Override
    public void recordHits(int count) {
        this.hitCount.add(count);
    }

    @Override
    public void recordMisses(int count) {
        this.missCount.add(count);
    }

    @Override
    public void recordLoadSuccess(long loadTime) {
        this.loadSuccessCount.increment();
        this.totalLoadTime.add(loadTime);
    }

    @Override
    public void recordLoadFailure(long loadTime) {
        this.loadFailureCount.increment();
        this.totalLoadTime.add(loadTime);
    }

    @Override
    @Deprecated
    public void recordEviction() {
        this.evictionCount.increment();
    }

    @Override
    @Deprecated
    public void recordEviction(int weight) {
        this.evictionCount.increment();
        this.evictionWeight.add(weight);
    }

    @Override
    public void recordEviction(int weight, RemovalCause cause) {
        this.evictionCount.increment();
        this.evictionWeight.add(weight);
    }

    @Override
    public CacheStats snapshot() {
        return CacheStats.of(ConcurrentStatsCounter.negativeToMaxValue(this.hitCount.sum()), ConcurrentStatsCounter.negativeToMaxValue(this.missCount.sum()), ConcurrentStatsCounter.negativeToMaxValue(this.loadSuccessCount.sum()), ConcurrentStatsCounter.negativeToMaxValue(this.loadFailureCount.sum()), ConcurrentStatsCounter.negativeToMaxValue(this.totalLoadTime.sum()), ConcurrentStatsCounter.negativeToMaxValue(this.evictionCount.sum()), ConcurrentStatsCounter.negativeToMaxValue(this.evictionWeight.sum()));
    }

    private static long negativeToMaxValue(long value) {
        return value >= 0L ? value : Long.MAX_VALUE;
    }

    public void incrementBy(@NonNull StatsCounter other) {
        CacheStats otherStats = other.snapshot();
        this.hitCount.add(otherStats.hitCount());
        this.missCount.add(otherStats.missCount());
        this.loadSuccessCount.add(otherStats.loadSuccessCount());
        this.loadFailureCount.add(otherStats.loadFailureCount());
        this.totalLoadTime.add(otherStats.totalLoadTime());
        this.evictionCount.add(otherStats.evictionCount());
        this.evictionWeight.add(otherStats.evictionWeight());
    }

    public String toString() {
        return this.snapshot().toString();
    }
}

