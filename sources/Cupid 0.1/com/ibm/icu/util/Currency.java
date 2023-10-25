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
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

public class Currency extends MeasureUnit implements Serializable {
  private static final long serialVersionUID = -5839973855554750484L;
  
  private static final boolean DEBUG = ICUDebug.enabled("currency");
  
  private static ICUCache<ULocale, List<TextTrieMap<CurrencyStringInfo>>> CURRENCY_NAME_CACHE = (ICUCache<ULocale, List<TextTrieMap<CurrencyStringInfo>>>)new SimpleCache();
  
  private String isoCode;
  
  public static final int SYMBOL_NAME = 0;
  
  public static final int LONG_NAME = 1;
  
  public static final int PLURAL_LONG_NAME = 2;
  
  private static ServiceShim shim;
  
  private static final String EUR_STR = "EUR";
  
  static abstract class ServiceShim {
    abstract ULocale[] getAvailableULocales();
    
    abstract Locale[] getAvailableLocales();
    
    abstract Currency createInstance(ULocale param1ULocale);
    
    abstract Object registerInstance(Currency param1Currency, ULocale param1ULocale);
    
    abstract boolean unregister(Object param1Object);
  }
  
  private static ServiceShim getShim() {
    if (shim == null)
      try {
        Class<?> cls = Class.forName("com.ibm.icu.util.CurrencyServiceShim");
        shim = (ServiceShim)cls.newInstance();
      } catch (Exception e) {
        if (DEBUG)
          e.printStackTrace(); 
        throw new RuntimeException(e.getMessage());
      }  
    return shim;
  }
  
  public static Currency getInstance(Locale locale) {
    return getInstance(ULocale.forLocale(locale));
  }
  
  public static Currency getInstance(ULocale locale) {
    String currency = locale.getKeywordValue("currency");
    if (currency != null)
      return getInstance(currency); 
    if (shim == null)
      return createCurrency(locale); 
    return shim.createInstance(locale);
  }
  
  public static String[] getAvailableCurrencyCodes(ULocale loc, Date d) {
    CurrencyMetaInfo.CurrencyFilter filter = CurrencyMetaInfo.CurrencyFilter.onDate(d).withRegion(loc.getCountry());
    List<String> list = getTenderCurrencies(filter);
    if (list.isEmpty())
      return null; 
    return list.<String>toArray(new String[list.size()]);
  }
  
  public static Set<Currency> getAvailableCurrencies() {
    CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
    List<String> list = info.currencies(CurrencyMetaInfo.CurrencyFilter.all());
    HashSet<Currency> resultSet = new HashSet<Currency>(list.size());
    for (String code : list)
      resultSet.add(new Currency(code)); 
    return resultSet;
  }
  
  private static final ICUCache<ULocale, String> currencyCodeCache = (ICUCache<ULocale, String>)new SimpleCache();
  
  static Currency createCurrency(ULocale loc) {
    String variant = loc.getVariant();
    if ("EURO".equals(variant))
      return new Currency("EUR"); 
    String code = (String)currencyCodeCache.get(loc);
    if (code == null) {
      String country = loc.getCountry();
      CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
      List<String> list = info.currencies(CurrencyMetaInfo.CurrencyFilter.onRegion(country));
      if (list.size() > 0) {
        code = list.get(0);
        boolean isPreEuro = "PREEURO".equals(variant);
        if (isPreEuro && "EUR".equals(code)) {
          if (list.size() < 2)
            return null; 
          code = list.get(1);
        } 
      } else {
        return null;
      } 
      currencyCodeCache.put(loc, code);
    } 
    return new Currency(code);
  }
  
  public static Currency getInstance(String theISOCode) {
    if (theISOCode == null)
      throw new NullPointerException("The input currency code is null."); 
    if (!isAlpha3Code(theISOCode))
      throw new IllegalArgumentException("The input currency code is not 3-letter alphabetic code."); 
    return new Currency(theISOCode.toUpperCase(Locale.ENGLISH));
  }
  
  private static boolean isAlpha3Code(String code) {
    if (code.length() != 3)
      return false; 
    for (int i = 0; i < 3; i++) {
      char ch = code.charAt(i);
      if (ch < 'A' || (ch > 'Z' && ch < 'a') || ch > 'z')
        return false; 
    } 
    return true;
  }
  
  public static Object registerInstance(Currency currency, ULocale locale) {
    return getShim().registerInstance(currency, locale);
  }
  
  public static boolean unregister(Object registryKey) {
    if (registryKey == null)
      throw new IllegalArgumentException("registryKey must not be null"); 
    if (shim == null)
      return false; 
    return shim.unregister(registryKey);
  }
  
  public static Locale[] getAvailableLocales() {
    if (shim == null)
      return ICUResourceBundle.getAvailableLocales(); 
    return shim.getAvailableLocales();
  }
  
  public static ULocale[] getAvailableULocales() {
    if (shim == null)
      return ICUResourceBundle.getAvailableULocales(); 
    return shim.getAvailableULocales();
  }
  
  public static final String[] getKeywordValuesForLocale(String key, ULocale locale, boolean commonlyUsed) {
    if (!"currency".equals(key))
      return EMPTY_STRING_ARRAY; 
    if (!commonlyUsed)
      return getAllTenderCurrencies().<String>toArray(new String[0]); 
    String prefRegion = locale.getCountry();
    if (prefRegion.length() == 0) {
      if (UND.equals(locale))
        return EMPTY_STRING_ARRAY; 
      ULocale loc = ULocale.addLikelySubtags(locale);
      prefRegion = loc.getCountry();
    } 
    CurrencyMetaInfo.CurrencyFilter filter = CurrencyMetaInfo.CurrencyFilter.now().withRegion(prefRegion);
    List<String> result = getTenderCurrencies(filter);
    if (result.size() == 0)
      return EMPTY_STRING_ARRAY; 
    return result.<String>toArray(new String[result.size()]);
  }
  
  private static final ULocale UND = new ULocale("und");
  
  private static final String[] EMPTY_STRING_ARRAY = new String[0];
  
  public int hashCode() {
    return this.isoCode.hashCode();
  }
  
  public boolean equals(Object rhs) {
    if (rhs == null)
      return false; 
    if (rhs == this)
      return true; 
    try {
      Currency c = (Currency)rhs;
      return this.isoCode.equals(c.isoCode);
    } catch (ClassCastException e) {
      return false;
    } 
  }
  
  public String getCurrencyCode() {
    return this.isoCode;
  }
  
  public int getNumericCode() {
    int code = 0;
    try {
      UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "currencyNumericCodes", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
      UResourceBundle codeMap = bundle.get("codeMap");
      UResourceBundle numCode = codeMap.get(this.isoCode);
      code = numCode.getInt();
    } catch (MissingResourceException e) {}
    return code;
  }
  
  public String getSymbol() {
    return getSymbol(ULocale.getDefault(ULocale.Category.DISPLAY));
  }
  
  public String getSymbol(Locale loc) {
    return getSymbol(ULocale.forLocale(loc));
  }
  
  public String getSymbol(ULocale uloc) {
    return getName(uloc, 0, new boolean[1]);
  }
  
  public String getName(Locale locale, int nameStyle, boolean[] isChoiceFormat) {
    return getName(ULocale.forLocale(locale), nameStyle, isChoiceFormat);
  }
  
  public String getName(ULocale locale, int nameStyle, boolean[] isChoiceFormat) {
    if (nameStyle != 0 && nameStyle != 1)
      throw new IllegalArgumentException("bad name style: " + nameStyle); 
    if (isChoiceFormat != null)
      isChoiceFormat[0] = false; 
    CurrencyDisplayNames names = CurrencyDisplayNames.getInstance(locale);
    return (nameStyle == 0) ? names.getSymbol(this.isoCode) : names.getName(this.isoCode);
  }
  
  public String getName(Locale locale, int nameStyle, String pluralCount, boolean[] isChoiceFormat) {
    return getName(ULocale.forLocale(locale), nameStyle, pluralCount, isChoiceFormat);
  }
  
  public String getName(ULocale locale, int nameStyle, String pluralCount, boolean[] isChoiceFormat) {
    if (nameStyle != 2)
      return getName(locale, nameStyle, isChoiceFormat); 
    if (isChoiceFormat != null)
      isChoiceFormat[0] = false; 
    CurrencyDisplayNames names = CurrencyDisplayNames.getInstance(locale);
    return names.getPluralName(this.isoCode, pluralCount);
  }
  
  public String getDisplayName() {
    return getName(Locale.getDefault(), 1, (boolean[])null);
  }
  
  public String getDisplayName(Locale locale) {
    return getName(locale, 1, (boolean[])null);
  }
  
  public static String parse(ULocale locale, String text, int type, ParsePosition pos) {
    List<TextTrieMap<CurrencyStringInfo>> currencyTrieVec = (List<TextTrieMap<CurrencyStringInfo>>)CURRENCY_NAME_CACHE.get(locale);
    if (currencyTrieVec == null) {
      TextTrieMap<CurrencyStringInfo> textTrieMap1 = new TextTrieMap(true);
      TextTrieMap<CurrencyStringInfo> currencySymbolTrie = new TextTrieMap(false);
      currencyTrieVec = new ArrayList<TextTrieMap<CurrencyStringInfo>>();
      currencyTrieVec.add(currencySymbolTrie);
      currencyTrieVec.add(textTrieMap1);
      setupCurrencyTrieVec(locale, currencyTrieVec);
      CURRENCY_NAME_CACHE.put(locale, currencyTrieVec);
    } 
    int maxLength = 0;
    String isoResult = null;
    TextTrieMap<CurrencyStringInfo> currencyNameTrie = currencyTrieVec.get(1);
    CurrencyNameResultHandler handler = new CurrencyNameResultHandler();
    currencyNameTrie.find(text, pos.getIndex(), handler);
    List<CurrencyStringInfo> list = handler.getMatchedCurrencyNames();
    if (list != null && list.size() != 0)
      for (CurrencyStringInfo info : list) {
        String isoCode = info.getISOCode();
        String currencyString = info.getCurrencyString();
        if (currencyString.length() > maxLength) {
          maxLength = currencyString.length();
          isoResult = isoCode;
        } 
      }  
    if (type != 1) {
      TextTrieMap<CurrencyStringInfo> currencySymbolTrie = currencyTrieVec.get(0);
      handler = new CurrencyNameResultHandler();
      currencySymbolTrie.find(text, pos.getIndex(), handler);
      list = handler.getMatchedCurrencyNames();
      if (list != null && list.size() != 0)
        for (CurrencyStringInfo info : list) {
          String isoCode = info.getISOCode();
          String currencyString = info.getCurrencyString();
          if (currencyString.length() > maxLength) {
            maxLength = currencyString.length();
            isoResult = isoCode;
          } 
        }  
    } 
    int start = pos.getIndex();
    pos.setIndex(start + maxLength);
    return isoResult;
  }
  
  private static void setupCurrencyTrieVec(ULocale locale, List<TextTrieMap<CurrencyStringInfo>> trieVec) {
    TextTrieMap<CurrencyStringInfo> symTrie = trieVec.get(0);
    TextTrieMap<CurrencyStringInfo> trie = trieVec.get(1);
    CurrencyDisplayNames names = CurrencyDisplayNames.getInstance(locale);
    for (Map.Entry<String, String> e : (Iterable<Map.Entry<String, String>>)names.symbolMap().entrySet()) {
      String symbol = e.getKey();
      String isoCode = e.getValue();
      symTrie.put(symbol, new CurrencyStringInfo(isoCode, symbol));
    } 
    for (Map.Entry<String, String> e : (Iterable<Map.Entry<String, String>>)names.nameMap().entrySet()) {
      String name = e.getKey();
      String isoCode = e.getValue();
      trie.put(name, new CurrencyStringInfo(isoCode, name));
    } 
  }
  
  private static final class CurrencyStringInfo {
    private String isoCode;
    
    private String currencyString;
    
    public CurrencyStringInfo(String isoCode, String currencyString) {
      this.isoCode = isoCode;
      this.currencyString = currencyString;
    }
    
    private String getISOCode() {
      return this.isoCode;
    }
    
    private String getCurrencyString() {
      return this.currencyString;
    }
  }
  
  private static class CurrencyNameResultHandler implements TextTrieMap.ResultHandler<CurrencyStringInfo> {
    private ArrayList<Currency.CurrencyStringInfo> resultList;
    
    private CurrencyNameResultHandler() {}
    
    public boolean handlePrefixMatch(int matchLength, Iterator<Currency.CurrencyStringInfo> values) {
      if (this.resultList == null)
        this.resultList = new ArrayList<Currency.CurrencyStringInfo>(); 
      while (values.hasNext()) {
        Currency.CurrencyStringInfo item = values.next();
        if (item == null)
          break; 
        int i = 0;
        for (; i < this.resultList.size(); i++) {
          Currency.CurrencyStringInfo tmp = this.resultList.get(i);
          if (item.getISOCode().equals(tmp.getISOCode())) {
            if (matchLength > tmp.getCurrencyString().length())
              this.resultList.set(i, item); 
            break;
          } 
        } 
        if (i == this.resultList.size())
          this.resultList.add(item); 
      } 
      return true;
    }
    
    List<Currency.CurrencyStringInfo> getMatchedCurrencyNames() {
      if (this.resultList == null || this.resultList.size() == 0)
        return null; 
      return this.resultList;
    }
  }
  
  public int getDefaultFractionDigits() {
    CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
    CurrencyMetaInfo.CurrencyDigits digits = info.currencyDigits(this.isoCode);
    return digits.fractionDigits;
  }
  
  public double getRoundingIncrement() {
    CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
    CurrencyMetaInfo.CurrencyDigits digits = info.currencyDigits(this.isoCode);
    int data1 = digits.roundingIncrement;
    if (data1 == 0)
      return 0.0D; 
    int data0 = digits.fractionDigits;
    if (data0 < 0 || data0 >= POW10.length)
      return 0.0D; 
    return data1 / POW10[data0];
  }
  
  public String toString() {
    return this.isoCode;
  }
  
  protected Currency(String theISOCode) {
    this.isoCode = theISOCode;
  }
  
  private static final int[] POW10 = new int[] { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };
  
  private static SoftReference<List<String>> ALL_TENDER_CODES;
  
  private static SoftReference<Set<String>> ALL_CODES_AS_SET;
  
  private static synchronized List<String> getAllTenderCurrencies() {
    List<String> all = (ALL_TENDER_CODES == null) ? null : ALL_TENDER_CODES.get();
    if (all == null) {
      CurrencyMetaInfo.CurrencyFilter filter = CurrencyMetaInfo.CurrencyFilter.all();
      all = Collections.unmodifiableList(getTenderCurrencies(filter));
      ALL_TENDER_CODES = new SoftReference<List<String>>(all);
    } 
    return all;
  }
  
  private static synchronized Set<String> getAllCurrenciesAsSet() {
    Set<String> all = (ALL_CODES_AS_SET == null) ? null : ALL_CODES_AS_SET.get();
    if (all == null) {
      CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
      all = Collections.unmodifiableSet(new HashSet<String>(info.currencies(CurrencyMetaInfo.CurrencyFilter.all())));
      ALL_CODES_AS_SET = new SoftReference<Set<String>>(all);
    } 
    return all;
  }
  
  public static boolean isAvailable(String code, Date from, Date to) {
    if (!isAlpha3Code(code))
      return false; 
    if (from != null && to != null && from.after(to))
      throw new IllegalArgumentException("To is before from"); 
    code = code.toUpperCase(Locale.ENGLISH);
    boolean isKnown = getAllCurrenciesAsSet().contains(code);
    if (!isKnown)
      return false; 
    if (from == null && to == null)
      return true; 
    CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
    List<String> allActive = info.currencies(CurrencyMetaInfo.CurrencyFilter.onDateRange(from, to).withCurrency(code));
    return allActive.contains(code);
  }
  
  private static List<String> getTenderCurrencies(CurrencyMetaInfo.CurrencyFilter filter) {
    CurrencyMetaInfo info = CurrencyMetaInfo.getInstance();
    return info.currencies(filter.withTender());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\Currency.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */