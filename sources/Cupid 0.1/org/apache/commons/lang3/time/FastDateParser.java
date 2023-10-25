package org.apache.commons.lang3.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastDateParser implements DateParser, Serializable {
  private static final long serialVersionUID = 2L;
  
  static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
  
  private final String pattern;
  
  private final TimeZone timeZone;
  
  private final Locale locale;
  
  private final int century;
  
  private final int startYear;
  
  private transient Pattern parsePattern;
  
  private transient Strategy[] strategies;
  
  private transient String currentFormatField;
  
  private transient Strategy nextStrategy;
  
  protected FastDateParser(String pattern, TimeZone timeZone, Locale locale) {
    this(pattern, timeZone, locale, null);
  }
  
  protected FastDateParser(String pattern, TimeZone timeZone, Locale locale, Date centuryStart) {
    int centuryStartYear;
    this.pattern = pattern;
    this.timeZone = timeZone;
    this.locale = locale;
    Calendar definingCalendar = Calendar.getInstance(timeZone, locale);
    if (centuryStart != null) {
      definingCalendar.setTime(centuryStart);
      centuryStartYear = definingCalendar.get(1);
    } else if (locale.equals(JAPANESE_IMPERIAL)) {
      centuryStartYear = 0;
    } else {
      definingCalendar.setTime(new Date());
      centuryStartYear = definingCalendar.get(1) - 80;
    } 
    this.century = centuryStartYear / 100 * 100;
    this.startYear = centuryStartYear - this.century;
    init(definingCalendar);
  }
  
  private void init(Calendar definingCalendar) {
    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();
    Matcher patternMatcher = formatPattern.matcher(this.pattern);
    if (!patternMatcher.lookingAt())
      throw new IllegalArgumentException("Illegal pattern character '" + this.pattern.charAt(patternMatcher.regionStart()) + "'"); 
    this.currentFormatField = patternMatcher.group();
    Strategy currentStrategy = getStrategy(this.currentFormatField, definingCalendar);
    while (true) {
      patternMatcher.region(patternMatcher.end(), patternMatcher.regionEnd());
      if (!patternMatcher.lookingAt()) {
        this.nextStrategy = null;
        break;
      } 
      String nextFormatField = patternMatcher.group();
      this.nextStrategy = getStrategy(nextFormatField, definingCalendar);
      if (currentStrategy.addRegex(this, regex))
        collector.add(currentStrategy); 
      this.currentFormatField = nextFormatField;
      currentStrategy = this.nextStrategy;
    } 
    if (patternMatcher.regionStart() != patternMatcher.regionEnd())
      throw new IllegalArgumentException("Failed to parse \"" + this.pattern + "\" ; gave up at index " + patternMatcher.regionStart()); 
    if (currentStrategy.addRegex(this, regex))
      collector.add(currentStrategy); 
    this.currentFormatField = null;
    this.strategies = collector.<Strategy>toArray(new Strategy[collector.size()]);
    this.parsePattern = Pattern.compile(regex.toString());
  }
  
  public String getPattern() {
    return this.pattern;
  }
  
  public TimeZone getTimeZone() {
    return this.timeZone;
  }
  
  public Locale getLocale() {
    return this.locale;
  }
  
  Pattern getParsePattern() {
    return this.parsePattern;
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof FastDateParser))
      return false; 
    FastDateParser other = (FastDateParser)obj;
    return (this.pattern.equals(other.pattern) && this.timeZone.equals(other.timeZone) && this.locale.equals(other.locale));
  }
  
  public int hashCode() {
    return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
  }
  
  public String toString() {
    return "FastDateParser[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    Calendar definingCalendar = Calendar.getInstance(this.timeZone, this.locale);
    init(definingCalendar);
  }
  
  public Object parseObject(String source) throws ParseException {
    return parse(source);
  }
  
  public Date parse(String source) throws ParseException {
    Date date = parse(source, new ParsePosition(0));
    if (date == null) {
      if (this.locale.equals(JAPANESE_IMPERIAL))
        throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\n" + "Unparseable date: \"" + source + "\" does not match " + this.parsePattern.pattern(), 0); 
      throw new ParseException("Unparseable date: \"" + source + "\" does not match " + this.parsePattern.pattern(), 0);
    } 
    return date;
  }
  
  public Object parseObject(String source, ParsePosition pos) {
    return parse(source, pos);
  }
  
  public Date parse(String source, ParsePosition pos) {
    int offset = pos.getIndex();
    Matcher matcher = this.parsePattern.matcher(source.substring(offset));
    if (!matcher.lookingAt())
      return null; 
    Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
    cal.clear();
    for (int i = 0; i < this.strategies.length; ) {
      Strategy strategy = this.strategies[i++];
      strategy.setCalendar(this, cal, matcher.group(i));
    } 
    pos.setIndex(offset + matcher.end());
    return cal.getTime();
  }
  
  private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    regex.append("\\Q");
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      switch (c) {
        case '\'':
          if (unquote) {
            if (++i == value.length())
              return regex; 
            c = value.charAt(i);
          } 
          break;
        case '\\':
          if (++i == value.length())
            break; 
          regex.append(c);
          c = value.charAt(i);
          if (c == 'E') {
            regex.append("E\\\\E\\");
            c = 'Q';
          } 
          break;
      } 
      regex.append(c);
    } 
    regex.append("\\E");
    return regex;
  }
  
  private static Map<String, Integer> getDisplayNames(int field, Calendar definingCalendar, Locale locale) {
    return definingCalendar.getDisplayNames(field, 0, locale);
  }
  
  private int adjustYear(int twoDigitYear) {
    int trial = this.century + twoDigitYear;
    return (twoDigitYear >= this.startYear) ? trial : (trial + 100);
  }
  
  boolean isNextNumber() {
    return (this.nextStrategy != null && this.nextStrategy.isNumber());
  }
  
  int getFieldWidth() {
    return this.currentFormatField.length();
  }
  
  private static abstract class Strategy {
    private Strategy() {}
    
    boolean isNumber() {
      return false;
    }
    
    void setCalendar(FastDateParser parser, Calendar cal, String value) {}
    
    abstract boolean addRegex(FastDateParser param1FastDateParser, StringBuilder param1StringBuilder);
  }
  
  private static final Pattern formatPattern = Pattern.compile("D+|E+|F+|G+|H+|K+|M+|S+|W+|Z+|a+|d+|h+|k+|m+|s+|w+|y+|z+|''|'[^']++(''[^']*+)*+'|[^'A-Za-z]++");
  
  private Strategy getStrategy(String formatField, Calendar definingCalendar) {
    switch (formatField.charAt(0)) {
      case '\'':
        if (formatField.length() > 2)
          return new CopyQuotedStrategy(formatField.substring(1, formatField.length() - 1)); 
      default:
        return new CopyQuotedStrategy(formatField);
      case 'D':
        return DAY_OF_YEAR_STRATEGY;
      case 'E':
        return getLocaleSpecificStrategy(7, definingCalendar);
      case 'F':
        return DAY_OF_WEEK_IN_MONTH_STRATEGY;
      case 'G':
        return getLocaleSpecificStrategy(0, definingCalendar);
      case 'H':
        return MODULO_HOUR_OF_DAY_STRATEGY;
      case 'K':
        return HOUR_STRATEGY;
      case 'M':
        return (formatField.length() >= 3) ? getLocaleSpecificStrategy(2, definingCalendar) : NUMBER_MONTH_STRATEGY;
      case 'S':
        return MILLISECOND_STRATEGY;
      case 'W':
        return WEEK_OF_MONTH_STRATEGY;
      case 'a':
        return getLocaleSpecificStrategy(9, definingCalendar);
      case 'd':
        return DAY_OF_MONTH_STRATEGY;
      case 'h':
        return MODULO_HOUR_STRATEGY;
      case 'k':
        return HOUR_OF_DAY_STRATEGY;
      case 'm':
        return MINUTE_STRATEGY;
      case 's':
        return SECOND_STRATEGY;
      case 'w':
        return WEEK_OF_YEAR_STRATEGY;
      case 'y':
        return (formatField.length() > 2) ? LITERAL_YEAR_STRATEGY : ABBREVIATED_YEAR_STRATEGY;
      case 'Z':
      case 'z':
        break;
    } 
    return getLocaleSpecificStrategy(15, definingCalendar);
  }
  
  private static final ConcurrentMap<Locale, Strategy>[] caches = (ConcurrentMap<Locale, Strategy>[])new ConcurrentMap[17];
  
  private static ConcurrentMap<Locale, Strategy> getCache(int field) {
    synchronized (caches) {
      if (caches[field] == null)
        caches[field] = new ConcurrentHashMap<Locale, Strategy>(3); 
      return caches[field];
    } 
  }
  
  private Strategy getLocaleSpecificStrategy(int field, Calendar definingCalendar) {
    ConcurrentMap<Locale, Strategy> cache = getCache(field);
    Strategy strategy = cache.get(this.locale);
    if (strategy == null) {
      strategy = (field == 15) ? new TimeZoneStrategy(this.locale) : new TextStrategy(field, definingCalendar, this.locale);
      Strategy inCache = cache.putIfAbsent(this.locale, strategy);
      if (inCache != null)
        return inCache; 
    } 
    return strategy;
  }
  
  private static class CopyQuotedStrategy extends Strategy {
    private final String formatField;
    
    CopyQuotedStrategy(String formatField) {
      this.formatField = formatField;
    }
    
    boolean isNumber() {
      char c = this.formatField.charAt(0);
      if (c == '\'')
        c = this.formatField.charAt(1); 
      return Character.isDigit(c);
    }
    
    boolean addRegex(FastDateParser parser, StringBuilder regex) {
      FastDateParser.escapeRegex(regex, this.formatField, true);
      return false;
    }
  }
  
  private static class TextStrategy extends Strategy {
    private final int field;
    
    private final Map<String, Integer> keyValues;
    
    TextStrategy(int field, Calendar definingCalendar, Locale locale) {
      this.field = field;
      this.keyValues = FastDateParser.getDisplayNames(field, definingCalendar, locale);
    }
    
    boolean addRegex(FastDateParser parser, StringBuilder regex) {
      regex.append('(');
      for (String textKeyValue : this.keyValues.keySet())
        FastDateParser.escapeRegex(regex, textKeyValue, false).append('|'); 
      regex.setCharAt(regex.length() - 1, ')');
      return true;
    }
    
    void setCalendar(FastDateParser parser, Calendar cal, String value) {
      Integer iVal = this.keyValues.get(value);
      if (iVal == null) {
        StringBuilder sb = new StringBuilder(value);
        sb.append(" not in (");
        for (String textKeyValue : this.keyValues.keySet())
          sb.append(textKeyValue).append(' '); 
        sb.setCharAt(sb.length() - 1, ')');
        throw new IllegalArgumentException(sb.toString());
      } 
      cal.set(this.field, iVal.intValue());
    }
  }
  
  private static class NumberStrategy extends Strategy {
    private final int field;
    
    NumberStrategy(int field) {
      this.field = field;
    }
    
    boolean isNumber() {
      return true;
    }
    
    boolean addRegex(FastDateParser parser, StringBuilder regex) {
      if (parser.isNextNumber()) {
        regex.append("(\\p{Nd}{").append(parser.getFieldWidth()).append("}+)");
      } else {
        regex.append("(\\p{Nd}++)");
      } 
      return true;
    }
    
    void setCalendar(FastDateParser parser, Calendar cal, String value) {
      cal.set(this.field, modify(Integer.parseInt(value)));
    }
    
    int modify(int iValue) {
      return iValue;
    }
  }
  
  private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1) {
      void setCalendar(FastDateParser parser, Calendar cal, String value) {
        int iValue = Integer.parseInt(value);
        if (iValue < 100)
          iValue = parser.adjustYear(iValue); 
        cal.set(1, iValue);
      }
    };
  
  private static class TimeZoneStrategy extends Strategy {
    private final String validTimeZoneChars;
    
    private final SortedMap<String, TimeZone> tzNames = new TreeMap<String, TimeZone>(String.CASE_INSENSITIVE_ORDER);
    
    private static final int ID = 0;
    
    private static final int LONG_STD = 1;
    
    private static final int SHORT_STD = 2;
    
    private static final int LONG_DST = 3;
    
    private static final int SHORT_DST = 4;
    
    TimeZoneStrategy(Locale locale) {
      String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
      for (String[] zone : zones) {
        if (!zone[0].startsWith("GMT")) {
          TimeZone tz = TimeZone.getTimeZone(zone[0]);
          if (!this.tzNames.containsKey(zone[1]))
            this.tzNames.put(zone[1], tz); 
          if (!this.tzNames.containsKey(zone[2]))
            this.tzNames.put(zone[2], tz); 
          if (tz.useDaylightTime()) {
            if (!this.tzNames.containsKey(zone[3]))
              this.tzNames.put(zone[3], tz); 
            if (!this.tzNames.containsKey(zone[4]))
              this.tzNames.put(zone[4], tz); 
          } 
        } 
      } 
      StringBuilder sb = new StringBuilder();
      sb.append("(GMT[+\\-]\\d{0,1}\\d{2}|[+\\-]\\d{2}:?\\d{2}|");
      for (String id : this.tzNames.keySet())
        FastDateParser.escapeRegex(sb, id, false).append('|'); 
      sb.setCharAt(sb.length() - 1, ')');
      this.validTimeZoneChars = sb.toString();
    }
    
    boolean addRegex(FastDateParser parser, StringBuilder regex) {
      regex.append(this.validTimeZoneChars);
      return true;
    }
    
    void setCalendar(FastDateParser parser, Calendar cal, String value) {
      TimeZone tz;
      if (value.charAt(0) == '+' || value.charAt(0) == '-') {
        tz = TimeZone.getTimeZone("GMT" + value);
      } else if (value.startsWith("GMT")) {
        tz = TimeZone.getTimeZone(value);
      } else {
        tz = this.tzNames.get(value);
        if (tz == null)
          throw new IllegalArgumentException(value + " is not a supported timezone name"); 
      } 
      cal.setTimeZone(tz);
    }
  }
  
  private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(2) {
      int modify(int iValue) {
        return iValue - 1;
      }
    };
  
  private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
  
  private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
  
  private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
  
  private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
  
  private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
  
  private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
  
  private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
  
  private static final Strategy MODULO_HOUR_OF_DAY_STRATEGY = new NumberStrategy(11) {
      int modify(int iValue) {
        return iValue % 24;
      }
    };
  
  private static final Strategy MODULO_HOUR_STRATEGY = new NumberStrategy(10) {
      int modify(int iValue) {
        return iValue % 12;
      }
    };
  
  private static final Strategy HOUR_STRATEGY = new NumberStrategy(10);
  
  private static final Strategy MINUTE_STRATEGY = new NumberStrategy(12);
  
  private static final Strategy SECOND_STRATEGY = new NumberStrategy(13);
  
  private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(14);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\time\FastDateParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */