package com.ibm.icu.text;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.RelativeDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import java.io.InvalidObjectException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

public abstract class DateFormat extends UFormat {
  protected Calendar calendar;
  
  protected NumberFormat numberFormat;
  
  public static final int ERA_FIELD = 0;
  
  public static final int YEAR_FIELD = 1;
  
  public static final int MONTH_FIELD = 2;
  
  public static final int DATE_FIELD = 3;
  
  public static final int HOUR_OF_DAY1_FIELD = 4;
  
  public static final int HOUR_OF_DAY0_FIELD = 5;
  
  public static final int MINUTE_FIELD = 6;
  
  public static final int SECOND_FIELD = 7;
  
  public static final int FRACTIONAL_SECOND_FIELD = 8;
  
  public static final int MILLISECOND_FIELD = 8;
  
  public static final int DAY_OF_WEEK_FIELD = 9;
  
  public static final int DAY_OF_YEAR_FIELD = 10;
  
  public static final int DAY_OF_WEEK_IN_MONTH_FIELD = 11;
  
  public static final int WEEK_OF_YEAR_FIELD = 12;
  
  public static final int WEEK_OF_MONTH_FIELD = 13;
  
  public static final int AM_PM_FIELD = 14;
  
  public static final int HOUR1_FIELD = 15;
  
  public static final int HOUR0_FIELD = 16;
  
  public static final int TIMEZONE_FIELD = 17;
  
  public static final int YEAR_WOY_FIELD = 18;
  
  public static final int DOW_LOCAL_FIELD = 19;
  
  public static final int EXTENDED_YEAR_FIELD = 20;
  
  public static final int JULIAN_DAY_FIELD = 21;
  
  public static final int MILLISECONDS_IN_DAY_FIELD = 22;
  
  public static final int TIMEZONE_RFC_FIELD = 23;
  
  public static final int TIMEZONE_GENERIC_FIELD = 24;
  
  public static final int STANDALONE_DAY_FIELD = 25;
  
  public static final int STANDALONE_MONTH_FIELD = 26;
  
  public static final int QUARTER_FIELD = 27;
  
  public static final int STANDALONE_QUARTER_FIELD = 28;
  
  public static final int TIMEZONE_SPECIAL_FIELD = 29;
  
  public static final int YEAR_NAME_FIELD = 30;
  
  public static final int TIMEZONE_LOCALIZED_GMT_OFFSET_FIELD = 31;
  
  public static final int TIMEZONE_ISO_FIELD = 32;
  
  public static final int TIMEZONE_ISO_LOCAL_FIELD = 33;
  
  public static final int FIELD_COUNT = 34;
  
  private static final long serialVersionUID = 7218322306649953788L;
  
  public static final int NONE = -1;
  
  public static final int FULL = 0;
  
  public static final int LONG = 1;
  
  public static final int MEDIUM = 2;
  
  public static final int SHORT = 3;
  
  public static final int DEFAULT = 2;
  
  public static final int RELATIVE = 128;
  
  public static final int RELATIVE_FULL = 128;
  
  public static final int RELATIVE_LONG = 129;
  
  public static final int RELATIVE_MEDIUM = 130;
  
  public static final int RELATIVE_SHORT = 131;
  
  public static final int RELATIVE_DEFAULT = 130;
  
  public static final String YEAR = "y";
  
  public static final String QUARTER = "QQQQ";
  
  public static final String ABBR_QUARTER = "QQQ";
  
  public static final String YEAR_QUARTER = "yQQQQ";
  
  public static final String YEAR_ABBR_QUARTER = "yQQQ";
  
  public static final String MONTH = "MMMM";
  
  public static final String ABBR_MONTH = "MMM";
  
  public static final String NUM_MONTH = "M";
  
  public static final String YEAR_MONTH = "yMMMM";
  
  public static final String YEAR_ABBR_MONTH = "yMMM";
  
  public static final String YEAR_NUM_MONTH = "yM";
  
  public static final String DAY = "d";
  
  public static final String YEAR_MONTH_DAY = "yMMMMd";
  
  public static final String YEAR_ABBR_MONTH_DAY = "yMMMd";
  
  public static final String YEAR_NUM_MONTH_DAY = "yMd";
  
  public static final String WEEKDAY = "EEEE";
  
  public static final String ABBR_WEEKDAY = "E";
  
  public static final String YEAR_MONTH_WEEKDAY_DAY = "yMMMMEEEEd";
  
  public static final String YEAR_ABBR_MONTH_WEEKDAY_DAY = "yMMMEd";
  
  public static final String YEAR_NUM_MONTH_WEEKDAY_DAY = "yMEd";
  
  public static final String MONTH_DAY = "MMMMd";
  
  public static final String ABBR_MONTH_DAY = "MMMd";
  
  public static final String NUM_MONTH_DAY = "Md";
  
  public static final String MONTH_WEEKDAY_DAY = "MMMMEEEEd";
  
  public static final String ABBR_MONTH_WEEKDAY_DAY = "MMMEd";
  
  public static final String NUM_MONTH_WEEKDAY_DAY = "MEd";
  
  public static final String HOUR = "j";
  
  public static final String HOUR24 = "H";
  
  public static final String MINUTE = "m";
  
  public static final String HOUR_MINUTE = "jm";
  
  public static final String HOUR24_MINUTE = "Hm";
  
  public static final String SECOND = "s";
  
  public static final String HOUR_MINUTE_SECOND = "jms";
  
  public static final String HOUR24_MINUTE_SECOND = "Hms";
  
  public static final String MINUTE_SECOND = "ms";
  
  public static final String LOCATION_TZ = "VVVV";
  
  public static final String GENERIC_TZ = "vvvv";
  
  public static final String ABBR_GENERIC_TZ = "v";
  
  public static final String SPECIFIC_TZ = "zzzz";
  
  public static final String ABBR_SPECIFIC_TZ = "z";
  
  public static final String ABBR_UTC_TZ = "ZZZZ";
  
  public static final String STANDALONE_MONTH = "LLLL";
  
  public static final String ABBR_STANDALONE_MONTH = "LLL";
  
  public static final String HOUR_MINUTE_GENERIC_TZ = "jmv";
  
  public static final String HOUR_MINUTE_TZ = "jmz";
  
  public static final String HOUR_GENERIC_TZ = "jv";
  
  public static final String HOUR_TZ = "jz";
  
  public final StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition fieldPosition) {
    if (obj instanceof Calendar)
      return format((Calendar)obj, toAppendTo, fieldPosition); 
    if (obj instanceof Date)
      return format((Date)obj, toAppendTo, fieldPosition); 
    if (obj instanceof Number)
      return format(new Date(((Number)obj).longValue()), toAppendTo, fieldPosition); 
    throw new IllegalArgumentException("Cannot format given Object (" + obj.getClass().getName() + ") as a Date");
  }
  
  public abstract StringBuffer format(Calendar paramCalendar, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
  
  public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
    this.calendar.setTime(date);
    return format(this.calendar, toAppendTo, fieldPosition);
  }
  
  public final String format(Date date) {
    return format(date, new StringBuffer(64), new FieldPosition(0)).toString();
  }
  
  public Date parse(String text) throws ParseException {
    ParsePosition pos = new ParsePosition(0);
    Date result = parse(text, pos);
    if (pos.getIndex() == 0)
      throw new ParseException("Unparseable date: \"" + text + "\"", pos.getErrorIndex()); 
    return result;
  }
  
  public abstract void parse(String paramString, Calendar paramCalendar, ParsePosition paramParsePosition);
  
  public Date parse(String text, ParsePosition pos) {
    Date result = null;
    int start = pos.getIndex();
    TimeZone tzsav = this.calendar.getTimeZone();
    this.calendar.clear();
    parse(text, this.calendar, pos);
    if (pos.getIndex() != start)
      try {
        result = this.calendar.getTime();
      } catch (IllegalArgumentException e) {
        pos.setIndex(start);
        pos.setErrorIndex(start);
      }  
    this.calendar.setTimeZone(tzsav);
    return result;
  }
  
  public Object parseObject(String source, ParsePosition pos) {
    return parse(source, pos);
  }
  
  public static final DateFormat getTimeInstance() {
    return get(-1, 2, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public static final DateFormat getTimeInstance(int style) {
    return get(-1, style, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public static final DateFormat getTimeInstance(int style, Locale aLocale) {
    return get(-1, style, ULocale.forLocale(aLocale));
  }
  
  public static final DateFormat getTimeInstance(int style, ULocale locale) {
    return get(-1, style, locale);
  }
  
  public static final DateFormat getDateInstance() {
    return get(2, -1, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public static final DateFormat getDateInstance(int style) {
    return get(style, -1, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public static final DateFormat getDateInstance(int style, Locale aLocale) {
    return get(style, -1, ULocale.forLocale(aLocale));
  }
  
  public static final DateFormat getDateInstance(int style, ULocale locale) {
    return get(style, -1, locale);
  }
  
  public static final DateFormat getDateTimeInstance() {
    return get(2, 2, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public static final DateFormat getDateTimeInstance(int dateStyle, int timeStyle) {
    return get(dateStyle, timeStyle, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public static final DateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale aLocale) {
    return get(dateStyle, timeStyle, ULocale.forLocale(aLocale));
  }
  
  public static final DateFormat getDateTimeInstance(int dateStyle, int timeStyle, ULocale locale) {
    return get(dateStyle, timeStyle, locale);
  }
  
  public static final DateFormat getInstance() {
    return getDateTimeInstance(3, 3);
  }
  
  public static Locale[] getAvailableLocales() {
    return ICUResourceBundle.getAvailableLocales();
  }
  
  public static ULocale[] getAvailableULocales() {
    return ICUResourceBundle.getAvailableULocales();
  }
  
  public void setCalendar(Calendar newCalendar) {
    this.calendar = newCalendar;
  }
  
  public Calendar getCalendar() {
    return this.calendar;
  }
  
  public void setNumberFormat(NumberFormat newNumberFormat) {
    this.numberFormat = newNumberFormat;
    this.numberFormat.setParseIntegerOnly(true);
  }
  
  public NumberFormat getNumberFormat() {
    return this.numberFormat;
  }
  
  public void setTimeZone(TimeZone zone) {
    this.calendar.setTimeZone(zone);
  }
  
  public TimeZone getTimeZone() {
    return this.calendar.getTimeZone();
  }
  
  public void setLenient(boolean lenient) {
    this.calendar.setLenient(lenient);
  }
  
  public boolean isLenient() {
    return this.calendar.isLenient();
  }
  
  public int hashCode() {
    return this.numberFormat.hashCode();
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null || getClass() != obj.getClass())
      return false; 
    DateFormat other = (DateFormat)obj;
    return (this.calendar.isEquivalentTo(other.calendar) && this.numberFormat.equals(other.numberFormat));
  }
  
  public Object clone() {
    DateFormat other = (DateFormat)super.clone();
    other.calendar = (Calendar)this.calendar.clone();
    other.numberFormat = (NumberFormat)this.numberFormat.clone();
    return other;
  }
  
  private static DateFormat get(int dateStyle, int timeStyle, ULocale loc) {
    if ((timeStyle != -1 && (timeStyle & 0x80) > 0) || (dateStyle != -1 && (dateStyle & 0x80) > 0)) {
      RelativeDateFormat r = new RelativeDateFormat(timeStyle, dateStyle, loc);
      return (DateFormat)r;
    } 
    if (timeStyle < -1 || timeStyle > 3)
      throw new IllegalArgumentException("Illegal time style " + timeStyle); 
    if (dateStyle < -1 || dateStyle > 3)
      throw new IllegalArgumentException("Illegal date style " + dateStyle); 
    try {
      Calendar cal = Calendar.getInstance(loc);
      DateFormat result = cal.getDateTimeFormat(dateStyle, timeStyle, loc);
      result.setLocale(cal.getLocale(ULocale.VALID_LOCALE), cal.getLocale(ULocale.ACTUAL_LOCALE));
      return result;
    } catch (MissingResourceException e) {
      return new SimpleDateFormat("M/d/yy h:mm a");
    } 
  }
  
  public static final DateFormat getDateInstance(Calendar cal, int dateStyle, Locale locale) {
    return getDateTimeInstance(cal, dateStyle, -1, ULocale.forLocale(locale));
  }
  
  public static final DateFormat getDateInstance(Calendar cal, int dateStyle, ULocale locale) {
    return getDateTimeInstance(cal, dateStyle, -1, locale);
  }
  
  public static final DateFormat getTimeInstance(Calendar cal, int timeStyle, Locale locale) {
    return getDateTimeInstance(cal, -1, timeStyle, ULocale.forLocale(locale));
  }
  
  public static final DateFormat getTimeInstance(Calendar cal, int timeStyle, ULocale locale) {
    return getDateTimeInstance(cal, -1, timeStyle, locale);
  }
  
  public static final DateFormat getDateTimeInstance(Calendar cal, int dateStyle, int timeStyle, Locale locale) {
    return cal.getDateTimeFormat(dateStyle, timeStyle, ULocale.forLocale(locale));
  }
  
  public static final DateFormat getDateTimeInstance(Calendar cal, int dateStyle, int timeStyle, ULocale locale) {
    return cal.getDateTimeFormat(dateStyle, timeStyle, locale);
  }
  
  public static final DateFormat getInstance(Calendar cal, Locale locale) {
    return getDateTimeInstance(cal, 3, 3, ULocale.forLocale(locale));
  }
  
  public static final DateFormat getInstance(Calendar cal, ULocale locale) {
    return getDateTimeInstance(cal, 3, 3, locale);
  }
  
  public static final DateFormat getInstance(Calendar cal) {
    return getInstance(cal, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public static final DateFormat getDateInstance(Calendar cal, int dateStyle) {
    return getDateInstance(cal, dateStyle, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public static final DateFormat getTimeInstance(Calendar cal, int timeStyle) {
    return getTimeInstance(cal, timeStyle, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public static final DateFormat getDateTimeInstance(Calendar cal, int dateStyle, int timeStyle) {
    return getDateTimeInstance(cal, dateStyle, timeStyle, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public static final DateFormat getPatternInstance(String skeleton) {
    return getPatternInstance(skeleton, ULocale.getDefault(ULocale.Category.FORMAT));
  }
  
  public static final DateFormat getPatternInstance(String skeleton, Locale locale) {
    return getPatternInstance(skeleton, ULocale.forLocale(locale));
  }
  
  public static final DateFormat getPatternInstance(String skeleton, ULocale locale) {
    DateTimePatternGenerator generator = DateTimePatternGenerator.getInstance(locale);
    String bestPattern = generator.getBestPattern(skeleton);
    return new SimpleDateFormat(bestPattern, locale);
  }
  
  public static final DateFormat getPatternInstance(Calendar cal, String skeleton, Locale locale) {
    return getPatternInstance(cal, skeleton, ULocale.forLocale(locale));
  }
  
  public static final DateFormat getPatternInstance(Calendar cal, String skeleton, ULocale locale) {
    DateTimePatternGenerator generator = DateTimePatternGenerator.getInstance(locale);
    String bestPattern = generator.getBestPattern(skeleton);
    SimpleDateFormat format = new SimpleDateFormat(bestPattern, locale);
    format.setCalendar(cal);
    return format;
  }
  
  public static class Field extends Format.Field {
    private static final long serialVersionUID = -3627456821000730829L;
    
    private static final int CAL_FIELD_COUNT;
    
    static {
      GregorianCalendar cal = new GregorianCalendar();
      CAL_FIELD_COUNT = cal.getFieldCount();
    }
    
    private static final Field[] CAL_FIELDS = new Field[CAL_FIELD_COUNT];
    
    private static final Map<String, Field> FIELD_NAME_MAP = new HashMap<String, Field>(CAL_FIELD_COUNT);
    
    public static final Field AM_PM = new Field("am pm", 9);
    
    public static final Field DAY_OF_MONTH = new Field("day of month", 5);
    
    public static final Field DAY_OF_WEEK = new Field("day of week", 7);
    
    public static final Field DAY_OF_WEEK_IN_MONTH = new Field("day of week in month", 8);
    
    public static final Field DAY_OF_YEAR = new Field("day of year", 6);
    
    public static final Field ERA = new Field("era", 0);
    
    public static final Field HOUR_OF_DAY0 = new Field("hour of day", 11);
    
    public static final Field HOUR_OF_DAY1 = new Field("hour of day 1", -1);
    
    public static final Field HOUR0 = new Field("hour", 10);
    
    public static final Field HOUR1 = new Field("hour 1", -1);
    
    public static final Field MILLISECOND = new Field("millisecond", 14);
    
    public static final Field MINUTE = new Field("minute", 12);
    
    public static final Field MONTH = new Field("month", 2);
    
    public static final Field SECOND = new Field("second", 13);
    
    public static final Field TIME_ZONE = new Field("time zone", -1);
    
    public static final Field WEEK_OF_MONTH = new Field("week of month", 4);
    
    public static final Field WEEK_OF_YEAR = new Field("week of year", 3);
    
    public static final Field YEAR = new Field("year", 1);
    
    public static final Field DOW_LOCAL = new Field("local day of week", 18);
    
    public static final Field EXTENDED_YEAR = new Field("extended year", 19);
    
    public static final Field JULIAN_DAY = new Field("Julian day", 20);
    
    public static final Field MILLISECONDS_IN_DAY = new Field("milliseconds in day", 21);
    
    public static final Field YEAR_WOY = new Field("year for week of year", 17);
    
    public static final Field QUARTER = new Field("quarter", -1);
    
    private final int calendarField;
    
    protected Field(String name, int calendarField) {
      super(name);
      this.calendarField = calendarField;
      if (getClass() == Field.class) {
        FIELD_NAME_MAP.put(name, this);
        if (calendarField >= 0 && calendarField < CAL_FIELD_COUNT)
          CAL_FIELDS[calendarField] = this; 
      } 
    }
    
    public static Field ofCalendarField(int calendarField) {
      if (calendarField < 0 || calendarField >= CAL_FIELD_COUNT)
        throw new IllegalArgumentException("Calendar field number is out of range"); 
      return CAL_FIELDS[calendarField];
    }
    
    public int getCalendarField() {
      return this.calendarField;
    }
    
    protected Object readResolve() throws InvalidObjectException {
      if (getClass() != Field.class)
        throw new InvalidObjectException("A subclass of DateFormat.Field must implement readResolve."); 
      Object o = FIELD_NAME_MAP.get(getName());
      if (o == null)
        throw new InvalidObjectException("Unknown attribute name."); 
      return o;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\DateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */