package com.ibm.icu.impl;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SimpleCache implements ICUCache {
   private static final int DEFAULT_CAPACITY = 16;
   private Reference cacheRef;
   private int type;
   private int capacity;

   public SimpleCache() {
      this.cacheRef = null;
      this.type = 0;
      this.capacity = 16;
   }

   public SimpleCache(int var1) {
      this(var1, 16);
   }

   public SimpleCache(int var1, int var2) {
      this.cacheRef = null;
      this.type = 0;
      this.capacity = 16;
      if (var1 == 1) {
         this.type = var1;
      }

      if (var2 > 0) {
         this.capacity = var2;
      }

   }

   public Object get(Object var1) {
      Reference var2 = this.cacheRef;
      if (var2 != null) {
         Map var3 = (Map)var2.get();
         if (var3 != null) {
            return var3.get(var1);
         }
      }

      return null;
   }

   public void put(Object var1, Object var2) {
      Reference var3 = this.cacheRef;
      Map var4 = null;
      if (var3 != null) {
         var4 = (Map)var3.get();
      }

      if (var4 == null) {
         var4 = Collections.synchronizedMap(new HashMap(this.capacity));
         Object var5;
         if (this.type == 1) {
            var5 = new WeakReference(var4);
         } else {
            var5 = new SoftReference(var4);
         }

         this.cacheRef = (Reference)var5;
      }

      var4.put(var1, var2);
   }

   public void clear() {
      this.cacheRef = null;
   }
}
