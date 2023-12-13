package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

public class ICUResourceTableAccess {
   public static String getTableString(String var0, ULocale var1, String var2, String var3) {
      ICUResourceBundle var4 = (ICUResourceBundle)UResourceBundle.getBundleInstance(var0, var1.getBaseName());
      return getTableString((ICUResourceBundle)var4, (String)var2, (String)null, var3);
   }

   public static String getTableString(ICUResourceBundle var0, String var1, String var2, String var3) {
      String var4 = null;

      try {
         while(true) {
            ICUResourceBundle var5;
            if ("currency".equals(var2)) {
               var5 = var0.getWithFallback("Currencies");
               var5 = var5.getWithFallback(var3);
               return var5.getString(1);
            }

            var5 = lookup(var0, var1);
            if (var5 == null) {
               return var3;
            }

            ICUResourceBundle var6 = var5;
            if (var2 != null) {
               var6 = lookup(var5, var2);
            }

            ICUResourceBundle var7;
            if (var6 != null) {
               var7 = lookup(var6, var3);
               if (var7 != null) {
                  var4 = var7.getString();
                  break;
               }
            }

            if (var2 == null) {
               String var10 = null;
               if (var1.equals("Countries")) {
                  var10 = LocaleIDs.getCurrentCountryID(var3);
               } else if (var1.equals("Languages")) {
                  var10 = LocaleIDs.getCurrentLanguageID(var3);
               }

               ICUResourceBundle var8 = lookup(var5, var10);
               if (var8 != null) {
                  var4 = var8.getString();
                  break;
               }
            }

            var7 = lookup(var5, "Fallback");
            if (var7 == null) {
               return var3;
            }

            String var11 = var7.getString();
            if (var11.length() == 0) {
               var11 = "root";
            }

            if (var11.equals(var5.getULocale().getName())) {
               return var3;
            }

            var0 = (ICUResourceBundle)UResourceBundle.getBundleInstance(var0.getBaseName(), var11);
         }
      } catch (Exception var9) {
      }

      return var4 != null && var4.length() > 0 ? var4 : var3;
   }

   private static ICUResourceBundle lookup(ICUResourceBundle var0, String var1) {
      return ICUResourceBundle.findResourceWithFallback(var1, var0, (UResourceBundle)null);
   }
}
