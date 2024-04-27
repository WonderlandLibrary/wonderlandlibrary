/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.Immutable
 *  org.checkerframework.checker.index.qual.NonNegative
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package com.github.benmanes.caffeine.cache.stats;

import com.google.errorprone.annotations.Immutable;
import java.util.Objects;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

@Immutable
public final class CacheStats {
    private static final CacheStats EMPTY_STATS = CacheStats.of(0L, 0L, 0L, 0L, 0L, 0L, 0L);
    private final long hitCount;
    private final long missCount;
    private final long loadSuccessCount;
    private final long loadFailureCount;
    private final long totalLoadTime;
    private final long evictionCount;
    private final long evictionWeight;

    @Deprecated
    public CacheStats(@NonNegative long hitCount, @NonNegative long missCount, @NonNegative long loadSuccessCount, @NonNegative long loadFailureCount, @NonNegative long totalLoadTime, @NonNegative long evictionCount) {
        this(hitCount, missCount, loadSuccessCount, loadFailureCount, totalLoadTime, evictionCount, 0L);
    }

    @Deprecated
    public CacheStats(@NonNegative long hitCount, @NonNegative long missCount, @NonNegative long loadSuccessCount, @NonNegative long loadFailureCount, @NonNegative long totalLoadTime, @NonNegative long evictionCount, @NonNegative long evictionWeight) {
        if (hitCount < 0L || missCount < 0L || loadSuccessCount < 0L || loadFailureCount < 0L || totalLoadTime < 0L || evictionCount < 0L || evictionWeight < 0L) {
            throw new IllegalArgumentException();
        }
        this.hitCount = hitCount;
        this.missCount = missCount;
        this.loadSuccessCount = loadSuccessCount;
        this.loadFailureCount = loadFailureCount;
        this.totalLoadTime = totalLoadTime;
        this.evictionCount = evictionCount;
        this.evictionWeight = evictionWeight;
    }

    public static CacheStats of(@NonNegative long hitCount, @NonNegative long missCount, @NonNegative long loadSuccessCount, @NonNegative long loadFailureCount, @NonNegative long totalLoadTime, @NonNegative long evictionCount, @NonNegative long evictionWeight) {
        return new CacheStats(hitCount, missCount, loadSuccessCount, loadFailureCount, totalLoadTime, evictionCount, evictionWeight);
    }

    public static @NonNull CacheStats empty() {
        return EMPTY_STATS;
    }

    public @NonNegative long requestCount() {
        return CacheStats.saturatedAdd(this.hitCount, this.missCount);
    }

    public @NonNegative long hitCount() {
        return this.hitCount;
    }

    public @NonNegative double hitRate() {
        long requestCount = this.requestCount();
        return requestCount == 0L ? 1.0 : (double)this.hitCount / (double)requestCount;
    }

    public @NonNegative long missCount() {
        return this.missCount;
    }

    public @NonNegative double missRate() {
        long requestCount = this.requestCount();
        return requestCount == 0L ? 0.0 : (double)this.missCount / (double)requestCount;
    }

    public @NonNegative long loadCount() {
        return CacheStats.saturatedAdd(this.loadSuccessCount, this.loadFailureCount);
    }

    public @NonNegative long loadSuccessCount() {
        return this.loadSuccessCount;
    }

    public @NonNegative long loadFailureCount() {
        return this.loadFailureCount;
    }

    public @NonNegative double loadFailureRate() {
        long totalLoadCount = CacheStats.saturatedAdd(this.loadSuccessCount, this.loadFailureCount);
        return totalLoadCount == 0L ? 0.0 : (double)this.loadFailureCount / (double)totalLoadCount;
    }

    public @NonNegative long totalLoadTime() {
        return this.totalLoadTime;
    }

    public @NonNegative double averageLoadPenalty() {
        long totalLoadCount = CacheStats.saturatedAdd(this.loadSuccessCount, this.loadFailureCount);
        return totalLoadCount == 0L ? 0.0 : (double)this.totalLoadTime / (double)totalLoadCount;
    }

    public @NonNegative long evictionCount() {
        return this.evictionCount;
    }

    public @NonNegative long evictionWeight() {
        return this.evictionWeight;
    }

    public @NonNull CacheStats minus(@NonNull CacheStats other) {
        return CacheStats.of(Math.max(0L, CacheStats.saturatedSubtract(this.hitCount, other.hitCount)), Math.max(0L, CacheStats.saturatedSubtract(this.missCount, other.missCount)), Math.max(0L, CacheStats.saturatedSubtract(this.loadSuccessCount, other.loadSuccessCount)), Math.max(0L, CacheStats.saturatedSubtract(this.loadFailureCount, other.loadFailureCount)), Math.max(0L, CacheStats.saturatedSubtract(this.totalLoadTime, other.totalLoadTime)), Math.max(0L, CacheStats.saturatedSubtract(this.evictionCount, other.evictionCount)), Math.max(0L, CacheStats.saturatedSubtract(this.evictionWeight, other.evictionWeight)));
    }

    public @NonNull CacheStats plus(@NonNull CacheStats other) {
        return CacheStats.of(CacheStats.saturatedAdd(this.hitCount, other.hitCount), CacheStats.saturatedAdd(this.missCount, other.missCount), CacheStats.saturatedAdd(this.loadSuccessCount, other.loadSuccessCount), CacheStats.saturatedAdd(this.loadFailureCount, other.loadFailureCount), CacheStats.saturatedAdd(this.totalLoadTime, other.totalLoadTime), CacheStats.saturatedAdd(this.evictionCount, other.evictionCount), CacheStats.saturatedAdd(this.evictionWeight, other.evictionWeight));
    }

    private static long saturatedSubtract(long a, long b) {
        long naiveDifference;
        if ((a ^ b) >= 0L | (a ^ (naiveDifference = a - b)) >= 0L) {
            return naiveDifference;
        }
        return Long.MAX_VALUE + (naiveDifference >>> 63 ^ 1L);
    }

    private static long saturatedAdd(long a, long b) {
        long naiveSum;
        if ((a ^ b) < 0L | (a ^ (naiveSum = a + b)) >= 0L) {
            return naiveSum;
        }
        return Long.MAX_VALUE + (naiveSum >>> 63 ^ 1L);
    }

    public int hashCode() {
        return Objects.hash(this.hitCount, this.missCount, this.loadSuccessCount, this.loadFailureCount, this.totalLoadTime, this.evictionCount, this.evictionWeight);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CacheStats)) {
            return false;
        }
        CacheStats other = (CacheStats)o;
        return this.hitCount == other.hitCount && this.missCount == other.missCount && this.loadSuccessCount == other.loadSuccessCount && this.loadFailureCount == other.loadFailureCount && this.totalLoadTime == other.totalLoadTime && this.evictionCount == other.evictionCount && this.evictionWeight == other.evictionWeight;
    }

    public String toString() {
        return this.getClass().getSimpleName() + '{' + "hitCount=" + this.hitCount + ", missCount=" + this.missCount + ", loadSuccessCount=" + this.loadSuccessCount + ", loadFailureCount=" + this.loadFailureCount + ", totalLoadTime=" + this.totalLoadTime + ", evictionCount=" + this.evictionCount + ", evictionWeight=" + this.evictionWeight + '}';
    }
}

