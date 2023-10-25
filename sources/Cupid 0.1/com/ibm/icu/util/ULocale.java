package com.ibm.icu.util;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUResourceTableAccess;
import com.ibm.icu.impl.LocaleIDParser;
import com.ibm.icu.impl.LocaleIDs;
import com.ibm.icu.impl.LocaleUtility;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.locale.AsciiUtil;
import com.ibm.icu.impl.locale.BaseLocale;
import com.ibm.icu.impl.locale.Extension;
import com.ibm.icu.impl.locale.InternalLocaleBuilder;
import com.ibm.icu.impl.locale.LanguageTag;
import com.ibm.icu.impl.locale.LocaleExtensions;
import com.ibm.icu.impl.locale.LocaleSyntaxException;
import com.ibm.icu.impl.locale.ParseStatus;
import com.ibm.icu.impl.locale.UnicodeLocaleExtension;
import com.ibm.icu.text.LocaleDisplayNames;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public final class ULocale implements Serializable {
  private static final long serialVersionUID = 3715177670352309217L;
  
  public static final ULocale ENGLISH = new ULocale("en", Locale.ENGLISH);
  
  public static final ULocale FRENCH = new ULocale("fr", Locale.FRENCH);
  
  public static final ULocale GERMAN = new ULocale("de", Locale.GERMAN);
  
  public static final ULocale ITALIAN = new ULocale("it", Locale.ITALIAN);
  
  public static final ULocale JAPANESE = new ULocale("ja", Locale.JAPANESE);
  
  public static final ULocale KOREAN = new ULocale("ko", Locale.KOREAN);
  
  public static final ULocale CHINESE = new ULocale("zh", Locale.CHINESE);
  
  public static final ULocale SIMPLIFIED_CHINESE = new ULocale("zh_Hans", Locale.CHINESE);
  
  public static final ULocale TRADITIONAL_CHINESE = new ULocale("zh_Hant", Locale.CHINESE);
  
  public static final ULocale FRANCE = new ULocale("fr_FR", Locale.FRANCE);
  
  public static final ULocale GERMANY = new ULocale("de_DE", Locale.GERMANY);
  
  public static final ULocale ITALY = new ULocale("it_IT", Locale.ITALY);
  
  public static final ULocale JAPAN = new ULocale("ja_JP", Locale.JAPAN);
  
  public static final ULocale KOREA = new ULocale("ko_KR", Locale.KOREA);
  
  public static final ULocale CHINA = new ULocale("zh_Hans_CN", Locale.CHINA);
  
  public static final ULocale PRC = CHINA;
  
  public static final ULocale TAIWAN = new ULocale("zh_Hant_TW", Locale.TAIWAN);
  
  public static final ULocale UK = new ULocale("en_GB", Locale.UK);
  
  public static final ULocale US = new ULocale("en_US", Locale.US);
  
  public static final ULocale CANADA = new ULocale("en_CA", Locale.CANADA);
  
  public static final ULocale CANADA_FRENCH = new ULocale("fr_CA", Locale.CANADA_FRENCH);
  
  private static final String EMPTY_STRING = "";
  
  private static final char UNDERSCORE = '_';
  
  private static final Locale EMPTY_LOCALE = new Locale("", "");
  
  private static final String LOCALE_ATTRIBUTE_KEY = "attribute";
  
  public static final ULocale ROOT = new ULocale("", EMPTY_LOCALE);
  
  public enum Category {
    DISPLAY, FORMAT;
  }
  
  private static final SimpleCache<Locale, ULocale> CACHE = new SimpleCache();
  
  private volatile transient Locale locale;
  
  private String localeID;
  
  private volatile transient BaseLocale baseLocale;
  
  private volatile transient LocaleExtensions extensions;
  
  private static String[][] CANONICALIZE_MAP;
  
  private static String[][] variantsToKeywords;
  
  private static void initCANONICALIZE_MAP() {
    if (CANONICALIZE_MAP == null) {
      String[][] tempCANONICALIZE_MAP = { 
          { "C", "en_US_POSIX", null, null }, { "art_LOJBAN", "jbo", null, null }, { "az_AZ_CYRL", "az_Cyrl_AZ", null, null }, { "az_AZ_LATN", "az_Latn_AZ", null, null }, { "ca_ES_PREEURO", "ca_ES", "currency", "ESP" }, { "cel_GAULISH", "cel__GAULISH", null, null }, { "de_1901", "de__1901", null, null }, { "de_1906", "de__1906", null, null }, { "de__PHONEBOOK", "de", "collation", "phonebook" }, { "de_AT_PREEURO", "de_AT", "currency", "ATS" }, 
          { "de_DE_PREEURO", "de_DE", "currency", "DEM" }, { "de_LU_PREEURO", "de_LU", "currency", "EUR" }, { "el_GR_PREEURO", "el_GR", "currency", "GRD" }, { "en_BOONT", "en__BOONT", null, null }, { "en_SCOUSE", "en__SCOUSE", null, null }, { "en_BE_PREEURO", "en_BE", "currency", "BEF" }, { "en_IE_PREEURO", "en_IE", "currency", "IEP" }, { "es__TRADITIONAL", "es", "collation", "traditional" }, { "es_ES_PREEURO", "es_ES", "currency", "ESP" }, { "eu_ES_PREEURO", "eu_ES", "currency", "ESP" }, 
          { "fi_FI_PREEURO", "fi_FI", "currency", "FIM" }, { "fr_BE_PREEURO", "fr_BE", "currency", "BEF" }, { "fr_FR_PREEURO", "fr_FR", "currency", "FRF" }, { "fr_LU_PREEURO", "fr_LU", "currency", "LUF" }, { "ga_IE_PREEURO", "ga_IE", "currency", "IEP" }, { "gl_ES_PREEURO", "gl_ES", "currency", "ESP" }, { "hi__DIRECT", "hi", "collation", "direct" }, { "it_IT_PREEURO", "it_IT", "currency", "ITL" }, { "ja_JP_TRADITIONAL", "ja_JP", "calendar", "japanese" }, { "nl_BE_PREEURO", "nl_BE", "currency", "BEF" }, 
          { "nl_NL_PREEURO", "nl_NL", "currency", "NLG" }, { "pt_PT_PREEURO", "pt_PT", "currency", "PTE" }, { "sl_ROZAJ", "sl__ROZAJ", null, null }, { "sr_SP_CYRL", "sr_Cyrl_RS", null, null }, { "sr_SP_LATN", "sr_Latn_RS", null, null }, { "sr_YU_CYRILLIC", "sr_Cyrl_RS", null, null }, { "th_TH_TRADITIONAL", "th_TH", "calendar", "buddhist" }, { "uz_UZ_CYRILLIC", "uz_Cyrl_UZ", null, null }, { "uz_UZ_CYRL", "uz_Cyrl_UZ", null, null }, { "uz_UZ_LATN", "uz_Latn_UZ", null, null }, 
          { "zh_CHS", "zh_Hans", null, null }, { "zh_CHT", "zh_Hant", null, null }, { "zh_GAN", "zh__GAN", null, null }, { "zh_GUOYU", "zh", null, null }, { "zh_HAKKA", "zh__HAKKA", null, null }, { "zh_MIN", "zh__MIN", null, null }, { "zh_MIN_NAN", "zh__MINNAN", null, null }, { "zh_WUU", "zh__WUU", null, null }, { "zh_XIANG", "zh__XIANG", null, null }, { "zh_YUE", "zh__YUE", null, null } };
      synchronized (ULocale.class) {
        if (CANONICALIZE_MAP == null)
          CANONICALIZE_MAP = tempCANONICALIZE_MAP; 
      } 
    } 
    if (variantsToKeywords == null) {
      String[][] tempVariantsToKeywords = { { "EURO", "currency", "EUR" }, { "PINYIN", "collation", "pinyin" }, { "STROKE", "collation", "stroke" } };
      synchronized (ULocale.class) {
        if (variantsToKeywords == null)
          variantsToKeywords = tempVariantsToKeywords; 
      } 
    } 
  }
  
  private ULocale(String localeID, Locale locale) {
    this.localeID = localeID;
    this.locale = locale;
  }
  
  private ULocale(Locale loc) {
    this.localeID = getName(forLocale(loc).toString());
    this.locale = loc;
  }
  
  public static ULocale forLocale(Locale loc) {
    if (loc == null)
      return null; 
    ULocale result = (ULocale)CACHE.get(loc);
    if (result == null) {
      result = JDKLocaleHelper.toULocale(loc);
      CACHE.put(loc, result);
    } 
    return result;
  }
  
  public ULocale(String localeID) {
    this.localeID = getName(localeID);
  }
  
  public ULocale(String a, String b) {
    this(a, b, (String)null);
  }
  
  public ULocale(String a, String b, String c) {
    this.localeID = getName(lscvToID(a, b, c, ""));
  }
  
  public static ULocale createCanonical(String nonCanonicalID) {
    return new ULocale(canonicalize(nonCanonicalID), (Locale)null);
  }
  
  private static String lscvToID(String lang, String script, String country, String variant) {
    StringBuilder buf = new StringBuilder();
    if (lang != null && lang.length() > 0)
      buf.append(lang); 
    if (script != null && script.length() > 0) {
      buf.append('_');
      buf.append(script);
    } 
    if (country != null && country.length() > 0) {
      buf.append('_');
      buf.append(country);
    } 
    if (variant != null && variant.length() > 0) {
      if (country == null || country.length() == 0)
        buf.append('_'); 
      buf.append('_');
      buf.append(variant);
    } 
    return buf.toString();
  }
  
  public Locale toLocale() {
    if (this.locale == null)
      this.locale = JDKLocaleHelper.toLocale(this); 
    return this.locale;
  }
  
  private static ICUCache<String, String> nameCache = (ICUCache<String, String>)new SimpleCache();
  
  private static Locale defaultLocale = Locale.getDefault();
  
  private static ULocale defaultULocale;
  
  private static Locale[] defaultCategoryLocales = new Locale[(Category.values()).length];
  
  private static ULocale[] defaultCategoryULocales = new ULocale[(Category.values()).length];
  
  public static Type ACTUAL_LOCALE;
  
  public static Type VALID_LOCALE;
  
  private static final String UNDEFINED_LANGUAGE = "und";
  
  private static final String UNDEFINED_SCRIPT = "Zzzz";
  
  private static final String UNDEFINED_REGION = "ZZ";
  
  public static final char PRIVATE_USE_EXTENSION = 'x';
  
  public static final char UNICODE_LOCALE_EXTENSION = 'u';
  
  static {
    defaultULocale = forLocale(defaultLocale);
    if (JDKLocaleHelper.isJava7orNewer()) {
      for (Category cat : Category.values()) {
        int idx = cat.ordinal();
        defaultCategoryLocales[idx] = JDKLocaleHelper.getDefault(cat);
        defaultCategoryULocales[idx] = forLocale(defaultCategoryLocales[idx]);
      } 
    } else {
      if (JDKLocaleHelper.isOriginalDefaultLocale(defaultLocale)) {
        String userScript = JDKLocaleHelper.getSystemProperty("user.script");
        if (userScript != null && LanguageTag.isScript(userScript)) {
          BaseLocale base = defaultULocale.base();
          BaseLocale newBase = BaseLocale.getInstance(base.getLanguage(), userScript, base.getRegion(), base.getVariant());
          defaultULocale = getInstance(newBase, defaultULocale.extensions());
        } 
      } 
      for (Category cat : Category.values()) {
        int idx = cat.ordinal();
        defaultCategoryLocales[idx] = defaultLocale;
        defaultCategoryULocales[idx] = defaultULocale;
      } 
    } 
    ACTUAL_LOCALE = new Type();
    VALID_LOCALE = new Type();
  }
  
  public static ULocale getDefault() {
    synchronized (ULocale.class) {
      if (defaultULocale == null)
        return ROOT; 
      Locale currentDefault = Locale.getDefault();
      if (!defaultLocale.equals(currentDefault)) {
        defaultLocale = currentDefault;
        defaultULocale = forLocale(currentDefault);
        if (!JDKLocaleHelper.isJava7orNewer())
          for (Category cat : Category.values()) {
            int idx = cat.ordinal();
            defaultCategoryLocales[idx] = currentDefault;
            defaultCategoryULocales[idx] = forLocale(currentDefault);
          }  
      } 
      return defaultULocale;
    } 
  }
  
  public static synchronized void setDefault(ULocale newLocale) {
    defaultLocale = newLocale.toLocale();
    Locale.setDefault(defaultLocale);
    defaultULocale = newLocale;
    for (Category cat : Category.values())
      setDefault(cat, newLocale); 
  }
  
  public static ULocale getDefault(Category category) {
    synchronized (ULocale.class) {
      int idx = category.ordinal();
      if (defaultCategoryULocales[idx] == null)
        return ROOT; 
      if (JDKLocaleHelper.isJava7orNewer()) {
        Locale currentCategoryDefault = JDKLocaleHelper.getDefault(category);
        if (!defaultCategoryLocales[idx].equals(currentCategoryDefault)) {
          defaultCategoryLocales[idx] = currentCategoryDefault;
          defaultCategoryULocales[idx] = forLocale(currentCategoryDefault);
        } 
      } else {
        Locale currentDefault = Locale.getDefault();
        if (!defaultLocale.equals(currentDefault)) {
          defaultLocale = currentDefault;
          defaultULocale = forLocale(currentDefault);
          for (Category cat : Category.values()) {
            int tmpIdx = cat.ordinal();
            defaultCategoryLocales[tmpIdx] = currentDefault;
            defaultCategoryULocales[tmpIdx] = forLocale(currentDefault);
          } 
        } 
      } 
      return defaultCategoryULocales[idx];
    } 
  }
  
  public static synchronized void setDefault(Category category, ULocale newLocale) {
    Locale newJavaDefault = newLocale.toLocale();
    int idx = category.ordinal();
    defaultCategoryULocales[idx] = newLocale;
    defaultCategoryLocales[idx] = newJavaDefault;
    JDKLocaleHelper.setDefault(category, newJavaDefault);
  }
  
  public Object clone() {
    return this;
  }
  
  public int hashCode() {
    return this.localeID.hashCode();
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj instanceof ULocale)
      return this.localeID.equals(((ULocale)obj).localeID); 
    return false;
  }
  
  public static ULocale[] getAvailableLocales() {
    return ICUResourceBundle.getAvailableULocales();
  }
  
  public static String[] getISOCountries() {
    return LocaleIDs.getISOCountries();
  }
  
  public static String[] getISOLanguages() {
    return LocaleIDs.getISOLanguages();
  }
  
  public String getLanguage() {
    return getLanguage(this.localeID);
  }
  
  public static String getLanguage(String localeID) {
    return (new LocaleIDParser(localeID)).getLanguage();
  }
  
  public String getScript() {
    return getScript(this.localeID);
  }
  
  public static String getScript(String localeID) {
    return (new LocaleIDParser(localeID)).getScript();
  }
  
  public String getCountry() {
    return getCountry(this.localeID);
  }
  
  public static String getCountry(String localeID) {
    return (new LocaleIDParser(localeID)).getCountry();
  }
  
  public String getVariant() {
    return getVariant(this.localeID);
  }
  
  public static String getVariant(String localeID) {
    return (new LocaleIDParser(localeID)).getVariant();
  }
  
  public static String getFallback(String localeID) {
    return getFallbackString(getName(localeID));
  }
  
  public ULocale getFallback() {
    if (this.localeID.length() == 0 || this.localeID.charAt(0) == '@')
      return null; 
    return new ULocale(getFallbackString(this.localeID), (Locale)null);
  }
  
  private static String getFallbackString(String fallback) {
    int extStart = fallback.indexOf('@');
    if (extStart == -1)
      extStart = fallback.length(); 
    int last = fallback.lastIndexOf('_', extStart);
    if (last == -1) {
      last = 0;
    } else {
      while (last > 0 && fallback.charAt(last - 1) == '_')
        last--; 
    } 
    return fallback.substring(0, last) + fallback.substring(extStart);
  }
  
  public String getBaseName() {
    return getBaseName(this.localeID);
  }
  
  public static String getBaseName(String localeID) {
    if (localeID.indexOf('@') == -1)
      return localeID; 
    return (new LocaleIDParser(localeID)).getBaseName();
  }
  
  public String getName() {
    return this.localeID;
  }
  
  private static int getShortestSubtagLength(String localeID) {
    int localeIDLength = localeID.length();
    int length = localeIDLength;
    boolean reset = true;
    int tmpLength = 0;
    for (int i = 0; i < localeIDLength; i++) {
      if (localeID.charAt(i) != '_' && localeID.charAt(i) != '-') {
        if (reset) {
          reset = false;
          tmpLength = 0;
        } 
        tmpLength++;
      } else {
        if (tmpLength != 0 && tmpLength < length)
          length = tmpLength; 
        reset = true;
      } 
    } 
    return length;
  }
  
  public static String getName(String localeID) {
    String tmpLocaleID;
    if (localeID != null && !localeID.contains("@") && getShortestSubtagLength(localeID) == 1) {
      tmpLocaleID = forLanguageTag(localeID).getName();
      if (tmpLocaleID.length() == 0)
        tmpLocaleID = localeID; 
    } else {
      tmpLocaleID = localeID;
    } 
    String name = (String)nameCache.get(tmpLocaleID);
    if (name == null) {
      name = (new LocaleIDParser(tmpLocaleID)).getName();
      nameCache.put(tmpLocaleID, name);
    } 
    return name;
  }
  
  public String toString() {
    return this.localeID;
  }
  
  public Iterator<String> getKeywords() {
    return getKeywords(this.localeID);
  }
  
  public static Iterator<String> getKeywords(String localeID) {
    return (new LocaleIDParser(localeID)).getKeywords();
  }
  
  public String getKeywordValue(String keywordName) {
    return getKeywordValue(this.localeID, keywordName);
  }
  
  public static String getKeywordValue(String localeID, String keywordName) {
    return (new LocaleIDParser(localeID)).getKeywordValue(keywordName);
  }
  
  public static String canonicalize(String localeID) {
    LocaleIDParser parser = new LocaleIDParser(localeID, true);
    String baseName = parser.getBaseName();
    boolean foundVariant = false;
    if (localeID.equals(""))
      return ""; 
    initCANONICALIZE_MAP();
    int i;
    for (i = 0; i < variantsToKeywords.length; i++) {
      String[] vals = variantsToKeywords[i];
      int idx = baseName.lastIndexOf("_" + vals[0]);
      if (idx > -1) {
        foundVariant = true;
        baseName = baseName.substring(0, idx);
        if (baseName.endsWith("_"))
          baseName = baseName.substring(0, --idx); 
        parser.setBaseName(baseName);
        parser.defaultKeywordValue(vals[1], vals[2]);
        break;
      } 
    } 
    for (i = 0; i < CANONICALIZE_MAP.length; i++) {
      if (CANONICALIZE_MAP[i][0].equals(baseName)) {
        foundVariant = true;
        String[] vals = CANONICALIZE_MAP[i];
        parser.setBaseName(vals[1]);
        if (vals[2] != null)
          parser.defaultKeywordValue(vals[2], vals[3]); 
        break;
      } 
    } 
    if (!foundVariant && parser.getLanguage().equals("nb") && parser.getVariant().equals("NY"))
      parser.setBaseName(lscvToID("nn", parser.getScript(), parser.getCountry(), null)); 
    return parser.getName();
  }
  
  public ULocale setKeywordValue(String keyword, String value) {
    return new ULocale(setKeywordValue(this.localeID, keyword, value), (Locale)null);
  }
  
  public static String setKeywordValue(String localeID, String keyword, String value) {
    LocaleIDParser parser = new LocaleIDParser(localeID);
    parser.setKeywordValue(keyword, value);
    return parser.getName();
  }
  
  public String getISO3Language() {
    return getISO3Language(this.localeID);
  }
  
  public static String getISO3Language(String localeID) {
    return LocaleIDs.getISO3Language(getLanguage(localeID));
  }
  
  public String getISO3Country() {
    return getISO3Country(this.localeID);
  }
  
  public static String getISO3Country(String localeID) {
    return LocaleIDs.getISO3Country(getCountry(localeID));
  }
  
  public String getDisplayLanguage() {
    return getDisplayLanguageInternal(this, getDefault(Category.DISPLAY), false);
  }
  
  public String getDisplayLanguage(ULocale displayLocale) {
    return getDisplayLanguageInternal(this, displayLocale, false);
  }
  
  public static String getDisplayLanguage(String localeID, String displayLocaleID) {
    return getDisplayLanguageInternal(new ULocale(localeID), new ULocale(displayLocaleID), false);
  }
  
  public static String getDisplayLanguage(String localeID, ULocale displayLocale) {
    return getDisplayLanguageInternal(new ULocale(localeID), displayLocale, false);
  }
  
  public String getDisplayLanguageWithDialect() {
    return getDisplayLanguageInternal(this, getDefault(Category.DISPLAY), true);
  }
  
  public String getDisplayLanguageWithDialect(ULocale displayLocale) {
    return getDisplayLanguageInternal(this, displayLocale, true);
  }
  
  public static String getDisplayLanguageWithDialect(String localeID, String displayLocaleID) {
    return getDisplayLanguageInternal(new ULocale(localeID), new ULocale(displayLocaleID), true);
  }
  
  public static String getDisplayLanguageWithDialect(String localeID, ULocale displayLocale) {
    return getDisplayLanguageInternal(new ULocale(localeID), displayLocale, true);
  }
  
  private static String getDisplayLanguageInternal(ULocale locale, ULocale displayLocale, boolean useDialect) {
    String lang = useDialect ? locale.getBaseName() : locale.getLanguage();
    return LocaleDisplayNames.getInstance(displayLocale).languageDisplayName(lang);
  }
  
  public String getDisplayScript() {
    return getDisplayScriptInternal(this, getDefault(Category.DISPLAY));
  }
  
  public String getDisplayScriptInContext() {
    return getDisplayScriptInContextInternal(this, getDefault(Category.DISPLAY));
  }
  
  public String getDisplayScript(ULocale displayLocale) {
    return getDisplayScriptInternal(this, displayLocale);
  }
  
  public String getDisplayScriptInContext(ULocale displayLocale) {
    return getDisplayScriptInContextInternal(this, displayLocale);
  }
  
  public static String getDisplayScript(String localeID, String displayLocaleID) {
    return getDisplayScriptInternal(new ULocale(localeID), new ULocale(displayLocaleID));
  }
  
  public static String getDisplayScriptInContext(String localeID, String displayLocaleID) {
    return getDisplayScriptInContextInternal(new ULocale(localeID), new ULocale(displayLocaleID));
  }
  
  public static String getDisplayScript(String localeID, ULocale displayLocale) {
    return getDisplayScriptInternal(new ULocale(localeID), displayLocale);
  }
  
  public static String getDisplayScriptInContext(String localeID, ULocale displayLocale) {
    return getDisplayScriptInContextInternal(new ULocale(localeID), displayLocale);
  }
  
  private static String getDisplayScriptInternal(ULocale locale, ULocale displayLocale) {
    return LocaleDisplayNames.getInstance(displayLocale).scriptDisplayName(locale.getScript());
  }
  
  private static String getDisplayScriptInContextInternal(ULocale locale, ULocale displayLocale) {
    return LocaleDisplayNames.getInstance(displayLocale).scriptDisplayNameInContext(locale.getScript());
  }
  
  public String getDisplayCountry() {
    return getDisplayCountryInternal(this, getDefault(Category.DISPLAY));
  }
  
  public String getDisplayCountry(ULocale displayLocale) {
    return getDisplayCountryInternal(this, displayLocale);
  }
  
  public static String getDisplayCountry(String localeID, String displayLocaleID) {
    return getDisplayCountryInternal(new ULocale(localeID), new ULocale(displayLocaleID));
  }
  
  public static String getDisplayCountry(String localeID, ULocale displayLocale) {
    return getDisplayCountryInternal(new ULocale(localeID), displayLocale);
  }
  
  private static String getDisplayCountryInternal(ULocale locale, ULocale displayLocale) {
    return LocaleDisplayNames.getInstance(displayLocale).regionDisplayName(locale.getCountry());
  }
  
  public String getDisplayVariant() {
    return getDisplayVariantInternal(this, getDefault(Category.DISPLAY));
  }
  
  public String getDisplayVariant(ULocale displayLocale) {
    return getDisplayVariantInternal(this, displayLocale);
  }
  
  public static String getDisplayVariant(String localeID, String displayLocaleID) {
    return getDisplayVariantInternal(new ULocale(localeID), new ULocale(displayLocaleID));
  }
  
  public static String getDisplayVariant(String localeID, ULocale displayLocale) {
    return getDisplayVariantInternal(new ULocale(localeID), displayLocale);
  }
  
  private static String getDisplayVariantInternal(ULocale locale, ULocale displayLocale) {
    return LocaleDisplayNames.getInstance(displayLocale).variantDisplayName(locale.getVariant());
  }
  
  public static String getDisplayKeyword(String keyword) {
    return getDisplayKeywordInternal(keyword, getDefault(Category.DISPLAY));
  }
  
  public static String getDisplayKeyword(String keyword, String displayLocaleID) {
    return getDisplayKeywordInternal(keyword, new ULocale(displayLocaleID));
  }
  
  public static String getDisplayKeyword(String keyword, ULocale displayLocale) {
    return getDisplayKeywordInternal(keyword, displayLocale);
  }
  
  private static String getDisplayKeywordInternal(String keyword, ULocale displayLocale) {
    return LocaleDisplayNames.getInstance(displayLocale).keyDisplayName(keyword);
  }
  
  public String getDisplayKeywordValue(String keyword) {
    return getDisplayKeywordValueInternal(this, keyword, getDefault(Category.DISPLAY));
  }
  
  public String getDisplayKeywordValue(String keyword, ULocale displayLocale) {
    return getDisplayKeywordValueInternal(this, keyword, displayLocale);
  }
  
  public static String getDisplayKeywordValue(String localeID, String keyword, String displayLocaleID) {
    return getDisplayKeywordValueInternal(new ULocale(localeID), keyword, new ULocale(displayLocaleID));
  }
  
  public static String getDisplayKeywordValue(String localeID, String keyword, ULocale displayLocale) {
    return getDisplayKeywordValueInternal(new ULocale(localeID), keyword, displayLocale);
  }
  
  private static String getDisplayKeywordValueInternal(ULocale locale, String keyword, ULocale displayLocale) {
    keyword = AsciiUtil.toLowerString(keyword.trim());
    String value = locale.getKeywordValue(keyword);
    return LocaleDisplayNames.getInstance(displayLocale).keyValueDisplayName(keyword, value);
  }
  
  public String getDisplayName() {
    return getDisplayNameInternal(this, getDefault(Category.DISPLAY));
  }
  
  public String getDisplayName(ULocale displayLocale) {
    return getDisplayNameInternal(this, displayLocale);
  }
  
  public static String getDisplayName(String localeID, String displayLocaleID) {
    return getDisplayNameInternal(new ULocale(localeID), new ULocale(displayLocaleID));
  }
  
  public static String getDisplayName(String localeID, ULocale displayLocale) {
    return getDisplayNameInternal(new ULocale(localeID), displayLocale);
  }
  
  private static String getDisplayNameInternal(ULocale locale, ULocale displayLocale) {
    return LocaleDisplayNames.getInstance(displayLocale).localeDisplayName(locale);
  }
  
  public String getDisplayNameWithDialect() {
    return getDisplayNameWithDialectInternal(this, getDefault(Category.DISPLAY));
  }
  
  public String getDisplayNameWithDialect(ULocale displayLocale) {
    return getDisplayNameWithDialectInternal(this, displayLocale);
  }
  
  public static String getDisplayNameWithDialect(String localeID, String displayLocaleID) {
    return getDisplayNameWithDialectInternal(new ULocale(localeID), new ULocale(displayLocaleID));
  }
  
  public static String getDisplayNameWithDialect(String localeID, ULocale displayLocale) {
    return getDisplayNameWithDialectInternal(new ULocale(localeID), displayLocale);
  }
  
  private static String getDisplayNameWithDialectInternal(ULocale locale, ULocale displayLocale) {
    return LocaleDisplayNames.getInstance(displayLocale, LocaleDisplayNames.DialectHandling.DIALECT_NAMES).localeDisplayName(locale);
  }
  
  public String getCharacterOrientation() {
    return ICUResourceTableAccess.getTableString("com/ibm/icu/impl/data/icudt51b", this, "layout", "characters");
  }
  
  public String getLineOrientation() {
    return ICUResourceTableAccess.getTableString("com/ibm/icu/impl/data/icudt51b", this, "layout", "lines");
  }
  
  public static final class Type {
    private Type() {}
  }
  
  public static ULocale acceptLanguage(String acceptLanguageList, ULocale[] availableLocales, boolean[] fallback) {
    if (acceptLanguageList == null)
      throw new NullPointerException(); 
    ULocale[] acceptList = null;
    try {
      acceptList = parseAcceptLanguage(acceptLanguageList, true);
    } catch (ParseException pe) {
      acceptList = null;
    } 
    if (acceptList == null)
      return null; 
    return acceptLanguage(acceptList, availableLocales, fallback);
  }
  
  public static ULocale acceptLanguage(ULocale[] acceptLanguageList, ULocale[] availableLocales, boolean[] fallback) {
    if (fallback != null)
      fallback[0] = true; 
    for (int i = 0; i < acceptLanguageList.length; ) {
      ULocale aLocale = acceptLanguageList[i];
      boolean[] setFallback = fallback;
      while (true) {
        for (int j = 0; j < availableLocales.length; j++) {
          if (availableLocales[j].equals(aLocale)) {
            if (setFallback != null)
              setFallback[0] = false; 
            return availableLocales[j];
          } 
          if (aLocale.getScript().length() == 0 && availableLocales[j].getScript().length() > 0 && availableLocales[j].getLanguage().equals(aLocale.getLanguage()) && availableLocales[j].getCountry().equals(aLocale.getCountry()) && availableLocales[j].getVariant().equals(aLocale.getVariant())) {
            ULocale minAvail = minimizeSubtags(availableLocales[j]);
            if (minAvail.getScript().length() == 0) {
              if (setFallback != null)
                setFallback[0] = false; 
              return aLocale;
            } 
          } 
        } 
        Locale loc = aLocale.toLocale();
        Locale parent = LocaleUtility.fallback(loc);
        if (parent != null) {
          aLocale = new ULocale(parent);
        } else {
          aLocale = null;
        } 
        setFallback = null;
        if (aLocale == null)
          i++; 
      } 
    } 
    return null;
  }
  
  public static ULocale acceptLanguage(String acceptLanguageList, boolean[] fallback) {
    return acceptLanguage(acceptLanguageList, getAvailableLocales(), fallback);
  }
  
  public static ULocale acceptLanguage(ULocale[] acceptLanguageList, boolean[] fallback) {
    return acceptLanguage(acceptLanguageList, getAvailableLocales(), fallback);
  }
  
  static ULocale[] parseAcceptLanguage(String acceptLanguage, boolean isLenient) throws ParseException {
    class ULocaleAcceptLanguageQ implements Comparable<ULocaleAcceptLanguageQ> {
      private double q;
      
      private double serial;
      
      public ULocaleAcceptLanguageQ(double theq, int theserial) {
        this.q = theq;
        this.serial = theserial;
      }
      
      public int compareTo(ULocaleAcceptLanguageQ other) {
        if (this.q > other.q)
          return -1; 
        if (this.q < other.q)
          return 1; 
        if (this.serial < other.serial)
          return -1; 
        if (this.serial > other.serial)
          return 1; 
        return 0;
      }
    };
    TreeMap<ULocaleAcceptLanguageQ, ULocale> map = new TreeMap<ULocaleAcceptLanguageQ, ULocale>();
    StringBuilder languageRangeBuf = new StringBuilder();
    StringBuilder qvalBuf = new StringBuilder();
    int state = 0;
    acceptLanguage = acceptLanguage + ",";
    boolean subTag = false;
    boolean q1 = false;
    int n;
    for (n = 0; n < acceptLanguage.length(); n++) {
      boolean gotLanguageQ = false;
      char c = acceptLanguage.charAt(n);
      switch (state) {
        case 0:
          if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z')) {
            languageRangeBuf.append(c);
            state = 1;
            subTag = false;
            break;
          } 
          if (c == '*') {
            languageRangeBuf.append(c);
            state = 2;
            break;
          } 
          if (c != ' ' && c != '\t')
            state = -1; 
          break;
        case 1:
          if (('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z')) {
            languageRangeBuf.append(c);
            break;
          } 
          if (c == '-') {
            subTag = true;
            languageRangeBuf.append(c);
            break;
          } 
          if (c == '_') {
            if (isLenient) {
              subTag = true;
              languageRangeBuf.append(c);
              break;
            } 
            state = -1;
            break;
          } 
          if ('0' <= c && c <= '9') {
            if (subTag) {
              languageRangeBuf.append(c);
              break;
            } 
            state = -1;
            break;
          } 
          if (c == ',') {
            gotLanguageQ = true;
            break;
          } 
          if (c == ' ' || c == '\t') {
            state = 3;
            break;
          } 
          if (c == ';') {
            state = 4;
            break;
          } 
          state = -1;
          break;
        case 2:
          if (c == ',') {
            gotLanguageQ = true;
            break;
          } 
          if (c == ' ' || c == '\t') {
            state = 3;
            break;
          } 
          if (c == ';') {
            state = 4;
            break;
          } 
          state = -1;
          break;
        case 3:
          if (c == ',') {
            gotLanguageQ = true;
            break;
          } 
          if (c == ';') {
            state = 4;
            break;
          } 
          if (c != ' ' && c != '\t')
            state = -1; 
          break;
        case 4:
          if (c == 'q') {
            state = 5;
            break;
          } 
          if (c != ' ' && c != '\t')
            state = -1; 
          break;
        case 5:
          if (c == '=') {
            state = 6;
            break;
          } 
          if (c != ' ' && c != '\t')
            state = -1; 
          break;
        case 6:
          if (c == '0') {
            q1 = false;
            qvalBuf.append(c);
            state = 7;
            break;
          } 
          if (c == '1') {
            qvalBuf.append(c);
            state = 7;
            break;
          } 
          if (c == '.') {
            if (isLenient) {
              qvalBuf.append(c);
              state = 8;
              break;
            } 
            state = -1;
            break;
          } 
          if (c != ' ' && c != '\t')
            state = -1; 
          break;
        case 7:
          if (c == '.') {
            qvalBuf.append(c);
            state = 8;
            break;
          } 
          if (c == ',') {
            gotLanguageQ = true;
            break;
          } 
          if (c == ' ' || c == '\t') {
            state = 10;
            break;
          } 
          state = -1;
          break;
        case 8:
          if ('0' <= c || c <= '9') {
            if (q1 && c != '0' && !isLenient) {
              state = -1;
              break;
            } 
            qvalBuf.append(c);
            state = 9;
            break;
          } 
          state = -1;
          break;
        case 9:
          if ('0' <= c && c <= '9') {
            if (q1 && c != '0') {
              state = -1;
              break;
            } 
            qvalBuf.append(c);
            break;
          } 
          if (c == ',') {
            gotLanguageQ = true;
            break;
          } 
          if (c == ' ' || c == '\t') {
            state = 10;
            break;
          } 
          state = -1;
          break;
        case 10:
          if (c == ',') {
            gotLanguageQ = true;
            break;
          } 
          if (c != ' ' && c != '\t')
            state = -1; 
          break;
      } 
      if (state == -1)
        throw new ParseException("Invalid Accept-Language", n); 
      if (gotLanguageQ) {
        double q = 1.0D;
        if (qvalBuf.length() != 0) {
          try {
            q = Double.parseDouble(qvalBuf.toString());
          } catch (NumberFormatException nfe) {
            q = 1.0D;
          } 
          if (q > 1.0D)
            q = 1.0D; 
        } 
        if (languageRangeBuf.charAt(0) != '*') {
          int serial = map.size();
          ULocaleAcceptLanguageQ entry = new ULocaleAcceptLanguageQ(q, serial);
          map.put(entry, new ULocale(canonicalize(languageRangeBuf.toString())));
        } 
        languageRangeBuf.setLength(0);
        qvalBuf.setLength(0);
        state = 0;
      } 
    } 
    if (state != 0)
      throw new ParseException("Invalid AcceptlLanguage", n); 
    ULocale[] acceptList = (ULocale[])map.values().toArray((Object[])new ULocale[map.size()]);
    return acceptList;
  }
  
  public static ULocale addLikelySubtags(ULocale loc) {
    String[] tags = new String[3];
    String trailing = null;
    int trailingIndex = parseTagString(loc.localeID, tags);
    if (trailingIndex < loc.localeID.length())
      trailing = loc.localeID.substring(trailingIndex); 
    String newLocaleID = createLikelySubtagsString(tags[0], tags[1], tags[2], trailing);
    return (newLocaleID == null) ? loc : new ULocale(newLocaleID);
  }
  
  public static ULocale minimizeSubtags(ULocale loc) {
    String[] tags = new String[3];
    int trailingIndex = parseTagString(loc.localeID, tags);
    String originalLang = tags[0];
    String originalScript = tags[1];
    String originalRegion = tags[2];
    String originalTrailing = null;
    if (trailingIndex < loc.localeID.length())
      originalTrailing = loc.localeID.substring(trailingIndex); 
    String maximizedLocaleID = createLikelySubtagsString(originalLang, originalScript, originalRegion, null);
    if (isEmptyString(maximizedLocaleID))
      return loc; 
    String tag = createLikelySubtagsString(originalLang, null, null, null);
    if (tag.equals(maximizedLocaleID)) {
      String newLocaleID = createTagString(originalLang, null, null, originalTrailing);
      return new ULocale(newLocaleID);
    } 
    if (originalRegion.length() != 0) {
      tag = createLikelySubtagsString(originalLang, null, originalRegion, null);
      if (tag.equals(maximizedLocaleID)) {
        String newLocaleID = createTagString(originalLang, null, originalRegion, originalTrailing);
        return new ULocale(newLocaleID);
      } 
    } 
    if (originalRegion.length() != 0 && originalScript.length() != 0) {
      tag = createLikelySubtagsString(originalLang, originalScript, null, null);
      if (tag.equals(maximizedLocaleID)) {
        String newLocaleID = createTagString(originalLang, originalScript, null, originalTrailing);
        return new ULocale(newLocaleID);
      } 
    } 
    return loc;
  }
  
  private static boolean isEmptyString(String string) {
    return (string == null || string.length() == 0);
  }
  
  private static void appendTag(String tag, StringBuilder buffer) {
    if (buffer.length() != 0)
      buffer.append('_'); 
    buffer.append(tag);
  }
  
  private static String createTagString(String lang, String script, String region, String trailing, String alternateTags) {
    LocaleIDParser parser = null;
    boolean regionAppended = false;
    StringBuilder tag = new StringBuilder();
    if (!isEmptyString(lang)) {
      appendTag(lang, tag);
    } else if (isEmptyString(alternateTags)) {
      appendTag("und", tag);
    } else {
      parser = new LocaleIDParser(alternateTags);
      String alternateLang = parser.getLanguage();
      appendTag(!isEmptyString(alternateLang) ? alternateLang : "und", tag);
    } 
    if (!isEmptyString(script)) {
      appendTag(script, tag);
    } else if (!isEmptyString(alternateTags)) {
      if (parser == null)
        parser = new LocaleIDParser(alternateTags); 
      String alternateScript = parser.getScript();
      if (!isEmptyString(alternateScript))
        appendTag(alternateScript, tag); 
    } 
    if (!isEmptyString(region)) {
      appendTag(region, tag);
      regionAppended = true;
    } else if (!isEmptyString(alternateTags)) {
      if (parser == null)
        parser = new LocaleIDParser(alternateTags); 
      String alternateRegion = parser.getCountry();
      if (!isEmptyString(alternateRegion)) {
        appendTag(alternateRegion, tag);
        regionAppended = true;
      } 
    } 
    if (trailing != null && trailing.length() > 1) {
      int separators = 0;
      if (trailing.charAt(0) == '_') {
        if (trailing.charAt(1) == '_')
          separators = 2; 
      } else {
        separators = 1;
      } 
      if (regionAppended) {
        if (separators == 2) {
          tag.append(trailing.substring(1));
        } else {
          tag.append(trailing);
        } 
      } else {
        if (separators == 1)
          tag.append('_'); 
        tag.append(trailing);
      } 
    } 
    return tag.toString();
  }
  
  static String createTagString(String lang, String script, String region, String trailing) {
    return createTagString(lang, script, region, trailing, null);
  }
  
  private static int parseTagString(String localeID, String[] tags) {
    LocaleIDParser parser = new LocaleIDParser(localeID);
    String lang = parser.getLanguage();
    String script = parser.getScript();
    String region = parser.getCountry();
    if (isEmptyString(lang)) {
      tags[0] = "und";
    } else {
      tags[0] = lang;
    } 
    if (script.equals("Zzzz")) {
      tags[1] = "";
    } else {
      tags[1] = script;
    } 
    if (region.equals("ZZ")) {
      tags[2] = "";
    } else {
      tags[2] = region;
    } 
    String variant = parser.getVariant();
    if (!isEmptyString(variant)) {
      int i = localeID.indexOf(variant);
      return (i > 0) ? (i - 1) : i;
    } 
    int index = localeID.indexOf('@');
    return (index == -1) ? localeID.length() : index;
  }
  
  private static String lookupLikelySubtags(String localeId) {
    UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "likelySubtags");
    try {
      return bundle.getString(localeId);
    } catch (MissingResourceException e) {
      return null;
    } 
  }
  
  private static String createLikelySubtagsString(String lang, String script, String region, String variants) {
    if (!isEmptyString(script) && !isEmptyString(region)) {
      String str1 = createTagString(lang, script, region, null);
      String str2 = lookupLikelySubtags(str1);
      if (str2 != null)
        return createTagString(null, null, null, variants, str2); 
    } 
    if (!isEmptyString(script)) {
      String str1 = createTagString(lang, script, null, null);
      String str2 = lookupLikelySubtags(str1);
      if (str2 != null)
        return createTagString(null, null, region, variants, str2); 
    } 
    if (!isEmptyString(region)) {
      String str1 = createTagString(lang, null, region, null);
      String str2 = lookupLikelySubtags(str1);
      if (str2 != null)
        return createTagString(null, script, null, variants, str2); 
    } 
    String searchTag = createTagString(lang, null, null, null);
    String likelySubtags = lookupLikelySubtags(searchTag);
    if (likelySubtags != null)
      return createTagString(null, script, region, variants, likelySubtags); 
    return null;
  }
  
  public String getExtension(char key) {
    if (!LocaleExtensions.isValidKey(key))
      throw new IllegalArgumentException("Invalid extension key: " + key); 
    return extensions().getExtensionValue(Character.valueOf(key));
  }
  
  public Set<Character> getExtensionKeys() {
    return extensions().getKeys();
  }
  
  public Set<String> getUnicodeLocaleAttributes() {
    return extensions().getUnicodeLocaleAttributes();
  }
  
  public String getUnicodeLocaleType(String key) {
    if (!LocaleExtensions.isValidUnicodeLocaleKey(key))
      throw new IllegalArgumentException("Invalid Unicode locale key: " + key); 
    return extensions().getUnicodeLocaleType(key);
  }
  
  public Set<String> getUnicodeLocaleKeys() {
    return extensions().getUnicodeLocaleKeys();
  }
  
  public String toLanguageTag() {
    BaseLocale base = base();
    LocaleExtensions exts = extensions();
    if (base.getVariant().equalsIgnoreCase("POSIX")) {
      base = BaseLocale.getInstance(base.getLanguage(), base.getScript(), base.getRegion(), "");
      if (exts.getUnicodeLocaleType("va") == null) {
        InternalLocaleBuilder ilocbld = new InternalLocaleBuilder();
        try {
          ilocbld.setLocale(BaseLocale.ROOT, exts);
          ilocbld.setUnicodeLocaleKeyword("va", "posix");
          exts = ilocbld.getLocaleExtensions();
        } catch (LocaleSyntaxException e) {
          throw new RuntimeException(e);
        } 
      } 
    } 
    LanguageTag tag = LanguageTag.parseLocale(base, exts);
    StringBuilder buf = new StringBuilder();
    String subtag = tag.getLanguage();
    if (subtag.length() > 0)
      buf.append(LanguageTag.canonicalizeLanguage(subtag)); 
    subtag = tag.getScript();
    if (subtag.length() > 0) {
      buf.append("-");
      buf.append(LanguageTag.canonicalizeScript(subtag));
    } 
    subtag = tag.getRegion();
    if (subtag.length() > 0) {
      buf.append("-");
      buf.append(LanguageTag.canonicalizeRegion(subtag));
    } 
    List<String> subtags = tag.getVariants();
    for (String s : subtags) {
      buf.append("-");
      buf.append(LanguageTag.canonicalizeVariant(s));
    } 
    subtags = tag.getExtensions();
    for (String s : subtags) {
      buf.append("-");
      buf.append(LanguageTag.canonicalizeExtension(s));
    } 
    subtag = tag.getPrivateuse();
    if (subtag.length() > 0) {
      if (buf.length() > 0)
        buf.append("-"); 
      buf.append("x").append("-");
      buf.append(LanguageTag.canonicalizePrivateuse(subtag));
    } 
    return buf.toString();
  }
  
  public static ULocale forLanguageTag(String languageTag) {
    LanguageTag tag = LanguageTag.parse(languageTag, null);
    InternalLocaleBuilder bldr = new InternalLocaleBuilder();
    bldr.setLanguageTag(tag);
    return getInstance(bldr.getBaseLocale(), bldr.getLocaleExtensions());
  }
  
  public static final class Builder {
    private final InternalLocaleBuilder _locbld = new InternalLocaleBuilder();
    
    public Builder setLocale(ULocale locale) {
      try {
        this._locbld.setLocale(locale.base(), locale.extensions());
      } catch (LocaleSyntaxException e) {
        throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
      } 
      return this;
    }
    
    public Builder setLanguageTag(String languageTag) {
      ParseStatus sts = new ParseStatus();
      LanguageTag tag = LanguageTag.parse(languageTag, sts);
      if (sts.isError())
        throw new IllformedLocaleException(sts.getErrorMessage(), sts.getErrorIndex()); 
      this._locbld.setLanguageTag(tag);
      return this;
    }
    
    public Builder setLanguage(String language) {
      try {
        this._locbld.setLanguage(language);
      } catch (LocaleSyntaxException e) {
        throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
      } 
      return this;
    }
    
    public Builder setScript(String script) {
      try {
        this._locbld.setScript(script);
      } catch (LocaleSyntaxException e) {
        throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
      } 
      return this;
    }
    
    public Builder setRegion(String region) {
      try {
        this._locbld.setRegion(region);
      } catch (LocaleSyntaxException e) {
        throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
      } 
      return this;
    }
    
    public Builder setVariant(String variant) {
      try {
        this._locbld.setVariant(variant);
      } catch (LocaleSyntaxException e) {
        throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
      } 
      return this;
    }
    
    public Builder setExtension(char key, String value) {
      try {
        this._locbld.setExtension(key, value);
      } catch (LocaleSyntaxException e) {
        throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
      } 
      return this;
    }
    
    public Builder setUnicodeLocaleKeyword(String key, String type) {
      try {
        this._locbld.setUnicodeLocaleKeyword(key, type);
      } catch (LocaleSyntaxException e) {
        throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
      } 
      return this;
    }
    
    public Builder addUnicodeLocaleAttribute(String attribute) {
      try {
        this._locbld.addUnicodeLocaleAttribute(attribute);
      } catch (LocaleSyntaxException e) {
        throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
      } 
      return this;
    }
    
    public Builder removeUnicodeLocaleAttribute(String attribute) {
      try {
        this._locbld.removeUnicodeLocaleAttribute(attribute);
      } catch (LocaleSyntaxException e) {
        throw new IllformedLocaleException(e.getMessage(), e.getErrorIndex());
      } 
      return this;
    }
    
    public Builder clear() {
      this._locbld.clear();
      return this;
    }
    
    public Builder clearExtensions() {
      this._locbld.clearExtensions();
      return this;
    }
    
    public ULocale build() {
      return ULocale.getInstance(this._locbld.getBaseLocale(), this._locbld.getLocaleExtensions());
    }
  }
  
  private static ULocale getInstance(BaseLocale base, LocaleExtensions exts) {
    String id = lscvToID(base.getLanguage(), base.getScript(), base.getRegion(), base.getVariant());
    Set<Character> extKeys = exts.getKeys();
    if (!extKeys.isEmpty()) {
      TreeMap<String, String> kwds = new TreeMap<String, String>();
      for (Character key : extKeys) {
        Extension ext = exts.getExtension(key);
        if (ext instanceof UnicodeLocaleExtension) {
          UnicodeLocaleExtension uext = (UnicodeLocaleExtension)ext;
          Set<String> ukeys = uext.getUnicodeLocaleKeys();
          for (String bcpKey : ukeys) {
            String bcpType = uext.getUnicodeLocaleType(bcpKey);
            String lkey = bcp47ToLDMLKey(bcpKey);
            String ltype = bcp47ToLDMLType(lkey, (bcpType.length() == 0) ? "yes" : bcpType);
            if (lkey.equals("va") && ltype.equals("posix") && base.getVariant().length() == 0) {
              id = id + "_POSIX";
              continue;
            } 
            kwds.put(lkey, ltype);
          } 
          Set<String> uattributes = uext.getUnicodeLocaleAttributes();
          if (uattributes.size() > 0) {
            StringBuilder attrbuf = new StringBuilder();
            for (String attr : uattributes) {
              if (attrbuf.length() > 0)
                attrbuf.append('-'); 
              attrbuf.append(attr);
            } 
            kwds.put("attribute", attrbuf.toString());
          } 
          continue;
        } 
        kwds.put(String.valueOf(key), ext.getValue());
      } 
      if (!kwds.isEmpty()) {
        StringBuilder buf = new StringBuilder(id);
        buf.append("@");
        Set<Map.Entry<String, String>> kset = kwds.entrySet();
        boolean insertSep = false;
        for (Map.Entry<String, String> kwd : kset) {
          if (insertSep) {
            buf.append(";");
          } else {
            insertSep = true;
          } 
          buf.append(kwd.getKey());
          buf.append("=");
          buf.append(kwd.getValue());
        } 
        id = buf.toString();
      } 
    } 
    return new ULocale(id);
  }
  
  private BaseLocale base() {
    if (this.baseLocale == null) {
      String language = getLanguage();
      if (equals(ROOT))
        language = ""; 
      this.baseLocale = BaseLocale.getInstance(language, getScript(), getCountry(), getVariant());
    } 
    return this.baseLocale;
  }
  
  private LocaleExtensions extensions() {
    if (this.extensions == null) {
      Iterator<String> kwitr = getKeywords();
      if (kwitr == null) {
        this.extensions = LocaleExtensions.EMPTY_EXTENSIONS;
      } else {
        InternalLocaleBuilder intbld = new InternalLocaleBuilder();
        while (kwitr.hasNext()) {
          String key = kwitr.next();
          if (key.equals("attribute")) {
            String[] uattributes = getKeywordValue(key).split("[-_]");
            for (String uattr : uattributes) {
              try {
                intbld.addUnicodeLocaleAttribute(uattr);
              } catch (LocaleSyntaxException e) {}
            } 
            continue;
          } 
          if (key.length() >= 2) {
            String bcpKey = ldmlKeyToBCP47(key);
            String bcpType = ldmlTypeToBCP47(key, getKeywordValue(key));
            if (bcpKey != null && bcpType != null)
              try {
                intbld.setUnicodeLocaleKeyword(bcpKey, bcpType);
              } catch (LocaleSyntaxException e) {} 
            continue;
          } 
          if (key.length() == 1 && key.charAt(0) != 'u')
            try {
              intbld.setExtension(key.charAt(0), getKeywordValue(key).replace("_", "-"));
            } catch (LocaleSyntaxException e) {} 
        } 
        this.extensions = intbld.getLocaleExtensions();
      } 
    } 
    return this.extensions;
  }
  
  private static String ldmlKeyToBCP47(String key) {
    UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
    UResourceBundle keyMap = keyTypeData.get("keyMap");
    key = AsciiUtil.toLowerString(key);
    String bcpKey = null;
    try {
      bcpKey = keyMap.getString(key);
    } catch (MissingResourceException mre) {}
    if (bcpKey == null) {
      if (key.length() == 2 && LanguageTag.isExtensionSubtag(key))
        return key; 
      return null;
    } 
    return bcpKey;
  }
  
  private static String bcp47ToLDMLKey(String bcpKey) {
    UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
    UResourceBundle keyMap = keyTypeData.get("keyMap");
    bcpKey = AsciiUtil.toLowerString(bcpKey);
    String key = null;
    for (int i = 0; i < keyMap.getSize(); i++) {
      UResourceBundle mapData = keyMap.get(i);
      if (bcpKey.equals(mapData.getString())) {
        key = mapData.getKey();
        break;
      } 
    } 
    if (key == null)
      return bcpKey; 
    return key;
  }
  
  private static String ldmlTypeToBCP47(String key, String type) {
    UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
    UResourceBundle typeMap = keyTypeData.get("typeMap");
    key = AsciiUtil.toLowerString(key);
    UResourceBundle typeMapForKey = null;
    String bcpType = null;
    String typeResKey = key.equals("timezone") ? type.replace('/', ':') : type;
    try {
      typeMapForKey = typeMap.get(key);
      bcpType = typeMapForKey.getString(typeResKey);
    } catch (MissingResourceException mre) {}
    if (bcpType == null && typeMapForKey != null) {
      UResourceBundle typeAlias = keyTypeData.get("typeAlias");
      try {
        UResourceBundle typeAliasForKey = typeAlias.get(key);
        typeResKey = typeAliasForKey.getString(typeResKey);
        bcpType = typeMapForKey.getString(typeResKey.replace('/', ':'));
      } catch (MissingResourceException mre) {}
    } 
    if (bcpType == null) {
      int typeLen = type.length();
      if (typeLen >= 3 && typeLen <= 8 && LanguageTag.isExtensionSubtag(type))
        return type; 
      return null;
    } 
    return bcpType;
  }
  
  private static String bcp47ToLDMLType(String key, String bcpType) {
    UResourceBundle keyTypeData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
    UResourceBundle typeMap = keyTypeData.get("typeMap");
    key = AsciiUtil.toLowerString(key);
    bcpType = AsciiUtil.toLowerString(bcpType);
    String type = null;
    try {
      UResourceBundle typeMapForKey = typeMap.get(key);
      for (int i = 0; i < typeMapForKey.getSize(); i++) {
        UResourceBundle mapData = typeMapForKey.get(i);
        if (bcpType.equals(mapData.getString())) {
          type = mapData.getKey();
          if (key.equals("timezone"))
            type = type.replace(':', '/'); 
          break;
        } 
      } 
    } catch (MissingResourceException mre) {}
    if (type == null)
      return bcpType; 
    return type;
  }
  
  private static final class JDKLocaleHelper {
    private static boolean isJava7orNewer = false;
    
    private static Method mGetScript;
    
    private static Method mGetExtensionKeys;
    
    private static Method mGetExtension;
    
    private static Method mGetUnicodeLocaleKeys;
    
    private static Method mGetUnicodeLocaleAttributes;
    
    private static Method mGetUnicodeLocaleType;
    
    private static Method mForLanguageTag;
    
    private static Method mGetDefault;
    
    private static Method mSetDefault;
    
    private static Object eDISPLAY;
    
    private static Object eFORMAT;
    
    private static final String[][] JAVA6_MAPDATA = new String[][] { { "ja_JP_JP", "ja_JP", "calendar", "japanese", "ja" }, { "no_NO_NY", "nn_NO", null, null, "nn" }, { "th_TH_TH", "th_TH", "numbers", "thai", "th" } };
    
    static {
      try {
        mGetScript = Locale.class.getMethod("getScript", (Class[])null);
        mGetExtensionKeys = Locale.class.getMethod("getExtensionKeys", (Class[])null);
        mGetExtension = Locale.class.getMethod("getExtension", new Class[] { char.class });
        mGetUnicodeLocaleKeys = Locale.class.getMethod("getUnicodeLocaleKeys", (Class[])null);
        mGetUnicodeLocaleAttributes = Locale.class.getMethod("getUnicodeLocaleAttributes", (Class[])null);
        mGetUnicodeLocaleType = Locale.class.getMethod("getUnicodeLocaleType", new Class[] { String.class });
        mForLanguageTag = Locale.class.getMethod("forLanguageTag", new Class[] { String.class });
        Class<?> cCategory = null;
        Class<?>[] classes = Locale.class.getDeclaredClasses();
        for (Class<?> c : classes) {
          if (c.getName().equals("java.util.Locale$Category")) {
            cCategory = c;
            break;
          } 
        } 
        if (cCategory != null) {
          mGetDefault = Locale.class.getDeclaredMethod("getDefault", new Class[] { cCategory });
          mSetDefault = Locale.class.getDeclaredMethod("setDefault", new Class[] { cCategory, Locale.class });
          Method mName = cCategory.getMethod("name", (Class[])null);
          Object[] enumConstants = cCategory.getEnumConstants();
          for (Object e : enumConstants) {
            String catVal = (String)mName.invoke(e, (Object[])null);
            if (catVal.equals("DISPLAY")) {
              eDISPLAY = e;
            } else if (catVal.equals("FORMAT")) {
              eFORMAT = e;
            } 
          } 
          if (eDISPLAY != null && eFORMAT != null)
            isJava7orNewer = true; 
        } 
      } catch (NoSuchMethodException e) {
      
      } catch (IllegalArgumentException e) {
      
      } catch (IllegalAccessException e) {
      
      } catch (InvocationTargetException e) {
      
      } catch (SecurityException e) {}
    }
    
    public static boolean isJava7orNewer() {
      return isJava7orNewer;
    }
    
    public static ULocale toULocale(Locale loc) {
      return isJava7orNewer ? toULocale7(loc) : toULocale6(loc);
    }
    
    public static Locale toLocale(ULocale uloc) {
      return isJava7orNewer ? toLocale7(uloc) : toLocale6(uloc);
    }
    
    private static ULocale toULocale7(Locale loc) {
      String language = loc.getLanguage();
      String script = "";
      String country = loc.getCountry();
      String variant = loc.getVariant();
      Set<String> attributes = null;
      Map<String, String> keywords = null;
      try {
        script = (String)mGetScript.invoke(loc, (Object[])null);
        Set<Character> extKeys = (Set<Character>)mGetExtensionKeys.invoke(loc, (Object[])null);
        if (!extKeys.isEmpty())
          for (Character extKey : extKeys) {
            if (extKey.charValue() == 'u') {
              Set<String> uAttributes = (Set<String>)mGetUnicodeLocaleAttributes.invoke(loc, (Object[])null);
              if (!uAttributes.isEmpty()) {
                attributes = new TreeSet<String>();
                for (String attr : uAttributes)
                  attributes.add(attr); 
              } 
              Set<String> uKeys = (Set<String>)mGetUnicodeLocaleKeys.invoke(loc, (Object[])null);
              for (String kwKey : uKeys) {
                String kwVal = (String)mGetUnicodeLocaleType.invoke(loc, new Object[] { kwKey });
                if (kwVal != null) {
                  if (kwKey.equals("va")) {
                    variant = (variant.length() == 0) ? kwVal : (kwVal + "_" + variant);
                    continue;
                  } 
                  if (keywords == null)
                    keywords = new TreeMap<String, String>(); 
                  keywords.put(kwKey, kwVal);
                } 
              } 
              continue;
            } 
            String extVal = (String)mGetExtension.invoke(loc, new Object[] { extKey });
            if (extVal != null) {
              if (keywords == null)
                keywords = new TreeMap<String, String>(); 
              keywords.put(String.valueOf(extKey), extVal);
            } 
          }  
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      } 
      if (language.equals("no") && country.equals("NO") && variant.equals("NY")) {
        language = "nn";
        variant = "";
      } 
      StringBuilder buf = new StringBuilder(language);
      if (script.length() > 0) {
        buf.append('_');
        buf.append(script);
      } 
      if (country.length() > 0) {
        buf.append('_');
        buf.append(country);
      } 
      if (variant.length() > 0) {
        if (country.length() == 0)
          buf.append('_'); 
        buf.append('_');
        buf.append(variant);
      } 
      if (attributes != null) {
        StringBuilder attrBuf = new StringBuilder();
        for (String attr : attributes) {
          if (attrBuf.length() != 0)
            attrBuf.append('-'); 
          attrBuf.append(attr);
        } 
        if (keywords == null)
          keywords = new TreeMap<String, String>(); 
        keywords.put("attribute", attrBuf.toString());
      } 
      if (keywords != null) {
        buf.append('@');
        boolean addSep = false;
        for (Map.Entry<String, String> kwEntry : keywords.entrySet()) {
          String kwKey = kwEntry.getKey();
          String kwVal = kwEntry.getValue();
          if (kwKey.length() != 1) {
            kwKey = ULocale.bcp47ToLDMLKey(kwKey);
            kwVal = ULocale.bcp47ToLDMLType(kwKey, (kwVal.length() == 0) ? "yes" : kwVal);
          } 
          if (addSep) {
            buf.append(';');
          } else {
            addSep = true;
          } 
          buf.append(kwKey);
          buf.append('=');
          buf.append(kwVal);
        } 
      } 
      return new ULocale(ULocale.getName(buf.toString()), loc);
    }
    
    private static ULocale toULocale6(Locale loc) {
      ULocale uloc = null;
      String locStr = loc.toString();
      if (locStr.length() == 0) {
        uloc = ULocale.ROOT;
      } else {
        for (int i = 0; i < JAVA6_MAPDATA.length; i++) {
          if (JAVA6_MAPDATA[i][0].equals(locStr)) {
            LocaleIDParser p = new LocaleIDParser(JAVA6_MAPDATA[i][1]);
            p.setKeywordValue(JAVA6_MAPDATA[i][2], JAVA6_MAPDATA[i][3]);
            locStr = p.getName();
            break;
          } 
        } 
        uloc = new ULocale(ULocale.getName(locStr), loc);
      } 
      return uloc;
    }
    
    private static Locale toLocale7(ULocale uloc) {
      Locale loc = null;
      String ulocStr = uloc.getName();
      if (uloc.getScript().length() > 0 || ulocStr.contains("@")) {
        String tag = uloc.toLanguageTag();
        tag = AsciiUtil.toUpperString(tag);
        try {
          loc = (Locale)mForLanguageTag.invoke(null, new Object[] { tag });
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e);
        } 
      } 
      if (loc == null)
        loc = new Locale(uloc.getLanguage(), uloc.getCountry(), uloc.getVariant()); 
      return loc;
    }
    
    private static Locale toLocale6(ULocale uloc) {
      String locstr = uloc.getBaseName();
      for (int i = 0; i < JAVA6_MAPDATA.length; i++) {
        if (locstr.equals(JAVA6_MAPDATA[i][1]) || locstr.equals(JAVA6_MAPDATA[i][4]))
          if (JAVA6_MAPDATA[i][2] != null) {
            String val = uloc.getKeywordValue(JAVA6_MAPDATA[i][2]);
            if (val != null && val.equals(JAVA6_MAPDATA[i][3])) {
              locstr = JAVA6_MAPDATA[i][0];
              break;
            } 
          } else {
            locstr = JAVA6_MAPDATA[i][0];
            break;
          }  
      } 
      LocaleIDParser p = new LocaleIDParser(locstr);
      String[] names = p.getLanguageScriptCountryVariant();
      return new Locale(names[0], names[2], names[3]);
    }
    
    public static Locale getDefault(ULocale.Category category) {
      Locale loc = Locale.getDefault();
      if (isJava7orNewer) {
        Object cat = null;
        switch (category) {
          case DISPLAY:
            cat = eDISPLAY;
            break;
          case FORMAT:
            cat = eFORMAT;
            break;
        } 
        if (cat != null)
          try {
            loc = (Locale)mGetDefault.invoke(null, new Object[] { cat });
          } catch (InvocationTargetException e) {
          
          } catch (IllegalArgumentException e) {
          
          } catch (IllegalAccessException e) {} 
      } 
      return loc;
    }
    
    public static void setDefault(ULocale.Category category, Locale newLocale) {
      if (isJava7orNewer) {
        Object cat = null;
        switch (category) {
          case DISPLAY:
            cat = eDISPLAY;
            break;
          case FORMAT:
            cat = eFORMAT;
            break;
        } 
        if (cat != null)
          try {
            mSetDefault.invoke(null, new Object[] { cat, newLocale });
          } catch (InvocationTargetException e) {
          
          } catch (IllegalArgumentException e) {
          
          } catch (IllegalAccessException e) {} 
      } 
    }
    
    public static boolean isOriginalDefaultLocale(Locale loc) {
      if (isJava7orNewer) {
        String script = "";
        try {
          script = (String)mGetScript.invoke(loc, (Object[])null);
        } catch (Exception e) {
          return false;
        } 
        return (loc.getLanguage().equals(getSystemProperty("user.language")) && loc.getCountry().equals(getSystemProperty("user.country")) && loc.getVariant().equals(getSystemProperty("user.variant")) && script.equals(getSystemProperty("user.script")));
      } 
      return (loc.getLanguage().equals(getSystemProperty("user.language")) && loc.getCountry().equals(getSystemProperty("user.country")) && loc.getVariant().equals(getSystemProperty("user.variant")));
    }
    
    public static String getSystemProperty(String key) {
      String val = null;
      final String fkey = key;
      if (System.getSecurityManager() != null) {
        try {
          val = AccessController.<String>doPrivileged(new PrivilegedAction<String>() {
                public String run() {
                  return System.getProperty(fkey);
                }
              });
        } catch (AccessControlException e) {}
      } else {
        val = System.getProperty(fkey);
      } 
      return val;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\ULocale.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */