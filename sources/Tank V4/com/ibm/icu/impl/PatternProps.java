package com.ibm.icu.impl;

public final class PatternProps {
   private static final byte[] latin1 = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 0, 3, 0, 3, 3, 0, 3, 0, 3, 3, 0, 0, 0, 0, 3, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0};
   private static final byte[] index2000 = new byte[]{2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 9};
   private static final int[] syntax2000 = new int[]{0, -1, -65536, 2147418367, 2146435070, -65536, 4194303, -1048576, -242, 65537};
   private static final int[] syntaxOrWhiteSpace2000 = new int[]{0, -1, -16384, 2147419135, 2146435070, -65536, 4194303, -1048576, -242, 65537};

   public static boolean isSyntax(int var0) {
      if (var0 < 0) {
         return false;
      } else if (var0 <= 255) {
         return latin1[var0] == 3;
      } else if (var0 < 8208) {
         return false;
      } else if (var0 <= 12336) {
         int var1 = syntax2000[index2000[var0 - 8192 >> 5]];
         return (var1 >> (var0 & 31) & 1) != 0;
      } else if (64830 <= var0 && var0 <= 65094) {
         return var0 <= 64831 || 65093 <= var0;
      } else {
         return false;
      }
   }

   public static int skipWhiteSpace(CharSequence var0, int var1) {
      while(var1 < var0.length() && var0.charAt(var1) < 0) {
         ++var1;
      }

      return var1;
   }

   public static String trimWhiteSpace(String var0) {
      if (var0.length() != 0 && (var0.charAt(0) >= 0 || var0.charAt(var0.length() - 1) >= 0)) {
         int var1 = 0;

         int var2;
         for(var2 = var0.length(); var1 < var2 && var0.charAt(var1) < 0; ++var1) {
         }

         if (var1 < var2) {
            while(var0.charAt(var2 - 1) < 0) {
               --var2;
            }
         }

         return var0.substring(var1, var2);
      } else {
         return var0;
      }
   }

   public static boolean isIdentifier(CharSequence var0) {
      int var1 = var0.length();
      if (var1 == 0) {
         return false;
      } else {
         int var2 = 0;

         while(var0.charAt(var2++) >= 0) {
            if (var2 >= var1) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean isIdentifier(CharSequence var0, int var1, int var2) {
      if (var1 >= var2) {
         return false;
      } else {
         while(var0.charAt(var1++) >= 0) {
            if (var1 >= var2) {
               return true;
            }
         }

         return false;
      }
   }

   public static int skipIdentifier(CharSequence var0, int var1) {
      while(var1 < var0.length() && var0.charAt(var1) < 0) {
         ++var1;
      }

      return var1;
   }
}
