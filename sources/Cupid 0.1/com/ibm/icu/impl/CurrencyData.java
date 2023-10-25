package com.ibm.icu.impl;

import com.ibm.icu.text.CurrencyDisplayNames;
import com.ibm.icu.util.ULocale;
import java.util.Collections;
import java.util.Map;

public class CurrencyData {
  public static final CurrencyDisplayInfoProvider provider;
  
  public static abstract class CurrencyDisplayInfo extends CurrencyDisplayNames {
    public abstract Map<String, String> getUnitPatterns();
    
    public abstract CurrencyData.CurrencyFormatInfo getFormatInfo(String param1String);
    
    public abstract CurrencyData.CurrencySpacingInfo getSpacingInfo();
  }
  
  public static final class CurrencyFormatInfo {
    public final String currencyPattern;
    
    public final char monetarySeparator;
    
    public final char monetaryGroupingSeparator;
    
    public CurrencyFormatInfo(String currencyPattern, char monetarySeparator, char monetaryGroupingSeparator) {
      this.currencyPattern = currencyPattern;
      this.monetarySeparator = monetarySeparator;
      this.monetaryGroupingSeparator = monetaryGroupingSeparator;
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
    
    public CurrencySpacingInfo(String beforeCurrencyMatch, String beforeContextMatch, String beforeInsert, String afterCurrencyMatch, String afterContextMatch, String afterInsert) {
      this.beforeCurrencyMatch = beforeCurrencyMatch;
      this.beforeContextMatch = beforeContextMatch;
      this.beforeInsert = beforeInsert;
      this.afterCurrencyMatch = afterCurrencyMatch;
      this.afterContextMatch = afterContextMatch;
      this.afterInsert = afterInsert;
    }
    
    public static final CurrencySpacingInfo DEFAULT = new CurrencySpacingInfo("[:letter:]", "[:digit:]", " ", "[:letter:]", "[:digit:]", " ");
  }
  
  static {
    CurrencyDisplayInfoProvider temp = null;
    try {
      Class<?> clzz = Class.forName("com.ibm.icu.impl.ICUCurrencyDisplayInfoProvider");
      temp = (CurrencyDisplayInfoProvider)clzz.newInstance();
    } catch (Throwable t) {
      temp = new CurrencyDisplayInfoProvider() {
          public CurrencyData.CurrencyDisplayInfo getInstance(ULocale locale, boolean withFallback) {
            return CurrencyData.DefaultInfo.getWithFallback(withFallback);
          }
          
          public boolean hasData() {
            return false;
          }
        };
    } 
    provider = temp;
  }
  
  public static class DefaultInfo extends CurrencyDisplayInfo {
    private final boolean fallback;
    
    private DefaultInfo(boolean fallback) {
      this.fallback = fallback;
    }
    
    public static final CurrencyData.CurrencyDisplayInfo getWithFallback(boolean fallback) {
      return fallback ? FALLBACK_INSTANCE : NO_FALLBACK_INSTANCE;
    }
    
    public String getName(String isoCode) {
      return this.fallback ? isoCode : null;
    }
    
    public String getPluralName(String isoCode, String pluralType) {
      return this.fallback ? isoCode : null;
    }
    
    public String getSymbol(String isoCode) {
      return this.fallback ? isoCode : null;
    }
    
    public Map<String, String> symbolMap() {
      return Collections.emptyMap();
    }
    
    public Map<String, String> nameMap() {
      return Collections.emptyMap();
    }
    
    public ULocale getULocale() {
      return ULocale.ROOT;
    }
    
    public Map<String, String> getUnitPatterns() {
      if (this.fallback)
        return Collections.emptyMap(); 
      return null;
    }
    
    public CurrencyData.CurrencyFormatInfo getFormatInfo(String isoCode) {
      return null;
    }
    
    public CurrencyData.CurrencySpacingInfo getSpacingInfo() {
      return this.fallback ? CurrencyData.CurrencySpacingInfo.DEFAULT : null;
    }
    
    private static final CurrencyData.CurrencyDisplayInfo FALLBACK_INSTANCE = new DefaultInfo(true);
    
    private static final CurrencyData.CurrencyDisplayInfo NO_FALLBACK_INSTANCE = new DefaultInfo(false);
  }
  
  public static interface CurrencyDisplayInfoProvider {
    CurrencyData.CurrencyDisplayInfo getInstance(ULocale param1ULocale, boolean param1Boolean);
    
    boolean hasData();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\CurrencyData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */