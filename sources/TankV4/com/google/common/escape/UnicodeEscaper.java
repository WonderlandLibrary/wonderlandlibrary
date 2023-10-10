package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

@Beta
@GwtCompatible
public abstract class UnicodeEscaper extends Escaper {
   private static final int DEST_PAD = 32;

   protected UnicodeEscaper() {
   }

   protected abstract char[] escape(int var1);

   protected int nextEscapeIndex(CharSequence var1, int var2, int var3) {
      int var4;
      int var5;
      for(var4 = var2; var4 < var3; var4 += Character.isSupplementaryCodePoint(var5) ? 2 : 1) {
         var5 = codePointAt(var1, var4, var3);
         if (var5 < 0 || this.escape(var5) != null) {
            break;
         }
      }

      return var4;
   }

   public String escape(String var1) {
      Preconditions.checkNotNull(var1);
      int var2 = var1.length();
      int var3 = this.nextEscapeIndex(var1, 0, var2);
      return var3 == var2 ? var1 : this.escapeSlow(var1, var3);
   }

   protected final String escapeSlow(String var1, int var2) {
      int var3 = var1.length();
      char[] var4 = Platform.charBufferFromThreadLocal();
      int var5 = 0;

      int var6;
      int var7;
      int var9;
      for(var6 = 0; var2 < var3; var2 = this.nextEscapeIndex(var1, var9, var3)) {
         var7 = codePointAt(var1, var2, var3);
         if (var7 < 0) {
            throw new IllegalArgumentException("Trailing high surrogate at end of input");
         }

         char[] var8 = this.escape(var7);
         var9 = var2 + (Character.isSupplementaryCodePoint(var7) ? 2 : 1);
         if (var8 != null) {
            int var10 = var2 - var6;
            int var11 = var5 + var10 + var8.length;
            if (var4.length < var11) {
               int var12 = var11 + (var3 - var2) + 32;
               var4 = growBuffer(var4, var5, var12);
            }

            if (var10 > 0) {
               var1.getChars(var6, var2, var4, var5);
               var5 += var10;
            }

            if (var8.length > 0) {
               System.arraycopy(var8, 0, var4, var5, var8.length);
               var5 += var8.length;
            }

            var6 = var9;
         }
      }

      var7 = var3 - var6;
      if (var7 > 0) {
         int var13 = var5 + var7;
         if (var4.length < var13) {
            var4 = growBuffer(var4, var5, var13);
         }

         var1.getChars(var6, var3, var4, var5);
         var5 = var13;
      }

      return new String(var4, 0, var5);
   }

   protected static int codePointAt(CharSequence var0, int var1, int var2) {
      Preconditions.checkNotNull(var0);
      if (var1 < var2) {
         char var3 = var0.charAt(var1++);
         if (var3 >= '\ud800' && var3 <= '\udfff') {
            if (var3 <= '\udbff') {
               if (var1 == var2) {
                  return -var3;
               } else {
                  char var4 = var0.charAt(var1);
                  if (Character.isLowSurrogate(var4)) {
                     return Character.toCodePoint(var3, var4);
                  } else {
                     throw new IllegalArgumentException("Expected low surrogate but got char '" + var4 + "' with value " + var4 + " at index " + var1 + " in '" + var0 + "'");
                  }
               }
            } else {
               throw new IllegalArgumentException("Unexpected low surrogate character '" + var3 + "' with value " + var3 + " at index " + (var1 - 1) + " in '" + var0 + "'");
            }
         } else {
            return var3;
         }
      } else {
         throw new IndexOutOfBoundsException("Index exceeds specified range");
      }
   }

   private static char[] growBuffer(char[] var0, int var1, int var2) {
      char[] var3 = new char[var2];
      if (var1 > 0) {
         System.arraycopy(var0, 0, var3, 0, var1);
      }

      return var3;
   }
}
