/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.base.Ticker;
import com.google.common.collect.ComputationException;
import com.google.common.collect.ComputingConcurrentHashMap;
import com.google.common.collect.GenericMapMaker;
import com.google.common.collect.ImmutableEntry;
import com.google.common.collect.MapMakerInternalMap;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

@GwtCompatible(emulated=true)
public final class MapMaker
extends GenericMapMaker<Object, Object> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
    private static final int DEFAULT_EXPIRATION_NANOS = 0;
    static final int UNSET_INT = -1;
    boolean useCustomMap;
    int initialCapacity = -1;
    int concurrencyLevel = -1;
    int maximumSize = -1;
    MapMakerInternalMap.Strength keyStrength;
    MapMakerInternalMap.Strength valueStrength;
    long expireAfterWriteNanos = -1L;
    long expireAfterAccessNanos = -1L;
    RemovalCause nullRemovalCause;
    Equivalence<Object> keyEquivalence;
    Ticker ticker;

    @GwtIncompatible(value="To be supported")
    MapMaker keyEquivalence(Equivalence<Object> equivalence) {
        Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", this.keyEquivalence);
        this.keyEquivalence = Preconditions.checkNotNull(equivalence);
        this.useCustomMap = true;
        return this;
    }

    Equivalence<Object> getKeyEquivalence() {
        return Objects.firstNonNull(this.keyEquivalence, this.getKeyStrength().defaultEquivalence());
    }

    public MapMaker initialCapacity(int initialCapacity) {
        Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
        Preconditions.checkArgument(initialCapacity >= 0);
        this.initialCapacity = initialCapacity;
        return this;
    }

    int getInitialCapacity() {
        return this.initialCapacity == -1 ? 16 : this.initialCapacity;
    }

    @Deprecated
    MapMaker maximumSize(int size) {
        Preconditions.checkState(this.maximumSize == -1, "maximum size was already set to %s", this.maximumSize);
        Preconditions.checkArgument(size >= 0, "maximum size must not be negative");
        this.maximumSize = size;
        this.useCustomMap = true;
        if (this.maximumSize == 0) {
            this.nullRemovalCause = RemovalCause.SIZE;
        }
        return this;
    }

    public MapMaker concurrencyLevel(int concurrencyLevel) {
        Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
        Preconditions.checkArgument(concurrencyLevel > 0);
        this.concurrencyLevel = concurrencyLevel;
        return this;
    }

    int getConcurrencyLevel() {
        return this.concurrencyLevel == -1 ? 4 : this.concurrencyLevel;
    }

    @GwtIncompatible(value="java.lang.ref.WeakReference")
    public MapMaker weakKeys() {
        return this.setKeyStrength(MapMakerInternalMap.Strength.WEAK);
    }

    MapMaker setKeyStrength(MapMakerInternalMap.Strength strength) {
        Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", new Object[]{this.keyStrength});
        this.keyStrength = Preconditions.checkNotNull(strength);
        Preconditions.checkArgument(this.keyStrength != MapMakerInternalMap.Strength.SOFT, "Soft keys are not supported");
        if (strength != MapMakerInternalMap.Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }

    MapMakerInternalMap.Strength getKeyStrength() {
        return Objects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
    }

    @GwtIncompatible(value="java.lang.ref.WeakReference")
    public MapMaker weakValues() {
        return this.setValueStrength(MapMakerInternalMap.Strength.WEAK);
    }

    @Deprecated
    @GwtIncompatible(value="java.lang.ref.SoftReference")
    public MapMaker softValues() {
        return this.setValueStrength(MapMakerInternalMap.Strength.SOFT);
    }

    MapMaker setValueStrength(MapMakerInternalMap.Strength strength) {
        Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", new Object[]{this.valueStrength});
        this.valueStrength = Preconditions.checkNotNull(strength);
        if (strength != MapMakerInternalMap.Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }

    MapMakerInternalMap.Strength getValueStrength() {
        return Objects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
    }

    @Deprecated
    MapMaker expireAfterWrite(long duration, TimeUnit unit) {
        this.checkExpiration(duration, unit);
        this.expireAfterWriteNanos = unit.toNanos(duration);
        if (duration == 0L && this.nullRemovalCause == null) {
            this.nullRemovalCause = RemovalCause.EXPIRED;
        }
        this.useCustomMap = true;
        return this;
    }

    private void checkExpiration(long duration, TimeUnit unit) {
        Preconditions.checkState(this.expireAfterWriteNanos == -1L, "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
        Preconditions.checkState(this.expireAfterAccessNanos == -1L, "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
        Preconditions.checkArgument(duration >= 0L, "duration cannot be negative: %s %s", new Object[]{duration, unit});
    }

    long getExpireAfterWriteNanos() {
        return this.expireAfterWriteNanos == -1L ? 0L : this.expireAfterWriteNanos;
    }

    @Deprecated
    @GwtIncompatible(value="To be supported")
    MapMaker expireAfterAccess(long duration, TimeUnit unit) {
        this.checkExpiration(duration, unit);
        this.expireAfterAccessNanos = unit.toNanos(duration);
        if (duration == 0L && this.nullRemovalCause == null) {
            this.nullRemovalCause = RemovalCause.EXPIRED;
        }
        this.useCustomMap = true;
        return this;
    }

    long getExpireAfterAccessNanos() {
        return this.expireAfterAccessNanos == -1L ? 0L : this.expireAfterAccessNanos;
    }

    Ticker getTicker() {
        return Objects.firstNonNull(this.ticker, Ticker.systemTicker());
    }

    @Deprecated
    @GwtIncompatible(value="To be supported")
    <K, V> GenericMapMaker<K, V> removalListener(RemovalListener<K, V> listener) {
        Preconditions.checkState(this.removalListener == null);
        MapMaker me = this;
        me.removalListener = Preconditions.checkNotNull(listener);
        this.useCustomMap = true;
        return me;
    }

    @Override
    public <K, V> ConcurrentMap<K, V> makeMap() {
        if (!this.useCustomMap) {
            return new ConcurrentHashMap(this.getInitialCapacity(), 0.75f, this.getConcurrencyLevel());
        }
        return (ConcurrentMap)((Object)(this.nullRemovalCause == null ? new MapMakerInternalMap(this) : new NullConcurrentMap(this)));
    }

    @Override
    @GwtIncompatible(value="MapMakerInternalMap")
    <K, V> MapMakerInternalMap<K, V> makeCustomMap() {
        return new MapMakerInternalMap(this);
    }

    @Override
    @Deprecated
    <K, V> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> computingFunction) {
        return (ConcurrentMap)((Object)(this.nullRemovalCause == null ? new ComputingMapAdapter<K, V>(this, computingFunction) : new NullComputingConcurrentMap<K, V>(this, computingFunction)));
    }

    public String toString() {
        Objects.ToStringHelper s2 = Objects.toStringHelper(this);
        if (this.initialCapacity != -1) {
            s2.add("initialCapacity", this.initialCapacity);
        }
        if (this.concurrencyLevel != -1) {
            s2.add("concurrencyLevel", this.concurrencyLevel);
        }
        if (this.maximumSize != -1) {
            s2.add("maximumSize", this.maximumSize);
        }
        if (this.expireAfterWriteNanos != -1L) {
            s2.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
        }
        if (this.expireAfterAccessNanos != -1L) {
            s2.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
        }
        if (this.keyStrength != null) {
            s2.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
        }
        if (this.valueStrength != null) {
            s2.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
        }
        if (this.keyEquivalence != null) {
            s2.addValue("keyEquivalence");
        }
        if (this.removalListener != null) {
            s2.addValue("removalListener");
        }
        return s2.toString();
    }

    static final class ComputingMapAdapter<K, V>
    extends ComputingConcurrentHashMap<K, V>
    implements Serializable {
        private static final long serialVersionUID = 0L;

        ComputingMapAdapter(MapMaker mapMaker, Function<? super K, ? extends V> computingFunction) {
            super(mapMaker, computingFunction);
        }

        @Override
        public V get(Object key) {
            Object value;
            try {
                value = this.getOrCompute(key);
            }
            catch (ExecutionException e2) {
                Throwable cause = e2.getCause();
                Throwables.propagateIfInstanceOf(cause, ComputationException.class);
                throw new ComputationException(cause);
            }
            if (value == null) {
                throw new NullPointerException(this.computingFunction + " returned null for key " + key + ".");
            }
            return value;
        }
    }

    static final class NullComputingConcurrentMap<K, V>
    extends NullConcurrentMap<K, V> {
        private static final long serialVersionUID = 0L;
        final Function<? super K, ? extends V> computingFunction;

        NullComputingConcurrentMap(MapMaker mapMaker, Function<? super K, ? extends V> computingFunction) {
            super(mapMaker);
            this.computingFunction = Preconditions.checkNotNull(computingFunction);
        }

        @Override
        public V get(Object k2) {
            Object key = k2;
            V value = this.compute(key);
            Preconditions.checkNotNull(value, "%s returned null for key %s.", this.computingFunction, key);
            this.notifyRemoval(key, value);
            return value;
        }

        private V compute(K key) {
            Preconditions.checkNotNull(key);
            try {
                return this.computingFunction.apply(key);
            }
            catch (ComputationException e2) {
                throw e2;
            }
            catch (Throwable t2) {
                throw new ComputationException(t2);
            }
        }
    }

    static class NullConcurrentMap<K, V>
    extends AbstractMap<K, V>
    implements ConcurrentMap<K, V>,
    Serializable {
        private static final long serialVersionUID = 0L;
        private final RemovalListener<K, V> removalListener;
        private final RemovalCause removalCause;

        NullConcurrentMap(MapMaker mapMaker) {
            this.removalListener = mapMaker.getRemovalListener();
            this.removalCause = mapMaker.nullRemovalCause;
        }

        @Override
        public boolean containsKey(@Nullable Object key) {
            return false;
        }

        @Override
        public boolean containsValue(@Nullable Object value) {
            return false;
        }

        @Override
        public V get(@Nullable Object key) {
            return null;
        }

        void notifyRemoval(K key, V value) {
            RemovalNotification<K, V> notification = new RemovalNotification<K, V>(key, value, this.removalCause);
            this.removalListener.onRemoval(notification);
        }

        @Override
        public V put(K key, V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            this.notifyRemoval(key, value);
            return null;
        }

        @Override
        public V putIfAbsent(K key, V value) {
            return this.put(key, value);
        }

        @Override
        public V remove(@Nullable Object key) {
            return null;
        }

        @Override
        public boolean remove(@Nullable Object key, @Nullable Object value) {
            return false;
        }

        @Override
        public V replace(K key, V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            return null;
        }

        @Override
        public boolean replace(K key, @Nullable V oldValue, V newValue) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(newValue);
            return false;
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return Collections.emptySet();
        }
    }

    static enum RemovalCause {
        EXPLICIT{

            @Override
            boolean wasEvicted() {
                return false;
            }
        }
        ,
        REPLACED{

            @Override
            boolean wasEvicted() {
                return false;
            }
        }
        ,
        COLLECTED{

            @Override
            boolean wasEvicted() {
                return true;
            }
        }
        ,
        EXPIRED{

            @Override
            boolean wasEvicted() {
                return true;
            }
        }
        ,
        SIZE{

            @Override
            boolean wasEvicted() {
                return true;
            }
        };


        abstract boolean wasEvicted();
    }

    static final class RemovalNotification<K, V>
    extends ImmutableEntry<K, V> {
        private static final long serialVersionUID = 0L;
        private final RemovalCause cause;

        RemovalNotification(@Nullable K key, @Nullable V value, RemovalCause cause) {
            super(key, value);
            this.cause = cause;
        }

        public RemovalCause getCause() {
            return this.cause;
        }

        public boolean wasEvicted() {
            return this.cause.wasEvicted();
        }
    }

    static interface RemovalListener<K, V> {
        public void onRemoval(RemovalNotification<K, V> var1);
    }
}

