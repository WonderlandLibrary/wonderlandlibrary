/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Async;
import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LocalCache;
import com.github.benmanes.caffeine.cache.Policy;
import com.github.benmanes.caffeine.cache.WriteThroughEntry;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

interface LocalAsyncCache<K, V>
extends AsyncCache<K, V> {
    public static final Logger logger = Logger.getLogger(LocalAsyncCache.class.getName());

    public LocalCache<K, CompletableFuture<V>> cache();

    public Policy<K, V> policy();

    @Override
    default public @Nullable CompletableFuture<V> getIfPresent(@NonNull Object key) {
        return this.cache().getIfPresent(key, true);
    }

    @Override
    default public CompletableFuture<V> get(@NonNull K key, @NonNull Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.get(key, (? super K k1, Executor executor) -> CompletableFuture.supplyAsync(() -> mappingFunction.apply((Object)key), executor));
    }

    @Override
    default public CompletableFuture<V> get(K key, BiFunction<? super K, Executor, CompletableFuture<V>> mappingFunction) {
        return this.get(key, mappingFunction, true);
    }

    default public CompletableFuture<V> get(K key, BiFunction<? super K, Executor, CompletableFuture<V>> mappingFunction, boolean recordStats) {
        long startTime = this.cache().statsTicker().read();
        CompletableFuture[] result = new CompletableFuture[1];
        CompletableFuture future = this.cache().computeIfAbsent(key, k -> {
            result[0] = (CompletableFuture)mappingFunction.apply((K)key, this.cache().executor());
            return Objects.requireNonNull(result[0]);
        }, recordStats, false);
        if (result[0] != null) {
            this.handleCompletion(key, result[0], startTime, false);
        }
        return future;
    }

    @Override
    default public CompletableFuture<Map<K, V>> getAll(Iterable<? extends @NonNull K> keys2, Function<Iterable<? extends K>, Map<K, V>> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.getAll(keys2, (Iterable<? extends K> keysToLoad, Executor executor) -> CompletableFuture.supplyAsync(() -> (Map)mappingFunction.apply((Iterable)keysToLoad), executor));
    }

    @Override
    default public CompletableFuture<Map<K, V>> getAll(Iterable<? extends @NonNull K> keys2, BiFunction<Iterable<? extends K>, Executor, CompletableFuture<Map<K, V>>> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        Objects.requireNonNull(keys2);
        LinkedHashMap<K, CompletableFuture<V>> futures = new LinkedHashMap<K, CompletableFuture<V>>();
        HashMap proxies = new HashMap();
        for (K key : keys2) {
            if (futures.containsKey(key)) continue;
            CompletableFuture<Object> future = this.cache().getIfPresent(key, false);
            if (future == null) {
                CompletableFuture proxy = new CompletableFuture();
                future = this.cache().putIfAbsent(key, proxy);
                if (future == null) {
                    future = proxy;
                    proxies.put(key, proxy);
                }
            }
            futures.put(key, future);
        }
        this.cache().statsCounter().recordMisses(proxies.size());
        this.cache().statsCounter().recordHits(futures.size() - proxies.size());
        if (proxies.isEmpty()) {
            return this.composeResult(futures);
        }
        AsyncBulkCompleter completer = new AsyncBulkCompleter(this.cache(), proxies);
        try {
            mappingFunction.apply(proxies.keySet(), this.cache().executor()).whenComplete((BiConsumer)completer);
            return this.composeResult(futures);
        }
        catch (Throwable t) {
            completer.accept(null, t);
            throw t;
        }
    }

    default public CompletableFuture<Map<K, V>> composeResult(Map<K, CompletableFuture<V>> futures) {
        if (futures.isEmpty()) {
            return CompletableFuture.completedFuture(Collections.emptyMap());
        }
        CompletableFuture[] array = futures.values().toArray(new CompletableFuture[0]);
        return CompletableFuture.allOf(array).thenApply(ignored -> {
            LinkedHashMap result = new LinkedHashMap(futures.size());
            futures.forEach((key, future) -> {
                Object value = future.getNow(null);
                if (value != null) {
                    result.put(key, value);
                }
            });
            return Collections.unmodifiableMap(result);
        });
    }

    @Override
    default public void put(K key, CompletableFuture<V> valueFuture) {
        if (valueFuture.isCompletedExceptionally() || valueFuture.isDone() && valueFuture.join() == null) {
            this.cache().statsCounter().recordLoadFailure(0L);
            this.cache().remove(key);
            return;
        }
        long startTime = this.cache().statsTicker().read();
        this.cache().put(key, valueFuture);
        this.handleCompletion(key, valueFuture, startTime, false);
    }

    default public void handleCompletion(K key, CompletableFuture<V> valueFuture, long startTime, boolean recordMiss) {
        AtomicBoolean completed = new AtomicBoolean();
        valueFuture.whenComplete((value, error) -> {
            if (!completed.compareAndSet(false, true)) {
                return;
            }
            long loadTime = this.cache().statsTicker().read() - startTime;
            if (value == null) {
                if (error != null) {
                    logger.log(Level.WARNING, "Exception thrown during asynchronous load", (Throwable)error);
                }
                this.cache().remove(key, valueFuture);
                this.cache().statsCounter().recordLoadFailure(loadTime);
                if (recordMiss) {
                    this.cache().statsCounter().recordMisses(1);
                }
            } else {
                this.cache().replace(key, valueFuture, valueFuture);
                this.cache().statsCounter().recordLoadSuccess(loadTime);
                if (recordMiss) {
                    this.cache().statsCounter().recordMisses(1);
                }
            }
        });
    }

    public static final class AsMapView<K, V>
    extends AbstractMap<K, V>
    implements ConcurrentMap<K, V> {
        final LocalCache<K, CompletableFuture<V>> delegate;
        @Nullable Collection<V> values;
        @Nullable Set<Map.Entry<K, V>> entries;

        AsMapView(LocalCache<K, CompletableFuture<V>> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean isEmpty() {
            return this.delegate.isEmpty();
        }

        @Override
        public int size() {
            return this.delegate.size();
        }

        @Override
        public void clear() {
            this.delegate.clear();
        }

        @Override
        public boolean containsKey(Object key) {
            return this.delegate.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            Objects.requireNonNull(value);
            for (CompletableFuture valueFuture : this.delegate.values()) {
                if (!value.equals(Async.getIfReady(valueFuture))) continue;
                return true;
            }
            return false;
        }

        @Override
        public @Nullable V get(Object key) {
            return Async.getIfReady((CompletableFuture)this.delegate.get(key));
        }

        @Override
        public @Nullable V putIfAbsent(K key, V value) {
            Object prior;
            Objects.requireNonNull(value);
            while (true) {
                CompletableFuture priorFuture;
                if ((priorFuture = (CompletableFuture)this.delegate.get(key)) != null) {
                    if (!priorFuture.isDone()) {
                        Async.getWhenSuccessful(priorFuture);
                        continue;
                    }
                    Object prior2 = Async.getWhenSuccessful(priorFuture);
                    if (prior2 != null) {
                        return prior2;
                    }
                }
                boolean[] added = new boolean[]{false};
                CompletableFuture computed = this.delegate.compute(key, (k, valueFuture) -> {
                    added[0] = valueFuture == null || valueFuture.isDone() && Async.getIfReady(valueFuture) == null;
                    return added[0] ? CompletableFuture.completedFuture(value) : valueFuture;
                }, false, false, false);
                if (added[0]) {
                    return null;
                }
                prior = Async.getWhenSuccessful(computed);
                if (prior != null) break;
            }
            return prior;
        }

        @Override
        public @Nullable V put(K key, V value) {
            Objects.requireNonNull(value);
            CompletableFuture<V> oldValueFuture = this.delegate.put(key, CompletableFuture.completedFuture(value));
            return Async.getWhenSuccessful(oldValueFuture);
        }

        @Override
        public @Nullable V remove(Object key) {
            CompletableFuture oldValueFuture = (CompletableFuture)this.delegate.remove(key);
            return Async.getWhenSuccessful(oldValueFuture);
        }

        @Override
        public boolean remove(Object key, Object value) {
            Objects.requireNonNull(key);
            if (value == null) {
                return false;
            }
            Object castedKey = key;
            boolean[] done = new boolean[]{false};
            boolean[] removed = new boolean[]{false};
            do {
                CompletableFuture future;
                if ((future = (CompletableFuture)this.delegate.get(key)) == null || future.isCompletedExceptionally()) {
                    return false;
                }
                Async.getWhenSuccessful(future);
                this.delegate.compute(castedKey, (k, oldValueFuture) -> {
                    if (oldValueFuture == null) {
                        done[0] = true;
                        return null;
                    }
                    if (!oldValueFuture.isDone()) {
                        return oldValueFuture;
                    }
                    done[0] = true;
                    Object oldValue = Async.getIfReady(oldValueFuture);
                    removed[0] = value.equals(oldValue);
                    return oldValue == null || removed[0] ? null : oldValueFuture;
                }, false, false, true);
            } while (!done[0]);
            return removed[0];
        }

        @Override
        public @Nullable V replace(K key, V value) {
            Objects.requireNonNull(value);
            Object[] oldValue = new Object[1];
            boolean[] done = new boolean[]{false};
            do {
                CompletableFuture future;
                if ((future = (CompletableFuture)this.delegate.get(key)) == null || future.isCompletedExceptionally()) {
                    return null;
                }
                Async.getWhenSuccessful(future);
                this.delegate.compute(key, (k, oldValueFuture) -> {
                    if (oldValueFuture == null) {
                        done[0] = true;
                        return null;
                    }
                    if (!oldValueFuture.isDone()) {
                        return oldValueFuture;
                    }
                    done[0] = true;
                    oldValue[0] = Async.getIfReady(oldValueFuture);
                    return oldValue[0] == null ? null : CompletableFuture.completedFuture(value);
                }, false, false, false);
            } while (!done[0]);
            return (V)oldValue[0];
        }

        @Override
        public boolean replace(K key, V oldValue, V newValue) {
            Objects.requireNonNull(oldValue);
            Objects.requireNonNull(newValue);
            boolean[] done = new boolean[]{false};
            boolean[] replaced = new boolean[]{false};
            do {
                CompletableFuture future;
                if ((future = (CompletableFuture)this.delegate.get(key)) == null || future.isCompletedExceptionally()) {
                    return false;
                }
                Async.getWhenSuccessful(future);
                this.delegate.compute(key, (k, oldValueFuture) -> {
                    if (oldValueFuture == null) {
                        done[0] = true;
                        return null;
                    }
                    if (!oldValueFuture.isDone()) {
                        return oldValueFuture;
                    }
                    done[0] = true;
                    replaced[0] = oldValue.equals(Async.getIfReady(oldValueFuture));
                    return replaced[0] ? CompletableFuture.completedFuture(newValue) : oldValueFuture;
                }, false, false, false);
            } while (!done[0]);
            return replaced[0];
        }

        @Override
        public @Nullable V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
            Object result;
            Objects.requireNonNull(mappingFunction);
            while (true) {
                CompletableFuture priorFuture;
                if ((priorFuture = (CompletableFuture)this.delegate.get(key)) != null) {
                    if (!priorFuture.isDone()) {
                        Async.getWhenSuccessful(priorFuture);
                        continue;
                    }
                    Object prior = Async.getWhenSuccessful(priorFuture);
                    if (prior != null) {
                        this.delegate.statsCounter().recordHits(1);
                        return prior;
                    }
                }
                CompletableFuture[] future = new CompletableFuture[1];
                CompletableFuture computed = this.delegate.compute(key, (k, valueFuture) -> {
                    if (valueFuture != null && valueFuture.isDone() && Async.getIfReady(valueFuture) != null) {
                        return valueFuture;
                    }
                    Object newValue = this.delegate.statsAware(mappingFunction, true).apply(key);
                    if (newValue == null) {
                        return null;
                    }
                    future[0] = CompletableFuture.completedFuture(newValue);
                    return future[0];
                }, false, false, false);
                result = Async.getWhenSuccessful(computed);
                if (computed == future[0] || result != null) break;
            }
            return result;
        }

        @Override
        public @Nullable V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            CompletableFuture valueFuture;
            Objects.requireNonNull(remappingFunction);
            Object[] newValue = new Object[1];
            do {
                Async.getWhenSuccessful((CompletableFuture)this.delegate.get(key));
                valueFuture = this.delegate.computeIfPresent(key, (k, oldValueFuture) -> {
                    if (!oldValueFuture.isDone()) {
                        return oldValueFuture;
                    }
                    Object oldValue = Async.getIfReady(oldValueFuture);
                    if (oldValue == null) {
                        return null;
                    }
                    newValue[0] = remappingFunction.apply((Object)key, (Object)oldValue);
                    return newValue[0] == null ? null : CompletableFuture.completedFuture(newValue[0]);
                });
                if (newValue[0] == null) continue;
                return (V)newValue[0];
            } while (valueFuture != null);
            return null;
        }

        @Override
        public @Nullable V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            CompletableFuture valueFuture;
            Objects.requireNonNull(remappingFunction);
            Object[] newValue = new Object[1];
            do {
                Async.getWhenSuccessful((CompletableFuture)this.delegate.get(key));
                valueFuture = this.delegate.compute(key, (k, oldValueFuture) -> {
                    if (oldValueFuture != null && !oldValueFuture.isDone()) {
                        return oldValueFuture;
                    }
                    Object oldValue = Async.getIfReady(oldValueFuture);
                    BiFunction function = this.delegate.statsAware(remappingFunction, false, true, true);
                    newValue[0] = function.apply(key, oldValue);
                    return newValue[0] == null ? null : CompletableFuture.completedFuture(newValue[0]);
                }, false, false, false);
                if (newValue[0] == null) continue;
                return (V)newValue[0];
            } while (valueFuture != null);
            return null;
        }

        @Override
        public @Nullable V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            CompletableFuture mergedValueFuture;
            Objects.requireNonNull(value);
            Objects.requireNonNull(remappingFunction);
            CompletableFuture<V> newValueFuture = CompletableFuture.completedFuture(value);
            boolean[] merged = new boolean[]{false};
            do {
                Async.getWhenSuccessful((CompletableFuture)this.delegate.get(key));
                mergedValueFuture = this.delegate.merge(key, newValueFuture, (oldValueFuture, valueFuture) -> {
                    if (oldValueFuture != null && !oldValueFuture.isDone()) {
                        return oldValueFuture;
                    }
                    merged[0] = true;
                    Object oldValue = Async.getIfReady(oldValueFuture);
                    if (oldValue == null) {
                        return valueFuture;
                    }
                    Object mergedValue = remappingFunction.apply((Object)oldValue, (Object)value);
                    if (mergedValue == null) {
                        return null;
                    }
                    if (mergedValue == oldValue) {
                        return oldValueFuture;
                    }
                    if (mergedValue == value) {
                        return valueFuture;
                    }
                    return CompletableFuture.completedFuture(mergedValue);
                });
            } while (!merged[0] && mergedValueFuture != newValueFuture);
            return Async.getWhenSuccessful(mergedValueFuture);
        }

        @Override
        public Set<K> keySet() {
            return this.delegate.keySet();
        }

        @Override
        public Collection<V> values() {
            return this.values == null ? (this.values = new Values()) : this.values;
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return this.entries == null ? (this.entries = new EntrySet()) : this.entries;
        }

        private final class EntrySet
        extends AbstractSet<Map.Entry<K, V>> {
            private EntrySet() {
            }

            @Override
            public boolean isEmpty() {
                return AsMapView.this.isEmpty();
            }

            @Override
            public int size() {
                return AsMapView.this.size();
            }

            @Override
            public boolean contains(Object o) {
                if (!(o instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry)o;
                Object value = AsMapView.this.get(entry.getKey());
                return value != null && value.equals(entry.getValue());
            }

            @Override
            public boolean remove(Object obj) {
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry)obj;
                return AsMapView.this.remove(entry.getKey(), entry.getValue());
            }

            @Override
            public void clear() {
                AsMapView.this.clear();
            }

            @Override
            public Iterator<Map.Entry<K, V>> iterator() {
                return new Iterator<Map.Entry<K, V>>(){
                    Iterator<Map.Entry<K, CompletableFuture<V>>> iterator;
                    @Nullable Map.Entry<K, V> cursor;
                    @Nullable K removalKey;
                    {
                        this.iterator = AsMapView.this.delegate.entrySet().iterator();
                    }

                    @Override
                    public boolean hasNext() {
                        while (this.cursor == null && this.iterator.hasNext()) {
                            Map.Entry entry = this.iterator.next();
                            Object value = Async.getIfReady(entry.getValue());
                            if (value == null) continue;
                            this.cursor = new WriteThroughEntry(AsMapView.this, entry.getKey(), value);
                        }
                        return this.cursor != null;
                    }

                    @Override
                    public Map.Entry<K, V> next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        Object key = this.cursor.getKey();
                        Map.Entry entry = this.cursor;
                        this.removalKey = key;
                        this.cursor = null;
                        return entry;
                    }

                    @Override
                    public void remove() {
                        Caffeine.requireState(this.removalKey != null);
                        AsMapView.this.delegate.remove(this.removalKey);
                        this.removalKey = null;
                    }
                };
            }
        }

        private final class Values
        extends AbstractCollection<V> {
            private Values() {
            }

            @Override
            public boolean isEmpty() {
                return AsMapView.this.isEmpty();
            }

            @Override
            public int size() {
                return AsMapView.this.size();
            }

            @Override
            public boolean contains(Object o) {
                return AsMapView.this.containsValue(o);
            }

            @Override
            public void clear() {
                AsMapView.this.clear();
            }

            @Override
            public Iterator<V> iterator() {
                return new Iterator<V>(){
                    Iterator<Map.Entry<K, V>> iterator;
                    {
                        this.iterator = AsMapView.this.entrySet().iterator();
                    }

                    @Override
                    public boolean hasNext() {
                        return this.iterator.hasNext();
                    }

                    @Override
                    public V next() {
                        return this.iterator.next().getValue();
                    }

                    @Override
                    public void remove() {
                        this.iterator.remove();
                    }
                };
            }
        }
    }

    public static abstract class AbstractCacheView<K, V>
    implements Cache<K, V>,
    Serializable {
        transient @Nullable AsMapView<K, V> asMapView;

        abstract LocalAsyncCache<K, V> asyncCache();

        @Override
        public @Nullable V getIfPresent(Object key) {
            CompletableFuture<V> future = this.asyncCache().cache().getIfPresent(key, true);
            return Async.getIfReady(future);
        }

        @Override
        public Map<K, V> getAllPresent(Iterable<?> keys2) {
            LinkedHashSet uniqueKeys = new LinkedHashSet();
            for (Object key : keys2) {
                uniqueKeys.add(key);
            }
            int misses = 0;
            LinkedHashMap result = new LinkedHashMap();
            for (Object key : uniqueKeys) {
                CompletableFuture future = (CompletableFuture)this.asyncCache().cache().get(key);
                Object value = Async.getIfReady(future);
                if (value == null) {
                    ++misses;
                    continue;
                }
                result.put(key, value);
            }
            this.asyncCache().cache().statsCounter().recordMisses(misses);
            this.asyncCache().cache().statsCounter().recordHits(result.size());
            LinkedHashMap castedResult = result;
            return Collections.unmodifiableMap(castedResult);
        }

        @Override
        public V get(K key, Function<? super K, ? extends V> mappingFunction) {
            return AbstractCacheView.resolve(this.asyncCache().get((K)key, mappingFunction));
        }

        @Override
        public Map<K, V> getAll(Iterable<? extends K> keys2, Function<Iterable<? extends K>, Map<K, V>> mappingFunction) {
            return AbstractCacheView.resolve(this.asyncCache().getAll(keys2, mappingFunction));
        }

        protected static <T> T resolve(CompletableFuture<T> future) throws Error {
            try {
                return future.get();
            }
            catch (ExecutionException e) {
                if (e.getCause() instanceof AsyncBulkCompleter.NullMapCompletionException) {
                    throw new NullPointerException(e.getCause().getMessage());
                }
                if (e.getCause() instanceof RuntimeException) {
                    throw (RuntimeException)e.getCause();
                }
                if (e.getCause() instanceof Error) {
                    throw (Error)e.getCause();
                }
                throw new CompletionException(e.getCause());
            }
            catch (InterruptedException e) {
                throw new CompletionException(e);
            }
        }

        @Override
        public void put(K key, V value) {
            Objects.requireNonNull(value);
            this.asyncCache().cache().put(key, CompletableFuture.completedFuture(value));
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> map) {
            map.forEach(this::put);
        }

        @Override
        public void invalidate(Object key) {
            this.asyncCache().cache().remove(key);
        }

        @Override
        public void invalidateAll(Iterable<?> keys2) {
            this.asyncCache().cache().invalidateAll(keys2);
        }

        @Override
        public void invalidateAll() {
            this.asyncCache().cache().clear();
        }

        @Override
        public long estimatedSize() {
            return this.asyncCache().cache().size();
        }

        @Override
        public CacheStats stats() {
            return this.asyncCache().cache().statsCounter().snapshot();
        }

        @Override
        public void cleanUp() {
            this.asyncCache().cache().cleanUp();
        }

        @Override
        public Policy<K, V> policy() {
            return this.asyncCache().policy();
        }

        @Override
        public ConcurrentMap<K, V> asMap() {
            return this.asMapView == null ? (this.asMapView = new AsMapView<K, V>(this.asyncCache().cache())) : this.asMapView;
        }
    }

    public static final class CacheView<K, V>
    extends AbstractCacheView<K, V> {
        private static final long serialVersionUID = 1L;
        final LocalAsyncCache<K, V> asyncCache;

        CacheView(LocalAsyncCache<K, V> asyncCache) {
            this.asyncCache = Objects.requireNonNull(asyncCache);
        }

        @Override
        LocalAsyncCache<K, V> asyncCache() {
            return this.asyncCache;
        }
    }

    public static final class AsyncAsMapView<K, V>
    implements ConcurrentMap<K, CompletableFuture<V>> {
        final LocalAsyncCache<K, V> asyncCache;

        AsyncAsMapView(LocalAsyncCache<K, V> asyncCache) {
            this.asyncCache = Objects.requireNonNull(asyncCache);
        }

        @Override
        public boolean isEmpty() {
            return this.asyncCache.cache().isEmpty();
        }

        @Override
        public int size() {
            return this.asyncCache.cache().size();
        }

        @Override
        public void clear() {
            this.asyncCache.cache().clear();
        }

        @Override
        public boolean containsKey(Object key) {
            return this.asyncCache.cache().containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return this.asyncCache.cache().containsValue(value);
        }

        @Override
        public @Nullable CompletableFuture<V> get(Object key) {
            return (CompletableFuture)this.asyncCache.cache().get(key);
        }

        @Override
        public CompletableFuture<V> putIfAbsent(K key, CompletableFuture<V> value) {
            CompletableFuture<V> prior = this.asyncCache.cache().putIfAbsent(key, value);
            long startTime = this.asyncCache.cache().statsTicker().read();
            if (prior == null) {
                this.asyncCache.handleCompletion(key, value, startTime, false);
            }
            return prior;
        }

        @Override
        public CompletableFuture<V> put(K key, CompletableFuture<V> value) {
            CompletableFuture<V> prior = this.asyncCache.cache().put(key, value);
            long startTime = this.asyncCache.cache().statsTicker().read();
            this.asyncCache.handleCompletion(key, value, startTime, false);
            return prior;
        }

        @Override
        public void putAll(Map<? extends K, ? extends CompletableFuture<V>> map) {
            map.forEach(this::put);
        }

        @Override
        public CompletableFuture<V> replace(K key, CompletableFuture<V> value) {
            CompletableFuture<V> prior = this.asyncCache.cache().replace(key, value);
            long startTime = this.asyncCache.cache().statsTicker().read();
            if (prior != null) {
                this.asyncCache.handleCompletion(key, value, startTime, false);
            }
            return prior;
        }

        @Override
        public boolean replace(K key, CompletableFuture<V> oldValue, CompletableFuture<V> newValue) {
            boolean replaced = this.asyncCache.cache().replace(key, oldValue, newValue);
            long startTime = this.asyncCache.cache().statsTicker().read();
            if (replaced) {
                this.asyncCache.handleCompletion(key, newValue, startTime, false);
            }
            return replaced;
        }

        @Override
        public CompletableFuture<V> remove(Object key) {
            return (CompletableFuture)this.asyncCache.cache().remove(key);
        }

        @Override
        public boolean remove(Object key, Object value) {
            return this.asyncCache.cache().remove(key, value);
        }

        @Override
        public @Nullable CompletableFuture<V> computeIfAbsent(K key, Function<? super K, ? extends CompletableFuture<V>> mappingFunction) {
            Objects.requireNonNull(mappingFunction);
            CompletableFuture[] result = new CompletableFuture[1];
            long startTime = this.asyncCache.cache().statsTicker().read();
            CompletableFuture future = this.asyncCache.cache().computeIfAbsent(key, k -> {
                result[0] = (CompletableFuture)mappingFunction.apply((Object)k);
                return result[0];
            }, false, false);
            if (result[0] == null) {
                if (future != null && this.asyncCache.cache().isRecordingStats()) {
                    future.whenComplete((r, e) -> {
                        if (r != null || e == null) {
                            this.asyncCache.cache().statsCounter().recordHits(1);
                        }
                    });
                }
            } else {
                this.asyncCache.handleCompletion(key, result[0], startTime, true);
            }
            return future;
        }

        @Override
        public CompletableFuture<V> computeIfPresent(K key, BiFunction<? super K, ? super CompletableFuture<V>, ? extends CompletableFuture<V>> remappingFunction) {
            Objects.requireNonNull(remappingFunction);
            CompletableFuture[] result = new CompletableFuture[1];
            long startTime = this.asyncCache.cache().statsTicker().read();
            this.asyncCache.cache().compute(key, (k, oldValue) -> {
                result[0] = oldValue == null ? null : (CompletableFuture)remappingFunction.apply((Object)k, (Object)oldValue);
                return result[0];
            }, false, false, false);
            if (result[0] != null) {
                this.asyncCache.handleCompletion(key, result[0], startTime, false);
            }
            return result[0];
        }

        @Override
        public CompletableFuture<V> compute(K key, BiFunction<? super K, ? super CompletableFuture<V>, ? extends CompletableFuture<V>> remappingFunction) {
            Objects.requireNonNull(remappingFunction);
            CompletableFuture[] result = new CompletableFuture[1];
            long startTime = this.asyncCache.cache().statsTicker().read();
            this.asyncCache.cache().compute(key, (k, oldValue) -> {
                result[0] = (CompletableFuture)remappingFunction.apply((Object)k, (Object)oldValue);
                return result[0];
            }, false, false, false);
            if (result[0] != null) {
                this.asyncCache.handleCompletion(key, result[0], startTime, false);
            }
            return result[0];
        }

        @Override
        public CompletableFuture<V> merge(K key, CompletableFuture<V> value, BiFunction<? super CompletableFuture<V>, ? super CompletableFuture<V>, ? extends CompletableFuture<V>> remappingFunction) {
            Objects.requireNonNull(value);
            Objects.requireNonNull(remappingFunction);
            CompletableFuture[] result = new CompletableFuture[1];
            long startTime = this.asyncCache.cache().statsTicker().read();
            this.asyncCache.cache().compute(key, (k, oldValue) -> {
                result[0] = oldValue == null ? value : (CompletableFuture)remappingFunction.apply((Object)oldValue, (Object)value);
                return result[0];
            }, false, false, false);
            if (result[0] != null) {
                this.asyncCache.handleCompletion(key, result[0], startTime, false);
            }
            return result[0];
        }

        @Override
        public Set<K> keySet() {
            return this.asyncCache.cache().keySet();
        }

        @Override
        public Collection<CompletableFuture<V>> values() {
            return this.asyncCache.cache().values();
        }

        @Override
        public Set<Map.Entry<K, CompletableFuture<V>>> entrySet() {
            return this.asyncCache.cache().entrySet();
        }

        @Override
        public boolean equals(Object o) {
            return this.asyncCache.cache().equals(o);
        }

        @Override
        public int hashCode() {
            return this.asyncCache.cache().hashCode();
        }

        public String toString() {
            return this.asyncCache.cache().toString();
        }
    }

    public static final class AsyncBulkCompleter<K, V>
    implements BiConsumer<Map<K, V>, Throwable> {
        private final LocalCache<K, CompletableFuture<V>> cache;
        private final Map<K, CompletableFuture<V>> proxies;
        private final long startTime;

        AsyncBulkCompleter(LocalCache<K, CompletableFuture<V>> cache, Map<K, CompletableFuture<V>> proxies) {
            this.startTime = cache.statsTicker().read();
            this.proxies = proxies;
            this.cache = cache;
        }

        @Override
        public void accept(@Nullable Map<K, V> result, @Nullable Throwable error) {
            long loadTime = this.cache.statsTicker().read() - this.startTime;
            if (result == null) {
                if (error == null) {
                    error = new NullMapCompletionException();
                }
                for (Map.Entry<K, CompletableFuture<V>> entry : this.proxies.entrySet()) {
                    this.cache.remove(entry.getKey(), entry.getValue());
                    entry.getValue().obtrudeException(error);
                }
                this.cache.statsCounter().recordLoadFailure(loadTime);
                logger.log(Level.WARNING, "Exception thrown during asynchronous load", error);
            } else {
                this.fillProxies(result);
                this.addNewEntries(result);
                this.cache.statsCounter().recordLoadSuccess(loadTime);
            }
        }

        private void fillProxies(Map<K, V> result) {
            this.proxies.forEach((key, future) -> {
                Object value = result.get(key);
                future.obtrudeValue(value);
                if (value == null) {
                    this.cache.remove(key, future);
                } else {
                    this.cache.replace(key, (CompletableFuture<CompletableFuture>)future, (CompletableFuture<CompletableFuture>)future);
                }
            });
        }

        private void addNewEntries(Map<K, V> result) {
            if (this.proxies.size() == result.size()) {
                return;
            }
            result.forEach((key, value) -> {
                if (!this.proxies.containsKey(key)) {
                    this.cache.put(key, CompletableFuture.completedFuture(value));
                }
            });
        }

        static final class NullMapCompletionException
        extends CompletionException {
            private static final long serialVersionUID = 1L;

            public NullMapCompletionException() {
                super("null map", null);
            }
        }
    }
}

