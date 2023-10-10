package com.ibm.icu.impl;

import com.ibm.icu.text.CurrencyDisplayNames;
import com.ibm.icu.util.ULocale;
import java.util.Collections;
import java.util.Map;

public class CurrencyData {
   public static final CurrencyData.CurrencyDisplayInfoProvider provider;

   static {
      CurrencyData.CurrencyDisplayInfoProvider var0 = null;

      try {
         Class var1 = Class.forName("com.ibm.icu.impl.ICUCurrencyDisplayInfoProvider");
         var0 = (CurrencyData.CurrencyDisplayInfoProvider)var1.newInstance();
      } catch (Throwable var2) {
         var0 = new CurrencyData.CurrencyDisplayInfoProvider() {
            public CurrencyData.CurrencyDisplayInfo getInstance(ULocale var1, boolean var2) {
               return CurrencyData.DefaultInfo.getWithFallback(var2);
            }

            public boolean hasData() {
               return false;
            }
         };
      }

      provider = var0;
   }

   public static class DefaultInfo extends CurrencyData.CurrencyDisplayInfo {
      private final boolean fallback;
      private static final CurrencyData.CurrencyDisplayInfo FALLBACK_INSTANCE = new CurrencyData.DefaultInfo(true);
      private static final CurrencyData.CurrencyDisplayInfo NO_FALLBACK_INSTANCE = new CurrencyData.DefaultInfo(false);

      private DefaultInfo(boolean var1) {
         this.fallback = var1;
      }

      public static final CurrencyData.CurrencyDisplayInfo getWithFallback(boolean var0) {
         return var0 ? FALLBACK_INSTANCE : NO_FALLBACK_INSTANCE;
      }

      public String getName(String var1) {
         return this.fallback ? var1 : null;
      }

      public String getPluralName(String var1, String var2) {
         return this.fallback ? var1 : null;
      }

      public String getSymbol(String var1) {
         return this.fallback ? var1 : null;
      }

      public Map symbolMap() {
         return Collections.emptyMap();
      }

      public Map nameMap() {
         return Collections.emptyMap();
      }

      public ULocale getULocale() {
         return ULocale.ROOT;
      }

      public Map getUnitPatterns() {
         return this.fallback ? Collections.emptyMap() : null;
      }

      public CurrencyData.CurrencyFormatInfo getFormatInfo(String var1) {
         return null;
      }

      public CurrencyData.CurrencySpacingInfo getSpacingInfo() {
         return this.fallback ? CurrencyData.CurrencySpacingInfo.DEFAULT : null;
      }
   }

   public static final class CurrencySpacingInfo {
      public final String beforeCurrencyMatch;
      public final String beforeContextMatch;
      public final String beforeInsert;
      public final String afterCurrencyMatch;
      public final String afterContextMatch;
      public final String afterInsert;
      private static final String DEFAULT_CUR_MATCH = "[:letter:]";
      private static final String DEFAULT_CTX_MATCH = "[:digit:]";
      private static final String DEFAULT_INSERT = " ";
      public static final CurrencyData.CurrencySpacingInfo DEFAULT = new CurrencyData.CurrencySpacingInfo("[:letter:]", "[:digit:]", " ", "[:letter:]", "[:digit:]", " ");

      public CurrencySpacingInfo(String var1, String var2, String var3, String var4, String var5, String var6) {
         this.beforeCurrencyMatch = var1;
         this.beforeContextMatch = var2;
         this.beforeInsert = var3;
         this.afterCurrencyMatch = var4;
         this.afterContextMatch = var5;
         this.afterInsert = var6;
      }
   }

   public static final class CurrencyFormatInfo {
      public final String currencyPattern;
      public final char monetarySeparator;
      public final char monetaryGroupingSeparator;

      public CurrencyFormatInfo(String var1, char var2, char var3) {
         this.currencyPattern = var1;
         this.monetarySeparator = var2;
         this.monetaryGroupingSeparator = var3;
      }
   }

   public abstract static class CurrencyDisplayInfo extends CurrencyDisplayNames {
      public abstract Map getUnitPatterns();

      public abstract CurrencyData.CurrencyFormatInfo getFormatInfo(String var1);

      public abstract CurrencyData.CurrencySpacingInfo getSpacingInfo();
   }

   public interface CurrencyDisplayInfoProvider {
      CurrencyData.CurrencyDisplayInfo getInstance(ULocale var1, boolean var2);

      boolean hasData();
   }
}
