package com.google.zxing.common;

public final class BitSource {
   private final byte[] bytes;
   private int byteOffset;
   private int bitOffset;

   public BitSource(byte[] bytes) {
      this.bytes = bytes;
   }

   public int getBitOffset() {
      return this.bitOffset;
   }

   public int getByteOffset() {
      return this.byteOffset;
   }

   public int readBits(int numBits) {
      if (numBits >= 1 && numBits <= 32 && numBits <= this.available()) {
         int result = 0;
         int bitsToNotRead;
         int mask;
         if (this.bitOffset > 0) {
            bitsToNotRead = 8 - this.bitOffset;
            mask = numBits < bitsToNotRead ? numBits : bitsToNotRead;
            int bitsToNotRead = bitsToNotRead - mask;
            int mask = 255 >> 8 - mask << bitsToNotRead;
            result = (this.bytes[this.byteOffset] & mask) >> bitsToNotRead;
            numBits -= mask;
            this.bitOffset += mask;
            if (this.bitOffset == 8) {
               this.bitOffset = 0;
               ++this.byteOffset;
            }
         }

         if (numBits > 0) {
            while(numBits >= 8) {
               result = result << 8 | this.bytes[this.byteOffset] & 255;
               ++this.byteOffset;
               numBits -= 8;
            }

            if (numBits > 0) {
               bitsToNotRead = 8 - numBits;
               mask = 255 >> bitsToNotRead << bitsToNotRead;
               result = result << numBits | (this.bytes[this.byteOffset] & mask) >> bitsToNotRead;
               this.bitOffset += numBits;
            }
         }

         return result;
      } else {
         throw new IllegalArgumentException(String.valueOf(numBits));
      }
   }

   public int available() {
      return 8 * (this.bytes.length - this.byteOffset) - this.bitOffset;
   }
}
