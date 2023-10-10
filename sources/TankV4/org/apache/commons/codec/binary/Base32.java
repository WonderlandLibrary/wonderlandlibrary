package org.apache.commons.codec.binary;

public class Base32 extends BaseNCodec {
   private static final int BITS_PER_ENCODED_BYTE = 5;
   private static final int BYTES_PER_ENCODED_BLOCK = 8;
   private static final int BYTES_PER_UNENCODED_BLOCK = 5;
   private static final byte[] CHUNK_SEPARATOR = new byte[]{13, 10};
   private static final byte[] DECODE_TABLE = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
   private static final byte[] ENCODE_TABLE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 50, 51, 52, 53, 54, 55};
   private static final byte[] HEX_DECODE_TABLE = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32};
   private static final byte[] HEX_ENCODE_TABLE = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86};
   private static final int MASK_5BITS = 31;
   private final int decodeSize;
   private final byte[] decodeTable;
   private final int encodeSize;
   private final byte[] encodeTable;
   private final byte[] lineSeparator;

   public Base32() {
      this(false);
   }

   public Base32(boolean var1) {
      this(0, (byte[])null, var1);
   }

   public Base32(int var1) {
      this(var1, CHUNK_SEPARATOR);
   }

   public Base32(int var1, byte[] var2) {
      this(var1, var2, false);
   }

   public Base32(int var1, byte[] var2, boolean var3) {
      super(5, 8, var1, var2 == null ? 0 : var2.length);
      if (var3) {
         this.encodeTable = HEX_ENCODE_TABLE;
         this.decodeTable = HEX_DECODE_TABLE;
      } else {
         this.encodeTable = ENCODE_TABLE;
         this.decodeTable = DECODE_TABLE;
      }

      if (var1 > 0) {
         if (var2 == null) {
            throw new IllegalArgumentException("lineLength " + var1 + " > 0, but lineSeparator is null");
         }

         if (this.containsAlphabetOrPad(var2)) {
            String var4 = StringUtils.newStringUtf8(var2);
            throw new IllegalArgumentException("lineSeparator must not contain Base32 characters: [" + var4 + "]");
         }

         this.encodeSize = 8 + var2.length;
         this.lineSeparator = new byte[var2.length];
         System.arraycopy(var2, 0, this.lineSeparator, 0, var2.length);
      } else {
         this.encodeSize = 8;
         this.lineSeparator = null;
      }

      this.decodeSize = this.encodeSize - 1;
   }

   void decode(byte[] var1, int var2, int var3, BaseNCodec.Context var4) {
      if (!var4.eof) {
         if (var3 < 0) {
            var4.eof = true;
         }

         for(int var5 = 0; var5 < var3; ++var5) {
            byte var6 = var1[var2++];
            if (var6 == 61) {
               var4.eof = true;
               break;
            }

            byte[] var7 = this.ensureBufferSize(this.decodeSize, var4);
            if (var6 >= 0 && var6 < this.decodeTable.length) {
               byte var8 = this.decodeTable[var6];
               if (var8 >= 0) {
                  var4.modulus = (var4.modulus + 1) % 8;
                  var4.lbitWorkArea = (var4.lbitWorkArea << 5) + (long)var8;
                  if (var4.modulus == 0) {
                     var7[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 32 & 255L));
                     var7[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 24 & 255L));
                     var7[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 16 & 255L));
                     var7[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 8 & 255L));
                     var7[var4.pos++] = (byte)((int)(var4.lbitWorkArea & 255L));
                  }
               }
            }
         }

         if (var4.eof && var4.modulus >= 2) {
            byte[] var9 = this.ensureBufferSize(this.decodeSize, var4);
            switch(var4.modulus) {
            case 2:
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 2 & 255L));
               break;
            case 3:
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 7 & 255L));
               break;
            case 4:
               var4.lbitWorkArea >>= 4;
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 8 & 255L));
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea & 255L));
               break;
            case 5:
               var4.lbitWorkArea >>= 1;
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 16 & 255L));
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 8 & 255L));
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea & 255L));
               break;
            case 6:
               var4.lbitWorkArea >>= 6;
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 16 & 255L));
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 8 & 255L));
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea & 255L));
               break;
            case 7:
               var4.lbitWorkArea >>= 3;
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 24 & 255L));
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 16 & 255L));
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea >> 8 & 255L));
               var9[var4.pos++] = (byte)((int)(var4.lbitWorkArea & 255L));
               break;
            default:
               throw new IllegalStateException("Impossible modulus " + var4.modulus);
            }
         }

      }
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
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 3) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea << 2) & 31];
               var5[var4.pos++] = 61;
               var5[var4.pos++] = 61;
               var5[var4.pos++] = 61;
               var5[var4.pos++] = 61;
               var5[var4.pos++] = 61;
               var5[var4.pos++] = 61;
               break;
            case 2:
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 11) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 6) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 1) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea << 4) & 31];
               var5[var4.pos++] = 61;
               var5[var4.pos++] = 61;
               var5[var4.pos++] = 61;
               var5[var4.pos++] = 61;
               break;
            case 3:
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 19) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 14) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 9) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 4) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea << 1) & 31];
               var5[var4.pos++] = 61;
               var5[var4.pos++] = 61;
               var5[var4.pos++] = 61;
               break;
            case 4:
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 27) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 22) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 17) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 12) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 7) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 2) & 31];
               var5[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea << 3) & 31];
               var5[var4.pos++] = 61;
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
               var4.modulus = (var4.modulus + 1) % 5;
               int var7 = var1[var2++];
               if (var7 < 0) {
                  var7 += 256;
               }

               var4.lbitWorkArea = (var4.lbitWorkArea << 8) + (long)var7;
               if (0 == var4.modulus) {
                  var9[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 35) & 31];
                  var9[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 30) & 31];
                  var9[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 25) & 31];
                  var9[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 20) & 31];
                  var9[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 15) & 31];
                  var9[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 10) & 31];
                  var9[var4.pos++] = this.encodeTable[(int)(var4.lbitWorkArea >> 5) & 31];
                  var9[var4.pos++] = this.encodeTable[(int)var4.lbitWorkArea & 31];
                  var4.currentLinePos += 8;
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

   public boolean isInAlphabet(byte var1) {
      return var1 >= 0 && var1 < this.decodeTable.length && this.decodeTable[var1] != -1;
   }
}
