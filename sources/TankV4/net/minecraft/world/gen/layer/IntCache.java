package net.minecraft.world.gen.layer;

import com.google.common.collect.Lists;
import java.util.List;

public class IntCache {
   private static List inUseLargeArrays = Lists.newArrayList();
   private static List inUseSmallArrays = Lists.newArrayList();
   private static List freeLargeArrays = Lists.newArrayList();
   private static List freeSmallArrays = Lists.newArrayList();
   private static int intCacheSize = 256;

   public static synchronized int[] getIntCache(int var0) {
      int[] var1;
      if (var0 <= 256) {
         if (freeSmallArrays.isEmpty()) {
            var1 = new int[256];
            inUseSmallArrays.add(var1);
            return var1;
         } else {
            var1 = (int[])freeSmallArrays.remove(freeSmallArrays.size() - 1);
            inUseSmallArrays.add(var1);
            return var1;
         }
      } else if (var0 > intCacheSize) {
         intCacheSize = var0;
         freeLargeArrays.clear();
         inUseLargeArrays.clear();
         var1 = new int[intCacheSize];
         inUseLargeArrays.add(var1);
         return var1;
      } else if (freeLargeArrays.isEmpty()) {
         var1 = new int[intCacheSize];
         inUseLargeArrays.add(var1);
         return var1;
      } else {
         var1 = (int[])freeLargeArrays.remove(freeLargeArrays.size() - 1);
         inUseLargeArrays.add(var1);
         return var1;
      }
   }

   public static synchronized void resetIntCache() {
      if (!freeLargeArrays.isEmpty()) {
         freeLargeArrays.remove(freeLargeArrays.size() - 1);
      }

      if (!freeSmallArrays.isEmpty()) {
         freeSmallArrays.remove(freeSmallArrays.size() - 1);
      }

      freeLargeArrays.addAll(inUseLargeArrays);
      freeSmallArrays.addAll(inUseSmallArrays);
      inUseLargeArrays.clear();
      inUseSmallArrays.clear();
   }

   public static synchronized String getCacheSizes() {
      return "cache: " + freeLargeArrays.size() + ", tcache: " + freeSmallArrays.size() + ", allocated: " + inUseLargeArrays.size() + ", tallocated: " + inUseSmallArrays.size();
   }
}
