/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.LocalAsyncCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

abstract class LocalAsyncLoadingCache<K, V>
implements LocalAsyncCache<K, V>,
AsyncLoadingCache<K, V> {
    static final Logger logger = Logger.getLogger(LocalAsyncLoadingCache.class.getName());
    final boolean canBulkLoad;
    final AsyncCacheLoader<K, V> loader;
    @Nullable LoadingCacheView<K, V> cacheView;

    LocalAsyncLoadingCache(AsyncCacheLoader<? super K, V> loader) {
        this.loader = loader;
        this.canBulkLoad = LocalAsyncLoadingCache.canBulkLoad(loader);
    }

    private static boolean canBulkLoad(AsyncCacheLoader<?, ?> loader) {
        try {
            Class<AsyncCacheLoader> defaultLoaderClass = AsyncCacheLoader.class;
            if (loader instanceof CacheLoader) {
                Method defaultLoadAll;
                defaultLoaderClass = CacheLoader.class;
                Method classLoadAll = loader.getClass().getMethod("loadAll", Iterable.class);
                if (!classLoadAll.equals(defaultLoadAll = CacheLoader.class.getMethod("loadAll", Iterable.class))) {
                    return true;
                }
            }
            Method classAsyncLoadAll = loader.getClass().getMethod("asyncLoadAll", Iterable.class, Executor.class);
            Method defaultAsyncLoadAll = defaultLoaderClass.getMethod("asyncLoadAll", Iterable.class, Executor.class);
            return !classAsyncLoadAll.equals(defaultAsyncLoadAll);
        }
        catch (NoSuchMethodException | SecurityException e) {
            logger.log(Level.WARNING, "Cannot determine if CacheLoader can bulk load", e);
            return false;
        }
    }

    @Override
    public CompletableFuture<V> get(K key) {
        return this.get(key, this.loader::asyncLoad);
    }

    @Override
    public CompletableFuture<Map<K, V>> getAll(Iterable<? extends K> keys2) {
        if (this.canBulkLoad) {
            return this.getAll(keys2, this.loader::asyncLoadAll);
        }
        LinkedHashMap<Object, CompletableFuture> result = new LinkedHashMap<Object, CompletableFuture>();
        Function<Object, CompletableFuture> mappingFunction = this::get;
        for (K key : keys2) {
            CompletableFuture future = result.computeIfAbsent(key, mappingFunction);
            Objects.requireNonNull(future);
        }
        return this.composeResult(result);
    }

    @Override
    public LoadingCache<K, V> synchronous() {
        return this.cacheView == null ? (this.cacheView = new LoadingCacheView(this)) : this.cacheView;
    }

    static final class LoadingCacheView<K, V>
    extends LocalAsyncCache.AbstractCacheView<K, V>
    implements LoadingCache<K, V> {
        private static final long serialVersionUID = 1L;
        final LocalAsyncLoadingCache<K, V> asyncCache;

        LoadingCacheView(LocalAsyncLoadingCache<K, V> asyncCache) {
            this.asyncCache = Objects.requireNonNull(asyncCache);
        }

        @Override
        LocalAsyncLoadingCache<K, V> asyncCache() {
            return this.asyncCache;
        }

        @Override
        public V get(K key) {
            return LoadingCacheView.resolve(this.asyncCache.get(key));
        }

        @Override
        public Map<K, V> getAll(Iterable<? extends K> keys2) {
            return LoadingCacheView.resolve(this.asyncCache.getAll(keys2));
        }

        @Override
        public void refresh(K key) {
            CompletableFuture oldValueFuture;
            long[] writeTime;
            block5: {
                block4: {
                    Objects.requireNonNull(key);
                    writeTime = new long[1];
                    oldValueFuture = this.asyncCache.cache().getIfPresentQuietly(key, writeTime);
                    if (oldValueFuture == null) break block4;
                    if (!oldValueFuture.isDone() || !oldValueFuture.isCompletedExceptionally()) break block5;
                }
                this.asyncCache.get(key, this.asyncCache.loader::asyncLoad, false);
                return;
            }
            if (!oldValueFuture.isDone()) {
                return;
            }
            oldValueFuture.thenAccept(oldValue -> {
                long now = this.asyncCache.cache().statsTicker().read();
                CompletableFuture<Object> refreshFuture = oldValue == null ? this.asyncCache.loader.asyncLoad(key, this.asyncCache.cache().executor()) : this.asyncCache.loader.asyncReload(key, oldValue, this.asyncCache.cache().executor());
                refreshFuture.whenComplete((newValue, error) -> {
                    long loadTime = this.asyncCache.cache().statsTicker().read() - now;
                    if (error != null) {
                        this.asyncCache.cache().statsCounter().recordLoadFailure(loadTime);
                        logger.log(Level.WARNING, "Exception thrown during refresh", (Throwable)error);
                        return;
                    }
                    boolean[] discard = new boolean[1];
                    this.asyncCache.cache().compute(key, (k, currentValue) -> {
                        if (currentValue == null) {
                            return newValue == null ? null : refreshFuture;
                        }
                        if (currentValue == oldValueFuture) {
                            long expectedWriteTime = writeTime[0];
                            if (this.asyncCache.cache().hasWriteTime()) {
                                this.asyncCache.cache().getIfPresentQuietly(key, writeTime);
                            }
                            if (writeTime[0] == expectedWriteTime) {
                                return newValue == null ? null : refreshFuture;
                            }
                        }
                        discard[0] = true;
                        return currentValue;
                    }, false, false, true);
                    if (discard[0] && this.asyncCache.cache().hasRemovalListener()) {
                        this.asyncCache.cache().notifyRemoval(key, refreshFuture, RemovalCause.REPLACED);
                    }
                    if (newValue == null) {
                        this.asyncCache.cache().statsCounter().recordLoadFailure(loadTime);
                    } else {
                        this.asyncCache.cache().statsCounter().recordLoadSuccess(loadTime);
                    }
                });
            });
        }
    }
}

