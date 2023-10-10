package org.apache.commons.codec.binary;

import java.nio.charset.Charset;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class Hex implements BinaryEncoder, BinaryDecoder {
   public static final Charset DEFAULT_CHARSET;
   public static final String DEFAULT_CHARSET_NAME = "UTF-8";
   private static final char[] DIGITS_LOWER;
   private static final char[] DIGITS_UPPER;
   private final Charset charset;

   public static byte[] decodeHex(char[] var0) throws DecoderException {
      int var1 = var0.length;
      if ((var1 & 1) != 0) {
         throw new DecoderException("Odd number of characters.");
      } else {
         byte[] var2 = new byte[var1 >> 1];
         int var3 = 0;

         for(int var4 = 0; var4 < var1; ++var3) {
            int var5 = toDigit(var0[var4], var4) << 4;
            ++var4;
            var5 |= toDigit(var0[var4], var4);
            ++var4;
            var2[var3] = (byte)(var5 & 255);
         }

         return var2;
      }
   }

   public static char[] encodeHex(byte[] var0) {
      return encodeHex(var0, true);
   }

   public static char[] encodeHex(byte[] var0, boolean var1) {
      return encodeHex(var0, var1 ? DIGITS_LOWER : DIGITS_UPPER);
   }

   protected static char[] encodeHex(byte[] var0, char[] var1) {
      int var2 = var0.length;
      char[] var3 = new char[var2 << 1];
      int var4 = 0;

      for(int var5 = 0; var4 < var2; ++var4) {
         var3[var5++] = var1[(240 & var0[var4]) >>> 4];
         var3[var5++] = var1[15 & var0[var4]];
      }

      return var3;
   }

   public static String encodeHexString(byte[] var0) {
      return new String(encodeHex(var0));
   }

   protected static int toDigit(char var0, int var1) throws DecoderException {
      int var2 = Character.digit(var0, 16);
      if (var2 == -1) {
         throw new DecoderException("Illegal hexadecimal character " + var0 + " at index " + var1);
      } else {
         return var2;
      }
   }

   public Hex() {
      this.charset = DEFAULT_CHARSET;
   }

   public Hex(Charset var1) {
      this.charset = var1;
   }

   public Hex(String var1) {
      this(Charset.forName(var1));
   }

   public byte[] decode(byte[] var1) throws DecoderException {
      return decodeHex((new String(var1, this.getCharset())).toCharArray());
   }

   public Object decode(Object var1) throws DecoderException {
      try {
         char[] var2 = var1 instanceof String ? ((String)var1).toCharArray() : (char[])((char[])var1);
         return decodeHex(var2);
      } catch (ClassCastException var3) {
         throw new DecoderException(var3.getMessage(), var3);
      }
   }

   public byte[] encode(byte[] var1) {
      return encodeHexString(var1).getBytes(this.getCharset());
   }

   public Object encode(Object var1) throws EncoderException {
      try {
         byte[] var2 = var1 instanceof String ? ((String)var1).getBytes(this.getCharset()) : (byte[])((byte[])var1);
         return encodeHex(var2);
      } catch (ClassCastException var3) {
         throw new EncoderException(var3.getMessage(), var3);
      }
   }

   public Charset getCharset() {
      return this.charset;
   }

   public String getCharsetName() {
      return this.charset.name();
   }

   public String toString() {
      return super.toString() + "[charsetName=" + this.charset + "]";
   }

   static {
      DEFAULT_CHARSET = Charsets.UTF_8;
      DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
      DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   }
}
