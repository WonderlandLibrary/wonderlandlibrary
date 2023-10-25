package com.ibm.icu.text;

import com.ibm.icu.impl.LocaleDisplayNamesImpl;
import com.ibm.icu.util.ULocale;
import java.util.Locale;

public abstract class LocaleDisplayNames {
  public enum DialectHandling {
    STANDARD_NAMES, DIALECT_NAMES;
  }
  
  public static LocaleDisplayNames getInstance(ULocale locale) {
    return getInstance(locale, DialectHandling.STANDARD_NAMES);
  }
  
  public static LocaleDisplayNames getInstance(ULocale locale, DialectHandling dialectHandling) {
    return LocaleDisplayNamesImpl.getInstance(locale, dialectHandling);
  }
  
  public static LocaleDisplayNames getInstance(ULocale locale, DisplayContext... contexts) {
    return LocaleDisplayNamesImpl.getInstance(locale, contexts);
  }
  
  public abstract ULocale getLocale();
  
  public abstract DialectHandling getDialectHandling();
  
  public abstract DisplayContext getContext(DisplayContext.Type paramType);
  
  public abstract String localeDisplayName(ULocale paramULocale);
  
  public abstract String localeDisplayName(Locale paramLocale);
  
  public abstract String localeDisplayName(String paramString);
  
  public abstract String languageDisplayName(String paramString);
  
  public abstract String scriptDisplayName(String paramString);
  
  public String scriptDisplayNameInContext(String script) {
    return scriptDisplayName(script);
  }
  
  public abstract String scriptDisplayName(int paramInt);
  
  public abstract String regionDisplayName(String paramString);
  
  public abstract String variantDisplayName(String paramString);
  
  public abstract String keyDisplayName(String paramString);
  
  public abstract String keyValueDisplayName(String paramString1, String paramString2);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\LocaleDisplayNames.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */