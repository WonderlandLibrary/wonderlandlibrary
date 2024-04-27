/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache.stats;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.github.benmanes.caffeine.cache.stats.StatsCounter;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

final class GuardedStatsCounter
implements StatsCounter {
    static final Logger logger = Logger.getLogger(GuardedStatsCounter.class.getName());
    final StatsCounter delegate;

    GuardedStatsCounter(StatsCounter delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public void recordHits(int count) {
        try {
            this.delegate.recordHits(count);
        }
        catch (Throwable t) {
            logger.log(Level.WARNING, "Exception thrown by stats counter", t);
        }
    }

    @Override
    public void recordMisses(int count) {
        try {
            this.delegate.recordMisses(count);
        }
        catch (Throwable t) {
            logger.log(Level.WARNING, "Exception thrown by stats counter", t);
        }
    }

    @Override
    public void recordLoadSuccess(long loadTime) {
        try {
            this.delegate.recordLoadSuccess(loadTime);
        }
        catch (Throwable t) {
            logger.log(Level.WARNING, "Exception thrown by stats counter", t);
        }
    }

    @Override
    public void recordLoadFailure(long loadTime) {
        try {
            this.delegate.recordLoadFailure(loadTime);
        }
        catch (Throwable t) {
            logger.log(Level.WARNING, "Exception thrown by stats counter", t);
        }
    }

    @Override
    public void recordEviction() {
        try {
            this.delegate.recordEviction();
        }
        catch (Throwable t) {
            logger.log(Level.WARNING, "Exception thrown by stats counter", t);
        }
    }

    @Override
    public void recordEviction(int weight) {
        try {
            this.delegate.recordEviction(weight);
        }
        catch (Throwable t) {
            logger.log(Level.WARNING, "Exception thrown by stats counter", t);
        }
    }

    @Override
    public void recordEviction(int weight, RemovalCause cause) {
        try {
            this.delegate.recordEviction(weight, cause);
        }
        catch (Throwable t) {
            logger.log(Level.WARNING, "Exception thrown by stats counter", t);
        }
    }

    @Override
    public CacheStats snapshot() {
        try {
            return this.delegate.snapshot();
        }
        catch (Throwable t) {
            logger.log(Level.WARNING, "Exception thrown by stats counter", t);
            return CacheStats.empty();
        }
    }

    public String toString() {
        return this.delegate.toString();
    }
}

