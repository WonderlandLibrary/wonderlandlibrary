package com.ibm.icu.text;

import com.ibm.icu.impl.LocaleDisplayNamesImpl;
import com.ibm.icu.util.ULocale;
import java.util.Locale;

public abstract class LocaleDisplayNames {
   public static LocaleDisplayNames getInstance(ULocale var0) {
      return getInstance(var0, LocaleDisplayNames.DialectHandling.STANDARD_NAMES);
   }

   public static LocaleDisplayNames getInstance(ULocale var0, LocaleDisplayNames.DialectHandling var1) {
      return LocaleDisplayNamesImpl.getInstance(var0, var1);
   }

   public static LocaleDisplayNames getInstance(ULocale var0, DisplayContext... var1) {
      return LocaleDisplayNamesImpl.getInstance(var0, var1);
   }

   public abstract ULocale getLocale();

   public abstract LocaleDisplayNames.DialectHandling getDialectHandling();

   public abstract DisplayContext getContext(DisplayContext.Type var1);

   public abstract String localeDisplayName(ULocale var1);

   public abstract String localeDisplayName(Locale var1);

   public abstract String localeDisplayName(String var1);

   public abstract String languageDisplayName(String var1);

   public abstract String scriptDisplayName(String var1);

   /** @deprecated */
   public String scriptDisplayNameInContext(String var1) {
      return this.scriptDisplayName(var1);
   }

   public abstract String scriptDisplayName(int var1);

   public abstract String regionDisplayName(String var1);

   public abstract String variantDisplayName(String var1);

   public abstract String keyDisplayName(String var1);

   public abstract String keyValueDisplayName(String var1, String var2);

   /** @deprecated */
   protected LocaleDisplayNames() {
   }

   public static enum DialectHandling {
      STANDARD_NAMES,
      DIALECT_NAMES;

      private static final LocaleDisplayNames.DialectHandling[] $VALUES = new LocaleDisplayNames.DialectHandling[]{STANDARD_NAMES, DIALECT_NAMES};
   }
}
