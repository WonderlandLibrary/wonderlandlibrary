package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.DisplayContext;
import com.ibm.icu.text.LocaleDisplayNames;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

public class LocaleDisplayNamesImpl extends LocaleDisplayNames {
   private final ULocale locale;
   private final LocaleDisplayNames.DialectHandling dialectHandling;
   private final DisplayContext capitalization;
   private final LocaleDisplayNamesImpl.DataTable langData;
   private final LocaleDisplayNamesImpl.DataTable regionData;
   private final LocaleDisplayNamesImpl.Appender appender;
   private final MessageFormat format;
   private final MessageFormat keyTypeFormat;
   private static final LocaleDisplayNamesImpl.Cache cache = new LocaleDisplayNamesImpl.Cache();
   private Map capitalizationUsage;
   private static final Map contextUsageTypeMap = new HashMap();

   public static LocaleDisplayNames getInstance(ULocale var0, LocaleDisplayNames.DialectHandling var1) {
      LocaleDisplayNamesImpl.Cache var2;
      synchronized(var2 = cache){}
      return cache.get(var0, var1);
   }

   public static LocaleDisplayNames getInstance(ULocale var0, DisplayContext... var1) {
      LocaleDisplayNamesImpl.Cache var2;
      synchronized(var2 = cache){}
      return cache.get(var0, var1);
   }

   public LocaleDisplayNamesImpl(ULocale var1, LocaleDisplayNames.DialectHandling var2) {
      this(var1, var2 == LocaleDisplayNames.DialectHandling.STANDARD_NAMES ? DisplayContext.STANDARD_NAMES : DisplayContext.DIALECT_NAMES, DisplayContext.CAPITALIZATION_NONE);
   }

   public LocaleDisplayNamesImpl(ULocale var1, DisplayContext... var2) {
      this.capitalizationUsage = null;
      LocaleDisplayNames.DialectHandling var3 = LocaleDisplayNames.DialectHandling.STANDARD_NAMES;
      DisplayContext var4 = DisplayContext.CAPITALIZATION_NONE;
      DisplayContext[] var5 = var2;
      int var6 = var2.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         DisplayContext var8 = var5[var7];
         switch(var8.type()) {
         case DIALECT_HANDLING:
            var3 = var8.value() == DisplayContext.STANDARD_NAMES.value() ? LocaleDisplayNames.DialectHandling.STANDARD_NAMES : LocaleDisplayNames.DialectHandling.DIALECT_NAMES;
            break;
         case CAPITALIZATION:
            var4 = var8;
         }
      }

      this.dialectHandling = var3;
      this.capitalization = var4;
      this.langData = LocaleDisplayNamesImpl.LangDataTables.impl.get(var1);
      this.regionData = LocaleDisplayNamesImpl.RegionDataTables.impl.get(var1);
      this.locale = ULocale.ROOT.equals(this.langData.getLocale()) ? this.regionData.getLocale() : this.langData.getLocale();
      String var19 = this.langData.get("localeDisplayPattern", "separator");
      if ("separator".equals(var19)) {
         var19 = ", ";
      }

      this.appender = new LocaleDisplayNamesImpl.Appender(var19);
      String var20 = this.langData.get("localeDisplayPattern", "pattern");
      if ("pattern".equals(var20)) {
         var20 = "{0} ({1})";
      }

      this.format = new MessageFormat(var20);
      String var21 = this.langData.get("localeDisplayPattern", "keyTypePattern");
      if ("keyTypePattern".equals(var21)) {
         var21 = "{0}={1}";
      }

      this.keyTypeFormat = new MessageFormat(var21);
      if (var4 == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU || var4 == DisplayContext.CAPITALIZATION_FOR_STANDALONE) {
         this.capitalizationUsage = new HashMap();
         boolean[] var22 = new boolean[]{false, false};
         LocaleDisplayNamesImpl.CapitalizationContextUsage[] var9 = LocaleDisplayNamesImpl.CapitalizationContextUsage.values();
         LocaleDisplayNamesImpl.CapitalizationContextUsage[] var10 = var9;
         int var11 = var9.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            LocaleDisplayNamesImpl.CapitalizationContextUsage var13 = var10[var12];
            this.capitalizationUsage.put(var13, var22);
         }

         ICUResourceBundle var23 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var1);
         ICUResourceBundle var24 = null;

         try {
            var24 = var23.getWithFallback("contextTransforms");
         } catch (MissingResourceException var18) {
            var24 = null;
         }

         if (var24 != null) {
            UResourceBundleIterator var25 = var24.getIterator();

            while(var25.hasNext()) {
               UResourceBundle var26 = var25.next();
               int[] var14 = var26.getIntVector();
               if (var14.length >= 2) {
                  String var15 = var26.getKey();
                  LocaleDisplayNamesImpl.CapitalizationContextUsage var16 = (LocaleDisplayNamesImpl.CapitalizationContextUsage)contextUsageTypeMap.get(var15);
                  if (var16 != null) {
                     boolean[] var17 = new boolean[]{var14[0] != 0, var14[1] != 0};
                     this.capitalizationUsage.put(var16, var17);
                  }
               }
            }
         }
      }

   }

   public ULocale getLocale() {
      return this.locale;
   }

   public LocaleDisplayNames.DialectHandling getDialectHandling() {
      return this.dialectHandling;
   }

   public DisplayContext getContext(DisplayContext.Type var1) {
      DisplayContext var2;
      switch(var1) {
      case DIALECT_HANDLING:
         var2 = this.dialectHandling == LocaleDisplayNames.DialectHandling.STANDARD_NAMES ? DisplayContext.STANDARD_NAMES : DisplayContext.DIALECT_NAMES;
         break;
      case CAPITALIZATION:
         var2 = this.capitalization;
         break;
      default:
         var2 = DisplayContext.STANDARD_NAMES;
      }

      return var2;
   }

   private String adjustForUsageAndContext(LocaleDisplayNamesImpl.CapitalizationContextUsage var1, String var2) {
      String var3 = var2;
      boolean var4 = false;
      switch(this.capitalization) {
      case CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE:
         var4 = true;
         break;
      case CAPITALIZATION_FOR_UI_LIST_OR_MENU:
      case CAPITALIZATION_FOR_STANDALONE:
         if (this.capitalizationUsage != null) {
            boolean[] var5 = (boolean[])this.capitalizationUsage.get(var1);
            var4 = this.capitalization == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU ? var5[0] : var5[1];
         }
      }

      if (var4) {
         int var6 = 8;
         int var7 = var2.length();
         if (var6 > var7) {
            var6 = var7;
         }

         int var10;
         for(var10 = 0; var10 < var6; ++var10) {
            int var8 = var2.codePointAt(var10);
            if (var8 < 65 || var8 > 90 && var8 < 97 || var8 > 122 && var8 < 192) {
               break;
            }

            if (var8 >= 65536) {
               ++var10;
            }
         }

         if (var10 > 0 && var10 < var7) {
            String var11 = var2.substring(0, var10);
            String var9 = UCharacter.toTitleCase(this.locale, var11, (BreakIterator)null, 768);
            var3 = var9.concat(var2.substring(var10));
         } else {
            var3 = UCharacter.toTitleCase(this.locale, var2, (BreakIterator)null, 768);
         }
      }

      return var3;
   }

   public String localeDisplayName(ULocale var1) {
      return this.localeDisplayNameInternal(var1);
   }

   public String localeDisplayName(Locale var1) {
      return this.localeDisplayNameInternal(ULocale.forLocale(var1));
   }

   public String localeDisplayName(String var1) {
      return this.localeDisplayNameInternal(new ULocale(var1));
   }

   private String localeDisplayNameInternal(ULocale var1) {
      String var2 = null;
      String var3 = var1.getLanguage();
      if (var1.getBaseName().length() == 0) {
         var3 = "root";
      }

      String var4 = var1.getScript();
      String var5 = var1.getCountry();
      String var6 = var1.getVariant();
      boolean var7 = var4.length() > 0;
      boolean var8 = var5.length() > 0;
      boolean var9 = var6.length() > 0;
      if (this.dialectHandling == LocaleDisplayNames.DialectHandling.DIALECT_NAMES) {
         label90: {
            String var10;
            String var11;
            if (var7 && var8) {
               var10 = var3 + '_' + var4 + '_' + var5;
               var11 = this.localeIdName(var10);
               if (!var11.equals(var10)) {
                  var2 = var11;
                  var7 = false;
                  var8 = false;
                  break label90;
               }
            }

            if (var7) {
               var10 = var3 + '_' + var4;
               var11 = this.localeIdName(var10);
               if (!var11.equals(var10)) {
                  var2 = var11;
                  var7 = false;
                  break label90;
               }
            }

            if (var8) {
               var10 = var3 + '_' + var5;
               var11 = this.localeIdName(var10);
               if (!var11.equals(var10)) {
                  var2 = var11;
                  var8 = false;
               }
            }
         }
      }

      if (var2 == null) {
         var2 = this.localeIdName(var3);
      }

      StringBuilder var17 = new StringBuilder();
      if (var7) {
         var17.append(this.scriptDisplayNameInContext(var4));
      }

      if (var8) {
         this.appender.append(this.regionDisplayName(var5), var17);
      }

      if (var9) {
         this.appender.append(this.variantDisplayName(var6), var17);
      }

      Iterator var18 = var1.getKeywords();
      String var12;
      if (var18 != null) {
         while(var18.hasNext()) {
            var12 = (String)var18.next();
            String var13 = var1.getKeywordValue(var12);
            String var14 = this.keyDisplayName(var12);
            String var15 = this.keyValueDisplayName(var12, var13);
            if (!var15.equals(var13)) {
               this.appender.append(var15, var17);
            } else if (!var12.equals(var14)) {
               String var16 = this.keyTypeFormat.format(new String[]{var14, var15});
               this.appender.append(var16, var17);
            } else {
               this.appender.append(var14, var17).append("=").append(var15);
            }
         }
      }

      var12 = null;
      if (var17.length() > 0) {
         var12 = var17.toString();
      }

      if (var12 != null) {
         var2 = this.format.format(new Object[]{var2, var12});
      }

      return this.adjustForUsageAndContext(LocaleDisplayNamesImpl.CapitalizationContextUsage.LANGUAGE, var2);
   }

   private String localeIdName(String var1) {
      return this.langData.get("Languages", var1);
   }

   public String languageDisplayName(String var1) {
      return !var1.equals("root") && var1.indexOf(95) == -1 ? this.adjustForUsageAndContext(LocaleDisplayNamesImpl.CapitalizationContextUsage.LANGUAGE, this.langData.get("Languages", var1)) : var1;
   }

   public String scriptDisplayName(String var1) {
      String var2 = this.langData.get("Scripts%stand-alone", var1);
      if (var2.equals(var1)) {
         var2 = this.langData.get("Scripts", var1);
      }

      return this.adjustForUsageAndContext(LocaleDisplayNamesImpl.CapitalizationContextUsage.SCRIPT, var2);
   }

   public String scriptDisplayNameInContext(String var1) {
      return this.adjustForUsageAndContext(LocaleDisplayNamesImpl.CapitalizationContextUsage.SCRIPT, this.langData.get("Scripts", var1));
   }

   public String scriptDisplayName(int var1) {
      return this.adjustForUsageAndContext(LocaleDisplayNamesImpl.CapitalizationContextUsage.SCRIPT, this.scriptDisplayName(UScript.getShortName(var1)));
   }

   public String regionDisplayName(String var1) {
      return this.adjustForUsageAndContext(LocaleDisplayNamesImpl.CapitalizationContextUsage.TERRITORY, this.regionData.get("Countries", var1));
   }

   public String variantDisplayName(String var1) {
      return this.adjustForUsageAndContext(LocaleDisplayNamesImpl.CapitalizationContextUsage.VARIANT, this.langData.get("Variants", var1));
   }

   public String keyDisplayName(String var1) {
      return this.adjustForUsageAndContext(LocaleDisplayNamesImpl.CapitalizationContextUsage.KEY, this.langData.get("Keys", var1));
   }

   public String keyValueDisplayName(String var1, String var2) {
      return this.adjustForUsageAndContext(LocaleDisplayNamesImpl.CapitalizationContextUsage.TYPE, this.langData.get("Types", var1, var2));
   }

   public static boolean haveData(LocaleDisplayNamesImpl.DataTableType var0) {
      switch(var0) {
      case LANG:
         return LocaleDisplayNamesImpl.LangDataTables.impl instanceof LocaleDisplayNamesImpl.ICUDataTables;
      case REGION:
         return LocaleDisplayNamesImpl.RegionDataTables.impl instanceof LocaleDisplayNamesImpl.ICUDataTables;
      default:
         throw new IllegalArgumentException("unknown type: " + var0);
      }
   }

   static {
      contextUsageTypeMap.put("languages", LocaleDisplayNamesImpl.CapitalizationContextUsage.LANGUAGE);
      contextUsageTypeMap.put("script", LocaleDisplayNamesImpl.CapitalizationContextUsage.SCRIPT);
      contextUsageTypeMap.put("territory", LocaleDisplayNamesImpl.CapitalizationContextUsage.TERRITORY);
      contextUsageTypeMap.put("variant", LocaleDisplayNamesImpl.CapitalizationContextUsage.VARIANT);
      contextUsageTypeMap.put("key", LocaleDisplayNamesImpl.CapitalizationContextUsage.KEY);
      contextUsageTypeMap.put("type", LocaleDisplayNamesImpl.CapitalizationContextUsage.TYPE);
   }

   private static class Cache {
      private ULocale locale;
      private LocaleDisplayNames.DialectHandling dialectHandling;
      private DisplayContext capitalization;
      private LocaleDisplayNames cache;

      private Cache() {
      }

      public LocaleDisplayNames get(ULocale var1, LocaleDisplayNames.DialectHandling var2) {
         if (var2 != this.dialectHandling || DisplayContext.CAPITALIZATION_NONE != this.capitalization || !var1.equals(this.locale)) {
            this.locale = var1;
            this.dialectHandling = var2;
            this.capitalization = DisplayContext.CAPITALIZATION_NONE;
            this.cache = new LocaleDisplayNamesImpl(var1, var2);
         }

         return this.cache;
      }

      public LocaleDisplayNames get(ULocale var1, DisplayContext... var2) {
         LocaleDisplayNames.DialectHandling var3 = LocaleDisplayNames.DialectHandling.STANDARD_NAMES;
         DisplayContext var4 = DisplayContext.CAPITALIZATION_NONE;
         DisplayContext[] var5 = var2;
         int var6 = var2.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            DisplayContext var8 = var5[var7];
            switch(var8.type()) {
            case DIALECT_HANDLING:
               var3 = var8.value() == DisplayContext.STANDARD_NAMES.value() ? LocaleDisplayNames.DialectHandling.STANDARD_NAMES : LocaleDisplayNames.DialectHandling.DIALECT_NAMES;
               break;
            case CAPITALIZATION:
               var4 = var8;
            }
         }

         if (var3 != this.dialectHandling || var4 != this.capitalization || !var1.equals(this.locale)) {
            this.locale = var1;
            this.dialectHandling = var3;
            this.capitalization = var4;
            this.cache = new LocaleDisplayNamesImpl(var1, var2);
         }

         return this.cache;
      }

      Cache(Object var1) {
         this();
      }
   }

   static class Appender {
      private final String sep;

      Appender(String var1) {
         this.sep = var1;
      }

      StringBuilder append(String var1, StringBuilder var2) {
         if (var2.length() > 0) {
            var2.append(this.sep);
         }

         var2.append(var1);
         return var2;
      }
   }

   public static enum DataTableType {
      LANG,
      REGION;

      private static final LocaleDisplayNamesImpl.DataTableType[] $VALUES = new LocaleDisplayNamesImpl.DataTableType[]{LANG, REGION};
   }

   static class RegionDataTables {
      static final LocaleDisplayNamesImpl.DataTables impl = LocaleDisplayNamesImpl.DataTables.load("com.ibm.icu.impl.ICURegionDataTables");
   }

   static class LangDataTables {
      static final LocaleDisplayNamesImpl.DataTables impl = LocaleDisplayNamesImpl.DataTables.load("com.ibm.icu.impl.ICULangDataTables");
   }

   abstract static class ICUDataTables extends LocaleDisplayNamesImpl.DataTables {
      private final String path;

      protected ICUDataTables(String var1) {
         this.path = var1;
      }

      public LocaleDisplayNamesImpl.DataTable get(ULocale var1) {
         return new LocaleDisplayNamesImpl.ICUDataTable(this.path, var1);
      }
   }

   abstract static class DataTables {
      public abstract LocaleDisplayNamesImpl.DataTable get(ULocale var1);

      public static LocaleDisplayNamesImpl.DataTables load(String var0) {
         try {
            return (LocaleDisplayNamesImpl.DataTables)Class.forName(var0).newInstance();
         } catch (Throwable var3) {
            LocaleDisplayNamesImpl.DataTable var2 = new LocaleDisplayNamesImpl.DataTable();
            return new LocaleDisplayNamesImpl.DataTables(var2) {
               final LocaleDisplayNamesImpl.DataTable val$NO_OP;

               {
                  this.val$NO_OP = var1;
               }

               public LocaleDisplayNamesImpl.DataTable get(ULocale var1) {
                  return this.val$NO_OP;
               }
            };
         }
      }
   }

   static class ICUDataTable extends LocaleDisplayNamesImpl.DataTable {
      private final ICUResourceBundle bundle;

      public ICUDataTable(String var1, ULocale var2) {
         this.bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(var1, var2.getBaseName());
      }

      public ULocale getLocale() {
         return this.bundle.getULocale();
      }

      public String get(String var1, String var2, String var3) {
         return ICUResourceTableAccess.getTableString(this.bundle, var1, var2, var3);
      }
   }

   public static class DataTable {
      ULocale getLocale() {
         return ULocale.ROOT;
      }

      String get(String var1, String var2) {
         return this.get(var1, (String)null, var2);
      }

      String get(String var1, String var2, String var3) {
         return var3;
      }
   }

   private static enum CapitalizationContextUsage {
      LANGUAGE,
      SCRIPT,
      TERRITORY,
      VARIANT,
      KEY,
      TYPE;

      private static final LocaleDisplayNamesImpl.CapitalizationContextUsage[] $VALUES = new LocaleDisplayNamesImpl.CapitalizationContextUsage[]{LANGUAGE, SCRIPT, TERRITORY, VARIANT, KEY, TYPE};
   }
}
