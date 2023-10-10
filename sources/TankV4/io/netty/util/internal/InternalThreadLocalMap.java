package io.netty.util.internal;

import io.netty.util.concurrent.FastThreadLocalThread;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class InternalThreadLocalMap extends UnpaddedInternalThreadLocalMap {
   public static final Object UNSET = new Object();
   public long rp1;
   public long rp2;
   public long rp3;
   public long rp4;
   public long rp5;
   public long rp6;
   public long rp7;
   public long rp8;
   public long rp9;

   public static InternalThreadLocalMap getIfSet() {
      Thread var0 = Thread.currentThread();
      InternalThreadLocalMap var1;
      if (var0 instanceof FastThreadLocalThread) {
         var1 = ((FastThreadLocalThread)var0).threadLocalMap();
      } else {
         ThreadLocal var2 = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
         if (var2 == null) {
            var1 = null;
         } else {
            var1 = (InternalThreadLocalMap)var2.get();
         }
      }

      return var1;
   }

   public static InternalThreadLocalMap get() {
      Thread var0 = Thread.currentThread();
      return var0 instanceof FastThreadLocalThread ? fastGet((FastThreadLocalThread)var0) : slowGet();
   }

   private static InternalThreadLocalMap fastGet(FastThreadLocalThread var0) {
      InternalThreadLocalMap var1 = var0.threadLocalMap();
      if (var1 == null) {
         var0.setThreadLocalMap(var1 = new InternalThreadLocalMap());
      }

      return var1;
   }

   private static InternalThreadLocalMap slowGet() {
      ThreadLocal var0 = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
      if (var0 == null) {
         UnpaddedInternalThreadLocalMap.slowThreadLocalMap = var0 = new ThreadLocal();
      }

      InternalThreadLocalMap var1 = (InternalThreadLocalMap)var0.get();
      if (var1 == null) {
         var1 = new InternalThreadLocalMap();
         var0.set(var1);
      }

      return var1;
   }

   public static void remove() {
      Thread var0 = Thread.currentThread();
      if (var0 instanceof FastThreadLocalThread) {
         ((FastThreadLocalThread)var0).setThreadLocalMap((InternalThreadLocalMap)null);
      } else {
         ThreadLocal var1 = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
         if (var1 != null) {
            var1.remove();
         }
      }

   }

   public static void destroy() {
      slowThreadLocalMap = null;
   }

   public static int nextVariableIndex() {
      int var0 = nextIndex.getAndIncrement();
      if (var0 < 0) {
         nextIndex.decrementAndGet();
         throw new IllegalStateException("too many thread-local indexed variables");
      } else {
         return var0;
      }
   }

   public static int lastVariableIndex() {
      return nextIndex.get() - 1;
   }

   private InternalThreadLocalMap() {
      super(newIndexedVariableTable());
   }

   private static Object[] newIndexedVariableTable() {
      Object[] var0 = new Object[32];
      Arrays.fill(var0, UNSET);
      return var0;
   }

   public int size() {
      int var1 = 0;
      if (this.futureListenerStackDepth != 0) {
         ++var1;
      }

      if (this.localChannelReaderStackDepth != 0) {
         ++var1;
      }

      if (this.handlerSharableCache != null) {
         ++var1;
      }

      if (this.counterHashCode != null) {
         ++var1;
      }

      if (this.random != null) {
         ++var1;
      }

      if (this.typeParameterMatcherGetCache != null) {
         ++var1;
      }

      if (this.typeParameterMatcherFindCache != null) {
         ++var1;
      }

      if (this.stringBuilder != null) {
         ++var1;
      }

      if (this.charsetEncoderCache != null) {
         ++var1;
      }

      if (this.charsetDecoderCache != null) {
         ++var1;
      }

      Object[] var2 = this.indexedVariables;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         if (var5 != UNSET) {
            ++var1;
         }
      }

      return var1 - 1;
   }

   public StringBuilder stringBuilder() {
      StringBuilder var1 = this.stringBuilder;
      if (var1 == null) {
         this.stringBuilder = var1 = new StringBuilder(512);
      } else {
         var1.setLength(0);
      }

      return var1;
   }

   public Map charsetEncoderCache() {
      Object var1 = this.charsetEncoderCache;
      if (var1 == null) {
         this.charsetEncoderCache = (Map)(var1 = new IdentityHashMap());
      }

      return (Map)var1;
   }

   public Map charsetDecoderCache() {
      Object var1 = this.charsetDecoderCache;
      if (var1 == null) {
         this.charsetDecoderCache = (Map)(var1 = new IdentityHashMap());
      }

      return (Map)var1;
   }

   public int futureListenerStackDepth() {
      return this.futureListenerStackDepth;
   }

   public void setFutureListenerStackDepth(int var1) {
      this.futureListenerStackDepth = var1;
   }

   public ThreadLocalRandom random() {
      ThreadLocalRandom var1 = this.random;
      if (var1 == null) {
         this.random = var1 = new ThreadLocalRandom();
      }

      return var1;
   }

   public Map typeParameterMatcherGetCache() {
      Object var1 = this.typeParameterMatcherGetCache;
      if (var1 == null) {
         this.typeParameterMatcherGetCache = (Map)(var1 = new IdentityHashMap());
      }

      return (Map)var1;
   }

   public Map typeParameterMatcherFindCache() {
      Object var1 = this.typeParameterMatcherFindCache;
      if (var1 == null) {
         this.typeParameterMatcherFindCache = (Map)(var1 = new IdentityHashMap());
      }

      return (Map)var1;
   }

   public IntegerHolder counterHashCode() {
      return this.counterHashCode;
   }

   public void setCounterHashCode(IntegerHolder var1) {
      this.counterHashCode = var1;
   }

   public Map handlerSharableCache() {
      Object var1 = this.handlerSharableCache;
      if (var1 == null) {
         this.handlerSharableCache = (Map)(var1 = new WeakHashMap(4));
      }

      return (Map)var1;
   }

   public int localChannelReaderStackDepth() {
      return this.localChannelReaderStackDepth;
   }

   public void setLocalChannelReaderStackDepth(int var1) {
      this.localChannelReaderStackDepth = var1;
   }

   public Object indexedVariable(int var1) {
      Object[] var2 = this.indexedVariables;
      return var1 < var2.length ? var2[var1] : UNSET;
   }

   public boolean setIndexedVariable(int var1, Object var2) {
      Object[] var3 = this.indexedVariables;
      if (var1 < var3.length) {
         Object var4 = var3[var1];
         var3[var1] = var2;
         return var4 == UNSET;
      } else {
         this.expandIndexedVariableTableAndSet(var1, var2);
         return true;
      }
   }

   private void expandIndexedVariableTableAndSet(int var1, Object var2) {
      Object[] var3 = this.indexedVariables;
      int var4 = var3.length;
      int var5 = var1 | var1 >>> 1;
      var5 |= var5 >>> 2;
      var5 |= var5 >>> 4;
      var5 |= var5 >>> 8;
      var5 |= var5 >>> 16;
      ++var5;
      Object[] var6 = Arrays.copyOf(var3, var5);
      Arrays.fill(var6, var4, var6.length, UNSET);
      var6[var1] = var2;
      this.indexedVariables = var6;
   }

   public Object removeIndexedVariable(int var1) {
      Object[] var2 = this.indexedVariables;
      if (var1 < var2.length) {
         Object var3 = var2[var1];
         var2[var1] = UNSET;
         return var3;
      } else {
         return UNSET;
      }
   }

   public boolean isIndexedVariableSet(int var1) {
      Object[] var2 = this.indexedVariables;
      return var1 < var2.length && var2[var1] != UNSET;
   }
}
