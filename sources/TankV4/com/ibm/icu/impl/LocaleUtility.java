package com.ibm.icu.impl;

import java.util.Locale;

public class LocaleUtility {
   public static Locale getLocaleFromName(String var0) {
      String var1 = "";
      String var2 = "";
      String var3 = "";
      int var4 = var0.indexOf(95);
      if (var4 < 0) {
         var1 = var0;
      } else {
         var1 = var0.substring(0, var4);
         ++var4;
         int var5 = var0.indexOf(95, var4);
         if (var5 < 0) {
            var2 = var0.substring(var4);
         } else {
            var2 = var0.substring(var4, var5);
            var3 = var0.substring(var5 + 1);
         }
      }

      return new Locale(var1, var2, var3);
   }

   public static boolean isFallbackOf(String var0, String var1) {
      if (!var1.startsWith(var0)) {
         return false;
      } else {
         int var2 = var0.length();
         return var2 == var1.length() || var1.charAt(var2) == '_';
      }
   }

   public static boolean isFallbackOf(Locale var0, Locale var1) {
      return isFallbackOf(var0.toString(), var1.toString());
   }

   public static Locale fallback(Locale var0) {
      String[] var1 = new String[]{var0.getLanguage(), var0.getCountry(), var0.getVariant()};

      int var2;
      for(var2 = 2; var2 >= 0; --var2) {
         if (var1[var2].length() != 0) {
            var1[var2] = "";
            break;
         }
      }

      return var2 < 0 ? null : new Locale(var1[0], var1[1], var1[2]);
   }
}
