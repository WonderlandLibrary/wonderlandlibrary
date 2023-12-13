package com.ibm.icu.text;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;

public class NumberingSystem {
   private String desc = "0123456789";
   private int radix = 10;
   private boolean algorithmic = false;
   private String name = "latn";
   private static ICUCache cachedLocaleData = new SimpleCache();
   private static ICUCache cachedStringData = new SimpleCache();

   public static NumberingSystem getInstance(int var0, boolean var1, String var2) {
      return getInstance((String)null, var0, var1, var2);
   }

   private static NumberingSystem getInstance(String param0, int param1, boolean param2, String param3) {
      // $FF: Couldn't be decompiled
   }

   public static NumberingSystem getInstance(Locale var0) {
      return getInstance(ULocale.forLocale(var0));
   }

   public static NumberingSystem getInstance(ULocale var0) {
      String[] var1 = new String[]{"native", "traditional", "finance"};
      Boolean var3 = true;
      String var4 = var0.getKeywordValue("numbers");
      if (var4 != null) {
         String[] var5 = var1;
         int var6 = var1.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String var8 = var5[var7];
            if (var4.equals(var8)) {
               var3 = false;
               break;
            }
         }
      } else {
         var4 = "default";
         var3 = false;
      }

      NumberingSystem var2;
      if (var3) {
         var2 = getInstanceByName(var4);
         if (var2 != null) {
            return var2;
         }

         var4 = "default";
         var3 = false;
      }

      String var10 = var0.getBaseName();
      var2 = (NumberingSystem)cachedLocaleData.get(var10 + "@numbers=" + var4);
      if (var2 != null) {
         return var2;
      } else {
         String var11 = null;

         while(!var3) {
            try {
               ICUResourceBundle var12 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var0);
               var12 = var12.getWithFallback("NumberElements");
               var11 = var12.getStringWithFallback(var4);
               var3 = true;
            } catch (MissingResourceException var9) {
               if (!var4.equals("native") && !var4.equals("finance")) {
                  if (var4.equals("traditional")) {
                     var4 = "native";
                  } else {
                     var3 = true;
                  }
               } else {
                  var4 = "default";
               }
            }
         }

         if (var11 != null) {
            var2 = getInstanceByName(var11);
         }

         if (var2 == null) {
            var2 = new NumberingSystem();
         }

         cachedLocaleData.put(var10 + "@numbers=" + var4, var2);
         return var2;
      }
   }

   public static NumberingSystem getInstance() {
      return getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public static NumberingSystem getInstanceByName(String var0) {
      NumberingSystem var4 = (NumberingSystem)cachedStringData.get(var0);
      if (var4 != null) {
         return var4;
      } else {
         int var1;
         boolean var2;
         String var3;
         try {
            UResourceBundle var5 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "numberingSystems");
            UResourceBundle var6 = var5.get("numberingSystems");
            UResourceBundle var7 = var6.get(var0);
            var3 = var7.getString("desc");
            UResourceBundle var8 = var7.get("radix");
            UResourceBundle var9 = var7.get("algorithmic");
            var1 = var8.getInt();
            int var10 = var9.getInt();
            var2 = var10 == 1;
         } catch (MissingResourceException var11) {
            return null;
         }

         var4 = getInstance(var0, var1, var2, var3);
         cachedStringData.put(var0, var4);
         return var4;
      }
   }

   public static String[] getAvailableNames() {
      UResourceBundle var0 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "numberingSystems");
      UResourceBundle var1 = var0.get("numberingSystems");
      ArrayList var4 = new ArrayList();
      UResourceBundleIterator var5 = var1.getIterator();

      while(var5.hasNext()) {
         UResourceBundle var2 = var5.next();
         String var3 = var2.getKey();
         var4.add(var3);
      }

      return (String[])var4.toArray(new String[var4.size()]);
   }

   public int getRadix() {
      return this.radix;
   }

   public String getDescription() {
      return this.desc;
   }

   public String getName() {
      return this.name;
   }

   public boolean isAlgorithmic() {
      return this.algorithmic;
   }
}
