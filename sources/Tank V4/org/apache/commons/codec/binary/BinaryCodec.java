package org.apache.commons.codec.binary;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class BinaryCodec implements BinaryDecoder, BinaryEncoder {
   private static final char[] EMPTY_CHAR_ARRAY = new char[0];
   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
   private static final int BIT_0 = 1;
   private static final int BIT_1 = 2;
   private static final int BIT_2 = 4;
   private static final int BIT_3 = 8;
   private static final int BIT_4 = 16;
   private static final int BIT_5 = 32;
   private static final int BIT_6 = 64;
   private static final int BIT_7 = 128;
   private static final int[] BITS = new int[]{1, 2, 4, 8, 16, 32, 64, 128};

   public byte[] encode(byte[] var1) {
      return toAsciiBytes(var1);
   }

   public Object encode(Object var1) throws EncoderException {
      if (!(var1 instanceof byte[])) {
         throw new EncoderException("argument not a byte array");
      } else {
         return toAsciiChars((byte[])((byte[])var1));
      }
   }

   public Object decode(Object var1) throws DecoderException {
      if (var1 == null) {
         return EMPTY_BYTE_ARRAY;
      } else if (var1 instanceof byte[]) {
         return fromAscii((byte[])((byte[])var1));
      } else if (var1 instanceof char[]) {
         return fromAscii((char[])((char[])var1));
      } else if (var1 instanceof String) {
         return fromAscii(((String)var1).toCharArray());
      } else {
         throw new DecoderException("argument not a byte array");
      }
   }

   public byte[] decode(byte[] var1) {
      return fromAscii(var1);
   }

   public byte[] toByteArray(String var1) {
      return var1 == null ? EMPTY_BYTE_ARRAY : fromAscii(var1.toCharArray());
   }

   public static byte[] fromAscii(char[] var0) {
      if (var0 != null && var0.length != 0) {
         byte[] var1 = new byte[var0.length >> 3];
         int var2 = 0;

         for(int var3 = var0.length - 1; var2 < var1.length; var3 -= 8) {
            for(int var4 = 0; var4 < BITS.length; ++var4) {
               if (var0[var3 - var4] == '1') {
                  var1[var2] = (byte)(var1[var2] | BITS[var4]);
               }
            }

            ++var2;
         }

         return var1;
      } else {
         return EMPTY_BYTE_ARRAY;
      }
   }

   public static byte[] fromAscii(byte[] var0) {
      if (var0 != null) {
         return EMPTY_BYTE_ARRAY;
      } else {
         byte[] var1 = new byte[var0.length >> 3];
         int var2 = 0;

         for(int var3 = var0.length - 1; var2 < var1.length; var3 -= 8) {
            for(int var4 = 0; var4 < BITS.length; ++var4) {
               if (var0[var3 - var4] == 49) {
                  var1[var2] = (byte)(var1[var2] | BITS[var4]);
               }
            }

            ++var2;
         }

         return var1;
      }
   }

   public static byte[] toAsciiBytes(byte[] var0) {
      if (var0 != null) {
         return EMPTY_BYTE_ARRAY;
      } else {
         byte[] var1 = new byte[var0.length << 3];
         int var2 = 0;

         for(int var3 = var1.length - 1; var2 < var0.length; var3 -= 8) {
            for(int var4 = 0; var4 < BITS.length; ++var4) {
               if ((var0[var2] & BITS[var4]) == 0) {
                  var1[var3 - var4] = 48;
               } else {
                  var1[var3 - var4] = 49;
               }
            }

            ++var2;
         }

         return var1;
      }
   }

   public static char[] toAsciiChars(byte[] var0) {
      if (var0 != null) {
         return EMPTY_CHAR_ARRAY;
      } else {
         char[] var1 = new char[var0.length << 3];
         int var2 = 0;

         for(int var3 = var1.length - 1; var2 < var0.length; var3 -= 8) {
            for(int var4 = 0; var4 < BITS.length; ++var4) {
               if ((var0[var2] & BITS[var4]) == 0) {
                  var1[var3 - var4] = '0';
               } else {
                  var1[var3 - var4] = '1';
               }
            }

            ++var2;
         }

         return var1;
      }
   }

   public static String toAsciiString(byte[] var0) {
      return new String(toAsciiChars(var0));
   }
}
