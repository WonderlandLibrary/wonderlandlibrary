package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.CRC32;

public abstract class ZipUtil {
   private static final byte[] DOS_TIME_MIN = ZipLong.getBytes(8448L);

   public static ZipLong toDosTime(Date var0) {
      return new ZipLong(toDosTime(var0.getTime()));
   }

   public static byte[] toDosTime(long var0) {
      Calendar var2 = Calendar.getInstance();
      var2.setTimeInMillis(var0);
      int var3 = var2.get(1);
      if (var3 < 1980) {
         return copy(DOS_TIME_MIN);
      } else {
         int var4 = var2.get(2) + 1;
         long var5 = (long)(var3 - 1980 << 25 | var4 << 21 | var2.get(5) << 16 | var2.get(11) << 11 | var2.get(12) << 5 | var2.get(13) >> 1);
         return ZipLong.getBytes(var5);
      }
   }

   public static long adjustToLong(int var0) {
      return var0 < 0 ? 4294967296L + (long)var0 : (long)var0;
   }

   public static byte[] reverse(byte[] var0) {
      int var1 = var0.length - 1;

      for(int var2 = 0; var2 < var0.length / 2; ++var2) {
         byte var3 = var0[var2];
         var0[var2] = var0[var1 - var2];
         var0[var1 - var2] = var3;
      }

      return var0;
   }

   static long bigToLong(BigInteger var0) {
      if (var0.bitLength() <= 63) {
         return var0.longValue();
      } else {
         throw new NumberFormatException("The BigInteger cannot fit inside a 64 bit java long: [" + var0 + "]");
      }
   }

   static BigInteger longToBig(long var0) {
      if (var0 < -2147483648L) {
         throw new IllegalArgumentException("Negative longs < -2^31 not permitted: [" + var0 + "]");
      } else {
         if (var0 < 0L && var0 >= -2147483648L) {
            var0 = adjustToLong((int)var0);
         }

         return BigInteger.valueOf(var0);
      }
   }

   public static int signedByteToUnsignedInt(byte var0) {
      return var0 >= 0 ? var0 : 256 + var0;
   }

   public static byte unsignedIntToSignedByte(int var0) {
      if (var0 <= 255 && var0 >= 0) {
         return var0 < 128 ? (byte)var0 : (byte)(var0 - 256);
      } else {
         throw new IllegalArgumentException("Can only convert non-negative integers between [0,255] to byte: [" + var0 + "]");
      }
   }

   public static Date fromDosTime(ZipLong var0) {
      long var1 = var0.getValue();
      return new Date(dosToJavaTime(var1));
   }

   public static long dosToJavaTime(long var0) {
      Calendar var2 = Calendar.getInstance();
      var2.set(1, (int)(var0 >> 25 & 127L) + 1980);
      var2.set(2, (int)(var0 >> 21 & 15L) - 1);
      var2.set(5, (int)(var0 >> 16) & 31);
      var2.set(11, (int)(var0 >> 11) & 31);
      var2.set(12, (int)(var0 >> 5) & 63);
      var2.set(13, (int)(var0 << 1) & 62);
      var2.set(14, 0);
      return var2.getTime().getTime();
   }

   static void setNameAndCommentFromExtraFields(ZipArchiveEntry var0, byte[] var1, byte[] var2) {
      UnicodePathExtraField var3 = (UnicodePathExtraField)var0.getExtraField(UnicodePathExtraField.UPATH_ID);
      String var4 = var0.getName();
      String var5 = getUnicodeStringIfOriginalMatches(var3, var1);
      if (var5 != null && !var4.equals(var5)) {
         var0.setName(var5);
      }

      if (var2 != null && var2.length > 0) {
         UnicodeCommentExtraField var6 = (UnicodeCommentExtraField)var0.getExtraField(UnicodeCommentExtraField.UCOM_ID);
         String var7 = getUnicodeStringIfOriginalMatches(var6, var2);
         if (var7 != null) {
            var0.setComment(var7);
         }
      }

   }

   private static String getUnicodeStringIfOriginalMatches(AbstractUnicodeExtraField var0, byte[] var1) {
      if (var0 != null) {
         CRC32 var2 = new CRC32();
         var2.update(var1);
         long var3 = var2.getValue();
         if (var3 == var0.getNameCRC32()) {
            try {
               return ZipEncodingHelper.UTF8_ZIP_ENCODING.decode(var0.getUnicodeName());
            } catch (IOException var6) {
               return null;
            }
         }
      }

      return null;
   }

   static byte[] copy(byte[] var0) {
      if (var0 != null) {
         byte[] var1 = new byte[var0.length];
         System.arraycopy(var0, 0, var1, 0, var1.length);
         return var1;
      } else {
         return null;
      }
   }

   static boolean canHandleEntryData(ZipArchiveEntry var0) {
      return var0 == false && var0 != false;
   }

   static void checkRequestedFeatures(ZipArchiveEntry var0) throws UnsupportedZipFeatureException {
      if (var0 == false) {
         throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.ENCRYPTION, var0);
      } else if (var0 != false) {
         ZipMethod var1 = ZipMethod.getMethodByCode(var0.getMethod());
         if (var1 == null) {
            throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.METHOD, var0);
         } else {
            throw new UnsupportedZipFeatureException(var1, var0);
         }
      }
   }
}
