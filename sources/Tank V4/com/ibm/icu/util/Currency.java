package com.ibm.icu.util;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.TextTrieMap;
import com.ibm.icu.text.CurrencyDisplayNames;
import com.ibm.icu.text.CurrencyMetaInfo;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.Map.Entry;

public class Currency extends MeasureUnit implements Serializable {
   private static final long serialVersionUID = -5839973855554750484L;
   private static final boolean DEBUG = ICUDebug.enabled("currency");
   private static ICUCache CURRENCY_NAME_CACHE = new SimpleCache();
   private String isoCode;
   public static final int SYMBOL_NAME = 0;
   public static final int LONG_NAME = 1;
   public static final int PLURAL_LONG_NAME = 2;
   private static Currency.ServiceShim shim;
   private static final String EUR_STR = "EUR";
   private static final ICUCache currencyCodeCache = new SimpleCache();
   private static final ULocale UND = new ULocale("und");
   private static final String[] EMPTY_STRING_ARRAY = new String[0];
   private static final int[] POW10 = new int[]{1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
   private static SoftReference ALL_TENDER_CODES;
   private static SoftReference ALL_CODES_AS_SET;

   private static Currency.ServiceShim getShim() {
      if (shim == null) {
         try {
            Class var0 = Class.forName("com.ibm.icu.util.CurrencyServiceShim");
            shim = (Currency.ServiceShim)var0.newInstance();
         } catch (Exception var1) {
            if (DEBUG) {
               var1.printStackTrace();
            }

            throw new RuntimeException(var1.getMessage());
         }
      }

      return shim;
   }

   public static Currency getInstance(Locale var0) {
      return getInstance(ULocale.forLocale(var0));
   }

   public static Currency getInstance(ULocale var0) {
      String var1 = var0.getKeywordValue("currency");
      if (var1 != null) {
         return getInstance(var1);
      } else {
         return shim == null ? createCurrency(var0) : shim.createInstance(var0);
      }
   }

   public static String[] getAvailableCurrencyCodes(ULocale var0, Date var1) {
      CurrencyMetaInfo.CurrencyFilter var2 = CurrencyMetaInfo.CurrencyFilter.onDate(var1).withRegion(var0.getCountry());
      List var3 = getTenderCurrencies(var2);
      return var3.isEmpty() ? null : (String[])var3.toArray(new String[var3.size()]);
   }

   public static Set getAvailableCurrencies() {
      CurrencyMetaInfo var0 = CurrencyMetaInfo.getInstance();
      List var1 = var0.currencies(CurrencyMetaInfo.CurrencyFilter.all());
      HashSet var2 = new HashSet(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add(new Currency(var4));
      }

      return var2;
   }

   static Currency createCurrency(ULocale var0) {
      String var1 = var0.getVariant();
      if ("EURO".equals(var1)) {
         return new Currency("EUR");
      } else {
         String var2 = (String)currencyCodeCache.get(var0);
         if (var2 == null) {
            String var3 = var0.getCountry();
            CurrencyMetaInfo var4 = CurrencyMetaInfo.getInstance();
            List var5 = var4.currencies(CurrencyMetaInfo.CurrencyFilter.onRegion(var3));
            if (var5.size() <= 0) {
               return null;
            }

            var2 = (String)var5.get(0);
            boolean var6 = "PREEURO".equals(var1);
            if (var6 && "EUR".equals(var2)) {
               if (var5.size() < 2) {
                  return null;
               }

               var2 = (String)var5.get(1);
            }

            currencyCodeCache.put(var0, var2);
         }

         return new Currency(var2);
      }
   }

   public static Currency getInstance(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static Object registerInstance(Currency var0, ULocale var1) {
      return getShim().registerInstance(var0, var1);
   }

   public static boolean unregister(Object var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("registryKey must not be null");
      } else {
         return shim == null ? false : shim.unregister(var0);
      }
   }

   public static Locale[] getAvailableLocales() {
      return shim == null ? ICUResourceBundle.getAvailableLocales() : shim.getAvailableLocales();
   }

   public static ULocale[] getAvailableULocales() {
      return shim == null ? ICUResourceBundle.getAvailableULocales() : shim.getAvailableULocales();
   }

   public static final String[] getKeywordValuesForLocale(String var0, ULocale var1, boolean var2) {
      if (!"currency".equals(var0)) {
         return EMPTY_STRING_ARRAY;
      } else if (!var2) {
         return (String[])getAllTenderCurrencies().toArray(new String[0]);
      } else {
         String var3 = var1.getCountry();
         if (var3.length() == 0) {
            if (UND.equals(var1)) {
               return EMPTY_STRING_ARRAY;
            }

            ULocale var4 = ULocale.addLikelySubtags(var1);
            var3 = var4.getCountry();
         }

         CurrencyMetaInfo.CurrencyFilter var6 = CurrencyMetaInfo.CurrencyFilter.now().withRegion(var3);
         List var5 = getTenderCurrencies(var6);
         return var5.size() == 0 ? EMPTY_STRING_ARRAY : (String[])var5.toArray(new String[var5.size()]);
      }
   }

   public int hashCode() {
      return this.isoCode.hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else {
         try {
            Currency var2 = (Currency)var1;
            return this.isoCode.equals(var2.isoCode);
         } catch (ClassCastException var3) {
            return false;
         }
      }
   }

   public String getCurrencyCode() {
      return this.isoCode;
   }

   public int getNumericCode() {
      int var1 = 0;

      try {
         UResourceBundle var2 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "currencyNumericCodes", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
         UResourceBundle var3 = var2.get("codeMap");
         UResourceBundle var4 = var3.get(this.isoCode);
         var1 = var4.getInt();
      } catch (MissingResourceException var5) {
      }

      return var1;
   }

   public String getSymbol() {
      return this.getSymbol(ULocale.getDefault(ULocale.Category.DISPLAY));
   }

   public String getSymbol(Locale var1) {
      return this.getSymbol(ULocale.forLocale(var1));
   }

   public String getSymbol(ULocale var1) {
      return this.getName((ULocale)var1, 0, new boolean[1]);
   }

   public String getName(Locale var1, int var2, boolean[] var3) {
      return this.getName(ULocale.forLocale(var1), var2, var3);
   }

   public String getName(ULocale var1, int var2, boolean[] var3) {
      if (var2 != 0 && var2 != 1) {
         throw new IllegalArgumentException("bad name style: " + var2);
      } else {
         if (var3 != null) {
            var3[0] = false;
         }

         CurrencyDisplayNames var4 = CurrencyDisplayNames.getInstance(var1);
         return var2 == 0 ? var4.getSymbol(this.isoCode) : var4.getName(this.isoCode);
      }
   }

   public String getName(Locale var1, int var2, String var3, boolean[] var4) {
      return this.getName(ULocale.forLocale(var1), var2, var3, var4);
   }

   public String getName(ULocale var1, int var2, String var3, boolean[] var4) {
      if (var2 != 2) {
         return this.getName(var1, var2, var4);
      } else {
         if (var4 != null) {
            var4[0] = false;
         }

         CurrencyDisplayNames var5 = CurrencyDisplayNames.getInstance(var1);
         return var5.getPluralName(this.isoCode, var3);
      }
   }

   public String getDisplayName() {
      return this.getName((Locale)Locale.getDefault(), 1, (boolean[])null);
   }

   public String getDisplayName(Locale var1) {
      return this.getName((Locale)var1, 1, (boolean[])null);
   }

   /** @deprecated */
   public static String parse(ULocale var0, String var1, int var2, ParsePosition var3) {
      Object var4 = (List)CURRENCY_NAME_CACHE.get(var0);
      if (var4 == null) {
         TextTrieMap var5 = new TextTrieMap(true);
         TextTrieMap var6 = new TextTrieMap(false);
         var4 = new ArrayList();
         ((List)var4).add(var6);
         ((List)var4).add(var5);
         setupCurrencyTrieVec(var0, (List)var4);
         CURRENCY_NAME_CACHE.put(var0, var4);
      }

      int var15 = 0;
      String var16 = null;
      TextTrieMap var7 = (TextTrieMap)((List)var4).get(1);
      Currency.CurrencyNameResultHandler var8 = new Currency.CurrencyNameResultHandler();
      var7.find(var1, var3.getIndex(), var8);
      List var9 = var8.getMatchedCurrencyNames();
      String var13;
      if (var9 != null && var9.size() != 0) {
         Iterator var10 = var9.iterator();

         while(var10.hasNext()) {
            Currency.CurrencyStringInfo var11 = (Currency.CurrencyStringInfo)var10.next();
            String var12 = Currency.CurrencyStringInfo.access$100(var11);
            var13 = Currency.CurrencyStringInfo.access$200(var11);
            if (var13.length() > var15) {
               var15 = var13.length();
               var16 = var12;
            }
         }
      }

      if (var2 != 1) {
         TextTrieMap var17 = (TextTrieMap)((List)var4).get(0);
         var8 = new Currency.CurrencyNameResultHandler();
         var17.find(var1, var3.getIndex(), var8);
         var9 = var8.getMatchedCurrencyNames();
         if (var9 != null && var9.size() != 0) {
            Iterator var19 = var9.iterator();

            while(var19.hasNext()) {
               Currency.CurrencyStringInfo var20 = (Currency.CurrencyStringInfo)var19.next();
               var13 = Currency.CurrencyStringInfo.access$100(var20);
               String var14 = Currency.CurrencyStringInfo.access$200(var20);
               if (var14.length() > var15) {
                  var15 = var14.length();
                  var16 = var13;
               }
            }
         }
      }

      int var18 = var3.getIndex();
      var3.setIndex(var18 + var15);
      return var16;
   }

   private static void setupCurrencyTrieVec(ULocale var0, List var1) {
      TextTrieMap var2 = (TextTrieMap)var1.get(0);
      TextTrieMap var3 = (TextTrieMap)var1.get(1);
      CurrencyDisplayNames var4 = CurrencyDisplayNames.getInstance(var0);
      Iterator var5 = var4.symbolMap().entrySet().iterator();

      Entry var6;
      String var7;
      String var8;
      while(var5.hasNext()) {
         var6 = (Entry)var5.next();
         var7 = (String)var6.getKey();
         var8 = (String)var6.getValue();
         var2.put(var7, new Currency.CurrencyStringInfo(var8, var7));
      }

      var5 = var4.nameMap().entrySet().iterator();

      while(var5.hasNext()) {
         var6 = (Entry)var5.next();
         var7 = (String)var6.getKey();
         var8 = (String)var6.getValue();
         var3.put(var7, new Currency.CurrencyStringInfo(var8, var7));
      }

   }

   public int getDefaultFractionDigits() {
      CurrencyMetaInfo var1 = CurrencyMetaInfo.getInstance();
      CurrencyMetaInfo.CurrencyDigits var2 = var1.currencyDigits(this.isoCode);
      return var2.fractionDigits;
   }

   public double getRoundingIncrement() {
      CurrencyMetaInfo var1 = CurrencyMetaInfo.getInstance();
      CurrencyMetaInfo.CurrencyDigits var2 = var1.currencyDigits(this.isoCode);
      int var3 = var2.roundingIncrement;
      if (var3 == 0) {
         return 0.0D;
      } else {
         int var4 = var2.fractionDigits;
         return var4 >= 0 && var4 < POW10.length ? (double)var3 / (double)POW10[var4] : 0.0D;
      }
   }

   public String toString() {
      return this.isoCode;
   }

   protected Currency(String var1) {
      this.isoCode = var1;
   }

   private static synchronized List getAllTenderCurrencies() {
      List var0 = ALL_TENDER_CODES == null ? null : (List)ALL_TENDER_CODES.get();
      if (var0 == null) {
         CurrencyMetaInfo.CurrencyFilter var1 = CurrencyMetaInfo.CurrencyFilter.all();
         var0 = Collections.unmodifiableList(getTenderCurrencies(var1));
         ALL_TENDER_CODES = new SoftReference(var0);
      }

      return var0;
   }

   private static synchronized Set getAllCurrenciesAsSet() {
      Set var0 = ALL_CODES_AS_SET == null ? null : (Set)ALL_CODES_AS_SET.get();
      if (var0 == null) {
         CurrencyMetaInfo var1 = CurrencyMetaInfo.getInstance();
         var0 = Collections.unmodifiableSet(new HashSet(var1.currencies(CurrencyMetaInfo.CurrencyFilter.all())));
         ALL_CODES_AS_SET = new SoftReference(var0);
      }

      return var0;
   }

   public static boolean isAvailable(String param0, Date param1, Date param2) {
      // $FF: Couldn't be decompiled
   }

   private static List getTenderCurrencies(CurrencyMetaInfo.CurrencyFilter var0) {
      CurrencyMetaInfo var1 = CurrencyMetaInfo.getInstance();
      return var1.currencies(var0.withTender());
   }

   private static class CurrencyNameResultHandler implements TextTrieMap.ResultHandler {
      private ArrayList resultList;

      private CurrencyNameResultHandler() {
      }

      public boolean handlePrefixMatch(int var1, Iterator var2) {
         if (this.resultList == null) {
            this.resultList = new ArrayList();
         }

         while(var2.hasNext()) {
            Currency.CurrencyStringInfo var3 = (Currency.CurrencyStringInfo)var2.next();
            if (var3 == null) {
               break;
            }

            int var4;
            for(var4 = 0; var4 < this.resultList.size(); ++var4) {
               Currency.CurrencyStringInfo var5 = (Currency.CurrencyStringInfo)this.resultList.get(var4);
               if (Currency.CurrencyStringInfo.access$100(var3).equals(Currency.CurrencyStringInfo.access$100(var5))) {
                  if (var1 > Currency.CurrencyStringInfo.access$200(var5).length()) {
                     this.resultList.set(var4, var3);
                  }
                  break;
               }
            }

            if (var4 == this.resultList.size()) {
               this.resultList.add(var3);
            }
         }

         return true;
      }

      List getMatchedCurrencyNames() {
         return this.resultList != null && this.resultList.size() != 0 ? this.resultList : null;
      }

      CurrencyNameResultHandler(Object var1) {
         this();
      }
   }

   private static final class CurrencyStringInfo {
      private String isoCode;
      private String currencyString;

      public CurrencyStringInfo(String var1, String var2) {
         this.isoCode = var1;
         this.currencyString = var2;
      }

      private String getISOCode() {
         return this.isoCode;
      }

      private String getCurrencyString() {
         return this.currencyString;
      }

      static String access$100(Currency.CurrencyStringInfo var0) {
         return var0.getISOCode();
      }

      static String access$200(Currency.CurrencyStringInfo var0) {
         return var0.getCurrencyString();
      }
   }

   abstract static class ServiceShim {
      abstract ULocale[] getAvailableULocales();

      abstract Locale[] getAvailableLocales();

      abstract Currency createInstance(ULocale var1);

      abstract Object registerInstance(Currency var1, ULocale var2);

      abstract boolean unregister(Object var1);
   }
}
