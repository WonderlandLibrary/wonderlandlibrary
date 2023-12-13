package com.ibm.icu.impl;

public class CalendarCache {
   private static final int[] primes = new int[]{61, 127, 509, 1021, 2039, 4093, 8191, 16381, 32749, 65521, 131071, 262139};
   private int pIndex = 0;
   private int size = 0;
   private int arraySize;
   private int threshold;
   private long[] keys;
   private long[] values;
   public static long EMPTY = Long.MIN_VALUE;

   public CalendarCache() {
      this.arraySize = primes[this.pIndex];
      this.threshold = this.arraySize * 3 / 4;
      this.keys = new long[this.arraySize];
      this.values = new long[this.arraySize];
      this.makeArrays(this.arraySize);
   }

   private void makeArrays(int var1) {
      this.keys = new long[var1];
      this.values = new long[var1];

      for(int var2 = 0; var2 < var1; ++var2) {
         this.values[var2] = EMPTY;
      }

      this.arraySize = var1;
      this.threshold = (int)((double)this.arraySize * 0.75D);
      this.size = 0;
   }

   public synchronized long get(long var1) {
      return this.values[this.findIndex(var1)];
   }

   public synchronized void put(long var1, long var3) {
      if (this.size >= this.threshold) {
         this.rehash();
      }

      int var5 = this.findIndex(var1);
      this.keys[var5] = var1;
      this.values[var5] = var3;
      ++this.size;
   }

   private final int findIndex(long var1) {
      int var3 = this.hash(var1);

      for(int var4 = 0; this.values[var3] != EMPTY && this.keys[var3] != var1; var3 = (var3 + var4) % this.arraySize) {
         if (var4 == 0) {
            var4 = this.hash2(var1);
         }
      }

      return var3;
   }

   private void rehash() {
      int var1 = this.arraySize;
      long[] var2 = this.keys;
      long[] var3 = this.values;
      if (this.pIndex < primes.length - 1) {
         this.arraySize = primes[++this.pIndex];
      } else {
         this.arraySize = this.arraySize * 2 + 1;
      }

      this.size = 0;
      this.makeArrays(this.arraySize);

      for(int var4 = 0; var4 < var1; ++var4) {
         if (var3[var4] != EMPTY) {
            this.put(var2[var4], var3[var4]);
         }
      }

   }

   private final int hash(long var1) {
      int var3 = (int)((var1 * 15821L + 1L) % (long)this.arraySize);
      if (var3 < 0) {
         var3 += this.arraySize;
      }

      return var3;
   }

   private final int hash2(long var1) {
      return this.arraySize - 2 - (int)(var1 % (long)(this.arraySize - 2));
   }
}
