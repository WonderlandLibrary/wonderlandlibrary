package org.apache.commons.codec.digest;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.Charsets;

public class Md5Crypt {
   static final String APR1_PREFIX = "$apr1$";
   private static final int BLOCKSIZE = 16;
   static final String MD5_PREFIX = "$1$";
   private static final int ROUNDS = 1000;

   public static String apr1Crypt(byte[] var0) {
      return apr1Crypt(var0, "$apr1$" + B64.getRandomSalt(8));
   }

   public static String apr1Crypt(byte[] var0, String var1) {
      if (var1 != null && !var1.startsWith("$apr1$")) {
         var1 = "$apr1$" + var1;
      }

      return md5Crypt(var0, var1, "$apr1$");
   }

   public static String apr1Crypt(String var0) {
      return apr1Crypt(var0.getBytes(Charsets.UTF_8));
   }

   public static String apr1Crypt(String var0, String var1) {
      return apr1Crypt(var0.getBytes(Charsets.UTF_8), var1);
   }

   public static String md5Crypt(byte[] var0) {
      return md5Crypt(var0, "$1$" + B64.getRandomSalt(8));
   }

   public static String md5Crypt(byte[] var0, String var1) {
      return md5Crypt(var0, var1, "$1$");
   }

   public static String md5Crypt(byte[] var0, String var1, String var2) {
      int var3 = var0.length;
      String var4;
      if (var1 == null) {
         var4 = B64.getRandomSalt(8);
      } else {
         Pattern var5 = Pattern.compile("^" + var2.replace("$", "\\$") + "([\\.\\/a-zA-Z0-9]{1,8}).*");
         Matcher var6 = var5.matcher(var1);
         if (var6 == null || !var6.find()) {
            throw new IllegalArgumentException("Invalid salt value: " + var1);
         }

         var4 = var6.group(1);
      }

      byte[] var13 = var4.getBytes(Charsets.UTF_8);
      MessageDigest var14 = DigestUtils.getMd5Digest();
      var14.update(var0);
      var14.update(var2.getBytes(Charsets.UTF_8));
      var14.update(var13);
      MessageDigest var7 = DigestUtils.getMd5Digest();
      var7.update(var0);
      var7.update(var13);
      var7.update(var0);
      byte[] var8 = var7.digest();

      int var9;
      for(var9 = var3; var9 > 0; var9 -= 16) {
         var14.update(var8, 0, var9 > 16 ? 16 : var9);
      }

      Arrays.fill(var8, (byte)0);
      var9 = var3;

      for(boolean var10 = false; var9 > 0; var9 >>= 1) {
         if ((var9 & 1) == 1) {
            var14.update(var8[0]);
         } else {
            var14.update(var0[0]);
         }
      }

      StringBuilder var11 = new StringBuilder(var2 + var4 + "$");
      var8 = var14.digest();

      for(int var12 = 0; var12 < 1000; ++var12) {
         var7 = DigestUtils.getMd5Digest();
         if ((var12 & 1) != 0) {
            var7.update(var0);
         } else {
            var7.update(var8, 0, 16);
         }

         if (var12 % 3 != 0) {
            var7.update(var13);
         }

         if (var12 % 7 != 0) {
            var7.update(var0);
         }

         if ((var12 & 1) != 0) {
            var7.update(var8, 0, 16);
         } else {
            var7.update(var0);
         }

         var8 = var7.digest();
      }

      B64.b64from24bit(var8[0], var8[6], var8[12], 4, var11);
      B64.b64from24bit(var8[1], var8[7], var8[13], 4, var11);
      B64.b64from24bit(var8[2], var8[8], var8[14], 4, var11);
      B64.b64from24bit(var8[3], var8[9], var8[15], 4, var11);
      B64.b64from24bit(var8[4], var8[10], var8[5], 4, var11);
      B64.b64from24bit((byte)0, (byte)0, var8[11], 2, var11);
      var14.reset();
      var7.reset();
      Arrays.fill(var0, (byte)0);
      Arrays.fill(var13, (byte)0);
      Arrays.fill(var8, (byte)0);
      return var11.toString();
   }
}
