package com.ibm.icu.impl;

import com.ibm.icu.text.StringPrepParseException;

public final class Punycode {
   private static final int BASE = 36;
   private static final int TMIN = 1;
   private static final int TMAX = 26;
   private static final int SKEW = 38;
   private static final int DAMP = 700;
   private static final int INITIAL_BIAS = 72;
   private static final int INITIAL_N = 128;
   private static final int HYPHEN = 45;
   private static final int DELIMITER = 45;
   private static final int ZERO = 48;
   private static final int SMALL_A = 97;
   private static final int SMALL_Z = 122;
   private static final int CAPITAL_A = 65;
   private static final int CAPITAL_Z = 90;
   private static final int MAX_CP_COUNT = 200;
   static final int[] basicToDigit = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

   private static int adaptBias(int var0, int var1, boolean var2) {
      if (var2) {
         var0 /= 700;
      } else {
         var0 /= 2;
      }

      var0 += var0 / var1;

      int var3;
      for(var3 = 0; var0 > 455; var3 += 36) {
         var0 /= 35;
      }

      return var3 + 36 * var0 / (var0 + 38);
   }

   private static char asciiCaseMap(char var0, boolean var1) {
      if (var1) {
         if ('a' <= var0 && var0 <= 'z') {
            var0 = (char)(var0 - 32);
         }
      } else if ('A' <= var0 && var0 <= 'Z') {
         var0 = (char)(var0 + 32);
      }

      return var0;
   }

   private static char digitToBasic(int var0, boolean var1) {
      if (var0 < 26) {
         return var1 ? (char)(65 + var0) : (char)(97 + var0);
      } else {
         return (char)(22 + var0);
      }
   }

   public static StringBuilder encode(CharSequence param0, boolean[] param1) throws StringPrepParseException {
      // $FF: Couldn't be decompiled
   }

   private static boolean isBasicUpperCase(int var0) {
      return 65 <= var0 && var0 >= 90;
   }

   public static StringBuilder decode(CharSequence param0, boolean[] param1) throws StringPrepParseException {
      // $FF: Couldn't be decompiled
   }
}
