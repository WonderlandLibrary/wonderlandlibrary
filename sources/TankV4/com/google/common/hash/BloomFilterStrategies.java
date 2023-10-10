package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.math.RoundingMode;
import java.util.Arrays;

enum BloomFilterStrategies implements BloomFilter.Strategy {
   MURMUR128_MITZ_32 {
      public boolean put(Object var1, Funnel var2, int var3, BloomFilterStrategies.BitArray var4) {
         long var5 = var4.bitSize();
         long var7 = Hashing.murmur3_128().hashObject(var1, var2).asLong();
         int var9 = (int)var7;
         int var10 = (int)(var7 >>> 32);
         boolean var11 = false;

         for(int var12 = 1; var12 <= var3; ++var12) {
            int var13 = var9 + var12 * var10;
            if (var13 < 0) {
               var13 = ~var13;
            }

            var11 |= var4.set((long)var13 % var5);
         }

         return var11;
      }

      public boolean mightContain(Object var1, Funnel var2, int var3, BloomFilterStrategies.BitArray var4) {
         long var5 = var4.bitSize();
         long var7 = Hashing.murmur3_128().hashObject(var1, var2).asLong();
         int var9 = (int)var7;
         int var10 = (int)(var7 >>> 32);

         for(int var11 = 1; var11 <= var3; ++var11) {
            int var12 = var9 + var11 * var10;
            if (var12 < 0) {
               var12 = ~var12;
            }

            if (!var4.get((long)var12 % var5)) {
               return false;
            }
         }

         return true;
      }
   },
   MURMUR128_MITZ_64 {
      public boolean put(Object var1, Funnel var2, int var3, BloomFilterStrategies.BitArray var4) {
         long var5 = var4.bitSize();
         byte[] var7 = Hashing.murmur3_128().hashObject(var1, var2).getBytesInternal();
         long var8 = this.lowerEight(var7);
         long var10 = this.upperEight(var7);
         boolean var12 = false;
         long var13 = var8;

         for(int var15 = 0; var15 < var3; ++var15) {
            var12 |= var4.set((var13 & Long.MAX_VALUE) % var5);
            var13 += var10;
         }

         return var12;
      }

      public boolean mightContain(Object var1, Funnel var2, int var3, BloomFilterStrategies.BitArray var4) {
         long var5 = var4.bitSize();
         byte[] var7 = Hashing.murmur3_128().hashObject(var1, var2).getBytesInternal();
         long var8 = this.lowerEight(var7);
         long var10 = this.upperEight(var7);
         long var12 = var8;

         for(int var14 = 0; var14 < var3; ++var14) {
            if (!var4.get((var12 & Long.MAX_VALUE) % var5)) {
               return false;
            }

            var12 += var10;
         }

         return true;
      }

      private long lowerEight(byte[] var1) {
         return Longs.fromBytes(var1[7], var1[6], var1[5], var1[4], var1[3], var1[2], var1[1], var1[0]);
      }

      private long upperEight(byte[] var1) {
         return Longs.fromBytes(var1[15], var1[14], var1[13], var1[12], var1[11], var1[10], var1[9], var1[8]);
      }
   };

   private static final BloomFilterStrategies[] $VALUES = new BloomFilterStrategies[]{MURMUR128_MITZ_32, MURMUR128_MITZ_64};

   private BloomFilterStrategies() {
   }

   BloomFilterStrategies(Object var3) {
      this();
   }

   static final class BitArray {
      final long[] data;
      long bitCount;

      BitArray(long var1) {
         this(new long[Ints.checkedCast(LongMath.divide(var1, 64L, RoundingMode.CEILING))]);
      }

      BitArray(long[] var1) {
         Preconditions.checkArgument(var1.length > 0, "data length is zero!");
         this.data = var1;
         long var2 = 0L;
         long[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            long var7 = var4[var6];
            var2 += (long)Long.bitCount(var7);
         }

         this.bitCount = var2;
      }

      boolean set(long var1) {
         if (var1 != false) {
            long[] var10001 = this.data;
            var10001[(int)(var1 >>> 6)] |= 1L << (int)var1;
            ++this.bitCount;
            return true;
         } else {
            return false;
         }
      }

      long bitSize() {
         return (long)this.data.length * 64L;
      }

      long bitCount() {
         return this.bitCount;
      }

      BloomFilterStrategies.BitArray copy() {
         return new BloomFilterStrategies.BitArray((long[])this.data.clone());
      }

      void putAll(BloomFilterStrategies.BitArray var1) {
         Preconditions.checkArgument(this.data.length == var1.data.length, "BitArrays must be of equal length (%s != %s)", this.data.length, var1.data.length);
         this.bitCount = 0L;

         for(int var2 = 0; var2 < this.data.length; ++var2) {
            long[] var10000 = this.data;
            var10000[var2] |= var1.data[var2];
            this.bitCount += (long)Long.bitCount(this.data[var2]);
         }

      }

      public boolean equals(Object var1) {
         if (var1 instanceof BloomFilterStrategies.BitArray) {
            BloomFilterStrategies.BitArray var2 = (BloomFilterStrategies.BitArray)var1;
            return Arrays.equals(this.data, var2.data);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Arrays.hashCode(this.data);
      }
   }
}
