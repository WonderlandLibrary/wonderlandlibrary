package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.ArrayList;
import java.util.MissingResourceException;

public class CalendarData {
   private ICUResourceBundle fBundle;
   private String fMainType;
   private String fFallbackType;

   public CalendarData(ULocale var1, String var2) {
      this((ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var1), var2);
   }

   public CalendarData(ICUResourceBundle var1, String var2) {
      this.fBundle = var1;
      if (var2 != null && !var2.equals("") && !var2.equals("gregorian")) {
         this.fMainType = var2;
         this.fFallbackType = "gregorian";
      } else {
         this.fMainType = "gregorian";
         this.fFallbackType = null;
      }

   }

   public ICUResourceBundle get(String var1) {
      try {
         return this.fBundle.getWithFallback("calendar/" + this.fMainType + "/" + var1);
      } catch (MissingResourceException var3) {
         if (this.fFallbackType != null) {
            return this.fBundle.getWithFallback("calendar/" + this.fFallbackType + "/" + var1);
         } else {
            throw var3;
         }
      }
   }

   public ICUResourceBundle get(String var1, String var2) {
      try {
         return this.fBundle.getWithFallback("calendar/" + this.fMainType + "/" + var1 + "/format/" + var2);
      } catch (MissingResourceException var4) {
         if (this.fFallbackType != null) {
            return this.fBundle.getWithFallback("calendar/" + this.fFallbackType + "/" + var1 + "/format/" + var2);
         } else {
            throw var4;
         }
      }
   }

   public ICUResourceBundle get(String var1, String var2, String var3) {
      try {
         return this.fBundle.getWithFallback("calendar/" + this.fMainType + "/" + var1 + "/" + var2 + "/" + var3);
      } catch (MissingResourceException var5) {
         if (this.fFallbackType != null) {
            return this.fBundle.getWithFallback("calendar/" + this.fFallbackType + "/" + var1 + "/" + var2 + "/" + var3);
         } else {
            throw var5;
         }
      }
   }

   public ICUResourceBundle get(String var1, String var2, String var3, String var4) {
      try {
         return this.fBundle.getWithFallback("calendar/" + this.fMainType + "/" + var1 + "/" + var2 + "/" + var3 + "/" + var4);
      } catch (MissingResourceException var6) {
         if (this.fFallbackType != null) {
            return this.fBundle.getWithFallback("calendar/" + this.fFallbackType + "/" + var1 + "/" + var2 + "/" + var3 + "/" + var4);
         } else {
            throw var6;
         }
      }
   }

   public String[] getStringArray(String var1) {
      return this.get(var1).getStringArray();
   }

   public String[] getStringArray(String var1, String var2) {
      return this.get(var1, var2).getStringArray();
   }

   public String[] getStringArray(String var1, String var2, String var3) {
      return this.get(var1, var2, var3).getStringArray();
   }

   public String[] getEras(String var1) {
      ICUResourceBundle var2 = this.get("eras/" + var1);
      return var2.getStringArray();
   }

   public String[] getDateTimePatterns() {
      ICUResourceBundle var1 = this.get("DateTimePatterns");
      ArrayList var2 = new ArrayList();
      UResourceBundleIterator var3 = var1.getIterator();

      while(var3.hasNext()) {
         UResourceBundle var4 = var3.next();
         int var5 = var4.getType();
         switch(var5) {
         case 0:
            var2.add(var4.getString());
            break;
         case 8:
            String[] var6 = var4.getStringArray();
            var2.add(var6[0]);
         }
      }

      return (String[])var2.toArray(new String[var2.size()]);
   }

   public String[] getOverrides() {
      ICUResourceBundle var1 = this.get("DateTimePatterns");
      ArrayList var2 = new ArrayList();
      UResourceBundleIterator var3 = var1.getIterator();

      while(var3.hasNext()) {
         UResourceBundle var4 = var3.next();
         int var5 = var4.getType();
         switch(var5) {
         case 0:
            var2.add((Object)null);
            break;
         case 8:
            String[] var6 = var4.getStringArray();
            var2.add(var6[1]);
         }
      }

      return (String[])var2.toArray(new String[var2.size()]);
   }

   public ULocale getULocale() {
      return this.fBundle.getULocale();
   }
}
