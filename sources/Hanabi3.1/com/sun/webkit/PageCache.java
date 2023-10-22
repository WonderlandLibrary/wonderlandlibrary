package com.sun.webkit;

public final class PageCache {
   private PageCache() {
      throw new AssertionError();
   }

   public static int getCapacity() {
      return twkGetCapacity();
   }

   public static void setCapacity(int var0) {
      if (var0 < 0) {
         throw new IllegalArgumentException("capacity is negative:" + var0);
      } else {
         twkSetCapacity(var0);
      }
   }

   private static native int twkGetCapacity();

   private static native void twkSetCapacity(int var0);
}
