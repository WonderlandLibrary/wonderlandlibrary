package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public final class Utf8 {
   public static int encodedLength(CharSequence var0) {
      int var1 = var0.length();
      int var2 = var1;

      int var3;
      for(var3 = 0; var3 < var1 && var0.charAt(var3) < 128; ++var3) {
      }

      while(var3 < var1) {
         char var4 = var0.charAt(var3);
         if (var4 >= 2048) {
            var2 += encodedLengthGeneral(var0, var3);
            break;
         }

         var2 += 127 - var4 >>> 31;
         ++var3;
      }

      if (var2 < var1) {
         throw new IllegalArgumentException("UTF-8 length does not fit in int: " + ((long)var2 + 4294967296L));
      } else {
         return var2;
      }
   }

   private static int encodedLengthGeneral(CharSequence var0, int var1) {
      int var2 = var0.length();
      int var3 = 0;

      for(int var4 = var1; var4 < var2; ++var4) {
         char var5 = var0.charAt(var4);
         if (var5 < 2048) {
            var3 += 127 - var5 >>> 31;
         } else {
            var3 += 2;
            if ('\ud800' <= var5 && var5 <= '\udfff') {
               int var6 = Character.codePointAt(var0, var4);
               if (var6 < 65536) {
                  throw new IllegalArgumentException("Unpaired surrogate at index " + var4);
               }

               ++var4;
            }
         }
      }

      return var3;
   }

   public static boolean isWellFormed(byte[] var0) {
      return isWellFormed(var0, 0, var0.length);
   }

   public static boolean isWellFormed(byte[] var0, int var1, int var2) {
      int var3 = var1 + var2;
      Preconditions.checkPositionIndexes(var1, var3, var0.length);

      for(int var4 = var1; var4 < var3; ++var4) {
         if (var0[var4] < 0) {
            return isWellFormedSlowPath(var0, var4, var3);
         }
      }

      return true;
   }

   private static boolean isWellFormedSlowPath(byte[] var0, int var1, int var2) {
      int var3 = var1;

      while(true) {
         byte var4;
         do {
            if (var3 >= var2) {
               return true;
            }
         } while((var4 = var0[var3++]) >= 0);

         if (var4 < -32) {
            if (var3 == var2) {
               return false;
            }

            if (var4 < -62 || var0[var3++] > -65) {
               return false;
            }
         } else {
            byte var5;
            if (var4 < -16) {
               if (var3 + 1 >= var2) {
                  return false;
               }

               var5 = var0[var3++];
               if (var5 > -65 || var4 == -32 && var5 < -96 || var4 == -19 && -96 <= var5 || var0[var3++] > -65) {
                  return false;
               }
            } else {
               if (var3 + 2 >= var2) {
                  return false;
               }

               var5 = var0[var3++];
               if (var5 > -65 || (var4 << 28) + (var5 - -112) >> 30 != 0 || var0[var3++] > -65 || var0[var3++] > -65) {
                  return false;
               }
            }
         }
      }
   }

   private Utf8() {
   }
}
