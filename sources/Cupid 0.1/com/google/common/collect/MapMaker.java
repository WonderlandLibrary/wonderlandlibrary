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

@GwtCompatible(emulated = true)
public final class MapMaker extends GenericMapMaker<Object, Object> {
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
  
  @GwtIncompatible("To be supported")
  MapMaker keyEquivalence(Equivalence<Object> equivalence) {
    Preconditions.checkState((this.keyEquivalence == null), "key equivalence was already set to %s", new Object[] { this.keyEquivalence });
    this.keyEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
    this.useCustomMap = true;
    return this;
  }
  
  Equivalence<Object> getKeyEquivalence() {
    return (Equivalence<Object>)Objects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
  }
  
  public MapMaker initialCapacity(int initialCapacity) {
    Preconditions.checkState((this.initialCapacity == -1), "initial capacity was already set to %s", new Object[] { Integer.valueOf(this.initialCapacity) });
    Preconditions.checkArgument((initialCapacity >= 0));
    this.initialCapacity = initialCapacity;
    return this;
  }
  
  int getInitialCapacity() {
    return (this.initialCapacity == -1) ? 16 : this.initialCapacity;
  }
  
  @Deprecated
  MapMaker maximumSize(int size) {
    Preconditions.checkState((this.maximumSize == -1), "maximum size was already set to %s", new Object[] { Integer.valueOf(this.maximumSize) });
    Preconditions.checkArgument((size >= 0), "maximum size must not be negative");
    this.maximumSize = size;
    this.useCustomMap = true;
    if (this.maximumSize == 0)
      this.nullRemovalCause = RemovalCause.SIZE; 
    return this;
  }
  
  public MapMaker concurrencyLevel(int concurrencyLevel) {
    Preconditions.checkState((this.concurrencyLevel == -1), "concurrency level was already set to %s", new Object[] { Integer.valueOf(this.concurrencyLevel) });
    Preconditions.checkArgument((concurrencyLevel > 0));
    this.concurrencyLevel = concurrencyLevel;
    return this;
  }
  
  int getConcurrencyLevel() {
    return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel;
  }
  
  @GwtIncompatible("java.lang.ref.WeakReference")
  public MapMaker weakKeys() {
    return setKeyStrength(MapMakerInternalMap.Strength.WEAK);
  }
  
  MapMaker setKeyStrength(MapMakerInternalMap.Strength strength) {
    Preconditions.checkState((this.keyStrength == null), "Key strength was already set to %s", new Object[] { this.keyStrength });
    this.keyStrength = (MapMakerInternalMap.Strength)Preconditions.checkNotNull(strength);
    Preconditions.checkArgument((this.keyStrength != MapMakerInternalMap.Strength.SOFT), "Soft keys are not supported");
    if (strength != MapMakerInternalMap.Strength.STRONG)
      this.useCustomMap = true; 
    return this;
  }
  
  MapMakerInternalMap.Strength getKeyStrength() {
    return (MapMakerInternalMap.Strength)Objects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
  }
  
  @GwtIncompatible("java.lang.ref.WeakReference")
  public MapMaker weakValues() {
    return setValueStrength(MapMakerInternalMap.Strength.WEAK);
  }
  
  @Deprecated
  @GwtIncompatible("java.lang.ref.SoftReference")
  public MapMaker softValues() {
    return setValueStrength(MapMakerInternalMap.Strength.SOFT);
  }
  
  MapMaker setValueStrength(MapMakerInternalMap.Strength strength) {
    Preconditions.checkState((this.valueStrength == null), "Value strength was already set to %s", new Object[] { this.valueStrength });
    this.valueStrength = (MapMakerInternalMap.Strength)Preconditions.checkNotNull(strength);
    if (strength != MapMakerInternalMap.Strength.STRONG)
      this.useCustomMap = true; 
    return this;
  }
  
  MapMakerInternalMap.Strength getValueStrength() {
    return (MapMakerInternalMap.Strength)Objects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
  }
  
  @Deprecated
  MapMaker expireAfterWrite(long duration, TimeUnit unit) {
    checkExpiration(duration, unit);
    this.expireAfterWriteNanos = unit.toNanos(duration);
    if (duration == 0L && this.nullRemovalCause == null)
      this.nullRemovalCause = RemovalCause.EXPIRED; 
    this.useCustomMap = true;
    return this;
  }
  
  private void checkExpiration(long duration, TimeUnit unit) {
    Preconditions.checkState((this.expireAfterWriteNanos == -1L), "expireAfterWrite was already set to %s ns", new Object[] { Long.valueOf(this.expireAfterWriteNanos) });
    Preconditions.checkState((this.expireAfterAccessNanos == -1L), "expireAfterAccess was already set to %s ns", new Object[] { Long.valueOf(this.expireAfterAccessNanos) });
    Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", new Object[] { Long.valueOf(duration), unit });
  }
  
  long getExpireAfterWriteNanos() {
    return (this.expireAfterWriteNanos == -1L) ? 0L : this.expireAfterWriteNanos;
  }
  
  @Deprecated
  @GwtIncompatible("To be supported")
  MapMaker expireAfterAccess(long duration, TimeUnit unit) {
    checkExpiration(duration, unit);
    this.expireAfterAccessNanos = unit.toNanos(duration);
    if (duration == 0L && this.nullRemovalCause == null)
      this.nullRemovalCause = RemovalCause.EXPIRED; 
    this.useCustomMap = true;
    return this;
  }
  
  long getExpireAfterAccessNanos() {
    return (this.expireAfterAccessNanos == -1L) ? 0L : this.expireAfterAccessNanos;
  }
  
  Ticker getTicker() {
    return (Ticker)Objects.firstNonNull(this.ticker, Ticker.systemTicker());
  }
  
  @Deprecated
  @GwtIncompatible("To be supported")
  <K, V> GenericMapMaker<K, V> removalListener(RemovalListener<K, V> listener) {
    Preconditions.checkState((this.removalListener == null));
    GenericMapMaker<K, V> me = this;
    me.removalListener = (RemovalListener<K, V>)Preconditions.checkNotNull(listener);
    this.useCustomMap = true;
    return me;
  }
  
  public <K, V> ConcurrentMap<K, V> makeMap() {
    if (!this.useCustomMap)
      return new ConcurrentHashMap<K, V>(getInitialCapacity(), 0.75F, getConcurrencyLevel()); 
    return (this.nullRemovalCause == null) ? new MapMakerInternalMap<K, V>(this) : new NullConcurrentMap<K, V>(this);
  }
  
  @GwtIncompatible("MapMakerInternalMap")
  <K, V> MapMakerInternalMap<K, V> makeCustomMap() {
    return new MapMakerInternalMap<K, V>(this);
  }
  
  @Deprecated
  <K, V> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> computingFunction) {
    return (this.nullRemovalCause == null) ? new ComputingMapAdapter<K, V>(this, computingFunction) : new NullComputingConcurrentMap<K, V>(this, computingFunction);
  }
  
  public String toString() {
    Objects.ToStringHelper s = Objects.toStringHelper(this);
    if (this.initialCapacity != -1)
      s.add("initialCapacity", this.initialCapacity); 
    if (this.concurrencyLevel != -1)
      s.add("concurrencyLevel", this.concurrencyLevel); 
    if (this.maximumSize != -1)
      s.add("maximumSize", this.maximumSize); 
    if (this.expireAfterWriteNanos != -1L)
      s.add("expireAfterWrite", this.expireAfterWriteNanos + "ns"); 
    if (this.expireAfterAccessNanos != -1L)
      s.add("expireAfterAccess", this.expireAfterAccessNanos + "ns"); 
    if (this.keyStrength != null)
      s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString())); 
    if (this.valueStrength != null)
      s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString())); 
    if (this.keyEquivalence != null)
      s.addValue("keyEquivalence"); 
    if (this.removalListener != null)
      s.addValue("removalListener"); 
    return s.toString();
  }
  
  static interface RemovalListener<K, V> {
    void onRemoval(MapMaker.RemovalNotification<K, V> param1RemovalNotification);
  }
  
  static final class RemovalNotification<K, V> extends ImmutableEntry<K, V> {
    private static final long serialVersionUID = 0L;
    
    private final MapMaker.RemovalCause cause;
    
    RemovalNotification(@Nullable K key, @Nullable V value, MapMaker.RemovalCause cause) {
      super(key, value);
      this.cause = cause;
    }
    
    public MapMaker.RemovalCause getCause() {
      return this.cause;
    }
    
    public boolean wasEvicted() {
      return this.cause.wasEvicted();
    }
  }
  
  enum RemovalCause {
    EXPLICIT {
      boolean wasEvicted() {
        return false;
      }
    },
    REPLACED {
      boolean wasEvicted() {
        return false;
      }
    },
    COLLECTED {
      boolean wasEvicted() {
        return true;
      }
    },
    EXPIRED {
      boolean wasEvicted() {
        return true;
      }
    },
    SIZE {
      boolean wasEvicted() {
        return true;
      }
    };
    
    abstract boolean wasEvicted();
  }
  
  static class NullConcurrentMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
    private static final long serialVersionUID = 0L;
    
    private final MapMaker.RemovalListener<K, V> removalListener;
    
    private final MapMaker.RemovalCause removalCause;
    
    NullConcurrentMap(MapMaker mapMaker) {
      this.removalListener = mapMaker.getRemovalListener();
      this.removalCause = mapMaker.nullRemovalCause;
    }
    
    public boolean containsKey(@Nullable Object key) {
      return false;
    }
    
    public boolean containsValue(@Nullable Object value) {
      return false;
    }
    
    public V get(@Nullable Object key) {
      return null;
    }
    
    void notifyRemoval(K key, V value) {
      MapMaker.RemovalNotification<K, V> notification = new MapMaker.RemovalNotification<K, V>(key, value, this.removalCause);
      this.removalListener.onRemoval(notification);
    }
    
    public V put(K key, V value) {
      Preconditions.checkNotNull(key);
      Preconditions.checkNotNull(value);
      notifyRemoval(key, value);
      return null;
    }
    
    public V putIfAbsent(K key, V value) {
      return put(key, value);
    }
    
    public V remove(@Nullable Object key) {
      return null;
    }
    
    public boolean remove(@Nullable Object key, @Nullable Object value) {
      return false;
    }
    
    public V replace(K key, V value) {
      Preconditions.checkNotNull(key);
      Preconditions.checkNotNull(value);
      return null;
    }
    
    public boolean replace(K key, @Nullable V oldValue, V newValue) {
      Preconditions.checkNotNull(key);
      Preconditions.checkNotNull(newValue);
      return false;
    }
    
    public Set<Map.Entry<K, V>> entrySet() {
      return Collections.emptySet();
    }
  }
  
  static final class NullComputingConcurrentMap<K, V> extends NullConcurrentMap<K, V> {
    private static final long serialVersionUID = 0L;
    
    final Function<? super K, ? extends V> computingFunction;
    
    NullComputingConcurrentMap(MapMaker mapMaker, Function<? super K, ? extends V> computingFunction) {
      super(mapMaker);
      this.computingFunction = (Function<? super K, ? extends V>)Preconditions.checkNotNull(computingFunction);
    }
    
    public V get(Object k) {
      K key = (K)k;
      V value = compute(key);
      Preconditions.checkNotNull(value, "%s returned null for key %s.", new Object[] { this.computingFunction, key });
      notifyRemoval(key, value);
      return value;
    }
    
    private V compute(K key) {
      Preconditions.checkNotNull(key);
      try {
        return (V)this.computingFunction.apply(key);
      } catch (ComputationException e) {
        throw e;
      } catch (Throwable t) {
        throw new ComputationException(t);
      } 
    }
  }
  
  static final class ComputingMapAdapter<K, V> extends ComputingConcurrentHashMap<K, V> implements Serializable {
    private static final long serialVersionUID = 0L;
    
    ComputingMapAdapter(MapMaker mapMaker, Function<? super K, ? extends V> computingFunction) {
      super(mapMaker, computingFunction);
    }
    
    public V get(Object key) {
      V value;
      try {
        value = getOrCompute((K)key);
      } catch (ExecutionException e) {
        Throwable cause = e.getCause();
        Throwables.propagateIfInstanceOf(cause, ComputationException.class);
        throw new ComputationException(cause);
      } 
      if (value == null)
        throw new NullPointerException(this.computingFunction + " returned null for key " + key + "."); 
      return value;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\MapMaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */