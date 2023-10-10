package com.mojang.realmsclient.util;

import java.util.HashMap;
import java.util.Map;

public class UploadTokenCache {
   private static Map tokenCache = new HashMap();

   public static String get(long var0) {
      return (String)tokenCache.get(var0);
   }

   public static void invalidate(long var0) {
      tokenCache.remove(var0);
   }

   public static void put(long var0, String var2) {
      tokenCache.put(var0, var2);
   }
}
