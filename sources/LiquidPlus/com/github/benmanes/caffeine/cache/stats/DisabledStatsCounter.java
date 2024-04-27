/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache.stats;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.github.benmanes.caffeine.cache.stats.StatsCounter;

enum DisabledStatsCounter implements StatsCounter
{
    INSTANCE;


    @Override
    public void recordHits(int count) {
    }

    @Override
    public void recordMisses(int count) {
    }

    @Override
    public void recordLoadSuccess(long loadTime) {
    }

    @Override
    public void recordLoadFailure(long loadTime) {
    }

    @Override
    public void recordEviction() {
    }

    @Override
    public CacheStats snapshot() {
        return CacheStats.empty();
    }

    public String toString() {
        return this.snapshot().toString();
    }
}

