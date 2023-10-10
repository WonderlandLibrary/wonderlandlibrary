package org.apache.commons.compress.utils;

import java.io.UnsupportedEncodingException;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtils {
   private ArchiveUtils() {
   }

   public static String toString(ArchiveEntry var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append((char)(var0.isDirectory() ? 'd' : '-'));
      String var2 = Long.toString(var0.getSize());
      var1.append(' ');

      for(int var3 = 7; var3 > var2.length(); --var3) {
         var1.append(' ');
      }

      var1.append(var2);
      var1.append(' ').append(var0.getName());
      return var1.toString();
   }

   public static boolean matchAsciiBuffer(String var0, byte[] var1, int var2, int var3) {
      byte[] var4;
      try {
         var4 = var0.getBytes("US-ASCII");
      } catch (UnsupportedEncodingException var6) {
         throw new RuntimeException(var6);
      }

      return isEqual(var4, 0, var4.length, var1, var2, var3, false);
   }

   public static boolean matchAsciiBuffer(String var0, byte[] var1) {
      return matchAsciiBuffer(var0, var1, 0, var1.length);
   }

   public static byte[] toAsciiBytes(String var0) {
      try {
         return var0.getBytes("US-ASCII");
      } catch (UnsupportedEncodingException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static String toAsciiString(byte[] var0) {
      try {
         return new String(var0, "US-ASCII");
      } catch (UnsupportedEncodingException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static String toAsciiString(byte[] var0, int var1, int var2) {
      try {
         return new String(var0, var1, var2, "US-ASCII");
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }
   }

   public static boolean isEqual(byte[] var0, int var1, int var2, byte[] var3, int var4, int var5, boolean var6) {
      int var7 = var2 < var5 ? var2 : var5;

      int var8;
      for(var8 = 0; var8 < var7; ++var8) {
         if (var0[var1 + var8] != var3[var4 + var8]) {
            return false;
         }
      }

      if (var2 == var5) {
         return true;
      } else if (!var6) {
         return false;
      } else {
         if (var2 > var5) {
            for(var8 = var5; var8 < var2; ++var8) {
               if (var0[var1 + var8] != 0) {
                  return false;
               }
            }
         } else {
            for(var8 = var2; var8 < var5; ++var8) {
               if (var3[var4 + var8] != 0) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public static boolean isEqual(byte[] var0, int var1, int var2, byte[] var3, int var4, int var5) {
      return isEqual(var0, var1, var2, var3, var4, var5, false);
   }

   public static boolean isEqual(byte[] var0, byte[] var1) {
      return isEqual(var0, 0, var0.length, var1, 0, var1.length, false);
   }

   public static boolean isEqual(byte[] var0, byte[] var1, boolean var2) {
      return isEqual(var0, 0, var0.length, var1, 0, var1.length, var2);
   }

   public static boolean isEqualWithNull(byte[] var0, int var1, int var2, byte[] var3, int var4, int var5) {
      return isEqual(var0, var1, var2, var3, var4, var5, true);
   }

   public static boolean isArrayZero(byte[] var0, int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         if (var0[var2] != 0) {
            return false;
         }
      }

      return true;
   }
}
