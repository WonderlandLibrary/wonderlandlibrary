package org.apache.logging.log4j.core.util.datetime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastDateParser implements DateParser, Serializable {
  private static final long serialVersionUID = 3L;
  
  static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
  
  private final String pattern;
  
  private final TimeZone timeZone;
  
  private final Locale locale;
  
  private final int century;
  
  private final int startYear;
  
  private transient List<StrategyAndWidth> patterns;
  
  private static final Comparator<String> LONGER_FIRST_LOWERCASE;
  
  static {
    LONGER_FIRST_LOWERCASE = ((left, right) -> right.compareTo(left));
  }
  
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
    this.patterns = new ArrayList<>();
    StrategyParser fm = new StrategyParser(definingCalendar);
    while (true) {
      StrategyAndWidth field = fm.getNextStrategy();
      if (field == null)
        break; 
      this.patterns.add(field);
    } 
  }
  
  private static class StrategyAndWidth {
    final FastDateParser.Strategy strategy;
    
    final int width;
    
    StrategyAndWidth(FastDateParser.Strategy strategy, int width) {
      this.strategy = strategy;
      this.width = width;
    }
    
    int getMaxWidth(ListIterator<StrategyAndWidth> lt) {
      if (!this.strategy.isNumber() || !lt.hasNext())
        return 0; 
      FastDateParser.Strategy nextStrategy = ((StrategyAndWidth)lt.next()).strategy;
      lt.previous();
      return nextStrategy.isNumber() ? this.width : 0;
    }
  }
  
  private class StrategyParser {
    private final Calendar definingCalendar;
    
    private int currentIdx;
    
    StrategyParser(Calendar definingCalendar) {
      this.definingCalendar = definingCalendar;
    }
    
    FastDateParser.StrategyAndWidth getNextStrategy() {
      if (this.currentIdx >= FastDateParser.this.pattern.length())
        return null; 
      char c = FastDateParser.this.pattern.charAt(this.currentIdx);
      if (FastDateParser.isFormatLetter(c))
        return letterPattern(c); 
      return literal();
    }
    
    private FastDateParser.StrategyAndWidth letterPattern(char c) {
      int begin = this.currentIdx;
      do {
      
      } while (++this.currentIdx < FastDateParser.this.pattern.length() && 
        FastDateParser.this.pattern.charAt(this.currentIdx) == c);
      int width = this.currentIdx - begin;
      return new FastDateParser.StrategyAndWidth(FastDateParser.this.getStrategy(c, width, this.definingCalendar), width);
    }
    
    private FastDateParser.StrategyAndWidth literal() {
      boolean activeQuote = false;
      StringBuilder sb = new StringBuilder();
      while (this.currentIdx < FastDateParser.this.pattern.length()) {
        char c = FastDateParser.this.pattern.charAt(this.currentIdx);
        if (!activeQuote && FastDateParser.isFormatLetter(c))
          break; 
        if (c == '\'' && (++this.currentIdx == FastDateParser.this.pattern.length() || FastDateParser.this.pattern.charAt(this.currentIdx) != '\'')) {
          activeQuote = !activeQuote;
          continue;
        } 
        this.currentIdx++;
        sb.append(c);
      } 
      if (activeQuote)
        throw new IllegalArgumentException("Unterminated quote"); 
      String formatField = sb.toString();
      return new FastDateParser.StrategyAndWidth(new FastDateParser.CopyQuotedStrategy(formatField), formatField.length());
    }
  }
  
  private static boolean isFormatLetter(char c) {
    return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'));
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
  
  public boolean equals(Object obj) {
    if (!(obj instanceof FastDateParser))
      return false; 
    FastDateParser other = (FastDateParser)obj;
    return (this.pattern.equals(other.pattern) && this.timeZone
      .equals(other.timeZone) && this.locale
      .equals(other.locale));
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
    ParsePosition pp = new ParsePosition(0);
    Date date = parse(source, pp);
    if (date == null) {
      if (this.locale.equals(JAPANESE_IMPERIAL))
        throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\nUnparseable date: \"" + source, pp
            
            .getErrorIndex()); 
      throw new ParseException("Unparseable date: " + source, pp.getErrorIndex());
    } 
    return date;
  }
  
  public Object parseObject(String source, ParsePosition pos) {
    return parse(source, pos);
  }
  
  public Date parse(String source, ParsePosition pos) {
    Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
    cal.clear();
    return parse(source, pos, cal) ? cal.getTime() : null;
  }
  
  public boolean parse(String source, ParsePosition pos, Calendar calendar) {
    ListIterator<StrategyAndWidth> lt = this.patterns.listIterator();
    while (lt.hasNext()) {
      StrategyAndWidth strategyAndWidth = lt.next();
      int maxWidth = strategyAndWidth.getMaxWidth(lt);
      if (!strategyAndWidth.strategy.parse(this, calendar, source, pos, maxWidth))
        return false; 
    } 
    return true;
  }
  
  private static StringBuilder simpleQuote(StringBuilder sb, String value) {
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      switch (c) {
        case '$':
        case '(':
        case ')':
        case '*':
        case '+':
        case '.':
        case '?':
        case '[':
        case '\\':
        case '^':
        case '{':
        case '|':
          sb.append('\\');
          break;
      } 
      sb.append(c);
    } 
    return sb;
  }
  
  private static Map<String, Integer> appendDisplayNames(Calendar cal, Locale locale, int field, StringBuilder regex) {
    Map<String, Integer> values = new HashMap<>();
    Map<String, Integer> displayNames = cal.getDisplayNames(field, 0, locale);
    TreeSet<String> sorted = new TreeSet<>(LONGER_FIRST_LOWERCASE);
    for (Map.Entry<String, Integer> displayName : displayNames.entrySet()) {
      String key = ((String)displayName.getKey()).toLowerCase(locale);
      if (sorted.add(key))
        values.put(key, displayName.getValue()); 
    } 
    for (String symbol : sorted)
      simpleQuote(regex, symbol).append('|'); 
    return values;
  }
  
  private int adjustYear(int twoDigitYear) {
    int trial = this.century + twoDigitYear;
    return (twoDigitYear >= this.startYear) ? trial : (trial + 100);
  }
  
  private static abstract class Strategy {
    private Strategy() {}
    
    boolean isNumber() {
      return false;
    }
    
    abstract boolean parse(FastDateParser param1FastDateParser, Calendar param1Calendar, String param1String, ParsePosition param1ParsePosition, int param1Int);
  }
  
  private static abstract class PatternStrategy extends Strategy {
    private Pattern pattern;
    
    private PatternStrategy() {}
    
    void createPattern(StringBuilder regex) {
      createPattern(regex.toString());
    }
    
    void createPattern(String regex) {
      this.pattern = Pattern.compile(regex);
    }
    
    boolean isNumber() {
      return false;
    }
    
    boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
      Matcher matcher = this.pattern.matcher(source.substring(pos.getIndex()));
      if (!matcher.lookingAt()) {
        pos.setErrorIndex(pos.getIndex());
        return false;
      } 
      pos.setIndex(pos.getIndex() + matcher.end(1));
      setCalendar(parser, calendar, matcher.group(1));
      return true;
    }
    
    abstract void setCalendar(FastDateParser param1FastDateParser, Calendar param1Calendar, String param1String);
  }
  
  private Strategy getStrategy(char f, int width, Calendar definingCalendar) {
    switch (f) {
      default:
        throw new IllegalArgumentException("Format '" + f + "' not supported");
      case 'D':
        return DAY_OF_YEAR_STRATEGY;
      case 'E':
        return getLocaleSpecificStrategy(7, definingCalendar);
      case 'F':
        return DAY_OF_WEEK_IN_MONTH_STRATEGY;
      case 'G':
        return getLocaleSpecificStrategy(0, definingCalendar);
      case 'H':
        return HOUR_OF_DAY_STRATEGY;
      case 'K':
        return HOUR_STRATEGY;
      case 'M':
        return (width >= 3) ? getLocaleSpecificStrategy(2, definingCalendar) : NUMBER_MONTH_STRATEGY;
      case 'S':
        return MILLISECOND_STRATEGY;
      case 'W':
        return WEEK_OF_MONTH_STRATEGY;
      case 'a':
        return getLocaleSpecificStrategy(9, definingCalendar);
      case 'd':
        return DAY_OF_MONTH_STRATEGY;
      case 'h':
        return HOUR12_STRATEGY;
      case 'k':
        return HOUR24_OF_DAY_STRATEGY;
      case 'm':
        return MINUTE_STRATEGY;
      case 's':
        return SECOND_STRATEGY;
      case 'u':
        return DAY_OF_WEEK_STRATEGY;
      case 'w':
        return WEEK_OF_YEAR_STRATEGY;
      case 'Y':
      case 'y':
        return (width > 2) ? LITERAL_YEAR_STRATEGY : ABBREVIATED_YEAR_STRATEGY;
      case 'X':
        return ISO8601TimeZoneStrategy.getStrategy(width);
      case 'Z':
        if (width == 2)
          return ISO8601TimeZoneStrategy.ISO_8601_3_STRATEGY; 
        break;
      case 'z':
        break;
    } 
    return getLocaleSpecificStrategy(15, definingCalendar);
  }
  
  private static final ConcurrentMap<Locale, Strategy>[] caches = (ConcurrentMap<Locale, Strategy>[])new ConcurrentMap[17];
  
  private static ConcurrentMap<Locale, Strategy> getCache(int field) {
    synchronized (caches) {
      if (caches[field] == null)
        caches[field] = new ConcurrentHashMap<>(3); 
      return caches[field];
    } 
  }
  
  private Strategy getLocaleSpecificStrategy(int field, Calendar definingCalendar) {
    ConcurrentMap<Locale, Strategy> cache = getCache(field);
    Strategy strategy = cache.get(this.locale);
    if (strategy == null) {
      strategy = (field == 15) ? new TimeZoneStrategy(this.locale) : new CaseInsensitiveTextStrategy(field, definingCalendar, this.locale);
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
      return false;
    }
    
    boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
      for (int idx = 0; idx < this.formatField.length(); idx++) {
        int sIdx = idx + pos.getIndex();
        if (sIdx == source.length()) {
          pos.setErrorIndex(sIdx);
          return false;
        } 
        if (this.formatField.charAt(idx) != source.charAt(sIdx)) {
          pos.setErrorIndex(sIdx);
          return false;
        } 
      } 
      pos.setIndex(this.formatField.length() + pos.getIndex());
      return true;
    }
  }
  
  private static class CaseInsensitiveTextStrategy extends PatternStrategy {
    private final int field;
    
    final Locale locale;
    
    private final Map<String, Integer> lKeyValues;
    
    CaseInsensitiveTextStrategy(int field, Calendar definingCalendar, Locale locale) {
      this.field = field;
      this.locale = locale;
      StringBuilder regex = new StringBuilder();
      regex.append("((?iu)");
      this.lKeyValues = FastDateParser.appendDisplayNames(definingCalendar, locale, field, regex);
      regex.setLength(regex.length() - 1);
      regex.append(")");
      createPattern(regex);
    }
    
    void setCalendar(FastDateParser parser, Calendar cal, String value) {
      Integer iVal = this.lKeyValues.get(value.toLowerCase(this.locale));
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
    
    boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
      int idx = pos.getIndex();
      int last = source.length();
      if (maxWidth == 0) {
        for (; idx < last; idx++) {
          char c = source.charAt(idx);
          if (!Character.isWhitespace(c))
            break; 
        } 
        pos.setIndex(idx);
      } else {
        int end = idx + maxWidth;
        if (last > end)
          last = end; 
      } 
      for (; idx < last; idx++) {
        char c = source.charAt(idx);
        if (!Character.isDigit(c))
          break; 
      } 
      if (pos.getIndex() == idx) {
        pos.setErrorIndex(idx);
        return false;
      } 
      int value = Integer.parseInt(source.substring(pos.getIndex(), idx));
      pos.setIndex(idx);
      calendar.set(this.field, modify(parser, value));
      return true;
    }
    
    int modify(FastDateParser parser, int iValue) {
      return iValue;
    }
  }
  
  private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1) {
      int modify(FastDateParser parser, int iValue) {
        return (iValue < 100) ? parser.adjustYear(iValue) : iValue;
      }
    };
  
  static class TimeZoneStrategy extends PatternStrategy {
    private static final String RFC_822_TIME_ZONE = "[+-]\\d{4}";
    
    private static final String GMT_OPTION = "GMT[+-]\\d{1,2}:\\d{2}";
    
    private final Locale locale;
    
    private final Map<String, TzInfo> tzNames = new HashMap<>();
    
    private static final int ID = 0;
    
    private static class TzInfo {
      TimeZone zone;
      
      int dstOffset;
      
      TzInfo(TimeZone tz, boolean useDst) {
        this.zone = tz;
        this.dstOffset = useDst ? tz.getDSTSavings() : 0;
      }
    }
    
    TimeZoneStrategy(Locale locale) {
      this.locale = locale;
      StringBuilder sb = new StringBuilder();
      sb.append("((?iu)[+-]\\d{4}|GMT[+-]\\d{1,2}:\\d{2}");
      Set<String> sorted = new TreeSet<>(FastDateParser.LONGER_FIRST_LOWERCASE);
      String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
      for (String[] zoneNames : zones) {
        String tzId = zoneNames[0];
        if (!tzId.equalsIgnoreCase("GMT")) {
          TimeZone tz = TimeZone.getTimeZone(tzId);
          TzInfo standard = new TzInfo(tz, false);
          TzInfo tzInfo = standard;
          for (int i = 1; i < zoneNames.length; i++) {
            switch (i) {
              case 3:
                tzInfo = new TzInfo(tz, true);
                break;
              case 5:
                tzInfo = standard;
                break;
            } 
            if (zoneNames[i] != null) {
              String key = zoneNames[i].toLowerCase(locale);
              if (sorted.add(key))
                this.tzNames.put(key, tzInfo); 
            } 
          } 
        } 
      } 
      for (String zoneName : sorted)
        FastDateParser.simpleQuote(sb.append('|'), zoneName); 
      sb.append(")");
      createPattern(sb);
    }
    
    void setCalendar(FastDateParser parser, Calendar cal, String value) {
      if (value.charAt(0) == '+' || value.charAt(0) == '-') {
        TimeZone tz = TimeZone.getTimeZone("GMT" + value);
        cal.setTimeZone(tz);
      } else if (value.regionMatches(true, 0, "GMT", 0, 3)) {
        TimeZone tz = TimeZone.getTimeZone(value.toUpperCase());
        cal.setTimeZone(tz);
      } else {
        TzInfo tzInfo = this.tzNames.get(value.toLowerCase(this.locale));
        cal.set(16, tzInfo.dstOffset);
        cal.set(15, tzInfo.zone.getRawOffset());
      } 
    }
  }
  
  private static class ISO8601TimeZoneStrategy extends PatternStrategy {
    ISO8601TimeZoneStrategy(String pattern) {
      createPattern(pattern);
    }
    
    void setCalendar(FastDateParser parser, Calendar cal, String value) {
      if (value.equals("Z")) {
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
      } else {
        cal.setTimeZone(TimeZone.getTimeZone("GMT" + value));
      } 
    }
    
    private static final FastDateParser.Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
    
    private static final FastDateParser.Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
    
    private static final FastDateParser.Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");
    
    static FastDateParser.Strategy getStrategy(int tokenLen) {
      switch (tokenLen) {
        case 1:
          return ISO_8601_1_STRATEGY;
        case 2:
          return ISO_8601_2_STRATEGY;
        case 3:
          return ISO_8601_3_STRATEGY;
      } 
      throw new IllegalArgumentException("invalid number of X");
    }
  }
  
  private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(2) {
      int modify(FastDateParser parser, int iValue) {
        return iValue - 1;
      }
    };
  
  private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
  
  private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
  
  private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
  
  private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
  
  private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
  
  private static final Strategy DAY_OF_WEEK_STRATEGY = new NumberStrategy(7) {
      int modify(FastDateParser parser, int iValue) {
        return (iValue != 7) ? (iValue + 1) : 1;
      }
    };
  
  private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
  
  private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
  
  private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(11) {
      int modify(FastDateParser parser, int iValue) {
        return (iValue == 24) ? 0 : iValue;
      }
    };
  
  private static final Strategy HOUR12_STRATEGY = new NumberStrategy(10) {
      int modify(FastDateParser parser, int iValue) {
        return (iValue == 12) ? 0 : iValue;
      }
    };
  
  private static final Strategy HOUR_STRATEGY = new NumberStrategy(10);
  
  private static final Strategy MINUTE_STRATEGY = new NumberStrategy(12);
  
  private static final Strategy SECOND_STRATEGY = new NumberStrategy(13);
  
  private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(14);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\datetime\FastDateParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */