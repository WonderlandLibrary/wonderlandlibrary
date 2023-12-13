package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Ticker;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckReturnValue;

@GwtCompatible(
   emulated = true
)
public final class CacheBuilder {
   private static final int DEFAULT_INITIAL_CAPACITY = 16;
   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
   private static final int DEFAULT_EXPIRATION_NANOS = 0;
   private static final int DEFAULT_REFRESH_NANOS = 0;
   static final Supplier NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter() {
      public void recordHits(int var1) {
      }

      public void recordMisses(int var1) {
      }

      public void recordLoadSuccess(long var1) {
      }

      public void recordLoadException(long var1) {
      }

      public void recordEviction() {
      }

      public CacheStats snapshot() {
         return CacheBuilder.EMPTY_STATS;
      }
   });
   static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
   static final Supplier CACHE_STATS_COUNTER = new Supplier() {
      public AbstractCache.StatsCounter get() {
         return new AbstractCache.SimpleStatsCounter();
      }

      public Object get() {
         return this.get();
      }
   };
   static final Ticker NULL_TICKER = new Ticker() {
      public long read() {
         return 0L;
      }
   };
   private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
   static final int UNSET_INT = -1;
   boolean strictParsing = true;
   int initialCapacity = -1;
   int concurrencyLevel = -1;
   long maximumSize = -1L;
   long maximumWeight = -1L;
   Weigher weigher;
   LocalCache.Strength keyStrength;
   LocalCache.Strength valueStrength;
   long expireAfterWriteNanos = -1L;
   long expireAfterAccessNanos = -1L;
   long refreshNanos = -1L;
   Equivalence keyEquivalence;
   Equivalence valueEquivalence;
   RemovalListener removalListener;
   Ticker ticker;
   Supplier statsCounterSupplier;

   CacheBuilder() {
      this.statsCounterSupplier = NULL_STATS_COUNTER;
   }

   public static CacheBuilder newBuilder() {
      return new CacheBuilder();
   }

   @Beta
   @GwtIncompatible("To be supported")
   public static CacheBuilder from(CacheBuilderSpec var0) {
      return var0.toCacheBuilder().lenientParsing();
   }

   @Beta
   @GwtIncompatible("To be supported")
   public static CacheBuilder from(String var0) {
      return from(CacheBuilderSpec.parse(var0));
   }

   @GwtIncompatible("To be supported")
   CacheBuilder lenientParsing() {
      this.strictParsing = false;
      return this;
   }

   @GwtIncompatible("To be supported")
   CacheBuilder keyEquivalence(Equivalence var1) {
      Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", this.keyEquivalence);
      this.keyEquivalence = (Equivalence)Preconditions.checkNotNull(var1);
      return this;
   }

   Equivalence getKeyEquivalence() {
      return (Equivalence)Objects.firstNonNull(this.keyEquivalence, this.getKeyStrength().defaultEquivalence());
   }

   @GwtIncompatible("To be supported")
   CacheBuilder valueEquivalence(Equivalence var1) {
      Preconditions.checkState(this.valueEquivalence == null, "value equivalence was already set to %s", this.valueEquivalence);
      this.valueEquivalence = (Equivalence)Preconditions.checkNotNull(var1);
      return this;
   }

   Equivalence getValueEquivalence() {
      return (Equivalence)Objects.firstNonNull(this.valueEquivalence, this.getValueStrength().defaultEquivalence());
   }

   public CacheBuilder initialCapacity(int var1) {
      Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
      Preconditions.checkArgument(var1 >= 0);
      this.initialCapacity = var1;
      return this;
   }

   int getInitialCapacity() {
      return this.initialCapacity == -1 ? 16 : this.initialCapacity;
   }

   public CacheBuilder concurrencyLevel(int var1) {
      Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
      Preconditions.checkArgument(var1 > 0);
      this.concurrencyLevel = var1;
      return this;
   }

   int getConcurrencyLevel() {
      return this.concurrencyLevel == -1 ? 4 : this.concurrencyLevel;
   }

   public CacheBuilder maximumSize(long var1) {
      Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", this.maximumSize);
      Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", this.maximumWeight);
      Preconditions.checkState(this.weigher == null, "maximum size can not be combined with weigher");
      Preconditions.checkArgument(var1 >= 0L, "maximum size must not be negative");
      this.maximumSize = var1;
      return this;
   }

   @GwtIncompatible("To be supported")
   public CacheBuilder maximumWeight(long var1) {
      Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", this.maximumWeight);
      Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", this.maximumSize);
      this.maximumWeight = var1;
      Preconditions.checkArgument(var1 >= 0L, "maximum weight must not be negative");
      return this;
   }

   @GwtIncompatible("To be supported")
   public CacheBuilder weigher(Weigher var1) {
      Preconditions.checkState(this.weigher == null);
      if (this.strictParsing) {
         Preconditions.checkState(this.maximumSize == -1L, "weigher can not be combined with maximum size", this.maximumSize);
      }

      this.weigher = (Weigher)Preconditions.checkNotNull(var1);
      return this;
   }

   long getMaximumWeight() {
      if (this.expireAfterWriteNanos != 0L && this.expireAfterAccessNanos != 0L) {
         return this.weigher == null ? this.maximumSize : this.maximumWeight;
      } else {
         return 0L;
      }
   }

   Weigher getWeigher() {
      return (Weigher)Objects.firstNonNull(this.weigher, CacheBuilder.OneWeigher.INSTANCE);
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   public CacheBuilder weakKeys() {
      return this.setKeyStrength(LocalCache.Strength.WEAK);
   }

   CacheBuilder setKeyStrength(LocalCache.Strength var1) {
      Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", this.keyStrength);
      this.keyStrength = (LocalCache.Strength)Preconditions.checkNotNull(var1);
      return this;
   }

   LocalCache.Strength getKeyStrength() {
      return (LocalCache.Strength)Objects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   public CacheBuilder weakValues() {
      return this.setValueStrength(LocalCache.Strength.WEAK);
   }

   @GwtIncompatible("java.lang.ref.SoftReference")
   public CacheBuilder softValues() {
      return this.setValueStrength(LocalCache.Strength.SOFT);
   }

   CacheBuilder setValueStrength(LocalCache.Strength var1) {
      Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", this.valueStrength);
      this.valueStrength = (LocalCache.Strength)Preconditions.checkNotNull(var1);
      return this;
   }

   LocalCache.Strength getValueStrength() {
      return (LocalCache.Strength)Objects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
   }

   public CacheBuilder expireAfterWrite(long var1, TimeUnit var3) {
      Preconditions.checkState(this.expireAfterWriteNanos == -1L, "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
      Preconditions.checkArgument(var1 >= 0L, "duration cannot be negative: %s %s", var1, var3);
      this.expireAfterWriteNanos = var3.toNanos(var1);
      return this;
   }

   long getExpireAfterWriteNanos() {
      return this.expireAfterWriteNanos == -1L ? 0L : this.expireAfterWriteNanos;
   }

   public CacheBuilder expireAfterAccess(long var1, TimeUnit var3) {
      Preconditions.checkState(this.expireAfterAccessNanos == -1L, "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
      Preconditions.checkArgument(var1 >= 0L, "duration cannot be negative: %s %s", var1, var3);
      this.expireAfterAccessNanos = var3.toNanos(var1);
      return this;
   }

   long getExpireAfterAccessNanos() {
      return this.expireAfterAccessNanos == -1L ? 0L : this.expireAfterAccessNanos;
   }

   @Beta
   @GwtIncompatible("To be supported (synchronously).")
   public CacheBuilder refreshAfterWrite(long var1, TimeUnit var3) {
      Preconditions.checkNotNull(var3);
      Preconditions.checkState(this.refreshNanos == -1L, "refresh was already set to %s ns", this.refreshNanos);
      Preconditions.checkArgument(var1 > 0L, "duration must be positive: %s %s", var1, var3);
      this.refreshNanos = var3.toNanos(var1);
      return this;
   }

   long getRefreshNanos() {
      return this.refreshNanos == -1L ? 0L : this.refreshNanos;
   }

   public CacheBuilder ticker(Ticker var1) {
      Preconditions.checkState(this.ticker == null);
      this.ticker = (Ticker)Preconditions.checkNotNull(var1);
      return this;
   }

   Ticker getTicker(boolean var1) {
      if (this.ticker != null) {
         return this.ticker;
      } else {
         return var1 ? Ticker.systemTicker() : NULL_TICKER;
      }
   }

   @CheckReturnValue
   public CacheBuilder removalListener(RemovalListener var1) {
      Preconditions.checkState(this.removalListener == null);
      this.removalListener = (RemovalListener)Preconditions.checkNotNull(var1);
      return this;
   }

   RemovalListener getRemovalListener() {
      return (RemovalListener)Objects.firstNonNull(this.removalListener, CacheBuilder.NullListener.INSTANCE);
   }

   public CacheBuilder recordStats() {
      this.statsCounterSupplier = CACHE_STATS_COUNTER;
      return this;
   }

   boolean isRecordingStats() {
      return this.statsCounterSupplier == CACHE_STATS_COUNTER;
   }

   Supplier getStatsCounterSupplier() {
      return this.statsCounterSupplier;
   }

   public LoadingCache build(CacheLoader var1) {
      this.checkWeightWithWeigher();
      return new LocalCache.LocalLoadingCache(this, var1);
   }

   public Cache build() {
      this.checkWeightWithWeigher();
      this.checkNonLoadingCache();
      return new LocalCache.LocalManualCache(this);
   }

   private void checkNonLoadingCache() {
      Preconditions.checkState(this.refreshNanos == -1L, "refreshAfterWrite requires a LoadingCache");
   }

   private void checkWeightWithWeigher() {
      if (this.weigher == null) {
         Preconditions.checkState(this.maximumWeight == -1L, "maximumWeight requires weigher");
      } else if (this.strictParsing) {
         Preconditions.checkState(this.maximumWeight != -1L, "weigher requires maximumWeight");
      } else if (this.maximumWeight == -1L) {
         logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
      }

   }

   public String toString() {
      Objects.ToStringHelper var1 = Objects.toStringHelper((Object)this);
      if (this.initialCapacity != -1) {
         var1.add("initialCapacity", this.initialCapacity);
      }

      if (this.concurrencyLevel != -1) {
         var1.add("concurrencyLevel", this.concurrencyLevel);
      }

      if (this.maximumSize != -1L) {
         var1.add("maximumSize", this.maximumSize);
      }

      if (this.maximumWeight != -1L) {
         var1.add("maximumWeight", this.maximumWeight);
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

      if (this.valueEquivalence != null) {
         var1.addValue("valueEquivalence");
      }

      if (this.removalListener != null) {
         var1.addValue("removalListener");
      }

      return var1.toString();
   }

   static enum OneWeigher implements Weigher {
      INSTANCE;

      private static final CacheBuilder.OneWeigher[] $VALUES = new CacheBuilder.OneWeigher[]{INSTANCE};

      public int weigh(Object var1, Object var2) {
         return 1;
      }
   }

   static enum NullListener implements RemovalListener {
      INSTANCE;

      private static final CacheBuilder.NullListener[] $VALUES = new CacheBuilder.NullListener[]{INSTANCE};

      public void onRemoval(RemovalNotification var1) {
      }
   }
}
