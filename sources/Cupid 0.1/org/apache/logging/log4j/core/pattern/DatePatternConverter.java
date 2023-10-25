package org.apache.logging.log4j.core.pattern;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.core.time.MutableInstant;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.core.util.datetime.FastDateFormat;
import org.apache.logging.log4j.core.util.datetime.FixedDateFormat;
import org.apache.logging.log4j.util.PerformanceSensitive;

@Plugin(name = "DatePatternConverter", category = "Converter")
@ConverterKeys({"d", "date"})
@PerformanceSensitive({"allocation"})
public final class DatePatternConverter extends LogEventPatternConverter implements ArrayPatternConverter {
  private static final String UNIX_FORMAT = "UNIX";
  
  private static final String UNIX_MILLIS_FORMAT = "UNIX_MILLIS";
  
  private final String[] options;
  
  private static abstract class Formatter {
    long previousTime;
    
    int nanos;
    
    private Formatter() {}
    
    public String toPattern() {
      return null;
    }
    
    abstract String format(Instant param1Instant);
    
    abstract void formatToBuffer(Instant param1Instant, StringBuilder param1StringBuilder);
  }
  
  private static final class PatternFormatter extends Formatter {
    private final FastDateFormat fastDateFormat;
    
    private final StringBuilder cachedBuffer = new StringBuilder(64);
    
    PatternFormatter(FastDateFormat fastDateFormat) {
      this.fastDateFormat = fastDateFormat;
    }
    
    String format(Instant instant) {
      return this.fastDateFormat.format(instant.getEpochMillisecond());
    }
    
    void formatToBuffer(Instant instant, StringBuilder destination) {
      long timeMillis = instant.getEpochMillisecond();
      if (this.previousTime != timeMillis) {
        this.cachedBuffer.setLength(0);
        this.fastDateFormat.format(timeMillis, this.cachedBuffer);
      } 
      destination.append(this.cachedBuffer);
    }
    
    public String toPattern() {
      return this.fastDateFormat.getPattern();
    }
  }
  
  private static final class FixedFormatter extends Formatter {
    private final FixedDateFormat fixedDateFormat;
    
    private final char[] cachedBuffer = new char[70];
    
    private int length = 0;
    
    FixedFormatter(FixedDateFormat fixedDateFormat) {
      this.fixedDateFormat = fixedDateFormat;
    }
    
    String format(Instant instant) {
      return this.fixedDateFormat.formatInstant(instant);
    }
    
    void formatToBuffer(Instant instant, StringBuilder destination) {
      long epochSecond = instant.getEpochSecond();
      int nanoOfSecond = instant.getNanoOfSecond();
      if (!this.fixedDateFormat.isEquivalent(this.previousTime, this.nanos, epochSecond, nanoOfSecond)) {
        this.length = this.fixedDateFormat.formatInstant(instant, this.cachedBuffer, 0);
        this.previousTime = epochSecond;
        this.nanos = nanoOfSecond;
      } 
      destination.append(this.cachedBuffer, 0, this.length);
    }
    
    public String toPattern() {
      return this.fixedDateFormat.getFormat();
    }
  }
  
  private static final class UnixFormatter extends Formatter {
    private UnixFormatter() {}
    
    String format(Instant instant) {
      return Long.toString(instant.getEpochSecond());
    }
    
    void formatToBuffer(Instant instant, StringBuilder destination) {
      destination.append(instant.getEpochSecond());
    }
  }
  
  private static final class UnixMillisFormatter extends Formatter {
    private UnixMillisFormatter() {}
    
    String format(Instant instant) {
      return Long.toString(instant.getEpochMillisecond());
    }
    
    void formatToBuffer(Instant instant, StringBuilder destination) {
      destination.append(instant.getEpochMillisecond());
    }
  }
  
  private final class CachedTime {
    public long epochSecond;
    
    public int nanoOfSecond;
    
    public String formatted;
    
    public CachedTime(Instant instant) {
      this.epochSecond = instant.getEpochSecond();
      this.nanoOfSecond = instant.getNanoOfSecond();
      this.formatted = DatePatternConverter.this.formatter.format(instant);
    }
  }
  
  private final ThreadLocal<MutableInstant> threadLocalMutableInstant = new ThreadLocal<>();
  
  private final ThreadLocal<Formatter> threadLocalFormatter = new ThreadLocal<>();
  
  private final AtomicReference<CachedTime> cachedTime;
  
  private final Formatter formatter;
  
  private DatePatternConverter(String[] options) {
    super("Date", "date");
    this.options = (options == null) ? null : Arrays.<String>copyOf(options, options.length);
    this.formatter = createFormatter(options);
    this.cachedTime = new AtomicReference<>(fromEpochMillis(System.currentTimeMillis()));
  }
  
  private CachedTime fromEpochMillis(long epochMillis) {
    MutableInstant temp = new MutableInstant();
    temp.initFromEpochMilli(epochMillis, 0);
    return new CachedTime((Instant)temp);
  }
  
  private Formatter createFormatter(String[] options) {
    FixedDateFormat fixedDateFormat = FixedDateFormat.createIfSupported(options);
    if (fixedDateFormat != null)
      return createFixedFormatter(fixedDateFormat); 
    return createNonFixedFormatter(options);
  }
  
  public static DatePatternConverter newInstance(String[] options) {
    return new DatePatternConverter(options);
  }
  
  private static Formatter createFixedFormatter(FixedDateFormat fixedDateFormat) {
    return new FixedFormatter(fixedDateFormat);
  }
  
  private static Formatter createNonFixedFormatter(String[] options) {
    Objects.requireNonNull(options);
    if (options.length == 0)
      throw new IllegalArgumentException("Options array must have at least one element"); 
    Objects.requireNonNull(options[0]);
    String patternOption = options[0];
    if ("UNIX".equals(patternOption))
      return new UnixFormatter(); 
    if ("UNIX_MILLIS".equals(patternOption))
      return new UnixMillisFormatter(); 
    FixedDateFormat.FixedFormat fixedFormat = FixedDateFormat.FixedFormat.lookup(patternOption);
    String pattern = (fixedFormat == null) ? patternOption : fixedFormat.getPattern();
    TimeZone tz = null;
    if (options.length > 1 && options[1] != null)
      tz = TimeZone.getTimeZone(options[1]); 
    try {
      FastDateFormat tempFormat = FastDateFormat.getInstance(pattern, tz);
      return new PatternFormatter(tempFormat);
    } catch (IllegalArgumentException e) {
      LOGGER.warn("Could not instantiate FastDateFormat with pattern " + pattern, e);
      return createFixedFormatter(FixedDateFormat.create(FixedDateFormat.FixedFormat.DEFAULT, tz));
    } 
  }
  
  public void format(Date date, StringBuilder toAppendTo) {
    format(date.getTime(), toAppendTo);
  }
  
  public void format(LogEvent event, StringBuilder output) {
    format(event.getInstant(), output);
  }
  
  public void format(long epochMilli, StringBuilder output) {
    MutableInstant instant = getMutableInstant();
    instant.initFromEpochMilli(epochMilli, 0);
    format((Instant)instant, output);
  }
  
  private MutableInstant getMutableInstant() {
    if (Constants.ENABLE_THREADLOCALS) {
      MutableInstant result = this.threadLocalMutableInstant.get();
      if (result == null) {
        result = new MutableInstant();
        this.threadLocalMutableInstant.set(result);
      } 
      return result;
    } 
    return new MutableInstant();
  }
  
  public void format(Instant instant, StringBuilder output) {
    if (Constants.ENABLE_THREADLOCALS) {
      formatWithoutAllocation(instant, output);
    } else {
      formatWithoutThreadLocals(instant, output);
    } 
  }
  
  private void formatWithoutAllocation(Instant instant, StringBuilder output) {
    getThreadLocalFormatter().formatToBuffer(instant, output);
  }
  
  private Formatter getThreadLocalFormatter() {
    Formatter result = this.threadLocalFormatter.get();
    if (result == null) {
      result = createFormatter(this.options);
      this.threadLocalFormatter.set(result);
    } 
    return result;
  }
  
  private void formatWithoutThreadLocals(Instant instant, StringBuilder output) {
    CachedTime cached = this.cachedTime.get();
    if (instant.getEpochSecond() != cached.epochSecond || instant.getNanoOfSecond() != cached.nanoOfSecond) {
      CachedTime newTime = new CachedTime(instant);
      if (this.cachedTime.compareAndSet(cached, newTime)) {
        cached = newTime;
      } else {
        cached = this.cachedTime.get();
      } 
    } 
    output.append(cached.formatted);
  }
  
  public void format(Object obj, StringBuilder output) {
    if (obj instanceof Date)
      format((Date)obj, output); 
    super.format(obj, output);
  }
  
  public void format(StringBuilder toAppendTo, Object... objects) {
    for (Object obj : objects) {
      if (obj instanceof Date) {
        format(obj, toAppendTo);
        break;
      } 
    } 
  }
  
  public String getPattern() {
    return this.formatter.toPattern();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\pattern\DatePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */