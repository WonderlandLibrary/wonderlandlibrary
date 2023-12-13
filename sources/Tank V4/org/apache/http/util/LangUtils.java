package org.apache.http.util;

public final class LangUtils {
   public static final int HASH_SEED = 17;
   public static final int HASH_OFFSET = 37;

   private LangUtils() {
   }

   public static int hashCode(int var0, int var1) {
      return var0 * 37 + var1;
   }

   public static int hashCode(int var0, boolean var1) {
      return hashCode(var0, var1 ? 1 : 0);
   }

   public static int hashCode(int var0, Object var1) {
      return hashCode(var0, var1 != null ? var1.hashCode() : 0);
   }

   public static boolean equals(Object[] var0, Object[] var1) {
      if (var0 == null) {
         return var1 == null;
      } else if (var1 != null && var0.length == var1.length) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            Object var10000 = var0[var2];
            if (var1[var2] == null) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
