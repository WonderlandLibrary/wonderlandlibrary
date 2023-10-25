package org.apache.commons.lang3.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.Validate;

public class FastDatePrinter implements DatePrinter, Serializable {
  private static final long serialVersionUID = 1L;
  
  public static final int FULL = 0;
  
  public static final int LONG = 1;
  
  public static final int MEDIUM = 2;
  
  public static final int SHORT = 3;
  
  private final String mPattern;
  
  private final TimeZone mTimeZone;
  
  private final Locale mLocale;
  
  private transient Rule[] mRules;
  
  private transient int mMaxLengthEstimate;
  
  protected FastDatePrinter(String pattern, TimeZone timeZone, Locale locale) {
    this.mPattern = pattern;
    this.mTimeZone = timeZone;
    this.mLocale = locale;
    init();
  }
  
  private void init() {
    List<Rule> rulesList = parsePattern();
    this.mRules = rulesList.<Rule>toArray(new Rule[rulesList.size()]);
    int len = 0;
    for (int i = this.mRules.length; --i >= 0;)
      len += this.mRules[i].estimateLength(); 
    this.mMaxLengthEstimate = len;
  }
  
  protected List<Rule> parsePattern() {
    DateFormatSymbols symbols = new DateFormatSymbols(this.mLocale);
    List<Rule> rules = new ArrayList<Rule>();
    String[] ERAs = symbols.getEras();
    String[] months = symbols.getMonths();
    String[] shortMonths = symbols.getShortMonths();
    String[] weekdays = symbols.getWeekdays();
    String[] shortWeekdays = symbols.getShortWeekdays();
    String[] AmPmStrings = symbols.getAmPmStrings();
    int length = this.mPattern.length();
    int[] indexRef = new int[1];
    for (int i = 0; i < length; i++) {
      Rule rule;
      String sub;
      indexRef[0] = i;
      String token = parseToken(this.mPattern, indexRef);
      i = indexRef[0];
      int tokenLen = token.length();
      if (tokenLen == 0)
        break; 
      char c = token.charAt(0);
      switch (c) {
        case 'G':
          rule = new TextField(0, ERAs);
          break;
        case 'y':
          if (tokenLen == 2) {
            rule = TwoDigitYearField.INSTANCE;
            break;
          } 
          rule = selectNumberRule(1, (tokenLen < 4) ? 4 : tokenLen);
          break;
        case 'M':
          if (tokenLen >= 4) {
            rule = new TextField(2, months);
            break;
          } 
          if (tokenLen == 3) {
            rule = new TextField(2, shortMonths);
            break;
          } 
          if (tokenLen == 2) {
            rule = TwoDigitMonthField.INSTANCE;
            break;
          } 
          rule = UnpaddedMonthField.INSTANCE;
          break;
        case 'd':
          rule = selectNumberRule(5, tokenLen);
          break;
        case 'h':
          rule = new TwelveHourField(selectNumberRule(10, tokenLen));
          break;
        case 'H':
          rule = selectNumberRule(11, tokenLen);
          break;
        case 'm':
          rule = selectNumberRule(12, tokenLen);
          break;
        case 's':
          rule = selectNumberRule(13, tokenLen);
          break;
        case 'S':
          rule = selectNumberRule(14, tokenLen);
          break;
        case 'E':
          rule = new TextField(7, (tokenLen < 4) ? shortWeekdays : weekdays);
          break;
        case 'D':
          rule = selectNumberRule(6, tokenLen);
          break;
        case 'F':
          rule = selectNumberRule(8, tokenLen);
          break;
        case 'w':
          rule = selectNumberRule(3, tokenLen);
          break;
        case 'W':
          rule = selectNumberRule(4, tokenLen);
          break;
        case 'a':
          rule = new TextField(9, AmPmStrings);
          break;
        case 'k':
          rule = new TwentyFourHourField(selectNumberRule(11, tokenLen));
          break;
        case 'K':
          rule = selectNumberRule(10, tokenLen);
          break;
        case 'z':
          if (tokenLen >= 4) {
            rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 1);
            break;
          } 
          rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
          break;
        case 'Z':
          if (tokenLen == 1) {
            rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
            break;
          } 
          rule = TimeZoneNumberRule.INSTANCE_COLON;
          break;
        case '\'':
          sub = token.substring(1);
          if (sub.length() == 1) {
            rule = new CharacterLiteral(sub.charAt(0));
            break;
          } 
          rule = new StringLiteral(sub);
          break;
        default:
          throw new IllegalArgumentException("Illegal pattern component: " + token);
      } 
      rules.add(rule);
    } 
    return rules;
  }
  
  protected String parseToken(String pattern, int[] indexRef) {
    StringBuilder buf = new StringBuilder();
    int i = indexRef[0];
    int length = pattern.length();
    char c = pattern.charAt(i);
    if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
      buf.append(c);
      while (i + 1 < length) {
        char peek = pattern.charAt(i + 1);
        if (peek == c) {
          buf.append(c);
          i++;
        } 
      } 
    } else {
      buf.append('\'');
      boolean inLiteral = false;
      for (; i < length; i++) {
        c = pattern.charAt(i);
        if (c == '\'') {
          if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
            i++;
            buf.append(c);
          } else {
            inLiteral = !inLiteral;
          } 
        } else {
          if (!inLiteral && ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
            i--;
            break;
          } 
          buf.append(c);
        } 
      } 
    } 
    indexRef[0] = i;
    return buf.toString();
  }
  
  protected NumberRule selectNumberRule(int field, int padding) {
    switch (padding) {
      case 1:
        return new UnpaddedNumberField(field);
      case 2:
        return new TwoDigitNumberField(field);
    } 
    return new PaddedNumberField(field, padding);
  }
  
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
    if (obj instanceof Date)
      return format((Date)obj, toAppendTo); 
    if (obj instanceof Calendar)
      return format((Calendar)obj, toAppendTo); 
    if (obj instanceof Long)
      return format(((Long)obj).longValue(), toAppendTo); 
    throw new IllegalArgumentException("Unknown class: " + ((obj == null) ? "<null>" : obj.getClass().getName()));
  }
  
  public String format(long millis) {
    Calendar c = newCalendar();
    c.setTimeInMillis(millis);
    return applyRulesToString(c);
  }
  
  private String applyRulesToString(Calendar c) {
    return applyRules(c, new StringBuffer(this.mMaxLengthEstimate)).toString();
  }
  
  private GregorianCalendar newCalendar() {
    return new GregorianCalendar(this.mTimeZone, this.mLocale);
  }
  
  public String format(Date date) {
    Calendar c = newCalendar();
    c.setTime(date);
    return applyRulesToString(c);
  }
  
  public String format(Calendar calendar) {
    return format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
  }
  
  public StringBuffer format(long millis, StringBuffer buf) {
    return format(new Date(millis), buf);
  }
  
  public StringBuffer format(Date date, StringBuffer buf) {
    Calendar c = newCalendar();
    c.setTime(date);
    return applyRules(c, buf);
  }
  
  public StringBuffer format(Calendar calendar, StringBuffer buf) {
    return applyRules(calendar, buf);
  }
  
  protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
    for (Rule rule : this.mRules)
      rule.appendTo(buf, calendar); 
    return buf;
  }
  
  public String getPattern() {
    return this.mPattern;
  }
  
  public TimeZone getTimeZone() {
    return this.mTimeZone;
  }
  
  public Locale getLocale() {
    return this.mLocale;
  }
  
  public int getMaxLengthEstimate() {
    return this.mMaxLengthEstimate;
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof FastDatePrinter))
      return false; 
    FastDatePrinter other = (FastDatePrinter)obj;
    return (this.mPattern.equals(other.mPattern) && this.mTimeZone.equals(other.mTimeZone) && this.mLocale.equals(other.mLocale));
  }
  
  public int hashCode() {
    return this.mPattern.hashCode() + 13 * (this.mTimeZone.hashCode() + 13 * this.mLocale.hashCode());
  }
  
  public String toString() {
    return "FastDatePrinter[" + this.mPattern + "," + this.mLocale + "," + this.mTimeZone.getID() + "]";
  }
  
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    init();
  }
  
  private static interface Rule {
    int estimateLength();
    
    void appendTo(StringBuffer param1StringBuffer, Calendar param1Calendar);
  }
  
  private static interface NumberRule extends Rule {
    void appendTo(StringBuffer param1StringBuffer, int param1Int);
  }
  
  private static class CharacterLiteral implements Rule {
    private final char mValue;
    
    CharacterLiteral(char value) {
      this.mValue = value;
    }
    
    public int estimateLength() {
      return 1;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      buffer.append(this.mValue);
    }
  }
  
  private static class StringLiteral implements Rule {
    private final String mValue;
    
    StringLiteral(String value) {
      this.mValue = value;
    }
    
    public int estimateLength() {
      return this.mValue.length();
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      buffer.append(this.mValue);
    }
  }
  
  private static class TextField implements Rule {
    private final int mField;
    
    private final String[] mValues;
    
    TextField(int field, String[] values) {
      this.mField = field;
      this.mValues = values;
    }
    
    public int estimateLength() {
      int max = 0;
      for (int i = this.mValues.length; --i >= 0; ) {
        int len = this.mValues[i].length();
        if (len > max)
          max = len; 
      } 
      return max;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      buffer.append(this.mValues[calendar.get(this.mField)]);
    }
  }
  
  private static class UnpaddedNumberField implements NumberRule {
    private final int mField;
    
    UnpaddedNumberField(int field) {
      this.mField = field;
    }
    
    public int estimateLength() {
      return 4;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      appendTo(buffer, calendar.get(this.mField));
    }
    
    public final void appendTo(StringBuffer buffer, int value) {
      if (value < 10) {
        buffer.append((char)(value + 48));
      } else if (value < 100) {
        buffer.append((char)(value / 10 + 48));
        buffer.append((char)(value % 10 + 48));
      } else {
        buffer.append(Integer.toString(value));
      } 
    }
  }
  
  private static class UnpaddedMonthField implements NumberRule {
    static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();
    
    public int estimateLength() {
      return 2;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      appendTo(buffer, calendar.get(2) + 1);
    }
    
    public final void appendTo(StringBuffer buffer, int value) {
      if (value < 10) {
        buffer.append((char)(value + 48));
      } else {
        buffer.append((char)(value / 10 + 48));
        buffer.append((char)(value % 10 + 48));
      } 
    }
  }
  
  private static class PaddedNumberField implements NumberRule {
    private final int mField;
    
    private final int mSize;
    
    PaddedNumberField(int field, int size) {
      if (size < 3)
        throw new IllegalArgumentException(); 
      this.mField = field;
      this.mSize = size;
    }
    
    public int estimateLength() {
      return 4;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      appendTo(buffer, calendar.get(this.mField));
    }
    
    public final void appendTo(StringBuffer buffer, int value) {
      if (value < 100) {
        for (int i = this.mSize; --i >= 2;)
          buffer.append('0'); 
        buffer.append((char)(value / 10 + 48));
        buffer.append((char)(value % 10 + 48));
      } else {
        int digits;
        if (value < 1000) {
          digits = 3;
        } else {
          Validate.isTrue((value > -1), "Negative values should not be possible", value);
          digits = Integer.toString(value).length();
        } 
        for (int i = this.mSize; --i >= digits;)
          buffer.append('0'); 
        buffer.append(Integer.toString(value));
      } 
    }
  }
  
  private static class TwoDigitNumberField implements NumberRule {
    private final int mField;
    
    TwoDigitNumberField(int field) {
      this.mField = field;
    }
    
    public int estimateLength() {
      return 2;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      appendTo(buffer, calendar.get(this.mField));
    }
    
    public final void appendTo(StringBuffer buffer, int value) {
      if (value < 100) {
        buffer.append((char)(value / 10 + 48));
        buffer.append((char)(value % 10 + 48));
      } else {
        buffer.append(Integer.toString(value));
      } 
    }
  }
  
  private static class TwoDigitYearField implements NumberRule {
    static final TwoDigitYearField INSTANCE = new TwoDigitYearField();
    
    public int estimateLength() {
      return 2;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      appendTo(buffer, calendar.get(1) % 100);
    }
    
    public final void appendTo(StringBuffer buffer, int value) {
      buffer.append((char)(value / 10 + 48));
      buffer.append((char)(value % 10 + 48));
    }
  }
  
  private static class TwoDigitMonthField implements NumberRule {
    static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();
    
    public int estimateLength() {
      return 2;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      appendTo(buffer, calendar.get(2) + 1);
    }
    
    public final void appendTo(StringBuffer buffer, int value) {
      buffer.append((char)(value / 10 + 48));
      buffer.append((char)(value % 10 + 48));
    }
  }
  
  private static class TwelveHourField implements NumberRule {
    private final FastDatePrinter.NumberRule mRule;
    
    TwelveHourField(FastDatePrinter.NumberRule rule) {
      this.mRule = rule;
    }
    
    public int estimateLength() {
      return this.mRule.estimateLength();
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      int value = calendar.get(10);
      if (value == 0)
        value = calendar.getLeastMaximum(10) + 1; 
      this.mRule.appendTo(buffer, value);
    }
    
    public void appendTo(StringBuffer buffer, int value) {
      this.mRule.appendTo(buffer, value);
    }
  }
  
  private static class TwentyFourHourField implements NumberRule {
    private final FastDatePrinter.NumberRule mRule;
    
    TwentyFourHourField(FastDatePrinter.NumberRule rule) {
      this.mRule = rule;
    }
    
    public int estimateLength() {
      return this.mRule.estimateLength();
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      int value = calendar.get(11);
      if (value == 0)
        value = calendar.getMaximum(11) + 1; 
      this.mRule.appendTo(buffer, value);
    }
    
    public void appendTo(StringBuffer buffer, int value) {
      this.mRule.appendTo(buffer, value);
    }
  }
  
  private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache = new ConcurrentHashMap<TimeZoneDisplayKey, String>(7);
  
  static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
    TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
    String value = cTimeZoneDisplayCache.get(key);
    if (value == null) {
      value = tz.getDisplayName(daylight, style, locale);
      String prior = cTimeZoneDisplayCache.putIfAbsent(key, value);
      if (prior != null)
        value = prior; 
    } 
    return value;
  }
  
  private static class TimeZoneNameRule implements Rule {
    private final Locale mLocale;
    
    private final int mStyle;
    
    private final String mStandard;
    
    private final String mDaylight;
    
    TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
      this.mLocale = locale;
      this.mStyle = style;
      this.mStandard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, style, locale);
      this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, style, locale);
    }
    
    public int estimateLength() {
      return Math.max(this.mStandard.length(), this.mDaylight.length());
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      TimeZone zone = calendar.getTimeZone();
      if (zone.useDaylightTime() && calendar.get(16) != 0) {
        buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, true, this.mStyle, this.mLocale));
      } else {
        buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, false, this.mStyle, this.mLocale));
      } 
    }
  }
  
  private static class TimeZoneNumberRule implements Rule {
    static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
    
    static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
    
    final boolean mColon;
    
    TimeZoneNumberRule(boolean colon) {
      this.mColon = colon;
    }
    
    public int estimateLength() {
      return 5;
    }
    
    public void appendTo(StringBuffer buffer, Calendar calendar) {
      int offset = calendar.get(15) + calendar.get(16);
      if (offset < 0) {
        buffer.append('-');
        offset = -offset;
      } else {
        buffer.append('+');
      } 
      int hours = offset / 3600000;
      buffer.append((char)(hours / 10 + 48));
      buffer.append((char)(hours % 10 + 48));
      if (this.mColon)
        buffer.append(':'); 
      int minutes = offset / 60000 - 60 * hours;
      buffer.append((char)(minutes / 10 + 48));
      buffer.append((char)(minutes % 10 + 48));
    }
  }
  
  private static class TimeZoneDisplayKey {
    private final TimeZone mTimeZone;
    
    private final int mStyle;
    
    private final Locale mLocale;
    
    TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale) {
      this.mTimeZone = timeZone;
      if (daylight) {
        this.mStyle = style | Integer.MIN_VALUE;
      } else {
        this.mStyle = style;
      } 
      this.mLocale = locale;
    }
    
    public int hashCode() {
      return (this.mStyle * 31 + this.mLocale.hashCode()) * 31 + this.mTimeZone.hashCode();
    }
    
    public boolean equals(Object obj) {
      if (this == obj)
        return true; 
      if (obj instanceof TimeZoneDisplayKey) {
        TimeZoneDisplayKey other = (TimeZoneDisplayKey)obj;
        return (this.mTimeZone.equals(other.mTimeZone) && this.mStyle == other.mStyle && this.mLocale.equals(other.mLocale));
      } 
      return false;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\time\FastDatePrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */