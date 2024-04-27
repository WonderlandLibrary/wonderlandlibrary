/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.FormatMethod
 *  org.checkerframework.checker.index.qual.NonNegative
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Async;
import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.BoundedLocalCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.github.benmanes.caffeine.cache.Ticker;
import com.github.benmanes.caffeine.cache.UnboundedLocalCache;
import com.github.benmanes.caffeine.cache.Weigher;
import com.github.benmanes.caffeine.cache.stats.ConcurrentStatsCounter;
import com.github.benmanes.caffeine.cache.stats.StatsCounter;
import com.google.errorprone.annotations.FormatMethod;
import java.io.Serializable;
import java.time.Duration;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class Caffeine<K, V> {
    static final Logger logger = Logger.getLogger(Caffeine.class.getName());
    static final Supplier<StatsCounter> ENABLED_STATS_COUNTER_SUPPLIER = ConcurrentStatsCounter::new;
    static final int UNSET_INT = -1;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int DEFAULT_EXPIRATION_NANOS = 0;
    static final int DEFAULT_REFRESH_NANOS = 0;
    boolean strictParsing = true;
    long maximumSize = -1L;
    long maximumWeight = -1L;
    int initialCapacity = -1;
    long expireAfterWriteNanos = -1L;
    long expireAfterAccessNanos = -1L;
    long refreshAfterWriteNanos = -1L;
    @Nullable RemovalListener<? super K, ? super V> evictionListener;
    @Nullable RemovalListener<? super K, ? super V> removalListener;
    @Nullable Supplier<StatsCounter> statsCounterSupplier;
    @Nullable CacheWriter<? super K, ? super V> writer;
    @Nullable Weigher<? super K, ? super V> weigher;
    @Nullable Expiry<? super K, ? super V> expiry;
    @Nullable Scheduler scheduler;
    @Nullable Executor executor;
    @Nullable Ticker ticker;
    @Nullable Strength keyStrength;
    @Nullable Strength valueStrength;

    private Caffeine() {
    }

    @FormatMethod
    static void requireArgument(boolean expression, String template, Object ... args2) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(template, args2));
        }
    }

    static void requireArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    static void requireState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    @FormatMethod
    static void requireState(boolean expression, String template, Object ... args2) {
        if (!expression) {
            throw new IllegalStateException(String.format(template, args2));
        }
    }

    static int ceilingPowerOfTwo(int x) {
        return 1 << -Integer.numberOfLeadingZeros(x - 1);
    }

    static long ceilingPowerOfTwo(long x) {
        return 1L << -Long.numberOfLeadingZeros(x - 1L);
    }

    public static @NonNull Caffeine<Object, Object> newBuilder() {
        return new Caffeine<Object, Object>();
    }

    public static @NonNull Caffeine<Object, Object> from(CaffeineSpec spec) {
        Caffeine<Object, Object> builder = spec.toBuilder();
        builder.strictParsing = false;
        return builder;
    }

    public static @NonNull Caffeine<Object, Object> from(String spec) {
        return Caffeine.from(CaffeineSpec.parse(spec));
    }

    public @NonNull Caffeine<K, V> initialCapacity(@NonNegative int initialCapacity) {
        Caffeine.requireState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
        Caffeine.requireArgument(initialCapacity >= 0);
        this.initialCapacity = initialCapacity;
        return this;
    }

    boolean hasInitialCapacity() {
        return this.initialCapacity != -1;
    }

    int getInitialCapacity() {
        return this.hasInitialCapacity() ? this.initialCapacity : 16;
    }

    public @NonNull Caffeine<K, V> executor(@NonNull Executor executor) {
        Caffeine.requireState(this.executor == null, "executor was already set to %s", this.executor);
        this.executor = Objects.requireNonNull(executor);
        return this;
    }

    @NonNull Executor getExecutor() {
        return this.executor == null ? ForkJoinPool.commonPool() : this.executor;
    }

    public @NonNull Caffeine<K, V> scheduler(@NonNull Scheduler scheduler) {
        Caffeine.requireState(this.scheduler == null, "scheduler was already set to %s", this.scheduler);
        this.scheduler = Objects.requireNonNull(scheduler);
        return this;
    }

    @NonNull Scheduler getScheduler() {
        if (this.scheduler == null || this.scheduler == Scheduler.disabledScheduler()) {
            return Scheduler.disabledScheduler();
        }
        if (this.scheduler == Scheduler.systemScheduler()) {
            return this.scheduler;
        }
        return Scheduler.guardedScheduler(this.scheduler);
    }

    public @NonNull Caffeine<K, V> maximumSize(@NonNegative long maximumSize) {
        Caffeine.requireState(this.maximumSize == -1L, "maximum size was already set to %s", this.maximumSize);
        Caffeine.requireState(this.maximumWeight == -1L, "maximum weight was already set to %s", this.maximumWeight);
        Caffeine.requireState(this.weigher == null, "maximum size can not be combined with weigher", new Object[0]);
        Caffeine.requireArgument(maximumSize >= 0L, "maximum size must not be negative", new Object[0]);
        this.maximumSize = maximumSize;
        return this;
    }

    public @NonNull Caffeine<K, V> maximumWeight(@NonNegative long maximumWeight) {
        Caffeine.requireState(this.maximumWeight == -1L, "maximum weight was already set to %s", this.maximumWeight);
        Caffeine.requireState(this.maximumSize == -1L, "maximum size was already set to %s", this.maximumSize);
        Caffeine.requireArgument(maximumWeight >= 0L, "maximum weight must not be negative", new Object[0]);
        this.maximumWeight = maximumWeight;
        return this;
    }

    public <K1 extends K, V1 extends V> @NonNull Caffeine<K1, V1> weigher(@NonNull Weigher<? super K1, ? super V1> weigher) {
        Objects.requireNonNull(weigher);
        Caffeine.requireState(this.weigher == null, "weigher was already set to %s", this.weigher);
        Caffeine.requireState(!this.strictParsing || this.maximumSize == -1L, "weigher can not be combined with maximum size", new Object[0]);
        Caffeine self = this;
        self.weigher = weigher;
        return self;
    }

    boolean evicts() {
        return this.getMaximum() != -1L;
    }

    boolean isWeighted() {
        return this.weigher != null;
    }

    long getMaximum() {
        return this.isWeighted() ? this.maximumWeight : this.maximumSize;
    }

    <K1 extends K, V1 extends V> @NonNull Weigher<K1, V1> getWeigher(boolean isAsync) {
        Async.AsyncWeigher delegate = this.weigher == null || this.weigher == Weigher.singletonWeigher() ? Weigher.singletonWeigher() : Weigher.boundedWeigher(this.weigher);
        return isAsync ? new Async.AsyncWeigher(delegate) : delegate;
    }

    public @NonNull Caffeine<K, V> weakKeys() {
        Caffeine.requireState(this.keyStrength == null, "Key strength was already set to %s", new Object[]{this.keyStrength});
        Caffeine.requireState(this.writer == null, "Weak keys may not be used with CacheWriter", new Object[0]);
        this.keyStrength = Strength.WEAK;
        return this;
    }

    boolean isStrongKeys() {
        return this.keyStrength == null;
    }

    public @NonNull Caffeine<K, V> weakValues() {
        Caffeine.requireState(this.valueStrength == null, "Value strength was already set to %s", new Object[]{this.valueStrength});
        this.valueStrength = Strength.WEAK;
        return this;
    }

    boolean isStrongValues() {
        return this.valueStrength == null;
    }

    boolean isWeakValues() {
        return this.valueStrength == Strength.WEAK;
    }

    public @NonNull Caffeine<K, V> softValues() {
        Caffeine.requireState(this.valueStrength == null, "Value strength was already set to %s", new Object[]{this.valueStrength});
        this.valueStrength = Strength.SOFT;
        return this;
    }

    public @NonNull Caffeine<K, V> expireAfterWrite(@NonNull Duration duration) {
        return this.expireAfterWrite(Caffeine.saturatedToNanos(duration), TimeUnit.NANOSECONDS);
    }

    public @NonNull Caffeine<K, V> expireAfterWrite(@NonNegative long duration, @NonNull TimeUnit unit) {
        Caffeine.requireState(this.expireAfterWriteNanos == -1L, "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
        Caffeine.requireState(this.expiry == null, "expireAfterWrite may not be used with variable expiration", new Object[0]);
        Caffeine.requireArgument(duration >= 0L, "duration cannot be negative: %s %s", new Object[]{duration, unit});
        this.expireAfterWriteNanos = unit.toNanos(duration);
        return this;
    }

    long getExpiresAfterWriteNanos() {
        return this.expiresAfterWrite() ? this.expireAfterWriteNanos : 0L;
    }

    boolean expiresAfterWrite() {
        return this.expireAfterWriteNanos != -1L;
    }

    public @NonNull Caffeine<K, V> expireAfterAccess(@NonNull Duration duration) {
        return this.expireAfterAccess(Caffeine.saturatedToNanos(duration), TimeUnit.NANOSECONDS);
    }

    public @NonNull Caffeine<K, V> expireAfterAccess(@NonNegative long duration, @NonNull TimeUnit unit) {
        Caffeine.requireState(this.expireAfterAccessNanos == -1L, "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
        Caffeine.requireState(this.expiry == null, "expireAfterAccess may not be used with variable expiration", new Object[0]);
        Caffeine.requireArgument(duration >= 0L, "duration cannot be negative: %s %s", new Object[]{duration, unit});
        this.expireAfterAccessNanos = unit.toNanos(duration);
        return this;
    }

    long getExpiresAfterAccessNanos() {
        return this.expiresAfterAccess() ? this.expireAfterAccessNanos : 0L;
    }

    boolean expiresAfterAccess() {
        return this.expireAfterAccessNanos != -1L;
    }

    public <K1 extends K, V1 extends V> @NonNull Caffeine<K1, V1> expireAfter(@NonNull Expiry<? super K1, ? super V1> expiry) {
        Objects.requireNonNull(expiry);
        Caffeine.requireState(this.expiry == null, "Expiry was already set to %s", this.expiry);
        Caffeine.requireState(this.expireAfterAccessNanos == -1L, "Expiry may not be used with expiresAfterAccess", new Object[0]);
        Caffeine.requireState(this.expireAfterWriteNanos == -1L, "Expiry may not be used with expiresAfterWrite", new Object[0]);
        Caffeine self = this;
        self.expiry = expiry;
        return self;
    }

    boolean expiresVariable() {
        return this.expiry != null;
    }

    @Nullable Expiry<K, V> getExpiry(boolean isAsync) {
        return isAsync && this.expiry != null ? new Async.AsyncExpiry<K, V>(this.expiry) : this.expiry;
    }

    public @NonNull Caffeine<K, V> refreshAfterWrite(@NonNull Duration duration) {
        return this.refreshAfterWrite(Caffeine.saturatedToNanos(duration), TimeUnit.NANOSECONDS);
    }

    public @NonNull Caffeine<K, V> refreshAfterWrite(@NonNegative long duration, @NonNull TimeUnit unit) {
        Objects.requireNonNull(unit);
        Caffeine.requireState(this.refreshAfterWriteNanos == -1L, "refreshAfterWriteNanos was already set to %s ns", this.refreshAfterWriteNanos);
        Caffeine.requireArgument(duration > 0L, "duration must be positive: %s %s", new Object[]{duration, unit});
        this.refreshAfterWriteNanos = unit.toNanos(duration);
        return this;
    }

    long getRefreshAfterWriteNanos() {
        return this.refreshAfterWrite() ? this.refreshAfterWriteNanos : 0L;
    }

    boolean refreshAfterWrite() {
        return this.refreshAfterWriteNanos != -1L;
    }

    public @NonNull Caffeine<K, V> ticker(@NonNull Ticker ticker) {
        Caffeine.requireState(this.ticker == null, "Ticker was already set to %s", this.ticker);
        this.ticker = Objects.requireNonNull(ticker);
        return this;
    }

    @NonNull Ticker getTicker() {
        boolean useTicker;
        boolean bl = useTicker = this.expiresVariable() || this.expiresAfterAccess() || this.expiresAfterWrite() || this.refreshAfterWrite() || this.isRecordingStats();
        return useTicker ? (this.ticker == null ? Ticker.systemTicker() : this.ticker) : Ticker.disabledTicker();
    }

    public <K1 extends K, V1 extends V> @NonNull Caffeine<K1, V1> evictionListener(@NonNull RemovalListener<? super K1, ? super V1> evictionListener) {
        Caffeine.requireState(this.evictionListener == null, "eviction listener was already set to %s", this.evictionListener);
        Caffeine self = this;
        self.evictionListener = Objects.requireNonNull(evictionListener);
        return self;
    }

    public <K1 extends K, V1 extends V> @NonNull Caffeine<K1, V1> removalListener(@NonNull RemovalListener<? super K1, ? super V1> removalListener) {
        Caffeine.requireState(this.removalListener == null, "removal listener was already set to %s", this.removalListener);
        Caffeine self = this;
        self.removalListener = Objects.requireNonNull(removalListener);
        return self;
    }

    <K1 extends K, V1 extends V> @Nullable RemovalListener<K1, V1> getRemovalListener(boolean async) {
        Async.AsyncRemovalListener castedListener = this.removalListener;
        return async && castedListener != null ? new Async.AsyncRemovalListener(castedListener, this.getExecutor()) : castedListener;
    }

    @Deprecated
    public <K1 extends K, V1 extends V> @NonNull Caffeine<K1, V1> writer(@NonNull CacheWriter<? super K1, ? super V1> writer) {
        Caffeine.requireState(this.writer == null, "Writer was already set to %s", this.writer);
        Caffeine.requireState(this.keyStrength == null, "Weak keys may not be used with CacheWriter", new Object[0]);
        Caffeine.requireState(this.evictionListener == null, "Eviction listener may not be used with CacheWriter", new Object[0]);
        Caffeine self = this;
        self.writer = Objects.requireNonNull(writer);
        return self;
    }

    <K1 extends K, V1 extends V> CacheWriter<K1, V1> getCacheWriter(boolean async) {
        CacheWriter<? super K, ? super V> castedWriter = this.evictionListener == null ? this.writer : new CacheWriterAdapter<K, V>(this.evictionListener, async);
        return castedWriter == null ? CacheWriter.disabledWriter() : castedWriter;
    }

    public @NonNull Caffeine<K, V> recordStats() {
        Caffeine.requireState(this.statsCounterSupplier == null, "Statistics recording was already set", new Object[0]);
        this.statsCounterSupplier = ENABLED_STATS_COUNTER_SUPPLIER;
        return this;
    }

    public @NonNull Caffeine<K, V> recordStats(@NonNull Supplier<? extends StatsCounter> statsCounterSupplier) {
        Caffeine.requireState(this.statsCounterSupplier == null, "Statistics recording was already set", new Object[0]);
        Objects.requireNonNull(statsCounterSupplier);
        this.statsCounterSupplier = () -> StatsCounter.guardedStatsCounter((StatsCounter)statsCounterSupplier.get());
        return this;
    }

    boolean isRecordingStats() {
        return this.statsCounterSupplier != null;
    }

    @NonNull Supplier<StatsCounter> getStatsCounterSupplier() {
        return this.statsCounterSupplier == null ? StatsCounter::disabledStatsCounter : this.statsCounterSupplier;
    }

    boolean isBounded() {
        return this.maximumSize != -1L || this.maximumWeight != -1L || this.expireAfterAccessNanos != -1L || this.expireAfterWriteNanos != -1L || this.expiry != null || this.keyStrength != null || this.valueStrength != null;
    }

    public <K1 extends K, V1 extends V> @NonNull Cache<K1, V1> build() {
        this.requireWeightWithWeigher();
        this.requireNonLoadingCache();
        Caffeine self = this;
        return this.isBounded() ? new BoundedLocalCache.BoundedLocalManualCache(self) : new UnboundedLocalCache.UnboundedLocalManualCache(self);
    }

    public <K1 extends K, V1 extends V> @NonNull LoadingCache<K1, V1> build(@NonNull CacheLoader<? super K1, V1> loader) {
        this.requireWeightWithWeigher();
        Caffeine self = this;
        return this.isBounded() || this.refreshAfterWrite() ? new BoundedLocalCache.BoundedLocalLoadingCache<K1, V1>(self, loader) : new UnboundedLocalCache.UnboundedLocalLoadingCache<K1, V1>(self, loader);
    }

    public <K1 extends K, V1 extends V> @NonNull AsyncCache<K1, V1> buildAsync() {
        Caffeine.requireState(this.valueStrength == null, "Weak or soft values can not be combined with AsyncCache", new Object[0]);
        Caffeine.requireState(this.writer == null, "CacheWriter can not be combined with AsyncCache", new Object[0]);
        Caffeine.requireState(this.isStrongKeys() || this.evictionListener == null, "Weak keys cannot be combined eviction listener and with AsyncLoadingCache", new Object[0]);
        this.requireWeightWithWeigher();
        this.requireNonLoadingCache();
        Caffeine self = this;
        return this.isBounded() ? new BoundedLocalCache.BoundedLocalAsyncCache(self) : new UnboundedLocalCache.UnboundedLocalAsyncCache(self);
    }

    public <K1 extends K, V1 extends V> @NonNull AsyncLoadingCache<K1, V1> buildAsync(@NonNull CacheLoader<? super K1, V1> loader) {
        return this.buildAsync((AsyncCacheLoader<? super K1, V1>)loader);
    }

    public <K1 extends K, V1 extends V> @NonNull AsyncLoadingCache<K1, V1> buildAsync(@NonNull AsyncCacheLoader<? super K1, V1> loader) {
        Caffeine.requireState(this.isStrongValues(), "Weak or soft values cannot be combined with AsyncLoadingCache", new Object[0]);
        Caffeine.requireState(this.writer == null, "CacheWriter cannot be combined with AsyncLoadingCache", new Object[0]);
        Caffeine.requireState(this.isStrongKeys() || this.evictionListener == null, "Weak keys cannot be combined eviction listener and with AsyncLoadingCache", new Object[0]);
        this.requireWeightWithWeigher();
        Objects.requireNonNull(loader);
        Caffeine self = this;
        return this.isBounded() || this.refreshAfterWrite() ? new BoundedLocalCache.BoundedLocalAsyncLoadingCache<K1, V1>(self, loader) : new UnboundedLocalCache.UnboundedLocalAsyncLoadingCache<K1, V1>(self, loader);
    }

    void requireNonLoadingCache() {
        Caffeine.requireState(this.refreshAfterWriteNanos == -1L, "refreshAfterWrite requires a LoadingCache", new Object[0]);
    }

    void requireWeightWithWeigher() {
        if (this.weigher == null) {
            Caffeine.requireState(this.maximumWeight == -1L, "maximumWeight requires weigher", new Object[0]);
        } else if (this.strictParsing) {
            Caffeine.requireState(this.maximumWeight != -1L, "weigher requires maximumWeight", new Object[0]);
        } else if (this.maximumWeight == -1L) {
            logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
        }
    }

    private static long saturatedToNanos(Duration duration) {
        try {
            return duration.toNanos();
        }
        catch (ArithmeticException tooBig) {
            return duration.isNegative() ? Long.MIN_VALUE : Long.MAX_VALUE;
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder(75);
        s.append(this.getClass().getSimpleName()).append('{');
        int baseLength = s.length();
        if (this.initialCapacity != -1) {
            s.append("initialCapacity=").append(this.initialCapacity).append(", ");
        }
        if (this.maximumSize != -1L) {
            s.append("maximumSize=").append(this.maximumSize).append(", ");
        }
        if (this.maximumWeight != -1L) {
            s.append("maximumWeight=").append(this.maximumWeight).append(", ");
        }
        if (this.expireAfterWriteNanos != -1L) {
            s.append("expireAfterWrite=").append(this.expireAfterWriteNanos).append("ns, ");
        }
        if (this.expireAfterAccessNanos != -1L) {
            s.append("expireAfterAccess=").append(this.expireAfterAccessNanos).append("ns, ");
        }
        if (this.expiry != null) {
            s.append("expiry, ");
        }
        if (this.refreshAfterWriteNanos != -1L) {
            s.append("refreshAfterWriteNanos=").append(this.refreshAfterWriteNanos).append("ns, ");
        }
        if (this.keyStrength != null) {
            s.append("keyStrength=").append(this.keyStrength.toString().toLowerCase(Locale.US)).append(", ");
        }
        if (this.valueStrength != null) {
            s.append("valueStrength=").append(this.valueStrength.toString().toLowerCase(Locale.US)).append(", ");
        }
        if (this.evictionListener != null) {
            s.append("evictionListener, ");
        }
        if (this.removalListener != null) {
            s.append("removalListener, ");
        }
        if (this.writer != null) {
            s.append("writer, ");
        }
        if (s.length() > baseLength) {
            s.deleteCharAt(s.length() - 2);
        }
        return s.append('}').toString();
    }

    static final class CacheWriterAdapter<K, V>
    implements CacheWriter<K, V>,
    Serializable {
        private static final long serialVersionUID = 1L;
        final RemovalListener<? super K, ? super V> delegate;
        final boolean isAsync;

        CacheWriterAdapter(RemovalListener<? super K, ? super V> delegate, boolean isAsync) {
            this.delegate = delegate;
            this.isAsync = isAsync;
        }

        @Override
        public void write(K key, V value) {
        }

        @Override
        public void delete(K key, @Nullable V value, RemovalCause cause) {
            if (cause.wasEvicted()) {
                try {
                    if (this.isAsync && value != null) {
                        CompletableFuture future = (CompletableFuture)value;
                        value = Async.getIfReady(future);
                    }
                    this.delegate.onRemoval(key, value, cause);
                }
                catch (Throwable t) {
                    logger.log(Level.WARNING, "Exception thrown by eviction listener", t);
                }
            }
        }
    }

    static enum Strength {
        WEAK,
        SOFT;

    }
}

