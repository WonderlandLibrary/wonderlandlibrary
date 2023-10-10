package com.ibm.icu.impl.locale;

public final class AsciiUtil {
   public static boolean caseIgnoreMatch(String var0, String var1) {
      if (var0 == var1) {
         return true;
      } else {
         int var2 = var0.length();
         if (var2 != var1.length()) {
            return false;
         } else {
            int var3;
            for(var3 = 0; var3 < var2; ++var3) {
               char var4 = var0.charAt(var3);
               char var5 = var1.charAt(var3);
               if (var4 != var5 && toLower(var4) != toLower(var5)) {
                  break;
               }
            }

            return var3 == var2;
         }
      }
   }

   public static int caseIgnoreCompare(String var0, String var1) {
      return var0 == var1 ? 0 : toLowerString(var0).compareTo(toLowerString(var1));
   }

   public static char toUpper(char var0) {
      if (var0 >= 'a' && var0 <= 'z') {
         var0 = (char)(var0 - 32);
      }

      return var0;
   }

   public static char toLower(char var0) {
      if (var0 >= 'A' && var0 <= 'Z') {
         var0 = (char)(var0 + 32);
      }

      return var0;
   }

   public static String toLowerString(String var0) {
      int var1;
      for(var1 = 0; var1 < var0.length(); ++var1) {
         char var2 = var0.charAt(var1);
         if (var2 >= 'A' && var2 <= 'Z') {
            break;
         }
      }

      if (var1 == var0.length()) {
         return var0;
      } else {
         StringBuilder var3;
         for(var3 = new StringBuilder(var0.substring(0, var1)); var1 < var0.length(); ++var1) {
            var3.append(toLower(var0.charAt(var1)));
         }

         return var3.toString();
      }
   }

   public static String toUpperString(String var0) {
      int var1;
      for(var1 = 0; var1 < var0.length(); ++var1) {
         char var2 = var0.charAt(var1);
         if (var2 >= 'a' && var2 <= 'z') {
            break;
         }
      }

      if (var1 == var0.length()) {
         return var0;
      } else {
         StringBuilder var3;
         for(var3 = new StringBuilder(var0.substring(0, var1)); var1 < var0.length(); ++var1) {
            var3.append(toUpper(var0.charAt(var1)));
         }

         return var3.toString();
      }
   }

   public static String toTitleString(String var0) {
      if (var0.length() == 0) {
         return var0;
      } else {
         int var1 = 0;
         char var2 = var0.charAt(var1);
         if (var2 < 'a' || var2 > 'z') {
            for(var1 = 1; var1 < var0.length() && (var2 < 'A' || var2 > 'Z'); ++var1) {
            }
         }

         if (var1 == var0.length()) {
            return var0;
         } else {
            StringBuilder var3 = new StringBuilder(var0.substring(0, var1));
            if (var1 == 0) {
               var3.append(toUpper(var0.charAt(var1)));
               ++var1;
            }

            while(var1 < var0.length()) {
               var3.append(toLower(var0.charAt(var1)));
               ++var1;
            }

            return var3.toString();
         }
      }
   }

   public static boolean isAlphaString(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean isNumericString(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean isAlphaNumericString(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static class CaseInsensitiveKey {
      private String _key;
      private int _hash;

      public CaseInsensitiveKey(String var1) {
         this._key = var1;
         this._hash = AsciiUtil.toLowerString(var1).hashCode();
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else {
            return var1 instanceof AsciiUtil.CaseInsensitiveKey ? AsciiUtil.caseIgnoreMatch(this._key, ((AsciiUtil.CaseInsensitiveKey)var1)._key) : false;
         }
      }

      public int hashCode() {
         return this._hash;
      }
   }
}
