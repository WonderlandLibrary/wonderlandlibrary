package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Duration implements Serializable, Comparable<Duration> {
  private static final long serialVersionUID = -3756810052716342061L;
  
  public static final Duration ZERO = new Duration(0L);
  
  private static final int HOURS_PER_DAY = 24;
  
  private static final int MINUTES_PER_HOUR = 60;
  
  private static final int SECONDS_PER_MINUTE = 60;
  
  private static final int SECONDS_PER_HOUR = 3600;
  
  private static final int SECONDS_PER_DAY = 86400;
  
  private static final Pattern PATTERN = Pattern.compile("P?(?:([0-9]+)D)?(T?(?:([0-9]+)H)?(?:([0-9]+)M)?(?:([0-9]+)?S)?)?", 2);
  
  private final long seconds;
  
  private Duration(long seconds) {
    this.seconds = seconds;
  }
  
  public static Duration parse(CharSequence text) {
    Objects.requireNonNull(text, "text");
    Matcher matcher = PATTERN.matcher(text);
    if (matcher.matches())
      if (!"T".equals(matcher.group(2))) {
        String dayMatch = matcher.group(1);
        String hourMatch = matcher.group(3);
        String minuteMatch = matcher.group(4);
        String secondMatch = matcher.group(5);
        if (dayMatch != null || hourMatch != null || minuteMatch != null || secondMatch != null) {
          long daysAsSecs = parseNumber(text, dayMatch, 86400, "days");
          long hoursAsSecs = parseNumber(text, hourMatch, 3600, "hours");
          long minsAsSecs = parseNumber(text, minuteMatch, 60, "minutes");
          long seconds = parseNumber(text, secondMatch, 1, "seconds");
          try {
            return create(daysAsSecs, hoursAsSecs, minsAsSecs, seconds);
          } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("Text cannot be parsed to a Duration (overflow) " + text, ex);
          } 
        } 
      }  
    throw new IllegalArgumentException("Text cannot be parsed to a Duration: " + text);
  }
  
  private static long parseNumber(CharSequence text, String parsed, int multiplier, String errorText) {
    if (parsed == null)
      return 0L; 
    try {
      long val = Long.parseLong(parsed);
      return val * multiplier;
    } catch (Exception ex) {
      throw new IllegalArgumentException("Text cannot be parsed to a Duration: " + errorText + " (in " + text + ")", ex);
    } 
  }
  
  private static Duration create(long daysAsSecs, long hoursAsSecs, long minsAsSecs, long secs) {
    return create(daysAsSecs + hoursAsSecs + minsAsSecs + secs);
  }
  
  private static Duration create(long seconds) {
    if (seconds == 0L)
      return ZERO; 
    return new Duration(seconds);
  }
  
  public long toMillis() {
    return this.seconds * 1000L;
  }
  
  public boolean equals(Object obj) {
    if (obj == this)
      return true; 
    if (!(obj instanceof Duration))
      return false; 
    Duration other = (Duration)obj;
    return (other.seconds == this.seconds);
  }
  
  public int hashCode() {
    return (int)(this.seconds ^ this.seconds >>> 32L);
  }
  
  public String toString() {
    if (this == ZERO)
      return "PT0S"; 
    long days = this.seconds / 86400L;
    long hours = this.seconds % 86400L / 3600L;
    int minutes = (int)(this.seconds % 3600L / 60L);
    int secs = (int)(this.seconds % 60L);
    StringBuilder buf = new StringBuilder(24);
    buf.append("P");
    if (days != 0L)
      buf.append(days).append('D'); 
    if ((hours | minutes | secs) != 0L)
      buf.append('T'); 
    if (hours != 0L)
      buf.append(hours).append('H'); 
    if (minutes != 0)
      buf.append(minutes).append('M'); 
    if (secs == 0 && buf.length() > 0)
      return buf.toString(); 
    buf.append(secs).append('S');
    return buf.toString();
  }
  
  public int compareTo(Duration other) {
    return Long.signum(toMillis() - other.toMillis());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\Duration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */