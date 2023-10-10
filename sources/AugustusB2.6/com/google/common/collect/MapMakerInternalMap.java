// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.AbstractSet;
import java.util.AbstractCollection;
import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;
import java.lang.ref.Reference;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import com.google.j2objc.annotations.Weak;
import java.lang.ref.WeakReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Iterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;
import java.util.concurrent.atomic.AtomicReferenceArray;
import com.google.common.primitives.Ints;
import com.google.common.annotations.VisibleForTesting;
import java.util.Map;
import java.util.Collection;
import java.util.Set;
import com.google.common.base.Equivalence;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;
import java.util.AbstractMap;

@GwtIncompatible
class MapMakerInternalMap<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable
{
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final int DRAIN_THRESHOLD = 63;
    static final int DRAIN_MAX = 16;
    static final long CLEANUP_EXECUTOR_DELAY_SECS = 60L;
    final transient int segmentMask;
    final transient int segmentShift;
    final transient Segment<K, V, E, S>[] segments;
    final int concurrencyLevel;
    final Equivalence<Object> keyEquivalence;
    final transient InternalEntryHelper<K, V, E, S> entryHelper;
    static final WeakValueReference<Object, Object, DummyInternalEntry> UNSET_WEAK_VALUE_REFERENCE;
    transient Set<K> keySet;
    transient Collection<V> values;
    transient Set<Map.Entry<K, V>> entrySet;
    private static final long serialVersionUID = 5L;
    
    private MapMakerInternalMap(final MapMaker builder, final InternalEntryHelper<K, V, E, S> entryHelper) {
        this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
        this.keyEquivalence = builder.getKeyEquivalence();
        this.entryHelper = entryHelper;
        final int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
        int segmentShift = 0;
        int segmentCount;
        for (segmentCount = 1; segmentCount < this.concurrencyLevel; segmentCount <<= 1) {
            ++segmentShift;
        }
        this.segmentShift = 32 - segmentShift;
        this.segmentMask = segmentCount - 1;
        this.segments = this.newSegmentArray(segmentCount);
        int segmentCapacity = initialCapacity / segmentCount;
        if (segmentCapacity * segmentCount < initialCapacity) {
            ++segmentCapacity;
        }
        int segmentSize;
        for (segmentSize = 1; segmentSize < segmentCapacity; segmentSize <<= 1) {}
        for (int i = 0; i < this.segments.length; ++i) {
            this.segments[i] = this.createSegment(segmentSize, -1);
        }
    }
    
    static <K, V> MapMakerInternalMap<K, V, ? extends InternalEntry<K, V, ?>, ?> create(final MapMaker builder) {
        if (builder.getKeyStrength() == Strength.STRONG && builder.getValueStrength() == Strength.STRONG) {
            return new MapMakerInternalMap<K, V, InternalEntry<K, V, ?>, Object>(builder, (InternalEntryHelper<K, V, ? extends InternalEntry<K, V, ?>, ?>)StrongKeyStrongValueEntry.Helper.instance());
        }
        if (builder.getKeyStrength() == Strength.STRONG && builder.getValueStrength() == Strength.WEAK) {
            return new MapMakerInternalMap<K, V, InternalEntry<K, V, ?>, Object>(builder, (InternalEntryHelper<K, V, ? extends InternalEntry<K, V, ?>, ?>)StrongKeyWeakValueEntry.Helper.instance());
        }
        if (builder.getKeyStrength() == Strength.WEAK && builder.getValueStrength() == Strength.STRONG) {
            return new MapMakerInternalMap<K, V, InternalEntry<K, V, ?>, Object>(builder, (InternalEntryHelper<K, V, ? extends InternalEntry<K, V, ?>, ?>)WeakKeyStrongValueEntry.Helper.instance());
        }
        if (builder.getKeyStrength() == Strength.WEAK && builder.getValueStrength() == Strength.WEAK) {
            return new MapMakerInternalMap<K, V, InternalEntry<K, V, ?>, Object>(builder, (InternalEntryHelper<K, V, ? extends InternalEntry<K, V, ?>, ?>)WeakKeyWeakValueEntry.Helper.instance());
        }
        throw new AssertionError();
    }
    
    static <K> MapMakerInternalMap<K, MapMaker.Dummy, ? extends InternalEntry<K, MapMaker.Dummy, ?>, ?> createWithDummyValues(final MapMaker builder) {
        if (builder.getKeyStrength() == Strength.STRONG && builder.getValueStrength() == Strength.STRONG) {
            return new MapMakerInternalMap<K, MapMaker.Dummy, InternalEntry<K, MapMaker.Dummy, ?>, Object>(builder, (InternalEntryHelper<K, MapMaker.Dummy, ? extends InternalEntry<K, MapMaker.Dummy, ?>, ?>)StrongKeyDummyValueEntry.Helper.instance());
        }
        if (builder.getKeyStrength() == Strength.WEAK && builder.getValueStrength() == Strength.STRONG) {
            return new MapMakerInternalMap<K, MapMaker.Dummy, InternalEntry<K, MapMaker.Dummy, ?>, Object>(builder, (InternalEntryHelper<K, MapMaker.Dummy, ? extends InternalEntry<K, MapMaker.Dummy, ?>, ?>)WeakKeyDummyValueEntry.Helper.instance());
        }
        if (builder.getValueStrength() == Strength.WEAK) {
            throw new IllegalArgumentException("Map cannot have both weak and dummy values");
        }
        throw new AssertionError();
    }
    
    static <K, V, E extends InternalEntry<K, V, E>> WeakValueReference<K, V, E> unsetWeakValueReference() {
        return (WeakValueReference<K, V, E>)MapMakerInternalMap.UNSET_WEAK_VALUE_REFERENCE;
    }
    
    static int rehash(int h) {
        h += (h << 15 ^ 0xFFFFCD7D);
        h ^= h >>> 10;
        h += h << 3;
        h ^= h >>> 6;
        h += (h << 2) + (h << 14);
        return h ^ h >>> 16;
    }
    
    @VisibleForTesting
    E copyEntry(final E original, final E newNext) {
        final int hash = original.getHash();
        return this.segmentFor(hash).copyEntry(original, newNext);
    }
    
    int hash(final Object key) {
        final int h = this.keyEquivalence.hash(key);
        return rehash(h);
    }
    
    void reclaimValue(final WeakValueReference<K, V, E> valueReference) {
        final E entry = valueReference.getEntry();
        final int hash = entry.getHash();
        this.segmentFor(hash).reclaimValue(((InternalEntry<K, V, E>)entry).getKey(), hash, valueReference);
    }
    
    void reclaimKey(final E entry) {
        final int hash = entry.getHash();
        this.segmentFor(hash).reclaimKey(entry, hash);
    }
    
    @VisibleForTesting
    boolean isLiveForTesting(final InternalEntry<K, V, ?> entry) {
        return this.segmentFor(entry.getHash()).getLiveValueForTesting(entry) != null;
    }
    
    Segment<K, V, E, S> segmentFor(final int hash) {
        return this.segments[hash >>> this.segmentShift & this.segmentMask];
    }
    
    Segment<K, V, E, S> createSegment(final int initialCapacity, final int maxSegmentSize) {
        return this.entryHelper.newSegment(this, initialCapacity, maxSegmentSize);
    }
    
    V getLiveValue(final E entry) {
        if (entry.getKey() == null) {
            return null;
        }
        return ((InternalEntry<K, V, E>)entry).getValue();
    }
    
    final Segment<K, V, E, S>[] newSegmentArray(final int ssize) {
        return (Segment<K, V, E, S>[])new Segment[ssize];
    }
    
    @VisibleForTesting
    Strength keyStrength() {
        return this.entryHelper.keyStrength();
    }
    
    @VisibleForTesting
    Strength valueStrength() {
        return this.entryHelper.valueStrength();
    }
    
    @VisibleForTesting
    Equivalence<Object> valueEquivalence() {
        return this.entryHelper.valueStrength().defaultEquivalence();
    }
    
    @Override
    public boolean isEmpty() {
        long sum = 0L;
        final Segment<K, V, E, S>[] segments = this.segments;
        for (int i = 0; i < segments.length; ++i) {
            if (segments[i].count != 0) {
                return false;
            }
            sum += segments[i].modCount;
        }
        if (sum != 0L) {
            for (int i = 0; i < segments.length; ++i) {
                if (segments[i].count != 0) {
                    return false;
                }
                sum -= segments[i].modCount;
            }
            return sum == 0L;
        }
        return true;
    }
    
    @Override
    public int size() {
        final Segment<K, V, E, S>[] segments = this.segments;
        long sum = 0L;
        for (int i = 0; i < segments.length; ++i) {
            sum += segments[i].count;
        }
        return Ints.saturatedCast(sum);
    }
    
    @Override
    public V get(final Object key) {
        if (key == null) {
            return null;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).get(key, hash);
    }
    
    E getEntry(final Object key) {
        if (key == null) {
            return null;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).getEntry(key, hash);
    }
    
    @Override
    public boolean containsKey(final Object key) {
        if (key == null) {
            return false;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).containsKey(key, hash);
    }
    
    @Override
    public boolean containsValue(final Object value) {
        if (value == null) {
            return false;
        }
        final Segment<K, V, E, S>[] segments = this.segments;
        long last = -1L;
        for (int i = 0; i < 3; ++i) {
            long sum = 0L;
            for (final Segment<K, V, E, S> segment : segments) {
                final int unused = segment.count;
                final AtomicReferenceArray<E> table = segment.table;
                for (int j = 0; j < table.length(); ++j) {
                    for (E e = table.get(j); e != null; e = ((InternalEntry<K, V, E>)e).getNext()) {
                        final V v = segment.getLiveValue(e);
                        if (v != null && this.valueEquivalence().equivalent(value, v)) {
                            return true;
                        }
                    }
                }
                sum += segment.modCount;
            }
            if (sum == last) {
                break;
            }
            last = sum;
        }
        return false;
    }
    
    @CanIgnoreReturnValue
    @Override
    public V put(final K key, final V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        final int hash = this.hash(key);
        return this.segmentFor(hash).put(key, hash, value, false);
    }
    
    @CanIgnoreReturnValue
    @Override
    public V putIfAbsent(final K key, final V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        final int hash = this.hash(key);
        return this.segmentFor(hash).put(key, hash, value, true);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        for (final Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            this.put(e.getKey(), e.getValue());
        }
    }
    
    @CanIgnoreReturnValue
    @Override
    public V remove(final Object key) {
        if (key == null) {
            return null;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).remove(key, hash);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean remove(final Object key, final Object value) {
        if (key == null || value == null) {
            return false;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).remove(key, hash, value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public boolean replace(final K key, final V oldValue, final V newValue) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(newValue);
        if (oldValue == null) {
            return false;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).replace(key, hash, oldValue, newValue);
    }
    
    @CanIgnoreReturnValue
    @Override
    public V replace(final K key, final V value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        final int hash = this.hash(key);
        return this.segmentFor(hash).replace(key, hash, value);
    }
    
    @Override
    public void clear() {
        for (final Segment<K, V, E, S> segment : this.segments) {
            segment.clear();
        }
    }
    
    @Override
    public Set<K> keySet() {
        final Set<K> ks = this.keySet;
        return (ks != null) ? ks : (this.keySet = new KeySet());
    }
    
    @Override
    public Collection<V> values() {
        final Collection<V> vs = this.values;
        return (vs != null) ? vs : (this.values = new Values());
    }
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final Set<Map.Entry<K, V>> es = this.entrySet;
        return (es != null) ? es : (this.entrySet = new EntrySet());
    }
    
    private static <E> ArrayList<E> toArrayList(final Collection<E> c) {
        final ArrayList<E> result = new ArrayList<E>(c.size());
        Iterators.addAll(result, (Iterator<? extends E>)c.iterator());
        return result;
    }
    
    Object writeReplace() {
        return new SerializationProxy(this.entryHelper.keyStrength(), this.entryHelper.valueStrength(), this.keyEquivalence, this.entryHelper.valueStrength().defaultEquivalence(), this.concurrencyLevel, (ConcurrentMap<Object, Object>)this);
    }
    
    static {
        UNSET_WEAK_VALUE_REFERENCE = new WeakValueReference<Object, Object, DummyInternalEntry>() {
            @Override
            public DummyInternalEntry getEntry() {
                return null;
            }
            
            @Override
            public void clear() {
            }
            
            @Override
            public Object get() {
                return null;
            }
            
            @Override
            public WeakValueReference<Object, Object, DummyInternalEntry> copyFor(final ReferenceQueue<Object> queue, final DummyInternalEntry entry) {
                return this;
            }
        };
    }
    
    enum Strength
    {
        STRONG(0) {
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
        }, 
        WEAK(1) {
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };
        
        abstract Equivalence<Object> defaultEquivalence();
        
        private static /* synthetic */ Strength[] $values() {
            return new Strength[] { Strength.STRONG, Strength.WEAK };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    abstract static class AbstractStrongKeyEntry<K, V, E extends InternalEntry<K, V, E>> implements InternalEntry<K, V, E>
    {
        final K key;
        final int hash;
        final E next;
        
        AbstractStrongKeyEntry(final K key, final int hash, final E next) {
            this.key = key;
            this.hash = hash;
            this.next = next;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public int getHash() {
            return this.hash;
        }
        
        @Override
        public E getNext() {
            return this.next;
        }
    }
    
    static final class StrongKeyStrongValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, StrongKeyStrongValueEntry<K, V>>
    {
        private volatile V value;
        
        StrongKeyStrongValueEntry(final K key, final int hash, final StrongKeyStrongValueEntry<K, V> next) {
            super(key, hash, next);
            this.value = null;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        void setValue(final V value) {
            this.value = value;
        }
        
        StrongKeyStrongValueEntry<K, V> copy(final StrongKeyStrongValueEntry<K, V> newNext) {
            final StrongKeyStrongValueEntry<K, V> newEntry = new StrongKeyStrongValueEntry<K, V>((K)this.key, this.hash, newNext);
            newEntry.value = this.value;
            return newEntry;
        }
        
        static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>>
        {
            private static final Helper<?, ?> INSTANCE;
            
            static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>)Helper.INSTANCE;
            }
            
            @Override
            public Strength keyStrength() {
                return Strength.STRONG;
            }
            
            @Override
            public Strength valueStrength() {
                return Strength.STRONG;
            }
            
            @Override
            public StrongKeyStrongValueSegment<K, V> newSegment(final MapMakerInternalMap<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> map, final int initialCapacity, final int maxSegmentSize) {
                return new StrongKeyStrongValueSegment<K, V>(map, initialCapacity, maxSegmentSize);
            }
            
            @Override
            public StrongKeyStrongValueEntry<K, V> copy(final StrongKeyStrongValueSegment<K, V> segment, final StrongKeyStrongValueEntry<K, V> entry, final StrongKeyStrongValueEntry<K, V> newNext) {
                return entry.copy(newNext);
            }
            
            @Override
            public void setValue(final StrongKeyStrongValueSegment<K, V> segment, final StrongKeyStrongValueEntry<K, V> entry, final V value) {
                entry.setValue(value);
            }
            
            @Override
            public StrongKeyStrongValueEntry<K, V> newEntry(final StrongKeyStrongValueSegment<K, V> segment, final K key, final int hash, final StrongKeyStrongValueEntry<K, V> next) {
                return new StrongKeyStrongValueEntry<K, V>(key, hash, next);
            }
            
            static {
                INSTANCE = new Helper<Object, Object>();
            }
        }
    }
    
    static final class StrongKeyWeakValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, StrongKeyWeakValueEntry<K, V>>
    {
        private volatile WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> valueReference;
        
        StrongKeyWeakValueEntry(final K key, final int hash, final StrongKeyWeakValueEntry<K, V> next) {
            super(key, hash, next);
            this.valueReference = MapMakerInternalMap.unsetWeakValueReference();
        }
        
        @Override
        public V getValue() {
            return this.valueReference.get();
        }
        
        @Override
        public void clearValue() {
            this.valueReference.clear();
        }
        
        void setValue(final V value, final ReferenceQueue<V> queueForValues) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     4: astore_3        /* previous */
            //     5: aload_0         /* this */
            //     6: new             Lcom/google/common/collect/MapMakerInternalMap$WeakValueReferenceImpl;
            //     9: dup            
            //    10: aload_2         /* queueForValues */
            //    11: aload_1         /* value */
            //    12: aload_0         /* this */
            //    13: invokespecial   com/google/common/collect/MapMakerInternalMap$WeakValueReferenceImpl.<init>:(Ljava/lang/ref/ReferenceQueue;Ljava/lang/Object;Lcom/google/common/collect/MapMakerInternalMap$InternalEntry;)V
            //    16: putfield        com/google/common/collect/MapMakerInternalMap$StrongKeyWeakValueEntry.valueReference:Lcom/google/common/collect/MapMakerInternalMap$WeakValueReference;
            //    19: aload_3         /* previous */
            //    20: invokeinterface com/google/common/collect/MapMakerInternalMap$WeakValueReference.clear:()V
            //    25: return         
            //    Signature:
            //  (TV;Ljava/lang/ref/ReferenceQueue<TV;>;)V
            // 
            // The error that occurred was:
            // 
            // com.strobel.assembler.metadata.MetadataHelper$AdaptFailure
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitGenericParameter(MetadataHelper.java:2300)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitGenericParameter(MetadataHelper.java:2221)
            //     at com.strobel.assembler.metadata.GenericParameter.accept(GenericParameter.java:85)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2255)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2232)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitClassType(MetadataHelper.java:2239)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitClassType(MetadataHelper.java:2221)
            //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2255)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2232)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2245)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2221)
            //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:932)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1061)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.invalidateDependentExpressions(TypeAnalysis.java:759)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1011)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        StrongKeyWeakValueEntry<K, V> copy(final ReferenceQueue<V> queueForValues, final StrongKeyWeakValueEntry<K, V> newNext) {
            final StrongKeyWeakValueEntry<K, V> newEntry = new StrongKeyWeakValueEntry<K, V>((K)this.key, this.hash, newNext);
            newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
            return newEntry;
        }
        
        @Override
        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getValueReference() {
            return this.valueReference;
        }
        
        static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>>
        {
            private static final Helper<?, ?> INSTANCE;
            
            static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>)Helper.INSTANCE;
            }
            
            @Override
            public Strength keyStrength() {
                return Strength.STRONG;
            }
            
            @Override
            public Strength valueStrength() {
                return Strength.WEAK;
            }
            
            @Override
            public StrongKeyWeakValueSegment<K, V> newSegment(final MapMakerInternalMap<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> map, final int initialCapacity, final int maxSegmentSize) {
                return new StrongKeyWeakValueSegment<K, V>(map, initialCapacity, maxSegmentSize);
            }
            
            @Override
            public StrongKeyWeakValueEntry<K, V> copy(final StrongKeyWeakValueSegment<K, V> segment, final StrongKeyWeakValueEntry<K, V> entry, final StrongKeyWeakValueEntry<K, V> newNext) {
                if (Segment.isCollected(entry)) {
                    return null;
                }
                return entry.copy(((StrongKeyWeakValueSegment<Object, Object>)segment).queueForValues, newNext);
            }
            
            @Override
            public void setValue(final StrongKeyWeakValueSegment<K, V> segment, final StrongKeyWeakValueEntry<K, V> entry, final V value) {
                entry.setValue(value, ((StrongKeyWeakValueSegment<Object, Object>)segment).queueForValues);
            }
            
            @Override
            public StrongKeyWeakValueEntry<K, V> newEntry(final StrongKeyWeakValueSegment<K, V> segment, final K key, final int hash, final StrongKeyWeakValueEntry<K, V> next) {
                return new StrongKeyWeakValueEntry<K, V>(key, hash, next);
            }
            
            static {
                INSTANCE = new Helper<Object, Object>();
            }
        }
    }
    
    static final class StrongKeyDummyValueEntry<K> extends AbstractStrongKeyEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> implements StrongValueEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>>
    {
        StrongKeyDummyValueEntry(final K key, final int hash, final StrongKeyDummyValueEntry<K> next) {
            super(key, hash, next);
        }
        
        @Override
        public MapMaker.Dummy getValue() {
            return MapMaker.Dummy.VALUE;
        }
        
        void setValue(final MapMaker.Dummy value) {
        }
        
        StrongKeyDummyValueEntry<K> copy(final StrongKeyDummyValueEntry<K> newNext) {
            return new StrongKeyDummyValueEntry<K>((K)this.key, this.hash, newNext);
        }
        
        static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>>
        {
            private static final Helper<?> INSTANCE;
            
            static <K> Helper<K> instance() {
                return (Helper<K>)Helper.INSTANCE;
            }
            
            @Override
            public Strength keyStrength() {
                return Strength.STRONG;
            }
            
            @Override
            public Strength valueStrength() {
                return Strength.STRONG;
            }
            
            @Override
            public StrongKeyDummyValueSegment<K> newSegment(final MapMakerInternalMap<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> map, final int initialCapacity, final int maxSegmentSize) {
                return new StrongKeyDummyValueSegment<K>(map, initialCapacity, maxSegmentSize);
            }
            
            @Override
            public StrongKeyDummyValueEntry<K> copy(final StrongKeyDummyValueSegment<K> segment, final StrongKeyDummyValueEntry<K> entry, final StrongKeyDummyValueEntry<K> newNext) {
                return entry.copy(newNext);
            }
            
            @Override
            public void setValue(final StrongKeyDummyValueSegment<K> segment, final StrongKeyDummyValueEntry<K> entry, final MapMaker.Dummy value) {
            }
            
            @Override
            public StrongKeyDummyValueEntry<K> newEntry(final StrongKeyDummyValueSegment<K> segment, final K key, final int hash, final StrongKeyDummyValueEntry<K> next) {
                return new StrongKeyDummyValueEntry<K>(key, hash, next);
            }
            
            static {
                INSTANCE = new Helper<Object>();
            }
        }
    }
    
    abstract static class AbstractWeakKeyEntry<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<K> implements InternalEntry<K, V, E>
    {
        final int hash;
        final E next;
        
        AbstractWeakKeyEntry(final ReferenceQueue<K> queue, final K key, final int hash, final E next) {
            super(key, queue);
            this.hash = hash;
            this.next = next;
        }
        
        @Override
        public K getKey() {
            return this.get();
        }
        
        @Override
        public int getHash() {
            return this.hash;
        }
        
        @Override
        public E getNext() {
            return this.next;
        }
    }
    
    static final class WeakKeyDummyValueEntry<K> extends AbstractWeakKeyEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>> implements StrongValueEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>>
    {
        WeakKeyDummyValueEntry(final ReferenceQueue<K> queue, final K key, final int hash, final WeakKeyDummyValueEntry<K> next) {
            super(queue, key, hash, next);
        }
        
        @Override
        public MapMaker.Dummy getValue() {
            return MapMaker.Dummy.VALUE;
        }
        
        void setValue(final MapMaker.Dummy value) {
        }
        
        WeakKeyDummyValueEntry<K> copy(final ReferenceQueue<K> queueForKeys, final WeakKeyDummyValueEntry<K> newNext) {
            return new WeakKeyDummyValueEntry<K>(queueForKeys, this.getKey(), this.hash, newNext);
        }
        
        static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>>
        {
            private static final Helper<?> INSTANCE;
            
            static <K> Helper<K> instance() {
                return (Helper<K>)Helper.INSTANCE;
            }
            
            @Override
            public Strength keyStrength() {
                return Strength.WEAK;
            }
            
            @Override
            public Strength valueStrength() {
                return Strength.STRONG;
            }
            
            @Override
            public WeakKeyDummyValueSegment<K> newSegment(final MapMakerInternalMap<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> map, final int initialCapacity, final int maxSegmentSize) {
                return new WeakKeyDummyValueSegment<K>(map, initialCapacity, maxSegmentSize);
            }
            
            @Override
            public WeakKeyDummyValueEntry<K> copy(final WeakKeyDummyValueSegment<K> segment, final WeakKeyDummyValueEntry<K> entry, final WeakKeyDummyValueEntry<K> newNext) {
                if (entry.getKey() == null) {
                    return null;
                }
                return entry.copy(((WeakKeyDummyValueSegment<Object>)segment).queueForKeys, newNext);
            }
            
            @Override
            public void setValue(final WeakKeyDummyValueSegment<K> segment, final WeakKeyDummyValueEntry<K> entry, final MapMaker.Dummy value) {
            }
            
            @Override
            public WeakKeyDummyValueEntry<K> newEntry(final WeakKeyDummyValueSegment<K> segment, final K key, final int hash, final WeakKeyDummyValueEntry<K> next) {
                return new WeakKeyDummyValueEntry<K>(((WeakKeyDummyValueSegment<Object>)segment).queueForKeys, key, hash, next);
            }
            
            static {
                INSTANCE = new Helper<Object>();
            }
        }
    }
    
    static final class WeakKeyStrongValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, WeakKeyStrongValueEntry<K, V>>
    {
        private volatile V value;
        
        WeakKeyStrongValueEntry(final ReferenceQueue<K> queue, final K key, final int hash, final WeakKeyStrongValueEntry<K, V> next) {
            super(queue, key, hash, next);
            this.value = null;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        void setValue(final V value) {
            this.value = value;
        }
        
        WeakKeyStrongValueEntry<K, V> copy(final ReferenceQueue<K> queueForKeys, final WeakKeyStrongValueEntry<K, V> newNext) {
            final WeakKeyStrongValueEntry<K, V> newEntry = new WeakKeyStrongValueEntry<K, V>(queueForKeys, this.getKey(), this.hash, newNext);
            newEntry.setValue(this.value);
            return newEntry;
        }
        
        static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>>
        {
            private static final Helper<?, ?> INSTANCE;
            
            static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>)Helper.INSTANCE;
            }
            
            @Override
            public Strength keyStrength() {
                return Strength.WEAK;
            }
            
            @Override
            public Strength valueStrength() {
                return Strength.STRONG;
            }
            
            @Override
            public WeakKeyStrongValueSegment<K, V> newSegment(final MapMakerInternalMap<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> map, final int initialCapacity, final int maxSegmentSize) {
                return new WeakKeyStrongValueSegment<K, V>(map, initialCapacity, maxSegmentSize);
            }
            
            @Override
            public WeakKeyStrongValueEntry<K, V> copy(final WeakKeyStrongValueSegment<K, V> segment, final WeakKeyStrongValueEntry<K, V> entry, final WeakKeyStrongValueEntry<K, V> newNext) {
                if (entry.getKey() == null) {
                    return null;
                }
                return entry.copy(((WeakKeyStrongValueSegment<Object, Object>)segment).queueForKeys, newNext);
            }
            
            @Override
            public void setValue(final WeakKeyStrongValueSegment<K, V> segment, final WeakKeyStrongValueEntry<K, V> entry, final V value) {
                entry.setValue(value);
            }
            
            @Override
            public WeakKeyStrongValueEntry<K, V> newEntry(final WeakKeyStrongValueSegment<K, V> segment, final K key, final int hash, final WeakKeyStrongValueEntry<K, V> next) {
                return new WeakKeyStrongValueEntry<K, V>(((WeakKeyStrongValueSegment<Object, Object>)segment).queueForKeys, key, hash, next);
            }
            
            static {
                INSTANCE = new Helper<Object, Object>();
            }
        }
    }
    
    static final class WeakKeyWeakValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, WeakKeyWeakValueEntry<K, V>>
    {
        private volatile WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> valueReference;
        
        WeakKeyWeakValueEntry(final ReferenceQueue<K> queue, final K key, final int hash, final WeakKeyWeakValueEntry<K, V> next) {
            super(queue, key, hash, next);
            this.valueReference = MapMakerInternalMap.unsetWeakValueReference();
        }
        
        @Override
        public V getValue() {
            return this.valueReference.get();
        }
        
        WeakKeyWeakValueEntry<K, V> copy(final ReferenceQueue<K> queueForKeys, final ReferenceQueue<V> queueForValues, final WeakKeyWeakValueEntry<K, V> newNext) {
            final WeakKeyWeakValueEntry<K, V> newEntry = new WeakKeyWeakValueEntry<K, V>(queueForKeys, this.getKey(), this.hash, newNext);
            newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
            return newEntry;
        }
        
        @Override
        public void clearValue() {
            this.valueReference.clear();
        }
        
        void setValue(final V value, final ReferenceQueue<V> queueForValues) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     4: astore_3        /* previous */
            //     5: aload_0         /* this */
            //     6: new             Lcom/google/common/collect/MapMakerInternalMap$WeakValueReferenceImpl;
            //     9: dup            
            //    10: aload_2         /* queueForValues */
            //    11: aload_1         /* value */
            //    12: aload_0         /* this */
            //    13: invokespecial   com/google/common/collect/MapMakerInternalMap$WeakValueReferenceImpl.<init>:(Ljava/lang/ref/ReferenceQueue;Ljava/lang/Object;Lcom/google/common/collect/MapMakerInternalMap$InternalEntry;)V
            //    16: putfield        com/google/common/collect/MapMakerInternalMap$WeakKeyWeakValueEntry.valueReference:Lcom/google/common/collect/MapMakerInternalMap$WeakValueReference;
            //    19: aload_3         /* previous */
            //    20: invokeinterface com/google/common/collect/MapMakerInternalMap$WeakValueReference.clear:()V
            //    25: return         
            //    Signature:
            //  (TV;Ljava/lang/ref/ReferenceQueue<TV;>;)V
            // 
            // The error that occurred was:
            // 
            // com.strobel.assembler.metadata.MetadataHelper$AdaptFailure
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitGenericParameter(MetadataHelper.java:2300)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitGenericParameter(MetadataHelper.java:2221)
            //     at com.strobel.assembler.metadata.GenericParameter.accept(GenericParameter.java:85)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2255)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2232)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitClassType(MetadataHelper.java:2239)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitClassType(MetadataHelper.java:2221)
            //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2255)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2232)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2245)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2221)
            //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:932)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1061)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.invalidateDependentExpressions(TypeAnalysis.java:759)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1011)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getValueReference() {
            return this.valueReference;
        }
        
        static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>>
        {
            private static final Helper<?, ?> INSTANCE;
            
            static <K, V> Helper<K, V> instance() {
                return (Helper<K, V>)Helper.INSTANCE;
            }
            
            @Override
            public Strength keyStrength() {
                return Strength.WEAK;
            }
            
            @Override
            public Strength valueStrength() {
                return Strength.WEAK;
            }
            
            @Override
            public WeakKeyWeakValueSegment<K, V> newSegment(final MapMakerInternalMap<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> map, final int initialCapacity, final int maxSegmentSize) {
                return new WeakKeyWeakValueSegment<K, V>(map, initialCapacity, maxSegmentSize);
            }
            
            @Override
            public WeakKeyWeakValueEntry<K, V> copy(final WeakKeyWeakValueSegment<K, V> segment, final WeakKeyWeakValueEntry<K, V> entry, final WeakKeyWeakValueEntry<K, V> newNext) {
                if (entry.getKey() == null) {
                    return null;
                }
                if (Segment.isCollected(entry)) {
                    return null;
                }
                return entry.copy(((WeakKeyWeakValueSegment<Object, Object>)segment).queueForKeys, ((WeakKeyWeakValueSegment<Object, Object>)segment).queueForValues, newNext);
            }
            
            @Override
            public void setValue(final WeakKeyWeakValueSegment<K, V> segment, final WeakKeyWeakValueEntry<K, V> entry, final V value) {
                entry.setValue(value, ((WeakKeyWeakValueSegment<Object, Object>)segment).queueForValues);
            }
            
            @Override
            public WeakKeyWeakValueEntry<K, V> newEntry(final WeakKeyWeakValueSegment<K, V> segment, final K key, final int hash, final WeakKeyWeakValueEntry<K, V> next) {
                return new WeakKeyWeakValueEntry<K, V>(((WeakKeyWeakValueSegment<Object, Object>)segment).queueForKeys, key, hash, next);
            }
            
            static {
                INSTANCE = new Helper<Object, Object>();
            }
        }
    }
    
    static final class DummyInternalEntry implements InternalEntry<Object, Object, DummyInternalEntry>
    {
        private DummyInternalEntry() {
            throw new AssertionError();
        }
        
        @Override
        public DummyInternalEntry getNext() {
            throw new AssertionError();
        }
        
        @Override
        public int getHash() {
            throw new AssertionError();
        }
        
        @Override
        public Object getKey() {
            throw new AssertionError();
        }
        
        @Override
        public Object getValue() {
            throw new AssertionError();
        }
    }
    
    static final class WeakValueReferenceImpl<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<V> implements WeakValueReference<K, V, E>
    {
        @Weak
        final E entry;
        
        WeakValueReferenceImpl(final ReferenceQueue<V> queue, final V referent, final E entry) {
            super(referent, queue);
            this.entry = entry;
        }
        
        @Override
        public E getEntry() {
            return this.entry;
        }
        
        @Override
        public WeakValueReference<K, V, E> copyFor(final ReferenceQueue<V> queue, final E entry) {
            return new WeakValueReferenceImpl((ReferenceQueue<Object>)queue, this.get(), entry);
        }
    }
    
    abstract static class Segment<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> extends ReentrantLock
    {
        @Weak
        final MapMakerInternalMap<K, V, E, S> map;
        volatile int count;
        int modCount;
        int threshold;
        volatile AtomicReferenceArray<E> table;
        final int maxSegmentSize;
        final AtomicInteger readCount;
        
        Segment(final MapMakerInternalMap<K, V, E, S> map, final int initialCapacity, final int maxSegmentSize) {
            this.readCount = new AtomicInteger();
            this.map = map;
            this.maxSegmentSize = maxSegmentSize;
            this.initTable(this.newEntryArray(initialCapacity));
        }
        
        abstract S self();
        
        @GuardedBy("this")
        void maybeDrainReferenceQueues() {
        }
        
        void maybeClearReferenceQueues() {
        }
        
        void setValue(final E entry, final V value) {
            this.map.entryHelper.setValue(this.self(), entry, value);
        }
        
        E copyEntry(final E original, final E newNext) {
            return this.map.entryHelper.copy(this.self(), original, newNext);
        }
        
        AtomicReferenceArray<E> newEntryArray(final int size) {
            return new AtomicReferenceArray<E>(size);
        }
        
        void initTable(final AtomicReferenceArray<E> newTable) {
            this.threshold = newTable.length() * 3 / 4;
            if (this.threshold == this.maxSegmentSize) {
                ++this.threshold;
            }
            this.table = newTable;
        }
        
        abstract E castForTesting(final InternalEntry<K, V, ?> p0);
        
        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            throw new AssertionError();
        }
        
        ReferenceQueue<V> getValueReferenceQueueForTesting() {
            throw new AssertionError();
        }
        
        WeakValueReference<K, V, E> getWeakValueReferenceForTesting(final InternalEntry<K, V, ?> entry) {
            throw new AssertionError();
        }
        
        WeakValueReference<K, V, E> newWeakValueReferenceForTesting(final InternalEntry<K, V, ?> entry, final V value) {
            throw new AssertionError();
        }
        
        void setWeakValueReferenceForTesting(final InternalEntry<K, V, ?> entry, final WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> valueReference) {
            throw new AssertionError();
        }
        
        void setTableEntryForTesting(final int i, final InternalEntry<K, V, ?> entry) {
            this.table.set(i, this.castForTesting(entry));
        }
        
        E copyForTesting(final InternalEntry<K, V, ?> entry, final InternalEntry<K, V, ?> newNext) {
            return this.map.entryHelper.copy(this.self(), this.castForTesting(entry), this.castForTesting(newNext));
        }
        
        void setValueForTesting(final InternalEntry<K, V, ?> entry, final V value) {
            this.map.entryHelper.setValue(this.self(), this.castForTesting(entry), value);
        }
        
        E newEntryForTesting(final K key, final int hash, final InternalEntry<K, V, ?> next) {
            return this.map.entryHelper.newEntry(this.self(), key, hash, this.castForTesting(next));
        }
        
        @CanIgnoreReturnValue
        boolean removeTableEntryForTesting(final InternalEntry<K, V, ?> entry) {
            return this.removeEntryForTesting(this.castForTesting(entry));
        }
        
        E removeFromChainForTesting(final InternalEntry<K, V, ?> first, final InternalEntry<K, V, ?> entry) {
            return this.removeFromChain(this.castForTesting(first), this.castForTesting(entry));
        }
        
        V getLiveValueForTesting(final InternalEntry<K, V, ?> entry) {
            return this.getLiveValue(this.castForTesting(entry));
        }
        
        void tryDrainReferenceQueues() {
            if (this.tryLock()) {
                try {
                    this.maybeDrainReferenceQueues();
                }
                finally {
                    this.unlock();
                }
            }
        }
        
        @GuardedBy("this")
        void drainKeyReferenceQueue(final ReferenceQueue<K> keyReferenceQueue) {
            int i = 0;
            Reference<? extends K> ref;
            while ((ref = keyReferenceQueue.poll()) != null) {
                final E entry = (E)(InternalEntry)ref;
                this.map.reclaimKey(entry);
                if (++i == 16) {
                    break;
                }
            }
        }
        
        @GuardedBy("this")
        void drainValueReferenceQueue(final ReferenceQueue<V> valueReferenceQueue) {
            int i = 0;
            Reference<? extends V> ref;
            while ((ref = valueReferenceQueue.poll()) != null) {
                final WeakValueReference<K, V, E> valueReference = (WeakValueReference<K, V, E>)(WeakValueReference)ref;
                this.map.reclaimValue(valueReference);
                if (++i == 16) {
                    break;
                }
            }
        }
        
         <T> void clearReferenceQueue(final ReferenceQueue<T> referenceQueue) {
            while (referenceQueue.poll() != null) {}
        }
        
        E getFirst(final int hash) {
            final AtomicReferenceArray<E> table = this.table;
            return table.get(hash & table.length() - 1);
        }
        
        E getEntry(final Object key, final int hash) {
            if (this.count != 0) {
                for (E e = this.getFirst(hash); e != null; e = ((InternalEntry<K, V, E>)e).getNext()) {
                    if (e.getHash() == hash) {
                        final K entryKey = ((InternalEntry<K, V, E>)e).getKey();
                        if (entryKey == null) {
                            this.tryDrainReferenceQueues();
                        }
                        else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
                            return e;
                        }
                    }
                }
            }
            return null;
        }
        
        E getLiveEntry(final Object key, final int hash) {
            return this.getEntry(key, hash);
        }
        
        V get(final Object key, final int hash) {
            try {
                final E e = this.getLiveEntry(key, hash);
                if (e == null) {
                    return null;
                }
                final V value = ((InternalEntry<K, V, E>)e).getValue();
                if (value == null) {
                    this.tryDrainReferenceQueues();
                }
                return value;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        boolean containsKey(final Object key, final int hash) {
            try {
                if (this.count != 0) {
                    final E e = this.getLiveEntry(key, hash);
                    return e != null && e.getValue() != null;
                }
                return false;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        @VisibleForTesting
        boolean containsValue(final Object value) {
            try {
                if (this.count != 0) {
                    final AtomicReferenceArray<E> table = this.table;
                    for (int length = table.length(), i = 0; i < length; ++i) {
                        for (E e = table.get(i); e != null; e = ((InternalEntry<K, V, E>)e).getNext()) {
                            final V entryValue = this.getLiveValue(e);
                            if (entryValue != null) {
                                if (this.map.valueEquivalence().equivalent(value, entryValue)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        V put(final K key, final int hash, final V value, final boolean onlyIfAbsent) {
            this.lock();
            try {
                this.preWriteCleanup();
                int newCount = this.count + 1;
                if (newCount > this.threshold) {
                    this.expand();
                    newCount = this.count + 1;
                }
                final AtomicReferenceArray<E> table = this.table;
                final int index = hash & table.length() - 1;
                E e;
                final E first = e = table.get(index);
                while (e != null) {
                    final K entryKey = ((InternalEntry<K, V, E>)e).getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final V entryValue = ((InternalEntry<K, V, E>)e).getValue();
                        if (entryValue == null) {
                            ++this.modCount;
                            this.setValue(e, value);
                            newCount = this.count;
                            this.count = newCount;
                            return null;
                        }
                        if (onlyIfAbsent) {
                            return entryValue;
                        }
                        ++this.modCount;
                        this.setValue(e, value);
                        return entryValue;
                    }
                    else {
                        e = ((InternalEntry<K, V, E>)e).getNext();
                    }
                }
                ++this.modCount;
                final E newEntry = this.map.entryHelper.newEntry(this.self(), key, hash, first);
                this.setValue(newEntry, value);
                table.set(index, newEntry);
                this.count = newCount;
                return null;
            }
            finally {
                this.unlock();
            }
        }
        
        @GuardedBy("this")
        void expand() {
            final AtomicReferenceArray<E> oldTable = this.table;
            final int oldCapacity = oldTable.length();
            if (oldCapacity >= 1073741824) {
                return;
            }
            int newCount = this.count;
            final AtomicReferenceArray<E> newTable = this.newEntryArray(oldCapacity << 1);
            this.threshold = newTable.length() * 3 / 4;
            final int newMask = newTable.length() - 1;
            for (int oldIndex = 0; oldIndex < oldCapacity; ++oldIndex) {
                final E head = oldTable.get(oldIndex);
                if (head != null) {
                    final E next = ((InternalEntry<K, V, E>)head).getNext();
                    final int headIndex = head.getHash() & newMask;
                    if (next == null) {
                        newTable.set(headIndex, head);
                    }
                    else {
                        E tail = head;
                        int tailIndex = headIndex;
                        for (E e = next; e != null; e = ((InternalEntry<K, V, E>)e).getNext()) {
                            final int newIndex = e.getHash() & newMask;
                            if (newIndex != tailIndex) {
                                tailIndex = newIndex;
                                tail = e;
                            }
                        }
                        newTable.set(tailIndex, tail);
                        for (E e = head; e != tail; e = ((InternalEntry<K, V, E>)e).getNext()) {
                            final int newIndex = e.getHash() & newMask;
                            final E newNext = newTable.get(newIndex);
                            final E newFirst = this.copyEntry(e, newNext);
                            if (newFirst != null) {
                                newTable.set(newIndex, newFirst);
                            }
                            else {
                                --newCount;
                            }
                        }
                    }
                }
            }
            this.table = newTable;
            this.count = newCount;
        }
        
        boolean replace(final K key, final int hash, final V oldValue, final V newValue) {
            this.lock();
            try {
                this.preWriteCleanup();
                final AtomicReferenceArray<E> table = this.table;
                final int index = hash & table.length() - 1;
                E e;
                final E first = e = table.get(index);
                while (e != null) {
                    final K entryKey = ((InternalEntry<K, V, E>)e).getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final V entryValue = ((InternalEntry<K, V, E>)e).getValue();
                        if (entryValue == null) {
                            if (isCollected(e)) {
                                int newCount = this.count - 1;
                                ++this.modCount;
                                final E newFirst = this.removeFromChain(first, e);
                                newCount = this.count - 1;
                                table.set(index, newFirst);
                                this.count = newCount;
                            }
                            return false;
                        }
                        if (this.map.valueEquivalence().equivalent(oldValue, entryValue)) {
                            ++this.modCount;
                            this.setValue(e, newValue);
                            return true;
                        }
                        return false;
                    }
                    else {
                        e = ((InternalEntry<K, V, E>)e).getNext();
                    }
                }
                return false;
            }
            finally {
                this.unlock();
            }
        }
        
        V replace(final K key, final int hash, final V newValue) {
            this.lock();
            try {
                this.preWriteCleanup();
                final AtomicReferenceArray<E> table = this.table;
                final int index = hash & table.length() - 1;
                E e;
                final E first = e = table.get(index);
                while (e != null) {
                    final K entryKey = ((InternalEntry<K, V, E>)e).getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final V entryValue = ((InternalEntry<K, V, E>)e).getValue();
                        if (entryValue == null) {
                            if (isCollected(e)) {
                                int newCount = this.count - 1;
                                ++this.modCount;
                                final E newFirst = this.removeFromChain(first, e);
                                newCount = this.count - 1;
                                table.set(index, newFirst);
                                this.count = newCount;
                            }
                            return null;
                        }
                        ++this.modCount;
                        this.setValue(e, newValue);
                        return entryValue;
                    }
                    else {
                        e = ((InternalEntry<K, V, E>)e).getNext();
                    }
                }
                return null;
            }
            finally {
                this.unlock();
            }
        }
        
        @CanIgnoreReturnValue
        V remove(final Object key, final int hash) {
            this.lock();
            try {
                this.preWriteCleanup();
                int newCount = this.count - 1;
                final AtomicReferenceArray<E> table = this.table;
                final int index = hash & table.length() - 1;
                E e;
                for (E first = e = table.get(index); e != null; e = ((InternalEntry<K, V, E>)e).getNext()) {
                    final K entryKey = ((InternalEntry<K, V, E>)e).getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final V entryValue = ((InternalEntry<K, V, E>)e).getValue();
                        if (entryValue == null) {
                            if (!isCollected(e)) {
                                return null;
                            }
                        }
                        ++this.modCount;
                        final E newFirst = this.removeFromChain(first, e);
                        newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount;
                        return entryValue;
                    }
                }
                return null;
            }
            finally {
                this.unlock();
            }
        }
        
        boolean remove(final Object key, final int hash, final Object value) {
            this.lock();
            try {
                this.preWriteCleanup();
                int newCount = this.count - 1;
                final AtomicReferenceArray<E> table = this.table;
                final int index = hash & table.length() - 1;
                E e;
                for (E first = e = table.get(index); e != null; e = ((InternalEntry<K, V, E>)e).getNext()) {
                    final K entryKey = ((InternalEntry<K, V, E>)e).getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final V entryValue = ((InternalEntry<K, V, E>)e).getValue();
                        boolean explicitRemoval = false;
                        if (this.map.valueEquivalence().equivalent(value, entryValue)) {
                            explicitRemoval = true;
                        }
                        else if (!isCollected(e)) {
                            return false;
                        }
                        ++this.modCount;
                        final E newFirst = this.removeFromChain(first, e);
                        newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount;
                        return explicitRemoval;
                    }
                }
                return false;
            }
            finally {
                this.unlock();
            }
        }
        
        void clear() {
            if (this.count != 0) {
                this.lock();
                try {
                    final AtomicReferenceArray<E> table = this.table;
                    for (int i = 0; i < table.length(); ++i) {
                        table.set(i, null);
                    }
                    this.maybeClearReferenceQueues();
                    this.readCount.set(0);
                    ++this.modCount;
                    this.count = 0;
                }
                finally {
                    this.unlock();
                }
            }
        }
        
        @GuardedBy("this")
        E removeFromChain(final E first, final E entry) {
            int newCount = this.count;
            E newFirst = ((InternalEntry<K, V, E>)entry).getNext();
            for (E e = first; e != entry; e = ((InternalEntry<K, V, E>)e).getNext()) {
                final E next = this.copyEntry(e, newFirst);
                if (next != null) {
                    newFirst = next;
                }
                else {
                    --newCount;
                }
            }
            this.count = newCount;
            return newFirst;
        }
        
        @CanIgnoreReturnValue
        boolean reclaimKey(final E entry, final int hash) {
            this.lock();
            try {
                int newCount = this.count - 1;
                final AtomicReferenceArray<E> table = this.table;
                final int index = hash & table.length() - 1;
                E e;
                for (E first = e = table.get(index); e != null; e = ((InternalEntry<K, V, E>)e).getNext()) {
                    if (e == entry) {
                        ++this.modCount;
                        final E newFirst = this.removeFromChain(first, e);
                        newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount;
                        return true;
                    }
                }
                return false;
            }
            finally {
                this.unlock();
            }
        }
        
        @CanIgnoreReturnValue
        boolean reclaimValue(final K key, final int hash, final WeakValueReference<K, V, E> valueReference) {
            this.lock();
            try {
                int newCount = this.count - 1;
                final AtomicReferenceArray<E> table = this.table;
                final int index = hash & table.length() - 1;
                E e;
                final E first = e = table.get(index);
                while (e != null) {
                    final K entryKey = ((InternalEntry<K, V, E>)e).getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final WeakValueReference<K, V, E> v = ((WeakValueEntry)e).getValueReference();
                        if (v == valueReference) {
                            ++this.modCount;
                            final E newFirst = this.removeFromChain(first, e);
                            newCount = this.count - 1;
                            table.set(index, newFirst);
                            this.count = newCount;
                            return true;
                        }
                        return false;
                    }
                    else {
                        e = ((InternalEntry<K, V, E>)e).getNext();
                    }
                }
                return false;
            }
            finally {
                this.unlock();
            }
        }
        
        @CanIgnoreReturnValue
        boolean clearValueForTesting(final K key, final int hash, final WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> valueReference) {
            this.lock();
            try {
                final AtomicReferenceArray<E> table = this.table;
                final int index = hash & table.length() - 1;
                E e;
                final E first = e = table.get(index);
                while (e != null) {
                    final K entryKey = ((InternalEntry<K, V, E>)e).getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final WeakValueReference<K, V, E> v = ((WeakValueEntry)e).getValueReference();
                        if (v == valueReference) {
                            final E newFirst = this.removeFromChain(first, e);
                            table.set(index, newFirst);
                            return true;
                        }
                        return false;
                    }
                    else {
                        e = ((InternalEntry<K, V, E>)e).getNext();
                    }
                }
                return false;
            }
            finally {
                this.unlock();
            }
        }
        
        @GuardedBy("this")
        boolean removeEntryForTesting(final E entry) {
            final int hash = entry.getHash();
            int newCount = this.count - 1;
            final AtomicReferenceArray<E> table = this.table;
            final int index = hash & table.length() - 1;
            E e;
            for (E first = e = table.get(index); e != null; e = ((InternalEntry<K, V, E>)e).getNext()) {
                if (e == entry) {
                    ++this.modCount;
                    final E newFirst = this.removeFromChain(first, e);
                    newCount = this.count - 1;
                    table.set(index, newFirst);
                    this.count = newCount;
                    return true;
                }
            }
            return false;
        }
        
        static <K, V, E extends InternalEntry<K, V, E>> boolean isCollected(final E entry) {
            return entry.getValue() == null;
        }
        
        V getLiveValue(final E entry) {
            if (entry.getKey() == null) {
                this.tryDrainReferenceQueues();
                return null;
            }
            final V value = ((InternalEntry<K, V, E>)entry).getValue();
            if (value == null) {
                this.tryDrainReferenceQueues();
                return null;
            }
            return value;
        }
        
        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 0x3F) == 0x0) {
                this.runCleanup();
            }
        }
        
        @GuardedBy("this")
        void preWriteCleanup() {
            this.runLockedCleanup();
        }
        
        void runCleanup() {
            this.runLockedCleanup();
        }
        
        void runLockedCleanup() {
            if (this.tryLock()) {
                try {
                    this.maybeDrainReferenceQueues();
                    this.readCount.set(0);
                }
                finally {
                    this.unlock();
                }
            }
        }
    }
    
    static final class StrongKeyStrongValueSegment<K, V> extends Segment<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>>
    {
        StrongKeyStrongValueSegment(final MapMakerInternalMap<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> map, final int initialCapacity, final int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
        }
        
        @Override
        StrongKeyStrongValueSegment<K, V> self() {
            return this;
        }
        
        public StrongKeyStrongValueEntry<K, V> castForTesting(final InternalEntry<K, V, ?> entry) {
            return (StrongKeyStrongValueEntry<K, V>)(StrongKeyStrongValueEntry)entry;
        }
    }
    
    static final class StrongKeyWeakValueSegment<K, V> extends Segment<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>>
    {
        private final ReferenceQueue<V> queueForValues;
        
        StrongKeyWeakValueSegment(final MapMakerInternalMap<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> map, final int initialCapacity, final int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
            this.queueForValues = new ReferenceQueue<V>();
        }
        
        @Override
        StrongKeyWeakValueSegment<K, V> self() {
            return this;
        }
        
        @Override
        ReferenceQueue<V> getValueReferenceQueueForTesting() {
            return this.queueForValues;
        }
        
        public StrongKeyWeakValueEntry<K, V> castForTesting(final InternalEntry<K, V, ?> entry) {
            return (StrongKeyWeakValueEntry<K, V>)(StrongKeyWeakValueEntry)entry;
        }
        
        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(final InternalEntry<K, V, ?> e) {
            return this.castForTesting(e).getValueReference();
        }
        
        public WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(final InternalEntry<K, V, ?> e, final V value) {
            return new WeakValueReferenceImpl<K, V, StrongKeyWeakValueEntry<K, V>>(this.queueForValues, value, this.castForTesting(e));
        }
        
        public void setWeakValueReferenceForTesting(final InternalEntry<K, V, ?> e, final WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> valueReference) {
            final StrongKeyWeakValueEntry<K, V> entry = this.castForTesting(e);
            final WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> newValueReference = (WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>>)valueReference;
            final WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> previous = (WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>>)((StrongKeyWeakValueEntry<Object, Object>)entry).valueReference;
            ((StrongKeyWeakValueEntry<Object, Object>)entry).valueReference = (WeakValueReference<Object, Object, StrongKeyWeakValueEntry<Object, Object>>)newValueReference;
            previous.clear();
        }
        
        @Override
        void maybeDrainReferenceQueues() {
            this.drainValueReferenceQueue(this.queueForValues);
        }
        
        @Override
        void maybeClearReferenceQueues() {
            this.clearReferenceQueue(this.queueForValues);
        }
    }
    
    static final class StrongKeyDummyValueSegment<K> extends Segment<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>>
    {
        StrongKeyDummyValueSegment(final MapMakerInternalMap<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> map, final int initialCapacity, final int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
        }
        
        @Override
        StrongKeyDummyValueSegment<K> self() {
            return this;
        }
        
        public StrongKeyDummyValueEntry<K> castForTesting(final InternalEntry<K, MapMaker.Dummy, ?> entry) {
            return (StrongKeyDummyValueEntry<K>)(StrongKeyDummyValueEntry)entry;
        }
    }
    
    static final class WeakKeyStrongValueSegment<K, V> extends Segment<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>>
    {
        private final ReferenceQueue<K> queueForKeys;
        
        WeakKeyStrongValueSegment(final MapMakerInternalMap<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> map, final int initialCapacity, final int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
            this.queueForKeys = new ReferenceQueue<K>();
        }
        
        @Override
        WeakKeyStrongValueSegment<K, V> self() {
            return this;
        }
        
        @Override
        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }
        
        public WeakKeyStrongValueEntry<K, V> castForTesting(final InternalEntry<K, V, ?> entry) {
            return (WeakKeyStrongValueEntry<K, V>)(WeakKeyStrongValueEntry)entry;
        }
        
        @Override
        void maybeDrainReferenceQueues() {
            this.drainKeyReferenceQueue(this.queueForKeys);
        }
        
        @Override
        void maybeClearReferenceQueues() {
            this.clearReferenceQueue(this.queueForKeys);
        }
    }
    
    static final class WeakKeyWeakValueSegment<K, V> extends Segment<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>>
    {
        private final ReferenceQueue<K> queueForKeys;
        private final ReferenceQueue<V> queueForValues;
        
        WeakKeyWeakValueSegment(final MapMakerInternalMap<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> map, final int initialCapacity, final int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
            this.queueForKeys = new ReferenceQueue<K>();
            this.queueForValues = new ReferenceQueue<V>();
        }
        
        @Override
        WeakKeyWeakValueSegment<K, V> self() {
            return this;
        }
        
        @Override
        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }
        
        @Override
        ReferenceQueue<V> getValueReferenceQueueForTesting() {
            return this.queueForValues;
        }
        
        public WeakKeyWeakValueEntry<K, V> castForTesting(final InternalEntry<K, V, ?> entry) {
            return (WeakKeyWeakValueEntry<K, V>)(WeakKeyWeakValueEntry)entry;
        }
        
        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(final InternalEntry<K, V, ?> e) {
            return this.castForTesting(e).getValueReference();
        }
        
        public WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(final InternalEntry<K, V, ?> e, final V value) {
            return new WeakValueReferenceImpl<K, V, WeakKeyWeakValueEntry<K, V>>(this.queueForValues, value, this.castForTesting(e));
        }
        
        public void setWeakValueReferenceForTesting(final InternalEntry<K, V, ?> e, final WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> valueReference) {
            final WeakKeyWeakValueEntry<K, V> entry = this.castForTesting(e);
            final WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> newValueReference = (WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>>)valueReference;
            final WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> previous = (WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>>)((WeakKeyWeakValueEntry<Object, Object>)entry).valueReference;
            ((WeakKeyWeakValueEntry<Object, Object>)entry).valueReference = (WeakValueReference<Object, Object, WeakKeyWeakValueEntry<Object, Object>>)newValueReference;
            previous.clear();
        }
        
        @Override
        void maybeDrainReferenceQueues() {
            this.drainKeyReferenceQueue(this.queueForKeys);
            this.drainValueReferenceQueue(this.queueForValues);
        }
        
        @Override
        void maybeClearReferenceQueues() {
            this.clearReferenceQueue(this.queueForKeys);
        }
    }
    
    static final class WeakKeyDummyValueSegment<K> extends Segment<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>>
    {
        private final ReferenceQueue<K> queueForKeys;
        
        WeakKeyDummyValueSegment(final MapMakerInternalMap<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> map, final int initialCapacity, final int maxSegmentSize) {
            super(map, initialCapacity, maxSegmentSize);
            this.queueForKeys = new ReferenceQueue<K>();
        }
        
        @Override
        WeakKeyDummyValueSegment<K> self() {
            return this;
        }
        
        @Override
        ReferenceQueue<K> getKeyReferenceQueueForTesting() {
            return this.queueForKeys;
        }
        
        public WeakKeyDummyValueEntry<K> castForTesting(final InternalEntry<K, MapMaker.Dummy, ?> entry) {
            return (WeakKeyDummyValueEntry<K>)(WeakKeyDummyValueEntry)entry;
        }
        
        @Override
        void maybeDrainReferenceQueues() {
            this.drainKeyReferenceQueue(this.queueForKeys);
        }
        
        @Override
        void maybeClearReferenceQueues() {
            this.clearReferenceQueue(this.queueForKeys);
        }
    }
    
    static final class CleanupMapTask implements Runnable
    {
        final WeakReference<MapMakerInternalMap<?, ?, ?, ?>> mapReference;
        
        public CleanupMapTask(final MapMakerInternalMap<?, ?, ?, ?> map) {
            this.mapReference = new WeakReference<MapMakerInternalMap<?, ?, ?, ?>>(map);
        }
        
        @Override
        public void run() {
            final MapMakerInternalMap<?, ?, ?, ?> map = this.mapReference.get();
            if (map == null) {
                throw new CancellationException();
            }
            for (final Segment<?, ?, ?, ?> segment : map.segments) {
                segment.runCleanup();
            }
        }
    }
    
    abstract class HashIterator<T> implements Iterator<T>
    {
        int nextSegmentIndex;
        int nextTableIndex;
        Segment<K, V, E, S> currentSegment;
        AtomicReferenceArray<E> currentTable;
        E nextEntry;
        WriteThroughEntry nextExternal;
        WriteThroughEntry lastReturned;
        
        HashIterator() {
            this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
            this.nextTableIndex = -1;
            this.advance();
        }
        
        @Override
        public abstract T next();
        
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
                if (this.currentSegment.count != 0) {
                    this.currentTable = this.currentSegment.table;
                    this.nextTableIndex = this.currentTable.length() - 1;
                    if (this.nextInTable()) {
                        return;
                    }
                    continue;
                }
            }
        }
        
        boolean nextInChain() {
            if (this.nextEntry != null) {
                this.nextEntry = ((InternalEntry<K, V, E>)this.nextEntry).getNext();
                while (this.nextEntry != null) {
                    if (this.advanceTo(this.nextEntry)) {
                        return true;
                    }
                    this.nextEntry = ((InternalEntry<K, V, E>)this.nextEntry).getNext();
                }
            }
            return false;
        }
        
        boolean nextInTable() {
            while (this.nextTableIndex >= 0) {
                final InternalEntry<K, V, E> nextEntry = this.currentTable.get(this.nextTableIndex--);
                this.nextEntry = (E)nextEntry;
                if (nextEntry != null && (this.advanceTo(this.nextEntry) || this.nextInChain())) {
                    return true;
                }
            }
            return false;
        }
        
        boolean advanceTo(final E entry) {
            try {
                final K key = ((InternalEntry<K, V, E>)entry).getKey();
                final V value = MapMakerInternalMap.this.getLiveValue(entry);
                if (value != null) {
                    this.nextExternal = new WriteThroughEntry(key, value);
                    return true;
                }
                return false;
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
    
    final class KeyIterator extends HashIterator<K>
    {
        KeyIterator(final MapMakerInternalMap this$0) {
            this$0.super();
        }
        
        @Override
        public K next() {
            return this.nextEntry().getKey();
        }
    }
    
    final class ValueIterator extends HashIterator<V>
    {
        ValueIterator(final MapMakerInternalMap this$0) {
            this$0.super();
        }
        
        @Override
        public V next() {
            return this.nextEntry().getValue();
        }
    }
    
    final class WriteThroughEntry extends AbstractMapEntry<K, V>
    {
        final K key;
        V value;
        
        WriteThroughEntry(final K key, final V value) {
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
        public boolean equals(final Object object) {
            if (object instanceof Map.Entry) {
                final Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
                return this.key.equals(that.getKey()) && this.value.equals(that.getValue());
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }
        
        @Override
        public V setValue(final V newValue) {
            final V oldValue = MapMakerInternalMap.this.put(this.key, newValue);
            this.value = newValue;
            return oldValue;
        }
    }
    
    final class EntryIterator extends HashIterator<Map.Entry<K, V>>
    {
        EntryIterator(final MapMakerInternalMap this$0) {
            this$0.super();
        }
        
        @Override
        public Map.Entry<K, V> next() {
            return this.nextEntry();
        }
    }
    
    final class KeySet extends SafeToArraySet<K>
    {
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
        public boolean contains(final Object o) {
            return MapMakerInternalMap.this.containsKey(o);
        }
        
        @Override
        public boolean remove(final Object o) {
            return MapMakerInternalMap.this.remove(o) != null;
        }
        
        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
    }
    
    final class Values extends AbstractCollection<V>
    {
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
        public boolean contains(final Object o) {
            return MapMakerInternalMap.this.containsValue(o);
        }
        
        @Override
        public void clear() {
            MapMakerInternalMap.this.clear();
        }
        
        @Override
        public Object[] toArray() {
            return toArrayList((Collection<Object>)this).toArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] a) {
            return toArrayList((Collection<Object>)this).toArray(a);
        }
    }
    
    final class EntrySet extends SafeToArraySet<Map.Entry<K, V>>
    {
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            final Object key = e.getKey();
            if (key == null) {
                return false;
            }
            final V v = MapMakerInternalMap.this.get(key);
            return v != null && MapMakerInternalMap.this.valueEquivalence().equivalent(e.getValue(), v);
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
            final Object key = e.getKey();
            return key != null && MapMakerInternalMap.this.remove(key, e.getValue());
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
    
    private abstract static class SafeToArraySet<E> extends AbstractSet<E>
    {
        @Override
        public Object[] toArray() {
            return toArrayList((Collection<Object>)this).toArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] a) {
            return toArrayList((Collection<Object>)this).toArray(a);
        }
    }
    
    abstract static class AbstractSerializationProxy<K, V> extends ForwardingConcurrentMap<K, V> implements Serializable
    {
        private static final long serialVersionUID = 3L;
        final Strength keyStrength;
        final Strength valueStrength;
        final Equivalence<Object> keyEquivalence;
        final Equivalence<Object> valueEquivalence;
        final int concurrencyLevel;
        transient ConcurrentMap<K, V> delegate;
        
        AbstractSerializationProxy(final Strength keyStrength, final Strength valueStrength, final Equivalence<Object> keyEquivalence, final Equivalence<Object> valueEquivalence, final int concurrencyLevel, final ConcurrentMap<K, V> delegate) {
            this.keyStrength = keyStrength;
            this.valueStrength = valueStrength;
            this.keyEquivalence = keyEquivalence;
            this.valueEquivalence = valueEquivalence;
            this.concurrencyLevel = concurrencyLevel;
            this.delegate = delegate;
        }
        
        @Override
        protected ConcurrentMap<K, V> delegate() {
            return this.delegate;
        }
        
        void writeMapTo(final ObjectOutputStream out) throws IOException {
            out.writeInt(this.delegate.size());
            for (final Map.Entry<K, V> entry : this.delegate.entrySet()) {
                out.writeObject(entry.getKey());
                out.writeObject(entry.getValue());
            }
            out.writeObject(null);
        }
        
        MapMaker readMapMaker(final ObjectInputStream in) throws IOException {
            final int size = in.readInt();
            return new MapMaker().initialCapacity(size).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).concurrencyLevel(this.concurrencyLevel);
        }
        
        void readEntries(final ObjectInputStream in) throws IOException, ClassNotFoundException {
            while (true) {
                final K key = (K)in.readObject();
                if (key == null) {
                    break;
                }
                final V value = (V)in.readObject();
                this.delegate.put(key, value);
            }
        }
    }
    
    private static final class SerializationProxy<K, V> extends AbstractSerializationProxy<K, V>
    {
        private static final long serialVersionUID = 3L;
        
        SerializationProxy(final Strength keyStrength, final Strength valueStrength, final Equivalence<Object> keyEquivalence, final Equivalence<Object> valueEquivalence, final int concurrencyLevel, final ConcurrentMap<K, V> delegate) {
            super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, concurrencyLevel, delegate);
        }
        
        private void writeObject(final ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            this.writeMapTo(out);
        }
        
        private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            final MapMaker mapMaker = this.readMapMaker(in);
            this.delegate = (ConcurrentMap<K, V>)mapMaker.makeMap();
            this.readEntries(in);
        }
        
        private Object readResolve() {
            return this.delegate;
        }
    }
    
    interface InternalEntry<K, V, E extends InternalEntry<K, V, E>>
    {
        E getNext();
        
        int getHash();
        
        K getKey();
        
        V getValue();
    }
    
    interface WeakValueReference<K, V, E extends InternalEntry<K, V, E>>
    {
        V get();
        
        E getEntry();
        
        void clear();
        
        WeakValueReference<K, V, E> copyFor(final ReferenceQueue<V> p0, final E p1);
    }
    
    interface WeakValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E>
    {
        WeakValueReference<K, V, E> getValueReference();
        
        void clearValue();
    }
    
    interface InternalEntryHelper<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>>
    {
        Strength keyStrength();
        
        Strength valueStrength();
        
        S newSegment(final MapMakerInternalMap<K, V, E, S> p0, final int p1, final int p2);
        
        E newEntry(final S p0, final K p1, final int p2, final E p3);
        
        E copy(final S p0, final E p1, final E p2);
        
        void setValue(final S p0, final E p1, final V p2);
    }
    
    interface StrongValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E>
    {
    }
}
