package com.google.common.collect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Equivalence;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
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

class MapMakerInternalMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
  static final int MAXIMUM_CAPACITY = 1073741824;
  
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
  
  MapMakerInternalMap(MapMaker builder) {
    this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
    this.keyStrength = builder.getKeyStrength();
    this.valueStrength = builder.getValueStrength();
    this.keyEquivalence = builder.getKeyEquivalence();
    this.valueEquivalence = this.valueStrength.defaultEquivalence();
    this.maximumSize = builder.maximumSize;
    this.expireAfterAccessNanos = builder.getExpireAfterAccessNanos();
    this.expireAfterWriteNanos = builder.getExpireAfterWriteNanos();
    this.entryFactory = EntryFactory.getFactory(this.keyStrength, expires(), evictsBySize());
    this.ticker = builder.getTicker();
    this.removalListener = builder.getRemovalListener();
    this.removalNotificationQueue = (this.removalListener == GenericMapMaker.NullListener.INSTANCE) ? discardingQueue() : new ConcurrentLinkedQueue<MapMaker.RemovalNotification<K, V>>();
    int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
    if (evictsBySize())
      initialCapacity = Math.min(initialCapacity, this.maximumSize); 
    int segmentShift = 0;
    int segmentCount = 1;
    while (segmentCount < this.concurrencyLevel && (!evictsBySize() || segmentCount * 2 <= this.maximumSize)) {
      segmentShift++;
      segmentCount <<= 1;
    } 
    this.segmentShift = 32 - segmentShift;
    this.segmentMask = segmentCount - 1;
    this.segments = newSegmentArray(segmentCount);
    int segmentCapacity = initialCapacity / segmentCount;
    if (segmentCapacity * segmentCount < initialCapacity)
      segmentCapacity++; 
    int segmentSize = 1;
    while (segmentSize < segmentCapacity)
      segmentSize <<= 1; 
    if (evictsBySize()) {
      int maximumSegmentSize = this.maximumSize / segmentCount + 1;
      int remainder = this.maximumSize % segmentCount;
      for (int i = 0; i < this.segments.length; i++) {
        if (i == remainder)
          maximumSegmentSize--; 
        this.segments[i] = createSegment(segmentSize, maximumSegmentSize);
      } 
    } else {
      for (int i = 0; i < this.segments.length; i++)
        this.segments[i] = createSegment(segmentSize, -1); 
    } 
  }
  
  boolean evictsBySize() {
    return (this.maximumSize != -1);
  }
  
  boolean expires() {
    return (expiresAfterWrite() || expiresAfterAccess());
  }
  
  boolean expiresAfterWrite() {
    return (this.expireAfterWriteNanos > 0L);
  }
  
  boolean expiresAfterAccess() {
    return (this.expireAfterAccessNanos > 0L);
  }
  
  boolean usesKeyReferences() {
    return (this.keyStrength != Strength.STRONG);
  }
  
  boolean usesValueReferences() {
    return (this.valueStrength != Strength.STRONG);
  }
  
  enum Strength {
    STRONG {
      <K, V> MapMakerInternalMap.ValueReference<K, V> referenceValue(MapMakerInternalMap.Segment<K, V> segment, MapMakerInternalMap.ReferenceEntry<K, V> entry, V value) {
        return new MapMakerInternalMap.StrongValueReference<K, V>(value);
      }
      
      Equivalence<Object> defaultEquivalence() {
        return Equivalence.equals();
      }
    },
    SOFT {
      <K, V> MapMakerInternalMap.ValueReference<K, V> referenceValue(MapMakerInternalMap.Segment<K, V> segment, MapMakerInternalMap.ReferenceEntry<K, V> entry, V value) {
        return new MapMakerInternalMap.SoftValueReference<K, V>(segment.valueReferenceQueue, value, entry);
      }
      
      Equivalence<Object> defaultEquivalence() {
        return Equivalence.identity();
      }
    },
    WEAK {
      <K, V> MapMakerInternalMap.ValueReference<K, V> referenceValue(MapMakerInternalMap.Segment<K, V> segment, MapMakerInternalMap.ReferenceEntry<K, V> entry, V value) {
        return new MapMakerInternalMap.WeakValueReference<K, V>(segment.valueReferenceQueue, value, entry);
      }
      
      Equivalence<Object> defaultEquivalence() {
        return Equivalence.identity();
      }
    };
    
    abstract <K, V> MapMakerInternalMap.ValueReference<K, V> referenceValue(MapMakerInternalMap.Segment<K, V> param1Segment, MapMakerInternalMap.ReferenceEntry<K, V> param1ReferenceEntry, V param1V);
    
    abstract Equivalence<Object> defaultEquivalence();
  }
  
  enum EntryFactory {
    STRONG {
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
        return new MapMakerInternalMap.StrongEntry<K, V>(key, hash, next);
      }
    },
    STRONG_EXPIRABLE {
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
        return new MapMakerInternalMap.StrongExpirableEntry<K, V>(key, hash, next);
      }
      
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> segment, MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newNext) {
        MapMakerInternalMap.ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
        copyExpirableEntry(original, newEntry);
        return newEntry;
      }
    },
    STRONG_EVICTABLE {
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
        return new MapMakerInternalMap.StrongEvictableEntry<K, V>(key, hash, next);
      }
      
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> segment, MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newNext) {
        MapMakerInternalMap.ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
        copyEvictableEntry(original, newEntry);
        return newEntry;
      }
    },
    STRONG_EXPIRABLE_EVICTABLE {
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
        return new MapMakerInternalMap.StrongExpirableEvictableEntry<K, V>(key, hash, next);
      }
      
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> segment, MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newNext) {
        MapMakerInternalMap.ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
        copyExpirableEntry(original, newEntry);
        copyEvictableEntry(original, newEntry);
        return newEntry;
      }
    },
    WEAK {
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
        return new MapMakerInternalMap.WeakEntry<K, V>(segment.keyReferenceQueue, key, hash, next);
      }
    },
    WEAK_EXPIRABLE {
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
        return new MapMakerInternalMap.WeakExpirableEntry<K, V>(segment.keyReferenceQueue, key, hash, next);
      }
      
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> segment, MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newNext) {
        MapMakerInternalMap.ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
        copyExpirableEntry(original, newEntry);
        return newEntry;
      }
    },
    WEAK_EVICTABLE {
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
        return new MapMakerInternalMap.WeakEvictableEntry<K, V>(segment.keyReferenceQueue, key, hash, next);
      }
      
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> segment, MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newNext) {
        MapMakerInternalMap.ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
        copyEvictableEntry(original, newEntry);
        return newEntry;
      }
    },
    WEAK_EXPIRABLE_EVICTABLE {
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
        return new MapMakerInternalMap.WeakExpirableEvictableEntry<K, V>(segment.keyReferenceQueue, key, hash, next);
      }
      
      <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> segment, MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newNext) {
        MapMakerInternalMap.ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
        copyExpirableEntry(original, newEntry);
        copyEvictableEntry(original, newEntry);
        return newEntry;
      }
    };
    
    static final int EXPIRABLE_MASK = 1;
    
    static final int EVICTABLE_MASK = 2;
    
    static final EntryFactory[][] factories = new EntryFactory[][] { { STRONG, STRONG_EXPIRABLE, STRONG_EVICTABLE, STRONG_EXPIRABLE_EVICTABLE }, {}, { WEAK, WEAK_EXPIRABLE, WEAK_EVICTABLE, WEAK_EXPIRABLE_EVICTABLE } };
    
    static {
    
    }
    
    static EntryFactory getFactory(MapMakerInternalMap.Strength keyStrength, boolean expireAfterWrite, boolean evictsBySize) {
      int flags = (expireAfterWrite ? 1 : 0) | (evictsBySize ? 2 : 0);
      return factories[keyStrength.ordinal()][flags];
    }
    
    @GuardedBy("Segment.this")
    <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> segment, MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newNext) {
      return newEntry(segment, original.getKey(), original.getHash(), newNext);
    }
    
    @GuardedBy("Segment.this")
    <K, V> void copyExpirableEntry(MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newEntry) {
      newEntry.setExpirationTime(original.getExpirationTime());
      MapMakerInternalMap.connectExpirables(original.getPreviousExpirable(), newEntry);
      MapMakerInternalMap.connectExpirables(newEntry, original.getNextExpirable());
      MapMakerInternalMap.nullifyExpirable(original);
    }
    
    @GuardedBy("Segment.this")
    <K, V> void copyEvictableEntry(MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newEntry) {
      MapMakerInternalMap.connectEvictables(original.getPreviousEvictable(), newEntry);
      MapMakerInternalMap.connectEvictables(newEntry, original.getNextEvictable());
      MapMakerInternalMap.nullifyEvictable(original);
    }
    
    abstract <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> param1Segment, K param1K, int param1Int, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> param1ReferenceEntry);
  }
  
  static final ValueReference<Object, Object> UNSET = new ValueReference<Object, Object>() {
      public Object get() {
        return null;
      }
      
      public MapMakerInternalMap.ReferenceEntry<Object, Object> getEntry() {
        return null;
      }
      
      public MapMakerInternalMap.ValueReference<Object, Object> copyFor(ReferenceQueue<Object> queue, @Nullable Object value, MapMakerInternalMap.ReferenceEntry<Object, Object> entry) {
        return this;
      }
      
      public boolean isComputingReference() {
        return false;
      }
      
      public Object waitForValue() {
        return null;
      }
      
      public void clear(MapMakerInternalMap.ValueReference<Object, Object> newValue) {}
    };
  
  static <K, V> ValueReference<K, V> unset() {
    return (ValueReference)UNSET;
  }
  
  private enum NullEntry implements ReferenceEntry<Object, Object> {
    INSTANCE;
    
    public MapMakerInternalMap.ValueReference<Object, Object> getValueReference() {
      return null;
    }
    
    public void setValueReference(MapMakerInternalMap.ValueReference<Object, Object> valueReference) {}
    
    public MapMakerInternalMap.ReferenceEntry<Object, Object> getNext() {
      return null;
    }
    
    public int getHash() {
      return 0;
    }
    
    public Object getKey() {
      return null;
    }
    
    public long getExpirationTime() {
      return 0L;
    }
    
    public void setExpirationTime(long time) {}
    
    public MapMakerInternalMap.ReferenceEntry<Object, Object> getNextExpirable() {
      return this;
    }
    
    public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<Object, Object> next) {}
    
    public MapMakerInternalMap.ReferenceEntry<Object, Object> getPreviousExpirable() {
      return this;
    }
    
    public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<Object, Object> previous) {}
    
    public MapMakerInternalMap.ReferenceEntry<Object, Object> getNextEvictable() {
      return this;
    }
    
    public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<Object, Object> next) {}
    
    public MapMakerInternalMap.ReferenceEntry<Object, Object> getPreviousEvictable() {
      return this;
    }
    
    public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<Object, Object> previous) {}
  }
  
  static abstract class AbstractReferenceEntry<K, V> implements ReferenceEntry<K, V> {
    public MapMakerInternalMap.ValueReference<K, V> getValueReference() {
      throw new UnsupportedOperationException();
    }
    
    public void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNext() {
      throw new UnsupportedOperationException();
    }
    
    public int getHash() {
      throw new UnsupportedOperationException();
    }
    
    public K getKey() {
      throw new UnsupportedOperationException();
    }
    
    public long getExpirationTime() {
      throw new UnsupportedOperationException();
    }
    
    public void setExpirationTime(long time) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
      throw new UnsupportedOperationException();
    }
    
    public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
      throw new UnsupportedOperationException();
    }
    
    public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
      throw new UnsupportedOperationException();
    }
    
    public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
      throw new UnsupportedOperationException();
    }
    
    public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      throw new UnsupportedOperationException();
    }
  }
  
  static <K, V> ReferenceEntry<K, V> nullEntry() {
    return NullEntry.INSTANCE;
  }
  
  static final Queue<? extends Object> DISCARDING_QUEUE = new AbstractQueue() {
      public boolean offer(Object o) {
        return true;
      }
      
      public Object peek() {
        return null;
      }
      
      public Object poll() {
        return null;
      }
      
      public int size() {
        return 0;
      }
      
      public Iterator<Object> iterator() {
        return Iterators.emptyIterator();
      }
    };
  
  transient Set<K> keySet;
  
  transient Collection<V> values;
  
  transient Set<Map.Entry<K, V>> entrySet;
  
  private static final long serialVersionUID = 5L;
  
  static <E> Queue<E> discardingQueue() {
    return (Queue)DISCARDING_QUEUE;
  }
  
  static class StrongEntry<K, V> implements ReferenceEntry<K, V> {
    final K key;
    
    final int hash;
    
    final MapMakerInternalMap.ReferenceEntry<K, V> next;
    
    volatile MapMakerInternalMap.ValueReference<K, V> valueReference;
    
    StrongEntry(K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.valueReference = MapMakerInternalMap.unset();
      this.key = key;
      this.hash = hash;
      this.next = next;
    }
    
    public K getKey() {
      return this.key;
    }
    
    public long getExpirationTime() {
      throw new UnsupportedOperationException();
    }
    
    public void setExpirationTime(long time) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
      throw new UnsupportedOperationException();
    }
    
    public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
      throw new UnsupportedOperationException();
    }
    
    public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
      throw new UnsupportedOperationException();
    }
    
    public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
      throw new UnsupportedOperationException();
    }
    
    public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ValueReference<K, V> getValueReference() {
      return this.valueReference;
    }
    
    public void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) {
      MapMakerInternalMap.ValueReference<K, V> previous = this.valueReference;
      this.valueReference = valueReference;
      previous.clear(valueReference);
    }
    
    public int getHash() {
      return this.hash;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNext() {
      return this.next;
    }
  }
  
  static final class StrongExpirableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V> {
    volatile long time;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable;
    
    StrongExpirableEntry(K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      super(key, hash, next);
      this.time = Long.MAX_VALUE;
      this.nextExpirable = MapMakerInternalMap.nullEntry();
      this.previousExpirable = MapMakerInternalMap.nullEntry();
    }
    
    public long getExpirationTime() {
      return this.time;
    }
    
    public void setExpirationTime(long time) {
      this.time = time;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
      return this.nextExpirable;
    }
    
    public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.nextExpirable = next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
      return this.previousExpirable;
    }
    
    public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      this.previousExpirable = previous;
    }
  }
  
  static final class StrongEvictableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V> {
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
    
    StrongEvictableEntry(K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      super(key, hash, next);
      this.nextEvictable = MapMakerInternalMap.nullEntry();
      this.previousEvictable = MapMakerInternalMap.nullEntry();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
      return this.nextEvictable;
    }
    
    public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.nextEvictable = next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
      return this.previousEvictable;
    }
    
    public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      this.previousEvictable = previous;
    }
  }
  
  static final class StrongExpirableEvictableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V> {
    volatile long time;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
    
    StrongExpirableEvictableEntry(K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      super(key, hash, next);
      this.time = Long.MAX_VALUE;
      this.nextExpirable = MapMakerInternalMap.nullEntry();
      this.previousExpirable = MapMakerInternalMap.nullEntry();
      this.nextEvictable = MapMakerInternalMap.nullEntry();
      this.previousEvictable = MapMakerInternalMap.nullEntry();
    }
    
    public long getExpirationTime() {
      return this.time;
    }
    
    public void setExpirationTime(long time) {
      this.time = time;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
      return this.nextExpirable;
    }
    
    public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.nextExpirable = next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
      return this.previousExpirable;
    }
    
    public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      this.previousExpirable = previous;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
      return this.nextEvictable;
    }
    
    public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.nextEvictable = next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
      return this.previousEvictable;
    }
    
    public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      this.previousEvictable = previous;
    }
  }
  
  static class SoftEntry<K, V> extends SoftReference<K> implements ReferenceEntry<K, V> {
    final int hash;
    
    final MapMakerInternalMap.ReferenceEntry<K, V> next;
    
    volatile MapMakerInternalMap.ValueReference<K, V> valueReference;
    
    SoftEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      super(key, queue);
      this.valueReference = MapMakerInternalMap.unset();
      this.hash = hash;
      this.next = next;
    }
    
    public K getKey() {
      return get();
    }
    
    public long getExpirationTime() {
      throw new UnsupportedOperationException();
    }
    
    public void setExpirationTime(long time) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
      throw new UnsupportedOperationException();
    }
    
    public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
      throw new UnsupportedOperationException();
    }
    
    public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
      throw new UnsupportedOperationException();
    }
    
    public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
      throw new UnsupportedOperationException();
    }
    
    public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ValueReference<K, V> getValueReference() {
      return this.valueReference;
    }
    
    public void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) {
      MapMakerInternalMap.ValueReference<K, V> previous = this.valueReference;
      this.valueReference = valueReference;
      previous.clear(valueReference);
    }
    
    public int getHash() {
      return this.hash;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNext() {
      return this.next;
    }
  }
  
  static final class SoftExpirableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V> {
    volatile long time;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable;
    
    SoftExpirableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      super(queue, key, hash, next);
      this.time = Long.MAX_VALUE;
      this.nextExpirable = MapMakerInternalMap.nullEntry();
      this.previousExpirable = MapMakerInternalMap.nullEntry();
    }
    
    public long getExpirationTime() {
      return this.time;
    }
    
    public void setExpirationTime(long time) {
      this.time = time;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
      return this.nextExpirable;
    }
    
    public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.nextExpirable = next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
      return this.previousExpirable;
    }
    
    public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      this.previousExpirable = previous;
    }
  }
  
  static final class SoftEvictableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V> {
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
    
    SoftEvictableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      super(queue, key, hash, next);
      this.nextEvictable = MapMakerInternalMap.nullEntry();
      this.previousEvictable = MapMakerInternalMap.nullEntry();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
      return this.nextEvictable;
    }
    
    public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.nextEvictable = next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
      return this.previousEvictable;
    }
    
    public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      this.previousEvictable = previous;
    }
  }
  
  static final class SoftExpirableEvictableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V> {
    volatile long time;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
    
    SoftExpirableEvictableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      super(queue, key, hash, next);
      this.time = Long.MAX_VALUE;
      this.nextExpirable = MapMakerInternalMap.nullEntry();
      this.previousExpirable = MapMakerInternalMap.nullEntry();
      this.nextEvictable = MapMakerInternalMap.nullEntry();
      this.previousEvictable = MapMakerInternalMap.nullEntry();
    }
    
    public long getExpirationTime() {
      return this.time;
    }
    
    public void setExpirationTime(long time) {
      this.time = time;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
      return this.nextExpirable;
    }
    
    public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.nextExpirable = next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
      return this.previousExpirable;
    }
    
    public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      this.previousExpirable = previous;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
      return this.nextEvictable;
    }
    
    public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.nextEvictable = next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
      return this.previousEvictable;
    }
    
    public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      this.previousEvictable = previous;
    }
  }
  
  static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V> {
    final int hash;
    
    final MapMakerInternalMap.ReferenceEntry<K, V> next;
    
    volatile MapMakerInternalMap.ValueReference<K, V> valueReference;
    
    WeakEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      super(key, queue);
      this.valueReference = MapMakerInternalMap.unset();
      this.hash = hash;
      this.next = next;
    }
    
    public K getKey() {
      return get();
    }
    
    public long getExpirationTime() {
      throw new UnsupportedOperationException();
    }
    
    public void setExpirationTime(long time) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
      throw new UnsupportedOperationException();
    }
    
    public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
      throw new UnsupportedOperationException();
    }
    
    public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
      throw new UnsupportedOperationException();
    }
    
    public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
      throw new UnsupportedOperationException();
    }
    
    public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      throw new UnsupportedOperationException();
    }
    
    public MapMakerInternalMap.ValueReference<K, V> getValueReference() {
      return this.valueReference;
    }
    
    public void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) {
      MapMakerInternalMap.ValueReference<K, V> previous = this.valueReference;
      this.valueReference = valueReference;
      previous.clear(valueReference);
    }
    
    public int getHash() {
      return this.hash;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNext() {
      return this.next;
    }
  }
  
  static final class WeakExpirableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V> {
    volatile long time;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable;
    
    WeakExpirableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      super(queue, key, hash, next);
      this.time = Long.MAX_VALUE;
      this.nextExpirable = MapMakerInternalMap.nullEntry();
      this.previousExpirable = MapMakerInternalMap.nullEntry();
    }
    
    public long getExpirationTime() {
      return this.time;
    }
    
    public void setExpirationTime(long time) {
      this.time = time;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
      return this.nextExpirable;
    }
    
    public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.nextExpirable = next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
      return this.previousExpirable;
    }
    
    public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      this.previousExpirable = previous;
    }
  }
  
  static final class WeakEvictableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V> {
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
    
    WeakEvictableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      super(queue, key, hash, next);
      this.nextEvictable = MapMakerInternalMap.nullEntry();
      this.previousEvictable = MapMakerInternalMap.nullEntry();
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
      return this.nextEvictable;
    }
    
    public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.nextEvictable = next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
      return this.previousEvictable;
    }
    
    public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      this.previousEvictable = previous;
    }
  }
  
  static final class WeakExpirableEvictableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V> {
    volatile long time;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable;
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
    
    WeakExpirableEvictableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      super(queue, key, hash, next);
      this.time = Long.MAX_VALUE;
      this.nextExpirable = MapMakerInternalMap.nullEntry();
      this.previousExpirable = MapMakerInternalMap.nullEntry();
      this.nextEvictable = MapMakerInternalMap.nullEntry();
      this.previousEvictable = MapMakerInternalMap.nullEntry();
    }
    
    public long getExpirationTime() {
      return this.time;
    }
    
    public void setExpirationTime(long time) {
      this.time = time;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
      return this.nextExpirable;
    }
    
    public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.nextExpirable = next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
      return this.previousExpirable;
    }
    
    public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      this.previousExpirable = previous;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
      return this.nextEvictable;
    }
    
    public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
      this.nextEvictable = next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
      return this.previousEvictable;
    }
    
    public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
      this.previousEvictable = previous;
    }
  }
  
  static final class WeakValueReference<K, V> extends WeakReference<V> implements ValueReference<K, V> {
    final MapMakerInternalMap.ReferenceEntry<K, V> entry;
    
    WeakValueReference(ReferenceQueue<V> queue, V referent, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      super(referent, queue);
      this.entry = entry;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
      return this.entry;
    }
    
    public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {
      clear();
    }
    
    public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      return new WeakValueReference(queue, value, entry);
    }
    
    public boolean isComputingReference() {
      return false;
    }
    
    public V waitForValue() {
      return get();
    }
  }
  
  static final class SoftValueReference<K, V> extends SoftReference<V> implements ValueReference<K, V> {
    final MapMakerInternalMap.ReferenceEntry<K, V> entry;
    
    SoftValueReference(ReferenceQueue<V> queue, V referent, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      super(referent, queue);
      this.entry = entry;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
      return this.entry;
    }
    
    public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {
      clear();
    }
    
    public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      return new SoftValueReference(queue, value, entry);
    }
    
    public boolean isComputingReference() {
      return false;
    }
    
    public V waitForValue() {
      return get();
    }
  }
  
  static final class StrongValueReference<K, V> implements ValueReference<K, V> {
    final V referent;
    
    StrongValueReference(V referent) {
      this.referent = referent;
    }
    
    public V get() {
      return this.referent;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
      return null;
    }
    
    public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      return this;
    }
    
    public boolean isComputingReference() {
      return false;
    }
    
    public V waitForValue() {
      return get();
    }
    
    public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {}
  }
  
  static int rehash(int h) {
    h += h << 15 ^ 0xFFFFCD7D;
    h ^= h >>> 10;
    h += h << 3;
    h ^= h >>> 6;
    h += (h << 2) + (h << 14);
    return h ^ h >>> 16;
  }
  
  @GuardedBy("Segment.this")
  @VisibleForTesting
  ReferenceEntry<K, V> newEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
    return segmentFor(hash).newEntry(key, hash, next);
  }
  
  @GuardedBy("Segment.this")
  @VisibleForTesting
  ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
    int hash = original.getHash();
    return segmentFor(hash).copyEntry(original, newNext);
  }
  
  @GuardedBy("Segment.this")
  @VisibleForTesting
  ValueReference<K, V> newValueReference(ReferenceEntry<K, V> entry, V value) {
    int hash = entry.getHash();
    return this.valueStrength.referenceValue(segmentFor(hash), entry, value);
  }
  
  int hash(Object key) {
    int h = this.keyEquivalence.hash(key);
    return rehash(h);
  }
  
  void reclaimValue(ValueReference<K, V> valueReference) {
    ReferenceEntry<K, V> entry = valueReference.getEntry();
    int hash = entry.getHash();
    segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
  }
  
  void reclaimKey(ReferenceEntry<K, V> entry) {
    int hash = entry.getHash();
    segmentFor(hash).reclaimKey(entry, hash);
  }
  
  @VisibleForTesting
  boolean isLive(ReferenceEntry<K, V> entry) {
    return (segmentFor(entry.getHash()).getLiveValue(entry) != null);
  }
  
  Segment<K, V> segmentFor(int hash) {
    return this.segments[hash >>> this.segmentShift & this.segmentMask];
  }
  
  Segment<K, V> createSegment(int initialCapacity, int maxSegmentSize) {
    return new Segment<K, V>(this, initialCapacity, maxSegmentSize);
  }
  
  V getLiveValue(ReferenceEntry<K, V> entry) {
    if (entry.getKey() == null)
      return null; 
    V value = (V)entry.getValueReference().get();
    if (value == null)
      return null; 
    if (expires() && isExpired(entry))
      return null; 
    return value;
  }
  
  boolean isExpired(ReferenceEntry<K, V> entry) {
    return isExpired(entry, this.ticker.read());
  }
  
  boolean isExpired(ReferenceEntry<K, V> entry, long now) {
    return (now - entry.getExpirationTime() > 0L);
  }
  
  @GuardedBy("Segment.this")
  static <K, V> void connectExpirables(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
    previous.setNextExpirable(next);
    next.setPreviousExpirable(previous);
  }
  
  @GuardedBy("Segment.this")
  static <K, V> void nullifyExpirable(ReferenceEntry<K, V> nulled) {
    ReferenceEntry<K, V> nullEntry = nullEntry();
    nulled.setNextExpirable(nullEntry);
    nulled.setPreviousExpirable(nullEntry);
  }
  
  void processPendingNotifications() {
    MapMaker.RemovalNotification<K, V> notification;
    while ((notification = this.removalNotificationQueue.poll()) != null) {
      try {
        this.removalListener.onRemoval(notification);
      } catch (Exception e) {
        logger.log(Level.WARNING, "Exception thrown by removal listener", e);
      } 
    } 
  }
  
  @GuardedBy("Segment.this")
  static <K, V> void connectEvictables(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
    previous.setNextEvictable(next);
    next.setPreviousEvictable(previous);
  }
  
  @GuardedBy("Segment.this")
  static <K, V> void nullifyEvictable(ReferenceEntry<K, V> nulled) {
    ReferenceEntry<K, V> nullEntry = nullEntry();
    nulled.setNextEvictable(nullEntry);
    nulled.setPreviousEvictable(nullEntry);
  }
  
  final Segment<K, V>[] newSegmentArray(int ssize) {
    return (Segment<K, V>[])new Segment[ssize];
  }
  
  static class Segment<K, V> extends ReentrantLock {
    final MapMakerInternalMap<K, V> map;
    
    volatile int count;
    
    int modCount;
    
    int threshold;
    
    volatile AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table;
    
    final int maxSegmentSize;
    
    final ReferenceQueue<K> keyReferenceQueue;
    
    final ReferenceQueue<V> valueReferenceQueue;
    
    final Queue<MapMakerInternalMap.ReferenceEntry<K, V>> recencyQueue;
    
    final AtomicInteger readCount = new AtomicInteger();
    
    @GuardedBy("Segment.this")
    final Queue<MapMakerInternalMap.ReferenceEntry<K, V>> evictionQueue;
    
    @GuardedBy("Segment.this")
    final Queue<MapMakerInternalMap.ReferenceEntry<K, V>> expirationQueue;
    
    Segment(MapMakerInternalMap<K, V> map, int initialCapacity, int maxSegmentSize) {
      this.map = map;
      this.maxSegmentSize = maxSegmentSize;
      initTable(newEntryArray(initialCapacity));
      this.keyReferenceQueue = map.usesKeyReferences() ? new ReferenceQueue<K>() : null;
      this.valueReferenceQueue = map.usesValueReferences() ? new ReferenceQueue<V>() : null;
      this.recencyQueue = (map.evictsBySize() || map.expiresAfterAccess()) ? new ConcurrentLinkedQueue<MapMakerInternalMap.ReferenceEntry<K, V>>() : MapMakerInternalMap.<MapMakerInternalMap.ReferenceEntry<K, V>>discardingQueue();
      this.evictionQueue = map.evictsBySize() ? new MapMakerInternalMap.EvictionQueue<K, V>() : MapMakerInternalMap.<MapMakerInternalMap.ReferenceEntry<K, V>>discardingQueue();
      this.expirationQueue = map.expires() ? new MapMakerInternalMap.ExpirationQueue<K, V>() : MapMakerInternalMap.<MapMakerInternalMap.ReferenceEntry<K, V>>discardingQueue();
    }
    
    AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> newEntryArray(int size) {
      return new AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>>(size);
    }
    
    void initTable(AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> newTable) {
      this.threshold = newTable.length() * 3 / 4;
      if (this.threshold == this.maxSegmentSize)
        this.threshold++; 
      this.table = newTable;
    }
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> newEntry(K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
      return this.map.entryFactory.newEntry(this, key, hash, next);
    }
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newNext) {
      if (original.getKey() == null)
        return null; 
      MapMakerInternalMap.ValueReference<K, V> valueReference = original.getValueReference();
      V value = valueReference.get();
      if (value == null && !valueReference.isComputingReference())
        return null; 
      MapMakerInternalMap.ReferenceEntry<K, V> newEntry = this.map.entryFactory.copyEntry(this, original, newNext);
      newEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, value, newEntry));
      return newEntry;
    }
    
    @GuardedBy("Segment.this")
    void setValue(MapMakerInternalMap.ReferenceEntry<K, V> entry, V value) {
      MapMakerInternalMap.ValueReference<K, V> valueReference = this.map.valueStrength.referenceValue(this, entry, value);
      entry.setValueReference(valueReference);
      recordWrite(entry);
    }
    
    void tryDrainReferenceQueues() {
      if (tryLock())
        try {
          drainReferenceQueues();
        } finally {
          unlock();
        }  
    }
    
    @GuardedBy("Segment.this")
    void drainReferenceQueues() {
      if (this.map.usesKeyReferences())
        drainKeyReferenceQueue(); 
      if (this.map.usesValueReferences())
        drainValueReferenceQueue(); 
    }
    
    @GuardedBy("Segment.this")
    void drainKeyReferenceQueue() {
      int i = 0;
      Reference<? extends K> ref;
      while ((ref = this.keyReferenceQueue.poll()) != null) {
        MapMakerInternalMap.ReferenceEntry<K, V> entry = (MapMakerInternalMap.ReferenceEntry)ref;
        this.map.reclaimKey(entry);
        if (++i == 16)
          break; 
      } 
    }
    
    @GuardedBy("Segment.this")
    void drainValueReferenceQueue() {
      int i = 0;
      Reference<? extends V> ref;
      while ((ref = this.valueReferenceQueue.poll()) != null) {
        MapMakerInternalMap.ValueReference<K, V> valueReference = (MapMakerInternalMap.ValueReference)ref;
        this.map.reclaimValue(valueReference);
        if (++i == 16)
          break; 
      } 
    }
    
    void clearReferenceQueues() {
      if (this.map.usesKeyReferences())
        clearKeyReferenceQueue(); 
      if (this.map.usesValueReferences())
        clearValueReferenceQueue(); 
    }
    
    void clearKeyReferenceQueue() {
      while (this.keyReferenceQueue.poll() != null);
    }
    
    void clearValueReferenceQueue() {
      while (this.valueReferenceQueue.poll() != null);
    }
    
    void recordRead(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      if (this.map.expiresAfterAccess())
        recordExpirationTime(entry, this.map.expireAfterAccessNanos); 
      this.recencyQueue.add(entry);
    }
    
    @GuardedBy("Segment.this")
    void recordLockedRead(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      this.evictionQueue.add(entry);
      if (this.map.expiresAfterAccess()) {
        recordExpirationTime(entry, this.map.expireAfterAccessNanos);
        this.expirationQueue.add(entry);
      } 
    }
    
    @GuardedBy("Segment.this")
    void recordWrite(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      drainRecencyQueue();
      this.evictionQueue.add(entry);
      if (this.map.expires()) {
        long expiration = this.map.expiresAfterAccess() ? this.map.expireAfterAccessNanos : this.map.expireAfterWriteNanos;
        recordExpirationTime(entry, expiration);
        this.expirationQueue.add(entry);
      } 
    }
    
    @GuardedBy("Segment.this")
    void drainRecencyQueue() {
      MapMakerInternalMap.ReferenceEntry<K, V> e;
      while ((e = this.recencyQueue.poll()) != null) {
        if (this.evictionQueue.contains(e))
          this.evictionQueue.add(e); 
        if (this.map.expiresAfterAccess() && this.expirationQueue.contains(e))
          this.expirationQueue.add(e); 
      } 
    }
    
    void recordExpirationTime(MapMakerInternalMap.ReferenceEntry<K, V> entry, long expirationNanos) {
      entry.setExpirationTime(this.map.ticker.read() + expirationNanos);
    }
    
    void tryExpireEntries() {
      if (tryLock())
        try {
          expireEntries();
        } finally {
          unlock();
        }  
    }
    
    @GuardedBy("Segment.this")
    void expireEntries() {
      drainRecencyQueue();
      if (this.expirationQueue.isEmpty())
        return; 
      long now = this.map.ticker.read();
      MapMakerInternalMap.ReferenceEntry<K, V> e;
      while ((e = this.expirationQueue.peek()) != null && this.map.isExpired(e, now)) {
        if (!removeEntry(e, e.getHash(), MapMaker.RemovalCause.EXPIRED))
          throw new AssertionError(); 
      } 
    }
    
    void enqueueNotification(MapMakerInternalMap.ReferenceEntry<K, V> entry, MapMaker.RemovalCause cause) {
      enqueueNotification(entry.getKey(), entry.getHash(), (V)entry.getValueReference().get(), cause);
    }
    
    void enqueueNotification(@Nullable K key, int hash, @Nullable V value, MapMaker.RemovalCause cause) {
      if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
        MapMaker.RemovalNotification<K, V> notification = new MapMaker.RemovalNotification<K, V>(key, value, cause);
        this.map.removalNotificationQueue.offer(notification);
      } 
    }
    
    @GuardedBy("Segment.this")
    boolean evictEntries() {
      if (this.map.evictsBySize() && this.count >= this.maxSegmentSize) {
        drainRecencyQueue();
        MapMakerInternalMap.ReferenceEntry<K, V> e = this.evictionQueue.remove();
        if (!removeEntry(e, e.getHash(), MapMaker.RemovalCause.SIZE))
          throw new AssertionError(); 
        return true;
      } 
      return false;
    }
    
    MapMakerInternalMap.ReferenceEntry<K, V> getFirst(int hash) {
      AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
      return table.get(hash & table.length() - 1);
    }
    
    MapMakerInternalMap.ReferenceEntry<K, V> getEntry(Object key, int hash) {
      if (this.count != 0)
        for (MapMakerInternalMap.ReferenceEntry<K, V> e = getFirst(hash); e != null; e = e.getNext()) {
          if (e.getHash() == hash) {
            K entryKey = e.getKey();
            if (entryKey == null) {
              tryDrainReferenceQueues();
            } else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
              return e;
            } 
          } 
        }  
      return null;
    }
    
    MapMakerInternalMap.ReferenceEntry<K, V> getLiveEntry(Object key, int hash) {
      MapMakerInternalMap.ReferenceEntry<K, V> e = getEntry(key, hash);
      if (e == null)
        return null; 
      if (this.map.expires() && this.map.isExpired(e)) {
        tryExpireEntries();
        return null;
      } 
      return e;
    }
    
    V get(Object key, int hash) {
      try {
        MapMakerInternalMap.ReferenceEntry<K, V> e = getLiveEntry(key, hash);
        if (e == null)
          return null; 
        V value = (V)e.getValueReference().get();
        if (value != null) {
          recordRead(e);
        } else {
          tryDrainReferenceQueues();
        } 
        return value;
      } finally {
        postReadCleanup();
      } 
    }
    
    boolean containsKey(Object key, int hash) {
      try {
        if (this.count != 0) {
          MapMakerInternalMap.ReferenceEntry<K, V> e = getLiveEntry(key, hash);
          if (e == null)
            return false; 
          return (e.getValueReference().get() != null);
        } 
        return false;
      } finally {
        postReadCleanup();
      } 
    }
    
    @VisibleForTesting
    boolean containsValue(Object value) {
      try {
        if (this.count != 0) {
          AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
          int length = table.length();
          for (int i = 0; i < length; i++) {
            for (MapMakerInternalMap.ReferenceEntry<K, V> e = table.get(i); e != null; e = e.getNext()) {
              V entryValue = getLiveValue(e);
              if (entryValue != null)
                if (this.map.valueEquivalence.equivalent(value, entryValue))
                  return true;  
            } 
          } 
        } 
        return false;
      } finally {
        postReadCleanup();
      } 
    }
    
    V put(K key, int hash, V value, boolean onlyIfAbsent) {
      lock();
      try {
        preWriteCleanup();
        int newCount = this.count + 1;
        if (newCount > this.threshold) {
          expand();
          newCount = this.count + 1;
        } 
        AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        MapMakerInternalMap.ReferenceEntry<K, V> first = table.get(index);
        for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
            MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            if (entryValue == null) {
              this.modCount++;
              setValue(e, value);
              if (!valueReference.isComputingReference()) {
                enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.COLLECTED);
                newCount = this.count;
              } else if (evictEntries()) {
                newCount = this.count + 1;
              } 
              this.count = newCount;
              return null;
            } 
            if (onlyIfAbsent) {
              recordLockedRead(e);
              return entryValue;
            } 
            this.modCount++;
            enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.REPLACED);
            setValue(e, value);
            return entryValue;
          } 
        } 
        this.modCount++;
        MapMakerInternalMap.ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
        setValue(newEntry, value);
        table.set(index, newEntry);
        if (evictEntries())
          newCount = this.count + 1; 
        this.count = newCount;
        return null;
      } finally {
        unlock();
        postWriteCleanup();
      } 
    }
    
    @GuardedBy("Segment.this")
    void expand() {
      AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> oldTable = this.table;
      int oldCapacity = oldTable.length();
      if (oldCapacity >= 1073741824)
        return; 
      int newCount = this.count;
      AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> newTable = newEntryArray(oldCapacity << 1);
      this.threshold = newTable.length() * 3 / 4;
      int newMask = newTable.length() - 1;
      for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
        MapMakerInternalMap.ReferenceEntry<K, V> head = oldTable.get(oldIndex);
        if (head != null) {
          MapMakerInternalMap.ReferenceEntry<K, V> next = head.getNext();
          int headIndex = head.getHash() & newMask;
          if (next == null) {
            newTable.set(headIndex, head);
          } else {
            MapMakerInternalMap.ReferenceEntry<K, V> tail = head;
            int tailIndex = headIndex;
            MapMakerInternalMap.ReferenceEntry<K, V> e;
            for (e = next; e != null; e = e.getNext()) {
              int newIndex = e.getHash() & newMask;
              if (newIndex != tailIndex) {
                tailIndex = newIndex;
                tail = e;
              } 
            } 
            newTable.set(tailIndex, tail);
            for (e = head; e != tail; e = e.getNext()) {
              int newIndex = e.getHash() & newMask;
              MapMakerInternalMap.ReferenceEntry<K, V> newNext = newTable.get(newIndex);
              MapMakerInternalMap.ReferenceEntry<K, V> newFirst = copyEntry(e, newNext);
              if (newFirst != null) {
                newTable.set(newIndex, newFirst);
              } else {
                removeCollectedEntry(e);
                newCount--;
              } 
            } 
          } 
        } 
      } 
      this.table = newTable;
      this.count = newCount;
    }
    
    boolean replace(K key, int hash, V oldValue, V newValue) {
      lock();
      try {
        preWriteCleanup();
        AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        MapMakerInternalMap.ReferenceEntry<K, V> first = table.get(index);
        for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
            MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            if (entryValue == null) {
              if (isCollected(valueReference)) {
                int newCount = this.count - 1;
                this.modCount++;
                enqueueNotification(entryKey, hash, entryValue, MapMaker.RemovalCause.COLLECTED);
                MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
                newCount = this.count - 1;
                table.set(index, newFirst);
                this.count = newCount;
              } 
              return false;
            } 
            if (this.map.valueEquivalence.equivalent(oldValue, entryValue)) {
              this.modCount++;
              enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.REPLACED);
              setValue(e, newValue);
              return true;
            } 
            recordLockedRead(e);
            return false;
          } 
        } 
        return false;
      } finally {
        unlock();
        postWriteCleanup();
      } 
    }
    
    V replace(K key, int hash, V newValue) {
      lock();
      try {
        preWriteCleanup();
        AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        MapMakerInternalMap.ReferenceEntry<K, V> first = table.get(index);
        MapMakerInternalMap.ReferenceEntry<K, V> e;
        for (e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
            MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            if (entryValue == null) {
              if (isCollected(valueReference)) {
                int newCount = this.count - 1;
                this.modCount++;
                enqueueNotification(entryKey, hash, entryValue, MapMaker.RemovalCause.COLLECTED);
                MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
                newCount = this.count - 1;
                table.set(index, newFirst);
                this.count = newCount;
              } 
              return null;
            } 
            this.modCount++;
            enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.REPLACED);
            setValue(e, newValue);
            return entryValue;
          } 
        } 
        e = null;
        return (V)e;
      } finally {
        unlock();
        postWriteCleanup();
      } 
    }
    
    V remove(Object key, int hash) {
      lock();
      try {
        preWriteCleanup();
        int newCount = this.count - 1;
        AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        MapMakerInternalMap.ReferenceEntry<K, V> first = table.get(index);
        MapMakerInternalMap.ReferenceEntry<K, V> e;
        for (e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
            MapMaker.RemovalCause cause;
            MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            if (entryValue != null) {
              cause = MapMaker.RemovalCause.EXPLICIT;
            } else if (isCollected(valueReference)) {
              cause = MapMaker.RemovalCause.COLLECTED;
            } else {
              return null;
            } 
            this.modCount++;
            enqueueNotification(entryKey, hash, entryValue, cause);
            MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
            newCount = this.count - 1;
            table.set(index, newFirst);
            this.count = newCount;
            return entryValue;
          } 
        } 
        e = null;
        return (V)e;
      } finally {
        unlock();
        postWriteCleanup();
      } 
    }
    
    boolean remove(Object key, int hash, Object value) {
      lock();
      try {
        preWriteCleanup();
        int newCount = this.count - 1;
        AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        MapMakerInternalMap.ReferenceEntry<K, V> first = table.get(index);
        for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
            MapMaker.RemovalCause cause;
            MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
            V entryValue = valueReference.get();
            if (this.map.valueEquivalence.equivalent(value, entryValue)) {
              cause = MapMaker.RemovalCause.EXPLICIT;
            } else if (isCollected(valueReference)) {
              cause = MapMaker.RemovalCause.COLLECTED;
            } else {
              return false;
            } 
            this.modCount++;
            enqueueNotification(entryKey, hash, entryValue, cause);
            MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
            newCount = this.count - 1;
            table.set(index, newFirst);
            this.count = newCount;
            return (cause == MapMaker.RemovalCause.EXPLICIT);
          } 
        } 
        return false;
      } finally {
        unlock();
        postWriteCleanup();
      } 
    }
    
    void clear() {
      if (this.count != 0) {
        lock();
        try {
          AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
          if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE)
            for (int j = 0; j < table.length(); j++) {
              for (MapMakerInternalMap.ReferenceEntry<K, V> e = table.get(j); e != null; e = e.getNext()) {
                if (!e.getValueReference().isComputingReference())
                  enqueueNotification(e, MapMaker.RemovalCause.EXPLICIT); 
              } 
            }  
          for (int i = 0; i < table.length(); i++)
            table.set(i, null); 
          clearReferenceQueues();
          this.evictionQueue.clear();
          this.expirationQueue.clear();
          this.readCount.set(0);
          this.modCount++;
          this.count = 0;
        } finally {
          unlock();
          postWriteCleanup();
        } 
      } 
    }
    
    @GuardedBy("Segment.this")
    MapMakerInternalMap.ReferenceEntry<K, V> removeFromChain(MapMakerInternalMap.ReferenceEntry<K, V> first, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      this.evictionQueue.remove(entry);
      this.expirationQueue.remove(entry);
      int newCount = this.count;
      MapMakerInternalMap.ReferenceEntry<K, V> newFirst = entry.getNext();
      for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != entry; e = e.getNext()) {
        MapMakerInternalMap.ReferenceEntry<K, V> next = copyEntry(e, newFirst);
        if (next != null) {
          newFirst = next;
        } else {
          removeCollectedEntry(e);
          newCount--;
        } 
      } 
      this.count = newCount;
      return newFirst;
    }
    
    void removeCollectedEntry(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      enqueueNotification(entry, MapMaker.RemovalCause.COLLECTED);
      this.evictionQueue.remove(entry);
      this.expirationQueue.remove(entry);
    }
    
    boolean reclaimKey(MapMakerInternalMap.ReferenceEntry<K, V> entry, int hash) {
      lock();
      try {
        int newCount = this.count - 1;
        AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        MapMakerInternalMap.ReferenceEntry<K, V> first = table.get(index);
        for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          if (e == entry) {
            this.modCount++;
            enqueueNotification(e.getKey(), hash, (V)e.getValueReference().get(), MapMaker.RemovalCause.COLLECTED);
            MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
            newCount = this.count - 1;
            table.set(index, newFirst);
            this.count = newCount;
            return true;
          } 
        } 
        return false;
      } finally {
        unlock();
        postWriteCleanup();
      } 
    }
    
    boolean reclaimValue(K key, int hash, MapMakerInternalMap.ValueReference<K, V> valueReference) {
      lock();
      try {
        int newCount = this.count - 1;
        AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        MapMakerInternalMap.ReferenceEntry<K, V> first = table.get(index);
        for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
            MapMakerInternalMap.ValueReference<K, V> v = e.getValueReference();
            if (v == valueReference) {
              this.modCount++;
              enqueueNotification(key, hash, valueReference.get(), MapMaker.RemovalCause.COLLECTED);
              MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
              newCount = this.count - 1;
              table.set(index, newFirst);
              this.count = newCount;
              return true;
            } 
            return false;
          } 
        } 
        return false;
      } finally {
        unlock();
        if (!isHeldByCurrentThread())
          postWriteCleanup(); 
      } 
    }
    
    boolean clearValue(K key, int hash, MapMakerInternalMap.ValueReference<K, V> valueReference) {
      lock();
      try {
        AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
        int index = hash & table.length() - 1;
        MapMakerInternalMap.ReferenceEntry<K, V> first = table.get(index);
        for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
          K entryKey = e.getKey();
          if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
            MapMakerInternalMap.ValueReference<K, V> v = e.getValueReference();
            if (v == valueReference) {
              MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
              table.set(index, newFirst);
              return true;
            } 
            return false;
          } 
        } 
        return false;
      } finally {
        unlock();
        postWriteCleanup();
      } 
    }
    
    @GuardedBy("Segment.this")
    boolean removeEntry(MapMakerInternalMap.ReferenceEntry<K, V> entry, int hash, MapMaker.RemovalCause cause) {
      int newCount = this.count - 1;
      AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
      int index = hash & table.length() - 1;
      MapMakerInternalMap.ReferenceEntry<K, V> first = table.get(index);
      for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
        if (e == entry) {
          this.modCount++;
          enqueueNotification(e.getKey(), hash, (V)e.getValueReference().get(), cause);
          MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
          newCount = this.count - 1;
          table.set(index, newFirst);
          this.count = newCount;
          return true;
        } 
      } 
      return false;
    }
    
    boolean isCollected(MapMakerInternalMap.ValueReference<K, V> valueReference) {
      if (valueReference.isComputingReference())
        return false; 
      return (valueReference.get() == null);
    }
    
    V getLiveValue(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      if (entry.getKey() == null) {
        tryDrainReferenceQueues();
        return null;
      } 
      V value = (V)entry.getValueReference().get();
      if (value == null) {
        tryDrainReferenceQueues();
        return null;
      } 
      if (this.map.expires() && this.map.isExpired(entry)) {
        tryExpireEntries();
        return null;
      } 
      return value;
    }
    
    void postReadCleanup() {
      if ((this.readCount.incrementAndGet() & 0x3F) == 0)
        runCleanup(); 
    }
    
    @GuardedBy("Segment.this")
    void preWriteCleanup() {
      runLockedCleanup();
    }
    
    void postWriteCleanup() {
      runUnlockedCleanup();
    }
    
    void runCleanup() {
      runLockedCleanup();
      runUnlockedCleanup();
    }
    
    void runLockedCleanup() {
      if (tryLock())
        try {
          drainReferenceQueues();
          expireEntries();
          this.readCount.set(0);
        } finally {
          unlock();
        }  
    }
    
    void runUnlockedCleanup() {
      if (!isHeldByCurrentThread())
        this.map.processPendingNotifications(); 
    }
  }
  
  static final class EvictionQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
    final MapMakerInternalMap.ReferenceEntry<K, V> head = new MapMakerInternalMap.AbstractReferenceEntry<K, V>() {
        MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable = this;
        
        public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() {
          return this.nextEvictable;
        }
        
        public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
          this.nextEvictable = next;
        }
        
        MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable = this;
        
        public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() {
          return this.previousEvictable;
        }
        
        public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
          this.previousEvictable = previous;
        }
      };
    
    public boolean offer(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      MapMakerInternalMap.connectEvictables(entry.getPreviousEvictable(), entry.getNextEvictable());
      MapMakerInternalMap.connectEvictables(this.head.getPreviousEvictable(), entry);
      MapMakerInternalMap.connectEvictables(entry, this.head);
      return true;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> peek() {
      MapMakerInternalMap.ReferenceEntry<K, V> next = this.head.getNextEvictable();
      return (next == this.head) ? null : next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> poll() {
      MapMakerInternalMap.ReferenceEntry<K, V> next = this.head.getNextEvictable();
      if (next == this.head)
        return null; 
      remove(next);
      return next;
    }
    
    public boolean remove(Object o) {
      MapMakerInternalMap.ReferenceEntry<K, V> e = (MapMakerInternalMap.ReferenceEntry<K, V>)o;
      MapMakerInternalMap.ReferenceEntry<K, V> previous = e.getPreviousEvictable();
      MapMakerInternalMap.ReferenceEntry<K, V> next = e.getNextEvictable();
      MapMakerInternalMap.connectEvictables(previous, next);
      MapMakerInternalMap.nullifyEvictable(e);
      return (next != MapMakerInternalMap.NullEntry.INSTANCE);
    }
    
    public boolean contains(Object o) {
      MapMakerInternalMap.ReferenceEntry<K, V> e = (MapMakerInternalMap.ReferenceEntry<K, V>)o;
      return (e.getNextEvictable() != MapMakerInternalMap.NullEntry.INSTANCE);
    }
    
    public boolean isEmpty() {
      return (this.head.getNextEvictable() == this.head);
    }
    
    public int size() {
      int size = 0;
      for (MapMakerInternalMap.ReferenceEntry<K, V> e = this.head.getNextEvictable(); e != this.head; e = e.getNextEvictable())
        size++; 
      return size;
    }
    
    public void clear() {
      MapMakerInternalMap.ReferenceEntry<K, V> e = this.head.getNextEvictable();
      while (e != this.head) {
        MapMakerInternalMap.ReferenceEntry<K, V> next = e.getNextEvictable();
        MapMakerInternalMap.nullifyEvictable(e);
        e = next;
      } 
      this.head.setNextEvictable(this.head);
      this.head.setPreviousEvictable(this.head);
    }
    
    public Iterator<MapMakerInternalMap.ReferenceEntry<K, V>> iterator() {
      return new AbstractSequentialIterator<MapMakerInternalMap.ReferenceEntry<K, V>>(peek()) {
          protected MapMakerInternalMap.ReferenceEntry<K, V> computeNext(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
            MapMakerInternalMap.ReferenceEntry<K, V> next = previous.getNextEvictable();
            return (next == MapMakerInternalMap.EvictionQueue.this.head) ? null : next;
          }
        };
    }
  }
  
  static final class ExpirationQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
    final MapMakerInternalMap.ReferenceEntry<K, V> head = new MapMakerInternalMap.AbstractReferenceEntry<K, V>() {
        public long getExpirationTime() {
          return Long.MAX_VALUE;
        }
        
        public void setExpirationTime(long time) {}
        
        MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable = this;
        
        public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() {
          return this.nextExpirable;
        }
        
        public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) {
          this.nextExpirable = next;
        }
        
        MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable = this;
        
        public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() {
          return this.previousExpirable;
        }
        
        public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
          this.previousExpirable = previous;
        }
      };
    
    public boolean offer(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      MapMakerInternalMap.connectExpirables(entry.getPreviousExpirable(), entry.getNextExpirable());
      MapMakerInternalMap.connectExpirables(this.head.getPreviousExpirable(), entry);
      MapMakerInternalMap.connectExpirables(entry, this.head);
      return true;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> peek() {
      MapMakerInternalMap.ReferenceEntry<K, V> next = this.head.getNextExpirable();
      return (next == this.head) ? null : next;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> poll() {
      MapMakerInternalMap.ReferenceEntry<K, V> next = this.head.getNextExpirable();
      if (next == this.head)
        return null; 
      remove(next);
      return next;
    }
    
    public boolean remove(Object o) {
      MapMakerInternalMap.ReferenceEntry<K, V> e = (MapMakerInternalMap.ReferenceEntry<K, V>)o;
      MapMakerInternalMap.ReferenceEntry<K, V> previous = e.getPreviousExpirable();
      MapMakerInternalMap.ReferenceEntry<K, V> next = e.getNextExpirable();
      MapMakerInternalMap.connectExpirables(previous, next);
      MapMakerInternalMap.nullifyExpirable(e);
      return (next != MapMakerInternalMap.NullEntry.INSTANCE);
    }
    
    public boolean contains(Object o) {
      MapMakerInternalMap.ReferenceEntry<K, V> e = (MapMakerInternalMap.ReferenceEntry<K, V>)o;
      return (e.getNextExpirable() != MapMakerInternalMap.NullEntry.INSTANCE);
    }
    
    public boolean isEmpty() {
      return (this.head.getNextExpirable() == this.head);
    }
    
    public int size() {
      int size = 0;
      for (MapMakerInternalMap.ReferenceEntry<K, V> e = this.head.getNextExpirable(); e != this.head; e = e.getNextExpirable())
        size++; 
      return size;
    }
    
    public void clear() {
      MapMakerInternalMap.ReferenceEntry<K, V> e = this.head.getNextExpirable();
      while (e != this.head) {
        MapMakerInternalMap.ReferenceEntry<K, V> next = e.getNextExpirable();
        MapMakerInternalMap.nullifyExpirable(e);
        e = next;
      } 
      this.head.setNextExpirable(this.head);
      this.head.setPreviousExpirable(this.head);
    }
    
    public Iterator<MapMakerInternalMap.ReferenceEntry<K, V>> iterator() {
      return new AbstractSequentialIterator<MapMakerInternalMap.ReferenceEntry<K, V>>(peek()) {
          protected MapMakerInternalMap.ReferenceEntry<K, V> computeNext(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
            MapMakerInternalMap.ReferenceEntry<K, V> next = previous.getNextExpirable();
            return (next == MapMakerInternalMap.ExpirationQueue.this.head) ? null : next;
          }
        };
    }
  }
  
  static final class CleanupMapTask implements Runnable {
    final WeakReference<MapMakerInternalMap<?, ?>> mapReference;
    
    public CleanupMapTask(MapMakerInternalMap<?, ?> map) {
      this.mapReference = new WeakReference<MapMakerInternalMap<?, ?>>(map);
    }
    
    public void run() {
      MapMakerInternalMap<?, ?> map = this.mapReference.get();
      if (map == null)
        throw new CancellationException(); 
      for (MapMakerInternalMap.Segment<?, ?> segment : map.segments)
        segment.runCleanup(); 
    }
  }
  
  public boolean isEmpty() {
    long sum = 0L;
    Segment<K, V>[] segments = this.segments;
    int i;
    for (i = 0; i < segments.length; i++) {
      if ((segments[i]).count != 0)
        return false; 
      sum += (segments[i]).modCount;
    } 
    if (sum != 0L) {
      for (i = 0; i < segments.length; i++) {
        if ((segments[i]).count != 0)
          return false; 
        sum -= (segments[i]).modCount;
      } 
      if (sum != 0L)
        return false; 
    } 
    return true;
  }
  
  public int size() {
    Segment<K, V>[] segments = this.segments;
    long sum = 0L;
    for (int i = 0; i < segments.length; i++)
      sum += (segments[i]).count; 
    return Ints.saturatedCast(sum);
  }
  
  public V get(@Nullable Object key) {
    if (key == null)
      return null; 
    int hash = hash(key);
    return segmentFor(hash).get(key, hash);
  }
  
  ReferenceEntry<K, V> getEntry(@Nullable Object key) {
    if (key == null)
      return null; 
    int hash = hash(key);
    return segmentFor(hash).getEntry(key, hash);
  }
  
  public boolean containsKey(@Nullable Object key) {
    if (key == null)
      return false; 
    int hash = hash(key);
    return segmentFor(hash).containsKey(key, hash);
  }
  
  public boolean containsValue(@Nullable Object value) {
    if (value == null)
      return false; 
    Segment<K, V>[] segments = this.segments;
    long last = -1L;
    for (int i = 0; i < 3; i++) {
      long sum = 0L;
      for (Segment<K, V> segment : segments) {
        int c = segment.count;
        AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
        for (int j = 0; j < table.length(); j++) {
          for (ReferenceEntry<K, V> e = table.get(j); e != null; e = e.getNext()) {
            V v = segment.getLiveValue(e);
            if (v != null && this.valueEquivalence.equivalent(value, v))
              return true; 
          } 
        } 
        sum += segment.modCount;
      } 
      if (sum == last)
        break; 
      last = sum;
    } 
    return false;
  }
  
  public V put(K key, V value) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    int hash = hash(key);
    return segmentFor(hash).put(key, hash, value, false);
  }
  
  public V putIfAbsent(K key, V value) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    int hash = hash(key);
    return segmentFor(hash).put(key, hash, value, true);
  }
  
  public void putAll(Map<? extends K, ? extends V> m) {
    for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
      put(e.getKey(), e.getValue()); 
  }
  
  public V remove(@Nullable Object key) {
    if (key == null)
      return null; 
    int hash = hash(key);
    return segmentFor(hash).remove(key, hash);
  }
  
  public boolean remove(@Nullable Object key, @Nullable Object value) {
    if (key == null || value == null)
      return false; 
    int hash = hash(key);
    return segmentFor(hash).remove(key, hash, value);
  }
  
  public boolean replace(K key, @Nullable V oldValue, V newValue) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(newValue);
    if (oldValue == null)
      return false; 
    int hash = hash(key);
    return segmentFor(hash).replace(key, hash, oldValue, newValue);
  }
  
  public V replace(K key, V value) {
    Preconditions.checkNotNull(key);
    Preconditions.checkNotNull(value);
    int hash = hash(key);
    return segmentFor(hash).replace(key, hash, value);
  }
  
  public void clear() {
    for (Segment<K, V> segment : this.segments)
      segment.clear(); 
  }
  
  public Set<K> keySet() {
    Set<K> ks = this.keySet;
    return (ks != null) ? ks : (this.keySet = new KeySet());
  }
  
  public Collection<V> values() {
    Collection<V> vs = this.values;
    return (vs != null) ? vs : (this.values = new Values());
  }
  
  public Set<Map.Entry<K, V>> entrySet() {
    Set<Map.Entry<K, V>> es = this.entrySet;
    return (es != null) ? es : (this.entrySet = new EntrySet());
  }
  
  abstract class HashIterator<E> implements Iterator<E> {
    int nextSegmentIndex;
    
    int nextTableIndex;
    
    MapMakerInternalMap.Segment<K, V> currentSegment;
    
    AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> currentTable;
    
    MapMakerInternalMap.ReferenceEntry<K, V> nextEntry;
    
    MapMakerInternalMap<K, V>.WriteThroughEntry nextExternal;
    
    MapMakerInternalMap<K, V>.WriteThroughEntry lastReturned;
    
    HashIterator() {
      this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
      this.nextTableIndex = -1;
      advance();
    }
    
    public abstract E next();
    
    final void advance() {
      this.nextExternal = null;
      if (nextInChain())
        return; 
      if (nextInTable())
        return; 
      while (this.nextSegmentIndex >= 0) {
        this.currentSegment = MapMakerInternalMap.this.segments[this.nextSegmentIndex--];
        if (this.currentSegment.count != 0) {
          this.currentTable = this.currentSegment.table;
          this.nextTableIndex = this.currentTable.length() - 1;
          if (nextInTable())
            return; 
        } 
      } 
    }
    
    boolean nextInChain() {
      if (this.nextEntry != null)
        for (this.nextEntry = this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = this.nextEntry.getNext()) {
          if (advanceTo(this.nextEntry))
            return true; 
        }  
      return false;
    }
    
    boolean nextInTable() {
      while (this.nextTableIndex >= 0) {
        if ((this.nextEntry = this.currentTable.get(this.nextTableIndex--)) != null && (
          advanceTo(this.nextEntry) || nextInChain()))
          return true; 
      } 
      return false;
    }
    
    boolean advanceTo(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      try {
        K key = entry.getKey();
        V value = MapMakerInternalMap.this.getLiveValue(entry);
        if (value != null) {
          this.nextExternal = new MapMakerInternalMap.WriteThroughEntry(key, value);
          return true;
        } 
        return false;
      } finally {
        this.currentSegment.postReadCleanup();
      } 
    }
    
    public boolean hasNext() {
      return (this.nextExternal != null);
    }
    
    MapMakerInternalMap<K, V>.WriteThroughEntry nextEntry() {
      if (this.nextExternal == null)
        throw new NoSuchElementException(); 
      this.lastReturned = this.nextExternal;
      advance();
      return this.lastReturned;
    }
    
    public void remove() {
      CollectPreconditions.checkRemove((this.lastReturned != null));
      MapMakerInternalMap.this.remove(this.lastReturned.getKey());
      this.lastReturned = null;
    }
  }
  
  final class KeyIterator extends HashIterator<K> {
    public K next() {
      return nextEntry().getKey();
    }
  }
  
  final class ValueIterator extends HashIterator<V> {
    public V next() {
      return nextEntry().getValue();
    }
  }
  
  final class WriteThroughEntry extends AbstractMapEntry<K, V> {
    final K key;
    
    V value;
    
    WriteThroughEntry(K key, V value) {
      this.key = key;
      this.value = value;
    }
    
    public K getKey() {
      return this.key;
    }
    
    public V getValue() {
      return this.value;
    }
    
    public boolean equals(@Nullable Object object) {
      if (object instanceof Map.Entry) {
        Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
        return (this.key.equals(that.getKey()) && this.value.equals(that.getValue()));
      } 
      return false;
    }
    
    public int hashCode() {
      return this.key.hashCode() ^ this.value.hashCode();
    }
    
    public V setValue(V newValue) {
      V oldValue = MapMakerInternalMap.this.put(this.key, newValue);
      this.value = newValue;
      return oldValue;
    }
  }
  
  final class EntryIterator extends HashIterator<Map.Entry<K, V>> {
    public Map.Entry<K, V> next() {
      return nextEntry();
    }
  }
  
  final class KeySet extends AbstractSet<K> {
    public Iterator<K> iterator() {
      return new MapMakerInternalMap.KeyIterator();
    }
    
    public int size() {
      return MapMakerInternalMap.this.size();
    }
    
    public boolean isEmpty() {
      return MapMakerInternalMap.this.isEmpty();
    }
    
    public boolean contains(Object o) {
      return MapMakerInternalMap.this.containsKey(o);
    }
    
    public boolean remove(Object o) {
      return (MapMakerInternalMap.this.remove(o) != null);
    }
    
    public void clear() {
      MapMakerInternalMap.this.clear();
    }
  }
  
  final class Values extends AbstractCollection<V> {
    public Iterator<V> iterator() {
      return new MapMakerInternalMap.ValueIterator();
    }
    
    public int size() {
      return MapMakerInternalMap.this.size();
    }
    
    public boolean isEmpty() {
      return MapMakerInternalMap.this.isEmpty();
    }
    
    public boolean contains(Object o) {
      return MapMakerInternalMap.this.containsValue(o);
    }
    
    public void clear() {
      MapMakerInternalMap.this.clear();
    }
  }
  
  final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
    public Iterator<Map.Entry<K, V>> iterator() {
      return new MapMakerInternalMap.EntryIterator();
    }
    
    public boolean contains(Object o) {
      if (!(o instanceof Map.Entry))
        return false; 
      Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
      Object key = e.getKey();
      if (key == null)
        return false; 
      V v = (V)MapMakerInternalMap.this.get(key);
      return (v != null && MapMakerInternalMap.this.valueEquivalence.equivalent(e.getValue(), v));
    }
    
    public boolean remove(Object o) {
      if (!(o instanceof Map.Entry))
        return false; 
      Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
      Object key = e.getKey();
      return (key != null && MapMakerInternalMap.this.remove(key, e.getValue()));
    }
    
    public int size() {
      return MapMakerInternalMap.this.size();
    }
    
    public boolean isEmpty() {
      return MapMakerInternalMap.this.isEmpty();
    }
    
    public void clear() {
      MapMakerInternalMap.this.clear();
    }
  }
  
  Object writeReplace() {
    return new SerializationProxy<K, V>(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this);
  }
  
  static abstract class AbstractSerializationProxy<K, V> extends ForwardingConcurrentMap<K, V> implements Serializable {
    private static final long serialVersionUID = 3L;
    
    final MapMakerInternalMap.Strength keyStrength;
    
    final MapMakerInternalMap.Strength valueStrength;
    
    final Equivalence<Object> keyEquivalence;
    
    final Equivalence<Object> valueEquivalence;
    
    final long expireAfterWriteNanos;
    
    final long expireAfterAccessNanos;
    
    final int maximumSize;
    
    final int concurrencyLevel;
    
    final MapMaker.RemovalListener<? super K, ? super V> removalListener;
    
    transient ConcurrentMap<K, V> delegate;
    
    AbstractSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, int maximumSize, int concurrencyLevel, MapMaker.RemovalListener<? super K, ? super V> removalListener, ConcurrentMap<K, V> delegate) {
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
    
    protected ConcurrentMap<K, V> delegate() {
      return this.delegate;
    }
    
    void writeMapTo(ObjectOutputStream out) throws IOException {
      out.writeInt(this.delegate.size());
      for (Map.Entry<K, V> entry : this.delegate.entrySet()) {
        out.writeObject(entry.getKey());
        out.writeObject(entry.getValue());
      } 
      out.writeObject(null);
    }
    
    MapMaker readMapMaker(ObjectInputStream in) throws IOException {
      int size = in.readInt();
      MapMaker mapMaker = (new MapMaker()).initialCapacity(size).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).concurrencyLevel(this.concurrencyLevel);
      mapMaker.removalListener(this.removalListener);
      if (this.expireAfterWriteNanos > 0L)
        mapMaker.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS); 
      if (this.expireAfterAccessNanos > 0L)
        mapMaker.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS); 
      if (this.maximumSize != -1)
        mapMaker.maximumSize(this.maximumSize); 
      return mapMaker;
    }
    
    void readEntries(ObjectInputStream in) throws IOException, ClassNotFoundException {
      while (true) {
        K key = (K)in.readObject();
        if (key == null)
          break; 
        V value = (V)in.readObject();
        this.delegate.put(key, value);
      } 
    }
  }
  
  private static final class SerializationProxy<K, V> extends AbstractSerializationProxy<K, V> {
    private static final long serialVersionUID = 3L;
    
    SerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, int maximumSize, int concurrencyLevel, MapMaker.RemovalListener<? super K, ? super V> removalListener, ConcurrentMap<K, V> delegate) {
      super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, expireAfterWriteNanos, expireAfterAccessNanos, maximumSize, concurrencyLevel, removalListener, delegate);
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      writeMapTo(out);
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      MapMaker mapMaker = readMapMaker(in);
      this.delegate = mapMaker.makeMap();
      readEntries(in);
    }
    
    private Object readResolve() {
      return this.delegate;
    }
  }
  
  static interface ReferenceEntry<K, V> {
    MapMakerInternalMap.ValueReference<K, V> getValueReference();
    
    void setValueReference(MapMakerInternalMap.ValueReference<K, V> param1ValueReference);
    
    ReferenceEntry<K, V> getNext();
    
    int getHash();
    
    K getKey();
    
    long getExpirationTime();
    
    void setExpirationTime(long param1Long);
    
    ReferenceEntry<K, V> getNextExpirable();
    
    void setNextExpirable(ReferenceEntry<K, V> param1ReferenceEntry);
    
    ReferenceEntry<K, V> getPreviousExpirable();
    
    void setPreviousExpirable(ReferenceEntry<K, V> param1ReferenceEntry);
    
    ReferenceEntry<K, V> getNextEvictable();
    
    void setNextEvictable(ReferenceEntry<K, V> param1ReferenceEntry);
    
    ReferenceEntry<K, V> getPreviousEvictable();
    
    void setPreviousEvictable(ReferenceEntry<K, V> param1ReferenceEntry);
  }
  
  static interface ValueReference<K, V> {
    V get();
    
    V waitForValue() throws ExecutionException;
    
    MapMakerInternalMap.ReferenceEntry<K, V> getEntry();
    
    ValueReference<K, V> copyFor(ReferenceQueue<V> param1ReferenceQueue, @Nullable V param1V, MapMakerInternalMap.ReferenceEntry<K, V> param1ReferenceEntry);
    
    void clear(@Nullable ValueReference<K, V> param1ValueReference);
    
    boolean isComputingReference();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\MapMakerInternalMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */