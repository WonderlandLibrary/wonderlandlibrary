package com.ibm.icu.lang;

/** @deprecated */
public class CharSequences {
   /** @deprecated */
   public static int matchAfter(CharSequence var0, CharSequence var1, int var2, int var3) {
      int var4 = var2;
      int var5 = var3;
      int var6 = var0.length();

      for(int var7 = var1.length(); var4 < var6 && var5 < var7; ++var5) {
         char var8 = var0.charAt(var4);
         char var9 = var1.charAt(var5);
         if (var8 != var9) {
            break;
         }

         ++var4;
      }

      int var10 = var4 - var2;
      if (var10 != 0 && var4 > 0 && var5 > 0) {
         --var10;
      }

      return var10;
   }

   /** @deprecated */
   public int codePointLength(CharSequence var1) {
      return Character.codePointCount(var1, 0, var1.length());
   }

   /** @deprecated */
   public static final boolean equals(int var0, CharSequence var1) {
      if (var1 == null) {
         return false;
      } else {
         switch(var1.length()) {
         case 1:
            return var0 == var1.charAt(0);
         case 2:
            return var0 > 65535 && var0 == Character.codePointAt(var1, 0);
         default:
            return false;
         }
      }
   }

   /** @deprecated */
   public static final boolean equals(CharSequence var0, int var1) {
      return equals(var1, var0);
   }

   /** @deprecated */
   public static int compare(CharSequence var0, int var1) {
      if (var1 >= 0 && var1 <= 1114111) {
         int var2 = var0.length();
         if (var2 == 0) {
            return -1;
         } else {
            char var3 = var0.charAt(0);
            int var4 = var1 - 65536;
            if (var4 < 0) {
               int var8 = var3 - var1;
               return var8 != 0 ? var8 : var2 - 1;
            } else {
               char var5 = (char)((var4 >>> 10) + '\ud800');
               int var6 = var3 - var5;
               if (var6 != 0) {
                  return var6;
               } else {
                  if (var2 > 1) {
                     char var7 = (char)((var4 & 1023) + '\udc00');
                     var6 = var0.charAt(1) - var7;
                     if (var6 != 0) {
                        return var6;
                     }
                  }

                  return var2 - 2;
               }
            }
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   /** @deprecated */
   public static int compare(int var0, CharSequence var1) {
      return -compare(var1, var0);
   }

   /** @deprecated */
   public static int getSingleCodePoint(CharSequence var0) {
      int var1 = var0.length();
      if (var1 >= 1 && var1 <= 2) {
         int var2 = Character.codePointAt(var0, 0);
         return var2 < 65536 == (var1 == 1) ? var2 : Integer.MAX_VALUE;
      } else {
         return Integer.MAX_VALUE;
      }
   }

   /** @deprecated */
   public static final boolean equals(Object var0, Object var1) {
      return var0 == null ? var1 == null : (var1 == null ? false : var0.equals(var1));
   }

   /** @deprecated */
   public static int compare(CharSequence var0, CharSequence var1) {
      int var2 = var0.length();
      int var3 = var1.length();
      int var4 = var2 <= var3 ? var2 : var3;

      for(int var5 = 0; var5 < var4; ++var5) {
         int var6 = var0.charAt(var5) - var1.charAt(var5);
         if (var6 != 0) {
            return var6;
         }
      }

      return var2 - var3;
   }

   /** @deprecated */
   public static boolean equalsChars(CharSequence var0, CharSequence var1) {
      return var0.length() == var1.length() && compare(var0, var1) == 0;
   }

   /** @deprecated */
   public static int indexOf(CharSequence var0, int var1) {
      int var2;
      for(int var3 = 0; var3 < var0.length(); var3 += Character.charCount(var2)) {
         var2 = Character.codePointAt(var0, var3);
         if (var2 == var1) {
            return var3;
         }
      }

      return -1;
   }

   /** @deprecated */
   public static int[] codePoints(CharSequence var0) {
      int[] var1 = new int[var0.length()];
      int var2 = 0;

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         char var4 = var0.charAt(var3);
         if (var4 >= '\udc00' && var4 <= '\udfff' && var3 != 0) {
            char var5 = (char)var1[var2 - 1];
            if (var5 >= '\ud800' && var5 <= '\udbff') {
               var1[var2 - 1] = Character.toCodePoint(var5, var4);
               continue;
            }
         }

         var1[var2++] = var4;
      }

      if (var2 == var1.length) {
         return var1;
      } else {
         int[] var6 = new int[var2];
         System.arraycopy(var1, 0, var6, 0, var2);
         return var6;
      }
   }

   private CharSequences() {
   }
}
