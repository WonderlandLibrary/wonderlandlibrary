package com.ibm.icu.util;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.text.Collator;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class GlobalizationPreferences implements Freezable<GlobalizationPreferences> {
  public static final int NF_NUMBER = 0;
  
  public static final int NF_CURRENCY = 1;
  
  public static final int NF_PERCENT = 2;
  
  public static final int NF_SCIENTIFIC = 3;
  
  public static final int NF_INTEGER = 4;
  
  private static final int NF_LIMIT = 5;
  
  public static final int DF_FULL = 0;
  
  public static final int DF_LONG = 1;
  
  public static final int DF_MEDIUM = 2;
  
  public static final int DF_SHORT = 3;
  
  public static final int DF_NONE = 4;
  
  private static final int DF_LIMIT = 5;
  
  public static final int ID_LOCALE = 0;
  
  public static final int ID_LANGUAGE = 1;
  
  public static final int ID_SCRIPT = 2;
  
  public static final int ID_TERRITORY = 3;
  
  public static final int ID_VARIANT = 4;
  
  public static final int ID_KEYWORD = 5;
  
  public static final int ID_KEYWORD_VALUE = 6;
  
  public static final int ID_CURRENCY = 7;
  
  public static final int ID_CURRENCY_SYMBOL = 8;
  
  public static final int ID_TIMEZONE = 9;
  
  public static final int BI_CHARACTER = 0;
  
  public static final int BI_WORD = 1;
  
  public static final int BI_LINE = 2;
  
  public static final int BI_SENTENCE = 3;
  
  public static final int BI_TITLE = 4;
  
  private static final int BI_LIMIT = 5;
  
  private List<ULocale> locales;
  
  private String territory;
  
  private Currency currency;
  
  private TimeZone timezone;
  
  private Calendar calendar;
  
  private Collator collator;
  
  private BreakIterator[] breakIterators;
  
  private DateFormat[][] dateFormats;
  
  private NumberFormat[] numberFormats;
  
  private List<ULocale> implicitLocales;
  
  public GlobalizationPreferences() {
    reset();
  }
  
  public GlobalizationPreferences setLocales(List<ULocale> inputLocales) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    this.locales = processLocales(inputLocales);
    return this;
  }
  
  public List<ULocale> getLocales() {
    List<ULocale> result;
    if (this.locales == null) {
      result = guessLocales();
    } else {
      result = new ArrayList<ULocale>();
      result.addAll(this.locales);
    } 
    return result;
  }
  
  public ULocale getLocale(int index) {
    List<ULocale> lcls = this.locales;
    if (lcls == null)
      lcls = guessLocales(); 
    if (index >= 0 && index < lcls.size())
      return lcls.get(index); 
    return null;
  }
  
  public GlobalizationPreferences setLocales(ULocale[] uLocales) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    return setLocales(Arrays.asList(uLocales));
  }
  
  public GlobalizationPreferences setLocale(ULocale uLocale) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    return setLocales(new ULocale[] { uLocale });
  }
  
  public GlobalizationPreferences setLocales(String acceptLanguageString) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    ULocale[] acceptLocales = null;
    try {
      acceptLocales = ULocale.parseAcceptLanguage(acceptLanguageString, true);
    } catch (ParseException pe) {
      throw new IllegalArgumentException("Invalid Accept-Language string");
    } 
    return setLocales(acceptLocales);
  }
  
  public ResourceBundle getResourceBundle(String baseName) {
    return getResourceBundle(baseName, null);
  }
  
  public ResourceBundle getResourceBundle(String baseName, ClassLoader loader) {
    UResourceBundle urb = null;
    UResourceBundle candidate = null;
    String actualLocaleName = null;
    List<ULocale> fallbacks = getLocales();
    for (int i = 0; i < fallbacks.size(); i++) {
      String localeName = ((ULocale)fallbacks.get(i)).toString();
      if (actualLocaleName != null && localeName.equals(actualLocaleName)) {
        urb = candidate;
        break;
      } 
      try {
        if (loader == null) {
          candidate = UResourceBundle.getBundleInstance(baseName, localeName);
        } else {
          candidate = UResourceBundle.getBundleInstance(baseName, localeName, loader);
        } 
        if (candidate != null) {
          actualLocaleName = candidate.getULocale().getName();
          if (actualLocaleName.equals(localeName)) {
            urb = candidate;
            break;
          } 
          if (urb == null)
            urb = candidate; 
        } 
      } catch (MissingResourceException mre) {
        actualLocaleName = null;
      } 
    } 
    if (urb == null)
      throw new MissingResourceException("Can't find bundle for base name " + baseName, baseName, ""); 
    return urb;
  }
  
  public GlobalizationPreferences setTerritory(String territory) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    this.territory = territory;
    return this;
  }
  
  public String getTerritory() {
    if (this.territory == null)
      return guessTerritory(); 
    return this.territory;
  }
  
  public GlobalizationPreferences setCurrency(Currency currency) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    this.currency = currency;
    return this;
  }
  
  public Currency getCurrency() {
    if (this.currency == null)
      return guessCurrency(); 
    return this.currency;
  }
  
  public GlobalizationPreferences setCalendar(Calendar calendar) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    this.calendar = (Calendar)calendar.clone();
    return this;
  }
  
  public Calendar getCalendar() {
    if (this.calendar == null)
      return guessCalendar(); 
    Calendar temp = (Calendar)this.calendar.clone();
    temp.setTimeZone(getTimeZone());
    temp.setTimeInMillis(System.currentTimeMillis());
    return temp;
  }
  
  public GlobalizationPreferences setTimeZone(TimeZone timezone) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    this.timezone = (TimeZone)timezone.clone();
    return this;
  }
  
  public TimeZone getTimeZone() {
    if (this.timezone == null)
      return guessTimeZone(); 
    return this.timezone.cloneAsThawed();
  }
  
  public Collator getCollator() {
    if (this.collator == null)
      return guessCollator(); 
    try {
      return (Collator)this.collator.clone();
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException("Error in cloning collator");
    } 
  }
  
  public GlobalizationPreferences setCollator(Collator collator) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    try {
      this.collator = (Collator)collator.clone();
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException("Error in cloning collator");
    } 
    return this;
  }
  
  public BreakIterator getBreakIterator(int type) {
    if (type < 0 || type >= 5)
      throw new IllegalArgumentException("Illegal break iterator type"); 
    if (this.breakIterators == null || this.breakIterators[type] == null)
      return guessBreakIterator(type); 
    return (BreakIterator)this.breakIterators[type].clone();
  }
  
  public GlobalizationPreferences setBreakIterator(int type, BreakIterator iterator) {
    if (type < 0 || type >= 5)
      throw new IllegalArgumentException("Illegal break iterator type"); 
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    if (this.breakIterators == null)
      this.breakIterators = new BreakIterator[5]; 
    this.breakIterators[type] = (BreakIterator)iterator.clone();
    return this;
  }
  
  public String getDisplayName(String id, int type) {
    String result = id;
    for (ULocale locale : getLocales()) {
      String[] parts;
      Currency temp;
      SimpleDateFormat dtf;
      boolean isBadStr;
      String teststr;
      int sidx;
      int eidx;
      if (!isAvailableLocale(locale, 0))
        continue; 
      switch (type) {
        case 0:
          result = ULocale.getDisplayName(id, locale);
          break;
        case 1:
          result = ULocale.getDisplayLanguage(id, locale);
          break;
        case 2:
          result = ULocale.getDisplayScript("und-" + id, locale);
          break;
        case 3:
          result = ULocale.getDisplayCountry("und-" + id, locale);
          break;
        case 4:
          result = ULocale.getDisplayVariant("und-QQ-" + id, locale);
          break;
        case 5:
          result = ULocale.getDisplayKeyword(id, locale);
          break;
        case 6:
          parts = new String[2];
          Utility.split(id, '=', parts);
          result = ULocale.getDisplayKeywordValue("und@" + id, parts[0], locale);
          if (result.equals(parts[1]))
            continue; 
          break;
        case 7:
        case 8:
          temp = new Currency(id);
          result = temp.getName(locale, (type == 7) ? 1 : 0, new boolean[1]);
          break;
        case 9:
          dtf = new SimpleDateFormat("vvvv", locale);
          dtf.setTimeZone(TimeZone.getFrozenTimeZone(id));
          result = dtf.format(new Date());
          isBadStr = false;
          teststr = result;
          sidx = result.indexOf('(');
          eidx = result.indexOf(')');
          if (sidx != -1 && eidx != -1 && eidx - sidx == 3)
            teststr = result.substring(sidx + 1, eidx); 
          if (teststr.length() == 2) {
            isBadStr = true;
            for (int i = 0; i < 2; i++) {
              char c = teststr.charAt(i);
              if (c < 'A' || 'Z' < c) {
                isBadStr = false;
                break;
              } 
            } 
          } 
          if (isBadStr)
            continue; 
          break;
        default:
          throw new IllegalArgumentException("Unknown type: " + type);
      } 
      if (!id.equals(result))
        return result; 
    } 
    return result;
  }
  
  public GlobalizationPreferences setDateFormat(int dateStyle, int timeStyle, DateFormat format) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    if (this.dateFormats == null)
      this.dateFormats = new DateFormat[5][5]; 
    this.dateFormats[dateStyle][timeStyle] = (DateFormat)format.clone();
    return this;
  }
  
  public DateFormat getDateFormat(int dateStyle, int timeStyle) {
    if ((dateStyle == 4 && timeStyle == 4) || dateStyle < 0 || dateStyle >= 5 || timeStyle < 0 || timeStyle >= 5)
      throw new IllegalArgumentException("Illegal date format style arguments"); 
    DateFormat result = null;
    if (this.dateFormats != null)
      result = this.dateFormats[dateStyle][timeStyle]; 
    if (result != null) {
      result = (DateFormat)result.clone();
      result.setTimeZone(getTimeZone());
    } else {
      result = guessDateFormat(dateStyle, timeStyle);
    } 
    return result;
  }
  
  public NumberFormat getNumberFormat(int style) {
    if (style < 0 || style >= 5)
      throw new IllegalArgumentException("Illegal number format type"); 
    NumberFormat result = null;
    if (this.numberFormats != null)
      result = this.numberFormats[style]; 
    if (result != null) {
      result = (NumberFormat)result.clone();
    } else {
      result = guessNumberFormat(style);
    } 
    return result;
  }
  
  public GlobalizationPreferences setNumberFormat(int style, NumberFormat format) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    if (this.numberFormats == null)
      this.numberFormats = new NumberFormat[5]; 
    this.numberFormats[style] = (NumberFormat)format.clone();
    return this;
  }
  
  public GlobalizationPreferences reset() {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify immutable object"); 
    this.locales = null;
    this.territory = null;
    this.calendar = null;
    this.collator = null;
    this.breakIterators = null;
    this.timezone = null;
    this.currency = null;
    this.dateFormats = (DateFormat[][])null;
    this.numberFormats = null;
    this.implicitLocales = null;
    return this;
  }
  
  protected List<ULocale> processLocales(List<ULocale> inputLocales) {
    List<ULocale> result = new ArrayList<ULocale>();
    for (int i = 0; i < inputLocales.size(); i++) {
      ULocale uloc = inputLocales.get(i);
      String language = uloc.getLanguage();
      String script = uloc.getScript();
      String country = uloc.getCountry();
      String variant = uloc.getVariant();
      boolean bInserted = false;
      for (int j = 0; j < result.size(); j++) {
        ULocale u = result.get(j);
        if (u.getLanguage().equals(language)) {
          String s = u.getScript();
          String c = u.getCountry();
          String v = u.getVariant();
          if (!s.equals(script)) {
            if (s.length() == 0 && c.length() == 0 && v.length() == 0) {
              result.add(j, uloc);
              bInserted = true;
              break;
            } 
            if (s.length() == 0 && c.equals(country)) {
              result.add(j, uloc);
              bInserted = true;
              break;
            } 
            if (script.length() == 0 && country.length() > 0 && c.length() == 0) {
              result.add(j, uloc);
              bInserted = true;
              break;
            } 
          } else {
            if (!c.equals(country) && c.length() == 0 && v.length() == 0) {
              result.add(j, uloc);
              bInserted = true;
              break;
            } 
            if (!v.equals(variant) && v.length() == 0) {
              result.add(j, uloc);
              bInserted = true;
              break;
            } 
          } 
        } 
      } 
      if (!bInserted)
        result.add(uloc); 
    } 
    int index = 0;
    while (index < result.size()) {
      ULocale uloc = result.get(index);
      while (true) {
        uloc = uloc.getFallback();
        if (uloc.getLanguage().length() == 0)
          break; 
        index++;
        result.add(index, uloc);
      } 
      index++;
    } 
    index = 0;
    while (index < result.size() - 1) {
      ULocale uloc = result.get(index);
      boolean bRemoved = false;
      for (int j = index + 1; j < result.size(); j++) {
        if (uloc.equals(result.get(j))) {
          result.remove(index);
          bRemoved = true;
          break;
        } 
      } 
      if (!bRemoved)
        index++; 
    } 
    return result;
  }
  
  protected DateFormat guessDateFormat(int dateStyle, int timeStyle) {
    DateFormat result;
    ULocale dfLocale = getAvailableLocale(2);
    if (dfLocale == null)
      dfLocale = ULocale.ROOT; 
    if (timeStyle == 4) {
      result = DateFormat.getDateInstance(getCalendar(), dateStyle, dfLocale);
    } else if (dateStyle == 4) {
      result = DateFormat.getTimeInstance(getCalendar(), timeStyle, dfLocale);
    } else {
      result = DateFormat.getDateTimeInstance(getCalendar(), dateStyle, timeStyle, dfLocale);
    } 
    return result;
  }
  
  protected NumberFormat guessNumberFormat(int style) {
    NumberFormat result;
    ULocale nfLocale = getAvailableLocale(3);
    if (nfLocale == null)
      nfLocale = ULocale.ROOT; 
    switch (style) {
      case 0:
        result = NumberFormat.getInstance(nfLocale);
        return result;
      case 3:
        result = NumberFormat.getScientificInstance(nfLocale);
        return result;
      case 4:
        result = NumberFormat.getIntegerInstance(nfLocale);
        return result;
      case 2:
        result = NumberFormat.getPercentInstance(nfLocale);
        return result;
      case 1:
        result = NumberFormat.getCurrencyInstance(nfLocale);
        result.setCurrency(getCurrency());
        return result;
    } 
    throw new IllegalArgumentException("Unknown number format style");
  }
  
  protected String guessTerritory() {
    for (ULocale locale : getLocales()) {
      String str = locale.getCountry();
      if (str.length() != 0)
        return str; 
    } 
    ULocale firstLocale = getLocale(0);
    String language = firstLocale.getLanguage();
    String script = firstLocale.getScript();
    String result = null;
    if (script.length() != 0)
      result = language_territory_hack_map.get(language + "_" + script); 
    if (result == null)
      result = language_territory_hack_map.get(language); 
    if (result == null)
      result = "US"; 
    return result;
  }
  
  protected Currency guessCurrency() {
    return Currency.getInstance(new ULocale("und-" + getTerritory()));
  }
  
  protected List<ULocale> guessLocales() {
    if (this.implicitLocales == null) {
      List<ULocale> result = new ArrayList<ULocale>(1);
      result.add(ULocale.getDefault());
      this.implicitLocales = processLocales(result);
    } 
    return this.implicitLocales;
  }
  
  protected Collator guessCollator() {
    ULocale collLocale = getAvailableLocale(4);
    if (collLocale == null)
      collLocale = ULocale.ROOT; 
    return Collator.getInstance(collLocale);
  }
  
  protected BreakIterator guessBreakIterator(int type) {
    BreakIterator bitr = null;
    ULocale brkLocale = getAvailableLocale(5);
    if (brkLocale == null)
      brkLocale = ULocale.ROOT; 
    switch (type) {
      case 0:
        bitr = BreakIterator.getCharacterInstance(brkLocale);
        return bitr;
      case 4:
        bitr = BreakIterator.getTitleInstance(brkLocale);
        return bitr;
      case 1:
        bitr = BreakIterator.getWordInstance(brkLocale);
        return bitr;
      case 2:
        bitr = BreakIterator.getLineInstance(brkLocale);
        return bitr;
      case 3:
        bitr = BreakIterator.getSentenceInstance(brkLocale);
        return bitr;
    } 
    throw new IllegalArgumentException("Unknown break iterator type");
  }
  
  protected TimeZone guessTimeZone() {
    String timezoneString = territory_tzid_hack_map.get(getTerritory());
    if (timezoneString == null) {
      String[] attempt = TimeZone.getAvailableIDs(getTerritory());
      if (attempt.length == 0) {
        timezoneString = "Etc/GMT";
      } else {
        int i;
        for (i = 0; i < attempt.length && attempt[i].indexOf("/") < 0; i++);
        if (i > attempt.length)
          i = 0; 
        timezoneString = attempt[i];
      } 
    } 
    return TimeZone.getTimeZone(timezoneString);
  }
  
  protected Calendar guessCalendar() {
    ULocale calLocale = getAvailableLocale(1);
    if (calLocale == null)
      calLocale = ULocale.US; 
    return Calendar.getInstance(getTimeZone(), calLocale);
  }
  
  private ULocale getAvailableLocale(int type) {
    List<ULocale> locs = getLocales();
    ULocale result = null;
    for (int i = 0; i < locs.size(); i++) {
      ULocale l = locs.get(i);
      if (isAvailableLocale(l, type)) {
        result = l;
        break;
      } 
    } 
    return result;
  }
  
  private boolean isAvailableLocale(ULocale loc, int type) {
    BitSet bits = available_locales.get(loc);
    if (bits != null && bits.get(type))
      return true; 
    return false;
  }
  
  private static final HashMap<ULocale, BitSet> available_locales = new HashMap<ULocale, BitSet>();
  
  private static final int TYPE_GENERIC = 0;
  
  private static final int TYPE_CALENDAR = 1;
  
  private static final int TYPE_DATEFORMAT = 2;
  
  private static final int TYPE_NUMBERFORMAT = 3;
  
  private static final int TYPE_COLLATOR = 4;
  
  private static final int TYPE_BREAKITERATOR = 5;
  
  private static final int TYPE_LIMIT = 6;
  
  static {
    ULocale[] allLocales = ULocale.getAvailableLocales();
    for (int j = 0; j < allLocales.length; j++) {
      BitSet bits = new BitSet(6);
      available_locales.put(allLocales[j], bits);
      bits.set(0);
    } 
    ULocale[] calLocales = Calendar.getAvailableULocales();
    for (int k = 0; k < calLocales.length; k++) {
      BitSet bits = available_locales.get(calLocales[k]);
      if (bits == null) {
        bits = new BitSet(6);
        available_locales.put(allLocales[k], bits);
      } 
      bits.set(1);
    } 
    ULocale[] dateLocales = DateFormat.getAvailableULocales();
    for (int m = 0; m < dateLocales.length; m++) {
      BitSet bits = available_locales.get(dateLocales[m]);
      if (bits == null) {
        bits = new BitSet(6);
        available_locales.put(allLocales[m], bits);
      } 
      bits.set(2);
    } 
    ULocale[] numLocales = NumberFormat.getAvailableULocales();
    for (int n = 0; n < numLocales.length; n++) {
      BitSet bits = available_locales.get(numLocales[n]);
      if (bits == null) {
        bits = new BitSet(6);
        available_locales.put(allLocales[n], bits);
      } 
      bits.set(3);
    } 
    ULocale[] collLocales = Collator.getAvailableULocales();
    for (int i1 = 0; i1 < collLocales.length; i1++) {
      BitSet bits = available_locales.get(collLocales[i1]);
      if (bits == null) {
        bits = new BitSet(6);
        available_locales.put(allLocales[i1], bits);
      } 
      bits.set(4);
    } 
    ULocale[] brkLocales = BreakIterator.getAvailableULocales();
    for (int i2 = 0; i2 < brkLocales.length; i2++) {
      BitSet bits = available_locales.get(brkLocales[i2]);
      bits.set(5);
    } 
  }
  
  private static final Map<String, String> language_territory_hack_map = new HashMap<String, String>();
  
  private static final String[][] language_territory_hack = new String[][] { 
      { "af", "ZA" }, { "am", "ET" }, { "ar", "SA" }, { "as", "IN" }, { "ay", "PE" }, { "az", "AZ" }, { "bal", "PK" }, { "be", "BY" }, { "bg", "BG" }, { "bn", "IN" }, 
      { "bs", "BA" }, { "ca", "ES" }, { "ch", "MP" }, { "cpe", "SL" }, { "cs", "CZ" }, { "cy", "GB" }, { "da", "DK" }, { "de", "DE" }, { "dv", "MV" }, { "dz", "BT" }, 
      { "el", "GR" }, { "en", "US" }, { "es", "ES" }, { "et", "EE" }, { "eu", "ES" }, { "fa", "IR" }, { "fi", "FI" }, { "fil", "PH" }, { "fj", "FJ" }, { "fo", "FO" }, 
      { "fr", "FR" }, { "ga", "IE" }, { "gd", "GB" }, { "gl", "ES" }, { "gn", "PY" }, { "gu", "IN" }, { "gv", "GB" }, { "ha", "NG" }, { "he", "IL" }, { "hi", "IN" }, 
      { "ho", "PG" }, { "hr", "HR" }, { "ht", "HT" }, { "hu", "HU" }, { "hy", "AM" }, { "id", "ID" }, { "is", "IS" }, { "it", "IT" }, { "ja", "JP" }, { "ka", "GE" }, 
      { "kk", "KZ" }, { "kl", "GL" }, { "km", "KH" }, { "kn", "IN" }, { "ko", "KR" }, { "kok", "IN" }, { "ks", "IN" }, { "ku", "TR" }, { "ky", "KG" }, { "la", "VA" }, 
      { "lb", "LU" }, { "ln", "CG" }, { "lo", "LA" }, { "lt", "LT" }, { "lv", "LV" }, { "mai", "IN" }, { "men", "GN" }, { "mg", "MG" }, { "mh", "MH" }, { "mk", "MK" }, 
      { "ml", "IN" }, { "mn", "MN" }, { "mni", "IN" }, { "mo", "MD" }, { "mr", "IN" }, { "ms", "MY" }, { "mt", "MT" }, { "my", "MM" }, { "na", "NR" }, { "nb", "NO" }, 
      { "nd", "ZA" }, { "ne", "NP" }, { "niu", "NU" }, { "nl", "NL" }, { "nn", "NO" }, { "no", "NO" }, { "nr", "ZA" }, { "nso", "ZA" }, { "ny", "MW" }, { "om", "KE" }, 
      { "or", "IN" }, { "pa", "IN" }, { "pau", "PW" }, { "pl", "PL" }, { "ps", "PK" }, { "pt", "BR" }, { "qu", "PE" }, { "rn", "BI" }, { "ro", "RO" }, { "ru", "RU" }, 
      { "rw", "RW" }, { "sd", "IN" }, { "sg", "CF" }, { "si", "LK" }, { "sk", "SK" }, { "sl", "SI" }, { "sm", "WS" }, { "so", "DJ" }, { "sq", "CS" }, { "sr", "CS" }, 
      { "ss", "ZA" }, { "st", "ZA" }, { "sv", "SE" }, { "sw", "KE" }, { "ta", "IN" }, { "te", "IN" }, { "tem", "SL" }, { "tet", "TL" }, { "th", "TH" }, { "ti", "ET" }, 
      { "tg", "TJ" }, { "tk", "TM" }, { "tkl", "TK" }, { "tvl", "TV" }, { "tl", "PH" }, { "tn", "ZA" }, { "to", "TO" }, { "tpi", "PG" }, { "tr", "TR" }, { "ts", "ZA" }, 
      { "uk", "UA" }, { "ur", "IN" }, { "uz", "UZ" }, { "ve", "ZA" }, { "vi", "VN" }, { "wo", "SN" }, { "xh", "ZA" }, { "zh", "CN" }, { "zh_Hant", "TW" }, { "zu", "ZA" }, 
      { "aa", "ET" }, { "byn", "ER" }, { "eo", "DE" }, { "gez", "ET" }, { "haw", "US" }, { "iu", "CA" }, { "kw", "GB" }, { "sa", "IN" }, { "sh", "HR" }, { "sid", "ET" }, 
      { "syr", "SY" }, { "tig", "ER" }, { "tt", "RU" }, { "wal", "ET" } };
  
  static {
    int i;
    for (i = 0; i < language_territory_hack.length; i++)
      language_territory_hack_map.put(language_territory_hack[i][0], language_territory_hack[i][1]); 
  }
  
  static final Map<String, String> territory_tzid_hack_map = new HashMap<String, String>();
  
  static final String[][] territory_tzid_hack = new String[][] { 
      { "AQ", "Antarctica/McMurdo" }, { "AR", "America/Buenos_Aires" }, { "AU", "Australia/Sydney" }, { "BR", "America/Sao_Paulo" }, { "CA", "America/Toronto" }, { "CD", "Africa/Kinshasa" }, { "CL", "America/Santiago" }, { "CN", "Asia/Shanghai" }, { "EC", "America/Guayaquil" }, { "ES", "Europe/Madrid" }, 
      { "GB", "Europe/London" }, { "GL", "America/Godthab" }, { "ID", "Asia/Jakarta" }, { "ML", "Africa/Bamako" }, { "MX", "America/Mexico_City" }, { "MY", "Asia/Kuala_Lumpur" }, { "NZ", "Pacific/Auckland" }, { "PT", "Europe/Lisbon" }, { "RU", "Europe/Moscow" }, { "UA", "Europe/Kiev" }, 
      { "US", "America/New_York" }, { "UZ", "Asia/Tashkent" }, { "PF", "Pacific/Tahiti" }, { "FM", "Pacific/Kosrae" }, { "KI", "Pacific/Tarawa" }, { "KZ", "Asia/Almaty" }, { "MH", "Pacific/Majuro" }, { "MN", "Asia/Ulaanbaatar" }, { "SJ", "Arctic/Longyearbyen" }, { "UM", "Pacific/Midway" } };
  
  private boolean frozen;
  
  static {
    for (i = 0; i < territory_tzid_hack.length; i++)
      territory_tzid_hack_map.put(territory_tzid_hack[i][0], territory_tzid_hack[i][1]); 
  }
  
  public boolean isFrozen() {
    return this.frozen;
  }
  
  public GlobalizationPreferences freeze() {
    this.frozen = true;
    return this;
  }
  
  public GlobalizationPreferences cloneAsThawed() {
    try {
      GlobalizationPreferences result = (GlobalizationPreferences)clone();
      result.frozen = false;
      return result;
    } catch (CloneNotSupportedException e) {
      return null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\GlobalizationPreferences.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */