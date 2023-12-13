package org.apache.commons.codec.binary;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.commons.codec.Charsets;

public class StringUtils {
   private static byte[] getBytes(String var0, Charset var1) {
      return var0 == null ? null : var0.getBytes(var1);
   }

   public static byte[] getBytesIso8859_1(String var0) {
      return getBytes(var0, Charsets.ISO_8859_1);
   }

   public static byte[] getBytesUnchecked(String var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         try {
            return var0.getBytes(var1);
         } catch (UnsupportedEncodingException var3) {
            throw newIllegalStateException(var1, var3);
         }
      }
   }

   public static byte[] getBytesUsAscii(String var0) {
      return getBytes(var0, Charsets.US_ASCII);
   }

   public static byte[] getBytesUtf16(String var0) {
      return getBytes(var0, Charsets.UTF_16);
   }

   public static byte[] getBytesUtf16Be(String var0) {
      return getBytes(var0, Charsets.UTF_16BE);
   }

   public static byte[] getBytesUtf16Le(String var0) {
      return getBytes(var0, Charsets.UTF_16LE);
   }

   public static byte[] getBytesUtf8(String var0) {
      return getBytes(var0, Charsets.UTF_8);
   }

   private static IllegalStateException newIllegalStateException(String var0, UnsupportedEncodingException var1) {
      return new IllegalStateException(var0 + ": " + var1);
   }

   private static String newString(byte[] var0, Charset var1) {
      return var0 == null ? null : new String(var0, var1);
   }

   public static String newString(byte[] var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         try {
            return new String(var0, var1);
         } catch (UnsupportedEncodingException var3) {
            throw newIllegalStateException(var1, var3);
         }
      }
   }

   public static String newStringIso8859_1(byte[] var0) {
      return new String(var0, Charsets.ISO_8859_1);
   }

   public static String newStringUsAscii(byte[] var0) {
      return new String(var0, Charsets.US_ASCII);
   }

   public static String newStringUtf16(byte[] var0) {
      return new String(var0, Charsets.UTF_16);
   }

   public static String newStringUtf16Be(byte[] var0) {
      return new String(var0, Charsets.UTF_16BE);
   }

   public static String newStringUtf16Le(byte[] var0) {
      return new String(var0, Charsets.UTF_16LE);
   }

   public static String newStringUtf8(byte[] var0) {
      return newString(var0, Charsets.UTF_8);
   }
}
