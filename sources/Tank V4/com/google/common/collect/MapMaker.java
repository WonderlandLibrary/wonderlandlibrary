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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class MapMaker extends GenericMapMaker {
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
   MapMaker.RemovalCause nullRemovalCause;
   Equivalence keyEquivalence;
   Ticker ticker;

   @GwtIncompatible("To be supported")
   MapMaker keyEquivalence(Equivalence var1) {
      Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", this.keyEquivalence);
      this.keyEquivalence = (Equivalence)Preconditions.checkNotNull(var1);
      this.useCustomMap = true;
      return this;
   }

   Equivalence getKeyEquivalence() {
      return (Equivalence)Objects.firstNonNull(this.keyEquivalence, this.getKeyStrength().defaultEquivalence());
   }

   public MapMaker initialCapacity(int var1) {
      Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
      Preconditions.checkArgument(var1 >= 0);
      this.initialCapacity = var1;
      return this;
   }

   int getInitialCapacity() {
      return this.initialCapacity == -1 ? 16 : this.initialCapacity;
   }

   /** @deprecated */
   @Deprecated
   MapMaker maximumSize(int var1) {
      Preconditions.checkState(this.maximumSize == -1, "maximum size was already set to %s", this.maximumSize);
      Preconditions.checkArgument(var1 >= 0, "maximum size must not be negative");
      this.maximumSize = var1;
      this.useCustomMap = true;
      if (this.maximumSize == 0) {
         this.nullRemovalCause = MapMaker.RemovalCause.SIZE;
      }

      return this;
   }

   public MapMaker concurrencyLevel(int var1) {
      Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
      Preconditions.checkArgument(var1 > 0);
      this.concurrencyLevel = var1;
      return this;
   }

   int getConcurrencyLevel() {
      return this.concurrencyLevel == -1 ? 4 : this.concurrencyLevel;
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   public MapMaker weakKeys() {
      return this.setKeyStrength(MapMakerInternalMap.Strength.WEAK);
   }

   MapMaker setKeyStrength(MapMakerInternalMap.Strength var1) {
      Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", this.keyStrength);
      this.keyStrength = (MapMakerInternalMap.Strength)Preconditions.checkNotNull(var1);
      Preconditions.checkArgument(this.keyStrength != MapMakerInternalMap.Strength.SOFT, "Soft keys are not supported");
      if (var1 != MapMakerInternalMap.Strength.STRONG) {
         this.useCustomMap = true;
      }

      return this;
   }

   MapMakerInternalMap.Strength getKeyStrength() {
      return (MapMakerInternalMap.Strength)Objects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   public MapMaker weakValues() {
      return this.setValueStrength(MapMakerInternalMap.Strength.WEAK);
   }

   /** @deprecated */
   @Deprecated
   @GwtIncompatible("java.lang.ref.SoftReference")
   public MapMaker softValues() {
      return this.setValueStrength(MapMakerInternalMap.Strength.SOFT);
   }

   MapMaker setValueStrength(MapMakerInternalMap.Strength var1) {
      Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", this.valueStrength);
      this.valueStrength = (MapMakerInternalMap.Strength)Preconditions.checkNotNull(var1);
      if (var1 != MapMakerInternalMap.Strength.STRONG) {
         this.useCustomMap = true;
      }

      return this;
   }

   MapMakerInternalMap.Strength getValueStrength() {
      return (MapMakerInternalMap.Strength)Objects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
   }

   /** @deprecated */
   @Deprecated
   MapMaker expireAfterWrite(long var1, TimeUnit var3) {
      this.checkExpiration(var1, var3);
      this.expireAfterWriteNanos = var3.toNanos(var1);
      if (var1 == 0L && this.nullRemovalCause == null) {
         this.nullRemovalCause = MapMaker.RemovalCause.EXPIRED;
      }

      this.useCustomMap = true;
      return this;
   }

   private void checkExpiration(long var1, TimeUnit var3) {
      Preconditions.checkState(this.expireAfterWriteNanos == -1L, "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
      Preconditions.checkState(this.expireAfterAccessNanos == -1L, "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
      Preconditions.checkArgument(var1 >= 0L, "duration cannot be negative: %s %s", var1, var3);
   }

   long getExpireAfterWriteNanos() {
      return this.expireAfterWriteNanos == -1L ? 0L : this.expireAfterWriteNanos;
   }

   /** @deprecated */
   @Deprecated
   @GwtIncompatible("To be supported")
   MapMaker expireAfterAccess(long var1, TimeUnit var3) {
      this.checkExpiration(var1, var3);
      this.expireAfterAccessNanos = var3.toNanos(var1);
      if (var1 == 0L && this.nullRemovalCause == null) {
         this.nullRemovalCause = MapMaker.RemovalCause.EXPIRED;
      }

      this.useCustomMap = true;
      return this;
   }

   long getExpireAfterAccessNanos() {
      return this.expireAfterAccessNanos == -1L ? 0L : this.expireAfterAccessNanos;
   }

   Ticker getTicker() {
      return (Ticker)Objects.firstNonNull(this.ticker, Ticker.systemTicker());
   }

   /** @deprecated */
   @Deprecated
   @GwtIncompatible("To be supported")
   GenericMapMaker removalListener(MapMaker.RemovalListener var1) {
      Preconditions.checkState(this.removalListener == null);
      super.removalListener = (MapMaker.RemovalListener)Preconditions.checkNotNull(var1);
      this.useCustomMap = true;
      return this;
   }

   public ConcurrentMap makeMap() {
      return (ConcurrentMap)(!this.useCustomMap ? new ConcurrentHashMap(this.getInitialCapacity(), 0.75F, this.getConcurrencyLevel()) : (ConcurrentMap)(this.nullRemovalCause == null ? new MapMakerInternalMap(this) : new MapMaker.NullConcurrentMap(this)));
   }

   @GwtIncompatible("MapMakerInternalMap")
   MapMakerInternalMap makeCustomMap() {
      return new MapMakerInternalMap(this);
   }

   /** @deprecated */
   @Deprecated
   ConcurrentMap makeComputingMap(Function var1) {
      return (ConcurrentMap)(this.nullRemovalCause == null ? new MapMaker.ComputingMapAdapter(this, var1) : new MapMaker.NullComputingConcurrentMap(this, var1));
   }

   public String toString() {
      Objects.ToStringHelper var1 = Objects.toStringHelper((Object)this);
      if (this.initialCapacity != -1) {
         var1.add("initialCapacity", this.initialCapacity);
      }

      if (this.concurrencyLevel != -1) {
         var1.add("concurrencyLevel", this.concurrencyLevel);
      }

      if (this.maximumSize != -1) {
         var1.add("maximumSize", this.maximumSize);
      }

      if (this.expireAfterWriteNanos != -1L) {
         var1.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
      }

      if (this.expireAfterAccessNanos != -1L) {
         var1.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
      }

      if (this.keyStrength != null) {
         var1.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
      }

      if (this.valueStrength != null) {
         var1.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
      }

      if (this.keyEquivalence != null) {
         var1.addValue("keyEquivalence");
      }

      if (this.removalListener != null) {
         var1.addValue("removalListener");
      }

      return var1.toString();
   }

   GenericMapMaker expireAfterAccess(long var1, TimeUnit var3) {
      return this.expireAfterAccess(var1, var3);
   }

   GenericMapMaker expireAfterWrite(long var1, TimeUnit var3) {
      return this.expireAfterWrite(var1, var3);
   }

   public GenericMapMaker softValues() {
      return this.softValues();
   }

   public GenericMapMaker weakValues() {
      return this.weakValues();
   }

   public GenericMapMaker weakKeys() {
      return this.weakKeys();
   }

   public GenericMapMaker concurrencyLevel(int var1) {
      return this.concurrencyLevel(var1);
   }

   GenericMapMaker maximumSize(int var1) {
      return this.maximumSize(var1);
   }

   public GenericMapMaker initialCapacity(int var1) {
      return this.initialCapacity(var1);
   }

   GenericMapMaker keyEquivalence(Equivalence var1) {
      return this.keyEquivalence(var1);
   }

   static final class ComputingMapAdapter extends ComputingConcurrentHashMap implements Serializable {
      private static final long serialVersionUID = 0L;

      ComputingMapAdapter(MapMaker var1, Function var2) {
         super(var1, var2);
      }

      public Object get(Object var1) {
         Object var2;
         try {
            var2 = this.getOrCompute(var1);
         } catch (ExecutionException var5) {
            Throwable var4 = var5.getCause();
            Throwables.propagateIfInstanceOf(var4, ComputationException.class);
            throw new ComputationException(var4);
         }

         if (var2 == null) {
            throw new NullPointerException(this.computingFunction + " returned null for key " + var1 + ".");
         } else {
            return var2;
         }
      }
   }

   static final class NullComputingConcurrentMap extends MapMaker.NullConcurrentMap {
      private static final long serialVersionUID = 0L;
      final Function computingFunction;

      NullComputingConcurrentMap(MapMaker var1, Function var2) {
         super(var1);
         this.computingFunction = (Function)Preconditions.checkNotNull(var2);
      }

      public Object get(Object var1) {
         Object var3 = this.compute(var1);
         Preconditions.checkNotNull(var3, "%s returned null for key %s.", this.computingFunction, var1);
         this.notifyRemoval(var1, var3);
         return var3;
      }

      private Object compute(Object var1) {
         Preconditions.checkNotNull(var1);

         try {
            return this.computingFunction.apply(var1);
         } catch (ComputationException var3) {
            throw var3;
         } catch (Throwable var4) {
            throw new ComputationException(var4);
         }
      }
   }

   static class NullConcurrentMap extends AbstractMap implements ConcurrentMap, Serializable {
      private static final long serialVersionUID = 0L;
      private final MapMaker.RemovalListener removalListener;
      private final MapMaker.RemovalCause removalCause;

      NullConcurrentMap(MapMaker var1) {
         this.removalListener = var1.getRemovalListener();
         this.removalCause = var1.nullRemovalCause;
      }

      public boolean containsKey(@Nullable Object var1) {
         return false;
      }

      public boolean containsValue(@Nullable Object var1) {
         return false;
      }

      public Object get(@Nullable Object var1) {
         return null;
      }

      void notifyRemoval(Object var1, Object var2) {
         MapMaker.RemovalNotification var3 = new MapMaker.RemovalNotification(var1, var2, this.removalCause);
         this.removalListener.onRemoval(var3);
      }

      public Object put(Object var1, Object var2) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
         this.notifyRemoval(var1, var2);
         return null;
      }

      public Object putIfAbsent(Object var1, Object var2) {
         return this.put(var1, var2);
      }

      public Object remove(@Nullable Object var1) {
         return null;
      }

      public boolean remove(@Nullable Object var1, @Nullable Object var2) {
         return false;
      }

      public Object replace(Object var1, Object var2) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
         return null;
      }

      public boolean replace(Object var1, @Nullable Object var2, Object var3) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var3);
         return false;
      }

      public Set entrySet() {
         return Collections.emptySet();
      }
   }

   static enum RemovalCause {
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

      private static final MapMaker.RemovalCause[] $VALUES = new MapMaker.RemovalCause[]{EXPLICIT, REPLACED, COLLECTED, EXPIRED, SIZE};

      private RemovalCause() {
      }

      abstract boolean wasEvicted();

      RemovalCause(Object var3) {
         this();
      }
   }

   static final class RemovalNotification extends ImmutableEntry {
      private static final long serialVersionUID = 0L;
      private final MapMaker.RemovalCause cause;

      RemovalNotification(@Nullable Object var1, @Nullable Object var2, MapMaker.RemovalCause var3) {
         super(var1, var2);
         this.cause = var3;
      }

      public MapMaker.RemovalCause getCause() {
         return this.cause;
      }

      public boolean wasEvicted() {
         return this.cause.wasEvicted();
      }
   }

   interface RemovalListener {
      void onRemoval(MapMaker.RemovalNotification var1);
   }
}
