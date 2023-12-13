package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.escape.UnicodeEscaper;

@Beta
@GwtCompatible
public final class PercentEscaper extends UnicodeEscaper {
   private static final char[] PLUS_SIGN = new char[]{'+'};
   private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
   private final boolean plusForSpace;
   private final boolean[] safeOctets;

   public PercentEscaper(String var1, boolean var2) {
      Preconditions.checkNotNull(var1);
      if (var1.matches(".*[0-9A-Za-z].*")) {
         throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
      } else {
         var1 = var1 + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
         if (var2 && var1.contains(" ")) {
            throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
         } else {
            this.plusForSpace = var2;
            this.safeOctets = createSafeOctets(var1);
         }
      }
   }

   private static boolean[] createSafeOctets(String var0) {
      int var1 = -1;
      char[] var2 = var0.toCharArray();
      char[] var3 = var2;
      int var4 = var2.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         char var6 = var3[var5];
         var1 = Math.max(var6, var1);
      }

      boolean[] var8 = new boolean[var1 + 1];
      char[] var9 = var2;
      var5 = var2.length;

      for(int var10 = 0; var10 < var5; ++var10) {
         char var7 = var9[var10];
         var8[var7] = true;
      }

      return var8;
   }

   protected int nextEscapeIndex(CharSequence var1, int var2, int var3) {
      Preconditions.checkNotNull(var1);

      while(var2 < var3) {
         char var4 = var1.charAt(var2);
         if (var4 >= this.safeOctets.length || !this.safeOctets[var4]) {
            break;
         }

         ++var2;
      }

      return var2;
   }

   public String escape(String var1) {
      Preconditions.checkNotNull(var1);
      int var2 = var1.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         char var4 = var1.charAt(var3);
         if (var4 >= this.safeOctets.length || !this.safeOctets[var4]) {
            return this.escapeSlow(var1, var3);
         }
      }

      return var1;
   }

   protected char[] escape(int var1) {
      if (var1 < this.safeOctets.length && this.safeOctets[var1]) {
         return null;
      } else if (var1 == 32 && this.plusForSpace) {
         return PLUS_SIGN;
      } else {
         char[] var2;
         if (var1 <= 127) {
            var2 = new char[]{'%', UPPER_HEX_DIGITS[var1 >>> 4], UPPER_HEX_DIGITS[var1 & 15]};
            return var2;
         } else if (var1 <= 2047) {
            var2 = new char[]{'%', '\u0000', '\u0000', '%', '\u0000', UPPER_HEX_DIGITS[var1 & 15]};
            var1 >>>= 4;
            var2[4] = UPPER_HEX_DIGITS[8 | var1 & 3];
            var1 >>>= 2;
            var2[2] = UPPER_HEX_DIGITS[var1 & 15];
            var1 >>>= 4;
            var2[1] = UPPER_HEX_DIGITS[12 | var1];
            return var2;
         } else if (var1 <= 65535) {
            var2 = new char[9];
            var2[0] = '%';
            var2[1] = 'E';
            var2[3] = '%';
            var2[6] = '%';
            var2[8] = UPPER_HEX_DIGITS[var1 & 15];
            var1 >>>= 4;
            var2[7] = UPPER_HEX_DIGITS[8 | var1 & 3];
            var1 >>>= 2;
            var2[5] = UPPER_HEX_DIGITS[var1 & 15];
            var1 >>>= 4;
            var2[4] = UPPER_HEX_DIGITS[8 | var1 & 3];
            var1 >>>= 2;
            var2[2] = UPPER_HEX_DIGITS[var1];
            return var2;
         } else if (var1 <= 1114111) {
            var2 = new char[12];
            var2[0] = '%';
            var2[1] = 'F';
            var2[3] = '%';
            var2[6] = '%';
            var2[9] = '%';
            var2[11] = UPPER_HEX_DIGITS[var1 & 15];
            var1 >>>= 4;
            var2[10] = UPPER_HEX_DIGITS[8 | var1 & 3];
            var1 >>>= 2;
            var2[8] = UPPER_HEX_DIGITS[var1 & 15];
            var1 >>>= 4;
            var2[7] = UPPER_HEX_DIGITS[8 | var1 & 3];
            var1 >>>= 2;
            var2[5] = UPPER_HEX_DIGITS[var1 & 15];
            var1 >>>= 4;
            var2[4] = UPPER_HEX_DIGITS[8 | var1 & 3];
            var1 >>>= 2;
            var2[2] = UPPER_HEX_DIGITS[var1 & 7];
            return var2;
         } else {
            throw new IllegalArgumentException("Invalid unicode character value " + var1);
         }
      }
   }
}
