package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

@Beta
@GwtCompatible
public abstract class CharEscaper extends Escaper {
   private static final int DEST_PAD_MULTIPLIER = 2;

   protected CharEscaper() {
   }

   public String escape(String var1) {
      Preconditions.checkNotNull(var1);
      int var2 = var1.length();

      for(int var3 = 0; var3 < var2; ++var3) {
         if (this.escape(var1.charAt(var3)) != null) {
            return this.escapeSlow(var1, var3);
         }
      }

      return var1;
   }

   protected final String escapeSlow(String var1, int var2) {
      int var3 = var1.length();
      char[] var4 = Platform.charBufferFromThreadLocal();
      int var5 = var4.length;
      int var6 = 0;

      int var7;
      int var9;
      for(var7 = 0; var2 < var3; ++var2) {
         char[] var8 = this.escape(var1.charAt(var2));
         if (var8 != null) {
            var9 = var8.length;
            int var10 = var2 - var7;
            int var11 = var6 + var10 + var9;
            if (var5 < var11) {
               var5 = var11 + 2 * (var3 - var2);
               var4 = growBuffer(var4, var6, var5);
            }

            if (var10 > 0) {
               var1.getChars(var7, var2, var4, var6);
               var6 += var10;
            }

            if (var9 > 0) {
               System.arraycopy(var8, 0, var4, var6, var9);
               var6 += var9;
            }

            var7 = var2 + 1;
         }
      }

      int var12 = var3 - var7;
      if (var12 > 0) {
         var9 = var6 + var12;
         if (var5 < var9) {
            var4 = growBuffer(var4, var6, var9);
         }

         var1.getChars(var7, var3, var4, var6);
         var6 = var9;
      }

      return new String(var4, 0, var6);
   }

   protected abstract char[] escape(char var1);

   private static char[] growBuffer(char[] var0, int var1, int var2) {
      char[] var3 = new char[var2];
      if (var1 > 0) {
         System.arraycopy(var0, 0, var3, 0, var1);
      }

      return var3;
   }
}
