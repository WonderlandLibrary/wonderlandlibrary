package com.google.zxing.common;

public final class BitMatrix {
   private final int width;
   private final int height;
   private final int rowSize;
   private final int[] bits;

   public BitMatrix(int dimension) {
      this(dimension, dimension);
   }

   public BitMatrix(int width, int height) {
      if (width >= 1 && height >= 1) {
         this.width = width;
         this.height = height;
         this.rowSize = width + 31 >> 5;
         this.bits = new int[this.rowSize * height];
      } else {
         throw new IllegalArgumentException("Both dimensions must be greater than 0");
      }
   }

   public boolean get(int x, int y) {
      int offset = y * this.rowSize + (x >> 5);
      return (this.bits[offset] >>> (x & 31) & 1) != 0;
   }

   public void set(int x, int y) {
      int offset = y * this.rowSize + (x >> 5);
      int[] var10000 = this.bits;
      var10000[offset] |= 1 << (x & 31);
   }

   public void flip(int x, int y) {
      int offset = y * this.rowSize + (x >> 5);
      int[] var10000 = this.bits;
      var10000[offset] ^= 1 << (x & 31);
   }

   public void clear() {
      int max = this.bits.length;

      for(int i = 0; i < max; ++i) {
         this.bits[i] = 0;
      }

   }

   public void setRegion(int left, int top, int width, int height) {
      if (top >= 0 && left >= 0) {
         if (height >= 1 && width >= 1) {
            int right = left + width;
            int bottom = top + height;
            if (bottom <= this.height && right <= this.width) {
               for(int y = top; y < bottom; ++y) {
                  int offset = y * this.rowSize;

                  for(int x = left; x < right; ++x) {
                     int[] var10000 = this.bits;
                     var10000[offset + (x >> 5)] |= 1 << (x & 31);
                  }
               }

            } else {
               throw new IllegalArgumentException("The region must fit inside the matrix");
            }
         } else {
            throw new IllegalArgumentException("Height and width must be at least 1");
         }
      } else {
         throw new IllegalArgumentException("Left and top must be nonnegative");
      }
   }

   public BitArray getRow(int y, BitArray row) {
      if (row == null || row.getSize() < this.width) {
         row = new BitArray(this.width);
      }

      int offset = y * this.rowSize;

      for(int x = 0; x < this.rowSize; ++x) {
         row.setBulk(x << 5, this.bits[offset + x]);
      }

      return row;
   }

   public void setRow(int y, BitArray row) {
      System.arraycopy(row.getBitArray(), 0, this.bits, y * this.rowSize, this.rowSize);
   }

   public int[] getEnclosingRectangle() {
      int left = this.width;
      int top = this.height;
      int right = -1;
      int bottom = -1;

      int y;
      int x32;
      for(y = 0; y < this.height; ++y) {
         for(x32 = 0; x32 < this.rowSize; ++x32) {
            int theBits = this.bits[y * this.rowSize + x32];
            if (theBits != 0) {
               if (y < top) {
                  top = y;
               }

               if (y > bottom) {
                  bottom = y;
               }

               int bit;
               if (x32 * 32 < left) {
                  for(bit = 0; theBits << 31 - bit == 0; ++bit) {
                  }

                  if (x32 * 32 + bit < left) {
                     left = x32 * 32 + bit;
                  }
               }

               if (x32 * 32 + 31 > right) {
                  for(bit = 31; theBits >>> bit == 0; --bit) {
                  }

                  if (x32 * 32 + bit > right) {
                     right = x32 * 32 + bit;
                  }
               }
            }
         }
      }

      y = right - left;
      x32 = bottom - top;
      if (y >= 0 && x32 >= 0) {
         return new int[]{left, top, y, x32};
      } else {
         return null;
      }
   }

   public int[] getTopLeftOnBit() {
      int bitsOffset;
      for(bitsOffset = 0; bitsOffset < this.bits.length && this.bits[bitsOffset] == 0; ++bitsOffset) {
      }

      if (bitsOffset == this.bits.length) {
         return null;
      } else {
         int y = bitsOffset / this.rowSize;
         int x = bitsOffset % this.rowSize << 5;
         int theBits = this.bits[bitsOffset];

         int bit;
         for(bit = 0; theBits << 31 - bit == 0; ++bit) {
         }

         x += bit;
         return new int[]{x, y};
      }
   }

   public int[] getBottomRightOnBit() {
      int bitsOffset;
      for(bitsOffset = this.bits.length - 1; bitsOffset >= 0 && this.bits[bitsOffset] == 0; --bitsOffset) {
      }

      if (bitsOffset < 0) {
         return null;
      } else {
         int y = bitsOffset / this.rowSize;
         int x = bitsOffset % this.rowSize << 5;
         int theBits = this.bits[bitsOffset];

         int bit;
         for(bit = 31; theBits >>> bit == 0; --bit) {
         }

         x += bit;
         return new int[]{x, y};
      }
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public boolean equals(Object o) {
      if (!(o instanceof BitMatrix)) {
         return false;
      } else {
         BitMatrix other = (BitMatrix)o;
         if (this.width == other.width && this.height == other.height && this.rowSize == other.rowSize && this.bits.length == other.bits.length) {
            for(int i = 0; i < this.bits.length; ++i) {
               if (this.bits[i] != other.bits[i]) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public int hashCode() {
      int hash = this.width;
      hash = 31 * hash + this.width;
      hash = 31 * hash + this.height;
      hash = 31 * hash + this.rowSize;
      int[] arr$ = this.bits;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         int bit = arr$[i$];
         hash = 31 * hash + bit;
      }

      return hash;
   }

   public String toString() {
      StringBuilder result = new StringBuilder(this.height * (this.width + 1));

      for(int y = 0; y < this.height; ++y) {
         for(int x = 0; x < this.width; ++x) {
            result.append(this.get(x, y) ? "X " : "  ");
         }

         result.append('\n');
      }

      return result.toString();
   }
}
