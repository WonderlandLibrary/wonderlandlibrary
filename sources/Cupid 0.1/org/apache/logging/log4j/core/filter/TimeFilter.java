package org.apache.logging.log4j.core.filter;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.util.ClockFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "TimeFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({"allocation"})
public final class TimeFilter extends AbstractFilter {
  private static final Clock CLOCK = ClockFactory.getClock();
  
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
  
  private static final long HOUR_MS = 3600000L;
  
  private static final long DAY_MS = 86400000L;
  
  private volatile long start;
  
  private final LocalTime startTime;
  
  private volatile long end;
  
  private final LocalTime endTime;
  
  private final long duration;
  
  private final ZoneId timeZone;
  
  TimeFilter(LocalTime start, LocalTime end, ZoneId timeZone, Filter.Result onMatch, Filter.Result onMismatch, LocalDate now) {
    super(onMatch, onMismatch);
    this.startTime = start;
    this.endTime = end;
    this.timeZone = timeZone;
    this.start = ZonedDateTime.of(now, this.startTime, timeZone).withEarlierOffsetAtOverlap().toInstant().toEpochMilli();
    long endMillis = ZonedDateTime.of(now, this.endTime, timeZone).withEarlierOffsetAtOverlap().toInstant().toEpochMilli();
    if (end.isBefore(start))
      endMillis += 86400000L; 
    this
      .duration = this.startTime.isBefore(this.endTime) ? Duration.between(this.startTime, this.endTime).toMillis() : Duration.between(this.startTime, this.endTime).plusHours(24L).toMillis();
    long difference = endMillis - this.start - this.duration;
    if (difference != 0L)
      endMillis -= difference; 
    this.end = endMillis;
  }
  
  private TimeFilter(LocalTime start, LocalTime end, ZoneId timeZone, Filter.Result onMatch, Filter.Result onMismatch) {
    this(start, end, timeZone, onMatch, onMismatch, LocalDate.now(timeZone));
  }
  
  private synchronized void adjustTimes(long currentTimeMillis) {
    if (currentTimeMillis <= this.end)
      return; 
    LocalDate date = Instant.ofEpochMilli(currentTimeMillis).atZone(this.timeZone).toLocalDate();
    this.start = ZonedDateTime.of(date, this.startTime, this.timeZone).withEarlierOffsetAtOverlap().toInstant().toEpochMilli();
    long endMillis = ZonedDateTime.of(date, this.endTime, this.timeZone).withEarlierOffsetAtOverlap().toInstant().toEpochMilli();
    if (this.endTime.isBefore(this.startTime))
      endMillis += 86400000L; 
    long difference = endMillis - this.start - this.duration;
    if (difference != 0L)
      endMillis -= difference; 
    this.end = endMillis;
  }
  
  Filter.Result filter(long currentTimeMillis) {
    if (currentTimeMillis > this.end)
      adjustTimes(currentTimeMillis); 
    return (currentTimeMillis >= this.start && currentTimeMillis <= this.end) ? this.onMatch : this.onMismatch;
  }
  
  public Filter.Result filter(LogEvent event) {
    return filter(event.getTimeMillis());
  }
  
  private Filter.Result filter() {
    return filter(CLOCK.currentTimeMillis());
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return filter();
  }
  
  public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return filter();
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("start=").append(this.start);
    sb.append(", end=").append(this.end);
    sb.append(", timezone=").append(this.timeZone.toString());
    return sb.toString();
  }
  
  @PluginFactory
  public static TimeFilter createFilter(@PluginAttribute("start") String start, @PluginAttribute("end") String end, @PluginAttribute("timezone") String tz, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
    LocalTime startTime = parseTimestamp(start, LocalTime.MIN);
    LocalTime endTime = parseTimestamp(end, LocalTime.MAX);
    ZoneId timeZone = (tz == null) ? ZoneId.systemDefault() : ZoneId.of(tz);
    Filter.Result onMatch = (match == null) ? Filter.Result.NEUTRAL : match;
    Filter.Result onMismatch = (mismatch == null) ? Filter.Result.DENY : mismatch;
    return new TimeFilter(startTime, endTime, timeZone, onMatch, onMismatch);
  }
  
  private static LocalTime parseTimestamp(String timestamp, LocalTime defaultValue) {
    if (timestamp == null)
      return defaultValue; 
    try {
      return LocalTime.parse(timestamp, FORMATTER);
    } catch (Exception e) {
      LOGGER.warn("Error parsing TimeFilter timestamp value {}", timestamp, e);
      return defaultValue;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\TimeFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */