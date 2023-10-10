package com.ibm.icu.text;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public final class ListFormatter {
   private final String two;
   private final String start;
   private final String middle;
   private final String end;
   static Map localeToData = new HashMap();
   static ListFormatter.Cache cache = new ListFormatter.Cache();

   public ListFormatter(String var1, String var2, String var3, String var4) {
      this.two = var1;
      this.start = var2;
      this.middle = var3;
      this.end = var4;
   }

   public static ListFormatter getInstance(ULocale var0) {
      return cache.get(var0);
   }

   public static ListFormatter getInstance(Locale var0) {
      return getInstance(ULocale.forLocale(var0));
   }

   public static ListFormatter getInstance() {
      return getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public String format(Object... var1) {
      return this.format((Collection)Arrays.asList(var1));
   }

   public String format(Collection var1) {
      Iterator var2 = var1.iterator();
      int var3 = var1.size();
      switch(var3) {
      case 0:
         return "";
      case 1:
         return var2.next().toString();
      case 2:
         return this.format2(this.two, var2.next(), var2.next());
      default:
         String var4 = var2.next().toString();
         var4 = this.format2(this.start, var4, var2.next());

         for(var3 -= 3; var3 > 0; --var3) {
            var4 = this.format2(this.middle, var4, var2.next());
         }

         return this.format2(this.end, var4, var2.next());
      }
   }

   private String format2(String var1, Object var2, Object var3) {
      int var4 = var1.indexOf("{0}");
      int var5 = var1.indexOf("{1}");
      if (var4 >= 0 && var5 >= 0) {
         return var4 < var5 ? var1.substring(0, var4) + var2 + var1.substring(var4 + 3, var5) + var3 + var1.substring(var5 + 3) : var1.substring(0, var5) + var3 + var1.substring(var5 + 3, var4) + var2 + var1.substring(var4 + 3);
      } else {
         throw new IllegalArgumentException("Missing {0} or {1} in pattern " + var1);
      }
   }

   static void add(String var0, String... var1) {
      localeToData.put(new ULocale(var0), new ListFormatter(var1[0], var1[1], var1[2], var1[3]));
   }

   private static class Cache {
      private final ICUCache cache;

      private Cache() {
         this.cache = new SimpleCache();
      }

      public ListFormatter get(ULocale var1) {
         ListFormatter var2 = (ListFormatter)this.cache.get(var1);
         if (var2 == null) {
            var2 = load(var1);
            this.cache.put(var1, var2);
         }

         return var2;
      }

      private static ListFormatter load(ULocale var0) {
         ICUResourceBundle var1 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var0);
         var1 = var1.getWithFallback("listPattern/standard");
         return new ListFormatter(var1.getWithFallback("2").getString(), var1.getWithFallback("start").getString(), var1.getWithFallback("middle").getString(), var1.getWithFallback("end").getString());
      }

      Cache(Object var1) {
         this();
      }
   }
}
