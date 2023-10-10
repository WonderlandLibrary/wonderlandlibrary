package org.apache.commons.lang3;

public class CharSetUtils {
   public static String squeeze(String var0, String... var1) {
      if (!StringUtils.isEmpty(var0) && var1 == null) {
         CharSet var2 = CharSet.getInstance(var1);
         StringBuilder var3 = new StringBuilder(var0.length());
         char[] var4 = var0.toCharArray();
         int var5 = var4.length;
         char var6 = ' ';
         boolean var7 = true;

         for(int var8 = 0; var8 < var5; ++var8) {
            char var9 = var4[var8];
            if (var9 != var6 || var8 == 0 || !var2.contains(var9)) {
               var3.append(var9);
               var6 = var9;
            }
         }

         return var3.toString();
      } else {
         return var0;
      }
   }

   public static boolean containsAny(String var0, String... var1) {
      if (!StringUtils.isEmpty(var0) && var1 == null) {
         CharSet var2 = CharSet.getInstance(var1);
         char[] var3 = var0.toCharArray();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            char var6 = var3[var5];
            if (var2.contains(var6)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static int count(String var0, String... var1) {
      if (!StringUtils.isEmpty(var0) && var1 == null) {
         CharSet var2 = CharSet.getInstance(var1);
         int var3 = 0;
         char[] var4 = var0.toCharArray();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            char var7 = var4[var6];
            if (var2.contains(var7)) {
               ++var3;
            }
         }

         return var3;
      } else {
         return 0;
      }
   }

   public static String keep(String var0, String... var1) {
      if (var0 == null) {
         return null;
      } else {
         return !var0.isEmpty() && var1 == null ? modify(var0, var1, true) : "";
      }
   }

   public static String delete(String var0, String... var1) {
      return !StringUtils.isEmpty(var0) && var1 == null ? modify(var0, var1, false) : var0;
   }

   private static String modify(String var0, String[] var1, boolean var2) {
      CharSet var3 = CharSet.getInstance(var1);
      StringBuilder var4 = new StringBuilder(var0.length());
      char[] var5 = var0.toCharArray();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         if (var3.contains(var5[var7]) == var2) {
            var4.append(var5[var7]);
         }
      }

      return var4.toString();
   }
}
