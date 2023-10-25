package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReferenceArray;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

class ComputingConcurrentHashMap<K, V> extends MapMakerInternalMap<K, V> {
  final Function<? super K, ? extends V> computingFunction;
  
  private static final long serialVersionUID = 4L;
  
  ComputingConcurrentHashMap(MapMaker builder, Function<? super K, ? extends V> computingFunction) {
    super(builder);
    this.computingFunction = (Function<? super K, ? extends V>)Preconditions.checkNotNull(computingFunction);
  }
  
  MapMakerInternalMap.Segment<K, V> createSegment(int initialCapacity, int maxSegmentSize) {
    return new ComputingSegment<K, V>(this, initialCapacity, maxSegmentSize);
  }
  
  ComputingSegment<K, V> segmentFor(int hash) {
    return (ComputingSegment<K, V>)super.segmentFor(hash);
  }
  
  V getOrCompute(K key) throws ExecutionException {
    int hash = hash(Preconditions.checkNotNull(key));
    return segmentFor(hash).getOrCompute(key, hash, this.computingFunction);
  }
  
  static final class ComputingSegment<K, V> extends MapMakerInternalMap.Segment<K, V> {
    ComputingSegment(MapMakerInternalMap<K, V> map, int initialCapacity, int maxSegmentSize) {
      super(map, initialCapacity, maxSegmentSize);
    }
    
    V getOrCompute(K key, int hash, Function<? super K, ? extends V> computingFunction) throws ExecutionException {
      try {
        while (true) {
          MapMakerInternalMap.ReferenceEntry<K, V> e = getEntry(key, hash);
          if (e != null) {
            V v = getLiveValue(e);
            if (v != null) {
              recordRead(e);
              return v;
            } 
          } 
          if (e == null || !e.getValueReference().isComputingReference()) {
            boolean createNewEntry = true;
            ComputingConcurrentHashMap.ComputingValueReference<K, V> computingValueReference = null;
            lock();
            try {
              preWriteCleanup();
              int newCount = this.count - 1;
              AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
              int index = hash & table.length() - 1;
              MapMakerInternalMap.ReferenceEntry<K, V> first = table.get(index);
              for (e = first; e != null; e = e.getNext()) {
                K entryKey = e.getKey();
                if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                  MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
                  if (valueReference.isComputingReference()) {
                    createNewEntry = false;
                    break;
                  } 
                  V v = (V)e.getValueReference().get();
                  if (v == null) {
                    enqueueNotification(entryKey, hash, v, MapMaker.RemovalCause.COLLECTED);
                  } else if (this.map.expires() && this.map.isExpired(e)) {
                    enqueueNotification(entryKey, hash, v, MapMaker.RemovalCause.EXPIRED);
                  } else {
                    recordLockedRead(e);
                    return v;
                  } 
                  this.evictionQueue.remove(e);
                  this.expirationQueue.remove(e);
                  this.count = newCount;
                  break;
                } 
              } 
              if (createNewEntry) {
                computingValueReference = new ComputingConcurrentHashMap.ComputingValueReference<K, V>(computingFunction);
                if (e == null) {
                  e = newEntry(key, hash, first);
                  e.setValueReference(computingValueReference);
                  table.set(index, e);
                } else {
                  e.setValueReference(computingValueReference);
                } 
              } 
            } finally {
              unlock();
              postWriteCleanup();
            } 
            if (createNewEntry)
              return compute(key, hash, e, computingValueReference); 
          } 
          Preconditions.checkState(!Thread.holdsLock(e), "Recursive computation");
          V value = (V)e.getValueReference().waitForValue();
          if (value != null) {
            recordRead(e);
            return value;
          } 
        } 
      } finally {
        postReadCleanup();
      } 
    }
    
    V compute(K key, int hash, MapMakerInternalMap.ReferenceEntry<K, V> e, ComputingConcurrentHashMap.ComputingValueReference<K, V> computingValueReference) throws ExecutionException {
      V value = null;
      long start = System.nanoTime();
      long end = 0L;
      try {
        synchronized (e) {
          value = computingValueReference.compute(key, hash);
          end = System.nanoTime();
        } 
        if (value != null) {
          V oldValue = put(key, hash, value, true);
          if (oldValue != null)
            enqueueNotification(key, hash, value, MapMaker.RemovalCause.REPLACED); 
        } 
        return value;
      } finally {
        if (end == 0L)
          end = System.nanoTime(); 
        if (value == null)
          clearValue(key, hash, computingValueReference); 
      } 
    }
  }
  
  private static final class ComputationExceptionReference<K, V> implements MapMakerInternalMap.ValueReference<K, V> {
    final Throwable t;
    
    ComputationExceptionReference(Throwable t) {
      this.t = t;
    }
    
    public V get() {
      return null;
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
    
    public V waitForValue() throws ExecutionException {
      throw new ExecutionException(this.t);
    }
    
    public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {}
  }
  
  private static final class ComputedReference<K, V> implements MapMakerInternalMap.ValueReference<K, V> {
    final V value;
    
    ComputedReference(@Nullable V value) {
      this.value = value;
    }
    
    public V get() {
      return this.value;
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
  
  private static final class ComputingValueReference<K, V> implements MapMakerInternalMap.ValueReference<K, V> {
    final Function<? super K, ? extends V> computingFunction;
    
    @GuardedBy("ComputingValueReference.this")
    volatile MapMakerInternalMap.ValueReference<K, V> computedReference = MapMakerInternalMap.unset();
    
    public ComputingValueReference(Function<? super K, ? extends V> computingFunction) {
      this.computingFunction = computingFunction;
    }
    
    public V get() {
      return null;
    }
    
    public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() {
      return null;
    }
    
    public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, @Nullable V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
      return this;
    }
    
    public boolean isComputingReference() {
      return true;
    }
    
    public V waitForValue() throws ExecutionException {
      if (this.computedReference == MapMakerInternalMap.UNSET) {
        boolean interrupted = false;
        try {
          synchronized (this) {
            while (this.computedReference == MapMakerInternalMap.UNSET) {
              try {
                wait();
              } catch (InterruptedException ie) {
                interrupted = true;
              } 
            } 
          } 
        } finally {
          if (interrupted)
            Thread.currentThread().interrupt(); 
        } 
      } 
      return this.computedReference.waitForValue();
    }
    
    public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {
      setValueReference(newValue);
    }
    
    V compute(K key, int hash) throws ExecutionException {
      V value;
      try {
        value = (V)this.computingFunction.apply(key);
      } catch (Throwable t) {
        setValueReference(new ComputingConcurrentHashMap.ComputationExceptionReference<K, V>(t));
        throw new ExecutionException(t);
      } 
      setValueReference(new ComputingConcurrentHashMap.ComputedReference<K, V>(value));
      return value;
    }
    
    void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) {
      synchronized (this) {
        if (this.computedReference == MapMakerInternalMap.UNSET) {
          this.computedReference = valueReference;
          notifyAll();
        } 
      } 
    }
  }
  
  Object writeReplace() {
    return new ComputingSerializationProxy<K, V>(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this, this.computingFunction);
  }
  
  static final class ComputingSerializationProxy<K, V> extends MapMakerInternalMap.AbstractSerializationProxy<K, V> {
    final Function<? super K, ? extends V> computingFunction;
    
    private static final long serialVersionUID = 4L;
    
    ComputingSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, int maximumSize, int concurrencyLevel, MapMaker.RemovalListener<? super K, ? super V> removalListener, ConcurrentMap<K, V> delegate, Function<? super K, ? extends V> computingFunction) {
      super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, expireAfterWriteNanos, expireAfterAccessNanos, maximumSize, concurrencyLevel, removalListener, delegate);
      this.computingFunction = computingFunction;
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      writeMapTo(out);
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      MapMaker mapMaker = readMapMaker(in);
      this.delegate = mapMaker.makeComputingMap(this.computingFunction);
      readEntries(in);
    }
    
    Object readResolve() {
      return this.delegate;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ComputingConcurrentHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */