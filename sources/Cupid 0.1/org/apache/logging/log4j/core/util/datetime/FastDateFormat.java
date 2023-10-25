package org.apache.logging.log4j.core.util.datetime;

import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FastDateFormat extends Format implements DateParser, DatePrinter {
  private static final long serialVersionUID = 2L;
  
  public static final int FULL = 0;
  
  public static final int LONG = 1;
  
  public static final int MEDIUM = 2;
  
  public static final int SHORT = 3;
  
  private static final FormatCache<FastDateFormat> cache = new FormatCache<FastDateFormat>() {
      protected FastDateFormat createInstance(String pattern, TimeZone timeZone, Locale locale) {
        return new FastDateFormat(pattern, timeZone, locale);
      }
    };
  
  private final FastDatePrinter printer;
  
  private final FastDateParser parser;
  
  public static FastDateFormat getInstance() {
    return cache.getInstance();
  }
  
  public static FastDateFormat getInstance(String pattern) {
    return cache.getInstance(pattern, null, null);
  }
  
  public static FastDateFormat getInstance(String pattern, TimeZone timeZone) {
    return cache.getInstance(pattern, timeZone, null);
  }
  
  public static FastDateFormat getInstance(String pattern, Locale locale) {
    return cache.getInstance(pattern, null, locale);
  }
  
  public static FastDateFormat getInstance(String pattern, TimeZone timeZone, Locale locale) {
    return cache.getInstance(pattern, timeZone, locale);
  }
  
  public static FastDateFormat getDateInstance(int style) {
    return cache.getDateInstance(style, null, null);
  }
  
  public static FastDateFormat getDateInstance(int style, Locale locale) {
    return cache.getDateInstance(style, null, locale);
  }
  
  public static FastDateFormat getDateInstance(int style, TimeZone timeZone) {
    return cache.getDateInstance(style, timeZone, null);
  }
  
  public static FastDateFormat getDateInstance(int style, TimeZone timeZone, Locale locale) {
    return cache.getDateInstance(style, timeZone, locale);
  }
  
  public static FastDateFormat getTimeInstance(int style) {
    return cache.getTimeInstance(style, null, null);
  }
  
  public static FastDateFormat getTimeInstance(int style, Locale locale) {
    return cache.getTimeInstance(style, null, locale);
  }
  
  public static FastDateFormat getTimeInstance(int style, TimeZone timeZone) {
    return cache.getTimeInstance(style, timeZone, null);
  }
  
  public static FastDateFormat getTimeInstance(int style, TimeZone timeZone, Locale locale) {
    return cache.getTimeInstance(style, timeZone, locale);
  }
  
  public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle) {
    return cache.getDateTimeInstance(dateStyle, timeStyle, (TimeZone)null, (Locale)null);
  }
  
  public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale locale) {
    return cache.getDateTimeInstance(dateStyle, timeStyle, (TimeZone)null, locale);
  }
  
  public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone) {
    return getDateTimeInstance(dateStyle, timeStyle, timeZone, null);
  }
  
  public static FastDateFormat getDateTimeInstance(int dateStyle, int timeStyle, TimeZone timeZone, Locale locale) {
    return cache.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
  }
  
  protected FastDateFormat(String pattern, TimeZone timeZone, Locale locale) {
    this(pattern, timeZone, locale, null);
  }
  
  protected FastDateFormat(String pattern, TimeZone timeZone, Locale locale, Date centuryStart) {
    this.printer = new FastDatePrinter(pattern, timeZone, locale);
    this.parser = new FastDateParser(pattern, timeZone, locale, centuryStart);
  }
  
  public StringBuilder format(Object obj, StringBuilder toAppendTo, FieldPosition pos) {
    return toAppendTo.append(this.printer.format(obj));
  }
  
  public String format(long millis) {
    return this.printer.format(millis);
  }
  
  public String format(Date date) {
    return this.printer.format(date);
  }
  
  public String format(Calendar calendar) {
    return this.printer.format(calendar);
  }
  
  public <B extends Appendable> B format(long millis, B buf) {
    return this.printer.format(millis, buf);
  }
  
  public <B extends Appendable> B format(Date date, B buf) {
    return this.printer.format(date, buf);
  }
  
  public <B extends Appendable> B format(Calendar calendar, B buf) {
    return this.printer.format(calendar, buf);
  }
  
  public Date parse(String source) throws ParseException {
    return this.parser.parse(source);
  }
  
  public Date parse(String source, ParsePosition pos) {
    return this.parser.parse(source, pos);
  }
  
  public boolean parse(String source, ParsePosition pos, Calendar calendar) {
    return this.parser.parse(source, pos, calendar);
  }
  
  public Object parseObject(String source, ParsePosition pos) {
    return this.parser.parseObject(source, pos);
  }
  
  public String getPattern() {
    return this.printer.getPattern();
  }
  
  public TimeZone getTimeZone() {
    return this.printer.getTimeZone();
  }
  
  public Locale getLocale() {
    return this.printer.getLocale();
  }
  
  public int getMaxLengthEstimate() {
    return this.printer.getMaxLengthEstimate();
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof FastDateFormat))
      return false; 
    FastDateFormat other = (FastDateFormat)obj;
    return this.printer.equals(other.printer);
  }
  
  public int hashCode() {
    return this.printer.hashCode();
  }
  
  public String toString() {
    return "FastDateFormat[" + this.printer.getPattern() + "," + this.printer.getLocale() + "," + this.printer.getTimeZone().getID() + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\datetime\FastDateFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */