package com.ibm.icu.impl;

import com.ibm.icu.text.PluralRules;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeMap;

public class PluralRulesLoader {
  private final Map<String, PluralRules> rulesIdToRules = new HashMap<String, PluralRules>();
  
  private Map<String, String> localeIdToCardinalRulesId;
  
  private Map<String, String> localeIdToOrdinalRulesId;
  
  private Map<String, ULocale> rulesIdToEquivalentULocale;
  
  public ULocale[] getAvailableULocales() {
    Set<String> keys = getLocaleIdToRulesIdMap(PluralRules.PluralType.CARDINAL).keySet();
    ULocale[] locales = new ULocale[keys.size()];
    int n = 0;
    for (Iterator<String> iter = keys.iterator(); iter.hasNext();)
      locales[n++] = ULocale.createCanonical(iter.next()); 
    return locales;
  }
  
  public ULocale getFunctionalEquivalent(ULocale locale, boolean[] isAvailable) {
    if (isAvailable != null && isAvailable.length > 0) {
      String localeId = ULocale.canonicalize(locale.getBaseName());
      Map<String, String> idMap = getLocaleIdToRulesIdMap(PluralRules.PluralType.CARDINAL);
      isAvailable[0] = idMap.containsKey(localeId);
    } 
    String rulesId = getRulesIdForLocale(locale, PluralRules.PluralType.CARDINAL);
    if (rulesId == null || rulesId.trim().length() == 0)
      return ULocale.ROOT; 
    ULocale result = getRulesIdToEquivalentULocaleMap().get(rulesId);
    if (result == null)
      return ULocale.ROOT; 
    return result;
  }
  
  private Map<String, String> getLocaleIdToRulesIdMap(PluralRules.PluralType type) {
    checkBuildRulesIdMaps();
    return (type == PluralRules.PluralType.CARDINAL) ? this.localeIdToCardinalRulesId : this.localeIdToOrdinalRulesId;
  }
  
  private Map<String, ULocale> getRulesIdToEquivalentULocaleMap() {
    checkBuildRulesIdMaps();
    return this.rulesIdToEquivalentULocale;
  }
  
  private void checkBuildRulesIdMaps() {
    boolean haveMap;
    synchronized (this) {
      haveMap = (this.localeIdToCardinalRulesId != null);
    } 
    if (!haveMap) {
      Map<?, ?> map1;
      Map<?, ?> map2;
      Map<?, ?> map3;
      try {
        UResourceBundle pluralb = getPluralBundle();
        UResourceBundle localeb = pluralb.get("locales");
        map1 = new TreeMap<String, String>();
        map3 = new HashMap<String, ULocale>();
        int i;
        for (i = 0; i < localeb.getSize(); i++) {
          UResourceBundle b = localeb.get(i);
          String id = b.getKey();
          String value = b.getString().intern();
          map1.put(id, value);
          if (!map3.containsKey(value))
            map3.put(value, new ULocale(id)); 
        } 
        localeb = pluralb.get("locales_ordinals");
        map2 = new TreeMap<String, String>();
        for (i = 0; i < localeb.getSize(); i++) {
          UResourceBundle b = localeb.get(i);
          String id = b.getKey();
          String value = b.getString().intern();
          map2.put(id, value);
        } 
      } catch (MissingResourceException e) {
        map1 = Collections.emptyMap();
        map2 = Collections.emptyMap();
        map3 = Collections.emptyMap();
      } 
      synchronized (this) {
        if (this.localeIdToCardinalRulesId == null) {
          this.localeIdToCardinalRulesId = (Map)map1;
          this.localeIdToOrdinalRulesId = (Map)map2;
          this.rulesIdToEquivalentULocale = (Map)map3;
        } 
      } 
    } 
  }
  
  public String getRulesIdForLocale(ULocale locale, PluralRules.PluralType type) {
    Map<String, String> idMap = getLocaleIdToRulesIdMap(type);
    String localeId = ULocale.canonicalize(locale.getBaseName());
    String rulesId = null;
    while (null == (rulesId = idMap.get(localeId))) {
      int ix = localeId.lastIndexOf("_");
      if (ix == -1)
        break; 
      localeId = localeId.substring(0, ix);
    } 
    return rulesId;
  }
  
  public PluralRules getRulesForRulesId(String rulesId) {
    boolean hasRules;
    PluralRules rules = null;
    synchronized (this.rulesIdToRules) {
      hasRules = this.rulesIdToRules.containsKey(rulesId);
      if (hasRules)
        rules = this.rulesIdToRules.get(rulesId); 
    } 
    if (!hasRules) {
      try {
        UResourceBundle pluralb = getPluralBundle();
        UResourceBundle rulesb = pluralb.get("rules");
        UResourceBundle setb = rulesb.get(rulesId);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < setb.getSize(); i++) {
          UResourceBundle b = setb.get(i);
          if (i > 0)
            sb.append("; "); 
          sb.append(b.getKey());
          sb.append(": ");
          sb.append(b.getString());
        } 
        rules = PluralRules.parseDescription(sb.toString());
      } catch (ParseException e) {
      
      } catch (MissingResourceException e) {}
      synchronized (this.rulesIdToRules) {
        if (this.rulesIdToRules.containsKey(rulesId)) {
          rules = this.rulesIdToRules.get(rulesId);
        } else {
          this.rulesIdToRules.put(rulesId, rules);
        } 
      } 
    } 
    return rules;
  }
  
  public UResourceBundle getPluralBundle() throws MissingResourceException {
    return ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "plurals", ICUResourceBundle.ICU_DATA_CLASS_LOADER, true);
  }
  
  public PluralRules forLocale(ULocale locale, PluralRules.PluralType type) {
    String rulesId = getRulesIdForLocale(locale, type);
    if (rulesId == null || rulesId.trim().length() == 0)
      return PluralRules.DEFAULT; 
    PluralRules rules = getRulesForRulesId(rulesId);
    if (rules == null)
      rules = PluralRules.DEFAULT; 
    return rules;
  }
  
  public static final PluralRulesLoader loader = new PluralRulesLoader();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\PluralRulesLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */