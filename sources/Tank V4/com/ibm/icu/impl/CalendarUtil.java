package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.MissingResourceException;

public class CalendarUtil {
   private static ICUCache CALTYPE_CACHE = new SimpleCache();
   private static final String CALKEY = "calendar";
   private static final String DEFCAL = "gregorian";

   public static String getCalendarType(ULocale var0) {
      String var1 = null;
      var1 = var0.getKeywordValue("calendar");
      if (var1 != null) {
         return var1;
      } else {
         String var2 = var0.getBaseName();
         var1 = (String)CALTYPE_CACHE.get(var2);
         if (var1 != null) {
            return var1;
         } else {
            ULocale var3 = ULocale.createCanonical(var0.toString());
            var1 = var3.getKeywordValue("calendar");
            if (var1 == null) {
               String var4 = var3.getCountry();
               if (var4.length() == 0) {
                  ULocale var5 = ULocale.addLikelySubtags(var3);
                  var4 = var5.getCountry();
               }

               try {
                  UResourceBundle var11 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
                  UResourceBundle var6 = var11.get("calendarPreferenceData");
                  UResourceBundle var7 = null;

                  try {
                     var7 = var6.get(var4);
                  } catch (MissingResourceException var9) {
                     var7 = var6.get("001");
                  }

                  var1 = var7.getString(0);
               } catch (MissingResourceException var10) {
               }

               if (var1 == null) {
                  var1 = "gregorian";
               }
            }

            CALTYPE_CACHE.put(var2, var1);
            return var1;
         }
      }
   }
}
