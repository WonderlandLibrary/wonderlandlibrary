package org.apache.logging.log4j.core.time;

import java.io.Serializable;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({"allocation"})
public class MutableInstant implements Instant, Serializable, TemporalAccessor {
  private static final int MILLIS_PER_SECOND = 1000;
  
  private static final int NANOS_PER_MILLI = 1000000;
  
  private static final int NANOS_PER_SECOND = 1000000000;
  
  private long epochSecond;
  
  private int nanoOfSecond;
  
  public long getEpochSecond() {
    return this.epochSecond;
  }
  
  public int getNanoOfSecond() {
    return this.nanoOfSecond;
  }
  
  public long getEpochMillisecond() {
    int millis = this.nanoOfSecond / 1000000;
    long epochMillisecond = this.epochSecond * 1000L + millis;
    return epochMillisecond;
  }
  
  public int getNanoOfMillisecond() {
    int millis = this.nanoOfSecond / 1000000;
    int nanoOfMillisecond = this.nanoOfSecond - millis * 1000000;
    return nanoOfMillisecond;
  }
  
  public void initFrom(Instant other) {
    this.epochSecond = other.getEpochSecond();
    this.nanoOfSecond = other.getNanoOfSecond();
  }
  
  public void initFromEpochMilli(long epochMilli, int nanoOfMillisecond) {
    validateNanoOfMillisecond(nanoOfMillisecond);
    this.epochSecond = epochMilli / 1000L;
    this.nanoOfSecond = (int)(epochMilli - this.epochSecond * 1000L) * 1000000 + nanoOfMillisecond;
  }
  
  private void validateNanoOfMillisecond(int nanoOfMillisecond) {
    if (nanoOfMillisecond < 0 || nanoOfMillisecond >= 1000000)
      throw new IllegalArgumentException("Invalid nanoOfMillisecond " + nanoOfMillisecond); 
  }
  
  public void initFrom(Clock clock) {
    if (clock instanceof PreciseClock) {
      ((PreciseClock)clock).init(this);
    } else {
      initFromEpochMilli(clock.currentTimeMillis(), 0);
    } 
  }
  
  public void initFromEpochSecond(long epochSecond, int nano) {
    validateNanoOfSecond(nano);
    this.epochSecond = epochSecond;
    this.nanoOfSecond = nano;
  }
  
  private void validateNanoOfSecond(int nano) {
    if (nano < 0 || nano >= 1000000000)
      throw new IllegalArgumentException("Invalid nanoOfSecond " + nano); 
  }
  
  public static void instantToMillisAndNanos(long epochSecond, int nano, long[] result) {
    int millis = nano / 1000000;
    result[0] = epochSecond * 1000L + millis;
    result[1] = (nano - millis * 1000000);
  }
  
  public boolean isSupported(TemporalField field) {
    if (field instanceof ChronoField)
      return (field == ChronoField.INSTANT_SECONDS || field == ChronoField.NANO_OF_SECOND || field == ChronoField.MICRO_OF_SECOND || field == ChronoField.MILLI_OF_SECOND); 
    return (field != null && field.isSupportedBy(this));
  }
  
  public long getLong(TemporalField field) {
    if (field instanceof ChronoField) {
      switch ((ChronoField)field) {
        case NANO_OF_SECOND:
          return this.nanoOfSecond;
        case MICRO_OF_SECOND:
          return (this.nanoOfSecond / 1000);
        case MILLI_OF_SECOND:
          return (this.nanoOfSecond / 1000000);
        case INSTANT_SECONDS:
          return this.epochSecond;
      } 
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
    } 
    return field.getFrom(this);
  }
  
  public ValueRange range(TemporalField field) {
    return super.range(field);
  }
  
  public int get(TemporalField field) {
    if (field instanceof ChronoField) {
      switch ((ChronoField)field) {
        case NANO_OF_SECOND:
          return this.nanoOfSecond;
        case MICRO_OF_SECOND:
          return this.nanoOfSecond / 1000;
        case MILLI_OF_SECOND:
          return this.nanoOfSecond / 1000000;
        case INSTANT_SECONDS:
          ChronoField.INSTANT_SECONDS.checkValidIntValue(this.epochSecond);
          break;
      } 
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
    } 
    return range(field).checkValidIntValue(field.getFrom(this), field);
  }
  
  public <R> R query(TemporalQuery<R> query) {
    if (query == TemporalQueries.precision())
      return (R)ChronoUnit.NANOS; 
    if (query == TemporalQueries.chronology() || query == 
      TemporalQueries.zoneId() || query == 
      TemporalQueries.zone() || query == 
      TemporalQueries.offset() || query == 
      TemporalQueries.localDate() || query == 
      TemporalQueries.localTime())
      return null; 
    return query.queryFrom(this);
  }
  
  public boolean equals(Object object) {
    if (object == this)
      return true; 
    if (!(object instanceof MutableInstant))
      return false; 
    MutableInstant other = (MutableInstant)object;
    return (this.epochSecond == other.epochSecond && this.nanoOfSecond == other.nanoOfSecond);
  }
  
  public int hashCode() {
    int result = 17;
    result = 31 * result + (int)(this.epochSecond ^ this.epochSecond >>> 32L);
    result = 31 * result + this.nanoOfSecond;
    return result;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder(64);
    formatTo(sb);
    return sb.toString();
  }
  
  public void formatTo(StringBuilder buffer) {
    buffer.append("MutableInstant[epochSecond=").append(this.epochSecond).append(", nano=").append(this.nanoOfSecond).append("]");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\time\MutableInstant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */