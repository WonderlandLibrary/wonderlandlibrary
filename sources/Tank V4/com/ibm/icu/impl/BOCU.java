package com.ibm.icu.impl;

import com.ibm.icu.text.UCharacterIterator;

public class BOCU {
   private static final int SLOPE_MIN_ = 3;
   private static final int SLOPE_MAX_ = 255;
   private static final int SLOPE_MIDDLE_ = 129;
   private static final int SLOPE_TAIL_COUNT_ = 253;
   private static final int SLOPE_SINGLE_ = 80;
   private static final int SLOPE_LEAD_2_ = 42;
   private static final int SLOPE_LEAD_3_ = 3;
   private static final int SLOPE_REACH_POS_1_ = 80;
   private static final int SLOPE_REACH_NEG_1_ = -80;
   private static final int SLOPE_REACH_POS_2_ = 10667;
   private static final int SLOPE_REACH_NEG_2_ = -10668;
   private static final int SLOPE_REACH_POS_3_ = 192785;
   private static final int SLOPE_REACH_NEG_3_ = -192786;
   private static final int SLOPE_START_POS_2_ = 210;
   private static final int SLOPE_START_POS_3_ = 252;
   private static final int SLOPE_START_NEG_2_ = 49;
   private static final int SLOPE_START_NEG_3_ = 7;

   public static int compress(String var0, byte[] var1, int var2) {
      int var3 = 0;
      UCharacterIterator var4 = UCharacterIterator.getInstance(var0);

      for(int var5 = var4.nextCodePoint(); var5 != -1; var5 = var4.nextCodePoint()) {
         if (var3 >= 19968 && var3 < 40960) {
            var3 = 30292;
         } else {
            var3 = (var3 & -128) - -80;
         }

         var2 = writeDiff(var5 - var3, var1, var2);
         var3 = var5;
      }

      return var2;
   }

   public static int getCompressionLength(String var0) {
      int var1 = 0;
      int var2 = 0;
      UCharacterIterator var3 = UCharacterIterator.getInstance(var0);

      for(int var4 = var3.nextCodePoint(); var4 != -1; var1 = var4) {
         if (var1 >= 19968 && var1 < 40960) {
            var1 = 30292;
         } else {
            var1 = (var1 & -128) - -80;
         }

         var4 = var3.nextCodePoint();
         var2 += lengthOfDiff(var4 - var1);
      }

      return var2;
   }

   private BOCU() {
   }

   private static final long getNegDivMod(int var0, int var1) {
      int var2 = var0 % var1;
      long var3 = (long)(var0 / var1);
      if (var2 < 0) {
         --var3;
         var2 += var1;
      }

      return var3 << 32 | (long)var2;
   }

   private static final int writeDiff(int var0, byte[] var1, int var2) {
      if (var0 >= -80) {
         if (var0 <= 80) {
            var1[var2++] = (byte)(129 + var0);
         } else if (var0 <= 10667) {
            var1[var2++] = (byte)(210 + var0 / 253);
            var1[var2++] = (byte)(3 + var0 % 253);
         } else if (var0 <= 192785) {
            var1[var2 + 2] = (byte)(3 + var0 % 253);
            var0 /= 253;
            var1[var2 + 1] = (byte)(3 + var0 % 253);
            var1[var2] = (byte)(252 + var0 / 253);
            var2 += 3;
         } else {
            var1[var2 + 3] = (byte)(3 + var0 % 253);
            var0 /= 253;
            var1[var2] = (byte)(3 + var0 % 253);
            var0 /= 253;
            var1[var2 + 1] = (byte)(3 + var0 % 253);
            var1[var2] = -1;
            var2 += 4;
         }
      } else {
         long var3 = getNegDivMod(var0, 253);
         int var5 = (int)var3;
         if (var0 >= -10668) {
            var0 = (int)(var3 >> 32);
            var1[var2++] = (byte)(49 + var0);
            var1[var2++] = (byte)(3 + var5);
         } else if (var0 >= -192786) {
            var1[var2 + 2] = (byte)(3 + var5);
            var0 = (int)(var3 >> 32);
            var3 = getNegDivMod(var0, 253);
            var5 = (int)var3;
            var0 = (int)(var3 >> 32);
            var1[var2 + 1] = (byte)(3 + var5);
            var1[var2] = (byte)(7 + var0);
            var2 += 3;
         } else {
            var1[var2 + 3] = (byte)(3 + var5);
            var0 = (int)(var3 >> 32);
            var3 = getNegDivMod(var0, 253);
            var5 = (int)var3;
            var0 = (int)(var3 >> 32);
            var1[var2 + 2] = (byte)(3 + var5);
            var3 = getNegDivMod(var0, 253);
            var5 = (int)var3;
            var1[var2 + 1] = (byte)(3 + var5);
            var1[var2] = 3;
            var2 += 4;
         }
      }

      return var2;
   }

   private static final int lengthOfDiff(int var0) {
      if (var0 >= -80) {
         if (var0 <= 80) {
            return 1;
         } else if (var0 <= 10667) {
            return 2;
         } else {
            return var0 <= 192785 ? 3 : 4;
         }
      } else if (var0 >= -10668) {
         return 2;
      } else {
         return var0 >= -192786 ? 3 : 4;
      }
   }
}
