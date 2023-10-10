// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.cache;

import com.google.common.base.MoreObjects;
import javax.annotation.CheckForNull;
import com.google.common.base.Objects;
import com.google.common.math.LongMath;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class CacheStats
{
    private final long hitCount;
    private final long missCount;
    private final long loadSuccessCount;
    private final long loadExceptionCount;
    private final long totalLoadTime;
    private final long evictionCount;
    
    public CacheStats(final long hitCount, final long missCount, final long loadSuccessCount, final long loadExceptionCount, final long totalLoadTime, final long evictionCount) {
        Preconditions.checkArgument(hitCount >= 0L);
        Preconditions.checkArgument(missCount >= 0L);
        Preconditions.checkArgument(loadSuccessCount >= 0L);
        Preconditions.checkArgument(loadExceptionCount >= 0L);
        Preconditions.checkArgument(totalLoadTime >= 0L);
        Preconditions.checkArgument(evictionCount >= 0L);
        this.hitCount = hitCount;
        this.missCount = missCount;
        this.loadSuccessCount = loadSuccessCount;
        this.loadExceptionCount = loadExceptionCount;
        this.totalLoadTime = totalLoadTime;
        this.evictionCount = evictionCount;
    }
    
    public long requestCount() {
        return LongMath.saturatedAdd(this.hitCount, this.missCount);
    }
    
    public long hitCount() {
        return this.hitCount;
    }
    
    public double hitRate() {
        final long requestCount = this.requestCount();
        return (requestCount == 0L) ? 1.0 : (this.hitCount / (double)requestCount);
    }
    
    public long missCount() {
        return this.missCount;
    }
    
    public double missRate() {
        final long requestCount = this.requestCount();
        return (requestCount == 0L) ? 0.0 : (this.missCount / (double)requestCount);
    }
    
    public long loadCount() {
        return LongMath.saturatedAdd(this.loadSuccessCount, this.loadExceptionCount);
    }
    
    public long loadSuccessCount() {
        return this.loadSuccessCount;
    }
    
    public long loadExceptionCount() {
        return this.loadExceptionCount;
    }
    
    public double loadExceptionRate() {
        final long totalLoadCount = LongMath.saturatedAdd(this.loadSuccessCount, this.loadExceptionCount);
        return (totalLoadCount == 0L) ? 0.0 : (this.loadExceptionCount / (double)totalLoadCount);
    }
    
    public long totalLoadTime() {
        return this.totalLoadTime;
    }
    
    public double averageLoadPenalty() {
        final long totalLoadCount = LongMath.saturatedAdd(this.loadSuccessCount, this.loadExceptionCount);
        return (totalLoadCount == 0L) ? 0.0 : (this.totalLoadTime / (double)totalLoadCount);
    }
    
    public long evictionCount() {
        return this.evictionCount;
    }
    
    public CacheStats minus(final CacheStats other) {
        return new CacheStats(Math.max(0L, LongMath.saturatedSubtract(this.hitCount, other.hitCount)), Math.max(0L, LongMath.saturatedSubtract(this.missCount, other.missCount)), Math.max(0L, LongMath.saturatedSubtract(this.loadSuccessCount, other.loadSuccessCount)), Math.max(0L, LongMath.saturatedSubtract(this.loadExceptionCount, other.loadExceptionCount)), Math.max(0L, LongMath.saturatedSubtract(this.totalLoadTime, other.totalLoadTime)), Math.max(0L, LongMath.saturatedSubtract(this.evictionCount, other.evictionCount)));
    }
    
    public CacheStats plus(final CacheStats other) {
        return new CacheStats(LongMath.saturatedAdd(this.hitCount, other.hitCount), LongMath.saturatedAdd(this.missCount, other.missCount), LongMath.saturatedAdd(this.loadSuccessCount, other.loadSuccessCount), LongMath.saturatedAdd(this.loadExceptionCount, other.loadExceptionCount), LongMath.saturatedAdd(this.totalLoadTime, other.totalLoadTime), LongMath.saturatedAdd(this.evictionCount, other.evictionCount));
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.hitCount, this.missCount, this.loadSuccessCount, this.loadExceptionCount, this.totalLoadTime, this.evictionCount);
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        if (object instanceof CacheStats) {
            final CacheStats other = (CacheStats)object;
            return this.hitCount == other.hitCount && this.missCount == other.missCount && this.loadSuccessCount == other.loadSuccessCount && this.loadExceptionCount == other.loadExceptionCount && this.totalLoadTime == other.totalLoadTime && this.evictionCount == other.evictionCount;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("hitCount", this.hitCount).add("missCount", this.missCount).add("loadSuccessCount", this.loadSuccessCount).add("loadExceptionCount", this.loadExceptionCount).add("totalLoadTime", this.totalLoadTime).add("evictionCount", this.evictionCount).toString();
    }
}
