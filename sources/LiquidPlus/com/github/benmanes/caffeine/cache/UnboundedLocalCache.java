/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.Async;
import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LocalAsyncCache;
import com.github.benmanes.caffeine.cache.LocalAsyncLoadingCache;
import com.github.benmanes.caffeine.cache.LocalCache;
import com.github.benmanes.caffeine.cache.LocalLoadingCache;
import com.github.benmanes.caffeine.cache.LocalManualCache;
import com.github.benmanes.caffeine.cache.Policy;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.SerializationProxy;
import com.github.benmanes.caffeine.cache.Ticker;
import com.github.benmanes.caffeine.cache.WriteThroughEntry;
import com.github.benmanes.caffeine.cache.stats.StatsCounter;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.Nullable;

final class UnboundedLocalCache<K, V>
implements LocalCache<K, V> {
    final @Nullable RemovalListener<K, V> removalListener;
    final ConcurrentHashMap<K, V> data;
    final StatsCounter statsCounter;
    final boolean isRecordingStats;
    final CacheWriter<K, V> writer;
    final Executor executor;
    final Ticker ticker;
    transient @Nullable Set<K> keySet;
    transient @Nullable Collection<V> values;
    transient @Nullable Set<Map.Entry<K, V>> entrySet;

    UnboundedLocalCache(Caffeine<? super K, ? super V> builder, boolean async) {
        this.data = new ConcurrentHashMap(builder.getInitialCapacity());
        this.statsCounter = builder.getStatsCounterSupplier().get();
        this.removalListener = builder.getRemovalListener(async);
        this.isRecordingStats = builder.isRecordingStats();
        this.writer = builder.getCacheWriter(async);
        this.executor = builder.getExecutor();
        this.ticker = builder.getTicker();
    }

    @Override
    public boolean hasWriteTime() {
        return false;
    }

    @Override
    public @Nullable V getIfPresent(Object key, boolean recordStats) {
        V value = this.data.get(key);
        if (recordStats) {
            if (value == null) {
                this.statsCounter.recordMisses(1);
            } else {
                this.statsCounter.recordHits(1);
            }
        }
        return value;
    }

    @Override
    public @Nullable V getIfPresentQuietly(Object key, long[] writeTime) {
        return this.data.get(key);
    }

    @Override
    public long estimatedSize() {
        return this.data.mappingCount();
    }

    @Override
    public Map<K, V> getAllPresent(Iterable<?> keys2) {
        LinkedHashSet uniqueKeys = new LinkedHashSet();
        for (Object key : keys2) {
            uniqueKeys.add(key);
        }
        int misses = 0;
        LinkedHashMap result = new LinkedHashMap(uniqueKeys.size());
        for (Object key : uniqueKeys) {
            V value = this.data.get(key);
            if (value == null) {
                ++misses;
                continue;
            }
            result.put(key, value);
        }
        this.statsCounter.recordMisses(misses);
        this.statsCounter.recordHits(result.size());
        LinkedHashMap castedResult = result;
        return Collections.unmodifiableMap(castedResult);
    }

    @Override
    public void cleanUp() {
    }

    @Override
    public StatsCounter statsCounter() {
        return this.statsCounter;
    }

    @Override
    public boolean hasRemovalListener() {
        return this.removalListener != null;
    }

    @Override
    public RemovalListener<K, V> removalListener() {
        return this.removalListener;
    }

    @Override
    public void notifyRemoval(@Nullable K key, @Nullable V value, RemovalCause cause) {
        Objects.requireNonNull(this.removalListener(), "Notification should be guarded with a check");
        this.executor.execute(() -> this.removalListener().onRemoval(key, value, cause));
    }

    @Override
    public boolean isRecordingStats() {
        return this.isRecordingStats;
    }

    @Override
    public Executor executor() {
        return this.executor;
    }

    @Override
    public Ticker expirationTicker() {
        return Ticker.disabledTicker();
    }

    @Override
    public Ticker statsTicker() {
        return this.ticker;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        this.data.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        Object[] notificationKey = new Object[1];
        Object[] notificationValue = new Object[1];
        this.data.replaceAll((key, value) -> {
            Object newValue;
            if (notificationKey[0] != null) {
                this.notifyRemoval(notificationKey[0], notificationValue[0], RemovalCause.REPLACED);
                notificationValue[0] = null;
                notificationKey[0] = null;
            }
            if ((newValue = Objects.requireNonNull(function.apply((K)key, (V)value))) != value) {
                this.writer.write(key, newValue);
            }
            if (this.hasRemovalListener() && newValue != value) {
                notificationKey[0] = key;
                notificationValue[0] = value;
            }
            return newValue;
        });
        if (notificationKey[0] != null) {
            this.notifyRemoval(notificationKey[0], notificationValue[0], RemovalCause.REPLACED);
        }
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction, boolean recordStats, boolean recordLoad) {
        Objects.requireNonNull(mappingFunction);
        Object value = this.data.get(key);
        if (value != null) {
            if (recordStats) {
                this.statsCounter.recordHits(1);
            }
            return value;
        }
        boolean[] missed = new boolean[1];
        value = this.data.computeIfAbsent(key, k -> {
            missed[0] = true;
            return recordStats ? this.statsAware(mappingFunction, recordLoad).apply(key) : mappingFunction.apply((K)key);
        });
        if (!missed[0] && recordStats) {
            this.statsCounter.recordHits(1);
        }
        return value;
    }

    @Override
    public @Nullable V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        if (!this.data.containsKey(key)) {
            return null;
        }
        Object[] oldValue = new Object[1];
        RemovalCause[] cause = new RemovalCause[1];
        Object nv = this.data.computeIfPresent(key, (k, value) -> {
            BiFunction function = this.statsAware(remappingFunction, false, true, true);
            Object newValue = function.apply(k, value);
            RemovalCause removalCause = cause[0] = newValue == null ? RemovalCause.EXPLICIT : RemovalCause.REPLACED;
            if (this.hasRemovalListener() && newValue != value) {
                oldValue[0] = value;
            }
            return newValue;
        });
        if (oldValue[0] != null) {
            this.notifyRemoval(key, oldValue[0], cause[0]);
        }
        return (V)nv;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction, boolean recordMiss, boolean recordLoad, boolean recordLoadFailure) {
        Objects.requireNonNull(remappingFunction);
        return this.remap(key, this.statsAware(remappingFunction, recordMiss, recordLoad, recordLoadFailure));
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        return (V)this.remap(key, (k, oldValue) -> oldValue == null ? value : this.statsAware(remappingFunction).apply(oldValue, value));
    }

    V remap(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Object[] oldValue = new Object[1];
        RemovalCause[] cause = new RemovalCause[1];
        Object nv = this.data.compute(key, (k, value) -> {
            Object newValue = remappingFunction.apply((K)k, (V)value);
            if (value == null && newValue == null) {
                return null;
            }
            RemovalCause removalCause = cause[0] = newValue == null ? RemovalCause.EXPLICIT : RemovalCause.REPLACED;
            if (this.hasRemovalListener() && value != null && newValue != value) {
                oldValue[0] = value;
            }
            return newValue;
        });
        if (oldValue[0] != null) {
            this.notifyRemoval(key, oldValue[0], cause[0]);
        }
        return (V)nv;
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public void clear() {
        if (!this.hasRemovalListener() && this.writer == CacheWriter.disabledWriter()) {
            this.data.clear();
            return;
        }
        for (Object key : this.data.keySet()) {
            this.remove(key);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        return this.data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.data.containsValue(value);
    }

    @Override
    public @Nullable V get(Object key) {
        return this.getIfPresent(key, false);
    }

    @Override
    public @Nullable V put(K key, V value) {
        return this.put(key, value, true);
    }

    @Override
    public @Nullable V put(K key, V value, boolean notifyWriter) {
        Objects.requireNonNull(value);
        Object[] oldValue = new Object[1];
        if (this.writer == CacheWriter.disabledWriter() || !notifyWriter) {
            oldValue[0] = this.data.put(key, value);
        } else {
            this.data.compute(key, (k, v) -> {
                if (value != v) {
                    this.writer.write(key, value);
                }
                oldValue[0] = v;
                return value;
            });
        }
        if (this.hasRemovalListener() && oldValue[0] != null && oldValue[0] != value) {
            this.notifyRemoval(key, oldValue[0], RemovalCause.REPLACED);
        }
        return (V)oldValue[0];
    }

    @Override
    public @Nullable V putIfAbsent(K key, V value) {
        Objects.requireNonNull(value);
        boolean[] wasAbsent = new boolean[1];
        Object val = this.data.computeIfAbsent(key, k -> {
            this.writer.write(key, value);
            wasAbsent[0] = true;
            return value;
        });
        return (V)(wasAbsent[0] ? null : val);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        if (!this.hasRemovalListener() && this.writer == CacheWriter.disabledWriter()) {
            this.data.putAll(map);
            return;
        }
        map.forEach(this::put);
    }

    @Override
    public @Nullable V remove(Object key) {
        Object castKey = key;
        Object[] oldValue = new Object[1];
        if (this.writer == CacheWriter.disabledWriter()) {
            oldValue[0] = this.data.remove(key);
        } else {
            this.data.computeIfPresent(castKey, (k, v) -> {
                this.writer.delete(castKey, v, RemovalCause.EXPLICIT);
                oldValue[0] = v;
                return null;
            });
        }
        if (this.hasRemovalListener() && oldValue[0] != null) {
            this.notifyRemoval(castKey, oldValue[0], RemovalCause.EXPLICIT);
        }
        return (V)oldValue[0];
    }

    @Override
    public boolean remove(Object key, Object value) {
        boolean removed;
        if (value == null) {
            Objects.requireNonNull(key);
            return false;
        }
        Object castKey = key;
        Object[] oldValue = new Object[1];
        this.data.computeIfPresent(castKey, (k, v) -> {
            if (v.equals(value)) {
                this.writer.delete(castKey, v, RemovalCause.EXPLICIT);
                oldValue[0] = v;
                return null;
            }
            return v;
        });
        boolean bl = removed = oldValue[0] != null;
        if (this.hasRemovalListener() && removed) {
            this.notifyRemoval(castKey, oldValue[0], RemovalCause.EXPLICIT);
        }
        return removed;
    }

    @Override
    public @Nullable V replace(K key, V value) {
        Objects.requireNonNull(value);
        Object[] oldValue = new Object[1];
        this.data.computeIfPresent(key, (k, v) -> {
            if (value != v) {
                this.writer.write(key, value);
            }
            oldValue[0] = v;
            return value;
        });
        if (this.hasRemovalListener() && oldValue[0] != null && oldValue[0] != value) {
            this.notifyRemoval(key, value, RemovalCause.REPLACED);
        }
        return (V)oldValue[0];
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        boolean replaced;
        Objects.requireNonNull(oldValue);
        Objects.requireNonNull(newValue);
        Object[] prev = new Object[1];
        this.data.computeIfPresent(key, (k, v) -> {
            if (v.equals(oldValue)) {
                if (newValue != v) {
                    this.writer.write(key, newValue);
                }
                prev[0] = v;
                return newValue;
            }
            return v;
        });
        boolean bl = replaced = prev[0] != null;
        if (this.hasRemovalListener() && replaced && prev[0] != newValue) {
            this.notifyRemoval(key, prev[0], RemovalCause.REPLACED);
        }
        return replaced;
    }

    @Override
    public boolean equals(Object o) {
        return this.data.equals(o);
    }

    @Override
    public int hashCode() {
        return this.data.hashCode();
    }

    public String toString() {
        return this.data.toString();
    }

    @Override
    public Set<K> keySet() {
        Set<K> ks = this.keySet;
        return ks == null ? (this.keySet = new KeySetView(this)) : ks;
    }

    @Override
    public Collection<V> values() {
        Collection<V> vs = this.values;
        return vs == null ? (this.values = new ValuesView(this)) : vs;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = this.entrySet;
        return es == null ? (this.entrySet = new EntrySetView(this)) : es;
    }

    static final class UnboundedLocalAsyncLoadingCache<K, V>
    extends LocalAsyncLoadingCache<K, V>
    implements Serializable {
        private static final long serialVersionUID = 1L;
        final UnboundedLocalCache<K, CompletableFuture<V>> cache;
        @Nullable ConcurrentMap<K, CompletableFuture<V>> mapView;
        @Nullable Policy<K, V> policy;

        UnboundedLocalAsyncLoadingCache(Caffeine<K, V> builder, AsyncCacheLoader<? super K, V> loader) {
            super(loader);
            this.cache = new UnboundedLocalCache<K, V>(builder, true);
        }

        @Override
        public LocalCache<K, CompletableFuture<V>> cache() {
            return this.cache;
        }

        @Override
        public ConcurrentMap<K, CompletableFuture<V>> asMap() {
            return this.mapView == null ? (this.mapView = new LocalAsyncCache.AsyncAsMapView(this)) : this.mapView;
        }

        @Override
        public Policy<K, V> policy() {
            Function<CompletableFuture, Object> transformer;
            UnboundedLocalCache<K, CompletableFuture<V>> castCache = this.cache;
            Function<CompletableFuture, Object> castTransformer = transformer = Async::getIfReady;
            return this.policy == null ? (this.policy = new UnboundedPolicy<K, Object>(castCache, castTransformer)) : this.policy;
        }

        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        Object writeReplace() {
            SerializationProxy proxy = new SerializationProxy();
            proxy.isRecordingStats = this.cache.isRecordingStats();
            proxy.removalListener = this.cache.removalListener();
            proxy.ticker = this.cache.ticker;
            proxy.writer = this.cache.writer;
            proxy.loader = this.loader;
            proxy.async = true;
            return proxy;
        }
    }

    static final class UnboundedLocalAsyncCache<K, V>
    implements LocalAsyncCache<K, V>,
    Serializable {
        private static final long serialVersionUID = 1L;
        final UnboundedLocalCache<K, CompletableFuture<V>> cache;
        @Nullable ConcurrentMap<K, CompletableFuture<V>> mapView;
        @Nullable LocalAsyncCache.CacheView<K, V> cacheView;
        @Nullable Policy<K, V> policy;

        UnboundedLocalAsyncCache(Caffeine<K, V> builder) {
            this.cache = new UnboundedLocalCache<K, V>(builder, true);
        }

        @Override
        public UnboundedLocalCache<K, CompletableFuture<V>> cache() {
            return this.cache;
        }

        @Override
        public ConcurrentMap<K, CompletableFuture<V>> asMap() {
            return this.mapView == null ? (this.mapView = new LocalAsyncCache.AsyncAsMapView(this)) : this.mapView;
        }

        @Override
        public Cache<K, V> synchronous() {
            return this.cacheView == null ? (this.cacheView = new LocalAsyncCache.CacheView(this)) : this.cacheView;
        }

        @Override
        public Policy<K, V> policy() {
            Function<CompletableFuture, Object> transformer;
            UnboundedLocalCache<K, CompletableFuture<V>> castCache = this.cache;
            Function<CompletableFuture, Object> castTransformer = transformer = Async::getIfReady;
            return this.policy == null ? (this.policy = new UnboundedPolicy<K, Object>(castCache, castTransformer)) : this.policy;
        }

        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        Object writeReplace() {
            SerializationProxy proxy = new SerializationProxy();
            proxy.isRecordingStats = this.cache.isRecordingStats;
            proxy.removalListener = this.cache.removalListener;
            proxy.ticker = this.cache.ticker;
            proxy.writer = this.cache.writer;
            proxy.async = true;
            return proxy;
        }
    }

    static final class UnboundedLocalLoadingCache<K, V>
    extends UnboundedLocalManualCache<K, V>
    implements LocalLoadingCache<K, V> {
        private static final long serialVersionUID = 1L;
        final Function<K, V> mappingFunction;
        final CacheLoader<? super K, V> loader;
        final @Nullable Function<Iterable<? extends K>, Map<K, V>> bulkMappingFunction;

        UnboundedLocalLoadingCache(Caffeine<K, V> builder, CacheLoader<? super K, V> loader) {
            super(builder);
            this.loader = loader;
            this.mappingFunction = LocalLoadingCache.newMappingFunction(loader);
            this.bulkMappingFunction = LocalLoadingCache.newBulkMappingFunction(loader);
        }

        @Override
        public CacheLoader<? super K, V> cacheLoader() {
            return this.loader;
        }

        @Override
        public Function<K, V> mappingFunction() {
            return this.mappingFunction;
        }

        @Override
        public @Nullable Function<Iterable<? extends K>, Map<K, V>> bulkMappingFunction() {
            return this.bulkMappingFunction;
        }

        @Override
        Object writeReplace() {
            SerializationProxy proxy = (SerializationProxy)super.writeReplace();
            proxy.loader = this.loader;
            return proxy;
        }

        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }
    }

    static final class UnboundedPolicy<K, V>
    implements Policy<K, V> {
        final UnboundedLocalCache<K, V> cache;
        final Function<V, V> transformer;

        UnboundedPolicy(UnboundedLocalCache<K, V> cache, Function<V, V> transformer) {
            this.transformer = transformer;
            this.cache = cache;
        }

        @Override
        public boolean isRecordingStats() {
            return this.cache.isRecordingStats;
        }

        @Override
        public V getIfPresentQuietly(Object key) {
            return this.transformer.apply(this.cache.data.get(key));
        }

        @Override
        public Optional<Policy.Eviction<K, V>> eviction() {
            return Optional.empty();
        }

        @Override
        public Optional<Policy.Expiration<K, V>> expireAfterAccess() {
            return Optional.empty();
        }

        @Override
        public Optional<Policy.Expiration<K, V>> expireAfterWrite() {
            return Optional.empty();
        }

        @Override
        public Optional<Policy.Expiration<K, V>> refreshAfterWrite() {
            return Optional.empty();
        }
    }

    static class UnboundedLocalManualCache<K, V>
    implements LocalManualCache<K, V>,
    Serializable {
        private static final long serialVersionUID = 1L;
        final UnboundedLocalCache<K, V> cache;
        @Nullable Policy<K, V> policy;

        UnboundedLocalManualCache(Caffeine<K, V> builder) {
            this.cache = new UnboundedLocalCache<K, V>(builder, false);
        }

        @Override
        public UnboundedLocalCache<K, V> cache() {
            return this.cache;
        }

        @Override
        public Policy<K, V> policy() {
            return this.policy == null ? (this.policy = new UnboundedPolicy<K, V>(this.cache, Function.identity())) : this.policy;
        }

        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        Object writeReplace() {
            SerializationProxy proxy = new SerializationProxy();
            proxy.isRecordingStats = this.cache.isRecordingStats;
            proxy.removalListener = this.cache.removalListener;
            proxy.ticker = this.cache.ticker;
            proxy.writer = this.cache.writer;
            return proxy;
        }
    }

    static final class EntrySpliterator<K, V>
    implements Spliterator<Map.Entry<K, V>> {
        final Spliterator<Map.Entry<K, V>> spliterator;
        final UnboundedLocalCache<K, V> cache;

        EntrySpliterator(UnboundedLocalCache<K, V> cache) {
            this(cache, cache.data.entrySet().spliterator());
        }

        EntrySpliterator(UnboundedLocalCache<K, V> cache, Spliterator<Map.Entry<K, V>> spliterator) {
            this.spliterator = Objects.requireNonNull(spliterator);
            this.cache = Objects.requireNonNull(cache);
        }

        @Override
        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> action) {
            Objects.requireNonNull(action);
            this.spliterator.forEachRemaining((? super T entry) -> {
                WriteThroughEntry<K, V> e = new WriteThroughEntry<K, V>(this.cache, entry.getKey(), entry.getValue());
                action.accept(e);
            });
        }

        @Override
        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> action) {
            Objects.requireNonNull(action);
            return this.spliterator.tryAdvance((? super T entry) -> {
                WriteThroughEntry<K, V> e = new WriteThroughEntry<K, V>(this.cache, entry.getKey(), entry.getValue());
                action.accept(e);
            });
        }

        public @Nullable EntrySpliterator<K, V> trySplit() {
            Spliterator<Map.Entry<K, V>> split = this.spliterator.trySplit();
            return split == null ? null : new EntrySpliterator<K, V>(this.cache, split);
        }

        @Override
        public long estimateSize() {
            return this.spliterator.estimateSize();
        }

        @Override
        public int characteristics() {
            return this.spliterator.characteristics();
        }
    }

    static final class EntryIterator<K, V>
    implements Iterator<Map.Entry<K, V>> {
        final UnboundedLocalCache<K, V> cache;
        final Iterator<Map.Entry<K, V>> iterator;
        @Nullable Map.Entry<K, V> entry;

        EntryIterator(UnboundedLocalCache<K, V> cache) {
            this.cache = Objects.requireNonNull(cache);
            this.iterator = cache.data.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public Map.Entry<K, V> next() {
            this.entry = this.iterator.next();
            return new WriteThroughEntry<K, V>(this.cache, this.entry.getKey(), this.entry.getValue());
        }

        @Override
        public void remove() {
            if (this.entry == null) {
                throw new IllegalStateException();
            }
            this.cache.remove(this.entry.getKey());
            this.entry = null;
        }
    }

    static final class EntrySetView<K, V>
    extends AbstractSet<Map.Entry<K, V>> {
        final UnboundedLocalCache<K, V> cache;

        EntrySetView(UnboundedLocalCache<K, V> cache) {
            this.cache = Objects.requireNonNull(cache);
        }

        @Override
        public boolean isEmpty() {
            return this.cache.isEmpty();
        }

        @Override
        public int size() {
            return this.cache.size();
        }

        @Override
        public void clear() {
            this.cache.clear();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry)o;
            V value = this.cache.get(entry.getKey());
            return value != null && value.equals(entry.getValue());
        }

        @Override
        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry)obj;
            return this.cache.remove(entry.getKey(), entry.getValue());
        }

        @Override
        public boolean removeIf(Predicate<? super Map.Entry<K, V>> filter) {
            Objects.requireNonNull(filter);
            boolean removed = false;
            for (Map.Entry entry : this.cache.data.entrySet()) {
                if (!filter.test(entry)) continue;
                removed |= this.cache.remove(entry.getKey(), entry.getValue());
            }
            return removed;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator<K, V>(this.cache);
        }

        @Override
        public Spliterator<Map.Entry<K, V>> spliterator() {
            return new EntrySpliterator<K, V>(this.cache);
        }
    }

    static final class ValuesIterator<K, V>
    implements Iterator<V> {
        final UnboundedLocalCache<K, V> cache;
        final Iterator<Map.Entry<K, V>> iterator;
        @Nullable Map.Entry<K, V> entry;

        ValuesIterator(UnboundedLocalCache<K, V> cache) {
            this.cache = Objects.requireNonNull(cache);
            this.iterator = cache.data.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public V next() {
            this.entry = this.iterator.next();
            return this.entry.getValue();
        }

        @Override
        public void remove() {
            if (this.entry == null) {
                throw new IllegalStateException();
            }
            this.cache.remove(this.entry.getKey());
            this.entry = null;
        }
    }

    static final class ValuesView<K, V>
    extends AbstractCollection<V> {
        final UnboundedLocalCache<K, V> cache;

        ValuesView(UnboundedLocalCache<K, V> cache) {
            this.cache = Objects.requireNonNull(cache);
        }

        @Override
        public boolean isEmpty() {
            return this.cache.isEmpty();
        }

        @Override
        public int size() {
            return this.cache.size();
        }

        @Override
        public void clear() {
            this.cache.clear();
        }

        @Override
        public boolean contains(Object o) {
            return this.cache.containsValue(o);
        }

        @Override
        public boolean removeIf(Predicate<? super V> filter) {
            Objects.requireNonNull(filter);
            boolean removed = false;
            for (Map.Entry entry : this.cache.data.entrySet()) {
                if (!filter.test(entry.getValue())) continue;
                removed |= this.cache.remove(entry.getKey(), entry.getValue());
            }
            return removed;
        }

        @Override
        public Iterator<V> iterator() {
            return new ValuesIterator<K, V>(this.cache);
        }

        @Override
        public Spliterator<V> spliterator() {
            return this.cache.data.values().spliterator();
        }
    }

    static final class KeyIterator<K>
    implements Iterator<K> {
        final UnboundedLocalCache<K, ?> cache;
        final Iterator<K> iterator;
        @Nullable K current;

        KeyIterator(UnboundedLocalCache<K, ?> cache) {
            this.cache = Objects.requireNonNull(cache);
            this.iterator = ((ConcurrentHashMap.KeySetView)cache.data.keySet()).iterator();
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public K next() {
            this.current = this.iterator.next();
            return this.current;
        }

        @Override
        public void remove() {
            if (this.current == null) {
                throw new IllegalStateException();
            }
            this.cache.remove(this.current);
            this.current = null;
        }
    }

    static final class KeySetView<K>
    extends AbstractSet<K> {
        final UnboundedLocalCache<K, ?> cache;

        KeySetView(UnboundedLocalCache<K, ?> cache) {
            this.cache = Objects.requireNonNull(cache);
        }

        @Override
        public boolean isEmpty() {
            return this.cache.isEmpty();
        }

        @Override
        public int size() {
            return this.cache.size();
        }

        @Override
        public void clear() {
            this.cache.clear();
        }

        @Override
        public boolean contains(Object o) {
            return this.cache.containsKey(o);
        }

        @Override
        public boolean remove(Object obj) {
            return this.cache.remove(obj) != null;
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator<K>(this.cache);
        }

        @Override
        public Spliterator<K> spliterator() {
            return ((ConcurrentHashMap.KeySetView)this.cache.data.keySet()).spliterator();
        }
    }
}

