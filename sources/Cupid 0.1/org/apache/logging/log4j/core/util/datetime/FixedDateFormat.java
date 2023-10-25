package org.apache.logging.log4j.core.util.datetime;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.time.Instant;

public class FixedDateFormat {
  private static final char NONE = '\000';
  
  private final FixedFormat fixedFormat;
  
  private final TimeZone timeZone;
  
  private final int length;
  
  private final int secondFractionDigits;
  
  private final FastDateFormat fastDateFormat;
  
  private final char timeSeparatorChar;
  
  private final char millisSeparatorChar;
  
  private final int timeSeparatorLength;
  
  private final int millisSeparatorLength;
  
  private final FixedTimeZoneFormat fixedTimeZoneFormat;
  
  private volatile long midnightToday;
  
  private volatile long midnightTomorrow;
  
  public enum FixedFormat {
    ABSOLUTE("HH:mm:ss,SSS", null, 0, ':', 1, ',', 1, 3, null),
    ABSOLUTE_MICROS("HH:mm:ss,nnnnnn", null, 0, ':', 1, ',', 1, 6, null),
    ABSOLUTE_NANOS("HH:mm:ss,nnnnnnnnn", null, 0, ':', 1, ',', 1, 9, null),
    ABSOLUTE_PERIOD("HH:mm:ss.SSS", null, 0, ':', 1, '.', 1, 3, null),
    COMPACT("yyyyMMddHHmmssSSS", "yyyyMMdd", 0, ' ', 0, ' ', 0, 3, null),
    DATE("dd MMM yyyy HH:mm:ss,SSS", "dd MMM yyyy ", 0, ':', 1, ',', 1, 3, null),
    DATE_PERIOD("dd MMM yyyy HH:mm:ss.SSS", "dd MMM yyyy ", 0, ':', 1, '.', 1, 3, null),
    DEFAULT("yyyy-MM-dd HH:mm:ss,SSS", "yyyy-MM-dd ", 0, ':', 1, ',', 1, 3, null),
    DEFAULT_MICROS("yyyy-MM-dd HH:mm:ss,nnnnnn", "yyyy-MM-dd ", 0, ':', 1, ',', 1, 6, null),
    DEFAULT_NANOS("yyyy-MM-dd HH:mm:ss,nnnnnnnnn", "yyyy-MM-dd ", 0, ':', 1, ',', 1, 9, null),
    DEFAULT_PERIOD("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd ", 0, ':', 1, '.', 1, 3, null),
    ISO8601_BASIC("yyyyMMdd'T'HHmmss,SSS", "yyyyMMdd'T'", 2, ' ', 0, ',', 1, 3, null),
    ISO8601_BASIC_PERIOD("yyyyMMdd'T'HHmmss.SSS", "yyyyMMdd'T'", 2, ' ', 0, '.', 1, 3, null),
    ISO8601("yyyy-MM-dd'T'HH:mm:ss,SSS", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, null),
    ISO8601_OFFSET_DATE_TIME_HH("yyyy-MM-dd'T'HH:mm:ss,SSSX", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, FixedDateFormat.FixedTimeZoneFormat.HH),
    ISO8601_OFFSET_DATE_TIME_HHMM("yyyy-MM-dd'T'HH:mm:ss,SSSXX", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, FixedDateFormat.FixedTimeZoneFormat.HHMM),
    ISO8601_OFFSET_DATE_TIME_HHCMM("yyyy-MM-dd'T'HH:mm:ss,SSSXXX", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, FixedDateFormat.FixedTimeZoneFormat.HHCMM),
    ISO8601_PERIOD("yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'", 2, ':', 1, '.', 1, 3, null),
    ISO8601_PERIOD_MICROS("yyyy-MM-dd'T'HH:mm:ss.nnnnnn", "yyyy-MM-dd'T'", 2, ':', 1, '.', 1, 6, null),
    US_MONTH_DAY_YEAR2_TIME("dd/MM/yy HH:mm:ss.SSS", "dd/MM/yy ", 0, ':', 1, '.', 1, 3, null),
    US_MONTH_DAY_YEAR4_TIME("dd/MM/yyyy HH:mm:ss.SSS", "dd/MM/yyyy ", 0, ':', 1, '.', 1, 3, null);
    
    private static final String DEFAULT_SECOND_FRACTION_PATTERN = "SSS";
    
    private static final int MILLI_FRACTION_DIGITS = "SSS".length();
    
    private static final char SECOND_FRACTION_PATTERN = 'n';
    
    private final String pattern;
    
    private final String datePattern;
    
    private final int escapeCount;
    
    private final char timeSeparatorChar;
    
    private final int timeSeparatorLength;
    
    private final char millisSeparatorChar;
    
    private final int millisSeparatorLength;
    
    private final int secondFractionDigits;
    
    private final FixedDateFormat.FixedTimeZoneFormat fixedTimeZoneFormat;
    
    private static final int[] EMPTY_RANGE = new int[] { -1, -1 };
    
    static {
    
    }
    
    FixedFormat(String pattern, String datePattern, int escapeCount, char timeSeparator, int timeSepLength, char millisSeparator, int millisSepLength, int secondFractionDigits, FixedDateFormat.FixedTimeZoneFormat timeZoneFormat) {
      this.timeSeparatorChar = timeSeparator;
      this.timeSeparatorLength = timeSepLength;
      this.millisSeparatorChar = millisSeparator;
      this.millisSeparatorLength = millisSepLength;
      this.pattern = Objects.<String>requireNonNull(pattern);
      this.datePattern = datePattern;
      this.escapeCount = escapeCount;
      this.secondFractionDigits = secondFractionDigits;
      this.fixedTimeZoneFormat = timeZoneFormat;
    }
    
    public String getPattern() {
      return this.pattern;
    }
    
    public String getDatePattern() {
      return this.datePattern;
    }
    
    public static FixedFormat lookup(String nameOrPattern) {
      for (FixedFormat type : values()) {
        if (type.name().equals(nameOrPattern) || type.getPattern().equals(nameOrPattern))
          return type; 
      } 
      return null;
    }
    
    static FixedFormat lookupIgnoringNanos(String pattern) {
      int[] nanoRange = nanoRange(pattern);
      int nanoStart = nanoRange[0];
      int nanoEnd = nanoRange[1];
      if (nanoStart > 0) {
        String subPattern = pattern.substring(0, nanoStart) + "SSS" + pattern.substring(nanoEnd, pattern.length());
        for (FixedFormat type : values()) {
          if (type.getPattern().equals(subPattern))
            return type; 
        } 
      } 
      return null;
    }
    
    private static int[] nanoRange(String pattern) {
      int indexStart = pattern.indexOf('n');
      int indexEnd = -1;
      if (indexStart >= 0) {
        indexEnd = pattern.indexOf('Z', indexStart);
        indexEnd = (indexEnd < 0) ? pattern.indexOf('X', indexStart) : indexEnd;
        indexEnd = (indexEnd < 0) ? pattern.length() : indexEnd;
        for (int i = indexStart + 1; i < indexEnd; i++) {
          if (pattern.charAt(i) != 'n')
            return EMPTY_RANGE; 
        } 
      } 
      return new int[] { indexStart, indexEnd };
    }
    
    public int getLength() {
      return this.pattern.length() - this.escapeCount;
    }
    
    public int getDatePatternLength() {
      return (getDatePattern() == null) ? 0 : (getDatePattern().length() - this.escapeCount);
    }
    
    public FastDateFormat getFastDateFormat() {
      return getFastDateFormat((TimeZone)null);
    }
    
    public FastDateFormat getFastDateFormat(TimeZone tz) {
      return (getDatePattern() == null) ? null : FastDateFormat.getInstance(getDatePattern(), tz);
    }
    
    public int getSecondFractionDigits() {
      return this.secondFractionDigits;
    }
    
    public FixedDateFormat.FixedTimeZoneFormat getFixedTimeZoneFormat() {
      return this.fixedTimeZoneFormat;
    }
  }
  
  public enum FixedTimeZoneFormat {
    HH(false, false, 3),
    HHMM(false, true, 5),
    HHCMM(':', true, 6);
    
    private final char timeSeparatorChar;
    
    private final int timeSeparatorCharLen;
    
    private final boolean useMinutes;
    
    private final int length;
    
    FixedTimeZoneFormat(char timeSeparatorChar, boolean minutes, int length) {
      this.timeSeparatorChar = timeSeparatorChar;
      this.timeSeparatorCharLen = (timeSeparatorChar != '\000') ? 1 : 0;
      this.useMinutes = minutes;
      this.length = length;
    }
    
    public int getLength() {
      return this.length;
    }
    
    private int write(int offset, char[] buffer, int pos) {
      buffer[pos++] = (offset < 0) ? '-' : '+';
      int absOffset = Math.abs(offset);
      int hours = absOffset / 3600000;
      int ms = absOffset - 3600000 * hours;
      int temp = hours / 10;
      buffer[pos++] = (char)(temp + 48);
      buffer[pos++] = (char)(hours - 10 * temp + 48);
      if (this.useMinutes) {
        buffer[pos] = this.timeSeparatorChar;
        pos += this.timeSeparatorCharLen;
        int minutes = ms / 60000;
        ms -= 60000 * minutes;
        temp = minutes / 10;
        buffer[pos++] = (char)(temp + 48);
        buffer[pos++] = (char)(minutes - 10 * temp + 48);
      } 
      return pos;
    }
  }
  
  private final int[] dstOffsets = new int[25];
  
  private char[] cachedDate;
  
  private int dateLength;
  
  FixedDateFormat(FixedFormat fixedFormat, TimeZone tz) {
    this(fixedFormat, tz, fixedFormat.getSecondFractionDigits());
  }
  
  FixedDateFormat(FixedFormat fixedFormat, TimeZone tz, int secondFractionDigits) {
    this.fixedFormat = Objects.<FixedFormat>requireNonNull(fixedFormat);
    this.timeZone = Objects.<TimeZone>requireNonNull(tz);
    this.timeSeparatorChar = fixedFormat.timeSeparatorChar;
    this.timeSeparatorLength = fixedFormat.timeSeparatorLength;
    this.millisSeparatorChar = fixedFormat.millisSeparatorChar;
    this.millisSeparatorLength = fixedFormat.millisSeparatorLength;
    this.fixedTimeZoneFormat = fixedFormat.fixedTimeZoneFormat;
    this.length = fixedFormat.getLength();
    this.secondFractionDigits = Math.max(1, Math.min(9, secondFractionDigits));
    this.fastDateFormat = fixedFormat.getFastDateFormat(tz);
  }
  
  public static FixedDateFormat createIfSupported(String... options) {
    TimeZone tz;
    if (options == null || options.length == 0 || options[0] == null)
      return new FixedDateFormat(FixedFormat.DEFAULT, TimeZone.getDefault()); 
    if (options.length > 1) {
      if (options[1] != null) {
        tz = TimeZone.getTimeZone(options[1]);
      } else {
        tz = TimeZone.getDefault();
      } 
    } else {
      tz = TimeZone.getDefault();
    } 
    String option0 = options[0];
    FixedFormat withoutNanos = FixedFormat.lookupIgnoringNanos(option0);
    if (withoutNanos != null) {
      int[] nanoRange = FixedFormat.nanoRange(option0);
      int nanoStart = nanoRange[0];
      int nanoEnd = nanoRange[1];
      int secondFractionDigits = nanoEnd - nanoStart;
      return new FixedDateFormat(withoutNanos, tz, secondFractionDigits);
    } 
    FixedFormat type = FixedFormat.lookup(option0);
    return (type == null) ? null : new FixedDateFormat(type, tz);
  }
  
  public static FixedDateFormat create(FixedFormat format) {
    return new FixedDateFormat(format, TimeZone.getDefault());
  }
  
  public static FixedDateFormat create(FixedFormat format, TimeZone tz) {
    return new FixedDateFormat(format, (tz != null) ? tz : TimeZone.getDefault());
  }
  
  public String getFormat() {
    return this.fixedFormat.getPattern();
  }
  
  public TimeZone getTimeZone() {
    return this.timeZone;
  }
  
  public long millisSinceMidnight(long currentTime) {
    if (currentTime >= this.midnightTomorrow || currentTime < this.midnightToday)
      updateMidnightMillis(currentTime); 
    return currentTime - this.midnightToday;
  }
  
  private void updateMidnightMillis(long now) {
    if (now >= this.midnightTomorrow || now < this.midnightToday)
      synchronized (this) {
        updateCachedDate(now);
        this.midnightToday = calcMidnightMillis(now, 0);
        this.midnightTomorrow = calcMidnightMillis(now, 1);
        updateDaylightSavingTime();
      }  
  }
  
  private long calcMidnightMillis(long time, int addDays) {
    Calendar cal = Calendar.getInstance(this.timeZone);
    cal.setTimeInMillis(time);
    cal.set(11, 0);
    cal.set(12, 0);
    cal.set(13, 0);
    cal.set(14, 0);
    cal.add(5, addDays);
    return cal.getTimeInMillis();
  }
  
  private void updateDaylightSavingTime() {
    Arrays.fill(this.dstOffsets, 0);
    int ONE_HOUR = (int)TimeUnit.HOURS.toMillis(1L);
    if (this.timeZone.getOffset(this.midnightToday) != this.timeZone.getOffset(this.midnightToday + (23 * ONE_HOUR))) {
      int i;
      for (i = 0; i < this.dstOffsets.length; i++) {
        long time = this.midnightToday + (i * ONE_HOUR);
        this.dstOffsets[i] = this.timeZone.getOffset(time) - this.timeZone.getRawOffset();
      } 
      if (this.dstOffsets[0] > this.dstOffsets[23])
        for (i = this.dstOffsets.length - 1; i >= 0; i--)
          this.dstOffsets[i] = this.dstOffsets[i] - this.dstOffsets[0];  
    } 
  }
  
  private void updateCachedDate(long now) {
    if (this.fastDateFormat != null) {
      StringBuilder result = this.fastDateFormat.<StringBuilder>format(now, new StringBuilder());
      this.cachedDate = result.toString().toCharArray();
      this.dateLength = result.length();
    } 
  }
  
  public String formatInstant(Instant instant) {
    char[] result = new char[this.length << 1];
    int written = formatInstant(instant, result, 0);
    return new String(result, 0, written);
  }
  
  public int formatInstant(Instant instant, char[] buffer, int startPos) {
    long epochMillisecond = instant.getEpochMillisecond();
    int result = format(epochMillisecond, buffer, startPos);
    result -= digitsLessThanThree();
    int pos = formatNanoOfMillisecond(instant.getNanoOfMillisecond(), buffer, startPos + result);
    return writeTimeZone(epochMillisecond, buffer, pos);
  }
  
  private int digitsLessThanThree() {
    return Math.max(0, FixedFormat.MILLI_FRACTION_DIGITS - this.secondFractionDigits);
  }
  
  public String format(long epochMillis) {
    char[] result = new char[this.length << 1];
    int written = format(epochMillis, result, 0);
    return new String(result, 0, written);
  }
  
  public int format(long epochMillis, char[] buffer, int startPos) {
    int ms = (int)millisSinceMidnight(epochMillis);
    writeDate(buffer, startPos);
    int pos = writeTime(ms, buffer, startPos + this.dateLength);
    return pos - startPos;
  }
  
  private void writeDate(char[] buffer, int startPos) {
    if (this.cachedDate != null)
      System.arraycopy(this.cachedDate, 0, buffer, startPos, this.dateLength); 
  }
  
  private int writeTime(int ms, char[] buffer, int pos) {
    int hourOfDay = ms / 3600000;
    int hours = hourOfDay + daylightSavingTime(hourOfDay) / 3600000;
    ms -= 3600000 * hourOfDay;
    int minutes = ms / 60000;
    ms -= 60000 * minutes;
    int seconds = ms / 1000;
    ms -= 1000 * seconds;
    int temp = hours / 10;
    buffer[pos++] = (char)(temp + 48);
    buffer[pos++] = (char)(hours - 10 * temp + 48);
    buffer[pos] = this.timeSeparatorChar;
    pos += this.timeSeparatorLength;
    temp = minutes / 10;
    buffer[pos++] = (char)(temp + 48);
    buffer[pos++] = (char)(minutes - 10 * temp + 48);
    buffer[pos] = this.timeSeparatorChar;
    pos += this.timeSeparatorLength;
    temp = seconds / 10;
    buffer[pos++] = (char)(temp + 48);
    buffer[pos++] = (char)(seconds - 10 * temp + 48);
    buffer[pos] = this.millisSeparatorChar;
    pos += this.millisSeparatorLength;
    temp = ms / 100;
    buffer[pos++] = (char)(temp + 48);
    ms -= 100 * temp;
    temp = ms / 10;
    buffer[pos++] = (char)(temp + 48);
    ms -= 10 * temp;
    buffer[pos++] = (char)(ms + 48);
    return pos;
  }
  
  private int writeTimeZone(long epochMillis, char[] buffer, int pos) {
    if (this.fixedTimeZoneFormat != null)
      pos = this.fixedTimeZoneFormat.write(this.timeZone.getOffset(epochMillis), buffer, pos); 
    return pos;
  }
  
  static int[] TABLE = new int[] { 100000, 10000, 1000, 100, 10, 1 };
  
  private int formatNanoOfMillisecond(int nanoOfMillisecond, char[] buffer, int pos) {
    int remain = nanoOfMillisecond;
    for (int i = 0; i < this.secondFractionDigits - FixedFormat.MILLI_FRACTION_DIGITS; i++) {
      int divisor = TABLE[i];
      int temp = remain / divisor;
      buffer[pos++] = (char)(temp + 48);
      remain -= divisor * temp;
    } 
    return pos;
  }
  
  private int daylightSavingTime(int hourOfDay) {
    return (hourOfDay > 23) ? this.dstOffsets[23] : this.dstOffsets[hourOfDay];
  }
  
  public boolean isEquivalent(long oldEpochSecond, int oldNanoOfSecond, long epochSecond, int nanoOfSecond) {
    if (oldEpochSecond == epochSecond) {
      if (this.secondFractionDigits <= 3)
        return (oldNanoOfSecond / 1000000L == nanoOfSecond / 1000000L); 
      return (oldNanoOfSecond == nanoOfSecond);
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\datetime\FixedDateFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */