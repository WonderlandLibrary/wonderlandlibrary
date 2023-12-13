package com.ibm.icu.impl.locale;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

public abstract class LocaleObjectCache {
   private ConcurrentHashMap _map;
   private ReferenceQueue _queue;

   public LocaleObjectCache() {
      this(16, 0.75F, 16);
   }

   public LocaleObjectCache(int var1, float var2, int var3) {
      this._queue = new ReferenceQueue();
      this._map = new ConcurrentHashMap(var1, var2, var3);
   }

   public Object get(Object var1) {
      Object var2 = null;
      this.cleanStaleEntries();
      LocaleObjectCache.CacheEntry var3 = (LocaleObjectCache.CacheEntry)this._map.get(var1);
      if (var3 != null) {
         var2 = var3.get();
      }

      if (var2 == null) {
         var1 = this.normalizeKey(var1);
         Object var4 = this.createObject(var1);
         if (var1 == null || var4 == null) {
            return null;
         }

         for(LocaleObjectCache.CacheEntry var5 = new LocaleObjectCache.CacheEntry(var1, var4, this._queue); var2 == null; var2 = var3.get()) {
            this.cleanStaleEntries();
            var3 = (LocaleObjectCache.CacheEntry)this._map.putIfAbsent(var1, var5);
            if (var3 == null) {
               var2 = var4;
               break;
            }
         }
      }

      return var2;
   }

   private void cleanStaleEntries() {
      LocaleObjectCache.CacheEntry var1;
      while((var1 = (LocaleObjectCache.CacheEntry)this._queue.poll()) != null) {
         this._map.remove(var1.getKey());
      }

   }

   protected abstract Object createObject(Object var1);

   protected Object normalizeKey(Object var1) {
      return var1;
   }

   private static class CacheEntry extends SoftReference {
      private Object _key;

      CacheEntry(Object var1, Object var2, ReferenceQueue var3) {
         super(var2, var3);
         this._key = var1;
      }

      Object getKey() {
         return this._key;
      }
   }
}
