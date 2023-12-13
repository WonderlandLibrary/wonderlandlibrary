package com.ibm.icu.util;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.text.UnicodeSet;
import java.util.MissingResourceException;

public final class LocaleData {
   private static final String MEASUREMENT_SYSTEM = "MeasurementSystem";
   private static final String PAPER_SIZE = "PaperSize";
   private static final String LOCALE_DISPLAY_PATTERN = "localeDisplayPattern";
   private static final String PATTERN = "pattern";
   private static final String SEPARATOR = "separator";
   private boolean noSubstitute;
   private ICUResourceBundle bundle;
   private ICUResourceBundle langBundle;
   public static final int ES_STANDARD = 0;
   public static final int ES_AUXILIARY = 1;
   public static final int ES_INDEX = 2;
   /** @deprecated */
   public static final int ES_CURRENCY = 3;
   public static final int ES_PUNCTUATION = 4;
   public static final int ES_COUNT = 5;
   public static final int QUOTATION_START = 0;
   public static final int QUOTATION_END = 1;
   public static final int ALT_QUOTATION_START = 2;
   public static final int ALT_QUOTATION_END = 3;
   public static final int DELIMITER_COUNT = 4;
   private static final String[] DELIMITER_TYPES = new String[]{"quotationStart", "quotationEnd", "alternateQuotationStart", "alternateQuotationEnd"};
   private static VersionInfo gCLDRVersion = null;

   private LocaleData() {
   }

   public static UnicodeSet getExemplarSet(ULocale var0, int var1) {
      return getInstance(var0).getExemplarSet(var1, 0);
   }

   public static UnicodeSet getExemplarSet(ULocale var0, int var1, int var2) {
      return getInstance(var0).getExemplarSet(var1, var2);
   }

   public UnicodeSet getExemplarSet(int var1, int var2) {
      String[] var3 = new String[]{"ExemplarCharacters", "AuxExemplarCharacters", "ExemplarCharactersIndex", "ExemplarCharactersCurrency", "ExemplarCharactersPunctuation"};
      if (var2 == 3) {
         return new UnicodeSet();
      } else {
         try {
            ICUResourceBundle var4 = (ICUResourceBundle)this.bundle.get(var3[var2]);
            if (this.noSubstitute && var4.getLoadingStatus() == 2) {
               return null;
            } else {
               String var5 = var4.getString();
               if (var2 == 4) {
                  try {
                     return new UnicodeSet(var5, 1 | var1);
                  } catch (IllegalArgumentException var7) {
                     throw new IllegalArgumentException("Can't create exemplars for " + var3[var2] + " in " + this.bundle.getLocale(), var7);
                  }
               } else {
                  return new UnicodeSet(var5, 1 | var1);
               }
            }
         } catch (MissingResourceException var8) {
            if (var2 == 1) {
               return new UnicodeSet();
            } else if (var2 == 2) {
               return null;
            } else {
               throw var8;
            }
         }
      }
   }

   public static final LocaleData getInstance(ULocale var0) {
      LocaleData var1 = new LocaleData();
      var1.bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var0);
      var1.langBundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/lang", var0);
      var1.noSubstitute = false;
      return var1;
   }

   public static final LocaleData getInstance() {
      return getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public void setNoSubstitute(boolean var1) {
      this.noSubstitute = var1;
   }

   public boolean getNoSubstitute() {
      return this.noSubstitute;
   }

   public String getDelimiter(int var1) {
      ICUResourceBundle var2 = (ICUResourceBundle)this.bundle.get("delimiters");
      ICUResourceBundle var3 = var2.getWithFallback(DELIMITER_TYPES[var1]);
      return this.noSubstitute && var3.getLoadingStatus() == 2 ? null : var3.getString();
   }

   public static final LocaleData.MeasurementSystem getMeasurementSystem(ULocale var0) {
      ICUResourceBundle var1 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var0);
      UResourceBundle var2 = var1.get("MeasurementSystem");
      int var3 = var2.getInt();
      if (LocaleData.MeasurementSystem.access$000(LocaleData.MeasurementSystem.US, var3)) {
         return LocaleData.MeasurementSystem.US;
      } else {
         return LocaleData.MeasurementSystem.access$000(LocaleData.MeasurementSystem.SI, var3) ? LocaleData.MeasurementSystem.SI : null;
      }
   }

   public static final LocaleData.PaperSize getPaperSize(ULocale var0) {
      ICUResourceBundle var1 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var0);
      UResourceBundle var2 = var1.get("PaperSize");
      int[] var3 = var2.getIntVector();
      return new LocaleData.PaperSize(var3[0], var3[1]);
   }

   public String getLocaleDisplayPattern() {
      ICUResourceBundle var1 = (ICUResourceBundle)this.langBundle.get("localeDisplayPattern");
      String var2 = var1.getStringWithFallback("pattern");
      return var2;
   }

   public String getLocaleSeparator() {
      ICUResourceBundle var1 = (ICUResourceBundle)this.langBundle.get("localeDisplayPattern");
      String var2 = var1.getStringWithFallback("separator");
      return var2;
   }

   public static VersionInfo getCLDRVersion() {
      if (gCLDRVersion == null) {
         UResourceBundle var0 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
         UResourceBundle var1 = var0.get("cldrVersion");
         gCLDRVersion = VersionInfo.getInstance(var1.getString());
      }

      return gCLDRVersion;
   }

   public static final class PaperSize {
      private int height;
      private int width;

      private PaperSize(int var1, int var2) {
         this.height = var1;
         this.width = var2;
      }

      public int getHeight() {
         return this.height;
      }

      public int getWidth() {
         return this.width;
      }

      PaperSize(int var1, int var2, Object var3) {
         this(var1, var2);
      }
   }

   public static final class MeasurementSystem {
      public static final LocaleData.MeasurementSystem SI = new LocaleData.MeasurementSystem(0);
      public static final LocaleData.MeasurementSystem US = new LocaleData.MeasurementSystem(1);
      private int systemID;

      private MeasurementSystem(int var1) {
         this.systemID = var1;
      }

      private boolean equals(int var1) {
         return this.systemID == var1;
      }

      static boolean access$000(LocaleData.MeasurementSystem var0, int var1) {
         return var0.equals(var1);
      }
   }
}
