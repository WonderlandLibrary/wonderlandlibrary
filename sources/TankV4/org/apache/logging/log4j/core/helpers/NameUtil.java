package org.apache.logging.log4j.core.helpers;

import java.security.MessageDigest;

public final class NameUtil {
   private static final int MASK = 255;

   private NameUtil() {
   }

   public static String getSubName(String var0) {
      if (var0.isEmpty()) {
         return null;
      } else {
         int var1 = var0.lastIndexOf(46);
         return var1 > 0 ? var0.substring(0, var1) : "";
      }
   }

   public static String md5(String var0) {
      try {
         MessageDigest var1 = MessageDigest.getInstance("MD5");
         var1.update(var0.getBytes());
         byte[] var2 = var1.digest();
         StringBuilder var3 = new StringBuilder();
         byte[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            byte var7 = var4[var6];
            String var8 = Integer.toHexString(255 & var7);
            if (var8.length() == 1) {
               var3.append('0');
            }

            var3.append(var8);
         }

         return var3.toString();
      } catch (Exception var9) {
         return var0;
      }
   }
}
