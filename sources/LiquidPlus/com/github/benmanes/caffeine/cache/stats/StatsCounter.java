/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.index.qual.NonNegative
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package com.github.benmanes.caffeine.cache.stats;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.github.benmanes.caffeine.cache.stats.DisabledStatsCounter;
import com.github.benmanes.caffeine.cache.stats.GuardedStatsCounter;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface StatsCounter {
    public void recordHits(@NonNegative int var1);

    public void recordMisses(@NonNegative int var1);

    public void recordLoadSuccess(@NonNegative long var1);

    public void recordLoadFailure(@NonNegative long var1);

    @Deprecated
    public void recordEviction();

    @Deprecated
    default public void recordEviction(@NonNegative int weight) {
        this.recordEviction();
    }

    default public void recordEviction(@NonNegative int weight, RemovalCause cause) {
        this.recordEviction(weight);
    }

    public @NonNull CacheStats snapshot();

    public static @NonNull StatsCounter disabledStatsCounter() {
        return DisabledStatsCounter.INSTANCE;
    }

    public static @NonNull StatsCounter guardedStatsCounter(@NonNull StatsCounter statsCounter) {
        return statsCounter instanceof GuardedStatsCounter ? statsCounter : new GuardedStatsCounter(statsCounter);
    }
}

