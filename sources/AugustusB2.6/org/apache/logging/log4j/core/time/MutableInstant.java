// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.time;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.ValueRange;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.util.PerformanceSensitive;
import java.time.temporal.TemporalAccessor;
import java.io.Serializable;

@PerformanceSensitive({ "allocation" })
public class MutableInstant implements Instant, Serializable, TemporalAccessor
{
    private static final int MILLIS_PER_SECOND = 1000;
    private static final int NANOS_PER_MILLI = 1000000;
    private static final int NANOS_PER_SECOND = 1000000000;
    private long epochSecond;
    private int nanoOfSecond;
    
    @Override
    public long getEpochSecond() {
        return this.epochSecond;
    }
    
    @Override
    public int getNanoOfSecond() {
        return this.nanoOfSecond;
    }
    
    @Override
    public long getEpochMillisecond() {
        final int millis = this.nanoOfSecond / 1000000;
        final long epochMillisecond = this.epochSecond * 1000L + millis;
        return epochMillisecond;
    }
    
    @Override
    public int getNanoOfMillisecond() {
        final int millis = this.nanoOfSecond / 1000000;
        final int nanoOfMillisecond = this.nanoOfSecond - millis * 1000000;
        return nanoOfMillisecond;
    }
    
    public void initFrom(final Instant other) {
        this.epochSecond = other.getEpochSecond();
        this.nanoOfSecond = other.getNanoOfSecond();
    }
    
    public void initFromEpochMilli(final long epochMilli, final int nanoOfMillisecond) {
        this.validateNanoOfMillisecond(nanoOfMillisecond);
        this.epochSecond = epochMilli / 1000L;
        this.nanoOfSecond = (int)(epochMilli - this.epochSecond * 1000L) * 1000000 + nanoOfMillisecond;
    }
    
    private void validateNanoOfMillisecond(final int nanoOfMillisecond) {
        if (nanoOfMillisecond < 0 || nanoOfMillisecond >= 1000000) {
            throw new IllegalArgumentException("Invalid nanoOfMillisecond " + nanoOfMillisecond);
        }
    }
    
    public void initFrom(final Clock clock) {
        if (clock instanceof PreciseClock) {
            ((PreciseClock)clock).init(this);
        }
        else {
            this.initFromEpochMilli(clock.currentTimeMillis(), 0);
        }
    }
    
    public void initFromEpochSecond(final long epochSecond, final int nano) {
        this.validateNanoOfSecond(nano);
        this.epochSecond = epochSecond;
        this.nanoOfSecond = nano;
    }
    
    private void validateNanoOfSecond(final int nano) {
        if (nano < 0 || nano >= 1000000000) {
            throw new IllegalArgumentException("Invalid nanoOfSecond " + nano);
        }
    }
    
    public static void instantToMillisAndNanos(final long epochSecond, final int nano, final long[] result) {
        final int millis = nano / 1000000;
        result[0] = epochSecond * 1000L + millis;
        result[1] = nano - millis * 1000000;
    }
    
    @Override
    public boolean isSupported(final TemporalField field) {
        if (field instanceof ChronoField) {
            return field == ChronoField.INSTANT_SECONDS || field == ChronoField.NANO_OF_SECOND || field == ChronoField.MICRO_OF_SECOND || field == ChronoField.MILLI_OF_SECOND;
        }
        return field != null && field.isSupportedBy(this);
    }
    
    @Override
    public long getLong(final TemporalField field) {
        if (!(field instanceof ChronoField)) {
            return field.getFrom(this);
        }
        switch ((ChronoField)field) {
            case NANO_OF_SECOND: {
                return this.nanoOfSecond;
            }
            case MICRO_OF_SECOND: {
                return this.nanoOfSecond / 1000;
            }
            case MILLI_OF_SECOND: {
                return this.nanoOfSecond / 1000000;
            }
            case INSTANT_SECONDS: {
                return this.epochSecond;
            }
            default: {
                throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
            }
        }
    }
    
    @Override
    public ValueRange range(final TemporalField field) {
        return super.range(field);
    }
    
    @Override
    public int get(final TemporalField field) {
        if (field instanceof ChronoField) {
            switch ((ChronoField)field) {
                case NANO_OF_SECOND: {
                    return this.nanoOfSecond;
                }
                case MICRO_OF_SECOND: {
                    return this.nanoOfSecond / 1000;
                }
                case MILLI_OF_SECOND: {
                    return this.nanoOfSecond / 1000000;
                }
                case INSTANT_SECONDS: {
                    ChronoField.INSTANT_SECONDS.checkValidIntValue(this.epochSecond);
                    break;
                }
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
        return this.range(field).checkValidIntValue(field.getFrom(this), field);
    }
    
    @Override
    public <R> R query(final TemporalQuery<R> query) {
        if (query == TemporalQueries.precision()) {
            return (R)ChronoUnit.NANOS;
        }
        if (query == TemporalQueries.chronology() || query == TemporalQueries.zoneId() || query == TemporalQueries.zone() || query == TemporalQueries.offset() || query == TemporalQueries.localDate() || query == TemporalQueries.localTime()) {
            return null;
        }
        return query.queryFrom(this);
    }
    
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof MutableInstant)) {
            return false;
        }
        final MutableInstant other = (MutableInstant)object;
        return this.epochSecond == other.epochSecond && this.nanoOfSecond == other.nanoOfSecond;
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (int)(this.epochSecond ^ this.epochSecond >>> 32);
        result = 31 * result + this.nanoOfSecond;
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(64);
        this.formatTo(sb);
        return sb.toString();
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        buffer.append("MutableInstant[epochSecond=").append(this.epochSecond).append(", nano=").append(this.nanoOfSecond).append("]");
    }
}
