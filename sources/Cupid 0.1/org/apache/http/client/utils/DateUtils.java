package org.apache.http.client.utils;

import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public final class DateUtils {
  public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
  
  public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";
  
  public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
  
  private static final String[] DEFAULT_PATTERNS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy" };
  
  private static final Date DEFAULT_TWO_DIGIT_YEAR_START;
  
  public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
  
  static {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeZone(GMT);
    calendar.set(2000, 0, 1, 0, 0, 0);
    calendar.set(14, 0);
    DEFAULT_TWO_DIGIT_YEAR_START = calendar.getTime();
  }
  
  public static Date parseDate(String dateValue) {
    return parseDate(dateValue, null, null);
  }
  
  public static Date parseDate(String dateValue, String[] dateFormats) {
    return parseDate(dateValue, dateFormats, null);
  }
  
  public static Date parseDate(String dateValue, String[] dateFormats, Date startDate) {
    Args.notNull(dateValue, "Date value");
    String[] localDateFormats = (dateFormats != null) ? dateFormats : DEFAULT_PATTERNS;
    Date localStartDate = (startDate != null) ? startDate : DEFAULT_TWO_DIGIT_YEAR_START;
    String v = dateValue;
    if (v.length() > 1 && v.startsWith("'") && v.endsWith("'"))
      v = v.substring(1, v.length() - 1); 
    for (String dateFormat : localDateFormats) {
      SimpleDateFormat dateParser = DateFormatHolder.formatFor(dateFormat);
      dateParser.set2DigitYearStart(localStartDate);
      ParsePosition pos = new ParsePosition(0);
      Date result = dateParser.parse(v, pos);
      if (pos.getIndex() != 0)
        return result; 
    } 
    return null;
  }
  
  public static String formatDate(Date date) {
    return formatDate(date, "EEE, dd MMM yyyy HH:mm:ss zzz");
  }
  
  public static String formatDate(Date date, String pattern) {
    Args.notNull(date, "Date");
    Args.notNull(pattern, "Pattern");
    SimpleDateFormat formatter = DateFormatHolder.formatFor(pattern);
    return formatter.format(date);
  }
  
  public static void clearThreadLocal() {
    DateFormatHolder.clearThreadLocal();
  }
  
  static final class DateFormatHolder {
    private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS = new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>() {
        protected SoftReference<Map<String, SimpleDateFormat>> initialValue() {
          return new SoftReference<Map<String, SimpleDateFormat>>(new HashMap<String, SimpleDateFormat>());
        }
      };
    
    public static SimpleDateFormat formatFor(String pattern) {
      SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
      Map<String, SimpleDateFormat> formats = ref.get();
      if (formats == null) {
        formats = new HashMap<String, SimpleDateFormat>();
        THREADLOCAL_FORMATS.set(new SoftReference<Map<String, SimpleDateFormat>>(formats));
      } 
      SimpleDateFormat format = formats.get(pattern);
      if (format == null) {
        format = new SimpleDateFormat(pattern, Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        formats.put(pattern, format);
      } 
      return format;
    }
    
    public static void clearThreadLocal() {
      THREADLOCAL_FORMATS.remove();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\clien\\utils\DateUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */