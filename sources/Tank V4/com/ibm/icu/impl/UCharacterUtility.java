package com.ibm.icu.impl;

public final class UCharacterUtility {
   private static final int NON_CHARACTER_SUFFIX_MIN_3_0_ = 65534;
   private static final int NON_CHARACTER_MIN_3_1_ = 64976;
   private static final int NON_CHARACTER_MAX_3_1_ = 65007;

   public static boolean isNonCharacter(int var0) {
      if ((var0 & '\ufffe') == 65534) {
         return true;
      } else {
         return var0 >= 64976 && var0 <= 65007;
      }
   }

   static int toInt(char var0, char var1) {
      return var0 << 16 | var1;
   }

   static int getNullTermByteSubString(StringBuffer var0, byte[] var1, int var2) {
      for(byte var3 = 1; var3 != 0; ++var2) {
         var3 = var1[var2];
         if (var3 != 0) {
            var0.append((char)(var3 & 255));
         }
      }

      return var2;
   }

   static int compareNullTermByteSubString(String var0, byte[] var1, int var2, int var3) {
      byte var4 = 1;
      int var5 = var0.length();

      while(true) {
         if (var4 != 0) {
            var4 = var1[var3];
            ++var3;
            if (var4 != 0) {
               if (var2 != var5 && var0.charAt(var2) == (char)(var4 & 255)) {
                  ++var2;
                  continue;
               }

               return -1;
            }
         }

         return var2;
      }
   }

   static int skipNullTermByteSubString(byte[] var0, int var1, int var2) {
      for(int var4 = 0; var4 < var2; ++var4) {
         for(byte var3 = 1; var3 != 0; ++var1) {
            var3 = var0[var1];
         }
      }

      return var1;
   }

   static int skipByteSubString(byte[] var0, int var1, int var2, byte var3) {
      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         byte var5 = var0[var1 + var4];
         if (var5 == var3) {
            ++var4;
            break;
         }
      }

      return var4;
   }

   private UCharacterUtility() {
   }
}
