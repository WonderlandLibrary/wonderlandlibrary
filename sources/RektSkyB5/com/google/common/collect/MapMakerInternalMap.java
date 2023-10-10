/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.annotation.concurrent.GuardedBy
 */
package com.google.common.collect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import com.google.common.collect.AbstractMapEntry;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ForwardingConcurrentMap;
import com.google.common.collect.GenericMapMaker;
import com.google.common.collect.Iterators;
import com.google.common.collect.MapMaker;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

class MapMakerInternalMap<K, V>
extends AbstractMap<K, V>
implements ConcurrentMap<K, V>,
Serializable {
    static final int MAXIMUM_CAPACITY = 0x40000000;
    static final int MAX_SEGMENTS = 65536;
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final int DRAIN_THRESHOLD = 63;
    static final int DRAIN_MAX = 16;
    static final long CLEANUP_EXECUTOR_DELAY_SECS = 60L;
    private static final Logger logger = Logger.getLogger(MapMakerInternalMap.class.getName());
    final transient int segmentMask;
    final transient int segmentShift;
    final transient Segment<K, V>[] segments;
    final int concurrencyLevel;
    final Equivalence<Object> keyEquivalence;
    final Equivalence<Object> valueEquivalence;
    final Strength keyStrength;
    final Strength valueStrength;
    final int maximumSize;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final Queue<MapMaker.RemovalNotification<K, V>> removalNotificationQueue;
    final MapMaker.RemovalListener<K, V> removalListener;
    final transient EntryFactory entryFactory;
    final Ticker ticker;
    static final ValueReference<Object, Object> UNSET = new ValueReference<Object, Object>(){

        @Override
        public Object get() {
            return null;
        }

        @Override
        public ReferenceEntry<Object, Object> getEntry() {
            return null;
        }

        @Override
        public ValueReference<Object, Object> copyFor(ReferenceQueue<Object> queue, @Nullable Object value, ReferenceEntry<Object, Object> entry) {
            return this;
        }

        @Override
        public boolean isComputingReference() {
            return false;
        }

        @Override
        public Object waitForValue() {
            return null;
        }

        @Override
        public void clear(ValueReference<Object, Object> newValue) {
        }
    };
    static final Queue<? extends Object> DISCARDING_QUEUE = new AbstractQueue<Object>(){

        @Override
        public boolean offer(Object o2) {
            return true;
        }

        @Override
        public Object peek() {
            return null;
        }

        @Override
        public Object poll() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Iterator<Object> iterator() {
            return Iterators.emptyIterator();
        }
    };
    transient Set<K> keySet;
    transient Collection<V> values;
    transient Set<Map.Entry<K, V>> entrySet;
    private static final long serialVersionUID = 5L;

    MapMakerInternalMap(MapMaker builder) {
        int segmentSize;
        int segmentCount;
        this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
        this.keyStrength = builder.getKeyStrength();
        this.valueStrength = builder.getValueStrength();
        this.keyEquivalence = builder.getKeyEquivalence();
        this.valueEquivalence = this.valueStrength.defaultEquivalence();
        this.maximumSize = builder.maximumSize;
        this.expireAfterAccessNanos = builder.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = builder.getExpireAfterWriteNanos();
        this.entryFactory = EntryFactory.getFactory(this.keyStrength, this.expires(), this.evictsBySize());
        this.ticker = builder.getTicker();
        this.removalListener = builder.getRemovalListener();
        this.removalNotificationQueue = this.removalListener == GenericMapMaker.NullListener.INSTANCE ? MapMakerInternalMap.discardingQueue() : new ConcurrentLinkedQueue();
        int initialCapacity = Math.min(builder.getInitialCapacity(), 0x40000000);
        if (this.evictsBySize()) {
            initialCapacity = Math.min(initialCapacity, this.maximumSize);
        }
        int segmentShift = 0;
        for (segmentCount = 1; !(segmentCount >= this.concurrencyLevel || this.evictsBySize() && segmentCount * 2 > this.maximumSize); segmentCount <<= 1) {
            ++segmentShift;
        }
        this.segmentShift = 32 - segmentShift;
        this.segmentMask = segmentCount - 1;
        this.segments = this.newSegmentArray(segmentCount);
        int segmentCapacity = initialCapacity / segmentCount;
        if (segmentCapacity * segmentCount < initialCapacity) {
            ++segmentCapacity;
        }
        for (segmentSize = 1; segmentSize < segmentCapacity; segmentSize <<= 1) {
        }
        if (this.evictsBySize()) {
            int maximumSegmentSize = this.maximumSize / segmentCount + 1;
            int remainder = this.maximumSize % segmentCount;
            for (int i2 = 0; i2 < this.segments.length; ++i2) {
                if (i2 == remainder) {
                    --maximumSegmentSize;
                }
                this.segments[i2] = this.createSegment(segmentSize, maximumSegmentSize);
            }
        } else {
            for (int i3 = 0; i3 < this.segments.length; ++i3) {
                this.segments[i3] = this.createSegment(segmentSize, -1);
            }
        }
    }

    boolean evictsBySize() {
        return this.maximumSize != -1;
    }

    boolean expires() {
        return this.expiresAfterWrite() || this.expiresAfterAccess();
    }

    boolean expiresAfterWrite() {
        return this.expireAfterWriteNanos > 0L;
    }

    boolean expiresAfterAccess() {
        return this.expireAfterAccessNanos > 0L;
    }

    boolean usesKeyReferences() {
        return this.keyStrength != Strength.STRONG;
    }

    boolean usesValueReferences() {
        return this.valueStrength != Strength.STRONG;
    }

    static <K, V> ValueReference<K, V> unset() {
        return UNSET;
    }

    static <K, V> ReferenceEntry<K, V> nullEntry() {
        return NullEntry.INSTANCE;
    }

    static <E> Queue<E> discardingQueue() {
        return DISCARDING_QUEUE;
    }

    static int rehash(int h2) {
        h2 += h2 << 15 ^ 0xFFFFCD7D;
        h2 ^= h2 >>> 10;
        h2 += h2 << 3;
        h2 ^= h2 >>> 6;
        h2 += (h2 << 2) + (h2 << 14);
        return h2 ^ h2 >>> 16;
    }

    @GuardedBy(value="Segment.this")
    @VisibleForTesting
    ReferenceEntry<K, V> newEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
        return this.segmentFor(hash).newEntry(key, hash, next);
    }

    @GuardedBy(value="Segment.this")
    @VisibleForTesting
    ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
        int hash = original.getHash();
        return this.segmentFor(hash).copyEntry(original, newNext);
    }

    @GuardedBy(value="Segment.this")
    @VisibleForTesting
    ValueReference<K, V> newValueReference(ReferenceEntry<K, V> entry, V value) {
        int hash = entry.getHash();
        return this.valueStrength.referenceValue(this.segmentFor(hash), entry, value);
    }

    int hash(Object key) {
        int h2 = this.keyEquivalence.hash(key);
        return MapMakerInternalMap.rehash(h2);
    }

    void reclaimValue(ValueReference<K, V> valueReference) {
        ReferenceEntry<K, V> entry = valueReference.getEntry();
        int hash = entry.getHash();
        this.segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
    }

    void reclaimKey(ReferenceEntry<K, V> entry) {
        int hash = entry.getHash();
        this.segmentFor(hash).reclaimKey(entry, hash);
    }

    @VisibleForTesting
    boolean isLive(ReferenceEntry<K, V> entry) {
        return this.segmentFor(entry.getHash()).getLiveValue(entry) != null;
    }

    Segment<K, V> segmentFor(int hash) {
        return this.segments[hash >>> this.segmentShift & this.segmentMask];
    }

    Segment<K, V> createSegment(int initialCapacity, int maxSegmentSize) {
        return new Segment(this, initialCapacity, maxSegmentSize);
    }

    V getLiveValue(ReferenceEntry<K, V> entry) {
        if (entry.getKey() == null) {
            return null;
        }
        V value = entry.getValueReference().get();
        if (value == null) {
            return null;
        }
        if (this.expires() && this.isExpired(entry)) {
            return null;
        }
        return value;
    }

    boolean isExpired(ReferenceEntry<K, V> entry) {
        return this.isExpired(entry, this.ticker.read());
    }

    boolean isExpired(ReferenceEntry<K, V> entry, long now) {
        return now - entry.getExpirationTime() > 0L;
    }

    @GuardedBy(value="Segment.this")
    static <K, V> void connectExpirables(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
        previous.setNextExpirable(next);
        next.setPreviousExpirable(previous);
    }

    @GuardedBy(value="Segment.this")
    static <K, V> void nullifyExpirable(ReferenceEntry<K, V> nulled) {
        ReferenceEntry<K, V> nullEntry = MapMakerInternalMap.nullEntry();
        nulled.setNextExpirable(nullEntry);
        nulled.setPreviousExpirable(nullEntry);
    }

    void processPendingNotifications() {
        MapMaker.RemovalNotification<K, V> notification;
        while ((notification = this.removalNotificationQueue.poll()) != null) {
            try {
                this.removalListener.onRemoval(notification);
            }
            catch (Exception e2) {
                logger.log(Level.WARNING, "Exception thrown by removal listener", e2);
            }
        }
    }

    @GuardedBy(value="Segment.this")
    static <K, V> void connectEvictables(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
        previous.setNextEvictable(next);
        next.setPreviousEvictable(previous);
    }

    @GuardedBy(value="Segment.this")
    static <K, V> void nullifyEvictable(ReferenceEntry<K, V> nulled) {
        ReferenceEntry<K, V> nullEntry = MapMakerInternalMap.nullEntry();
        nulled.setNextEvictable(nullEntry);
        nulled.setPreviousEvictable(nullEntry);
    }

    final Segment<K, V>[] newSegmentArray(int ssize) {
        return new Segment[ssize];
    }

    @Override
    public boolean isEmpty() {
        int i2;
        long sum = 0L;
        Segment<K, V>[] segments = this.segments;
        for (i2 = 0; i2 < segments.length; ++i2) {
            if (segments[i2].count != 0) {
                return false;
            }
            sum += (long)segments[i2].modCount;
        }
        if (sum != 0L) {
            for (i2 = 0; i2 < segments.length; ++i2) {
                if (segments[i2].count != 0) {
                    return false;
                }
                sum -= (long)segments[i2].modCount;
            }
            if (sum != 0L) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        Segment<K, V>[] segments = this.segments;
        long sum = 0L;
        for (int i2 = 0; i2 < segments.length; ++i2) {
            sum += (long)segments[i2].count;
        }
        return Ints.saturatedCast(sum);
    }

    @Override
    public V get(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        int hash = this.hash(key);
        return this.segmentFor(hash).get(key, hash);
    }

    ReferenceEntry<K, V> getEntry(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        int hash = this.hash(key);
        return this.segmentFor(hash).getEntry(key, hash);
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        if (key == null) {
            return false;
        }
        int hash = this.hash(key);
        return this.segmentFor(hash).containsKey(key, hash);
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        if (value == null) {
            return false;
        }
        Segment<K, V>[] segments = this.segments;
        long last = -1L;
        for (int i2 = 0; i2 < 3; ++i2) {
            long sum = 0L;
            for (Segment segment : segments) {
                int c2 = segment.count;
                AtomicReferenceArray table = segment.table;
                for (int j2 = 0; j2 < table.length(); ++j2) {
                    for (ReferenceEntry e2 = table.get(j2); e2 != null; e2 = e2.getNext()) {
                        V v2 = segment.getLiveValue(e2);
                        if (v2 == null || !this.valueEquivalence.equivalent(value, v2)) continue;
                        return true;
                    }
                }
                sum += (long)segment.modCount;
            }
            if (sum == last) break;
            last = sum;
        }
        return false;
    }

    @Override
    public V put(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = this.hash(key);
        return this.segmentFor(hash).put(key, hash, value, false);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = this.hash(key);
        return this.segmentFor(hash).put(key, hash, value, true);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m2) {
        for (Map.Entry<K, V> e2 : m2.entrySet()) {
            this.put(e2.getKey(), e2.getValue());
        }
    }

    @Override
    public V remove(@Nullable Object key) {
        if (key == null) {
            return null;
        }
        int hash = this.hash(key);
        return this.segmentFor(hash).remove(key, hash);
    }

    @Override
    public boolean remove(@Nullable Object key, @Nullable Object value) {
        if (key == null || value == null) {
            return false;
        }
        int hash = this.hash(key);
        return this.segmentFor(hash).remove(key, hash, value);
    }

    @Override
    public boolean replace(K key, @Nullable V oldValue, V newValue) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(newValue);
        if (oldValue == null) {
            return false;
        }
        int hash = this.hash(key);
        return this.segmentFor(hash).replace(key, hash, oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        int hash = this.hash(key);
        return this.segmentFor(hash).replace(key, hash, value);
    }

    @Override
    public void clear() {
        for (Segment<K, V> segment : this.segments) {
            segment.clear();
        }
    }

    @Override
    public Set<K> keySet() {
        KeySet ks = this.keySet;
        return ks != null ? ks : (this.keySet = new KeySet());
    }

    @Override
    public Collection<V> values() {
        Values vs = this.values;
        return vs != null ? vs : (this.values = new Values());
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        EntrySet es = this.entrySet;
        return es != null ? es : (this.entrySet = new EntrySet());
    }

    Object writeReplace() {
        return new SerializationProxy<K, V>(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this);
    }

    private static final class SerializationProxy<K, V>
    extends AbstractSerializationProxy<K, V> {
        private static final long serialVersionUID = 3L;

        SerializationProxy(Strength keyStrength, Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, int maximumSize, int concurrencyLevel, MapMaker.RemovalListener<? super K, ? super V> removalListener, ConcurrentMap<K, V> delegate) {
            super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, expireAfterWriteNanos, expireAfterAccessNanos, maximumSize, concurrencyLevel, removalListener, delegate);
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            this.writeMapTo(out);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            MapMaker mapMaker = this.readMapMaker(in);
            this.delegate = mapMaker.makeMap();
            this.readEntries(in);
        }

        private Object readResolve() {
            return this.delegate;
        }
    }

    static abstract class AbstractSerializationProxy<K, V>
    extends ForwardingConcurrentMap<K, V>
    implements Serializable {
        private static final long serialVersionUID = 3L;
        final Strength keyStrength;
        final Strength valueStrength;
        final Equivalence<Object> keyEquivalence;
        final Equivalence<Object> valueEquivalence;
        final long expireAfterWriteNanos;
        final long expireAfterAccessNanos;
        final int maximumSize;
        final int concurrencyLevel;
        final MapMaker.RemovalListener<? super K, ? super V> removalListener;
        transient ConcurrentMap<K, V> delegate;

        AbstractSerializationProxy(Strength keyStrength, Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, int maximumSize, int concurrencyLevel, MapMaker.RemovalListener<? super K, ? super V> removalListener, ConcurrentMap<K, V> delegate) {
            this.keyStrength = keyStrength;
            this.valueStrength = valueStrength;
            this.keyEquivalence = keyEquivalence;
            this.valueEquivalence = valueEquivalence;
            this.expireAfterWriteNanos = expireAfterWriteNanos;
            this.expireAfterAccessNanos = expireAfterAccessNanos;
            this.maximumSize = maximumSize;
            this.concurrencyLevel = concurrencyLevel;
            this.removalListener = removalListener;
            this.delegate = delegate;
        }

        @Override
        protected ConcurrentMap<K, V> delegate() {
            return this.delegate;
        }

        void writeMapTo(ObjectOutputStream out) throws IOException {
            out.writeInt(this.delegate.size());
            for (Map.Entry entry : this.delegate.entrySet()) {
                out.writeObject(entry.getKey());
                out.writeObject(entry.getValue());
            }
            out.writeObject(null);
        }

        MapMaker readMapMaker(ObjectInputStream in) throws IOException {
            int size = in.readInt();
            MapMaker mapMaker = ((MapMaker)new MapMaker().initialCapacity(size).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence((Equivalence)this.keyEquivalence)).concurrencyLevel(this.concurrencyLevel);
            mapMaker.removalListener(this.removalListener);
            if (this.expireAfterWriteNanos > 0L) {
                mapMaker.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
            }
            if (this.expireAfterAccessNanos > 0L) {
                mapMaker.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
            }
            if (this.maximumSize != -1) {
                mapMaker.maximumSize(this.maximumSize);
            }
            return mapMaker;
        }

        void readEntries(ObjectInputStream in) throws IOException, ClassNotFoundException {
            Object key;
            while ((key = in.readObject()) != null) {
                Object value = in.readObject();
                this.delegate.put(key, value);
            }
        }
    }

    final class EntrySet
    extends AbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public boolean contains(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e2 = (Map.Entry)o2;
            Object key = e2.getKey();
            if (key == null) {
                return false;
            }
            Object v2 = MapMakerInternalMap.this.get(key);
            return v2 != null && MapMakerInternalMap.this.valueEquivalence.equivalent(e2.getValue(), v2);
        }

        @Override
        public boolean remove(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e2 = (Map.Entry)o2;
            Object key = e2.getKey();
            return key != null && MapMakerInternalMap.this.remove(key, e2.getValue());
        }

        @Override
        public int size() {
            return MapMakerInternalMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }

    final class Values
    extends AbstractCollection<V> {
        Values() {
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return MapMakerInternalMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override
        public boolean contains(Object o2) {
            return MapMakerInternalMap.this.containsValue(o2);
        }

        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }

    final class KeySet
    extends AbstractSet<K> {
        KeySet() {
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return MapMakerInternalMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return MapMakerInternalMap.this.isEmpty();
        }

        @Override
        public boolean contains(Object o2) {
            return MapMakerInternalMap.this.containsKey(o2);
        }

        @Override
        public boolean remove(Object o2) {
            return MapMakerInternalMap.this.remove(o2) != null;
        }

        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }

    final class EntryIterator
    extends HashIterator<Map.Entry<K, V>> {
        EntryIterator() {
        }

        @Override
        public Map.Entry<K, V> next() {
            return this.nextEntry();
        }
    }

    final class WriteThroughEntry
    extends AbstractMapEntry<K, V> {
        final K key;
        V value;

        WriteThroughEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public boolean equals(@Nullable Object object) {
            if (object instanceof Map.Entry) {
                Map.Entry that = (Map.Entry)object;
                return this.key.equals(that.getKey()) && this.value.equals(that.getValue());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }

        @Override
        public V setValue(V newValue) {
            Object oldValue = MapMakerInternalMap.this.put(this.key, newValue);
            this.value = newValue;
            return oldValue;
        }
    }

    final class ValueIterator
    extends HashIterator<V> {
        ValueIterator() {
        }

        @Override
        public V next() {
            return this.nextEntry().getValue();
        }
    }

    final class KeyIterator
    extends HashIterator<K> {
        KeyIterator() {
        }

        @Override
        public K next() {
            return this.nextEntry().getKey();
        }
    }

    abstract class HashIterator<E>
    implements Iterator<E> {
        int nextSegmentIndex;
        int nextTableIndex;
        Segment<K, V> currentSegment;
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
        ReferenceEntry<K, V> nextEntry;
        WriteThroughEntry nextExternal;
        WriteThroughEntry lastReturned;

        HashIterator() {
            this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
            this.nextTableIndex = -1;
            this.advance();
        }

        @Override
        public abstract E next();

        final void advance() {
            this.nextExternal = null;
            if (this.nextInChain()) {
                return;
            }
            if (this.nextInTable()) {
                return;
            }
            while (this.nextSegmentIndex >= 0) {
                this.currentSegment = MapMakerInternalMap.this.segments[this.nextSegmentIndex--];
                if (this.currentSegment.count == 0) continue;
                this.currentTable = this.currentSegment.table;
                this.nextTableIndex = this.currentTable.length() - 1;
                if (!this.nextInTable()) continue;
                return;
            }
        }

        boolean nextInChain() {
            if (this.nextEntry != null) {
                this.nextEntry = this.nextEntry.getNext();
                while (this.nextEntry != null) {
                    if (this.advanceTo(this.nextEntry)) {
                        return true;
                    }
                    this.nextEntry = this.nextEntry.getNext();
                }
            }
            return false;
        }

        boolean nextInTable() {
            while (this.nextTableIndex >= 0) {
                if ((this.nextEntry = this.currentTable.get(this.nextTableIndex--)) == null || !this.advanceTo(this.nextEntry) && !this.nextInChain()) continue;
                return true;
            }
            return false;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean advanceTo(ReferenceEntry<K, V> entry) {
            try {
                Object key = entry.getKey();
                Object value = MapMakerInternalMap.this.getLiveValue(entry);
                if (value != null) {
                    this.nextExternal = new WriteThroughEntry(key, value);
                    boolean bl = true;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.currentSegment.postReadCleanup();
            }
        }

        @Override
        public boolean hasNext() {
            return this.nextExternal != null;
        }

        WriteThroughEntry nextEntry() {
            if (this.nextExternal == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.nextExternal;
            this.advance();
            return this.lastReturned;
        }

        @Override
        public void remove() {
            CollectPreconditions.checkRemove(this.lastReturned != null);
            MapMakerInternalMap.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }

    static final class CleanupMapTask
    implements Runnable {
        final WeakReference<MapMakerInternalMap<?, ?>> mapReference;

        public CleanupMapTask(MapMakerInternalMap<?, ?> map) {
            this.mapReference = new WeakReference(map);
        }

        @Override
        public void run() {
            MapMakerInternalMap map = (MapMakerInternalMap)this.mapReference.get();
            if (map == null) {
                throw new CancellationException();
            }
            for (Segment segment : map.segments) {
                segment.runCleanup();
            }
        }
    }

    static final class ExpirationQueue<K, V>
    extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head = new AbstractReferenceEntry<K, V>(){
            ReferenceEntry<K, V> nextExpirable = this;
            ReferenceEntry<K, V> previousExpirable = this;

            @Override
            public long getExpirationTime() {
                return Long.MAX_VALUE;
            }

            @Override
            public void setExpirationTime(long time) {
            }

            @Override
            public ReferenceEntry<K, V> getNextExpirable() {
                return this.nextExpirable;
            }

            @Override
            public void setNextExpirable(ReferenceEntry<K, V> next) {
                this.nextExpirable = next;
            }

            @Override
            public ReferenceEntry<K, V> getPreviousExpirable() {
                return this.previousExpirable;
            }

            @Override
            public void setPreviousExpirable(ReferenceEntry<K, V> previous) {
                this.previousExpirable = previous;
            }
        };

        ExpirationQueue() {
        }

        @Override
        public boolean offer(ReferenceEntry<K, V> entry) {
            MapMakerInternalMap.connectExpirables(entry.getPreviousExpirable(), entry.getNextExpirable());
            MapMakerInternalMap.connectExpirables(this.head.getPreviousExpirable(), entry);
            MapMakerInternalMap.connectExpirables(entry, this.head);
            return true;
        }

        @Override
        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> next = this.head.getNextExpirable();
            return next == this.head ? null : next;
        }

        @Override
        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> next = this.head.getNextExpirable();
            if (next == this.head) {
                return null;
            }
            this.remove(next);
            return next;
        }

        @Override
        public boolean remove(Object o2) {
            ReferenceEntry e2 = (ReferenceEntry)o2;
            ReferenceEntry previous = e2.getPreviousExpirable();
            ReferenceEntry next = e2.getNextExpirable();
            MapMakerInternalMap.connectExpirables(previous, next);
            MapMakerInternalMap.nullifyExpirable(e2);
            return next != NullEntry.INSTANCE;
        }

        @Override
        public boolean contains(Object o2) {
            ReferenceEntry e2 = (ReferenceEntry)o2;
            return e2.getNextExpirable() != NullEntry.INSTANCE;
        }

        @Override
        public boolean isEmpty() {
            return this.head.getNextExpirable() == this.head;
        }

        @Override
        public int size() {
            int size = 0;
            for (ReferenceEntry<K, V> e2 = this.head.getNextExpirable(); e2 != this.head; e2 = e2.getNextExpirable()) {
                ++size;
            }
            return size;
        }

        @Override
        public void clear() {
            ReferenceEntry<K, V> e2 = this.head.getNextExpirable();
            while (e2 != this.head) {
                ReferenceEntry<K, V> next = e2.getNextExpirable();
                MapMakerInternalMap.nullifyExpirable(e2);
                e2 = next;
            }
            this.head.setNextExpirable(this.head);
            this.head.setPreviousExpirable(this.head);
        }

        @Override
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>((ReferenceEntry)this.peek()){

                @Override
                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> previous) {
                    ReferenceEntry next = previous.getNextExpirable();
                    return next == ExpirationQueue.this.head ? null : next;
                }
            };
        }
    }

    static final class EvictionQueue<K, V>
    extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head = new AbstractReferenceEntry<K, V>(){
            ReferenceEntry<K, V> nextEvictable = this;
            ReferenceEntry<K, V> previousEvictable = this;

            @Override
            public ReferenceEntry<K, V> getNextEvictable() {
                return this.nextEvictable;
            }

            @Override
            public void setNextEvictable(ReferenceEntry<K, V> next) {
                this.nextEvictable = next;
            }

            @Override
            public ReferenceEntry<K, V> getPreviousEvictable() {
                return this.previousEvictable;
            }

            @Override
            public void setPreviousEvictable(ReferenceEntry<K, V> previous) {
                this.previousEvictable = previous;
            }
        };

        EvictionQueue() {
        }

        @Override
        public boolean offer(ReferenceEntry<K, V> entry) {
            MapMakerInternalMap.connectEvictables(entry.getPreviousEvictable(), entry.getNextEvictable());
            MapMakerInternalMap.connectEvictables(this.head.getPreviousEvictable(), entry);
            MapMakerInternalMap.connectEvictables(entry, this.head);
            return true;
        }

        @Override
        public ReferenceEntry<K, V> peek() {
            ReferenceEntry<K, V> next = this.head.getNextEvictable();
            return next == this.head ? null : next;
        }

        @Override
        public ReferenceEntry<K, V> poll() {
            ReferenceEntry<K, V> next = this.head.getNextEvictable();
            if (next == this.head) {
                return null;
            }
            this.remove(next);
            return next;
        }

        @Override
        public boolean remove(Object o2) {
            ReferenceEntry e2 = (ReferenceEntry)o2;
            ReferenceEntry previous = e2.getPreviousEvictable();
            ReferenceEntry next = e2.getNextEvictable();
            MapMakerInternalMap.connectEvictables(previous, next);
            MapMakerInternalMap.nullifyEvictable(e2);
            return next != NullEntry.INSTANCE;
        }

        @Override
        public boolean contains(Object o2) {
            ReferenceEntry e2 = (ReferenceEntry)o2;
            return e2.getNextEvictable() != NullEntry.INSTANCE;
        }

        @Override
        public boolean isEmpty() {
            return this.head.getNextEvictable() == this.head;
        }

        @Override
        public int size() {
            int size = 0;
            for (ReferenceEntry<K, V> e2 = this.head.getNextEvictable(); e2 != this.head; e2 = e2.getNextEvictable()) {
                ++size;
            }
            return size;
        }

        @Override
        public void clear() {
            ReferenceEntry<K, V> e2 = this.head.getNextEvictable();
            while (e2 != this.head) {
                ReferenceEntry<K, V> next = e2.getNextEvictable();
                MapMakerInternalMap.nullifyEvictable(e2);
                e2 = next;
            }
            this.head.setNextEvictable(this.head);
            this.head.setPreviousEvictable(this.head);
        }

        @Override
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return new AbstractSequentialIterator<ReferenceEntry<K, V>>((ReferenceEntry)this.peek()){

                @Override
                protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> previous) {
                    ReferenceEntry next = previous.getNextEvictable();
                    return next == EvictionQueue.this.head ? null : next;
                }
            };
        }
    }

    static class Segment<K, V>
    extends ReentrantLock {
        final MapMakerInternalMap<K, V> map;
        volatile int count;
        int modCount;
        int threshold;
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        final int maxSegmentSize;
        final ReferenceQueue<K> keyReferenceQueue;
        final ReferenceQueue<V> valueReferenceQueue;
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        final AtomicInteger readCount = new AtomicInteger();
        @GuardedBy(value="Segment.this")
        final Queue<ReferenceEntry<K, V>> evictionQueue;
        @GuardedBy(value="Segment.this")
        final Queue<ReferenceEntry<K, V>> expirationQueue;

        Segment(MapMakerInternalMap<K, V> map, int initialCapacity, int maxSegmentSize) {
            this.map = map;
            this.maxSegmentSize = maxSegmentSize;
            this.initTable(this.newEntryArray(initialCapacity));
            this.keyReferenceQueue = map.usesKeyReferences() ? new ReferenceQueue() : null;
            this.valueReferenceQueue = map.usesValueReferences() ? new ReferenceQueue() : null;
            this.recencyQueue = map.evictsBySize() || map.expiresAfterAccess() ? new ConcurrentLinkedQueue() : MapMakerInternalMap.discardingQueue();
            this.evictionQueue = map.evictsBySize() ? new EvictionQueue() : MapMakerInternalMap.discardingQueue();
            this.expirationQueue = map.expires() ? new ExpirationQueue() : MapMakerInternalMap.discardingQueue();
        }

        AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(int size) {
            return new AtomicReferenceArray<ReferenceEntry<K, V>>(size);
        }

        void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> newTable) {
            this.threshold = newTable.length() * 3 / 4;
            if (this.threshold == this.maxSegmentSize) {
                ++this.threshold;
            }
            this.table = newTable;
        }

        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> newEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            return this.map.entryFactory.newEntry(this, key, hash, next);
        }

        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
            if (original.getKey() == null) {
                return null;
            }
            ValueReference<K, V> valueReference = original.getValueReference();
            V value = valueReference.get();
            if (value == null && !valueReference.isComputingReference()) {
                return null;
            }
            ReferenceEntry<K, V> newEntry = this.map.entryFactory.copyEntry(this, original, newNext);
            newEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, value, newEntry));
            return newEntry;
        }

        @GuardedBy(value="Segment.this")
        void setValue(ReferenceEntry<K, V> entry, V value) {
            ValueReference<K, V> valueReference = this.map.valueStrength.referenceValue(this, entry, value);
            entry.setValueReference(valueReference);
            this.recordWrite(entry);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void tryDrainReferenceQueues() {
            if (this.tryLock()) {
                try {
                    this.drainReferenceQueues();
                }
                finally {
                    this.unlock();
                }
            }
        }

        @GuardedBy(value="Segment.this")
        void drainReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                this.drainKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                this.drainValueReferenceQueue();
            }
        }

        @GuardedBy(value="Segment.this")
        void drainKeyReferenceQueue() {
            Reference<K> ref;
            int i2 = 0;
            while ((ref = this.keyReferenceQueue.poll()) != null) {
                ReferenceEntry entry = (ReferenceEntry)((Object)ref);
                this.map.reclaimKey(entry);
                if (++i2 != 16) continue;
                break;
            }
        }

        @GuardedBy(value="Segment.this")
        void drainValueReferenceQueue() {
            Reference<V> ref;
            int i2 = 0;
            while ((ref = this.valueReferenceQueue.poll()) != null) {
                ValueReference valueReference = (ValueReference)((Object)ref);
                this.map.reclaimValue(valueReference);
                if (++i2 != 16) continue;
                break;
            }
        }

        void clearReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                this.clearKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                this.clearValueReferenceQueue();
            }
        }

        void clearKeyReferenceQueue() {
            while (this.keyReferenceQueue.poll() != null) {
            }
        }

        void clearValueReferenceQueue() {
            while (this.valueReferenceQueue.poll() != null) {
            }
        }

        void recordRead(ReferenceEntry<K, V> entry) {
            if (this.map.expiresAfterAccess()) {
                this.recordExpirationTime(entry, this.map.expireAfterAccessNanos);
            }
            this.recencyQueue.add(entry);
        }

        @GuardedBy(value="Segment.this")
        void recordLockedRead(ReferenceEntry<K, V> entry) {
            this.evictionQueue.add(entry);
            if (this.map.expiresAfterAccess()) {
                this.recordExpirationTime(entry, this.map.expireAfterAccessNanos);
                this.expirationQueue.add(entry);
            }
        }

        @GuardedBy(value="Segment.this")
        void recordWrite(ReferenceEntry<K, V> entry) {
            this.drainRecencyQueue();
            this.evictionQueue.add(entry);
            if (this.map.expires()) {
                long expiration = this.map.expiresAfterAccess() ? this.map.expireAfterAccessNanos : this.map.expireAfterWriteNanos;
                this.recordExpirationTime(entry, expiration);
                this.expirationQueue.add(entry);
            }
        }

        @GuardedBy(value="Segment.this")
        void drainRecencyQueue() {
            ReferenceEntry<K, V> e2;
            while ((e2 = this.recencyQueue.poll()) != null) {
                if (this.evictionQueue.contains(e2)) {
                    this.evictionQueue.add(e2);
                }
                if (!this.map.expiresAfterAccess() || !this.expirationQueue.contains(e2)) continue;
                this.expirationQueue.add(e2);
            }
        }

        void recordExpirationTime(ReferenceEntry<K, V> entry, long expirationNanos) {
            entry.setExpirationTime(this.map.ticker.read() + expirationNanos);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void tryExpireEntries() {
            if (this.tryLock()) {
                try {
                    this.expireEntries();
                }
                finally {
                    this.unlock();
                }
            }
        }

        @GuardedBy(value="Segment.this")
        void expireEntries() {
            ReferenceEntry<K, V> e2;
            this.drainRecencyQueue();
            if (this.expirationQueue.isEmpty()) {
                return;
            }
            long now = this.map.ticker.read();
            while ((e2 = this.expirationQueue.peek()) != null && this.map.isExpired(e2, now)) {
                if (!this.removeEntry(e2, e2.getHash(), MapMaker.RemovalCause.EXPIRED)) {
                    throw new AssertionError();
                }
            }
        }

        void enqueueNotification(ReferenceEntry<K, V> entry, MapMaker.RemovalCause cause) {
            this.enqueueNotification(entry.getKey(), entry.getHash(), entry.getValueReference().get(), cause);
        }

        void enqueueNotification(@Nullable K key, int hash, @Nullable V value, MapMaker.RemovalCause cause) {
            if (this.map.removalNotificationQueue != DISCARDING_QUEUE) {
                MapMaker.RemovalNotification<K, V> notification = new MapMaker.RemovalNotification<K, V>(key, value, cause);
                this.map.removalNotificationQueue.offer(notification);
            }
        }

        @GuardedBy(value="Segment.this")
        boolean evictEntries() {
            if (this.map.evictsBySize() && this.count >= this.maxSegmentSize) {
                this.drainRecencyQueue();
                ReferenceEntry<K, V> e2 = this.evictionQueue.remove();
                if (!this.removeEntry(e2, e2.getHash(), MapMaker.RemovalCause.SIZE)) {
                    throw new AssertionError();
                }
                return true;
            }
            return false;
        }

        ReferenceEntry<K, V> getFirst(int hash) {
            AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            return table.get(hash & table.length() - 1);
        }

        ReferenceEntry<K, V> getEntry(Object key, int hash) {
            if (this.count != 0) {
                for (ReferenceEntry<K, V> e2 = this.getFirst(hash); e2 != null; e2 = e2.getNext()) {
                    if (e2.getHash() != hash) continue;
                    K entryKey = e2.getKey();
                    if (entryKey == null) {
                        this.tryDrainReferenceQueues();
                        continue;
                    }
                    if (!this.map.keyEquivalence.equivalent(key, entryKey)) continue;
                    return e2;
                }
            }
            return null;
        }

        ReferenceEntry<K, V> getLiveEntry(Object key, int hash) {
            ReferenceEntry<K, V> e2 = this.getEntry(key, hash);
            if (e2 == null) {
                return null;
            }
            if (this.map.expires() && this.map.isExpired(e2)) {
                this.tryExpireEntries();
                return null;
            }
            return e2;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V get(Object key, int hash) {
            try {
                ReferenceEntry<K, V> e2 = this.getLiveEntry(key, hash);
                if (e2 == null) {
                    V v2 = null;
                    return v2;
                }
                V value = e2.getValueReference().get();
                if (value != null) {
                    this.recordRead(e2);
                } else {
                    this.tryDrainReferenceQueues();
                }
                V v3 = value;
                return v3;
            }
            finally {
                this.postReadCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean containsKey(Object key, int hash) {
            try {
                if (this.count != 0) {
                    ReferenceEntry<K, V> e2 = this.getLiveEntry(key, hash);
                    if (e2 == null) {
                        boolean bl = false;
                        return bl;
                    }
                    boolean bl = e2.getValueReference().get() != null;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.postReadCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @VisibleForTesting
        boolean containsValue(Object value) {
            try {
                if (this.count != 0) {
                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int length = table.length();
                    for (int i2 = 0; i2 < length; ++i2) {
                        for (ReferenceEntry<K, V> e2 = table.get(i2); e2 != null; e2 = e2.getNext()) {
                            V entryValue = this.getLiveValue(e2);
                            if (entryValue == null || !this.map.valueEquivalence.equivalent(value, entryValue)) continue;
                            boolean bl = true;
                            return bl;
                        }
                    }
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.postReadCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V put(K key, int hash, V value, boolean onlyIfAbsent) {
            this.lock();
            try {
                ReferenceEntry<K, V> first;
                this.preWriteCleanup();
                int newCount = this.count + 1;
                if (newCount > this.threshold) {
                    this.expand();
                    newCount = this.count + 1;
                }
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & table.length() - 1;
                for (ReferenceEntry<K, V> e2 = first = table.get(index); e2 != null; e2 = e2.getNext()) {
                    K entryKey = e2.getKey();
                    if (e2.getHash() != hash || entryKey == null || !this.map.keyEquivalence.equivalent(key, entryKey)) continue;
                    ValueReference<K, V> valueReference = e2.getValueReference();
                    V entryValue = valueReference.get();
                    if (entryValue == null) {
                        ++this.modCount;
                        this.setValue(e2, value);
                        if (!valueReference.isComputingReference()) {
                            this.enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.COLLECTED);
                            newCount = this.count;
                        } else if (this.evictEntries()) {
                            newCount = this.count + 1;
                        }
                        this.count = newCount;
                        V v2 = null;
                        return v2;
                    }
                    if (onlyIfAbsent) {
                        this.recordLockedRead(e2);
                        V v3 = entryValue;
                        return v3;
                    }
                    ++this.modCount;
                    this.enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.REPLACED);
                    this.setValue(e2, value);
                    V v4 = entryValue;
                    return v4;
                }
                ++this.modCount;
                ReferenceEntry<K, V> newEntry = this.newEntry(key, hash, first);
                this.setValue(newEntry, value);
                table.set(index, newEntry);
                if (this.evictEntries()) {
                    newCount = this.count + 1;
                }
                this.count = newCount;
                V v5 = null;
                return v5;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        @GuardedBy(value="Segment.this")
        void expand() {
            AtomicReferenceArray<ReferenceEntry<K, V>> oldTable = this.table;
            int oldCapacity = oldTable.length();
            if (oldCapacity >= 0x40000000) {
                return;
            }
            int newCount = this.count;
            AtomicReferenceArray<ReferenceEntry<K, V>> newTable = this.newEntryArray(oldCapacity << 1);
            this.threshold = newTable.length() * 3 / 4;
            int newMask = newTable.length() - 1;
            for (int oldIndex = 0; oldIndex < oldCapacity; ++oldIndex) {
                int newIndex;
                ReferenceEntry<K, V> e2;
                ReferenceEntry<K, V> head = oldTable.get(oldIndex);
                if (head == null) continue;
                ReferenceEntry<K, V> next = head.getNext();
                int headIndex = head.getHash() & newMask;
                if (next == null) {
                    newTable.set(headIndex, head);
                    continue;
                }
                ReferenceEntry<K, V> tail = head;
                int tailIndex = headIndex;
                for (e2 = next; e2 != null; e2 = e2.getNext()) {
                    newIndex = e2.getHash() & newMask;
                    if (newIndex == tailIndex) continue;
                    tailIndex = newIndex;
                    tail = e2;
                }
                newTable.set(tailIndex, tail);
                for (e2 = head; e2 != tail; e2 = e2.getNext()) {
                    newIndex = e2.getHash() & newMask;
                    ReferenceEntry<K, V> newNext = newTable.get(newIndex);
                    ReferenceEntry<K, V> newFirst = this.copyEntry(e2, newNext);
                    if (newFirst != null) {
                        newTable.set(newIndex, newFirst);
                        continue;
                    }
                    this.removeCollectedEntry(e2);
                    --newCount;
                }
            }
            this.table = newTable;
            this.count = newCount;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean replace(K key, int hash, V oldValue, V newValue) {
            this.lock();
            try {
                ReferenceEntry<K, V> first;
                this.preWriteCleanup();
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & table.length() - 1;
                for (ReferenceEntry<K, V> e2 = first = table.get(index); e2 != null; e2 = e2.getNext()) {
                    K entryKey = e2.getKey();
                    if (e2.getHash() != hash || entryKey == null || !this.map.keyEquivalence.equivalent(key, entryKey)) continue;
                    ValueReference<K, V> valueReference = e2.getValueReference();
                    V entryValue = valueReference.get();
                    if (entryValue == null) {
                        if (this.isCollected(valueReference)) {
                            int newCount = this.count - 1;
                            ++this.modCount;
                            this.enqueueNotification(entryKey, hash, entryValue, MapMaker.RemovalCause.COLLECTED);
                            ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e2);
                            newCount = this.count - 1;
                            table.set(index, newFirst);
                            this.count = newCount;
                        }
                        boolean bl = false;
                        return bl;
                    }
                    if (this.map.valueEquivalence.equivalent(oldValue, entryValue)) {
                        ++this.modCount;
                        this.enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.REPLACED);
                        this.setValue(e2, newValue);
                        boolean bl = true;
                        return bl;
                    }
                    this.recordLockedRead(e2);
                    boolean bl = false;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V replace(K key, int hash, V newValue) {
            this.lock();
            try {
                ReferenceEntry<K, V> first;
                this.preWriteCleanup();
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & table.length() - 1;
                for (ReferenceEntry<K, V> e2 = first = table.get(index); e2 != null; e2 = e2.getNext()) {
                    K entryKey = e2.getKey();
                    if (e2.getHash() != hash || entryKey == null || !this.map.keyEquivalence.equivalent(key, entryKey)) continue;
                    ValueReference<K, V> valueReference = e2.getValueReference();
                    V entryValue = valueReference.get();
                    if (entryValue == null) {
                        if (this.isCollected(valueReference)) {
                            int newCount = this.count - 1;
                            ++this.modCount;
                            this.enqueueNotification(entryKey, hash, entryValue, MapMaker.RemovalCause.COLLECTED);
                            ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e2);
                            newCount = this.count - 1;
                            table.set(index, newFirst);
                            this.count = newCount;
                        }
                        V v2 = null;
                        return v2;
                    }
                    ++this.modCount;
                    this.enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.REPLACED);
                    this.setValue(e2, newValue);
                    V v3 = entryValue;
                    return v3;
                }
                V v4 = null;
                return v4;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        V remove(Object key, int hash) {
            this.lock();
            try {
                ReferenceEntry<K, V> first;
                this.preWriteCleanup();
                int newCount = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & table.length() - 1;
                for (ReferenceEntry<K, V> e2 = first = table.get(index); e2 != null; e2 = e2.getNext()) {
                    MapMaker.RemovalCause cause;
                    K entryKey = e2.getKey();
                    if (e2.getHash() != hash || entryKey == null || !this.map.keyEquivalence.equivalent(key, entryKey)) continue;
                    ValueReference<K, V> valueReference = e2.getValueReference();
                    V entryValue = valueReference.get();
                    if (entryValue != null) {
                        cause = MapMaker.RemovalCause.EXPLICIT;
                    } else if (this.isCollected(valueReference)) {
                        cause = MapMaker.RemovalCause.COLLECTED;
                    } else {
                        V v2 = null;
                        return v2;
                    }
                    ++this.modCount;
                    this.enqueueNotification(entryKey, hash, entryValue, cause);
                    ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e2);
                    newCount = this.count - 1;
                    table.set(index, newFirst);
                    this.count = newCount;
                    V v3 = entryValue;
                    return v3;
                }
                V v4 = null;
                return v4;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean remove(Object key, int hash, Object value) {
            this.lock();
            try {
                ReferenceEntry<K, V> first;
                this.preWriteCleanup();
                int newCount = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & table.length() - 1;
                for (ReferenceEntry<K, V> e2 = first = table.get(index); e2 != null; e2 = e2.getNext()) {
                    MapMaker.RemovalCause cause;
                    K entryKey = e2.getKey();
                    if (e2.getHash() != hash || entryKey == null || !this.map.keyEquivalence.equivalent(key, entryKey)) continue;
                    ValueReference<K, V> valueReference = e2.getValueReference();
                    V entryValue = valueReference.get();
                    if (this.map.valueEquivalence.equivalent(value, entryValue)) {
                        cause = MapMaker.RemovalCause.EXPLICIT;
                    } else if (this.isCollected(valueReference)) {
                        cause = MapMaker.RemovalCause.COLLECTED;
                    } else {
                        boolean bl = false;
                        return bl;
                    }
                    ++this.modCount;
                    this.enqueueNotification(entryKey, hash, entryValue, cause);
                    ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e2);
                    newCount = this.count - 1;
                    table.set(index, newFirst);
                    this.count = newCount;
                    boolean bl = cause == MapMaker.RemovalCause.EXPLICIT;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void clear() {
            if (this.count != 0) {
                this.lock();
                try {
                    int i2;
                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    if (this.map.removalNotificationQueue != DISCARDING_QUEUE) {
                        for (i2 = 0; i2 < table.length(); ++i2) {
                            for (ReferenceEntry<K, V> e2 = table.get(i2); e2 != null; e2 = e2.getNext()) {
                                if (e2.getValueReference().isComputingReference()) continue;
                                this.enqueueNotification(e2, MapMaker.RemovalCause.EXPLICIT);
                            }
                        }
                    }
                    for (i2 = 0; i2 < table.length(); ++i2) {
                        table.set(i2, null);
                    }
                    this.clearReferenceQueues();
                    this.evictionQueue.clear();
                    this.expirationQueue.clear();
                    this.readCount.set(0);
                    ++this.modCount;
                    this.count = 0;
                }
                finally {
                    this.unlock();
                    this.postWriteCleanup();
                }
            }
        }

        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> removeFromChain(ReferenceEntry<K, V> first, ReferenceEntry<K, V> entry) {
            this.evictionQueue.remove(entry);
            this.expirationQueue.remove(entry);
            int newCount = this.count;
            ReferenceEntry<K, V> newFirst = entry.getNext();
            for (ReferenceEntry<K, V> e2 = first; e2 != entry; e2 = e2.getNext()) {
                ReferenceEntry<K, V> next = this.copyEntry(e2, newFirst);
                if (next != null) {
                    newFirst = next;
                    continue;
                }
                this.removeCollectedEntry(e2);
                --newCount;
            }
            this.count = newCount;
            return newFirst;
        }

        void removeCollectedEntry(ReferenceEntry<K, V> entry) {
            this.enqueueNotification(entry, MapMaker.RemovalCause.COLLECTED);
            this.evictionQueue.remove(entry);
            this.expirationQueue.remove(entry);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean reclaimKey(ReferenceEntry<K, V> entry, int hash) {
            this.lock();
            try {
                ReferenceEntry<K, V> first;
                int newCount = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & table.length() - 1;
                for (ReferenceEntry<K, V> e2 = first = table.get(index); e2 != null; e2 = e2.getNext()) {
                    if (e2 != entry) continue;
                    ++this.modCount;
                    this.enqueueNotification(e2.getKey(), hash, e2.getValueReference().get(), MapMaker.RemovalCause.COLLECTED);
                    ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e2);
                    newCount = this.count - 1;
                    table.set(index, newFirst);
                    this.count = newCount;
                    boolean bl = true;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean reclaimValue(K key, int hash, ValueReference<K, V> valueReference) {
            this.lock();
            try {
                ReferenceEntry<K, V> first;
                int newCount = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & table.length() - 1;
                for (ReferenceEntry<K, V> e2 = first = table.get(index); e2 != null; e2 = e2.getNext()) {
                    K entryKey = e2.getKey();
                    if (e2.getHash() != hash || entryKey == null || !this.map.keyEquivalence.equivalent(key, entryKey)) continue;
                    ValueReference<K, V> v2 = e2.getValueReference();
                    if (v2 == valueReference) {
                        ++this.modCount;
                        this.enqueueNotification(key, hash, valueReference.get(), MapMaker.RemovalCause.COLLECTED);
                        ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e2);
                        newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount;
                        boolean bl = true;
                        return bl;
                    }
                    boolean bl = false;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.unlock();
                if (!this.isHeldByCurrentThread()) {
                    this.postWriteCleanup();
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        boolean clearValue(K key, int hash, ValueReference<K, V> valueReference) {
            this.lock();
            try {
                ReferenceEntry<K, V> first;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & table.length() - 1;
                for (ReferenceEntry<K, V> e2 = first = table.get(index); e2 != null; e2 = e2.getNext()) {
                    K entryKey = e2.getKey();
                    if (e2.getHash() != hash || entryKey == null || !this.map.keyEquivalence.equivalent(key, entryKey)) continue;
                    ValueReference<K, V> v2 = e2.getValueReference();
                    if (v2 == valueReference) {
                        ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e2);
                        table.set(index, newFirst);
                        boolean bl = true;
                        return bl;
                    }
                    boolean bl = false;
                    return bl;
                }
                boolean bl = false;
                return bl;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }

        @GuardedBy(value="Segment.this")
        boolean removeEntry(ReferenceEntry<K, V> entry, int hash, MapMaker.RemovalCause cause) {
            ReferenceEntry<K, V> first;
            int newCount = this.count - 1;
            AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            int index = hash & table.length() - 1;
            for (ReferenceEntry<K, V> e2 = first = table.get(index); e2 != null; e2 = e2.getNext()) {
                if (e2 != entry) continue;
                ++this.modCount;
                this.enqueueNotification(e2.getKey(), hash, e2.getValueReference().get(), cause);
                ReferenceEntry<K, V> newFirst = this.removeFromChain(first, e2);
                newCount = this.count - 1;
                table.set(index, newFirst);
                this.count = newCount;
                return true;
            }
            return false;
        }

        boolean isCollected(ValueReference<K, V> valueReference) {
            if (valueReference.isComputingReference()) {
                return false;
            }
            return valueReference.get() == null;
        }

        V getLiveValue(ReferenceEntry<K, V> entry) {
            if (entry.getKey() == null) {
                this.tryDrainReferenceQueues();
                return null;
            }
            V value = entry.getValueReference().get();
            if (value == null) {
                this.tryDrainReferenceQueues();
                return null;
            }
            if (this.map.expires() && this.map.isExpired(entry)) {
                this.tryExpireEntries();
                return null;
            }
            return value;
        }

        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 0x3F) == 0) {
                this.runCleanup();
            }
        }

        @GuardedBy(value="Segment.this")
        void preWriteCleanup() {
            this.runLockedCleanup();
        }

        void postWriteCleanup() {
            this.runUnlockedCleanup();
        }

        void runCleanup() {
            this.runLockedCleanup();
            this.runUnlockedCleanup();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void runLockedCleanup() {
            if (this.tryLock()) {
                try {
                    this.drainReferenceQueues();
                    this.expireEntries();
                    this.readCount.set(0);
                }
                finally {
                    this.unlock();
                }
            }
        }

        void runUnlockedCleanup() {
            if (!this.isHeldByCurrentThread()) {
                this.map.processPendingNotifications();
            }
        }
    }

    static final class StrongValueReference<K, V>
    implements ValueReference<K, V> {
        final V referent;

        StrongValueReference(V referent) {
            this.referent = referent;
        }

        @Override
        public V get() {
            return this.referent;
        }

        @Override
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }

        @Override
        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
            return this;
        }

        @Override
        public boolean isComputingReference() {
            return false;
        }

        @Override
        public V waitForValue() {
            return this.get();
        }

        @Override
        public void clear(ValueReference<K, V> newValue) {
        }
    }

    static final class SoftValueReference<K, V>
    extends SoftReference<V>
    implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        SoftValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry) {
            super(referent, queue);
            this.entry = entry;
        }

        @Override
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        @Override
        public void clear(ValueReference<K, V> newValue) {
            this.clear();
        }

        @Override
        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
            return new SoftValueReference<K, V>(queue, value, entry);
        }

        @Override
        public boolean isComputingReference() {
            return false;
        }

        @Override
        public V waitForValue() {
            return (V)this.get();
        }
    }

    static final class WeakValueReference<K, V>
    extends WeakReference<V>
    implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        WeakValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry) {
            super(referent, queue);
            this.entry = entry;
        }

        @Override
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }

        @Override
        public void clear(ValueReference<K, V> newValue) {
            this.clear();
        }

        @Override
        public ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
            return new WeakValueReference<K, V>(queue, value, entry);
        }

        @Override
        public boolean isComputingReference() {
            return false;
        }

        @Override
        public V waitForValue() {
            return (V)this.get();
        }
    }

    static final class WeakExpirableEvictableEntry<K, V>
    extends WeakEntry<K, V>
    implements ReferenceEntry<K, V> {
        volatile long time = Long.MAX_VALUE;
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        WeakExpirableEvictableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        @Override
        public long getExpirationTime() {
            return this.time;
        }

        @Override
        public void setExpirationTime(long time) {
            this.time = time;
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> next) {
            this.nextExpirable = next;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> previous) {
            this.previousExpirable = previous;
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> next) {
            this.nextEvictable = next;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> previous) {
            this.previousEvictable = previous;
        }
    }

    static final class WeakEvictableEntry<K, V>
    extends WeakEntry<K, V>
    implements ReferenceEntry<K, V> {
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        WeakEvictableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> next) {
            this.nextEvictable = next;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> previous) {
            this.previousEvictable = previous;
        }
    }

    static final class WeakExpirableEntry<K, V>
    extends WeakEntry<K, V>
    implements ReferenceEntry<K, V> {
        volatile long time = Long.MAX_VALUE;
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();

        WeakExpirableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        @Override
        public long getExpirationTime() {
            return this.time;
        }

        @Override
        public void setExpirationTime(long time) {
            this.time = time;
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> next) {
            this.nextExpirable = next;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> previous) {
            this.previousExpirable = previous;
        }
    }

    static class WeakEntry<K, V>
    extends WeakReference<K>
    implements ReferenceEntry<K, V> {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = MapMakerInternalMap.unset();

        WeakEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(key, queue);
            this.hash = hash;
            this.next = next;
        }

        @Override
        public K getKey() {
            return (K)this.get();
        }

        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setExpirationTime(long time) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override
        public void setValueReference(ValueReference<K, V> valueReference) {
            ValueReference<K, V> previous = this.valueReference;
            this.valueReference = valueReference;
            previous.clear(valueReference);
        }

        @Override
        public int getHash() {
            return this.hash;
        }

        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    static final class SoftExpirableEvictableEntry<K, V>
    extends SoftEntry<K, V>
    implements ReferenceEntry<K, V> {
        volatile long time = Long.MAX_VALUE;
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        SoftExpirableEvictableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        @Override
        public long getExpirationTime() {
            return this.time;
        }

        @Override
        public void setExpirationTime(long time) {
            this.time = time;
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> next) {
            this.nextExpirable = next;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> previous) {
            this.previousExpirable = previous;
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> next) {
            this.nextEvictable = next;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> previous) {
            this.previousEvictable = previous;
        }
    }

    static final class SoftEvictableEntry<K, V>
    extends SoftEntry<K, V>
    implements ReferenceEntry<K, V> {
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        SoftEvictableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> next) {
            this.nextEvictable = next;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> previous) {
            this.previousEvictable = previous;
        }
    }

    static final class SoftExpirableEntry<K, V>
    extends SoftEntry<K, V>
    implements ReferenceEntry<K, V> {
        volatile long time = Long.MAX_VALUE;
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();

        SoftExpirableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
        }

        @Override
        public long getExpirationTime() {
            return this.time;
        }

        @Override
        public void setExpirationTime(long time) {
            this.time = time;
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> next) {
            this.nextExpirable = next;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> previous) {
            this.previousExpirable = previous;
        }
    }

    static class SoftEntry<K, V>
    extends SoftReference<K>
    implements ReferenceEntry<K, V> {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = MapMakerInternalMap.unset();

        SoftEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(key, queue);
            this.hash = hash;
            this.next = next;
        }

        @Override
        public K getKey() {
            return (K)this.get();
        }

        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setExpirationTime(long time) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override
        public void setValueReference(ValueReference<K, V> valueReference) {
            ValueReference<K, V> previous = this.valueReference;
            this.valueReference = valueReference;
            previous.clear(valueReference);
        }

        @Override
        public int getHash() {
            return this.hash;
        }

        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    static final class StrongExpirableEvictableEntry<K, V>
    extends StrongEntry<K, V>
    implements ReferenceEntry<K, V> {
        volatile long time = Long.MAX_VALUE;
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        StrongExpirableEvictableEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(key, hash, next);
        }

        @Override
        public long getExpirationTime() {
            return this.time;
        }

        @Override
        public void setExpirationTime(long time) {
            this.time = time;
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> next) {
            this.nextExpirable = next;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> previous) {
            this.previousExpirable = previous;
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> next) {
            this.nextEvictable = next;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> previous) {
            this.previousEvictable = previous;
        }
    }

    static final class StrongEvictableEntry<K, V>
    extends StrongEntry<K, V>
    implements ReferenceEntry<K, V> {
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> nextEvictable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> previousEvictable = MapMakerInternalMap.nullEntry();

        StrongEvictableEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(key, hash, next);
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            return this.nextEvictable;
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> next) {
            this.nextEvictable = next;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            return this.previousEvictable;
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> previous) {
            this.previousEvictable = previous;
        }
    }

    static final class StrongExpirableEntry<K, V>
    extends StrongEntry<K, V>
    implements ReferenceEntry<K, V> {
        volatile long time = Long.MAX_VALUE;
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> nextExpirable = MapMakerInternalMap.nullEntry();
        @GuardedBy(value="Segment.this")
        ReferenceEntry<K, V> previousExpirable = MapMakerInternalMap.nullEntry();

        StrongExpirableEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            super(key, hash, next);
        }

        @Override
        public long getExpirationTime() {
            return this.time;
        }

        @Override
        public void setExpirationTime(long time) {
            this.time = time;
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            return this.nextExpirable;
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> next) {
            this.nextExpirable = next;
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            return this.previousExpirable;
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> previous) {
            this.previousExpirable = previous;
        }
    }

    static class StrongEntry<K, V>
    implements ReferenceEntry<K, V> {
        final K key;
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference = MapMakerInternalMap.unset();

        StrongEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
            this.key = key;
            this.hash = hash;
            this.next = next;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setExpirationTime(long time) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override
        public void setValueReference(ValueReference<K, V> valueReference) {
            ValueReference<K, V> previous = this.valueReference;
            this.valueReference = valueReference;
            previous.clear(valueReference);
        }

        @Override
        public int getHash() {
            return this.hash;
        }

        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    static abstract class AbstractReferenceEntry<K, V>
    implements ReferenceEntry<K, V> {
        AbstractReferenceEntry() {
        }

        @Override
        public ValueReference<K, V> getValueReference() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setValueReference(ValueReference<K, V> valueReference) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getHash() {
            throw new UnsupportedOperationException();
        }

        @Override
        public K getKey() {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getExpirationTime() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setExpirationTime(long time) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextExpirable(ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousExpirable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getNextEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setNextEvictable(ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReferenceEntry<K, V> getPreviousEvictable() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
    }

    private static enum NullEntry implements ReferenceEntry<Object, Object>
    {
        INSTANCE;


        @Override
        public ValueReference<Object, Object> getValueReference() {
            return null;
        }

        @Override
        public void setValueReference(ValueReference<Object, Object> valueReference) {
        }

        @Override
        public ReferenceEntry<Object, Object> getNext() {
            return null;
        }

        @Override
        public int getHash() {
            return 0;
        }

        @Override
        public Object getKey() {
            return null;
        }

        @Override
        public long getExpirationTime() {
            return 0L;
        }

        @Override
        public void setExpirationTime(long time) {
        }

        @Override
        public ReferenceEntry<Object, Object> getNextExpirable() {
            return this;
        }

        @Override
        public void setNextExpirable(ReferenceEntry<Object, Object> next) {
        }

        @Override
        public ReferenceEntry<Object, Object> getPreviousExpirable() {
            return this;
        }

        @Override
        public void setPreviousExpirable(ReferenceEntry<Object, Object> previous) {
        }

        @Override
        public ReferenceEntry<Object, Object> getNextEvictable() {
            return this;
        }

        @Override
        public void setNextEvictable(ReferenceEntry<Object, Object> next) {
        }

        @Override
        public ReferenceEntry<Object, Object> getPreviousEvictable() {
            return this;
        }

        @Override
        public void setPreviousEvictable(ReferenceEntry<Object, Object> previous) {
        }
    }

    static interface ReferenceEntry<K, V> {
        public ValueReference<K, V> getValueReference();

        public void setValueReference(ValueReference<K, V> var1);

        public ReferenceEntry<K, V> getNext();

        public int getHash();

        public K getKey();

        public long getExpirationTime();

        public void setExpirationTime(long var1);

        public ReferenceEntry<K, V> getNextExpirable();

        public void setNextExpirable(ReferenceEntry<K, V> var1);

        public ReferenceEntry<K, V> getPreviousExpirable();

        public void setPreviousExpirable(ReferenceEntry<K, V> var1);

        public ReferenceEntry<K, V> getNextEvictable();

        public void setNextEvictable(ReferenceEntry<K, V> var1);

        public ReferenceEntry<K, V> getPreviousEvictable();

        public void setPreviousEvictable(ReferenceEntry<K, V> var1);
    }

    static interface ValueReference<K, V> {
        public V get();

        public V waitForValue() throws ExecutionException;

        public ReferenceEntry<K, V> getEntry();

        public ValueReference<K, V> copyFor(ReferenceQueue<V> var1, @Nullable V var2, ReferenceEntry<K, V> var3);

        public void clear(@Nullable ValueReference<K, V> var1);

        public boolean isComputingReference();
    }

    static enum EntryFactory {
        STRONG{

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new StrongEntry<K, V>(key, hash, next);
            }
        }
        ,
        STRONG_EXPIRABLE{

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new StrongExpirableEntry<K, V>(key, hash, next);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                this.copyExpirableEntry(original, newEntry);
                return newEntry;
            }
        }
        ,
        STRONG_EVICTABLE{

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new StrongEvictableEntry<K, V>(key, hash, next);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                this.copyEvictableEntry(original, newEntry);
                return newEntry;
            }
        }
        ,
        STRONG_EXPIRABLE_EVICTABLE{

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new StrongExpirableEvictableEntry<K, V>(key, hash, next);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                this.copyExpirableEntry(original, newEntry);
                this.copyEvictableEntry(original, newEntry);
                return newEntry;
            }
        }
        ,
        WEAK{

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new WeakEntry(segment.keyReferenceQueue, key, hash, next);
            }
        }
        ,
        WEAK_EXPIRABLE{

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new WeakExpirableEntry(segment.keyReferenceQueue, key, hash, next);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                this.copyExpirableEntry(original, newEntry);
                return newEntry;
            }
        }
        ,
        WEAK_EVICTABLE{

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new WeakEvictableEntry(segment.keyReferenceQueue, key, hash, next);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                this.copyEvictableEntry(original, newEntry);
                return newEntry;
            }
        }
        ,
        WEAK_EXPIRABLE_EVICTABLE{

            @Override
            <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> segment, K key, int hash, @Nullable ReferenceEntry<K, V> next) {
                return new WeakExpirableEvictableEntry(segment.keyReferenceQueue, key, hash, next);
            }

            @Override
            <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                this.copyExpirableEntry(original, newEntry);
                this.copyEvictableEntry(original, newEntry);
                return newEntry;
            }
        };

        static final int EXPIRABLE_MASK = 1;
        static final int EVICTABLE_MASK = 2;
        static final EntryFactory[][] factories;

        static EntryFactory getFactory(Strength keyStrength, boolean expireAfterWrite, boolean evictsBySize) {
            int flags = (expireAfterWrite ? 1 : 0) | (evictsBySize ? 2 : 0);
            return factories[keyStrength.ordinal()][flags];
        }

        abstract <K, V> ReferenceEntry<K, V> newEntry(Segment<K, V> var1, K var2, int var3, @Nullable ReferenceEntry<K, V> var4);

        @GuardedBy(value="Segment.this")
        <K, V> ReferenceEntry<K, V> copyEntry(Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
            return this.newEntry(segment, original.getKey(), original.getHash(), newNext);
        }

        @GuardedBy(value="Segment.this")
        <K, V> void copyExpirableEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
            newEntry.setExpirationTime(original.getExpirationTime());
            MapMakerInternalMap.connectExpirables(original.getPreviousExpirable(), newEntry);
            MapMakerInternalMap.connectExpirables(newEntry, original.getNextExpirable());
            MapMakerInternalMap.nullifyExpirable(original);
        }

        @GuardedBy(value="Segment.this")
        <K, V> void copyEvictableEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
            MapMakerInternalMap.connectEvictables(original.getPreviousEvictable(), newEntry);
            MapMakerInternalMap.connectEvictables(newEntry, original.getNextEvictable());
            MapMakerInternalMap.nullifyEvictable(original);
        }

        static {
            factories = new EntryFactory[][]{{STRONG, STRONG_EXPIRABLE, STRONG_EVICTABLE, STRONG_EXPIRABLE_EVICTABLE}, new EntryFactory[0], {WEAK, WEAK_EXPIRABLE, WEAK_EVICTABLE, WEAK_EXPIRABLE_EVICTABLE}};
        }
    }

    static enum Strength {
        STRONG{

            @Override
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> entry, V value) {
                return new StrongValueReference(value);
            }

            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
        }
        ,
        SOFT{

            @Override
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> entry, V value) {
                return new SoftValueReference(segment.valueReferenceQueue, value, entry);
            }

            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        }
        ,
        WEAK{

            @Override
            <K, V> ValueReference<K, V> referenceValue(Segment<K, V> segment, ReferenceEntry<K, V> entry, V value) {
                return new WeakValueReference(segment.valueReferenceQueue, value, entry);
            }

            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };


        abstract <K, V> ValueReference<K, V> referenceValue(Segment<K, V> var1, ReferenceEntry<K, V> var2, V var3);

        abstract Equivalence<Object> defaultEquivalence();
    }
}

