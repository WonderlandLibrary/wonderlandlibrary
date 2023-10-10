package org.apache.commons.codec.digest;

import java.util.Random;

class B64 {
   static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

   static void b64from24bit(byte var0, byte var1, byte var2, int var3, StringBuilder var4) {
      int var5 = var0 << 16 & 16777215 | var1 << 8 & '\uffff' | var2 & 255;

      for(int var6 = var3; var6-- > 0; var5 >>= 6) {
         var4.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(var5 & 63));
      }

   }

   static String getRandomSalt(int var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 1; var2 <= var0; ++var2) {
         var1.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt((new Random()).nextInt(64)));
      }

      return var1.toString();
   }
}
