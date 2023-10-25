package org.apache.logging.log4j.core.appender.rolling;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.pattern.ArrayPatternConverter;
import org.apache.logging.log4j.core.pattern.DatePatternConverter;
import org.apache.logging.log4j.core.pattern.FormattingInfo;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.status.StatusLogger;

public class PatternProcessor {
  protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final String KEY = "FileConverter";
  
  private static final char YEAR_CHAR = 'y';
  
  private static final char MONTH_CHAR = 'M';
  
  private static final char[] WEEK_CHARS = new char[] { 'w', 'W' };
  
  private static final char[] DAY_CHARS = new char[] { 'D', 'd', 'F', 'E' };
  
  private static final char[] HOUR_CHARS = new char[] { 'H', 'K', 'h', 'k' };
  
  private static final char MINUTE_CHAR = 'm';
  
  private static final char SECOND_CHAR = 's';
  
  private static final char MILLIS_CHAR = 'S';
  
  private final ArrayPatternConverter[] patternConverters;
  
  private final FormattingInfo[] patternFields;
  
  private final FileExtension fileExtension;
  
  private long prevFileTime = 0L;
  
  private long nextFileTime = 0L;
  
  private long currentFileTime = 0L;
  
  private boolean isTimeBased = false;
  
  private RolloverFrequency frequency = null;
  
  private final String pattern;
  
  public String getPattern() {
    return this.pattern;
  }
  
  public String toString() {
    return this.pattern;
  }
  
  public PatternProcessor(String pattern) {
    this.pattern = pattern;
    PatternParser parser = createPatternParser();
    List<PatternConverter> converters = new ArrayList<>();
    List<FormattingInfo> fields = new ArrayList<>();
    parser.parse(pattern, converters, fields, false, false, false);
    FormattingInfo[] infoArray = new FormattingInfo[fields.size()];
    this.patternFields = fields.<FormattingInfo>toArray(infoArray);
    ArrayPatternConverter[] converterArray = new ArrayPatternConverter[converters.size()];
    this.patternConverters = converters.<ArrayPatternConverter>toArray(converterArray);
    this.fileExtension = FileExtension.lookupForFile(pattern);
    for (ArrayPatternConverter converter : this.patternConverters) {
      if (converter instanceof DatePatternConverter) {
        DatePatternConverter dateConverter = (DatePatternConverter)converter;
        this.frequency = calculateFrequency(dateConverter.getPattern());
      } 
    } 
  }
  
  public PatternProcessor(String pattern, PatternProcessor copy) {
    this(pattern);
    this.prevFileTime = copy.prevFileTime;
    this.nextFileTime = copy.nextFileTime;
    this.currentFileTime = copy.currentFileTime;
  }
  
  public void setTimeBased(boolean isTimeBased) {
    this.isTimeBased = isTimeBased;
  }
  
  public long getCurrentFileTime() {
    return this.currentFileTime;
  }
  
  public void setCurrentFileTime(long currentFileTime) {
    this.currentFileTime = currentFileTime;
  }
  
  public long getPrevFileTime() {
    return this.prevFileTime;
  }
  
  public void setPrevFileTime(long prevFileTime) {
    LOGGER.debug("Setting prev file time to {}", new Date(prevFileTime));
    this.prevFileTime = prevFileTime;
  }
  
  public FileExtension getFileExtension() {
    return this.fileExtension;
  }
  
  public long getNextTime(long currentMillis, int increment, boolean modulus) {
    this.prevFileTime = this.nextFileTime;
    if (this.frequency == null)
      throw new IllegalStateException("Pattern does not contain a date"); 
    Calendar currentCal = Calendar.getInstance();
    currentCal.setTimeInMillis(currentMillis);
    Calendar cal = Calendar.getInstance();
    currentCal.setMinimalDaysInFirstWeek(7);
    cal.setMinimalDaysInFirstWeek(7);
    cal.set(currentCal.get(1), 0, 1, 0, 0, 0);
    cal.set(14, 0);
    if (this.frequency == RolloverFrequency.ANNUALLY) {
      increment(cal, 1, increment, modulus);
      long l = cal.getTimeInMillis();
      cal.add(1, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return debugGetNextTime(l);
    } 
    cal.set(2, currentCal.get(2));
    if (this.frequency == RolloverFrequency.MONTHLY) {
      increment(cal, 2, increment, modulus);
      long l = cal.getTimeInMillis();
      cal.add(2, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return debugGetNextTime(l);
    } 
    if (this.frequency == RolloverFrequency.WEEKLY) {
      cal.set(3, currentCal.get(3));
      increment(cal, 3, increment, modulus);
      cal.set(7, currentCal.getFirstDayOfWeek());
      long l = cal.getTimeInMillis();
      cal.add(3, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return debugGetNextTime(l);
    } 
    cal.set(6, currentCal.get(6));
    if (this.frequency == RolloverFrequency.DAILY) {
      increment(cal, 6, increment, modulus);
      long l = cal.getTimeInMillis();
      cal.add(6, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return debugGetNextTime(l);
    } 
    cal.set(11, currentCal.get(11));
    if (this.frequency == RolloverFrequency.HOURLY) {
      increment(cal, 11, increment, modulus);
      long l = cal.getTimeInMillis();
      cal.add(11, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return debugGetNextTime(l);
    } 
    cal.set(12, currentCal.get(12));
    if (this.frequency == RolloverFrequency.EVERY_MINUTE) {
      increment(cal, 12, increment, modulus);
      long l = cal.getTimeInMillis();
      cal.add(12, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return debugGetNextTime(l);
    } 
    cal.set(13, currentCal.get(13));
    if (this.frequency == RolloverFrequency.EVERY_SECOND) {
      increment(cal, 13, increment, modulus);
      long l = cal.getTimeInMillis();
      cal.add(13, -1);
      this.nextFileTime = cal.getTimeInMillis();
      return debugGetNextTime(l);
    } 
    cal.set(14, currentCal.get(14));
    increment(cal, 14, increment, modulus);
    long nextTime = cal.getTimeInMillis();
    cal.add(14, -1);
    this.nextFileTime = cal.getTimeInMillis();
    return debugGetNextTime(nextTime);
  }
  
  public void updateTime() {
    if (this.nextFileTime != 0L || !this.isTimeBased) {
      this.prevFileTime = this.nextFileTime;
      this.currentFileTime = 0L;
    } 
  }
  
  private long debugGetNextTime(long nextTime) {
    if (LOGGER.isTraceEnabled())
      LOGGER.trace("PatternProcessor.getNextTime returning {}, nextFileTime={}, prevFileTime={}, current={}, freq={}", 
          format(nextTime), format(this.nextFileTime), format(this.prevFileTime), format(System.currentTimeMillis()), this.frequency); 
    return nextTime;
  }
  
  private String format(long time) {
    return (new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss.SSS")).format(new Date(time));
  }
  
  private void increment(Calendar cal, int type, int increment, boolean modulate) {
    int interval = modulate ? (increment - cal.get(type) % increment) : increment;
    cal.add(type, interval);
  }
  
  public final void formatFileName(StringBuilder buf, boolean useCurrentTime, Object obj) {
    long time = useCurrentTime ? this.currentFileTime : this.prevFileTime;
    if (time == 0L)
      time = System.currentTimeMillis(); 
    formatFileName(buf, new Object[] { new Date(time), obj });
  }
  
  public final void formatFileName(StrSubstitutor subst, StringBuilder buf, Object obj) {
    formatFileName(subst, buf, false, obj);
  }
  
  public final void formatFileName(StrSubstitutor subst, StringBuilder buf, boolean useCurrentTime, Object obj) {
    LOGGER.debug("Formatting file name. useCurrentTime={}. currentFileTime={}, prevFileTime={}", 
        Boolean.valueOf(useCurrentTime), Long.valueOf(this.currentFileTime), Long.valueOf(this.prevFileTime));
    long time = useCurrentTime ? ((this.currentFileTime != 0L) ? this.currentFileTime : System.currentTimeMillis()) : ((this.prevFileTime != 0L) ? this.prevFileTime : System.currentTimeMillis());
    formatFileName(buf, new Object[] { new Date(time), obj });
    Log4jLogEvent log4jLogEvent = (new Log4jLogEvent.Builder()).setTimeMillis(time).build();
    String fileName = subst.replace((LogEvent)log4jLogEvent, buf);
    buf.setLength(0);
    buf.append(fileName);
  }
  
  protected final void formatFileName(StringBuilder buf, Object... objects) {
    for (int i = 0; i < this.patternConverters.length; i++) {
      int fieldStart = buf.length();
      this.patternConverters[i].format(buf, objects);
      if (this.patternFields[i] != null)
        this.patternFields[i].format(fieldStart, buf); 
    } 
  }
  
  private RolloverFrequency calculateFrequency(String pattern) {
    if (patternContains(pattern, 'S'))
      return RolloverFrequency.EVERY_MILLISECOND; 
    if (patternContains(pattern, 's'))
      return RolloverFrequency.EVERY_SECOND; 
    if (patternContains(pattern, 'm'))
      return RolloverFrequency.EVERY_MINUTE; 
    if (patternContains(pattern, HOUR_CHARS))
      return RolloverFrequency.HOURLY; 
    if (patternContains(pattern, DAY_CHARS))
      return RolloverFrequency.DAILY; 
    if (patternContains(pattern, WEEK_CHARS))
      return RolloverFrequency.WEEKLY; 
    if (patternContains(pattern, 'M'))
      return RolloverFrequency.MONTHLY; 
    if (patternContains(pattern, 'y'))
      return RolloverFrequency.ANNUALLY; 
    return null;
  }
  
  private PatternParser createPatternParser() {
    return new PatternParser(null, "FileConverter", null);
  }
  
  private boolean patternContains(String pattern, char... chars) {
    for (char character : chars) {
      if (patternContains(pattern, character))
        return true; 
    } 
    return false;
  }
  
  private boolean patternContains(String pattern, char character) {
    return (pattern.indexOf(character) >= 0);
  }
  
  public RolloverFrequency getFrequency() {
    return this.frequency;
  }
  
  public long getNextFileTime() {
    return this.nextFileTime;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\PatternProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */