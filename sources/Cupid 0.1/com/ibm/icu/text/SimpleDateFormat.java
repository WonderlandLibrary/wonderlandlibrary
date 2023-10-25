package com.ibm.icu.text;

import com.ibm.icu.impl.CalendarData;
import com.ibm.icu.impl.DateNumberFormat;
import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.BasicTimeZone;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.HebrewCalendar;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.TimeZoneTransition;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

public class SimpleDateFormat extends DateFormat {
  private static final long serialVersionUID = 4774881970558875024L;
  
  static final int currentSerialVersion = 2;
  
  static boolean DelayedHebrewMonthCheck = false;
  
  private static final int[] CALENDAR_FIELD_TO_LEVEL = new int[] { 
      0, 10, 20, 20, 30, 30, 20, 30, 30, 40, 
      50, 50, 60, 70, 80, 0, 0, 10, 30, 10, 
      0, 40 };
  
  private static final int[] PATTERN_CHAR_TO_LEVEL = new int[] { 
      -1, 40, -1, -1, 20, 30, 30, 0, 50, -1, 
      -1, 50, 20, 20, -1, 0, -1, 20, -1, 80, 
      -1, 10, 0, 30, 0, 10, 0, -1, -1, -1, 
      -1, -1, -1, 40, -1, 30, 30, 30, -1, 0, 
      50, -1, -1, 50, -1, 60, -1, -1, -1, 20, 
      -1, 70, -1, 10, 0, 20, 0, 10, 0, -1, 
      -1, -1, -1, -1 };
  
  private static final int HEBREW_CAL_CUR_MILLENIUM_START_YEAR = 5000;
  
  private static final int HEBREW_CAL_CUR_MILLENIUM_END_YEAR = 6000;
  
  private int serialVersionOnStream = 2;
  
  private String pattern;
  
  private String override;
  
  private HashMap<String, NumberFormat> numberFormatters;
  
  private HashMap<Character, String> overrideMap;
  
  private DateFormatSymbols formatData;
  
  private transient ULocale locale;
  
  private Date defaultCenturyStart;
  
  private transient int defaultCenturyStartYear;
  
  private transient long defaultCenturyBase;
  
  private transient TimeZoneFormat.TimeType tztype = TimeZoneFormat.TimeType.UNKNOWN;
  
  private static final int millisPerHour = 3600000;
  
  private static final int ISOSpecialEra = -32000;
  
  private static final String SUPPRESS_NEGATIVE_PREFIX = "꬀";
  
  private transient boolean useFastFormat;
  
  private volatile TimeZoneFormat tzFormat;
  
  private transient DisplayContext capitalizationSetting;
  
  private enum ContextValue {
    UNKNOWN, CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE, CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE, CAPITALIZATION_FOR_UI_LIST_OR_MENU, CAPITALIZATION_FOR_STANDALONE;
  }
  
  public SimpleDateFormat() {
    this(getDefaultPattern(), (DateFormatSymbols)null, (Calendar)null, (NumberFormat)null, (ULocale)null, true, (String)null);
  }
  
  public SimpleDateFormat(String pattern) {
    this(pattern, (DateFormatSymbols)null, (Calendar)null, (NumberFormat)null, (ULocale)null, true, (String)null);
  }
  
  public SimpleDateFormat(String pattern, Locale loc) {
    this(pattern, (DateFormatSymbols)null, (Calendar)null, (NumberFormat)null, ULocale.forLocale(loc), true, (String)null);
  }
  
  public SimpleDateFormat(String pattern, ULocale loc) {
    this(pattern, (DateFormatSymbols)null, (Calendar)null, (NumberFormat)null, loc, true, (String)null);
  }
  
  public SimpleDateFormat(String pattern, String override, ULocale loc) {
    this(pattern, (DateFormatSymbols)null, (Calendar)null, (NumberFormat)null, loc, false, override);
  }
  
  public SimpleDateFormat(String pattern, DateFormatSymbols formatData) {
    this(pattern, (DateFormatSymbols)formatData.clone(), (Calendar)null, (NumberFormat)null, (ULocale)null, true, (String)null);
  }
  
  public SimpleDateFormat(String pattern, DateFormatSymbols formatData, ULocale loc) {
    this(pattern, (DateFormatSymbols)formatData.clone(), (Calendar)null, (NumberFormat)null, loc, true, (String)null);
  }
  
  SimpleDateFormat(String pattern, DateFormatSymbols formatData, Calendar calendar, ULocale locale, boolean useFastFormat, String override) {
    this(pattern, (DateFormatSymbols)formatData.clone(), (Calendar)calendar.clone(), (NumberFormat)null, locale, useFastFormat, override);
  }
  
  private SimpleDateFormat(String pattern, DateFormatSymbols formatData, Calendar calendar, NumberFormat numberFormat, ULocale locale, boolean useFastFormat, String override) {
    this.pattern = pattern;
    this.formatData = formatData;
    this.calendar = calendar;
    this.numberFormat = numberFormat;
    this.locale = locale;
    this.useFastFormat = useFastFormat;
    this.override = override;
    initialize();
  }
  
  public static SimpleDateFormat getInstance(Calendar.FormatConfiguration formatConfig) {
    String ostr = formatConfig.getOverrideString();
    boolean useFast = (ostr != null && ostr.length() > 0);
    return new SimpleDateFormat(formatConfig.getPatternString(), formatConfig.getDateFormatSymbols(), formatConfig.getCalendar(), null, formatConfig.getLocale(), useFast, formatConfig.getOverrideString());
  }
  
  private void initialize() {
    if (this.locale == null)
      this.locale = ULocale.getDefault(ULocale.Category.FORMAT); 
    if (this.formatData == null)
      this.formatData = new DateFormatSymbols(this.locale); 
    if (this.calendar == null)
      this.calendar = Calendar.getInstance(this.locale); 
    if (this.numberFormat == null) {
      NumberingSystem ns = NumberingSystem.getInstance(this.locale);
      if (ns.isAlgorithmic()) {
        this.numberFormat = NumberFormat.getInstance(this.locale);
      } else {
        String digitString = ns.getDescription();
        String nsName = ns.getName();
        this.numberFormat = (NumberFormat)new DateNumberFormat(this.locale, digitString, nsName);
      } 
    } 
    this.defaultCenturyBase = System.currentTimeMillis();
    setLocale(this.calendar.getLocale(ULocale.VALID_LOCALE), this.calendar.getLocale(ULocale.ACTUAL_LOCALE));
    initLocalZeroPaddingNumberFormat();
    if (this.override != null)
      initNumberFormatters(this.locale); 
    this.capitalizationSetting = DisplayContext.CAPITALIZATION_NONE;
  }
  
  private synchronized void initializeTimeZoneFormat(boolean bForceUpdate) {
    if (bForceUpdate || this.tzFormat == null) {
      this.tzFormat = TimeZoneFormat.getInstance(this.locale);
      String digits = null;
      if (this.numberFormat instanceof DecimalFormat) {
        DecimalFormatSymbols decsym = ((DecimalFormat)this.numberFormat).getDecimalFormatSymbols();
        digits = new String(decsym.getDigits());
      } else if (this.numberFormat instanceof DateNumberFormat) {
        digits = new String(((DateNumberFormat)this.numberFormat).getDigits());
      } 
      if (digits != null && 
        !this.tzFormat.getGMTOffsetDigits().equals(digits)) {
        if (this.tzFormat.isFrozen())
          this.tzFormat = this.tzFormat.cloneAsThawed(); 
        this.tzFormat.setGMTOffsetDigits(digits);
      } 
    } 
  }
  
  private TimeZoneFormat tzFormat() {
    if (this.tzFormat == null)
      initializeTimeZoneFormat(false); 
    return this.tzFormat;
  }
  
  private static ULocale cachedDefaultLocale = null;
  
  private static String cachedDefaultPattern = null;
  
  private static final String FALLBACKPATTERN = "yy/MM/dd HH:mm";
  
  private static final int PATTERN_CHAR_BASE = 64;
  
  private static synchronized String getDefaultPattern() {
    ULocale defaultLocale = ULocale.getDefault(ULocale.Category.FORMAT);
    if (!defaultLocale.equals(cachedDefaultLocale)) {
      cachedDefaultLocale = defaultLocale;
      Calendar cal = Calendar.getInstance(cachedDefaultLocale);
      try {
        CalendarData calData = new CalendarData(cachedDefaultLocale, cal.getType());
        String[] dateTimePatterns = calData.getDateTimePatterns();
        int glueIndex = 8;
        if (dateTimePatterns.length >= 13)
          glueIndex += 4; 
        cachedDefaultPattern = MessageFormat.format(dateTimePatterns[glueIndex], new Object[] { dateTimePatterns[3], dateTimePatterns[7] });
      } catch (MissingResourceException e) {
        cachedDefaultPattern = "yy/MM/dd HH:mm";
      } 
    } 
    return cachedDefaultPattern;
  }
  
  private void parseAmbiguousDatesAsAfter(Date startDate) {
    this.defaultCenturyStart = startDate;
    this.calendar.setTime(startDate);
    this.defaultCenturyStartYear = this.calendar.get(1);
  }
  
  private void initializeDefaultCenturyStart(long baseTime) {
    this.defaultCenturyBase = baseTime;
    Calendar tmpCal = (Calendar)this.calendar.clone();
    tmpCal.setTimeInMillis(baseTime);
    tmpCal.add(1, -80);
    this.defaultCenturyStart = tmpCal.getTime();
    this.defaultCenturyStartYear = tmpCal.get(1);
  }
  
  private Date getDefaultCenturyStart() {
    if (this.defaultCenturyStart == null)
      initializeDefaultCenturyStart(this.defaultCenturyBase); 
    return this.defaultCenturyStart;
  }
  
  private int getDefaultCenturyStartYear() {
    if (this.defaultCenturyStart == null)
      initializeDefaultCenturyStart(this.defaultCenturyBase); 
    return this.defaultCenturyStartYear;
  }
  
  public void set2DigitYearStart(Date startDate) {
    parseAmbiguousDatesAsAfter(startDate);
  }
  
  public Date get2DigitYearStart() {
    return getDefaultCenturyStart();
  }
  
  public StringBuffer format(Calendar cal, StringBuffer toAppendTo, FieldPosition pos) {
    TimeZone backupTZ = null;
    if (cal != this.calendar && !cal.getType().equals(this.calendar.getType())) {
      this.calendar.setTimeInMillis(cal.getTimeInMillis());
      backupTZ = this.calendar.getTimeZone();
      this.calendar.setTimeZone(cal.getTimeZone());
      cal = this.calendar;
    } 
    StringBuffer result = format(cal, this.capitalizationSetting, toAppendTo, pos, (List<FieldPosition>)null);
    if (backupTZ != null)
      this.calendar.setTimeZone(backupTZ); 
    return result;
  }
  
  private StringBuffer format(Calendar cal, DisplayContext capitalizationContext, StringBuffer toAppendTo, FieldPosition pos, List<FieldPosition> attributes) {
    pos.setBeginIndex(0);
    pos.setEndIndex(0);
    Object[] items = getPatternItems();
    for (int i = 0; i < items.length; i++) {
      if (items[i] instanceof String) {
        toAppendTo.append((String)items[i]);
      } else {
        PatternItem item = (PatternItem)items[i];
        int start = 0;
        if (attributes != null)
          start = toAppendTo.length(); 
        if (this.useFastFormat) {
          subFormat(toAppendTo, item.type, item.length, toAppendTo.length(), i, capitalizationContext, pos, cal);
        } else {
          toAppendTo.append(subFormat(item.type, item.length, toAppendTo.length(), i, capitalizationContext, pos, cal));
        } 
        if (attributes != null) {
          int end = toAppendTo.length();
          if (end - start > 0) {
            DateFormat.Field attr = patternCharToDateFormatField(item.type);
            FieldPosition fp = new FieldPosition(attr);
            fp.setBeginIndex(start);
            fp.setEndIndex(end);
            attributes.add(fp);
          } 
        } 
      } 
    } 
    return toAppendTo;
  }
  
  private static final int[] PATTERN_CHAR_TO_INDEX = new int[] { 
      -1, 22, -1, -1, 10, 9, 11, 0, 5, -1, 
      -1, 16, 26, 2, -1, 31, -1, 27, -1, 8, 
      -1, 30, 29, 13, 32, 18, 23, -1, -1, -1, 
      -1, -1, -1, 14, -1, 25, 3, 19, -1, 21, 
      15, -1, -1, 4, -1, 6, -1, -1, -1, 28, 
      -1, 7, -1, 20, 24, 12, 33, 1, 17, -1, 
      -1, -1, -1, -1 };
  
  private static final int[] PATTERN_INDEX_TO_CALENDAR_FIELD = new int[] { 
      0, 1, 2, 5, 11, 11, 12, 13, 14, 7, 
      6, 8, 3, 4, 9, 10, 10, 15, 17, 18, 
      19, 20, 21, 15, 15, 18, 2, 2, 2, 15, 
      1, 15, 15, 15 };
  
  private static final int[] PATTERN_INDEX_TO_DATE_FORMAT_FIELD = new int[] { 
      0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
      10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 
      20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 
      30, 31, 32, 33 };
  
  private static final DateFormat.Field[] PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE = new DateFormat.Field[] { 
      DateFormat.Field.ERA, DateFormat.Field.YEAR, DateFormat.Field.MONTH, DateFormat.Field.DAY_OF_MONTH, DateFormat.Field.HOUR_OF_DAY1, DateFormat.Field.HOUR_OF_DAY0, DateFormat.Field.MINUTE, DateFormat.Field.SECOND, DateFormat.Field.MILLISECOND, DateFormat.Field.DAY_OF_WEEK, 
      DateFormat.Field.DAY_OF_YEAR, DateFormat.Field.DAY_OF_WEEK_IN_MONTH, DateFormat.Field.WEEK_OF_YEAR, DateFormat.Field.WEEK_OF_MONTH, DateFormat.Field.AM_PM, DateFormat.Field.HOUR1, DateFormat.Field.HOUR0, DateFormat.Field.TIME_ZONE, DateFormat.Field.YEAR_WOY, DateFormat.Field.DOW_LOCAL, 
      DateFormat.Field.EXTENDED_YEAR, DateFormat.Field.JULIAN_DAY, DateFormat.Field.MILLISECONDS_IN_DAY, DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE, DateFormat.Field.DAY_OF_WEEK, DateFormat.Field.MONTH, DateFormat.Field.QUARTER, DateFormat.Field.QUARTER, DateFormat.Field.TIME_ZONE, 
      DateFormat.Field.YEAR, DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE };
  
  protected DateFormat.Field patternCharToDateFormatField(char ch) {
    int patternCharIndex = -1;
    if ('A' <= ch && ch <= 'z')
      patternCharIndex = PATTERN_CHAR_TO_INDEX[ch - 64]; 
    if (patternCharIndex != -1)
      return PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE[patternCharIndex]; 
    return null;
  }
  
  protected String subFormat(char ch, int count, int beginOffset, FieldPosition pos, DateFormatSymbols fmtData, Calendar cal) throws IllegalArgumentException {
    return subFormat(ch, count, beginOffset, 0, DisplayContext.CAPITALIZATION_NONE, pos, cal);
  }
  
  protected String subFormat(char ch, int count, int beginOffset, int fieldNum, DisplayContext capitalizationContext, FieldPosition pos, Calendar cal) {
    StringBuffer buf = new StringBuffer();
    subFormat(buf, ch, count, beginOffset, fieldNum, capitalizationContext, pos, cal);
    return buf.toString();
  }
  
  protected void subFormat(StringBuffer buf, char ch, int count, int beginOffset, int fieldNum, DisplayContext capitalizationContext, FieldPosition pos, Calendar cal) {
    int isLeapMonth;
    StringBuffer monthNumber;
    FieldPosition p;
    String[] monthNumberStrings;
    int maxIntCount = Integer.MAX_VALUE;
    int bufstart = buf.length();
    TimeZone tz = cal.getTimeZone();
    long date = cal.getTimeInMillis();
    String result = null;
    int patternCharIndex = -1;
    if ('A' <= ch && ch <= 'z')
      patternCharIndex = PATTERN_CHAR_TO_INDEX[ch - 64]; 
    if (patternCharIndex == -1) {
      if (ch == 'l')
        return; 
      throw new IllegalArgumentException("Illegal pattern character '" + ch + "' in \"" + this.pattern + '"');
    } 
    int field = PATTERN_INDEX_TO_CALENDAR_FIELD[patternCharIndex];
    int value = cal.get(field);
    NumberFormat currentNumberFormat = getNumberFormat(ch);
    DateFormatSymbols.CapitalizationContextUsage capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.OTHER;
    switch (patternCharIndex) {
      case 0:
        if (cal.getType().equals("chinese")) {
          zeroPaddingNumber(currentNumberFormat, buf, value, 1, 9);
          break;
        } 
        if (count == 5) {
          safeAppend(this.formatData.narrowEras, value, buf);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.ERA_NARROW;
          break;
        } 
        if (count == 4) {
          safeAppend(this.formatData.eraNames, value, buf);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.ERA_WIDE;
          break;
        } 
        safeAppend(this.formatData.eras, value, buf);
        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.ERA_ABBREV;
        break;
      case 30:
        if (this.formatData.shortYearNames != null && value <= this.formatData.shortYearNames.length) {
          safeAppend(this.formatData.shortYearNames, value - 1, buf);
          break;
        } 
      case 1:
      case 18:
        if (this.override != null && (this.override.compareTo("hebr") == 0 || this.override.indexOf("y=hebr") >= 0) && value > 5000 && value < 6000)
          value -= 5000; 
        if (count == 2) {
          zeroPaddingNumber(currentNumberFormat, buf, value, 2, 2);
          break;
        } 
        zeroPaddingNumber(currentNumberFormat, buf, value, count, 2147483647);
        break;
      case 2:
      case 26:
        if (cal.getType().equals("hebrew")) {
          boolean isLeap = HebrewCalendar.isLeapYear(cal.get(1));
          if (isLeap && value == 6 && count >= 3)
            value = 13; 
          if (!isLeap && value >= 6 && count < 3)
            value--; 
        } 
        isLeapMonth = (this.formatData.leapMonthPatterns != null && this.formatData.leapMonthPatterns.length >= 7) ? cal.get(22) : 0;
        if (count == 5) {
          if (patternCharIndex == 2) {
            safeAppendWithMonthPattern(this.formatData.narrowMonths, value, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[2] : null);
          } else {
            safeAppendWithMonthPattern(this.formatData.standaloneNarrowMonths, value, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[5] : null);
          } 
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.MONTH_NARROW;
          break;
        } 
        if (count == 4) {
          if (patternCharIndex == 2) {
            safeAppendWithMonthPattern(this.formatData.months, value, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[0] : null);
            capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.MONTH_FORMAT;
            break;
          } 
          safeAppendWithMonthPattern(this.formatData.standaloneMonths, value, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[3] : null);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.MONTH_STANDALONE;
          break;
        } 
        if (count == 3) {
          if (patternCharIndex == 2) {
            safeAppendWithMonthPattern(this.formatData.shortMonths, value, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[1] : null);
            capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.MONTH_FORMAT;
            break;
          } 
          safeAppendWithMonthPattern(this.formatData.standaloneShortMonths, value, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[4] : null);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.MONTH_STANDALONE;
          break;
        } 
        monthNumber = new StringBuffer();
        zeroPaddingNumber(currentNumberFormat, monthNumber, value + 1, count, 2147483647);
        monthNumberStrings = new String[1];
        monthNumberStrings[0] = monthNumber.toString();
        safeAppendWithMonthPattern(monthNumberStrings, 0, buf, (isLeapMonth != 0) ? this.formatData.leapMonthPatterns[6] : null);
        break;
      case 4:
        if (value == 0) {
          zeroPaddingNumber(currentNumberFormat, buf, cal.getMaximum(11) + 1, count, 2147483647);
          break;
        } 
        zeroPaddingNumber(currentNumberFormat, buf, value, count, 2147483647);
        break;
      case 8:
        this.numberFormat.setMinimumIntegerDigits(Math.min(3, count));
        this.numberFormat.setMaximumIntegerDigits(2147483647);
        if (count == 1) {
          value /= 100;
        } else if (count == 2) {
          value /= 10;
        } 
        p = new FieldPosition(-1);
        this.numberFormat.format(value, buf, p);
        if (count > 3) {
          this.numberFormat.setMinimumIntegerDigits(count - 3);
          this.numberFormat.format(0L, buf, p);
        } 
        break;
      case 19:
        if (count < 3) {
          zeroPaddingNumber(currentNumberFormat, buf, value, count, 2147483647);
          break;
        } 
        value = cal.get(7);
      case 9:
        if (count == 5) {
          safeAppend(this.formatData.narrowWeekdays, value, buf);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_NARROW;
          break;
        } 
        if (count == 4) {
          safeAppend(this.formatData.weekdays, value, buf);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_FORMAT;
          break;
        } 
        if (count == 6 && this.formatData.shorterWeekdays != null) {
          safeAppend(this.formatData.shorterWeekdays, value, buf);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_FORMAT;
          break;
        } 
        safeAppend(this.formatData.shortWeekdays, value, buf);
        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_FORMAT;
        break;
      case 14:
        safeAppend(this.formatData.ampms, value, buf);
        break;
      case 15:
        if (value == 0) {
          zeroPaddingNumber(currentNumberFormat, buf, cal.getLeastMaximum(10) + 1, count, 2147483647);
          break;
        } 
        zeroPaddingNumber(currentNumberFormat, buf, value, count, 2147483647);
        break;
      case 17:
        if (count < 4) {
          result = tzFormat().format(TimeZoneFormat.Style.SPECIFIC_SHORT, tz, date);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.METAZONE_SHORT;
        } else {
          result = tzFormat().format(TimeZoneFormat.Style.SPECIFIC_LONG, tz, date);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.METAZONE_LONG;
        } 
        buf.append(result);
        break;
      case 23:
        if (count < 4) {
          result = tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_LOCAL_FULL, tz, date);
        } else if (count == 5) {
          result = tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_FULL, tz, date);
        } else {
          result = tzFormat().format(TimeZoneFormat.Style.LOCALIZED_GMT, tz, date);
        } 
        buf.append(result);
        break;
      case 24:
        if (count == 1) {
          result = tzFormat().format(TimeZoneFormat.Style.GENERIC_SHORT, tz, date);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.METAZONE_SHORT;
        } else if (count == 4) {
          result = tzFormat().format(TimeZoneFormat.Style.GENERIC_LONG, tz, date);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.METAZONE_LONG;
        } 
        buf.append(result);
        break;
      case 29:
        if (count == 1) {
          result = tzFormat().format(TimeZoneFormat.Style.ZONE_ID_SHORT, tz, date);
        } else if (count == 2) {
          result = tzFormat().format(TimeZoneFormat.Style.ZONE_ID, tz, date);
        } else if (count == 3) {
          result = tzFormat().format(TimeZoneFormat.Style.EXEMPLAR_LOCATION, tz, date);
        } else if (count == 4) {
          result = tzFormat().format(TimeZoneFormat.Style.GENERIC_LOCATION, tz, date);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.ZONE_LONG;
        } 
        buf.append(result);
        break;
      case 31:
        if (count == 1) {
          result = tzFormat().format(TimeZoneFormat.Style.LOCALIZED_GMT_SHORT, tz, date);
        } else if (count == 4) {
          result = tzFormat().format(TimeZoneFormat.Style.LOCALIZED_GMT, tz, date);
        } 
        buf.append(result);
        break;
      case 32:
        if (count == 1) {
          result = tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_SHORT, tz, date);
        } else if (count == 2) {
          result = tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_FIXED, tz, date);
        } else if (count == 3) {
          result = tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_FIXED, tz, date);
        } else if (count == 4) {
          result = tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_FULL, tz, date);
        } else if (count == 5) {
          result = tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_FULL, tz, date);
        } 
        buf.append(result);
        break;
      case 33:
        if (count == 1) {
          result = tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_LOCAL_SHORT, tz, date);
        } else if (count == 2) {
          result = tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_LOCAL_FIXED, tz, date);
        } else if (count == 3) {
          result = tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_LOCAL_FIXED, tz, date);
        } else if (count == 4) {
          result = tzFormat().format(TimeZoneFormat.Style.ISO_BASIC_LOCAL_FULL, tz, date);
        } else if (count == 5) {
          result = tzFormat().format(TimeZoneFormat.Style.ISO_EXTENDED_LOCAL_FULL, tz, date);
        } 
        buf.append(result);
        break;
      case 25:
        if (count < 3) {
          zeroPaddingNumber(currentNumberFormat, buf, value, 1, 2147483647);
          break;
        } 
        value = cal.get(7);
        if (count == 5) {
          safeAppend(this.formatData.standaloneNarrowWeekdays, value, buf);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_NARROW;
          break;
        } 
        if (count == 4) {
          safeAppend(this.formatData.standaloneWeekdays, value, buf);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_STANDALONE;
          break;
        } 
        if (count == 6 && this.formatData.standaloneShorterWeekdays != null) {
          safeAppend(this.formatData.standaloneShorterWeekdays, value, buf);
          capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_STANDALONE;
          break;
        } 
        safeAppend(this.formatData.standaloneShortWeekdays, value, buf);
        capContextUsageType = DateFormatSymbols.CapitalizationContextUsage.DAY_STANDALONE;
        break;
      case 27:
        if (count >= 4) {
          safeAppend(this.formatData.quarters, value / 3, buf);
          break;
        } 
        if (count == 3) {
          safeAppend(this.formatData.shortQuarters, value / 3, buf);
          break;
        } 
        zeroPaddingNumber(currentNumberFormat, buf, value / 3 + 1, count, 2147483647);
        break;
      case 28:
        if (count >= 4) {
          safeAppend(this.formatData.standaloneQuarters, value / 3, buf);
          break;
        } 
        if (count == 3) {
          safeAppend(this.formatData.standaloneShortQuarters, value / 3, buf);
          break;
        } 
        zeroPaddingNumber(currentNumberFormat, buf, value / 3 + 1, count, 2147483647);
        break;
      default:
        zeroPaddingNumber(currentNumberFormat, buf, value, count, 2147483647);
        break;
    } 
    if (fieldNum == 0) {
      boolean titlecase = false;
      if (capitalizationContext != null)
        switch (capitalizationContext) {
          case CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE:
            titlecase = true;
            break;
          case CAPITALIZATION_FOR_UI_LIST_OR_MENU:
          case CAPITALIZATION_FOR_STANDALONE:
            if (this.formatData.capitalization != null) {
              boolean[] transforms = this.formatData.capitalization.get(capContextUsageType);
              titlecase = (capitalizationContext == DisplayContext.CAPITALIZATION_FOR_UI_LIST_OR_MENU) ? transforms[0] : transforms[1];
            } 
            break;
        }  
      if (titlecase) {
        String firstField = buf.substring(bufstart);
        String firstFieldTitleCase = UCharacter.toTitleCase(this.locale, firstField, null, 768);
        buf.replace(bufstart, buf.length(), firstFieldTitleCase);
      } 
    } 
    if (pos.getBeginIndex() == pos.getEndIndex())
      if (pos.getField() == PATTERN_INDEX_TO_DATE_FORMAT_FIELD[patternCharIndex]) {
        pos.setBeginIndex(beginOffset);
        pos.setEndIndex(beginOffset + buf.length() - bufstart);
      } else if (pos.getFieldAttribute() == PATTERN_INDEX_TO_DATE_FORMAT_ATTRIBUTE[patternCharIndex]) {
        pos.setBeginIndex(beginOffset);
        pos.setEndIndex(beginOffset + buf.length() - bufstart);
      }  
  }
  
  private static void safeAppend(String[] array, int value, StringBuffer appendTo) {
    if (array != null && value >= 0 && value < array.length)
      appendTo.append(array[value]); 
  }
  
  private static void safeAppendWithMonthPattern(String[] array, int value, StringBuffer appendTo, String monthPattern) {
    if (array != null && value >= 0 && value < array.length)
      if (monthPattern == null) {
        appendTo.append(array[value]);
      } else {
        appendTo.append(MessageFormat.format(monthPattern, new Object[] { array[value] }));
      }  
  }
  
  private static class PatternItem {
    final char type;
    
    final int length;
    
    final boolean isNumeric;
    
    PatternItem(char type, int length) {
      this.type = type;
      this.length = length;
      this.isNumeric = SimpleDateFormat.isNumeric(type, length);
    }
  }
  
  private static ICUCache<String, Object[]> PARSED_PATTERN_CACHE = (ICUCache<String, Object[]>)new SimpleCache();
  
  private transient Object[] patternItems;
  
  private transient boolean useLocalZeroPaddingNumberFormat;
  
  private transient char[] decDigits;
  
  private transient char[] decimalBuf;
  
  private static final String NUMERIC_FORMAT_CHARS = "MYyudehHmsSDFwWkK";
  
  private Object[] getPatternItems() {
    if (this.patternItems != null)
      return this.patternItems; 
    this.patternItems = (Object[])PARSED_PATTERN_CACHE.get(this.pattern);
    if (this.patternItems != null)
      return this.patternItems; 
    boolean isPrevQuote = false;
    boolean inQuote = false;
    StringBuilder text = new StringBuilder();
    char itemType = Character.MIN_VALUE;
    int itemLength = 1;
    List<Object> items = new ArrayList();
    for (int i = 0; i < this.pattern.length(); i++) {
      char ch = this.pattern.charAt(i);
      if (ch == '\'') {
        if (isPrevQuote) {
          text.append('\'');
          isPrevQuote = false;
        } else {
          isPrevQuote = true;
          if (itemType != '\000') {
            items.add(new PatternItem(itemType, itemLength));
            itemType = Character.MIN_VALUE;
          } 
        } 
        inQuote = !inQuote;
      } else {
        isPrevQuote = false;
        if (inQuote) {
          text.append(ch);
        } else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
          if (ch == itemType) {
            itemLength++;
          } else {
            if (itemType == '\000') {
              if (text.length() > 0) {
                items.add(text.toString());
                text.setLength(0);
              } 
            } else {
              items.add(new PatternItem(itemType, itemLength));
            } 
            itemType = ch;
            itemLength = 1;
          } 
        } else {
          if (itemType != '\000') {
            items.add(new PatternItem(itemType, itemLength));
            itemType = Character.MIN_VALUE;
          } 
          text.append(ch);
        } 
      } 
    } 
    if (itemType == '\000') {
      if (text.length() > 0) {
        items.add(text.toString());
        text.setLength(0);
      } 
    } else {
      items.add(new PatternItem(itemType, itemLength));
    } 
    this.patternItems = items.toArray(new Object[items.size()]);
    PARSED_PATTERN_CACHE.put(this.pattern, this.patternItems);
    return this.patternItems;
  }
  
  protected void zeroPaddingNumber(NumberFormat nf, StringBuffer buf, int value, int minDigits, int maxDigits) {
    if (this.useLocalZeroPaddingNumberFormat && value >= 0) {
      fastZeroPaddingNumber(buf, value, minDigits, maxDigits);
    } else {
      nf.setMinimumIntegerDigits(minDigits);
      nf.setMaximumIntegerDigits(maxDigits);
      nf.format(value, buf, new FieldPosition(-1));
    } 
  }
  
  public void setNumberFormat(NumberFormat newNumberFormat) {
    super.setNumberFormat(newNumberFormat);
    initLocalZeroPaddingNumberFormat();
    initializeTimeZoneFormat(true);
  }
  
  private void initLocalZeroPaddingNumberFormat() {
    if (this.numberFormat instanceof DecimalFormat) {
      this.decDigits = ((DecimalFormat)this.numberFormat).getDecimalFormatSymbols().getDigits();
      this.useLocalZeroPaddingNumberFormat = true;
    } else if (this.numberFormat instanceof DateNumberFormat) {
      this.decDigits = ((DateNumberFormat)this.numberFormat).getDigits();
      this.useLocalZeroPaddingNumberFormat = true;
    } else {
      this.useLocalZeroPaddingNumberFormat = false;
    } 
    if (this.useLocalZeroPaddingNumberFormat)
      this.decimalBuf = new char[10]; 
  }
  
  private void fastZeroPaddingNumber(StringBuffer buf, int value, int minDigits, int maxDigits) {
    int limit = (this.decimalBuf.length < maxDigits) ? this.decimalBuf.length : maxDigits;
    int index = limit - 1;
    while (true) {
      this.decimalBuf[index] = this.decDigits[value % 10];
      value /= 10;
      if (index == 0 || value == 0)
        break; 
      index--;
    } 
    int padding = minDigits - limit - index;
    while (padding > 0 && index > 0) {
      this.decimalBuf[--index] = this.decDigits[0];
      padding--;
    } 
    while (padding > 0) {
      buf.append(this.decDigits[0]);
      padding--;
    } 
    buf.append(this.decimalBuf, index, limit - index);
  }
  
  protected String zeroPaddingNumber(long value, int minDigits, int maxDigits) {
    this.numberFormat.setMinimumIntegerDigits(minDigits);
    this.numberFormat.setMaximumIntegerDigits(maxDigits);
    return this.numberFormat.format(value);
  }
  
  private static final boolean isNumeric(char formatChar, int count) {
    int i = "MYyudehHmsSDFwWkK".indexOf(formatChar);
    return (i > 0 || (i == 0 && count < 3));
  }
  
  public void parse(String text, Calendar cal, ParsePosition parsePos) {
    TimeZone backupTZ = null;
    Calendar resultCal = null;
    if (cal != this.calendar && !cal.getType().equals(this.calendar.getType())) {
      this.calendar.setTimeInMillis(cal.getTimeInMillis());
      backupTZ = this.calendar.getTimeZone();
      this.calendar.setTimeZone(cal.getTimeZone());
      resultCal = cal;
      cal = this.calendar;
    } 
    int pos = parsePos.getIndex();
    int start = pos;
    this.tztype = TimeZoneFormat.TimeType.UNKNOWN;
    boolean[] ambiguousYear = { false };
    int numericFieldStart = -1;
    int numericFieldLength = 0;
    int numericStartPos = 0;
    MessageFormat numericLeapMonthFormatter = null;
    if (this.formatData.leapMonthPatterns != null && this.formatData.leapMonthPatterns.length >= 7)
      numericLeapMonthFormatter = new MessageFormat(this.formatData.leapMonthPatterns[6], this.locale); 
    Object[] items = getPatternItems();
    int i = 0;
    while (i < items.length) {
      if (items[i] instanceof PatternItem) {
        PatternItem field = (PatternItem)items[i];
        if (field.isNumeric)
          if (numericFieldStart == -1)
            if (i + 1 < items.length && items[i + 1] instanceof PatternItem && ((PatternItem)items[i + 1]).isNumeric) {
              numericFieldStart = i;
              numericFieldLength = field.length;
              numericStartPos = pos;
            }   
        if (numericFieldStart != -1) {
          int len = field.length;
          if (numericFieldStart == i)
            len = numericFieldLength; 
          pos = subParse(text, pos, field.type, len, true, false, ambiguousYear, cal, numericLeapMonthFormatter);
          if (pos < 0) {
            numericFieldLength--;
            if (numericFieldLength == 0) {
              parsePos.setIndex(start);
              parsePos.setErrorIndex(pos);
              if (backupTZ != null)
                this.calendar.setTimeZone(backupTZ); 
              return;
            } 
            i = numericFieldStart;
            pos = numericStartPos;
            continue;
          } 
        } else if (field.type != 'l') {
          numericFieldStart = -1;
          int s = pos;
          pos = subParse(text, pos, field.type, field.length, false, true, ambiguousYear, cal, numericLeapMonthFormatter);
          if (pos < 0)
            if (pos == -32000) {
              pos = s;
              if (i + 1 < items.length) {
                String patl = null;
                try {
                  patl = (String)items[i + 1];
                } catch (ClassCastException cce) {
                  parsePos.setIndex(start);
                  parsePos.setErrorIndex(s);
                  if (backupTZ != null)
                    this.calendar.setTimeZone(backupTZ); 
                  return;
                } 
                if (patl == null)
                  patl = (String)items[i + 1]; 
                int plen = patl.length();
                int idx = 0;
                while (idx < plen) {
                  char pch = patl.charAt(idx);
                  if (PatternProps.isWhiteSpace(pch))
                    idx++; 
                } 
                if (idx == plen)
                  i++; 
              } 
            } else {
              parsePos.setIndex(start);
              parsePos.setErrorIndex(s);
              if (backupTZ != null)
                this.calendar.setTimeZone(backupTZ); 
              return;
            }  
        } 
      } else {
        numericFieldStart = -1;
        boolean[] complete = new boolean[1];
        pos = matchLiteral(text, pos, items, i, complete);
        if (!complete[0]) {
          parsePos.setIndex(start);
          parsePos.setErrorIndex(pos);
          if (backupTZ != null)
            this.calendar.setTimeZone(backupTZ); 
          return;
        } 
      } 
      i++;
    } 
    if (pos < text.length()) {
      char extra = text.charAt(pos);
      if (extra == '.' && isLenient() && items.length != 0) {
        Object lastItem = items[items.length - 1];
        if (lastItem instanceof PatternItem && !((PatternItem)lastItem).isNumeric)
          pos++; 
      } 
    } 
    parsePos.setIndex(pos);
    try {
      if (ambiguousYear[0] || this.tztype != TimeZoneFormat.TimeType.UNKNOWN) {
        if (ambiguousYear[0]) {
          Calendar copy = (Calendar)cal.clone();
          Date parsedDate = copy.getTime();
          if (parsedDate.before(getDefaultCenturyStart()))
            cal.set(1, getDefaultCenturyStartYear() + 100); 
        } 
        if (this.tztype != TimeZoneFormat.TimeType.UNKNOWN) {
          Calendar copy = (Calendar)cal.clone();
          TimeZone tz = copy.getTimeZone();
          BasicTimeZone btz = null;
          if (tz instanceof BasicTimeZone)
            btz = (BasicTimeZone)tz; 
          copy.set(15, 0);
          copy.set(16, 0);
          long localMillis = copy.getTimeInMillis();
          int[] offsets = new int[2];
          if (btz != null) {
            if (this.tztype == TimeZoneFormat.TimeType.STANDARD) {
              btz.getOffsetFromLocal(localMillis, 1, 1, offsets);
            } else {
              btz.getOffsetFromLocal(localMillis, 3, 3, offsets);
            } 
          } else {
            tz.getOffset(localMillis, true, offsets);
            if ((this.tztype == TimeZoneFormat.TimeType.STANDARD && offsets[1] != 0) || (this.tztype == TimeZoneFormat.TimeType.DAYLIGHT && offsets[1] == 0))
              tz.getOffset(localMillis - 86400000L, true, offsets); 
          } 
          int resolvedSavings = offsets[1];
          if (this.tztype == TimeZoneFormat.TimeType.STANDARD) {
            if (offsets[1] != 0)
              resolvedSavings = 0; 
          } else if (offsets[1] == 0) {
            if (btz != null) {
              TimeZoneTransition beforeTrs, afterTrs;
              long time = localMillis + offsets[0];
              long beforeT = time, afterT = time;
              int beforeSav = 0, afterSav = 0;
              do {
                beforeTrs = btz.getPreviousTransition(beforeT, true);
                if (beforeTrs == null)
                  break; 
                beforeT = beforeTrs.getTime() - 1L;
                beforeSav = beforeTrs.getFrom().getDSTSavings();
              } while (beforeSav == 0);
              do {
                afterTrs = btz.getNextTransition(afterT, false);
                if (afterTrs == null)
                  break; 
                afterT = afterTrs.getTime();
                afterSav = afterTrs.getTo().getDSTSavings();
              } while (afterSav == 0);
              if (beforeTrs != null && afterTrs != null) {
                if (time - beforeT > afterT - time) {
                  resolvedSavings = afterSav;
                } else {
                  resolvedSavings = beforeSav;
                } 
              } else if (beforeTrs != null && beforeSav != 0) {
                resolvedSavings = beforeSav;
              } else if (afterTrs != null && afterSav != 0) {
                resolvedSavings = afterSav;
              } else {
                resolvedSavings = btz.getDSTSavings();
              } 
            } else {
              resolvedSavings = tz.getDSTSavings();
            } 
            if (resolvedSavings == 0)
              resolvedSavings = 3600000; 
          } 
          cal.set(15, offsets[0]);
          cal.set(16, resolvedSavings);
        } 
      } 
    } catch (IllegalArgumentException e) {
      parsePos.setErrorIndex(pos);
      parsePos.setIndex(start);
      if (backupTZ != null)
        this.calendar.setTimeZone(backupTZ); 
      return;
    } 
    if (resultCal != null) {
      resultCal.setTimeZone(cal.getTimeZone());
      resultCal.setTimeInMillis(cal.getTimeInMillis());
    } 
    if (backupTZ != null)
      this.calendar.setTimeZone(backupTZ); 
  }
  
  private int matchLiteral(String text, int pos, Object[] items, int itemIndex, boolean[] complete) {
    int originalPos = pos;
    String patternLiteral = (String)items[itemIndex];
    int plen = patternLiteral.length();
    int tlen = text.length();
    int idx = 0;
    while (idx < plen && pos < tlen) {
      char pch = patternLiteral.charAt(idx);
      char ich = text.charAt(pos);
      if (PatternProps.isWhiteSpace(pch) && PatternProps.isWhiteSpace(ich)) {
        while (idx + 1 < plen && PatternProps.isWhiteSpace(patternLiteral.charAt(idx + 1)))
          idx++; 
        while (pos + 1 < tlen && PatternProps.isWhiteSpace(text.charAt(pos + 1)))
          pos++; 
      } else if (pch != ich) {
        if (ich == '.' && pos == originalPos && 0 < itemIndex && isLenient()) {
          Object before = items[itemIndex - 1];
          if (before instanceof PatternItem) {
            boolean isNumeric = ((PatternItem)before).isNumeric;
            if (!isNumeric) {
              pos++;
              continue;
            } 
          } 
        } 
        break;
      } 
      idx++;
      pos++;
    } 
    complete[0] = (idx == plen);
    if (!complete[0] && isLenient() && 0 < itemIndex && itemIndex < items.length - 1)
      if (originalPos < tlen) {
        Object before = items[itemIndex - 1];
        Object after = items[itemIndex + 1];
        if (before instanceof PatternItem && after instanceof PatternItem) {
          char beforeType = ((PatternItem)before).type;
          char afterType = ((PatternItem)after).type;
          if (DATE_PATTERN_TYPE.contains(beforeType) != DATE_PATTERN_TYPE.contains(afterType)) {
            int newPos = originalPos;
            while (true) {
              char ich = text.charAt(newPos);
              if (!PatternProps.isWhiteSpace(ich))
                break; 
              newPos++;
            } 
            complete[0] = (newPos > originalPos);
            pos = newPos;
          } 
        } 
      }  
    return pos;
  }
  
  static final UnicodeSet DATE_PATTERN_TYPE = (new UnicodeSet("[GyYuUQqMLlwWd]")).freeze();
  
  protected int matchString(String text, int start, int field, String[] data, Calendar cal) {
    return matchString(text, start, field, data, (String)null, cal);
  }
  
  protected int matchString(String text, int start, int field, String[] data, String monthPattern, Calendar cal) {
    int i = 0;
    int count = data.length;
    if (field == 7)
      i = 1; 
    int bestMatchLength = 0, bestMatch = -1;
    int isLeapMonth = 0;
    int matchLength = 0;
    for (; i < count; i++) {
      int length = data[i].length();
      if (length > bestMatchLength && (matchLength = regionMatchesWithOptionalDot(text, start, data[i], length)) >= 0) {
        bestMatch = i;
        bestMatchLength = matchLength;
        isLeapMonth = 0;
      } 
      if (monthPattern != null) {
        String leapMonthName = MessageFormat.format(monthPattern, new Object[] { data[i] });
        length = leapMonthName.length();
        if (length > bestMatchLength && (matchLength = regionMatchesWithOptionalDot(text, start, leapMonthName, length)) >= 0) {
          bestMatch = i;
          bestMatchLength = matchLength;
          isLeapMonth = 1;
        } 
      } 
    } 
    if (bestMatch >= 0) {
      if (field == 1)
        bestMatch++; 
      cal.set(field, bestMatch);
      if (monthPattern != null)
        cal.set(22, isLeapMonth); 
      return start + bestMatchLength;
    } 
    return -start;
  }
  
  private int regionMatchesWithOptionalDot(String text, int start, String data, int length) {
    boolean matches = text.regionMatches(true, start, data, 0, length);
    if (matches)
      return length; 
    if (data.length() > 0 && data.charAt(data.length() - 1) == '.' && 
      text.regionMatches(true, start, data, 0, length - 1))
      return length - 1; 
    return -1;
  }
  
  protected int matchQuarterString(String text, int start, int field, String[] data, Calendar cal) {
    int i = 0;
    int count = data.length;
    int bestMatchLength = 0, bestMatch = -1;
    int matchLength = 0;
    for (; i < count; i++) {
      int length = data[i].length();
      if (length > bestMatchLength && (matchLength = regionMatchesWithOptionalDot(text, start, data[i], length)) >= 0) {
        bestMatch = i;
        bestMatchLength = matchLength;
      } 
    } 
    if (bestMatch >= 0) {
      cal.set(field, bestMatch * 3);
      return start + bestMatchLength;
    } 
    return -start;
  }
  
  protected int subParse(String text, int start, char ch, int count, boolean obeyCount, boolean allowNegative, boolean[] ambiguousYear, Calendar cal) {
    return subParse(text, start, ch, count, obeyCount, allowNegative, ambiguousYear, cal, (MessageFormat)null);
  }
  
  protected int subParse(String text, int start, char ch, int count, boolean obeyCount, boolean allowNegative, boolean[] ambiguousYear, Calendar cal, MessageFormat numericLeapMonthFormatter) {
    int i, ps;
    boolean haveMonthPat;
    int j;
    Output<TimeZoneFormat.TimeType> tzTimeType;
    int newStart, k;
    TimeZoneFormat.Style style;
    TimeZone tz;
    Number number = null;
    NumberFormat currentNumberFormat = null;
    int value = 0;
    ParsePosition pos = new ParsePosition(0);
    boolean lenient = isLenient();
    int patternCharIndex = -1;
    if ('A' <= ch && ch <= 'z')
      patternCharIndex = PATTERN_CHAR_TO_INDEX[ch - 64]; 
    if (patternCharIndex == -1)
      return -start; 
    currentNumberFormat = getNumberFormat(ch);
    int field = PATTERN_INDEX_TO_CALENDAR_FIELD[patternCharIndex];
    if (numericLeapMonthFormatter != null)
      numericLeapMonthFormatter.setFormatByArgumentIndex(0, currentNumberFormat); 
    while (true) {
      if (start >= text.length())
        return -start; 
      int c = UTF16.charAt(text, start);
      if (!UCharacter.isUWhiteSpace(c) || !PatternProps.isWhiteSpace(c))
        break; 
      start += UTF16.getCharCount(c);
    } 
    pos.setIndex(start);
    if (patternCharIndex == 4 || patternCharIndex == 15 || (patternCharIndex == 2 && count <= 2) || (patternCharIndex == 26 && count <= 2) || patternCharIndex == 1 || patternCharIndex == 18 || patternCharIndex == 30 || (patternCharIndex == 0 && cal.getType().equals("chinese")) || patternCharIndex == 8) {
      boolean parsedNumericLeapMonth = false;
      if (numericLeapMonthFormatter != null && (patternCharIndex == 2 || patternCharIndex == 26)) {
        Object[] args = numericLeapMonthFormatter.parse(text, pos);
        if (args != null && pos.getIndex() > start && args[0] instanceof Number) {
          parsedNumericLeapMonth = true;
          number = (Number)args[0];
          cal.set(22, 1);
        } else {
          pos.setIndex(start);
          cal.set(22, 0);
        } 
      } 
      if (!parsedNumericLeapMonth) {
        if (obeyCount) {
          if (start + count > text.length())
            return -start; 
          number = parseInt(text, count, pos, allowNegative, currentNumberFormat);
        } else {
          number = parseInt(text, pos, allowNegative, currentNumberFormat);
        } 
        if (number == null && patternCharIndex != 30)
          return -start; 
      } 
      if (number != null)
        value = number.intValue(); 
    } 
    switch (patternCharIndex) {
      case 0:
        if (cal.getType().equals("chinese")) {
          cal.set(0, value);
          return pos.getIndex();
        } 
        ps = 0;
        if (count == 5) {
          ps = matchString(text, start, 0, this.formatData.narrowEras, (String)null, cal);
        } else if (count == 4) {
          ps = matchString(text, start, 0, this.formatData.eraNames, (String)null, cal);
        } else {
          ps = matchString(text, start, 0, this.formatData.eras, (String)null, cal);
        } 
        if (ps == -start)
          ps = -32000; 
        return ps;
      case 1:
      case 18:
        if (this.override != null && (this.override.compareTo("hebr") == 0 || this.override.indexOf("y=hebr") >= 0) && value < 1000) {
          value += 5000;
        } else if (count == 2 && pos.getIndex() - start == 2 && !cal.getType().equals("chinese") && UCharacter.isDigit(text.charAt(start)) && UCharacter.isDigit(text.charAt(start + 1))) {
          int ambiguousTwoDigitYear = getDefaultCenturyStartYear() % 100;
          ambiguousYear[0] = (value == ambiguousTwoDigitYear);
          value += getDefaultCenturyStartYear() / 100 * 100 + ((value < ambiguousTwoDigitYear) ? 100 : 0);
        } 
        cal.set(field, value);
        if (DelayedHebrewMonthCheck) {
          if (!HebrewCalendar.isLeapYear(value))
            cal.add(2, 1); 
          DelayedHebrewMonthCheck = false;
        } 
        return pos.getIndex();
      case 30:
        if (this.formatData.shortYearNames != null) {
          int m = matchString(text, start, 1, this.formatData.shortYearNames, (String)null, cal);
          if (m > 0)
            return m; 
        } 
        if (number != null && (lenient || this.formatData.shortYearNames == null || value > this.formatData.shortYearNames.length)) {
          cal.set(1, value);
          return pos.getIndex();
        } 
        return -start;
      case 2:
      case 26:
        if (count <= 2) {
          cal.set(2, value - 1);
          if (cal.getType().equals("hebrew") && value >= 6)
            if (cal.isSet(1)) {
              if (!HebrewCalendar.isLeapYear(cal.get(1)))
                cal.set(2, value); 
            } else {
              DelayedHebrewMonthCheck = true;
            }  
          return pos.getIndex();
        } 
        haveMonthPat = (this.formatData.leapMonthPatterns != null && this.formatData.leapMonthPatterns.length >= 7);
        k = (patternCharIndex == 2) ? matchString(text, start, 2, this.formatData.months, haveMonthPat ? this.formatData.leapMonthPatterns[0] : null, cal) : matchString(text, start, 2, this.formatData.standaloneMonths, haveMonthPat ? this.formatData.leapMonthPatterns[3] : null, cal);
        if (k > 0)
          return k; 
        return (patternCharIndex == 2) ? matchString(text, start, 2, this.formatData.shortMonths, haveMonthPat ? this.formatData.leapMonthPatterns[1] : null, cal) : matchString(text, start, 2, this.formatData.standaloneShortMonths, haveMonthPat ? this.formatData.leapMonthPatterns[4] : null, cal);
      case 4:
        if (value == cal.getMaximum(11) + 1)
          value = 0; 
        cal.set(11, value);
        return pos.getIndex();
      case 8:
        i = pos.getIndex() - start;
        if (i < 3) {
          while (i < 3) {
            value *= 10;
            i++;
          } 
        } else {
          int a = 1;
          while (i > 3) {
            a *= 10;
            i--;
          } 
          value /= a;
        } 
        cal.set(14, value);
        return pos.getIndex();
      case 9:
        j = matchString(text, start, 7, this.formatData.weekdays, (String)null, cal);
        if (j > 0)
          return j; 
        if ((j = matchString(text, start, 7, this.formatData.shortWeekdays, (String)null, cal)) > 0)
          return j; 
        if (this.formatData.shorterWeekdays != null)
          return matchString(text, start, 7, this.formatData.shorterWeekdays, (String)null, cal); 
        return j;
      case 25:
        j = matchString(text, start, 7, this.formatData.standaloneWeekdays, (String)null, cal);
        if (j > 0)
          return j; 
        if ((j = matchString(text, start, 7, this.formatData.standaloneShortWeekdays, (String)null, cal)) > 0)
          return j; 
        if (this.formatData.standaloneShorterWeekdays != null)
          return matchString(text, start, 7, this.formatData.standaloneShorterWeekdays, (String)null, cal); 
        return j;
      case 14:
        return matchString(text, start, 9, this.formatData.ampms, (String)null, cal);
      case 15:
        if (value == cal.getLeastMaximum(10) + 1)
          value = 0; 
        cal.set(10, value);
        return pos.getIndex();
      case 17:
        tzTimeType = new Output();
        style = (count < 4) ? TimeZoneFormat.Style.SPECIFIC_SHORT : TimeZoneFormat.Style.SPECIFIC_LONG;
        tz = tzFormat().parse(style, text, pos, tzTimeType);
        if (tz != null) {
          this.tztype = (TimeZoneFormat.TimeType)tzTimeType.value;
          cal.setTimeZone(tz);
          return pos.getIndex();
        } 
        return -start;
      case 23:
        tzTimeType = new Output();
        style = (count < 4) ? TimeZoneFormat.Style.ISO_BASIC_LOCAL_FULL : ((count == 5) ? TimeZoneFormat.Style.ISO_EXTENDED_FULL : TimeZoneFormat.Style.LOCALIZED_GMT);
        tz = tzFormat().parse(style, text, pos, tzTimeType);
        if (tz != null) {
          this.tztype = (TimeZoneFormat.TimeType)tzTimeType.value;
          cal.setTimeZone(tz);
          return pos.getIndex();
        } 
        return -start;
      case 24:
        tzTimeType = new Output();
        style = (count < 4) ? TimeZoneFormat.Style.GENERIC_SHORT : TimeZoneFormat.Style.GENERIC_LONG;
        tz = tzFormat().parse(style, text, pos, tzTimeType);
        if (tz != null) {
          this.tztype = (TimeZoneFormat.TimeType)tzTimeType.value;
          cal.setTimeZone(tz);
          return pos.getIndex();
        } 
        return -start;
      case 29:
        tzTimeType = new Output();
        style = null;
        switch (count) {
          case 1:
            style = TimeZoneFormat.Style.ZONE_ID_SHORT;
            break;
          case 2:
            style = TimeZoneFormat.Style.ZONE_ID;
            break;
          case 3:
            style = TimeZoneFormat.Style.EXEMPLAR_LOCATION;
            break;
          default:
            style = TimeZoneFormat.Style.GENERIC_LOCATION;
            break;
        } 
        tz = tzFormat().parse(style, text, pos, tzTimeType);
        if (tz != null) {
          this.tztype = (TimeZoneFormat.TimeType)tzTimeType.value;
          cal.setTimeZone(tz);
          return pos.getIndex();
        } 
        return -start;
      case 31:
        tzTimeType = new Output();
        style = (count < 4) ? TimeZoneFormat.Style.LOCALIZED_GMT_SHORT : TimeZoneFormat.Style.LOCALIZED_GMT;
        tz = tzFormat().parse(style, text, pos, tzTimeType);
        if (tz != null) {
          this.tztype = (TimeZoneFormat.TimeType)tzTimeType.value;
          cal.setTimeZone(tz);
          return pos.getIndex();
        } 
        return -start;
      case 32:
        tzTimeType = new Output();
        switch (count) {
          case 1:
            style = TimeZoneFormat.Style.ISO_BASIC_SHORT;
            break;
          case 2:
            style = TimeZoneFormat.Style.ISO_BASIC_FIXED;
            break;
          case 3:
            style = TimeZoneFormat.Style.ISO_EXTENDED_FIXED;
            break;
          case 4:
            style = TimeZoneFormat.Style.ISO_BASIC_FULL;
            break;
          default:
            style = TimeZoneFormat.Style.ISO_EXTENDED_FULL;
            break;
        } 
        tz = tzFormat().parse(style, text, pos, tzTimeType);
        if (tz != null) {
          this.tztype = (TimeZoneFormat.TimeType)tzTimeType.value;
          cal.setTimeZone(tz);
          return pos.getIndex();
        } 
        return -start;
      case 33:
        tzTimeType = new Output();
        switch (count) {
          case 1:
            style = TimeZoneFormat.Style.ISO_BASIC_LOCAL_SHORT;
            break;
          case 2:
            style = TimeZoneFormat.Style.ISO_BASIC_LOCAL_FIXED;
            break;
          case 3:
            style = TimeZoneFormat.Style.ISO_EXTENDED_LOCAL_FIXED;
            break;
          case 4:
            style = TimeZoneFormat.Style.ISO_BASIC_LOCAL_FULL;
            break;
          default:
            style = TimeZoneFormat.Style.ISO_EXTENDED_LOCAL_FULL;
            break;
        } 
        tz = tzFormat().parse(style, text, pos, tzTimeType);
        if (tz != null) {
          this.tztype = (TimeZoneFormat.TimeType)tzTimeType.value;
          cal.setTimeZone(tz);
          return pos.getIndex();
        } 
        return -start;
      case 27:
        if (count <= 2) {
          cal.set(2, (value - 1) * 3);
          return pos.getIndex();
        } 
        newStart = matchQuarterString(text, start, 2, this.formatData.quarters, cal);
        if (newStart > 0)
          return newStart; 
        return matchQuarterString(text, start, 2, this.formatData.shortQuarters, cal);
      case 28:
        if (count <= 2) {
          cal.set(2, (value - 1) * 3);
          return pos.getIndex();
        } 
        newStart = matchQuarterString(text, start, 2, this.formatData.standaloneQuarters, cal);
        if (newStart > 0)
          return newStart; 
        return matchQuarterString(text, start, 2, this.formatData.standaloneShortQuarters, cal);
    } 
    if (obeyCount) {
      if (start + count > text.length())
        return -start; 
      number = parseInt(text, count, pos, allowNegative, currentNumberFormat);
    } else {
      number = parseInt(text, pos, allowNegative, currentNumberFormat);
    } 
    if (number != null) {
      cal.set(field, number.intValue());
      return pos.getIndex();
    } 
    return -start;
  }
  
  private Number parseInt(String text, ParsePosition pos, boolean allowNegative, NumberFormat fmt) {
    return parseInt(text, -1, pos, allowNegative, fmt);
  }
  
  private Number parseInt(String text, int maxDigits, ParsePosition pos, boolean allowNegative, NumberFormat fmt) {
    Number number;
    int oldPos = pos.getIndex();
    if (allowNegative) {
      number = fmt.parse(text, pos);
    } else if (fmt instanceof DecimalFormat) {
      String oldPrefix = ((DecimalFormat)fmt).getNegativePrefix();
      ((DecimalFormat)fmt).setNegativePrefix("꬀");
      number = fmt.parse(text, pos);
      ((DecimalFormat)fmt).setNegativePrefix(oldPrefix);
    } else {
      boolean dateNumberFormat = fmt instanceof DateNumberFormat;
      if (dateNumberFormat)
        ((DateNumberFormat)fmt).setParsePositiveOnly(true); 
      number = fmt.parse(text, pos);
      if (dateNumberFormat)
        ((DateNumberFormat)fmt).setParsePositiveOnly(false); 
    } 
    if (maxDigits > 0) {
      int nDigits = pos.getIndex() - oldPos;
      if (nDigits > maxDigits) {
        double val = number.doubleValue();
        nDigits -= maxDigits;
        while (nDigits > 0) {
          val /= 10.0D;
          nDigits--;
        } 
        pos.setIndex(oldPos + maxDigits);
        number = Integer.valueOf((int)val);
      } 
    } 
    return number;
  }
  
  private String translatePattern(String pat, String from, String to) {
    StringBuilder result = new StringBuilder();
    boolean inQuote = false;
    for (int i = 0; i < pat.length(); i++) {
      char c = pat.charAt(i);
      if (inQuote) {
        if (c == '\'')
          inQuote = false; 
      } else if (c == '\'') {
        inQuote = true;
      } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
        int ci = from.indexOf(c);
        if (ci != -1)
          c = to.charAt(ci); 
      } 
      result.append(c);
    } 
    if (inQuote)
      throw new IllegalArgumentException("Unfinished quote in pattern"); 
    return result.toString();
  }
  
  public String toPattern() {
    return this.pattern;
  }
  
  public String toLocalizedPattern() {
    return translatePattern(this.pattern, "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx", this.formatData.localPatternChars);
  }
  
  public void applyPattern(String pat) {
    this.pattern = pat;
    setLocale(null, null);
    this.patternItems = null;
  }
  
  public void applyLocalizedPattern(String pat) {
    this.pattern = translatePattern(pat, this.formatData.localPatternChars, "GyMdkHmsSEDFwWahKzYeugAZvcLQqVUOXx");
    setLocale(null, null);
  }
  
  public DateFormatSymbols getDateFormatSymbols() {
    return (DateFormatSymbols)this.formatData.clone();
  }
  
  public void setDateFormatSymbols(DateFormatSymbols newFormatSymbols) {
    this.formatData = (DateFormatSymbols)newFormatSymbols.clone();
  }
  
  protected DateFormatSymbols getSymbols() {
    return this.formatData;
  }
  
  public TimeZoneFormat getTimeZoneFormat() {
    return tzFormat().freeze();
  }
  
  public void setTimeZoneFormat(TimeZoneFormat tzfmt) {
    if (tzfmt.isFrozen()) {
      this.tzFormat = tzfmt;
    } else {
      this.tzFormat = tzfmt.cloneAsThawed().freeze();
    } 
  }
  
  public void setContext(DisplayContext context) {
    if (context.type() == DisplayContext.Type.CAPITALIZATION)
      this.capitalizationSetting = context; 
  }
  
  public DisplayContext getContext(DisplayContext.Type type) {
    return (type == DisplayContext.Type.CAPITALIZATION && this.capitalizationSetting != null) ? this.capitalizationSetting : DisplayContext.CAPITALIZATION_NONE;
  }
  
  public Object clone() {
    SimpleDateFormat other = (SimpleDateFormat)super.clone();
    other.formatData = (DateFormatSymbols)this.formatData.clone();
    return other;
  }
  
  public int hashCode() {
    return this.pattern.hashCode();
  }
  
  public boolean equals(Object obj) {
    if (!super.equals(obj))
      return false; 
    SimpleDateFormat that = (SimpleDateFormat)obj;
    return (this.pattern.equals(that.pattern) && this.formatData.equals(that.formatData));
  }
  
  private void writeObject(ObjectOutputStream stream) throws IOException {
    if (this.defaultCenturyStart == null)
      initializeDefaultCenturyStart(this.defaultCenturyBase); 
    initializeTimeZoneFormat(false);
    stream.defaultWriteObject();
    stream.writeInt(this.capitalizationSetting.value());
  }
  
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    int capitalizationSettingValue = (this.serialVersionOnStream > 1) ? stream.readInt() : -1;
    if (this.serialVersionOnStream < 1) {
      this.defaultCenturyBase = System.currentTimeMillis();
    } else {
      parseAmbiguousDatesAsAfter(this.defaultCenturyStart);
    } 
    this.serialVersionOnStream = 2;
    this.locale = getLocale(ULocale.VALID_LOCALE);
    if (this.locale == null)
      this.locale = ULocale.getDefault(ULocale.Category.FORMAT); 
    initLocalZeroPaddingNumberFormat();
    this.capitalizationSetting = DisplayContext.CAPITALIZATION_NONE;
    if (capitalizationSettingValue >= 0)
      for (DisplayContext context : DisplayContext.values()) {
        if (context.value() == capitalizationSettingValue) {
          this.capitalizationSetting = context;
          break;
        } 
      }  
  }
  
  public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
    Calendar cal = this.calendar;
    if (obj instanceof Calendar) {
      cal = (Calendar)obj;
    } else if (obj instanceof Date) {
      this.calendar.setTime((Date)obj);
    } else if (obj instanceof Number) {
      this.calendar.setTimeInMillis(((Number)obj).longValue());
    } else {
      throw new IllegalArgumentException("Cannot format given Object as a Date");
    } 
    StringBuffer toAppendTo = new StringBuffer();
    FieldPosition pos = new FieldPosition(0);
    List<FieldPosition> attributes = new ArrayList<FieldPosition>();
    format(cal, this.capitalizationSetting, toAppendTo, pos, attributes);
    AttributedString as = new AttributedString(toAppendTo.toString());
    for (int i = 0; i < attributes.size(); i++) {
      FieldPosition fp = attributes.get(i);
      Format.Field attribute = fp.getFieldAttribute();
      as.addAttribute(attribute, attribute, fp.getBeginIndex(), fp.getEndIndex());
    } 
    return as.getIterator();
  }
  
  ULocale getLocale() {
    return this.locale;
  }
  
  boolean isFieldUnitIgnored(int field) {
    return isFieldUnitIgnored(this.pattern, field);
  }
  
  static boolean isFieldUnitIgnored(String pattern, int field) {
    int fieldLevel = CALENDAR_FIELD_TO_LEVEL[field];
    boolean inQuote = false;
    char prevCh = Character.MIN_VALUE;
    int count = 0;
    for (int i = 0; i < pattern.length(); i++) {
      char ch = pattern.charAt(i);
      if (ch != prevCh && count > 0) {
        int level = PATTERN_CHAR_TO_LEVEL[prevCh - 64];
        if (fieldLevel <= level)
          return false; 
        count = 0;
      } 
      if (ch == '\'') {
        if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '\'') {
          i++;
        } else {
          inQuote = !inQuote;
        } 
      } else if (!inQuote && ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))) {
        prevCh = ch;
        count++;
      } 
    } 
    if (count > 0) {
      int level = PATTERN_CHAR_TO_LEVEL[prevCh - 64];
      if (fieldLevel <= level)
        return false; 
    } 
    return true;
  }
  
  public final StringBuffer intervalFormatByAlgorithm(Calendar fromCalendar, Calendar toCalendar, StringBuffer appendTo, FieldPosition pos) throws IllegalArgumentException {
    if (!fromCalendar.isEquivalentTo(toCalendar))
      throw new IllegalArgumentException("can not format on two different calendars"); 
    Object[] items = getPatternItems();
    int diffBegin = -1;
    int diffEnd = -1;
    try {
      int j;
      for (j = 0; j < items.length; j++) {
        if (diffCalFieldValue(fromCalendar, toCalendar, items, j)) {
          diffBegin = j;
          break;
        } 
      } 
      if (diffBegin == -1)
        return format(fromCalendar, appendTo, pos); 
      for (j = items.length - 1; j >= diffBegin; j--) {
        if (diffCalFieldValue(fromCalendar, toCalendar, items, j)) {
          diffEnd = j;
          break;
        } 
      } 
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.toString());
    } 
    if (diffBegin == 0 && diffEnd == items.length - 1) {
      format(fromCalendar, appendTo, pos);
      appendTo.append(" – ");
      format(toCalendar, appendTo, pos);
      return appendTo;
    } 
    int highestLevel = 1000;
    int i;
    for (i = diffBegin; i <= diffEnd; i++) {
      if (!(items[i] instanceof String)) {
        PatternItem item = (PatternItem)items[i];
        char ch = item.type;
        int patternCharIndex = -1;
        if ('A' <= ch && ch <= 'z')
          patternCharIndex = PATTERN_CHAR_TO_LEVEL[ch - 64]; 
        if (patternCharIndex == -1)
          throw new IllegalArgumentException("Illegal pattern character '" + ch + "' in \"" + this.pattern + '"'); 
        if (patternCharIndex < highestLevel)
          highestLevel = patternCharIndex; 
      } 
    } 
    try {
      for (i = 0; i < diffBegin; i++) {
        if (lowerLevel(items, i, highestLevel)) {
          diffBegin = i;
          break;
        } 
      } 
      for (i = items.length - 1; i > diffEnd; i--) {
        if (lowerLevel(items, i, highestLevel)) {
          diffEnd = i;
          break;
        } 
      } 
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.toString());
    } 
    if (diffBegin == 0 && diffEnd == items.length - 1) {
      format(fromCalendar, appendTo, pos);
      appendTo.append(" – ");
      format(toCalendar, appendTo, pos);
      return appendTo;
    } 
    pos.setBeginIndex(0);
    pos.setEndIndex(0);
    for (i = 0; i <= diffEnd; i++) {
      if (items[i] instanceof String) {
        appendTo.append((String)items[i]);
      } else {
        PatternItem item = (PatternItem)items[i];
        if (this.useFastFormat) {
          subFormat(appendTo, item.type, item.length, appendTo.length(), i, this.capitalizationSetting, pos, fromCalendar);
        } else {
          appendTo.append(subFormat(item.type, item.length, appendTo.length(), i, this.capitalizationSetting, pos, fromCalendar));
        } 
      } 
    } 
    appendTo.append(" – ");
    for (i = diffBegin; i < items.length; i++) {
      if (items[i] instanceof String) {
        appendTo.append((String)items[i]);
      } else {
        PatternItem item = (PatternItem)items[i];
        if (this.useFastFormat) {
          subFormat(appendTo, item.type, item.length, appendTo.length(), i, this.capitalizationSetting, pos, toCalendar);
        } else {
          appendTo.append(subFormat(item.type, item.length, appendTo.length(), i, this.capitalizationSetting, pos, toCalendar));
        } 
      } 
    } 
    return appendTo;
  }
  
  private boolean diffCalFieldValue(Calendar fromCalendar, Calendar toCalendar, Object[] items, int i) throws IllegalArgumentException {
    if (items[i] instanceof String)
      return false; 
    PatternItem item = (PatternItem)items[i];
    char ch = item.type;
    int patternCharIndex = -1;
    if ('A' <= ch && ch <= 'z')
      patternCharIndex = PATTERN_CHAR_TO_INDEX[ch - 64]; 
    if (patternCharIndex == -1)
      throw new IllegalArgumentException("Illegal pattern character '" + ch + "' in \"" + this.pattern + '"'); 
    int field = PATTERN_INDEX_TO_CALENDAR_FIELD[patternCharIndex];
    int value = fromCalendar.get(field);
    int value_2 = toCalendar.get(field);
    if (value != value_2)
      return true; 
    return false;
  }
  
  private boolean lowerLevel(Object[] items, int i, int level) throws IllegalArgumentException {
    if (items[i] instanceof String)
      return false; 
    PatternItem item = (PatternItem)items[i];
    char ch = item.type;
    int patternCharIndex = -1;
    if ('A' <= ch && ch <= 'z')
      patternCharIndex = PATTERN_CHAR_TO_LEVEL[ch - 64]; 
    if (patternCharIndex == -1)
      throw new IllegalArgumentException("Illegal pattern character '" + ch + "' in \"" + this.pattern + '"'); 
    if (patternCharIndex >= level)
      return true; 
    return false;
  }
  
  protected NumberFormat getNumberFormat(char ch) {
    Character ovrField = Character.valueOf(ch);
    if (this.overrideMap != null && this.overrideMap.containsKey(ovrField)) {
      String nsName = ((String)this.overrideMap.get(ovrField)).toString();
      NumberFormat nf = this.numberFormatters.get(nsName);
      return nf;
    } 
    return this.numberFormat;
  }
  
  private void initNumberFormatters(ULocale loc) {
    this.numberFormatters = new HashMap<String, NumberFormat>();
    this.overrideMap = new HashMap<Character, String>();
    processOverrideString(loc, this.override);
  }
  
  private void processOverrideString(ULocale loc, String str) {
    if (str == null || str.length() == 0)
      return; 
    int start = 0;
    boolean moreToProcess = true;
    while (moreToProcess) {
      int end;
      String nsName;
      boolean fullOverride;
      int delimiterPosition = str.indexOf(";", start);
      if (delimiterPosition == -1) {
        moreToProcess = false;
        end = str.length();
      } else {
        end = delimiterPosition;
      } 
      String currentString = str.substring(start, end);
      int equalSignPosition = currentString.indexOf("=");
      if (equalSignPosition == -1) {
        nsName = currentString;
        fullOverride = true;
      } else {
        nsName = currentString.substring(equalSignPosition + 1);
        Character ovrField = Character.valueOf(currentString.charAt(0));
        this.overrideMap.put(ovrField, nsName);
        fullOverride = false;
      } 
      ULocale ovrLoc = new ULocale(loc.getBaseName() + "@numbers=" + nsName);
      NumberFormat nf = NumberFormat.createInstance(ovrLoc, 0);
      nf.setGroupingUsed(false);
      if (fullOverride) {
        setNumberFormat(nf);
      } else {
        this.useLocalZeroPaddingNumberFormat = false;
      } 
      if (!this.numberFormatters.containsKey(nsName))
        this.numberFormatters.put(nsName, nf); 
      start = delimiterPosition + 1;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\SimpleDateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */