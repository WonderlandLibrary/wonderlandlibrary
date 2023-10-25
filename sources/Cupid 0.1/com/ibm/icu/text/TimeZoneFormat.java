package com.ibm.icu.text;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.TextTrieMap;
import com.ibm.icu.impl.TimeZoneGenericNames;
import com.ibm.icu.impl.ZoneMeta;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Set;

public class TimeZoneFormat extends UFormat implements Freezable<TimeZoneFormat>, Serializable {
  private static final long serialVersionUID = 2281246852693575022L;
  
  private static final int ISO_Z_STYLE_FLAG = 128;
  
  private static final int ISO_LOCAL_STYLE_FLAG = 256;
  
  private ULocale _locale;
  
  private TimeZoneNames _tznames;
  
  private String _gmtPattern;
  
  private String[] _gmtOffsetPatterns;
  
  private String[] _gmtOffsetDigits;
  
  private String _gmtZeroFormat;
  
  private boolean _parseAllStyles;
  
  private volatile transient TimeZoneGenericNames _gnames;
  
  private transient String _gmtPatternPrefix;
  
  private transient String _gmtPatternSuffix;
  
  private transient Object[][] _gmtOffsetPatternItems;
  
  private transient boolean _abuttingOffsetHoursAndMinutes;
  
  private transient String _region;
  
  private transient boolean _frozen;
  
  private static final String TZID_GMT = "Etc/GMT";
  
  public enum Style {
    GENERIC_LOCATION(1),
    GENERIC_LONG(2),
    GENERIC_SHORT(4),
    SPECIFIC_LONG(8),
    SPECIFIC_SHORT(16),
    LOCALIZED_GMT(32),
    LOCALIZED_GMT_SHORT(64),
    ISO_BASIC_SHORT(128),
    ISO_BASIC_LOCAL_SHORT(256),
    ISO_BASIC_FIXED(128),
    ISO_BASIC_LOCAL_FIXED(256),
    ISO_BASIC_FULL(128),
    ISO_BASIC_LOCAL_FULL(256),
    ISO_EXTENDED_FIXED(128),
    ISO_EXTENDED_LOCAL_FIXED(256),
    ISO_EXTENDED_FULL(128),
    ISO_EXTENDED_LOCAL_FULL(256),
    ZONE_ID(512),
    ZONE_ID_SHORT(1024),
    EXEMPLAR_LOCATION(2048);
    
    final int flag;
    
    Style(int flag) {
      this.flag = flag;
    }
  }
  
  public enum GMTOffsetPatternType {
    POSITIVE_HM("+H:mm", "Hm", true),
    POSITIVE_HMS("+H:mm:ss", "Hms", true),
    NEGATIVE_HM("-H:mm", "Hm", false),
    NEGATIVE_HMS("-H:mm:ss", "Hms", false),
    POSITIVE_H("+H", "H", true),
    NEGATIVE_H("-H", "H", false);
    
    private String _defaultPattern;
    
    private String _required;
    
    private boolean _isPositive;
    
    GMTOffsetPatternType(String defaultPattern, String required, boolean isPositive) {
      this._defaultPattern = defaultPattern;
      this._required = required;
      this._isPositive = isPositive;
    }
    
    private String defaultPattern() {
      return this._defaultPattern;
    }
    
    private String required() {
      return this._required;
    }
    
    private boolean isPositive() {
      return this._isPositive;
    }
  }
  
  public enum TimeType {
    UNKNOWN, STANDARD, DAYLIGHT;
  }
  
  public enum ParseOption {
    ALL_STYLES;
  }
  
  private static final String[] ALT_GMT_STRINGS = new String[] { "GMT", "UTC", "UT" };
  
  private static final String DEFAULT_GMT_PATTERN = "GMT{0}";
  
  private static final String DEFAULT_GMT_ZERO = "GMT";
  
  private static final String[] DEFAULT_GMT_DIGITS = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
  
  private static final char DEFAULT_GMT_OFFSET_SEP = ':';
  
  private static final String ASCII_DIGITS = "0123456789";
  
  private static final String ISO8601_UTC = "Z";
  
  private static final String UNKNOWN_ZONE_ID = "Etc/Unknown";
  
  private static final String UNKNOWN_SHORT_ZONE_ID = "unk";
  
  private static final String UNKNOWN_LOCATION = "Unknown";
  
  private static final GMTOffsetPatternType[] PARSE_GMT_OFFSET_TYPES = new GMTOffsetPatternType[] { GMTOffsetPatternType.POSITIVE_HMS, GMTOffsetPatternType.NEGATIVE_HMS, GMTOffsetPatternType.POSITIVE_HM, GMTOffsetPatternType.NEGATIVE_HM, GMTOffsetPatternType.POSITIVE_H, GMTOffsetPatternType.NEGATIVE_H };
  
  private static final int MILLIS_PER_HOUR = 3600000;
  
  private static final int MILLIS_PER_MINUTE = 60000;
  
  private static final int MILLIS_PER_SECOND = 1000;
  
  private static final int MAX_OFFSET = 86400000;
  
  private static final int MAX_OFFSET_HOUR = 23;
  
  private static final int MAX_OFFSET_MINUTE = 59;
  
  private static final int MAX_OFFSET_SECOND = 59;
  
  private static final int UNKNOWN_OFFSET = 2147483647;
  
  private static TimeZoneFormatCache _tzfCache = new TimeZoneFormatCache();
  
  private static final EnumSet<TimeZoneNames.NameType> ALL_SIMPLE_NAME_TYPES = EnumSet.of(TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT, TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT, TimeZoneNames.NameType.EXEMPLAR_LOCATION);
  
  private static final EnumSet<TimeZoneGenericNames.GenericNameType> ALL_GENERIC_NAME_TYPES = EnumSet.of(TimeZoneGenericNames.GenericNameType.LOCATION, TimeZoneGenericNames.GenericNameType.LONG, TimeZoneGenericNames.GenericNameType.SHORT);
  
  private static volatile TextTrieMap<String> ZONE_ID_TRIE;
  
  private static volatile TextTrieMap<String> SHORT_ZONE_ID_TRIE;
  
  protected TimeZoneFormat(ULocale locale) {
    this._locale = locale;
    this._tznames = TimeZoneNames.getInstance(locale);
    String gmtPattern = null;
    String hourFormats = null;
    this._gmtZeroFormat = "GMT";
    try {
      ICUResourceBundle bundle = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/zone", locale);
      try {
        gmtPattern = bundle.getStringWithFallback("zoneStrings/gmtFormat");
      } catch (MissingResourceException e) {}
      try {
        hourFormats = bundle.getStringWithFallback("zoneStrings/hourFormat");
      } catch (MissingResourceException e) {}
      try {
        this._gmtZeroFormat = bundle.getStringWithFallback("zoneStrings/gmtZeroFormat");
      } catch (MissingResourceException e) {}
    } catch (MissingResourceException e) {}
    if (gmtPattern == null)
      gmtPattern = "GMT{0}"; 
    initGMTPattern(gmtPattern);
    String[] gmtOffsetPatterns = new String[(GMTOffsetPatternType.values()).length];
    if (hourFormats != null) {
      String[] hourPatterns = hourFormats.split(";", 2);
      gmtOffsetPatterns[GMTOffsetPatternType.POSITIVE_H.ordinal()] = truncateOffsetPattern(hourPatterns[0]);
      gmtOffsetPatterns[GMTOffsetPatternType.POSITIVE_HM.ordinal()] = hourPatterns[0];
      gmtOffsetPatterns[GMTOffsetPatternType.POSITIVE_HMS.ordinal()] = expandOffsetPattern(hourPatterns[0]);
      gmtOffsetPatterns[GMTOffsetPatternType.NEGATIVE_H.ordinal()] = truncateOffsetPattern(hourPatterns[1]);
      gmtOffsetPatterns[GMTOffsetPatternType.NEGATIVE_HM.ordinal()] = hourPatterns[1];
      gmtOffsetPatterns[GMTOffsetPatternType.NEGATIVE_HMS.ordinal()] = expandOffsetPattern(hourPatterns[1]);
    } else {
      for (GMTOffsetPatternType patType : GMTOffsetPatternType.values())
        gmtOffsetPatterns[patType.ordinal()] = patType.defaultPattern(); 
    } 
    initGMTOffsetPatterns(gmtOffsetPatterns);
    this._gmtOffsetDigits = DEFAULT_GMT_DIGITS;
    NumberingSystem ns = NumberingSystem.getInstance(locale);
    if (!ns.isAlgorithmic())
      this._gmtOffsetDigits = toCodePoints(ns.getDescription()); 
  }
  
  public static TimeZoneFormat getInstance(ULocale locale) {
    if (locale == null)
      throw new NullPointerException("locale is null"); 
    return (TimeZoneFormat)_tzfCache.getInstance(locale, locale);
  }
  
  public TimeZoneNames getTimeZoneNames() {
    return this._tznames;
  }
  
  private TimeZoneGenericNames getTimeZoneGenericNames() {
    if (this._gnames == null)
      synchronized (this) {
        if (this._gnames == null)
          this._gnames = TimeZoneGenericNames.getInstance(this._locale); 
      }  
    return this._gnames;
  }
  
  public TimeZoneFormat setTimeZoneNames(TimeZoneNames tznames) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    this._tznames = tznames;
    this._gnames = new TimeZoneGenericNames(this._locale, this._tznames);
    return this;
  }
  
  public String getGMTPattern() {
    return this._gmtPattern;
  }
  
  public TimeZoneFormat setGMTPattern(String pattern) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    initGMTPattern(pattern);
    return this;
  }
  
  public String getGMTOffsetPattern(GMTOffsetPatternType type) {
    return this._gmtOffsetPatterns[type.ordinal()];
  }
  
  public TimeZoneFormat setGMTOffsetPattern(GMTOffsetPatternType type, String pattern) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    if (pattern == null)
      throw new NullPointerException("Null GMT offset pattern"); 
    Object[] parsedItems = parseOffsetPattern(pattern, type.required());
    this._gmtOffsetPatterns[type.ordinal()] = pattern;
    this._gmtOffsetPatternItems[type.ordinal()] = parsedItems;
    checkAbuttingHoursAndMinutes();
    return this;
  }
  
  public String getGMTOffsetDigits() {
    StringBuilder buf = new StringBuilder(this._gmtOffsetDigits.length);
    for (String digit : this._gmtOffsetDigits)
      buf.append(digit); 
    return buf.toString();
  }
  
  public TimeZoneFormat setGMTOffsetDigits(String digits) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    if (digits == null)
      throw new NullPointerException("Null GMT offset digits"); 
    String[] digitArray = toCodePoints(digits);
    if (digitArray.length != 10)
      throw new IllegalArgumentException("Length of digits must be 10"); 
    this._gmtOffsetDigits = digitArray;
    return this;
  }
  
  public String getGMTZeroFormat() {
    return this._gmtZeroFormat;
  }
  
  public TimeZoneFormat setGMTZeroFormat(String gmtZeroFormat) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    if (gmtZeroFormat == null)
      throw new NullPointerException("Null GMT zero format"); 
    if (gmtZeroFormat.length() == 0)
      throw new IllegalArgumentException("Empty GMT zero format"); 
    this._gmtZeroFormat = gmtZeroFormat;
    return this;
  }
  
  public TimeZoneFormat setDefaultParseOptions(EnumSet<ParseOption> options) {
    this._parseAllStyles = options.contains(ParseOption.ALL_STYLES);
    return this;
  }
  
  public EnumSet<ParseOption> getDefaultParseOptions() {
    if (this._parseAllStyles)
      return EnumSet.of(ParseOption.ALL_STYLES); 
    return EnumSet.noneOf(ParseOption.class);
  }
  
  public final String formatOffsetISO8601Basic(int offset, boolean useUtcIndicator, boolean isShort, boolean ignoreSeconds) {
    return formatOffsetISO8601(offset, true, useUtcIndicator, isShort, ignoreSeconds);
  }
  
  public final String formatOffsetISO8601Extended(int offset, boolean useUtcIndicator, boolean isShort, boolean ignoreSeconds) {
    return formatOffsetISO8601(offset, false, useUtcIndicator, isShort, ignoreSeconds);
  }
  
  public String formatOffsetLocalizedGMT(int offset) {
    return formatOffsetLocalizedGMT(offset, false);
  }
  
  public String formatOffsetShortLocalizedGMT(int offset) {
    return formatOffsetLocalizedGMT(offset, true);
  }
  
  public final String format(Style style, TimeZone tz, long date) {
    return format(style, tz, date, null);
  }
  
  public String format(Style style, TimeZone tz, long date, Output<TimeType> timeType) {
    String result = null;
    if (timeType != null)
      timeType.value = TimeType.UNKNOWN; 
    switch (style) {
      case LONG_STANDARD:
        result = getTimeZoneGenericNames().getGenericLocationName(ZoneMeta.getCanonicalCLDRID(tz));
        break;
      case SHORT_STANDARD:
        result = getTimeZoneGenericNames().getDisplayName(tz, TimeZoneGenericNames.GenericNameType.LONG, date);
        break;
      case LONG_DAYLIGHT:
        result = getTimeZoneGenericNames().getDisplayName(tz, TimeZoneGenericNames.GenericNameType.SHORT, date);
        break;
      case SHORT_DAYLIGHT:
        result = formatSpecific(tz, TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT, date, timeType);
        break;
      case null:
        result = formatSpecific(tz, TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT, date, timeType);
        break;
    } 
    if (result == null) {
      int[] offsets = { 0, 0 };
      tz.getOffset(date, false, offsets);
      int offset = offsets[0] + offsets[1];
      switch (style) {
        case LONG_STANDARD:
        case SHORT_STANDARD:
        case SHORT_DAYLIGHT:
        case null:
          result = formatOffsetLocalizedGMT(offset);
          break;
        case LONG_DAYLIGHT:
        case null:
        case null:
          result = formatOffsetShortLocalizedGMT(offset);
          break;
        case null:
          result = formatOffsetISO8601Basic(offset, true, true, true);
          break;
        case null:
          result = formatOffsetISO8601Basic(offset, false, true, true);
          break;
        case null:
          result = formatOffsetISO8601Basic(offset, true, false, true);
          break;
        case null:
          result = formatOffsetISO8601Basic(offset, false, false, true);
          break;
        case null:
          result = formatOffsetISO8601Basic(offset, true, false, false);
          break;
        case null:
          result = formatOffsetISO8601Basic(offset, false, false, false);
          break;
        case null:
          result = formatOffsetISO8601Extended(offset, true, false, true);
          break;
        case null:
          result = formatOffsetISO8601Extended(offset, false, false, true);
          break;
        case null:
          result = formatOffsetISO8601Extended(offset, true, false, false);
          break;
        case null:
          result = formatOffsetISO8601Extended(offset, false, false, false);
          break;
        case null:
          result = tz.getID();
          break;
        case null:
          result = ZoneMeta.getShortID(tz);
          if (result == null)
            result = "unk"; 
          break;
        case null:
          result = formatExemplarLocation(tz);
          break;
      } 
      if (timeType != null)
        timeType.value = (offsets[1] != 0) ? TimeType.DAYLIGHT : TimeType.STANDARD; 
    } 
    assert result != null;
    return result;
  }
  
  public final int parseOffsetISO8601(String text, ParsePosition pos) {
    return parseOffsetISO8601(text, pos, false, null);
  }
  
  public int parseOffsetLocalizedGMT(String text, ParsePosition pos) {
    return parseOffsetLocalizedGMT(text, pos, false, null);
  }
  
  public int parseOffsetShortLocalizedGMT(String text, ParsePosition pos) {
    return parseOffsetLocalizedGMT(text, pos, true, null);
  }
  
  public TimeZone parse(Style style, String text, ParsePosition pos, EnumSet<ParseOption> options, Output<TimeType> timeType) {
    int offset;
    Output<Boolean> hasDigitOffset;
    EnumSet<TimeZoneNames.NameType> nameTypes;
    EnumSet<TimeZoneGenericNames.GenericNameType> genericNameTypes;
    String id;
    Collection<TimeZoneNames.MatchInfo> specificMatches;
    TimeZoneGenericNames.GenericMatchInfo bestGeneric;
    if (timeType == null) {
      timeType = new Output(TimeType.UNKNOWN);
    } else {
      timeType.value = TimeType.UNKNOWN;
    } 
    int startIdx = pos.getIndex();
    int maxPos = text.length();
    boolean fallbackLocalizedGMT = (style == Style.SPECIFIC_LONG || style == Style.GENERIC_LONG || style == Style.GENERIC_LOCATION);
    boolean fallbackShortLocalizedGMT = (style == Style.SPECIFIC_SHORT || style == Style.GENERIC_SHORT);
    int evaluated = 0;
    ParsePosition tmpPos = new ParsePosition(startIdx);
    int parsedOffset = Integer.MAX_VALUE;
    int parsedPos = -1;
    if (fallbackLocalizedGMT || fallbackShortLocalizedGMT) {
      Output<Boolean> output = new Output(Boolean.valueOf(false));
      int i = parseOffsetLocalizedGMT(text, tmpPos, fallbackShortLocalizedGMT, output);
      if (tmpPos.getErrorIndex() == -1) {
        if (tmpPos.getIndex() == maxPos || ((Boolean)output.value).booleanValue()) {
          pos.setIndex(tmpPos.getIndex());
          return getTimeZoneForOffset(i);
        } 
        parsedOffset = i;
        parsedPos = tmpPos.getIndex();
      } 
      evaluated |= Style.LOCALIZED_GMT.flag | Style.LOCALIZED_GMT_SHORT.flag;
    } 
    switch (style) {
      case null:
        tmpPos.setIndex(startIdx);
        tmpPos.setErrorIndex(-1);
        offset = parseOffsetLocalizedGMT(text, tmpPos);
        if (tmpPos.getErrorIndex() == -1) {
          pos.setIndex(tmpPos.getIndex());
          return getTimeZoneForOffset(offset);
        } 
        evaluated |= Style.LOCALIZED_GMT_SHORT.flag;
        break;
      case null:
        tmpPos.setIndex(startIdx);
        tmpPos.setErrorIndex(-1);
        offset = parseOffsetShortLocalizedGMT(text, tmpPos);
        if (tmpPos.getErrorIndex() == -1) {
          pos.setIndex(tmpPos.getIndex());
          return getTimeZoneForOffset(offset);
        } 
        evaluated |= Style.LOCALIZED_GMT.flag;
        break;
      case null:
      case null:
      case null:
      case null:
      case null:
        tmpPos.setIndex(startIdx);
        tmpPos.setErrorIndex(-1);
        offset = parseOffsetISO8601(text, tmpPos);
        if (tmpPos.getErrorIndex() == -1) {
          pos.setIndex(tmpPos.getIndex());
          return getTimeZoneForOffset(offset);
        } 
        break;
      case null:
      case null:
      case null:
      case null:
      case null:
        tmpPos.setIndex(startIdx);
        tmpPos.setErrorIndex(-1);
        hasDigitOffset = new Output(Boolean.valueOf(false));
        offset = parseOffsetISO8601(text, tmpPos, false, hasDigitOffset);
        if (tmpPos.getErrorIndex() == -1 && ((Boolean)hasDigitOffset.value).booleanValue()) {
          pos.setIndex(tmpPos.getIndex());
          return getTimeZoneForOffset(offset);
        } 
        break;
      case SHORT_DAYLIGHT:
      case null:
        nameTypes = null;
        if (style == Style.SPECIFIC_LONG) {
          nameTypes = EnumSet.of(TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT);
        } else {
          assert style == Style.SPECIFIC_SHORT;
          nameTypes = EnumSet.of(TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT);
        } 
        specificMatches = this._tznames.find(text, startIdx, nameTypes);
        if (specificMatches != null) {
          TimeZoneNames.MatchInfo specificMatch = null;
          for (TimeZoneNames.MatchInfo match : specificMatches) {
            if (startIdx + match.matchLength() > parsedPos) {
              specificMatch = match;
              parsedPos = startIdx + match.matchLength();
            } 
          } 
          if (specificMatch != null) {
            timeType.value = getTimeType(specificMatch.nameType());
            pos.setIndex(parsedPos);
            return TimeZone.getTimeZone(getTimeZoneID(specificMatch.tzID(), specificMatch.mzID()));
          } 
        } 
        break;
      case LONG_STANDARD:
      case SHORT_STANDARD:
      case LONG_DAYLIGHT:
        genericNameTypes = null;
        switch (style) {
          case LONG_STANDARD:
            genericNameTypes = EnumSet.of(TimeZoneGenericNames.GenericNameType.LOCATION);
            break;
          case SHORT_STANDARD:
            genericNameTypes = EnumSet.of(TimeZoneGenericNames.GenericNameType.LONG, TimeZoneGenericNames.GenericNameType.LOCATION);
            break;
          case LONG_DAYLIGHT:
            genericNameTypes = EnumSet.of(TimeZoneGenericNames.GenericNameType.SHORT, TimeZoneGenericNames.GenericNameType.LOCATION);
            break;
        } 
        bestGeneric = getTimeZoneGenericNames().findBestMatch(text, startIdx, genericNameTypes);
        if (bestGeneric != null && startIdx + bestGeneric.matchLength() > parsedPos) {
          timeType.value = bestGeneric.timeType();
          pos.setIndex(startIdx + bestGeneric.matchLength());
          return TimeZone.getTimeZone(bestGeneric.tzID());
        } 
        break;
      case null:
        tmpPos.setIndex(startIdx);
        tmpPos.setErrorIndex(-1);
        id = parseZoneID(text, tmpPos);
        if (tmpPos.getErrorIndex() == -1) {
          pos.setIndex(tmpPos.getIndex());
          return TimeZone.getTimeZone(id);
        } 
        break;
      case null:
        tmpPos.setIndex(startIdx);
        tmpPos.setErrorIndex(-1);
        id = parseShortZoneID(text, tmpPos);
        if (tmpPos.getErrorIndex() == -1) {
          pos.setIndex(tmpPos.getIndex());
          return TimeZone.getTimeZone(id);
        } 
        break;
      case null:
        tmpPos.setIndex(startIdx);
        tmpPos.setErrorIndex(-1);
        id = parseExemplarLocation(text, tmpPos);
        if (tmpPos.getErrorIndex() == -1) {
          pos.setIndex(tmpPos.getIndex());
          return TimeZone.getTimeZone(id);
        } 
        break;
    } 
    evaluated |= style.flag;
    if (parsedPos > startIdx) {
      assert parsedOffset != Integer.MAX_VALUE;
      pos.setIndex(parsedPos);
      return getTimeZoneForOffset(parsedOffset);
    } 
    String parsedID = null;
    TimeType parsedTimeType = TimeType.UNKNOWN;
    assert parsedPos < 0;
    assert parsedOffset == Integer.MAX_VALUE;
    if (parsedPos < maxPos && ((evaluated & 0x80) == 0 || (evaluated & 0x100) == 0)) {
      tmpPos.setIndex(startIdx);
      tmpPos.setErrorIndex(-1);
      Output<Boolean> output = new Output(Boolean.valueOf(false));
      offset = parseOffsetISO8601(text, tmpPos, false, output);
      if (tmpPos.getErrorIndex() == -1) {
        if (tmpPos.getIndex() == maxPos || ((Boolean)output.value).booleanValue()) {
          pos.setIndex(tmpPos.getIndex());
          return getTimeZoneForOffset(offset);
        } 
        if (parsedPos < tmpPos.getIndex()) {
          parsedOffset = offset;
          parsedID = null;
          parsedTimeType = TimeType.UNKNOWN;
          parsedPos = tmpPos.getIndex();
          assert parsedPos == startIdx + 1;
        } 
      } 
    } 
    if (parsedPos < maxPos && (evaluated & Style.LOCALIZED_GMT.flag) == 0) {
      tmpPos.setIndex(startIdx);
      tmpPos.setErrorIndex(-1);
      Output<Boolean> output = new Output(Boolean.valueOf(false));
      offset = parseOffsetLocalizedGMT(text, tmpPos, false, output);
      if (tmpPos.getErrorIndex() == -1) {
        if (tmpPos.getIndex() == maxPos || ((Boolean)output.value).booleanValue()) {
          pos.setIndex(tmpPos.getIndex());
          return getTimeZoneForOffset(offset);
        } 
        if (parsedPos < tmpPos.getIndex()) {
          parsedOffset = offset;
          parsedID = null;
          parsedTimeType = TimeType.UNKNOWN;
          parsedPos = tmpPos.getIndex();
        } 
      } 
    } 
    if (parsedPos < maxPos && (evaluated & Style.LOCALIZED_GMT_SHORT.flag) == 0) {
      tmpPos.setIndex(startIdx);
      tmpPos.setErrorIndex(-1);
      Output<Boolean> output = new Output(Boolean.valueOf(false));
      offset = parseOffsetLocalizedGMT(text, tmpPos, true, output);
      if (tmpPos.getErrorIndex() == -1) {
        if (tmpPos.getIndex() == maxPos || ((Boolean)output.value).booleanValue()) {
          pos.setIndex(tmpPos.getIndex());
          return getTimeZoneForOffset(offset);
        } 
        if (parsedPos < tmpPos.getIndex()) {
          parsedOffset = offset;
          parsedID = null;
          parsedTimeType = TimeType.UNKNOWN;
          parsedPos = tmpPos.getIndex();
        } 
      } 
    } 
    boolean parseAllStyles = (options == null) ? getDefaultParseOptions().contains(ParseOption.ALL_STYLES) : options.contains(ParseOption.ALL_STYLES);
    if (parseAllStyles) {
      if (parsedPos < maxPos) {
        Collection<TimeZoneNames.MatchInfo> collection = this._tznames.find(text, startIdx, ALL_SIMPLE_NAME_TYPES);
        TimeZoneNames.MatchInfo specificMatch = null;
        int matchPos = -1;
        if (collection != null)
          for (TimeZoneNames.MatchInfo match : collection) {
            if (startIdx + match.matchLength() > matchPos) {
              specificMatch = match;
              matchPos = startIdx + match.matchLength();
            } 
          }  
        if (parsedPos < matchPos) {
          parsedPos = matchPos;
          parsedID = getTimeZoneID(specificMatch.tzID(), specificMatch.mzID());
          parsedTimeType = getTimeType(specificMatch.nameType());
          parsedOffset = Integer.MAX_VALUE;
        } 
      } 
      if (parsedPos < maxPos) {
        TimeZoneGenericNames.GenericMatchInfo genericMatch = getTimeZoneGenericNames().findBestMatch(text, startIdx, ALL_GENERIC_NAME_TYPES);
        if (genericMatch != null && parsedPos < startIdx + genericMatch.matchLength()) {
          parsedPos = startIdx + genericMatch.matchLength();
          parsedID = genericMatch.tzID();
          parsedTimeType = genericMatch.timeType();
          parsedOffset = Integer.MAX_VALUE;
        } 
      } 
      if (parsedPos < maxPos && (evaluated & Style.ZONE_ID.flag) == 0) {
        tmpPos.setIndex(startIdx);
        tmpPos.setErrorIndex(-1);
        String str = parseZoneID(text, tmpPos);
        if (tmpPos.getErrorIndex() == -1 && parsedPos < tmpPos.getIndex()) {
          parsedPos = tmpPos.getIndex();
          parsedID = str;
          parsedTimeType = TimeType.UNKNOWN;
          parsedOffset = Integer.MAX_VALUE;
        } 
      } 
      if (parsedPos < maxPos && (evaluated & Style.ZONE_ID_SHORT.flag) == 0) {
        tmpPos.setIndex(startIdx);
        tmpPos.setErrorIndex(-1);
        String str = parseShortZoneID(text, tmpPos);
        if (tmpPos.getErrorIndex() == -1 && parsedPos < tmpPos.getIndex()) {
          parsedPos = tmpPos.getIndex();
          parsedID = str;
          parsedTimeType = TimeType.UNKNOWN;
          parsedOffset = Integer.MAX_VALUE;
        } 
      } 
    } 
    if (parsedPos > startIdx) {
      TimeZone parsedTZ = null;
      if (parsedID != null) {
        parsedTZ = TimeZone.getTimeZone(parsedID);
      } else {
        assert parsedOffset != Integer.MAX_VALUE;
        parsedTZ = getTimeZoneForOffset(parsedOffset);
      } 
      timeType.value = parsedTimeType;
      pos.setIndex(parsedPos);
      return parsedTZ;
    } 
    pos.setErrorIndex(startIdx);
    return null;
  }
  
  public TimeZone parse(Style style, String text, ParsePosition pos, Output<TimeType> timeType) {
    return parse(style, text, pos, null, timeType);
  }
  
  public final TimeZone parse(String text, ParsePosition pos) {
    return parse(Style.GENERIC_LOCATION, text, pos, EnumSet.of(ParseOption.ALL_STYLES), null);
  }
  
  public final TimeZone parse(String text) throws ParseException {
    ParsePosition pos = new ParsePosition(0);
    TimeZone tz = parse(text, pos);
    if (pos.getErrorIndex() >= 0)
      throw new ParseException("Unparseable time zone: \"" + text + "\"", 0); 
    assert tz != null;
    return tz;
  }
  
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
    TimeZone tz = null;
    long date = System.currentTimeMillis();
    if (obj instanceof TimeZone) {
      tz = (TimeZone)obj;
    } else if (obj instanceof Calendar) {
      tz = ((Calendar)obj).getTimeZone();
      date = ((Calendar)obj).getTimeInMillis();
    } else {
      throw new IllegalArgumentException("Cannot format given Object (" + obj.getClass().getName() + ") as a time zone");
    } 
    assert tz != null;
    String result = formatOffsetLocalizedGMT(tz.getOffset(date));
    toAppendTo.append(result);
    if (pos.getFieldAttribute() == DateFormat.Field.TIME_ZONE || pos.getField() == 17) {
      pos.setBeginIndex(0);
      pos.setEndIndex(result.length());
    } 
    return toAppendTo;
  }
  
  public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
    StringBuffer toAppendTo = new StringBuffer();
    FieldPosition pos = new FieldPosition(0);
    toAppendTo = format(obj, toAppendTo, pos);
    AttributedString as = new AttributedString(toAppendTo.toString());
    as.addAttribute(DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE);
    return as.getIterator();
  }
  
  public Object parseObject(String source, ParsePosition pos) {
    return parse(source, pos);
  }
  
  private String formatOffsetLocalizedGMT(int offset, boolean isShort) {
    Object[] offsetPatternItems;
    if (offset == 0)
      return this._gmtZeroFormat; 
    StringBuilder buf = new StringBuilder();
    boolean positive = true;
    if (offset < 0) {
      offset = -offset;
      positive = false;
    } 
    int offsetH = offset / 3600000;
    offset %= 3600000;
    int offsetM = offset / 60000;
    offset %= 60000;
    int offsetS = offset / 1000;
    if (offsetH > 23 || offsetM > 59 || offsetS > 59)
      throw new IllegalArgumentException("Offset out of range :" + offset); 
    if (positive) {
      if (offsetS != 0) {
        offsetPatternItems = this._gmtOffsetPatternItems[GMTOffsetPatternType.POSITIVE_HMS.ordinal()];
      } else if (offsetM != 0 || !isShort) {
        offsetPatternItems = this._gmtOffsetPatternItems[GMTOffsetPatternType.POSITIVE_HM.ordinal()];
      } else {
        offsetPatternItems = this._gmtOffsetPatternItems[GMTOffsetPatternType.POSITIVE_H.ordinal()];
      } 
    } else if (offsetS != 0) {
      offsetPatternItems = this._gmtOffsetPatternItems[GMTOffsetPatternType.NEGATIVE_HMS.ordinal()];
    } else if (offsetM != 0 || !isShort) {
      offsetPatternItems = this._gmtOffsetPatternItems[GMTOffsetPatternType.NEGATIVE_HM.ordinal()];
    } else {
      offsetPatternItems = this._gmtOffsetPatternItems[GMTOffsetPatternType.NEGATIVE_H.ordinal()];
    } 
    buf.append(this._gmtPatternPrefix);
    for (Object item : offsetPatternItems) {
      if (item instanceof String) {
        buf.append((String)item);
      } else if (item instanceof GMTOffsetField) {
        GMTOffsetField field = (GMTOffsetField)item;
        switch (field.getType()) {
          case 'H':
            appendOffsetDigits(buf, offsetH, isShort ? 1 : 2);
            break;
          case 'm':
            appendOffsetDigits(buf, offsetM, 2);
            break;
          case 's':
            appendOffsetDigits(buf, offsetS, 2);
            break;
        } 
      } 
    } 
    buf.append(this._gmtPatternSuffix);
    return buf.toString();
  }
  
  private enum OffsetFields {
    H, HM, HMS;
  }
  
  private String formatOffsetISO8601(int offset, boolean isBasic, boolean useUtcIndicator, boolean isShort, boolean ignoreSeconds) {
    int absOffset = (offset < 0) ? -offset : offset;
    if (useUtcIndicator && (absOffset < 1000 || (ignoreSeconds && absOffset < 60000)))
      return "Z"; 
    OffsetFields minFields = isShort ? OffsetFields.H : OffsetFields.HM;
    OffsetFields maxFields = ignoreSeconds ? OffsetFields.HM : OffsetFields.HMS;
    Character sep = isBasic ? null : Character.valueOf(':');
    if (absOffset >= 86400000)
      throw new IllegalArgumentException("Offset out of range :" + offset); 
    int[] fields = new int[3];
    fields[0] = absOffset / 3600000;
    absOffset %= 3600000;
    fields[1] = absOffset / 60000;
    absOffset %= 60000;
    fields[2] = absOffset / 1000;
    assert fields[0] >= 0 && fields[0] <= 23;
    assert fields[1] >= 0 && fields[1] <= 59;
    assert fields[2] >= 0 && fields[2] <= 59;
    int lastIdx = maxFields.ordinal();
    while (lastIdx > minFields.ordinal() && 
      fields[lastIdx] == 0)
      lastIdx--; 
    StringBuilder buf = new StringBuilder();
    char sign = '+';
    if (offset < 0)
      for (int i = 0; i <= lastIdx; i++) {
        if (fields[i] != 0) {
          sign = '-';
          break;
        } 
      }  
    buf.append(sign);
    for (int idx = 0; idx <= lastIdx; idx++) {
      if (sep != null && idx != 0)
        buf.append(sep); 
      if (fields[idx] < 10)
        buf.append('0'); 
      buf.append(fields[idx]);
    } 
    return buf.toString();
  }
  
  private String formatSpecific(TimeZone tz, TimeZoneNames.NameType stdType, TimeZoneNames.NameType dstType, long date, Output<TimeType> timeType) {
    assert stdType == TimeZoneNames.NameType.LONG_STANDARD || stdType == TimeZoneNames.NameType.SHORT_STANDARD;
    assert dstType == TimeZoneNames.NameType.LONG_DAYLIGHT || dstType == TimeZoneNames.NameType.SHORT_DAYLIGHT;
    boolean isDaylight = tz.inDaylightTime(new Date(date));
    String name = isDaylight ? getTimeZoneNames().getDisplayName(ZoneMeta.getCanonicalCLDRID(tz), dstType, date) : getTimeZoneNames().getDisplayName(ZoneMeta.getCanonicalCLDRID(tz), stdType, date);
    if (name != null && timeType != null)
      timeType.value = isDaylight ? TimeType.DAYLIGHT : TimeType.STANDARD; 
    return name;
  }
  
  private String formatExemplarLocation(TimeZone tz) {
    String location = getTimeZoneNames().getExemplarLocationName(ZoneMeta.getCanonicalCLDRID(tz));
    if (location == null) {
      location = getTimeZoneNames().getExemplarLocationName("Etc/Unknown");
      if (location == null)
        location = "Unknown"; 
    } 
    return location;
  }
  
  private String getTimeZoneID(String tzID, String mzID) {
    String id = tzID;
    if (id == null) {
      assert mzID != null;
      id = this._tznames.getReferenceZoneID(mzID, getTargetRegion());
      if (id == null)
        throw new IllegalArgumentException("Invalid mzID: " + mzID); 
    } 
    return id;
  }
  
  private synchronized String getTargetRegion() {
    if (this._region == null) {
      this._region = this._locale.getCountry();
      if (this._region.length() == 0) {
        ULocale tmp = ULocale.addLikelySubtags(this._locale);
        this._region = tmp.getCountry();
        if (this._region.length() == 0)
          this._region = "001"; 
      } 
    } 
    return this._region;
  }
  
  private TimeType getTimeType(TimeZoneNames.NameType nameType) {
    switch (nameType) {
      case LONG_STANDARD:
      case SHORT_STANDARD:
        return TimeType.STANDARD;
      case LONG_DAYLIGHT:
      case SHORT_DAYLIGHT:
        return TimeType.DAYLIGHT;
    } 
    return TimeType.UNKNOWN;
  }
  
  private void initGMTPattern(String gmtPattern) {
    int idx = gmtPattern.indexOf("{0}");
    if (idx < 0)
      throw new IllegalArgumentException("Bad localized GMT pattern: " + gmtPattern); 
    this._gmtPattern = gmtPattern;
    this._gmtPatternPrefix = unquote(gmtPattern.substring(0, idx));
    this._gmtPatternSuffix = unquote(gmtPattern.substring(idx + 3));
  }
  
  private static String unquote(String s) {
    if (s.indexOf('\'') < 0)
      return s; 
    boolean isPrevQuote = false;
    boolean inQuote = false;
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == '\'') {
        if (isPrevQuote) {
          buf.append(c);
          isPrevQuote = false;
        } else {
          isPrevQuote = true;
        } 
        inQuote = !inQuote;
      } else {
        isPrevQuote = false;
        buf.append(c);
      } 
    } 
    return buf.toString();
  }
  
  private void initGMTOffsetPatterns(String[] gmtOffsetPatterns) {
    int size = (GMTOffsetPatternType.values()).length;
    if (gmtOffsetPatterns.length < size)
      throw new IllegalArgumentException("Insufficient number of elements in gmtOffsetPatterns"); 
    Object[][] gmtOffsetPatternItems = new Object[size][];
    for (GMTOffsetPatternType t : GMTOffsetPatternType.values()) {
      int idx = t.ordinal();
      Object[] parsedItems = parseOffsetPattern(gmtOffsetPatterns[idx], t.required());
      gmtOffsetPatternItems[idx] = parsedItems;
    } 
    this._gmtOffsetPatterns = new String[size];
    System.arraycopy(gmtOffsetPatterns, 0, this._gmtOffsetPatterns, 0, size);
    this._gmtOffsetPatternItems = gmtOffsetPatternItems;
    checkAbuttingHoursAndMinutes();
  }
  
  private void checkAbuttingHoursAndMinutes() {
    this._abuttingOffsetHoursAndMinutes = false;
    for (Object[] items : this._gmtOffsetPatternItems) {
      boolean afterH = false;
      for (Object item : items) {
        if (item instanceof GMTOffsetField) {
          GMTOffsetField fld = (GMTOffsetField)item;
          if (afterH) {
            this._abuttingOffsetHoursAndMinutes = true;
          } else if (fld.getType() == 'H') {
            afterH = true;
          } 
        } else if (afterH) {
          break;
        } 
      } 
    } 
  }
  
  private static class GMTOffsetField {
    final char _type;
    
    final int _width;
    
    GMTOffsetField(char type, int width) {
      this._type = type;
      this._width = width;
    }
    
    char getType() {
      return this._type;
    }
    
    int getWidth() {
      return this._width;
    }
    
    static boolean isValid(char type, int width) {
      return (width == 1 || width == 2);
    }
  }
  
  private static Object[] parseOffsetPattern(String pattern, String letters) {
    boolean isPrevQuote = false;
    boolean inQuote = false;
    StringBuilder text = new StringBuilder();
    char itemType = Character.MIN_VALUE;
    int itemLength = 1;
    boolean invalidPattern = false;
    List<Object> items = new ArrayList();
    BitSet checkBits = new BitSet(letters.length());
    for (int i = 0; i < pattern.length(); i++) {
      char ch = pattern.charAt(i);
      if (ch == '\'') {
        if (isPrevQuote) {
          text.append('\'');
          isPrevQuote = false;
        } else {
          isPrevQuote = true;
          if (itemType != '\000') {
            if (GMTOffsetField.isValid(itemType, itemLength)) {
              items.add(new GMTOffsetField(itemType, itemLength));
            } else {
              invalidPattern = true;
              break;
            } 
            itemType = Character.MIN_VALUE;
          } 
        } 
        inQuote = !inQuote;
      } else {
        isPrevQuote = false;
        if (inQuote) {
          text.append(ch);
        } else {
          int patFieldIdx = letters.indexOf(ch);
          if (patFieldIdx >= 0) {
            if (ch == itemType) {
              itemLength++;
            } else {
              if (itemType == '\000') {
                if (text.length() > 0) {
                  items.add(text.toString());
                  text.setLength(0);
                } 
              } else if (GMTOffsetField.isValid(itemType, itemLength)) {
                items.add(new GMTOffsetField(itemType, itemLength));
              } else {
                invalidPattern = true;
                break;
              } 
              itemType = ch;
              itemLength = 1;
              checkBits.set(patFieldIdx);
            } 
          } else {
            if (itemType != '\000') {
              if (GMTOffsetField.isValid(itemType, itemLength)) {
                items.add(new GMTOffsetField(itemType, itemLength));
              } else {
                invalidPattern = true;
                break;
              } 
              itemType = Character.MIN_VALUE;
            } 
            text.append(ch);
          } 
        } 
      } 
    } 
    if (!invalidPattern)
      if (itemType == '\000') {
        if (text.length() > 0) {
          items.add(text.toString());
          text.setLength(0);
        } 
      } else if (GMTOffsetField.isValid(itemType, itemLength)) {
        items.add(new GMTOffsetField(itemType, itemLength));
      } else {
        invalidPattern = true;
      }  
    if (invalidPattern || checkBits.cardinality() != letters.length())
      throw new IllegalStateException("Bad localized GMT offset pattern: " + pattern); 
    return items.toArray(new Object[items.size()]);
  }
  
  private static String expandOffsetPattern(String offsetHM) {
    int idx_mm = offsetHM.indexOf("mm");
    if (idx_mm < 0)
      throw new RuntimeException("Bad time zone hour pattern data"); 
    String sep = ":";
    int idx_H = offsetHM.substring(0, idx_mm).lastIndexOf("H");
    if (idx_H >= 0)
      sep = offsetHM.substring(idx_H + 1, idx_mm); 
    return offsetHM.substring(0, idx_mm + 2) + sep + "ss" + offsetHM.substring(idx_mm + 2);
  }
  
  private static String truncateOffsetPattern(String offsetHM) {
    int idx_mm = offsetHM.indexOf("mm");
    if (idx_mm < 0)
      throw new RuntimeException("Bad time zone hour pattern data"); 
    int idx_HH = offsetHM.substring(0, idx_mm).lastIndexOf("HH");
    if (idx_HH >= 0)
      return offsetHM.substring(0, idx_HH + 2); 
    int idx_H = offsetHM.substring(0, idx_mm).lastIndexOf("H");
    if (idx_H >= 0)
      return offsetHM.substring(0, idx_H + 1); 
    throw new RuntimeException("Bad time zone hour pattern data");
  }
  
  private void appendOffsetDigits(StringBuilder buf, int n, int minDigits) {
    assert n >= 0 && n < 60;
    int numDigits = (n >= 10) ? 2 : 1;
    for (int i = 0; i < minDigits - numDigits; i++)
      buf.append(this._gmtOffsetDigits[0]); 
    if (numDigits == 2)
      buf.append(this._gmtOffsetDigits[n / 10]); 
    buf.append(this._gmtOffsetDigits[n % 10]);
  }
  
  private TimeZone getTimeZoneForOffset(int offset) {
    if (offset == 0)
      return TimeZone.getTimeZone("Etc/GMT"); 
    return ZoneMeta.getCustomTimeZone(offset);
  }
  
  private int parseOffsetLocalizedGMT(String text, ParsePosition pos, boolean isShort, Output<Boolean> hasDigitOffset) {
    int start = pos.getIndex();
    int offset = 0;
    int[] parsedLength = { 0 };
    if (hasDigitOffset != null)
      hasDigitOffset.value = Boolean.valueOf(false); 
    offset = parseOffsetLocalizedGMTPattern(text, start, isShort, parsedLength);
    if (parsedLength[0] > 0) {
      if (hasDigitOffset != null)
        hasDigitOffset.value = Boolean.valueOf(true); 
      pos.setIndex(start + parsedLength[0]);
      return offset;
    } 
    offset = parseOffsetDefaultLocalizedGMT(text, start, parsedLength);
    if (parsedLength[0] > 0) {
      if (hasDigitOffset != null)
        hasDigitOffset.value = Boolean.valueOf(true); 
      pos.setIndex(start + parsedLength[0]);
      return offset;
    } 
    if (text.regionMatches(true, start, this._gmtZeroFormat, 0, this._gmtZeroFormat.length())) {
      pos.setIndex(start + this._gmtZeroFormat.length());
      return 0;
    } 
    for (String defGMTZero : ALT_GMT_STRINGS) {
      if (text.regionMatches(true, start, defGMTZero, 0, defGMTZero.length())) {
        pos.setIndex(start + defGMTZero.length());
        return 0;
      } 
    } 
    pos.setErrorIndex(start);
    return 0;
  }
  
  private int parseOffsetLocalizedGMTPattern(String text, int start, boolean isShort, int[] parsedLen) {
    int idx = start;
    int offset = 0;
    boolean parsed = false;
    int len = this._gmtPatternPrefix.length();
    if (len <= 0 || text.regionMatches(true, idx, this._gmtPatternPrefix, 0, len)) {
      idx += len;
      int[] offsetLen = new int[1];
      offset = parseOffsetFields(text, idx, false, offsetLen);
      if (offsetLen[0] != 0) {
        idx += offsetLen[0];
        len = this._gmtPatternSuffix.length();
        if (len <= 0 || text.regionMatches(true, idx, this._gmtPatternSuffix, 0, len)) {
          idx += len;
          parsed = true;
        } 
      } 
    } 
    parsedLen[0] = parsed ? (idx - start) : 0;
    return offset;
  }
  
  private int parseOffsetFields(String text, int start, boolean isShort, int[] parsedLen) {
    int outLen = 0;
    int offset = 0;
    int sign = 1;
    if (parsedLen != null && parsedLen.length >= 1)
      parsedLen[0] = 0; 
    int offsetS = 0, offsetM = offsetS, offsetH = offsetM;
    int[] fields = { 0, 0, 0 };
    for (GMTOffsetPatternType gmtPatType : PARSE_GMT_OFFSET_TYPES) {
      Object[] items = this._gmtOffsetPatternItems[gmtPatType.ordinal()];
      assert items != null;
      outLen = parseOffsetFieldsWithPattern(text, start, items, false, fields);
      if (outLen > 0) {
        sign = gmtPatType.isPositive() ? 1 : -1;
        offsetH = fields[0];
        offsetM = fields[1];
        offsetS = fields[2];
        break;
      } 
    } 
    if (outLen > 0 && this._abuttingOffsetHoursAndMinutes) {
      int tmpLen = 0;
      int tmpSign = 1;
      for (GMTOffsetPatternType gmtPatType : PARSE_GMT_OFFSET_TYPES) {
        Object[] items = this._gmtOffsetPatternItems[gmtPatType.ordinal()];
        assert items != null;
        tmpLen = parseOffsetFieldsWithPattern(text, start, items, true, fields);
        if (tmpLen > 0) {
          tmpSign = gmtPatType.isPositive() ? 1 : -1;
          break;
        } 
      } 
      if (tmpLen > outLen) {
        outLen = tmpLen;
        sign = tmpSign;
        offsetH = fields[0];
        offsetM = fields[1];
        offsetS = fields[2];
      } 
    } 
    if (parsedLen != null && parsedLen.length >= 1)
      parsedLen[0] = outLen; 
    if (outLen > 0)
      offset = ((offsetH * 60 + offsetM) * 60 + offsetS) * 1000 * sign; 
    return offset;
  }
  
  private int parseOffsetFieldsWithPattern(String text, int start, Object[] patternItems, boolean forceSingleHourDigit, int[] fields) {
    assert fields != null && fields.length >= 3;
    fields[2] = 0;
    fields[1] = 0;
    fields[0] = 0;
    boolean failed = false;
    int offsetS = 0, offsetM = offsetS, offsetH = offsetM;
    int idx = start;
    int[] tmpParsedLen = { 0 };
    for (int i = 0; i < patternItems.length; i++) {
      if (patternItems[i] instanceof String) {
        String patStr = (String)patternItems[i];
        int len = patStr.length();
        if (!text.regionMatches(true, idx, patStr, 0, len)) {
          failed = true;
          break;
        } 
        idx += len;
      } else {
        assert patternItems[i] instanceof GMTOffsetField;
        GMTOffsetField field = (GMTOffsetField)patternItems[i];
        char fieldType = field.getType();
        if (fieldType == 'H') {
          int maxDigits = forceSingleHourDigit ? 1 : 2;
          offsetH = parseOffsetFieldWithLocalizedDigits(text, idx, 1, maxDigits, 0, 23, tmpParsedLen);
        } else if (fieldType == 'm') {
          offsetM = parseOffsetFieldWithLocalizedDigits(text, idx, 2, 2, 0, 59, tmpParsedLen);
        } else if (fieldType == 's') {
          offsetS = parseOffsetFieldWithLocalizedDigits(text, idx, 2, 2, 0, 59, tmpParsedLen);
        } 
        if (tmpParsedLen[0] == 0) {
          failed = true;
          break;
        } 
        idx += tmpParsedLen[0];
      } 
    } 
    if (failed)
      return 0; 
    fields[0] = offsetH;
    fields[1] = offsetM;
    fields[2] = offsetS;
    return idx - start;
  }
  
  private int parseOffsetDefaultLocalizedGMT(String text, int start, int[] parsedLen) {
    int idx = start;
    int offset = 0;
    int parsed = 0;
    int gmtLen = 0;
    for (String gmt : ALT_GMT_STRINGS) {
      int len = gmt.length();
      if (text.regionMatches(true, idx, gmt, 0, len)) {
        gmtLen = len;
        break;
      } 
    } 
    if (gmtLen != 0) {
      idx += gmtLen;
      if (idx + 1 < text.length()) {
        int sign = 1;
        char c = text.charAt(idx);
        if (c == '+') {
          sign = 1;
        } else if (c == '-') {
          sign = -1;
        } else {
          parsedLen[0] = parsed;
          return offset;
        } 
        idx++;
        int[] lenWithSep = { 0 };
        int offsetWithSep = parseDefaultOffsetFields(text, idx, ':', lenWithSep);
        if (lenWithSep[0] == text.length() - idx) {
          offset = offsetWithSep * sign;
          idx += lenWithSep[0];
        } else {
          int[] lenAbut = { 0 };
          int offsetAbut = parseAbuttingOffsetFields(text, idx, lenAbut);
          if (lenWithSep[0] > lenAbut[0]) {
            offset = offsetWithSep * sign;
            idx += lenWithSep[0];
          } else {
            offset = offsetAbut * sign;
            idx += lenAbut[0];
          } 
        } 
        parsed = idx - start;
      } 
    } 
    parsedLen[0] = parsed;
    return offset;
  }
  
  private int parseDefaultOffsetFields(String text, int start, char separator, int[] parsedLen) {
    int max = text.length();
    int idx = start;
    int[] len = { 0 };
    int hour = 0, min = 0, sec = 0;
    hour = parseOffsetFieldWithLocalizedDigits(text, idx, 1, 2, 0, 23, len);
    if (len[0] != 0) {
      idx += len[0];
      if (idx + 1 < max && text.charAt(idx) == separator) {
        min = parseOffsetFieldWithLocalizedDigits(text, idx + 1, 2, 2, 0, 59, len);
        if (len[0] != 0) {
          idx += 1 + len[0];
          if (idx + 1 < max && text.charAt(idx) == separator) {
            sec = parseOffsetFieldWithLocalizedDigits(text, idx + 1, 2, 2, 0, 59, len);
            if (len[0] != 0)
              idx += 1 + len[0]; 
          } 
        } 
      } 
    } 
    if (idx == start) {
      parsedLen[0] = 0;
      return 0;
    } 
    parsedLen[0] = idx - start;
    return hour * 3600000 + min * 60000 + sec * 1000;
  }
  
  private int parseAbuttingOffsetFields(String text, int start, int[] parsedLen) {
    int MAXDIGITS = 6;
    int[] digits = new int[6];
    int[] parsed = new int[6];
    int idx = start;
    int[] len = { 0 };
    int numDigits = 0;
    for (int i = 0; i < 6; i++) {
      digits[i] = parseSingleLocalizedDigit(text, idx, len);
      if (digits[i] < 0)
        break; 
      idx += len[0];
      parsed[i] = idx - start;
      numDigits++;
    } 
    if (numDigits == 0) {
      parsedLen[0] = 0;
      return 0;
    } 
    int offset = 0;
    while (numDigits > 0) {
      int hour = 0;
      int min = 0;
      int sec = 0;
      assert numDigits > 0 && numDigits <= 6;
      switch (numDigits) {
        case 1:
          hour = digits[0];
          break;
        case 2:
          hour = digits[0] * 10 + digits[1];
          break;
        case 3:
          hour = digits[0];
          min = digits[1] * 10 + digits[2];
          break;
        case 4:
          hour = digits[0] * 10 + digits[1];
          min = digits[2] * 10 + digits[3];
          break;
        case 5:
          hour = digits[0];
          min = digits[1] * 10 + digits[2];
          sec = digits[3] * 10 + digits[4];
          break;
        case 6:
          hour = digits[0] * 10 + digits[1];
          min = digits[2] * 10 + digits[3];
          sec = digits[4] * 10 + digits[5];
          break;
      } 
      if (hour <= 23 && min <= 59 && sec <= 59) {
        offset = hour * 3600000 + min * 60000 + sec * 1000;
        parsedLen[0] = parsed[numDigits - 1];
        break;
      } 
      numDigits--;
    } 
    return offset;
  }
  
  private int parseOffsetFieldWithLocalizedDigits(String text, int start, int minDigits, int maxDigits, int minVal, int maxVal, int[] parsedLen) {
    parsedLen[0] = 0;
    int decVal = 0;
    int numDigits = 0;
    int idx = start;
    int[] digitLen = { 0 };
    while (idx < text.length() && numDigits < maxDigits) {
      int digit = parseSingleLocalizedDigit(text, idx, digitLen);
      if (digit < 0)
        break; 
      int tmpVal = decVal * 10 + digit;
      if (tmpVal > maxVal)
        break; 
      decVal = tmpVal;
      numDigits++;
      idx += digitLen[0];
    } 
    if (numDigits < minDigits || decVal < minVal) {
      decVal = -1;
      numDigits = 0;
    } else {
      parsedLen[0] = idx - start;
    } 
    return decVal;
  }
  
  private int parseSingleLocalizedDigit(String text, int start, int[] len) {
    int digit = -1;
    len[0] = 0;
    if (start < text.length()) {
      int cp = Character.codePointAt(text, start);
      for (int i = 0; i < this._gmtOffsetDigits.length; i++) {
        if (cp == this._gmtOffsetDigits[i].codePointAt(0)) {
          digit = i;
          break;
        } 
      } 
      if (digit < 0)
        digit = UCharacter.digit(cp); 
      if (digit >= 0)
        len[0] = Character.charCount(cp); 
    } 
    return digit;
  }
  
  private static String[] toCodePoints(String str) {
    int len = str.codePointCount(0, str.length());
    String[] codePoints = new String[len];
    for (int i = 0, offset = 0; i < len; i++) {
      int code = str.codePointAt(offset);
      int codeLen = Character.charCount(code);
      codePoints[i] = str.substring(offset, offset + codeLen);
      offset += codeLen;
    } 
    return codePoints;
  }
  
  private static int parseOffsetISO8601(String text, ParsePosition pos, boolean extendedOnly, Output<Boolean> hasDigitOffset) {
    int sign;
    if (hasDigitOffset != null)
      hasDigitOffset.value = Boolean.valueOf(false); 
    int start = pos.getIndex();
    if (start >= text.length()) {
      pos.setErrorIndex(start);
      return 0;
    } 
    char firstChar = text.charAt(start);
    if (Character.toUpperCase(firstChar) == "Z".charAt(0)) {
      pos.setIndex(start + 1);
      return 0;
    } 
    if (firstChar == '+') {
      sign = 1;
    } else if (firstChar == '-') {
      sign = -1;
    } else {
      pos.setErrorIndex(start);
      return 0;
    } 
    ParsePosition posOffset = new ParsePosition(start + 1);
    int offset = parseAsciiOffsetFields(text, posOffset, ':', OffsetFields.H, OffsetFields.HMS);
    if (posOffset.getErrorIndex() == -1 && !extendedOnly && posOffset.getIndex() - start <= 3) {
      ParsePosition posBasic = new ParsePosition(start + 1);
      int tmpOffset = parseAbuttingAsciiOffsetFields(text, posBasic, OffsetFields.H, OffsetFields.HMS, false);
      if (posBasic.getErrorIndex() == -1 && posBasic.getIndex() > posOffset.getIndex()) {
        offset = tmpOffset;
        posOffset.setIndex(posBasic.getIndex());
      } 
    } 
    if (posOffset.getErrorIndex() != -1) {
      pos.setErrorIndex(start);
      return 0;
    } 
    pos.setIndex(posOffset.getIndex());
    if (hasDigitOffset != null)
      hasDigitOffset.value = Boolean.valueOf(true); 
    return sign * offset;
  }
  
  private static int parseAbuttingAsciiOffsetFields(String text, ParsePosition pos, OffsetFields minFields, OffsetFields maxFields, boolean fixedHourWidth) {
    int start = pos.getIndex();
    int minDigits = 2 * (minFields.ordinal() + 1) - (fixedHourWidth ? 0 : 1);
    int maxDigits = 2 * (maxFields.ordinal() + 1);
    int[] digits = new int[maxDigits];
    int numDigits = 0;
    int idx = start;
    while (numDigits < digits.length && idx < text.length()) {
      int digit = "0123456789".indexOf(text.charAt(idx));
      if (digit < 0)
        break; 
      digits[numDigits] = digit;
      numDigits++;
      idx++;
    } 
    if (fixedHourWidth && (numDigits & 0x1) != 0)
      numDigits--; 
    if (numDigits < minDigits) {
      pos.setErrorIndex(start);
      return 0;
    } 
    int hour = 0, min = 0, sec = 0;
    boolean bParsed = false;
    while (numDigits >= minDigits) {
      switch (numDigits) {
        case 1:
          hour = digits[0];
          break;
        case 2:
          hour = digits[0] * 10 + digits[1];
          break;
        case 3:
          hour = digits[0];
          min = digits[1] * 10 + digits[2];
          break;
        case 4:
          hour = digits[0] * 10 + digits[1];
          min = digits[2] * 10 + digits[3];
          break;
        case 5:
          hour = digits[0];
          min = digits[1] * 10 + digits[2];
          sec = digits[3] * 10 + digits[4];
          break;
        case 6:
          hour = digits[0] * 10 + digits[1];
          min = digits[2] * 10 + digits[3];
          sec = digits[4] * 10 + digits[5];
          break;
      } 
      if (hour <= 23 && min <= 59 && sec <= 59) {
        bParsed = true;
        break;
      } 
      numDigits -= fixedHourWidth ? 2 : 1;
      hour = min = sec = 0;
    } 
    if (!bParsed) {
      pos.setErrorIndex(start);
      return 0;
    } 
    pos.setIndex(start + numDigits);
    return ((hour * 60 + min) * 60 + sec) * 1000;
  }
  
  private static int parseAsciiOffsetFields(String text, ParsePosition pos, char sep, OffsetFields minFields, OffsetFields maxFields) {
    int start = pos.getIndex();
    int[] fieldVal = { 0, 0, 0 };
    int[] fieldLen = { 0, -1, -1 };
    for (int idx = start, fieldIdx = 0; idx < text.length() && fieldIdx <= maxFields.ordinal(); idx++) {
      char c = text.charAt(idx);
      if (c == sep) {
        if (fieldIdx == 0) {
          if (fieldLen[0] == 0)
            break; 
          fieldIdx++;
        } else {
          if (fieldLen[fieldIdx] != -1)
            break; 
          fieldLen[fieldIdx] = 0;
        } 
      } else {
        if (fieldLen[fieldIdx] == -1)
          break; 
        int digit = "0123456789".indexOf(c);
        if (digit < 0)
          break; 
        fieldVal[fieldIdx] = fieldVal[fieldIdx] * 10 + digit;
        fieldLen[fieldIdx] = fieldLen[fieldIdx] + 1;
        if (fieldLen[fieldIdx] >= 2)
          fieldIdx++; 
      } 
    } 
    int offset = 0;
    int parsedLen = 0;
    OffsetFields parsedFields = null;
    if (fieldLen[0] != 0)
      if (fieldVal[0] > 23) {
        offset = fieldVal[0] / 10 * 3600000;
        parsedFields = OffsetFields.H;
        parsedLen = 1;
      } else {
        offset = fieldVal[0] * 3600000;
        parsedLen = fieldLen[0];
        parsedFields = OffsetFields.H;
        if (fieldLen[1] == 2 && fieldVal[1] <= 59) {
          offset += fieldVal[1] * 60000;
          parsedLen += 1 + fieldLen[1];
          parsedFields = OffsetFields.HM;
          if (fieldLen[2] == 2 && fieldVal[2] <= 59) {
            offset += fieldVal[2] * 1000;
            parsedLen += 1 + fieldLen[2];
            parsedFields = OffsetFields.HMS;
          } 
        } 
      }  
    if (parsedFields == null || parsedFields.ordinal() < minFields.ordinal()) {
      pos.setErrorIndex(start);
      return 0;
    } 
    pos.setIndex(start + parsedLen);
    return offset;
  }
  
  private static String parseZoneID(String text, ParsePosition pos) {
    String resolvedID = null;
    if (ZONE_ID_TRIE == null)
      synchronized (TimeZoneFormat.class) {
        if (ZONE_ID_TRIE == null) {
          TextTrieMap<String> trie = new TextTrieMap(true);
          String[] ids = TimeZone.getAvailableIDs();
          for (String id : ids)
            trie.put(id, id); 
          ZONE_ID_TRIE = trie;
        } 
      }  
    int[] matchLen = { 0 };
    Iterator<String> itr = ZONE_ID_TRIE.get(text, pos.getIndex(), matchLen);
    if (itr != null) {
      resolvedID = itr.next();
      pos.setIndex(pos.getIndex() + matchLen[0]);
    } else {
      pos.setErrorIndex(pos.getIndex());
    } 
    return resolvedID;
  }
  
  private static String parseShortZoneID(String text, ParsePosition pos) {
    String resolvedID = null;
    if (SHORT_ZONE_ID_TRIE == null)
      synchronized (TimeZoneFormat.class) {
        if (SHORT_ZONE_ID_TRIE == null) {
          TextTrieMap<String> trie = new TextTrieMap(true);
          Set<String> canonicalIDs = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, null, null);
          for (String id : canonicalIDs) {
            String shortID = ZoneMeta.getShortID(id);
            if (shortID != null)
              trie.put(shortID, id); 
          } 
          trie.put("unk", "Etc/Unknown");
          SHORT_ZONE_ID_TRIE = trie;
        } 
      }  
    int[] matchLen = { 0 };
    Iterator<String> itr = SHORT_ZONE_ID_TRIE.get(text, pos.getIndex(), matchLen);
    if (itr != null) {
      resolvedID = itr.next();
      pos.setIndex(pos.getIndex() + matchLen[0]);
    } else {
      pos.setErrorIndex(pos.getIndex());
    } 
    return resolvedID;
  }
  
  private String parseExemplarLocation(String text, ParsePosition pos) {
    int startIdx = pos.getIndex();
    int parsedPos = -1;
    String tzID = null;
    EnumSet<TimeZoneNames.NameType> nameTypes = EnumSet.of(TimeZoneNames.NameType.EXEMPLAR_LOCATION);
    Collection<TimeZoneNames.MatchInfo> exemplarMatches = this._tznames.find(text, startIdx, nameTypes);
    if (exemplarMatches != null) {
      TimeZoneNames.MatchInfo exemplarMatch = null;
      for (TimeZoneNames.MatchInfo match : exemplarMatches) {
        if (startIdx + match.matchLength() > parsedPos) {
          exemplarMatch = match;
          parsedPos = startIdx + match.matchLength();
        } 
      } 
      if (exemplarMatch != null) {
        tzID = getTimeZoneID(exemplarMatch.tzID(), exemplarMatch.mzID());
        pos.setIndex(parsedPos);
      } 
    } 
    if (tzID == null)
      pos.setErrorIndex(startIdx); 
    return tzID;
  }
  
  private static class TimeZoneFormatCache extends SoftCache<ULocale, TimeZoneFormat, ULocale> {
    private TimeZoneFormatCache() {}
    
    protected TimeZoneFormat createInstance(ULocale key, ULocale data) {
      TimeZoneFormat fmt = new TimeZoneFormat(data);
      fmt.freeze();
      return fmt;
    }
  }
  
  private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[] { new ObjectStreamField("_locale", ULocale.class), new ObjectStreamField("_tznames", TimeZoneNames.class), new ObjectStreamField("_gmtPattern", String.class), new ObjectStreamField("_gmtOffsetPatterns", String[].class), new ObjectStreamField("_gmtOffsetDigits", String[].class), new ObjectStreamField("_gmtZeroFormat", String.class), new ObjectStreamField("_parseAllStyles", boolean.class) };
  
  private void writeObject(ObjectOutputStream oos) throws IOException {
    ObjectOutputStream.PutField fields = oos.putFields();
    fields.put("_locale", this._locale);
    fields.put("_tznames", this._tznames);
    fields.put("_gmtPattern", this._gmtPattern);
    fields.put("_gmtOffsetPatterns", this._gmtOffsetPatterns);
    fields.put("_gmtOffsetDigits", this._gmtOffsetDigits);
    fields.put("_gmtZeroFormat", this._gmtZeroFormat);
    fields.put("_parseAllStyles", this._parseAllStyles);
    oos.writeFields();
  }
  
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ObjectInputStream.GetField fields = ois.readFields();
    this._locale = (ULocale)fields.get("_locale", (Object)null);
    if (this._locale == null)
      throw new InvalidObjectException("Missing field: locale"); 
    this._tznames = (TimeZoneNames)fields.get("_tznames", (Object)null);
    if (this._tznames == null)
      throw new InvalidObjectException("Missing field: tznames"); 
    this._gmtPattern = (String)fields.get("_gmtPattern", (Object)null);
    if (this._gmtPattern == null)
      throw new InvalidObjectException("Missing field: gmtPattern"); 
    String[] tmpGmtOffsetPatterns = (String[])fields.get("_gmtOffsetPatterns", (Object)null);
    if (tmpGmtOffsetPatterns == null)
      throw new InvalidObjectException("Missing field: gmtOffsetPatterns"); 
    if (tmpGmtOffsetPatterns.length < 4)
      throw new InvalidObjectException("Incompatible field: gmtOffsetPatterns"); 
    this._gmtOffsetPatterns = new String[6];
    if (tmpGmtOffsetPatterns.length == 4) {
      for (int i = 0; i < 4; i++)
        this._gmtOffsetPatterns[i] = tmpGmtOffsetPatterns[i]; 
      this._gmtOffsetPatterns[GMTOffsetPatternType.POSITIVE_H.ordinal()] = truncateOffsetPattern(this._gmtOffsetPatterns[GMTOffsetPatternType.POSITIVE_HM.ordinal()]);
      this._gmtOffsetPatterns[GMTOffsetPatternType.NEGATIVE_H.ordinal()] = truncateOffsetPattern(this._gmtOffsetPatterns[GMTOffsetPatternType.NEGATIVE_HM.ordinal()]);
    } else {
      this._gmtOffsetPatterns = tmpGmtOffsetPatterns;
    } 
    this._gmtOffsetDigits = (String[])fields.get("_gmtOffsetDigits", (Object)null);
    if (this._gmtOffsetDigits == null)
      throw new InvalidObjectException("Missing field: gmtOffsetDigits"); 
    if (this._gmtOffsetDigits.length != 10)
      throw new InvalidObjectException("Incompatible field: gmtOffsetDigits"); 
    this._gmtZeroFormat = (String)fields.get("_gmtZeroFormat", (Object)null);
    if (this._gmtZeroFormat == null)
      throw new InvalidObjectException("Missing field: gmtZeroFormat"); 
    this._parseAllStyles = fields.get("_parseAllStyles", false);
    if (fields.defaulted("_parseAllStyles"))
      throw new InvalidObjectException("Missing field: parseAllStyles"); 
    if (this._tznames instanceof com.ibm.icu.impl.TimeZoneNamesImpl) {
      this._tznames = TimeZoneNames.getInstance(this._locale);
      this._gnames = null;
    } else {
      this._gnames = new TimeZoneGenericNames(this._locale, this._tznames);
    } 
    initGMTPattern(this._gmtPattern);
    initGMTOffsetPatterns(this._gmtOffsetPatterns);
  }
  
  public boolean isFrozen() {
    return this._frozen;
  }
  
  public TimeZoneFormat freeze() {
    this._frozen = true;
    return this;
  }
  
  public TimeZoneFormat cloneAsThawed() {
    TimeZoneFormat copy = (TimeZoneFormat)clone();
    copy._frozen = false;
    return copy;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\TimeZoneFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */