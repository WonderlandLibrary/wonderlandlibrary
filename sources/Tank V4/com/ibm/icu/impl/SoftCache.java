package com.ibm.icu.impl;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SoftCache extends CacheBase {
   private ConcurrentHashMap map = new ConcurrentHashMap();

   public final Object getInstance(Object var1, Object var2) {
      SoftCache.SettableSoftReference var3 = (SoftCache.SettableSoftReference)this.map.get(var1);
      Object var4;
      if (var3 != null) {
         synchronized(var3){}
         var4 = SoftCache.SettableSoftReference.access$000(var3).get();
         if (var4 != null) {
            return var4;
         } else {
            var4 = this.createInstance(var1, var2);
            if (var4 != null) {
               SoftCache.SettableSoftReference.access$002(var3, new SoftReference(var4));
            }

            return var4;
         }
      } else {
         var4 = this.createInstance(var1, var2);
         if (var4 == null) {
            return null;
         } else {
            var3 = (SoftCache.SettableSoftReference)this.map.putIfAbsent(var1, new SoftCache.SettableSoftReference(var4));
            return var3 == null ? var4 : SoftCache.SettableSoftReference.access$200(var3, var4);
         }
      }
   }

   private static final class SettableSoftReference {
      private SoftReference ref;

      private SettableSoftReference(Object var1) {
         this.ref = new SoftReference(var1);
      }

      private synchronized Object setIfAbsent(Object var1) {
         Object var2 = this.ref.get();
         if (var2 == null) {
            this.ref = new SoftReference(var1);
            return var1;
         } else {
            return var2;
         }
      }

      static SoftReference access$000(SoftCache.SettableSoftReference var0) {
         return var0.ref;
      }

      static SoftReference access$002(SoftCache.SettableSoftReference var0, SoftReference var1) {
         return var0.ref = var1;
      }

      SettableSoftReference(Object var1, Object var2) {
         this(var1);
      }

      static Object access$200(SoftCache.SettableSoftReference var0, Object var1) {
         return var0.setIfAbsent(var1);
      }
   }
}
