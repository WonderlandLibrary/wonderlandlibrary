package org.apache.commons.compress.archivers.tar;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;

public class TarUtils {
   private static final int BYTE_MASK = 255;
   static final ZipEncoding DEFAULT_ENCODING = ZipEncodingHelper.getZipEncoding((String)null);
   static final ZipEncoding FALLBACK_ENCODING = new ZipEncoding() {
      public boolean canEncode(String var1) {
         return true;
      }

      public ByteBuffer encode(String var1) {
         int var2 = var1.length();
         byte[] var3 = new byte[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = (byte)var1.charAt(var4);
         }

         return ByteBuffer.wrap(var3);
      }

      public String decode(byte[] var1) {
         int var2 = var1.length;
         StringBuilder var3 = new StringBuilder(var2);

         for(int var4 = 0; var4 < var2; ++var4) {
            byte var5 = var1[var4];
            if (var5 == 0) {
               break;
            }

            var3.append((char)(var5 & 255));
         }

         return var3.toString();
      }
   };

   private TarUtils() {
   }

   public static long parseOctal(byte[] var0, int var1, int var2) {
      long var3 = 0L;
      int var5 = var1 + var2;
      int var6 = var1;
      if (var2 < 2) {
         throw new IllegalArgumentException("Length " + var2 + " must be at least 2");
      } else if (var0[var1] == 0) {
         return 0L;
      } else {
         while(var6 < var5 && var0[var6] == 32) {
            ++var6;
         }

         for(byte var7 = var0[var5 - 1]; var6 < var5 && (var7 == 0 || var7 == 32); var7 = var0[var5 - 1]) {
            --var5;
         }

         while(var6 < var5) {
            byte var8 = var0[var6];
            if (var8 < 48 || var8 > 55) {
               throw new IllegalArgumentException(exceptionMessage(var0, var1, var2, var6, var8));
            }

            var3 = (var3 << 3) + (long)(var8 - 48);
            ++var6;
         }

         return var3;
      }
   }

   public static long parseOctalOrBinary(byte[] var0, int var1, int var2) {
      if ((var0[var1] & 128) == 0) {
         return parseOctal(var0, var1, var2);
      } else {
         boolean var3 = var0[var1] == -1;
         return var2 < 9 ? parseBinaryLong(var0, var1, var2, var3) : parseBinaryBigInteger(var0, var1, var2, var3);
      }
   }

   private static long parseBinaryLong(byte[] var0, int var1, int var2, boolean var3) {
      if (var2 >= 9) {
         throw new IllegalArgumentException("At offset " + var1 + ", " + var2 + " byte binary number" + " exceeds maximum signed long" + " value");
      } else {
         long var4 = 0L;

         for(int var6 = 1; var6 < var2; ++var6) {
            var4 = (var4 << 8) + (long)(var0[var1 + var6] & 255);
         }

         if (var3) {
            --var4;
            var4 ^= (long)Math.pow(2.0D, (double)((var2 - 1) * 8)) - 1L;
         }

         return var3 ? -var4 : var4;
      }
   }

   private static long parseBinaryBigInteger(byte[] var0, int var1, int var2, boolean var3) {
      byte[] var4 = new byte[var2 - 1];
      System.arraycopy(var0, var1 + 1, var4, 0, var2 - 1);
      BigInteger var5 = new BigInteger(var4);
      if (var3) {
         var5 = var5.add(BigInteger.valueOf(-1L)).not();
      }

      if (var5.bitLength() > 63) {
         throw new IllegalArgumentException("At offset " + var1 + ", " + var2 + " byte binary number" + " exceeds maximum signed long" + " value");
      } else {
         return var3 ? -var5.longValue() : var5.longValue();
      }
   }

   public static boolean parseBoolean(byte[] var0, int var1) {
      return var0[var1] == 1;
   }

   private static String exceptionMessage(byte[] var0, int var1, int var2, int var3, byte var4) {
      String var5 = new String(var0, var1, var2);
      var5 = var5.replaceAll("\u0000", "{NUL}");
      String var6 = "Invalid byte " + var4 + " at offset " + (var3 - var1) + " in '" + var5 + "' len=" + var2;
      return var6;
   }

   public static String parseName(byte[] var0, int var1, int var2) {
      try {
         return parseName(var0, var1, var2, DEFAULT_ENCODING);
      } catch (IOException var6) {
         try {
            return parseName(var0, var1, var2, FALLBACK_ENCODING);
         } catch (IOException var5) {
            throw new RuntimeException(var5);
         }
      }
   }

   public static String parseName(byte[] var0, int var1, int var2, ZipEncoding var3) throws IOException {
      int var4;
      for(var4 = var2; var4 > 0 && var0[var1 + var4 - 1] == 0; --var4) {
      }

      if (var4 > 0) {
         byte[] var5 = new byte[var4];
         System.arraycopy(var0, var1, var5, 0, var4);
         return var3.decode(var5);
      } else {
         return "";
      }
   }

   public static int formatNameBytes(String var0, byte[] var1, int var2, int var3) {
      try {
         return formatNameBytes(var0, var1, var2, var3, DEFAULT_ENCODING);
      } catch (IOException var7) {
         try {
            return formatNameBytes(var0, var1, var2, var3, FALLBACK_ENCODING);
         } catch (IOException var6) {
            throw new RuntimeException(var6);
         }
      }
   }

   public static int formatNameBytes(String var0, byte[] var1, int var2, int var3, ZipEncoding var4) throws IOException {
      int var5 = var0.length();

      ByteBuffer var6;
      for(var6 = var4.encode(var0); var6.limit() > var3 && var5 > 0; var6 = var4.encode(var0.substring(0, var5))) {
         --var5;
      }

      int var7 = var6.limit() - var6.position();
      System.arraycopy(var6.array(), var6.arrayOffset(), var1, var2, var7);

      for(int var8 = var7; var8 < var3; ++var8) {
         var1[var2 + var8] = 0;
      }

      return var2 + var3;
   }

   public static void formatUnsignedOctalString(long var0, byte[] var2, int var3, int var4) {
      int var5 = var4 - 1;
      if (var0 == 0L) {
         var2[var3 + var5--] = 48;
      } else {
         long var6;
         for(var6 = var0; var5 >= 0 && var6 != 0L; --var5) {
            var2[var3 + var5] = (byte)(48 + (byte)((int)(var6 & 7L)));
            var6 >>>= 3;
         }

         if (var6 != 0L) {
            throw new IllegalArgumentException(var0 + "=" + Long.toOctalString(var0) + " will not fit in octal number buffer of length " + var4);
         }
      }

      while(var5 >= 0) {
         var2[var3 + var5] = 48;
         --var5;
      }

   }

   public static int formatOctalBytes(long var0, byte[] var2, int var3, int var4) {
      int var5 = var4 - 2;
      formatUnsignedOctalString(var0, var2, var3, var5);
      var2[var3 + var5++] = 32;
      var2[var3 + var5] = 0;
      return var3 + var4;
   }

   public static int formatLongOctalBytes(long var0, byte[] var2, int var3, int var4) {
      int var5 = var4 - 1;
      formatUnsignedOctalString(var0, var2, var3, var5);
      var2[var3 + var5] = 32;
      return var3 + var4;
   }

   public static int formatLongOctalOrBinaryBytes(long var0, byte[] var2, int var3, int var4) {
      long var5 = var4 == 8 ? 2097151L : 8589934591L;
      boolean var7 = var0 < 0L;
      if (!var7 && var0 <= var5) {
         return formatLongOctalBytes(var0, var2, var3, var4);
      } else {
         if (var4 < 9) {
            formatLongBinary(var0, var2, var3, var4, var7);
         }

         formatBigIntegerBinary(var0, var2, var3, var4, var7);
         var2[var3] = (byte)(var7 ? 255 : 128);
         return var3 + var4;
      }
   }

   private static void formatLongBinary(long var0, byte[] var2, int var3, int var4, boolean var5) {
      int var6 = (var4 - 1) * 8;
      long var7 = 1L << var6;
      long var9 = Math.abs(var0);
      if (var9 >= var7) {
         throw new IllegalArgumentException("Value " + var0 + " is too large for " + var4 + " byte field.");
      } else {
         if (var5) {
            var9 ^= var7 - 1L;
            var9 |= (long)(255 << var6);
            ++var9;
         }

         for(int var11 = var3 + var4 - 1; var11 >= var3; --var11) {
            var2[var11] = (byte)((int)var9);
            var9 >>= 8;
         }

      }
   }

   private static void formatBigIntegerBinary(long var0, byte[] var2, int var3, int var4, boolean var5) {
      BigInteger var6 = BigInteger.valueOf(var0);
      byte[] var7 = var6.toByteArray();
      int var8 = var7.length;
      int var9 = var3 + var4 - var8;
      System.arraycopy(var7, 0, var2, var9, var8);
      byte var10 = (byte)(var5 ? 255 : 0);

      for(int var11 = var3 + 1; var11 < var9; ++var11) {
         var2[var11] = var10;
      }

   }

   public static int formatCheckSumOctalBytes(long var0, byte[] var2, int var3, int var4) {
      int var5 = var4 - 2;
      formatUnsignedOctalString(var0, var2, var3, var5);
      var2[var3 + var5++] = 0;
      var2[var3 + var5] = 32;
      return var3 + var4;
   }

   public static long computeCheckSum(byte[] var0) {
      long var1 = 0L;
      byte[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         byte var6 = var3[var5];
         var1 += (long)(255 & var6);
      }

      return var1;
   }

   public static boolean verifyCheckSum(byte[] var0) {
      long var1 = 0L;
      long var3 = 0L;
      long var5 = 0L;
      int var7 = 0;

      for(int var8 = 0; var8 < var0.length; ++var8) {
         byte var9 = var0[var8];
         if (148 <= var8 && var8 < 156) {
            if (48 <= var9 && var9 <= 55 && var7++ < 6) {
               var1 = var1 * 8L + (long)var9 - 48L;
            } else if (var7 > 0) {
               var7 = 6;
            }

            var9 = 32;
         }

         var3 += (long)(255 & var9);
         var5 += (long)var9;
      }

      return var1 == var3 || var1 == var5 || var1 > var3;
   }
}
