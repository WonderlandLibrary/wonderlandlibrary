package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import java.util.BitSet;

@GwtIncompatible("no precomputation is done in GWT")
final class SmallCharMatcher extends CharMatcher.FastMatcher {
   static final int MAX_SIZE = 1023;
   private final char[] table;
   private final boolean containsZero;
   private final long filter;
   private static final int C1 = -862048943;
   private static final int C2 = 461845907;
   private static final double DESIRED_LOAD_FACTOR = 0.5D;

   private SmallCharMatcher(char[] var1, long var2, boolean var4, String var5) {
      super(var5);
      this.table = var1;
      this.filter = var2;
      this.containsZero = var4;
   }

   static int smear(int var0) {
      return 461845907 * Integer.rotateLeft(var0 * -862048943, 15);
   }

   @VisibleForTesting
   static int chooseTableSize(int var0) {
      if (var0 == 1) {
         return 2;
      } else {
         int var1;
         for(var1 = Integer.highestOneBit(var0 - 1) << 1; (double)var1 * 0.5D < (double)var0; var1 <<= 1) {
         }

         return var1;
      }
   }

   static CharMatcher from(BitSet var0, String var1) {
      long var2 = 0L;
      int var4 = var0.cardinality();
      boolean var5 = var0.get(0);
      char[] var6 = new char[chooseTableSize(var4)];
      int var7 = var6.length - 1;

      for(int var8 = var0.nextSetBit(0); var8 != -1; var8 = var0.nextSetBit(var8 + 1)) {
         var2 |= 1L << var8;

         int var9;
         for(var9 = smear(var8) & var7; var6[var9] != 0; var9 = var9 + 1 & var7) {
         }

         var6[var9] = (char)var8;
      }

      return new SmallCharMatcher(var6, var2, var5, var1);
   }

   public boolean matches(char var1) {
      if (var1 == 0) {
         return this.containsZero;
      } else if (var1 == 0) {
         return false;
      } else {
         int var2 = this.table.length - 1;
         int var3 = smear(var1) & var2;
         int var4 = var3;

         while(this.table[var4] != 0) {
            if (this.table[var4] == var1) {
               return true;
            }

            var4 = var4 + 1 & var2;
            if (var4 == var3) {
               return false;
            }
         }

         return false;
      }
   }

   void setBits(BitSet var1) {
      if (this.containsZero) {
         var1.set(0);
      }

      char[] var2 = this.table;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char var5 = var2[var4];
         if (var5 != 0) {
            var1.set(var5);
         }
      }

   }
}
