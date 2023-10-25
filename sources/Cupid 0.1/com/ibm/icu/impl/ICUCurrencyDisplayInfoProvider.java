package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ICUCurrencyDisplayInfoProvider implements CurrencyData.CurrencyDisplayInfoProvider {
  public CurrencyData.CurrencyDisplayInfo getInstance(ULocale locale, boolean withFallback) {
    ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/curr", locale);
    if (!withFallback) {
      int status = rb.getLoadingStatus();
      if (status == 3 || status == 2)
        return null; 
    } 
    return new ICUCurrencyDisplayInfo(rb, withFallback);
  }
  
  public boolean hasData() {
    return true;
  }
  
  static class ICUCurrencyDisplayInfo extends CurrencyData.CurrencyDisplayInfo {
    private final boolean fallback;
    
    private final ICUResourceBundle rb;
    
    private final ICUResourceBundle currencies;
    
    private final ICUResourceBundle plurals;
    
    private SoftReference<Map<String, String>> _symbolMapRef;
    
    private SoftReference<Map<String, String>> _nameMapRef;
    
    public ICUCurrencyDisplayInfo(ICUResourceBundle rb, boolean fallback) {
      this.fallback = fallback;
      this.rb = rb;
      this.currencies = rb.findTopLevel("Currencies");
      this.plurals = rb.findTopLevel("CurrencyPlurals");
    }
    
    public ULocale getULocale() {
      return this.rb.getULocale();
    }
    
    public String getName(String isoCode) {
      return getName(isoCode, false);
    }
    
    public String getSymbol(String isoCode) {
      return getName(isoCode, true);
    }
    
    private String getName(String isoCode, boolean symbolName) {
      if (this.currencies != null) {
        ICUResourceBundle result = this.currencies.findWithFallback(isoCode);
        if (result != null) {
          if (!this.fallback) {
            int status = result.getLoadingStatus();
            if (status == 3 || status == 2)
              return null; 
          } 
          return result.getString(symbolName ? 0 : 1);
        } 
      } 
      return this.fallback ? isoCode : null;
    }
    
    public String getPluralName(String isoCode, String pluralKey) {
      if (this.plurals != null) {
        ICUResourceBundle pluralsBundle = this.plurals.findWithFallback(isoCode);
        if (pluralsBundle != null) {
          ICUResourceBundle pluralBundle = pluralsBundle.findWithFallback(pluralKey);
          if (pluralBundle == null) {
            if (!this.fallback)
              return null; 
            pluralBundle = pluralsBundle.findWithFallback("other");
            if (pluralBundle == null)
              return getName(isoCode); 
          } 
          return pluralBundle.getString();
        } 
      } 
      return this.fallback ? getName(isoCode) : null;
    }
    
    public Map<String, String> symbolMap() {
      Map<String, String> map = (this._symbolMapRef == null) ? null : this._symbolMapRef.get();
      if (map == null) {
        map = _createSymbolMap();
        this._symbolMapRef = new SoftReference<Map<String, String>>(map);
      } 
      return map;
    }
    
    public Map<String, String> nameMap() {
      Map<String, String> map = (this._nameMapRef == null) ? null : this._nameMapRef.get();
      if (map == null) {
        map = _createNameMap();
        this._nameMapRef = new SoftReference<Map<String, String>>(map);
      } 
      return map;
    }
    
    public Map<String, String> getUnitPatterns() {
      Map<String, String> result = new HashMap<String, String>();
      ULocale locale = this.rb.getULocale();
      for (; locale != null; locale = locale.getFallback()) {
        ICUResourceBundle r = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/curr", locale);
        if (r != null) {
          ICUResourceBundle cr = r.findWithFallback("CurrencyUnitPatterns");
          if (cr != null)
            for (int index = 0, size = cr.getSize(); index < size; index++) {
              ICUResourceBundle b = (ICUResourceBundle)cr.get(index);
              String key = b.getKey();
              if (!result.containsKey(key))
                result.put(key, b.getString()); 
            }  
        } 
      } 
      return Collections.unmodifiableMap(result);
    }
    
    public CurrencyData.CurrencyFormatInfo getFormatInfo(String isoCode) {
      ICUResourceBundle crb = this.currencies.findWithFallback(isoCode);
      if (crb != null && crb.getSize() > 2) {
        crb = crb.at(2);
        if (crb != null) {
          String pattern = crb.getString(0);
          char separator = crb.getString(1).charAt(0);
          char groupingSeparator = crb.getString(2).charAt(0);
          return new CurrencyData.CurrencyFormatInfo(pattern, separator, groupingSeparator);
        } 
      } 
      return null;
    }
    
    public CurrencyData.CurrencySpacingInfo getSpacingInfo() {
      ICUResourceBundle srb = this.rb.findWithFallback("currencySpacing");
      if (srb != null) {
        ICUResourceBundle brb = srb.findWithFallback("beforeCurrency");
        ICUResourceBundle arb = srb.findWithFallback("afterCurrency");
        if (arb != null && brb != null) {
          String beforeCurrencyMatch = brb.findWithFallback("currencyMatch").getString();
          String beforeContextMatch = brb.findWithFallback("surroundingMatch").getString();
          String beforeInsert = brb.findWithFallback("insertBetween").getString();
          String afterCurrencyMatch = arb.findWithFallback("currencyMatch").getString();
          String afterContextMatch = arb.findWithFallback("surroundingMatch").getString();
          String afterInsert = arb.findWithFallback("insertBetween").getString();
          return new CurrencyData.CurrencySpacingInfo(beforeCurrencyMatch, beforeContextMatch, beforeInsert, afterCurrencyMatch, afterContextMatch, afterInsert);
        } 
      } 
      return this.fallback ? CurrencyData.CurrencySpacingInfo.DEFAULT : null;
    }
    
    private Map<String, String> _createSymbolMap() {
      Map<String, String> result = new HashMap<String, String>();
      for (ULocale locale = this.rb.getULocale(); locale != null; locale = locale.getFallback()) {
        ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/curr", locale);
        ICUResourceBundle curr = bundle.findTopLevel("Currencies");
        if (curr != null)
          for (int i = 0; i < curr.getSize(); i++) {
            ICUResourceBundle item = curr.at(i);
            String isoCode = item.getKey();
            if (!result.containsKey(isoCode)) {
              result.put(isoCode, isoCode);
              String symbol = item.getString(0);
              result.put(symbol, isoCode);
            } 
          }  
      } 
      return Collections.unmodifiableMap(result);
    }
    
    private Map<String, String> _createNameMap() {
      Map<String, String> result = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
      Set<String> visited = new HashSet<String>();
      Map<String, Set<String>> visitedPlurals = new HashMap<String, Set<String>>();
      for (ULocale locale = this.rb.getULocale(); locale != null; locale = locale.getFallback()) {
        ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/curr", locale);
        ICUResourceBundle curr = bundle.findTopLevel("Currencies");
        if (curr != null)
          for (int i = 0; i < curr.getSize(); i++) {
            ICUResourceBundle item = curr.at(i);
            String isoCode = item.getKey();
            if (!visited.contains(isoCode)) {
              visited.add(isoCode);
              String name = item.getString(1);
              result.put(name, isoCode);
            } 
          }  
        ICUResourceBundle plurals = bundle.findTopLevel("CurrencyPlurals");
        if (plurals != null)
          for (int i = 0; i < plurals.getSize(); i++) {
            ICUResourceBundle item = plurals.at(i);
            String isoCode = item.getKey();
            Set<String> pluralSet = visitedPlurals.get(isoCode);
            if (pluralSet == null) {
              pluralSet = new HashSet<String>();
              visitedPlurals.put(isoCode, pluralSet);
            } 
            for (int j = 0; j < item.getSize(); j++) {
              ICUResourceBundle plural = item.at(j);
              String pluralType = plural.getKey();
              if (!pluralSet.contains(pluralType)) {
                String pluralName = plural.getString();
                result.put(pluralName, isoCode);
                pluralSet.add(pluralType);
              } 
            } 
          }  
      } 
      return Collections.unmodifiableMap(result);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\ICUCurrencyDisplayInfoProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */