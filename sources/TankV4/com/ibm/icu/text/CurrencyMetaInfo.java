package com.ibm.icu.text;

import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CurrencyMetaInfo {
   private static final CurrencyMetaInfo impl;
   private static final boolean hasData;
   /** @deprecated */
   protected static final CurrencyMetaInfo.CurrencyDigits defaultDigits = new CurrencyMetaInfo.CurrencyDigits(2, 0);

   public static CurrencyMetaInfo getInstance() {
      return impl;
   }

   public static CurrencyMetaInfo getInstance(boolean var0) {
      return hasData ? impl : null;
   }

   /** @deprecated */
   public static boolean hasData() {
      return hasData;
   }

   /** @deprecated */
   protected CurrencyMetaInfo() {
   }

   public List currencyInfo(CurrencyMetaInfo.CurrencyFilter var1) {
      return Collections.emptyList();
   }

   public List currencies(CurrencyMetaInfo.CurrencyFilter var1) {
      return Collections.emptyList();
   }

   public List regions(CurrencyMetaInfo.CurrencyFilter var1) {
      return Collections.emptyList();
   }

   public CurrencyMetaInfo.CurrencyDigits currencyDigits(String var1) {
      return defaultDigits;
   }

   private static String dateString(long var0) {
      if (var0 != Long.MAX_VALUE && var0 != Long.MIN_VALUE) {
         GregorianCalendar var2 = new GregorianCalendar();
         var2.setTimeZone(TimeZone.getTimeZone("GMT"));
         var2.setTimeInMillis(var0);
         return "" + var2.get(1) + '-' + (var2.get(2) + 1) + '-' + var2.get(5);
      } else {
         return null;
      }
   }

   private static String debugString(Object var0) {
      StringBuilder var1 = new StringBuilder();

      try {
         Field[] var2 = var0.getClass().getFields();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field var5 = var2[var4];
            Object var6 = var5.get(var0);
            if (var6 != null) {
               String var7;
               if (var6 instanceof Date) {
                  var7 = dateString(((Date)var6).getTime());
               } else if (var6 instanceof Long) {
                  var7 = dateString((Long)var6);
               } else {
                  var7 = String.valueOf(var6);
               }

               if (var7 != null) {
                  if (var1.length() > 0) {
                     var1.append(",");
                  }

                  var1.append(var5.getName()).append("='").append(var7).append("'");
               }
            }
         }
      } catch (Throwable var8) {
      }

      var1.insert(0, var0.getClass().getSimpleName() + "(");
      var1.append(")");
      return var1.toString();
   }

   static String access$000(Object var0) {
      return debugString(var0);
   }

   static {
      CurrencyMetaInfo var0 = null;
      boolean var1 = false;

      try {
         Class var2 = Class.forName("com.ibm.icu.impl.ICUCurrencyMetaInfo");
         var0 = (CurrencyMetaInfo)var2.newInstance();
         var1 = true;
      } catch (Throwable var3) {
         var0 = new CurrencyMetaInfo();
      }

      impl = var0;
      hasData = var1;
   }

   public static final class CurrencyInfo {
      public final String region;
      public final String code;
      public final long from;
      public final long to;
      public final int priority;
      private final boolean tender;

      /** @deprecated */
      public CurrencyInfo(String var1, String var2, long var3, long var5, int var7) {
         this(var1, var2, var3, var5, var7, true);
      }

      /** @deprecated */
      public CurrencyInfo(String var1, String var2, long var3, long var5, int var7, boolean var8) {
         this.region = var1;
         this.code = var2;
         this.from = var3;
         this.to = var5;
         this.priority = var7;
         this.tender = var8;
      }

      public String toString() {
         return CurrencyMetaInfo.access$000(this);
      }

      public boolean isTender() {
         return this.tender;
      }
   }

   public static final class CurrencyDigits {
      public final int fractionDigits;
      public final int roundingIncrement;

      public CurrencyDigits(int var1, int var2) {
         this.fractionDigits = var1;
         this.roundingIncrement = var2;
      }

      public String toString() {
         return CurrencyMetaInfo.access$000(this);
      }
   }

   public static final class CurrencyFilter {
      public final String region;
      public final String currency;
      public final long from;
      public final long to;
      /** @deprecated */
      public final boolean tenderOnly;
      private static final CurrencyMetaInfo.CurrencyFilter ALL = new CurrencyMetaInfo.CurrencyFilter((String)null, (String)null, Long.MIN_VALUE, Long.MAX_VALUE, false);

      private CurrencyFilter(String var1, String var2, long var3, long var5, boolean var7) {
         this.region = var1;
         this.currency = var2;
         this.from = var3;
         this.to = var5;
         this.tenderOnly = var7;
      }

      public static CurrencyMetaInfo.CurrencyFilter all() {
         return ALL;
      }

      public static CurrencyMetaInfo.CurrencyFilter now() {
         return ALL.withDate(new Date());
      }

      public static CurrencyMetaInfo.CurrencyFilter onRegion(String var0) {
         return ALL.withRegion(var0);
      }

      public static CurrencyMetaInfo.CurrencyFilter onCurrency(String var0) {
         return ALL.withCurrency(var0);
      }

      public static CurrencyMetaInfo.CurrencyFilter onDate(Date var0) {
         return ALL.withDate(var0);
      }

      public static CurrencyMetaInfo.CurrencyFilter onDateRange(Date var0, Date var1) {
         return ALL.withDateRange(var0, var1);
      }

      public static CurrencyMetaInfo.CurrencyFilter onDate(long var0) {
         return ALL.withDate(var0);
      }

      public static CurrencyMetaInfo.CurrencyFilter onDateRange(long var0, long var2) {
         return ALL.withDateRange(var0, var2);
      }

      public static CurrencyMetaInfo.CurrencyFilter onTender() {
         return ALL.withTender();
      }

      public CurrencyMetaInfo.CurrencyFilter withRegion(String var1) {
         return new CurrencyMetaInfo.CurrencyFilter(var1, this.currency, this.from, this.to, this.tenderOnly);
      }

      public CurrencyMetaInfo.CurrencyFilter withCurrency(String var1) {
         return new CurrencyMetaInfo.CurrencyFilter(this.region, var1, this.from, this.to, this.tenderOnly);
      }

      public CurrencyMetaInfo.CurrencyFilter withDate(Date var1) {
         return new CurrencyMetaInfo.CurrencyFilter(this.region, this.currency, var1.getTime(), var1.getTime(), this.tenderOnly);
      }

      public CurrencyMetaInfo.CurrencyFilter withDateRange(Date var1, Date var2) {
         long var3 = var1 == null ? Long.MIN_VALUE : var1.getTime();
         long var5 = var2 == null ? Long.MAX_VALUE : var2.getTime();
         return new CurrencyMetaInfo.CurrencyFilter(this.region, this.currency, var3, var5, this.tenderOnly);
      }

      public CurrencyMetaInfo.CurrencyFilter withDate(long var1) {
         return new CurrencyMetaInfo.CurrencyFilter(this.region, this.currency, var1, var1, this.tenderOnly);
      }

      public CurrencyMetaInfo.CurrencyFilter withDateRange(long var1, long var3) {
         return new CurrencyMetaInfo.CurrencyFilter(this.region, this.currency, var1, var3, this.tenderOnly);
      }

      public CurrencyMetaInfo.CurrencyFilter withTender() {
         return new CurrencyMetaInfo.CurrencyFilter(this.region, this.currency, this.from, this.to, true);
      }

      public boolean equals(Object var1) {
         return var1 instanceof CurrencyMetaInfo.CurrencyFilter && this != (CurrencyMetaInfo.CurrencyFilter)var1;
      }

      public int hashCode() {
         int var1 = 0;
         if (this.region != null) {
            var1 = this.region.hashCode();
         }

         if (this.currency != null) {
            var1 = var1 * 31 + this.currency.hashCode();
         }

         var1 = var1 * 31 + (int)this.from;
         var1 = var1 * 31 + (int)(this.from >>> 32);
         var1 = var1 * 31 + (int)this.to;
         var1 = var1 * 31 + (int)(this.to >>> 32);
         var1 = var1 * 31 + (this.tenderOnly ? 1 : 0);
         return var1;
      }

      public String toString() {
         return CurrencyMetaInfo.access$000(this);
      }
   }
}
