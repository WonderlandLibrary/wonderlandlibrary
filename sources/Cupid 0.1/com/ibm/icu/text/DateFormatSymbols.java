package com.ibm.icu.text;

import com.ibm.icu.impl.CalendarData;
import com.ibm.icu.impl.CalendarUtil;
import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DateFormatSymbols implements Serializable, Cloneable {
  public static final int FORMAT = 0;
  
  public static final int STANDALONE = 1;
  
  public static final int DT_CONTEXT_COUNT = 2;
  
  public static final int ABBREVIATED = 0;
  
  public static final int WIDE = 1;
  
  public static final int NARROW = 2;
  
  public static final int SHORT = 3;
  
  public static final int DT_WIDTH_COUNT = 4;
  
  static final int DT_LEAP_MONTH_PATTERN_FORMAT_WIDE = 0;
  
  static final int DT_LEAP_MONTH_PATTERN_FORMAT_ABBREV = 1;
  
  static final int DT_LEAP_MONTH_PATTERN_FORMAT_NARROW = 2;
  
  static final int DT_LEAP_MONTH_PATTERN_STANDALONE_WIDE = 3;
  
  static final int DT_LEAP_MONTH_PATTERN_STANDALONE_ABBREV = 4;
  
  static final int DT_LEAP_MONTH_PATTERN_STANDALONE_NARROW = 5;
  
  static final int DT_LEAP_MONTH_PATTERN_NUMERIC = 6;
  
  static final int DT_MONTH_PATTERN_COUNT = 7;
  
  String[] eras;
  
  String[] eraNames;
  
  String[] narrowEras;
  
  String[] months;
  
  String[] shortMonths;
  
  String[] narrowMonths;
  
  String[] standaloneMonths;
  
  String[] standaloneShortMonths;
  
  String[] standaloneNarrowMonths;
  
  String[] weekdays;
  
  String[] shortWeekdays;
  
  String[] shorterWeekdays;
  
  String[] narrowWeekdays;
  
  String[] standaloneWeekdays;
  
  String[] standaloneShortWeekdays;
  
  String[] standaloneShorterWeekdays;
  
  String[] standaloneNarrowWeekdays;
  
  String[] ampms;
  
  String[] shortQuarters;
  
  String[] quarters;
  
  String[] standaloneShortQuarters;
  
  String[] standaloneQuarters;
  
  String[] leapMonthPatterns;
  
  String[] shortYearNames;
  
  private String[][] zoneStrings;
  
  static final String patternChars = "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx";
  
  String localPatternChars;
  
  private static final long serialVersionUID = -5987973545549424702L;
  
  public DateFormatSymbols() {
    this(ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public DateFormatSymbols(Locale locale) {
    this(ULocale.forLocale(locale));
  }
  
  public DateFormatSymbols(ULocale locale) {
    this.eras = null;
    this.eraNames = null;
    this.narrowEras = null;
    this.months = null;
    this.shortMonths = null;
    this.narrowMonths = null;
    this.standaloneMonths = null;
    this.standaloneShortMonths = null;
    this.standaloneNarrowMonths = null;
    this.weekdays = null;
    this.shortWeekdays = null;
    this.shorterWeekdays = null;
    this.narrowWeekdays = null;
    this.standaloneWeekdays = null;
    this.standaloneShortWeekdays = null;
    this.standaloneShorterWeekdays = null;
    this.standaloneNarrowWeekdays = null;
    this.ampms = null;
    this.shortQuarters = null;
    this.quarters = null;
    this.standaloneShortQuarters = null;
    this.standaloneQuarters = null;
    this.leapMonthPatterns = null;
    this.shortYearNames = null;
    this.zoneStrings = (String[][])null;
    this.localPatternChars = null;
    this.capitalization = null;
    initializeData(locale, CalendarUtil.getCalendarType(locale));
  }
  
  public static DateFormatSymbols getInstance() {
    return new DateFormatSymbols();
  }
  
  public static DateFormatSymbols getInstance(Locale locale) {
    return new DateFormatSymbols(locale);
  }
  
  public static DateFormatSymbols getInstance(ULocale locale) {
    return new DateFormatSymbols(locale);
  }
  
  public static Locale[] getAvailableLocales() {
    return ICUResourceBundle.getAvailableLocales();
  }
  
  public static ULocale[] getAvailableULocales() {
    return ICUResourceBundle.getAvailableULocales();
  }
  
  private static final String[][] CALENDAR_CLASSES = new String[][] { 
      { "GregorianCalendar", "gregorian" }, { "JapaneseCalendar", "japanese" }, { "BuddhistCalendar", "buddhist" }, { "TaiwanCalendar", "roc" }, { "PersianCalendar", "persian" }, { "IslamicCalendar", "islamic" }, { "HebrewCalendar", "hebrew" }, { "ChineseCalendar", "chinese" }, { "IndianCalendar", "indian" }, { "CopticCalendar", "coptic" }, 
      { "EthiopicCalendar", "ethiopic" } };
  
  enum CapitalizationContextUsage {
    OTHER, MONTH_FORMAT, MONTH_STANDALONE, MONTH_NARROW, DAY_FORMAT, DAY_STANDALONE, DAY_NARROW, ERA_WIDE, ERA_ABBREV, ERA_NARROW, ZONE_LONG, ZONE_SHORT, METAZONE_LONG, METAZONE_SHORT;
  }
  
  private static final Map<String, CapitalizationContextUsage> contextUsageTypeMap = new HashMap<String, CapitalizationContextUsage>();
  
  Map<CapitalizationContextUsage, boolean[]> capitalization;
  
  static final int millisPerHour = 3600000;
  
  static {
    contextUsageTypeMap.put("month-format-except-narrow", CapitalizationContextUsage.MONTH_FORMAT);
    contextUsageTypeMap.put("month-standalone-except-narrow", CapitalizationContextUsage.MONTH_STANDALONE);
    contextUsageTypeMap.put("month-narrow", CapitalizationContextUsage.MONTH_NARROW);
    contextUsageTypeMap.put("day-format-except-narrow", CapitalizationContextUsage.DAY_FORMAT);
    contextUsageTypeMap.put("day-standalone-except-narrow", CapitalizationContextUsage.DAY_STANDALONE);
    contextUsageTypeMap.put("day-narrow", CapitalizationContextUsage.DAY_NARROW);
    contextUsageTypeMap.put("era-name", CapitalizationContextUsage.ERA_WIDE);
    contextUsageTypeMap.put("era-abbr", CapitalizationContextUsage.ERA_ABBREV);
    contextUsageTypeMap.put("era-narrow", CapitalizationContextUsage.ERA_NARROW);
    contextUsageTypeMap.put("zone-long", CapitalizationContextUsage.ZONE_LONG);
    contextUsageTypeMap.put("zone-short", CapitalizationContextUsage.ZONE_SHORT);
    contextUsageTypeMap.put("metazone-long", CapitalizationContextUsage.METAZONE_LONG);
    contextUsageTypeMap.put("metazone-short", CapitalizationContextUsage.METAZONE_SHORT);
  }
  
  public String[] getEras() {
    return duplicate(this.eras);
  }
  
  public void setEras(String[] newEras) {
    this.eras = duplicate(newEras);
  }
  
  public String[] getEraNames() {
    return duplicate(this.eraNames);
  }
  
  public void setEraNames(String[] newEraNames) {
    this.eraNames = duplicate(newEraNames);
  }
  
  public String[] getMonths() {
    return duplicate(this.months);
  }
  
  public String[] getMonths(int context, int width) {
    String[] returnValue = null;
    switch (context) {
      case 0:
        switch (width) {
          case 1:
            returnValue = this.months;
            break;
          case 0:
          case 3:
            returnValue = this.shortMonths;
            break;
          case 2:
            returnValue = this.narrowMonths;
            break;
        } 
        break;
      case 1:
        switch (width) {
          case 1:
            returnValue = this.standaloneMonths;
            break;
          case 0:
          case 3:
            returnValue = this.standaloneShortMonths;
            break;
          case 2:
            returnValue = this.standaloneNarrowMonths;
            break;
        } 
        break;
    } 
    return duplicate(returnValue);
  }
  
  public void setMonths(String[] newMonths) {
    this.months = duplicate(newMonths);
  }
  
  public void setMonths(String[] newMonths, int context, int width) {
    switch (context) {
      case 0:
        switch (width) {
          case 1:
            this.months = duplicate(newMonths);
            break;
          case 0:
            this.shortMonths = duplicate(newMonths);
            break;
          case 2:
            this.narrowMonths = duplicate(newMonths);
            break;
        } 
        break;
      case 1:
        switch (width) {
          case 1:
            this.standaloneMonths = duplicate(newMonths);
            break;
          case 0:
            this.standaloneShortMonths = duplicate(newMonths);
            break;
          case 2:
            this.standaloneNarrowMonths = duplicate(newMonths);
            break;
        } 
        break;
    } 
  }
  
  public String[] getShortMonths() {
    return duplicate(this.shortMonths);
  }
  
  public void setShortMonths(String[] newShortMonths) {
    this.shortMonths = duplicate(newShortMonths);
  }
  
  public String[] getWeekdays() {
    return duplicate(this.weekdays);
  }
  
  public String[] getWeekdays(int context, int width) {
    String[] returnValue = null;
    switch (context) {
      case 0:
        switch (width) {
          case 1:
            returnValue = this.weekdays;
            break;
          case 0:
            returnValue = this.shortWeekdays;
            break;
          case 3:
            returnValue = (this.shorterWeekdays != null) ? this.shorterWeekdays : this.shortWeekdays;
            break;
          case 2:
            returnValue = this.narrowWeekdays;
            break;
        } 
        break;
      case 1:
        switch (width) {
          case 1:
            returnValue = this.standaloneWeekdays;
            break;
          case 0:
            returnValue = this.standaloneShortWeekdays;
            break;
          case 3:
            returnValue = (this.standaloneShorterWeekdays != null) ? this.standaloneShorterWeekdays : this.standaloneShortWeekdays;
            break;
          case 2:
            returnValue = this.standaloneNarrowWeekdays;
            break;
        } 
        break;
    } 
    return duplicate(returnValue);
  }
  
  public void setWeekdays(String[] newWeekdays, int context, int width) {
    switch (context) {
      case 0:
        switch (width) {
          case 1:
            this.weekdays = duplicate(newWeekdays);
            break;
          case 0:
            this.shortWeekdays = duplicate(newWeekdays);
            break;
          case 3:
            this.shorterWeekdays = duplicate(newWeekdays);
            break;
          case 2:
            this.narrowWeekdays = duplicate(newWeekdays);
            break;
        } 
        break;
      case 1:
        switch (width) {
          case 1:
            this.standaloneWeekdays = duplicate(newWeekdays);
            break;
          case 0:
            this.standaloneShortWeekdays = duplicate(newWeekdays);
            break;
          case 3:
            this.standaloneShorterWeekdays = duplicate(newWeekdays);
            break;
          case 2:
            this.standaloneNarrowWeekdays = duplicate(newWeekdays);
            break;
        } 
        break;
    } 
  }
  
  public void setWeekdays(String[] newWeekdays) {
    this.weekdays = duplicate(newWeekdays);
  }
  
  public String[] getShortWeekdays() {
    return duplicate(this.shortWeekdays);
  }
  
  public void setShortWeekdays(String[] newAbbrevWeekdays) {
    this.shortWeekdays = duplicate(newAbbrevWeekdays);
  }
  
  public String[] getQuarters(int context, int width) {
    String[] returnValue = null;
    switch (context) {
      case 0:
        switch (width) {
          case 1:
            returnValue = this.quarters;
            break;
          case 0:
          case 3:
            returnValue = this.shortQuarters;
            break;
          case 2:
            returnValue = null;
            break;
        } 
        break;
      case 1:
        switch (width) {
          case 1:
            returnValue = this.standaloneQuarters;
            break;
          case 0:
          case 3:
            returnValue = this.standaloneShortQuarters;
            break;
          case 2:
            returnValue = null;
            break;
        } 
        break;
    } 
    return duplicate(returnValue);
  }
  
  public void setQuarters(String[] newQuarters, int context, int width) {
    switch (context) {
      case 0:
        switch (width) {
          case 1:
            this.quarters = duplicate(newQuarters);
            break;
          case 0:
            this.shortQuarters = duplicate(newQuarters);
            break;
        } 
        break;
      case 1:
        switch (width) {
          case 1:
            this.standaloneQuarters = duplicate(newQuarters);
            break;
          case 0:
            this.standaloneShortQuarters = duplicate(newQuarters);
            break;
        } 
        break;
    } 
  }
  
  public String[] getAmPmStrings() {
    return duplicate(this.ampms);
  }
  
  public void setAmPmStrings(String[] newAmpms) {
    this.ampms = duplicate(newAmpms);
  }
  
  public String[][] getZoneStrings() {
    if (this.zoneStrings != null)
      return duplicate(this.zoneStrings); 
    String[] tzIDs = TimeZone.getAvailableIDs();
    TimeZoneNames tznames = TimeZoneNames.getInstance(this.validLocale);
    long now = System.currentTimeMillis();
    String[][] array = new String[tzIDs.length][5];
    for (int i = 0; i < tzIDs.length; i++) {
      String canonicalID = TimeZone.getCanonicalID(tzIDs[i]);
      if (canonicalID == null)
        canonicalID = tzIDs[i]; 
      array[i][0] = tzIDs[i];
      array[i][1] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.LONG_STANDARD, now);
      array[i][2] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.SHORT_STANDARD, now);
      array[i][3] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.LONG_DAYLIGHT, now);
      array[i][4] = tznames.getDisplayName(canonicalID, TimeZoneNames.NameType.SHORT_DAYLIGHT, now);
    } 
    this.zoneStrings = array;
    return this.zoneStrings;
  }
  
  public void setZoneStrings(String[][] newZoneStrings) {
    this.zoneStrings = duplicate(newZoneStrings);
  }
  
  public String getLocalPatternChars() {
    return this.localPatternChars;
  }
  
  public void setLocalPatternChars(String newLocalPatternChars) {
    this.localPatternChars = newLocalPatternChars;
  }
  
  public Object clone() {
    try {
      DateFormatSymbols other = (DateFormatSymbols)super.clone();
      return other;
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException();
    } 
  }
  
  public int hashCode() {
    return this.requestedLocale.toString().hashCode();
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null || getClass() != obj.getClass())
      return false; 
    DateFormatSymbols that = (DateFormatSymbols)obj;
    return (Utility.arrayEquals((Object[])this.eras, that.eras) && Utility.arrayEquals((Object[])this.eraNames, that.eraNames) && Utility.arrayEquals((Object[])this.months, that.months) && Utility.arrayEquals((Object[])this.shortMonths, that.shortMonths) && Utility.arrayEquals((Object[])this.narrowMonths, that.narrowMonths) && Utility.arrayEquals((Object[])this.standaloneMonths, that.standaloneMonths) && Utility.arrayEquals((Object[])this.standaloneShortMonths, that.standaloneShortMonths) && Utility.arrayEquals((Object[])this.standaloneNarrowMonths, that.standaloneNarrowMonths) && Utility.arrayEquals((Object[])this.weekdays, that.weekdays) && Utility.arrayEquals((Object[])this.shortWeekdays, that.shortWeekdays) && Utility.arrayEquals((Object[])this.shorterWeekdays, that.shorterWeekdays) && Utility.arrayEquals((Object[])this.narrowWeekdays, that.narrowWeekdays) && Utility.arrayEquals((Object[])this.standaloneWeekdays, that.standaloneWeekdays) && Utility.arrayEquals((Object[])this.standaloneShortWeekdays, that.standaloneShortWeekdays) && Utility.arrayEquals((Object[])this.standaloneShorterWeekdays, that.standaloneShorterWeekdays) && Utility.arrayEquals((Object[])this.standaloneNarrowWeekdays, that.standaloneNarrowWeekdays) && Utility.arrayEquals((Object[])this.ampms, that.ampms) && arrayOfArrayEquals((Object[][])this.zoneStrings, (Object[][])that.zoneStrings) && this.requestedLocale.getDisplayName().equals(that.requestedLocale.getDisplayName()) && Utility.arrayEquals(this.localPatternChars, that.localPatternChars));
  }
  
  private static ICUCache<String, DateFormatSymbols> DFSCACHE = (ICUCache<String, DateFormatSymbols>)new SimpleCache();
  
  private ULocale requestedLocale;
  
  private ULocale validLocale;
  
  private ULocale actualLocale;
  
  protected void initializeData(ULocale desiredLocale, String type) {
    String key = desiredLocale.getBaseName() + "+" + type;
    DateFormatSymbols dfs = (DateFormatSymbols)DFSCACHE.get(key);
    if (dfs == null) {
      CalendarData calData = new CalendarData(desiredLocale, type);
      initializeData(desiredLocale, calData);
      if (getClass().getName().equals("com.ibm.icu.text.DateFormatSymbols")) {
        dfs = (DateFormatSymbols)clone();
        DFSCACHE.put(key, dfs);
      } 
    } else {
      initializeData(dfs);
    } 
  }
  
  void initializeData(DateFormatSymbols dfs) {
    this.eras = dfs.eras;
    this.eraNames = dfs.eraNames;
    this.narrowEras = dfs.narrowEras;
    this.months = dfs.months;
    this.shortMonths = dfs.shortMonths;
    this.narrowMonths = dfs.narrowMonths;
    this.standaloneMonths = dfs.standaloneMonths;
    this.standaloneShortMonths = dfs.standaloneShortMonths;
    this.standaloneNarrowMonths = dfs.standaloneNarrowMonths;
    this.weekdays = dfs.weekdays;
    this.shortWeekdays = dfs.shortWeekdays;
    this.shorterWeekdays = dfs.shorterWeekdays;
    this.narrowWeekdays = dfs.narrowWeekdays;
    this.standaloneWeekdays = dfs.standaloneWeekdays;
    this.standaloneShortWeekdays = dfs.standaloneShortWeekdays;
    this.standaloneShorterWeekdays = dfs.standaloneShorterWeekdays;
    this.standaloneNarrowWeekdays = dfs.standaloneNarrowWeekdays;
    this.ampms = dfs.ampms;
    this.shortQuarters = dfs.shortQuarters;
    this.quarters = dfs.quarters;
    this.standaloneShortQuarters = dfs.standaloneShortQuarters;
    this.standaloneQuarters = dfs.standaloneQuarters;
    this.leapMonthPatterns = dfs.leapMonthPatterns;
    this.shortYearNames = dfs.shortYearNames;
    this.zoneStrings = dfs.zoneStrings;
    this.localPatternChars = dfs.localPatternChars;
    this.capitalization = dfs.capitalization;
    this.actualLocale = dfs.actualLocale;
    this.validLocale = dfs.validLocale;
    this.requestedLocale = dfs.requestedLocale;
  }
  
  protected void initializeData(ULocale desiredLocale, CalendarData calData) {
    this.eras = calData.getEras("abbreviated");
    this.eraNames = calData.getEras("wide");
    this.narrowEras = calData.getEras("narrow");
    this.months = calData.getStringArray("monthNames", "wide");
    this.shortMonths = calData.getStringArray("monthNames", "abbreviated");
    this.narrowMonths = calData.getStringArray("monthNames", "narrow");
    this.standaloneMonths = calData.getStringArray("monthNames", "stand-alone", "wide");
    this.standaloneShortMonths = calData.getStringArray("monthNames", "stand-alone", "abbreviated");
    this.standaloneNarrowMonths = calData.getStringArray("monthNames", "stand-alone", "narrow");
    String[] lWeekdays = calData.getStringArray("dayNames", "wide");
    this.weekdays = new String[8];
    this.weekdays[0] = "";
    System.arraycopy(lWeekdays, 0, this.weekdays, 1, lWeekdays.length);
    String[] aWeekdays = calData.getStringArray("dayNames", "abbreviated");
    this.shortWeekdays = new String[8];
    this.shortWeekdays[0] = "";
    System.arraycopy(aWeekdays, 0, this.shortWeekdays, 1, aWeekdays.length);
    String[] sWeekdays = calData.getStringArray("dayNames", "short");
    this.shorterWeekdays = new String[8];
    this.shorterWeekdays[0] = "";
    System.arraycopy(sWeekdays, 0, this.shorterWeekdays, 1, sWeekdays.length);
    String[] nWeekdays = null;
    try {
      nWeekdays = calData.getStringArray("dayNames", "narrow");
    } catch (MissingResourceException e) {
      try {
        nWeekdays = calData.getStringArray("dayNames", "stand-alone", "narrow");
      } catch (MissingResourceException e1) {
        nWeekdays = calData.getStringArray("dayNames", "abbreviated");
      } 
    } 
    this.narrowWeekdays = new String[8];
    this.narrowWeekdays[0] = "";
    System.arraycopy(nWeekdays, 0, this.narrowWeekdays, 1, nWeekdays.length);
    String[] swWeekdays = null;
    swWeekdays = calData.getStringArray("dayNames", "stand-alone", "wide");
    this.standaloneWeekdays = new String[8];
    this.standaloneWeekdays[0] = "";
    System.arraycopy(swWeekdays, 0, this.standaloneWeekdays, 1, swWeekdays.length);
    String[] saWeekdays = null;
    saWeekdays = calData.getStringArray("dayNames", "stand-alone", "abbreviated");
    this.standaloneShortWeekdays = new String[8];
    this.standaloneShortWeekdays[0] = "";
    System.arraycopy(saWeekdays, 0, this.standaloneShortWeekdays, 1, saWeekdays.length);
    String[] ssWeekdays = null;
    ssWeekdays = calData.getStringArray("dayNames", "stand-alone", "short");
    this.standaloneShorterWeekdays = new String[8];
    this.standaloneShorterWeekdays[0] = "";
    System.arraycopy(ssWeekdays, 0, this.standaloneShorterWeekdays, 1, ssWeekdays.length);
    String[] snWeekdays = null;
    snWeekdays = calData.getStringArray("dayNames", "stand-alone", "narrow");
    this.standaloneNarrowWeekdays = new String[8];
    this.standaloneNarrowWeekdays[0] = "";
    System.arraycopy(snWeekdays, 0, this.standaloneNarrowWeekdays, 1, snWeekdays.length);
    this.ampms = calData.getStringArray("AmPmMarkers");
    this.quarters = calData.getStringArray("quarters", "wide");
    this.shortQuarters = calData.getStringArray("quarters", "abbreviated");
    this.standaloneQuarters = calData.getStringArray("quarters", "stand-alone", "wide");
    this.standaloneShortQuarters = calData.getStringArray("quarters", "stand-alone", "abbreviated");
    ICUResourceBundle monthPatternsBundle = null;
    try {
      monthPatternsBundle = calData.get("monthPatterns");
    } catch (MissingResourceException e) {
      monthPatternsBundle = null;
    } 
    if (monthPatternsBundle != null) {
      this.leapMonthPatterns = new String[7];
      this.leapMonthPatterns[0] = calData.get("monthPatterns", "wide").get("leap").getString();
      this.leapMonthPatterns[1] = calData.get("monthPatterns", "abbreviated").get("leap").getString();
      this.leapMonthPatterns[2] = calData.get("monthPatterns", "narrow").get("leap").getString();
      this.leapMonthPatterns[3] = calData.get("monthPatterns", "stand-alone", "wide").get("leap").getString();
      this.leapMonthPatterns[4] = calData.get("monthPatterns", "stand-alone", "abbreviated").get("leap").getString();
      this.leapMonthPatterns[5] = calData.get("monthPatterns", "stand-alone", "narrow").get("leap").getString();
      this.leapMonthPatterns[6] = calData.get("monthPatterns", "numeric", "all").get("leap").getString();
    } 
    ICUResourceBundle cyclicNameSetsBundle = null;
    try {
      cyclicNameSetsBundle = calData.get("cyclicNameSets");
    } catch (MissingResourceException e) {
      cyclicNameSetsBundle = null;
    } 
    if (cyclicNameSetsBundle != null)
      this.shortYearNames = calData.get("cyclicNameSets", "years", "format", "abbreviated").getStringArray(); 
    this.requestedLocale = desiredLocale;
    ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", desiredLocale);
    this.localPatternChars = "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx";
    ULocale uloc = rb.getULocale();
    setLocale(uloc, uloc);
    this.capitalization = (Map)new HashMap<CapitalizationContextUsage, boolean>();
    boolean[] noTransforms = new boolean[2];
    noTransforms[0] = false;
    noTransforms[1] = false;
    CapitalizationContextUsage[] allUsages = CapitalizationContextUsage.values();
    for (CapitalizationContextUsage usage : allUsages)
      this.capitalization.put(usage, noTransforms); 
    UResourceBundle contextTransformsBundle = null;
    try {
      ICUResourceBundle iCUResourceBundle = rb.getWithFallback("contextTransforms");
    } catch (MissingResourceException e) {
      contextTransformsBundle = null;
    } 
    if (contextTransformsBundle != null) {
      UResourceBundleIterator ctIterator = contextTransformsBundle.getIterator();
      while (ctIterator.hasNext()) {
        UResourceBundle contextTransformUsage = ctIterator.next();
        int[] intVector = contextTransformUsage.getIntVector();
        if (intVector.length >= 2) {
          String usageKey = contextTransformUsage.getKey();
          CapitalizationContextUsage usage = contextUsageTypeMap.get(usageKey);
          if (usage != null) {
            boolean[] transforms = new boolean[2];
            transforms[0] = (intVector[0] != 0);
            transforms[1] = (intVector[1] != 0);
            this.capitalization.put(usage, transforms);
          } 
        } 
      } 
    } 
  }
  
  private static final boolean arrayOfArrayEquals(Object[][] aa1, Object[][] aa2) {
    if (aa1 == aa2)
      return true; 
    if (aa1 == null || aa2 == null)
      return false; 
    if (aa1.length != aa2.length)
      return false; 
    boolean equal = true;
    for (int i = 0; i < aa1.length; i++) {
      equal = Utility.arrayEquals(aa1[i], aa2[i]);
      if (!equal)
        break; 
    } 
    return equal;
  }
  
  private final String[] duplicate(String[] srcArray) {
    return (String[])srcArray.clone();
  }
  
  private final String[][] duplicate(String[][] srcArray) {
    String[][] aCopy = new String[srcArray.length][];
    for (int i = 0; i < srcArray.length; i++)
      aCopy[i] = duplicate(srcArray[i]); 
    return aCopy;
  }
  
  public DateFormatSymbols(Calendar cal, Locale locale) {
    this.eras = null;
    this.eraNames = null;
    this.narrowEras = null;
    this.months = null;
    this.shortMonths = null;
    this.narrowMonths = null;
    this.standaloneMonths = null;
    this.standaloneShortMonths = null;
    this.standaloneNarrowMonths = null;
    this.weekdays = null;
    this.shortWeekdays = null;
    this.shorterWeekdays = null;
    this.narrowWeekdays = null;
    this.standaloneWeekdays = null;
    this.standaloneShortWeekdays = null;
    this.standaloneShorterWeekdays = null;
    this.standaloneNarrowWeekdays = null;
    this.ampms = null;
    this.shortQuarters = null;
    this.quarters = null;
    this.standaloneShortQuarters = null;
    this.standaloneQuarters = null;
    this.leapMonthPatterns = null;
    this.shortYearNames = null;
    this.zoneStrings = (String[][])null;
    this.localPatternChars = null;
    this.capitalization = null;
    initializeData(ULocale.forLocale(locale), cal.getType());
  }
  
  public DateFormatSymbols(Calendar cal, ULocale locale) {
    this.eras = null;
    this.eraNames = null;
    this.narrowEras = null;
    this.months = null;
    this.shortMonths = null;
    this.narrowMonths = null;
    this.standaloneMonths = null;
    this.standaloneShortMonths = null;
    this.standaloneNarrowMonths = null;
    this.weekdays = null;
    this.shortWeekdays = null;
    this.shorterWeekdays = null;
    this.narrowWeekdays = null;
    this.standaloneWeekdays = null;
    this.standaloneShortWeekdays = null;
    this.standaloneShorterWeekdays = null;
    this.standaloneNarrowWeekdays = null;
    this.ampms = null;
    this.shortQuarters = null;
    this.quarters = null;
    this.standaloneShortQuarters = null;
    this.standaloneQuarters = null;
    this.leapMonthPatterns = null;
    this.shortYearNames = null;
    this.zoneStrings = (String[][])null;
    this.localPatternChars = null;
    this.capitalization = null;
    initializeData(locale, cal.getType());
  }
  
  public DateFormatSymbols(Class<? extends Calendar> calendarClass, Locale locale) {
    this(calendarClass, ULocale.forLocale(locale));
  }
  
  public DateFormatSymbols(Class<? extends Calendar> calendarClass, ULocale locale) {
    this.eras = null;
    this.eraNames = null;
    this.narrowEras = null;
    this.months = null;
    this.shortMonths = null;
    this.narrowMonths = null;
    this.standaloneMonths = null;
    this.standaloneShortMonths = null;
    this.standaloneNarrowMonths = null;
    this.weekdays = null;
    this.shortWeekdays = null;
    this.shorterWeekdays = null;
    this.narrowWeekdays = null;
    this.standaloneWeekdays = null;
    this.standaloneShortWeekdays = null;
    this.standaloneShorterWeekdays = null;
    this.standaloneNarrowWeekdays = null;
    this.ampms = null;
    this.shortQuarters = null;
    this.quarters = null;
    this.standaloneShortQuarters = null;
    this.standaloneQuarters = null;
    this.leapMonthPatterns = null;
    this.shortYearNames = null;
    this.zoneStrings = (String[][])null;
    this.localPatternChars = null;
    this.capitalization = null;
    String fullName = calendarClass.getName();
    int lastDot = fullName.lastIndexOf('.');
    String className = fullName.substring(lastDot + 1);
    String calType = null;
    for (String[] calClassInfo : CALENDAR_CLASSES) {
      if (calClassInfo[0].equals(className)) {
        calType = calClassInfo[1];
        break;
      } 
    } 
    if (calType == null)
      calType = className.replaceAll("Calendar", "").toLowerCase(Locale.ENGLISH); 
    initializeData(locale, calType);
  }
  
  public DateFormatSymbols(ResourceBundle bundle, Locale locale) {
    this(bundle, ULocale.forLocale(locale));
  }
  
  public DateFormatSymbols(ResourceBundle bundle, ULocale locale) {
    this.eras = null;
    this.eraNames = null;
    this.narrowEras = null;
    this.months = null;
    this.shortMonths = null;
    this.narrowMonths = null;
    this.standaloneMonths = null;
    this.standaloneShortMonths = null;
    this.standaloneNarrowMonths = null;
    this.weekdays = null;
    this.shortWeekdays = null;
    this.shorterWeekdays = null;
    this.narrowWeekdays = null;
    this.standaloneWeekdays = null;
    this.standaloneShortWeekdays = null;
    this.standaloneShorterWeekdays = null;
    this.standaloneNarrowWeekdays = null;
    this.ampms = null;
    this.shortQuarters = null;
    this.quarters = null;
    this.standaloneShortQuarters = null;
    this.standaloneQuarters = null;
    this.leapMonthPatterns = null;
    this.shortYearNames = null;
    this.zoneStrings = (String[][])null;
    this.localPatternChars = null;
    this.capitalization = null;
    initializeData(locale, new CalendarData((ICUResourceBundle)bundle, CalendarUtil.getCalendarType(locale)));
  }
  
  public static ResourceBundle getDateFormatBundle(Class<? extends Calendar> calendarClass, Locale locale) throws MissingResourceException {
    return null;
  }
  
  public static ResourceBundle getDateFormatBundle(Class<? extends Calendar> calendarClass, ULocale locale) throws MissingResourceException {
    return null;
  }
  
  public static ResourceBundle getDateFormatBundle(Calendar cal, Locale locale) throws MissingResourceException {
    return null;
  }
  
  public static ResourceBundle getDateFormatBundle(Calendar cal, ULocale locale) throws MissingResourceException {
    return null;
  }
  
  public final ULocale getLocale(ULocale.Type type) {
    return (type == ULocale.ACTUAL_LOCALE) ? this.actualLocale : this.validLocale;
  }
  
  final void setLocale(ULocale valid, ULocale actual) {
    if (((valid == null) ? true : false) != ((actual == null) ? true : false))
      throw new IllegalArgumentException(); 
    this.validLocale = valid;
    this.actualLocale = actual;
  }
  
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\DateFormatSymbols.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */