package org.apache.commons.codec.binary;

import java.math.BigInteger;

public class Base64 extends BaseNCodec {
   private static final int BITS_PER_ENCODED_BYTE = 6;
   private static final int BYTES_PER_UNENCODED_BLOCK = 3;
   private static final int BYTES_PER_ENCODED_BLOCK = 4;
   static final byte[] CHUNK_SEPARATOR = new byte[]{13, 10};
   private static final byte[] STANDARD_ENCODE_TABLE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
   private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
   private static final byte[] DECODE_TABLE = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
   private static final int MASK_6BITS = 63;
   private final byte[] encodeTable;
   private final byte[] decodeTable;
   private final byte[] lineSeparator;
   private final int decodeSize;
   private final int encodeSize;

   public Base64() {
      this(0);
   }

   public Base64(boolean var1) {
      this(76, CHUNK_SEPARATOR, var1);
   }

   public Base64(int var1) {
      this(var1, CHUNK_SEPARATOR);
   }

   public Base64(int var1, byte[] var2) {
      this(var1, var2, false);
   }

   public Base64(int var1, byte[] var2, boolean var3) {
      super(3, 4, var1, var2 == null ? 0 : var2.length);
      this.decodeTable = DECODE_TABLE;
      if (var2 != null) {
         if (this.containsAlphabetOrPad(var2)) {
            String var4 = StringUtils.newStringUtf8(var2);
            throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + var4 + "]");
         }

         if (var1 > 0) {
            this.encodeSize = 4 + var2.length;
            this.lineSeparator = new byte[var2.length];
            System.arraycopy(var2, 0, this.lineSeparator, 0, var2.length);
         } else {
            this.encodeSize = 4;
            this.lineSeparator = null;
         }
      } else {
         this.encodeSize = 4;
         this.lineSeparator = null;
      }

      this.decodeSize = this.encodeSize - 1;
      this.encodeTable = var3 ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
   }

   public boolean isUrlSafe() {
      return this.encodeTable == URL_SAFE_ENCODE_TABLE;
   }

   void encode(byte[] var1, int var2, int var3, BaseNCodec.Context var4) {
      if (!var4.eof) {
         if (var3 < 0) {
            var4.eof = true;
            if (0 == var4.modulus && this.lineLength == 0) {
               return;
            }

            byte[] var5 = this.ensureBufferSize(this.encodeSize, var4);
            int var6 = var4.pos;
            switch(var4.modulus) {
            case 0:
               break;
            case 1:
               var5[var4.pos++] = this.encodeTable[var4.ibitWorkArea >> 2 & 63];
               var5[var4.pos++] = this.encodeTable[var4.ibitWorkArea << 4 & 63];
               if (this.encodeTable == STANDARD_ENCODE_TABLE) {
                  var5[var4.pos++] = 61;
                  var5[var4.pos++] = 61;
               }
               break;
            case 2:
               var5[var4.pos++] = this.encodeTable[var4.ibitWorkArea >> 10 & 63];
               var5[var4.pos++] = this.encodeTable[var4.ibitWorkArea >> 4 & 63];
               var5[var4.pos++] = this.encodeTable[var4.ibitWorkArea << 2 & 63];
               if (this.encodeTable == STANDARD_ENCODE_TABLE) {
                  var5[var4.pos++] = 61;
               }
               break;
            default:
               throw new IllegalStateException("Impossible modulus " + var4.modulus);
            }

            var4.currentLinePos += var4.pos - var6;
            if (this.lineLength > 0 && var4.currentLinePos > 0) {
               System.arraycopy(this.lineSeparator, 0, var5, var4.pos, this.lineSeparator.length);
               var4.pos += this.lineSeparator.length;
            }
         } else {
            for(int var8 = 0; var8 < var3; ++var8) {
               byte[] var9 = this.ensureBufferSize(this.encodeSize, var4);
               var4.modulus = (var4.modulus + 1) % 3;
               int var7 = var1[var2++];
               if (var7 < 0) {
                  var7 += 256;
               }

               var4.ibitWorkArea = (var4.ibitWorkArea << 8) + var7;
               if (0 == var4.modulus) {
                  var9[var4.pos++] = this.encodeTable[var4.ibitWorkArea >> 18 & 63];
                  var9[var4.pos++] = this.encodeTable[var4.ibitWorkArea >> 12 & 63];
                  var9[var4.pos++] = this.encodeTable[var4.ibitWorkArea >> 6 & 63];
                  var9[var4.pos++] = this.encodeTable[var4.ibitWorkArea & 63];
                  var4.currentLinePos += 4;
                  if (this.lineLength > 0 && this.lineLength <= var4.currentLinePos) {
                     System.arraycopy(this.lineSeparator, 0, var9, var4.pos, this.lineSeparator.length);
                     var4.pos += this.lineSeparator.length;
                     var4.currentLinePos = 0;
                  }
               }
            }
         }

      }
   }

   void decode(byte[] var1, int var2, int var3, BaseNCodec.Context var4) {
      if (!var4.eof) {
         if (var3 < 0) {
            var4.eof = true;
         }

         for(int var5 = 0; var5 < var3; ++var5) {
            byte[] var6 = this.ensureBufferSize(this.decodeSize, var4);
            byte var7 = var1[var2++];
            if (var7 == 61) {
               var4.eof = true;
               break;
            }

            if (var7 >= 0 && var7 < DECODE_TABLE.length) {
               byte var8 = DECODE_TABLE[var7];
               if (var8 >= 0) {
                  var4.modulus = (var4.modulus + 1) % 4;
                  var4.ibitWorkArea = (var4.ibitWorkArea << 6) + var8;
                  if (var4.modulus == 0) {
                     var6[var4.pos++] = (byte)(var4.ibitWorkArea >> 16 & 255);
                     var6[var4.pos++] = (byte)(var4.ibitWorkArea >> 8 & 255);
                     var6[var4.pos++] = (byte)(var4.ibitWorkArea & 255);
                  }
               }
            }
         }

         if (var4.eof && var4.modulus != 0) {
            byte[] var9 = this.ensureBufferSize(this.decodeSize, var4);
            switch(var4.modulus) {
            case 1:
               break;
            case 2:
               var4.ibitWorkArea >>= 4;
               var9[var4.pos++] = (byte)(var4.ibitWorkArea & 255);
               break;
            case 3:
               var4.ibitWorkArea >>= 2;
               var9[var4.pos++] = (byte)(var4.ibitWorkArea >> 8 & 255);
               var9[var4.pos++] = (byte)(var4.ibitWorkArea & 255);
               break;
            default:
               throw new IllegalStateException("Impossible modulus " + var4.modulus);
            }
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean isArrayByteBase64(byte[] var0) {
      return isBase64(var0);
   }

   public static boolean isBase64(String var0) {
      return isBase64(StringUtils.getBytesUtf8(var0));
   }

   public static boolean isBase64(byte[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static byte[] encodeBase64(byte[] var0) {
      return encodeBase64(var0, false);
   }

   public static String encodeBase64String(byte[] var0) {
      return StringUtils.newStringUtf8(encodeBase64(var0, false));
   }

   public static byte[] encodeBase64URLSafe(byte[] var0) {
      return encodeBase64(var0, false, true);
   }

   public static String encodeBase64URLSafeString(byte[] var0) {
      return StringUtils.newStringUtf8(encodeBase64(var0, false, true));
   }

   public static byte[] encodeBase64Chunked(byte[] var0) {
      return encodeBase64(var0, true);
   }

   public static byte[] encodeBase64(byte[] var0, boolean var1) {
      return encodeBase64(var0, var1, false);
   }

   public static byte[] encodeBase64(byte[] var0, boolean var1, boolean var2) {
      return encodeBase64(var0, var1, var2, Integer.MAX_VALUE);
   }

   public static byte[] encodeBase64(byte[] var0, boolean var1, boolean var2, int var3) {
      if (var0 != null && var0.length != 0) {
         Base64 var4 = var1 ? new Base64(var2) : new Base64(0, CHUNK_SEPARATOR, var2);
         long var5 = var4.getEncodedLength(var0);
         if (var5 > (long)var3) {
            throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + var5 + ") than the specified maximum size of " + var3);
         } else {
            return var4.encode(var0);
         }
      } else {
         return var0;
      }
   }

   public static byte[] decodeBase64(String var0) {
      return (new Base64()).decode(var0);
   }

   public static byte[] decodeBase64(byte[] var0) {
      return (new Base64()).decode(var0);
   }

   public static BigInteger decodeInteger(byte[] var0) {
      return new BigInteger(1, decodeBase64(var0));
   }

   public static byte[] encodeInteger(BigInteger var0) {
      if (var0 == null) {
         throw new NullPointerException("encodeInteger called with null parameter");
      } else {
         return encodeBase64(toIntegerBytes(var0), false);
      }
   }

   static byte[] toIntegerBytes(BigInteger var0) {
      int var1 = var0.bitLength();
      var1 = var1 + 7 >> 3 << 3;
      byte[] var2 = var0.toByteArray();
      if (var0.bitLength() % 8 != 0 && var0.bitLength() / 8 + 1 == var1 / 8) {
         return var2;
      } else {
         byte var3 = 0;
         int var4 = var2.length;
         if (var0.bitLength() % 8 == 0) {
            var3 = 1;
            --var4;
         }

         int var5 = var1 / 8 - var4;
         byte[] var6 = new byte[var1 / 8];
         System.arraycopy(var2, var3, var6, var5, var4);
         return var6;
      }
   }

   protected boolean isInAlphabet(byte var1) {
      return var1 >= 0 && var1 < this.decodeTable.length && this.decodeTable[var1] != -1;
   }
}
