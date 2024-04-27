/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.SS;
import com.github.benmanes.caffeine.cache.Ticker;
import com.github.benmanes.caffeine.cache.stats.StatsCounter;

class SSS<K, V>
extends SS<K, V> {
    final StatsCounter statsCounter;

    SSS(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
        super(builder, cacheLoader, async);
        this.statsCounter = builder.getStatsCounterSupplier().get();
    }

    @Override
    public final boolean isRecordingStats() {
        return true;
    }

    @Override
    public final Ticker statsTicker() {
        return Ticker.systemTicker();
    }

    @Override
    public final StatsCounter statsCounter() {
        return this.statsCounter;
    }
}

