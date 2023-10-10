// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.util.ClockFactory;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LogEvent;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import org.apache.logging.log4j.core.Filter;
import java.time.ZoneId;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "TimeFilter", category = "Core", elementType = "filter", printObject = true)
@PerformanceSensitive({ "allocation" })
public final class TimeFilter extends AbstractFilter
{
    private static final Clock CLOCK;
    private static final DateTimeFormatter FORMATTER;
    private static final long HOUR_MS = 3600000L;
    private static final long DAY_MS = 86400000L;
    private volatile long start;
    private final LocalTime startTime;
    private volatile long end;
    private final LocalTime endTime;
    private final long duration;
    private final ZoneId timeZone;
    
    TimeFilter(final LocalTime start, final LocalTime end, final ZoneId timeZone, final Filter.Result onMatch, final Filter.Result onMismatch, final LocalDate now) {
        super(onMatch, onMismatch);
        this.startTime = start;
        this.endTime = end;
        this.timeZone = timeZone;
        this.start = ZonedDateTime.of(now, this.startTime, timeZone).withEarlierOffsetAtOverlap().toInstant().toEpochMilli();
        long endMillis = ZonedDateTime.of(now, this.endTime, timeZone).withEarlierOffsetAtOverlap().toInstant().toEpochMilli();
        if (end.isBefore(start)) {
            endMillis += 86400000L;
        }
        this.duration = (this.startTime.isBefore(this.endTime) ? Duration.between(this.startTime, this.endTime).toMillis() : Duration.between(this.startTime, this.endTime).plusHours(24L).toMillis());
        final long difference = endMillis - this.start - this.duration;
        if (difference != 0L) {
            endMillis -= difference;
        }
        this.end = endMillis;
    }
    
    private TimeFilter(final LocalTime start, final LocalTime end, final ZoneId timeZone, final Filter.Result onMatch, final Filter.Result onMismatch) {
        this(start, end, timeZone, onMatch, onMismatch, LocalDate.now(timeZone));
    }
    
    private synchronized void adjustTimes(final long currentTimeMillis) {
        if (currentTimeMillis <= this.end) {
            return;
        }
        final LocalDate date = Instant.ofEpochMilli(currentTimeMillis).atZone(this.timeZone).toLocalDate();
        this.start = ZonedDateTime.of(date, this.startTime, this.timeZone).withEarlierOffsetAtOverlap().toInstant().toEpochMilli();
        long endMillis = ZonedDateTime.of(date, this.endTime, this.timeZone).withEarlierOffsetAtOverlap().toInstant().toEpochMilli();
        if (this.endTime.isBefore(this.startTime)) {
            endMillis += 86400000L;
        }
        final long difference = endMillis - this.start - this.duration;
        if (difference != 0L) {
            endMillis -= difference;
        }
        this.end = endMillis;
    }
    
    Filter.Result filter(final long currentTimeMillis) {
        if (currentTimeMillis > this.end) {
            this.adjustTimes(currentTimeMillis);
        }
        return (currentTimeMillis >= this.start && currentTimeMillis <= this.end) ? this.onMatch : this.onMismatch;
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        return this.filter(event.getTimeMillis());
    }
    
    private Filter.Result filter() {
        return this.filter(TimeFilter.CLOCK.currentTimeMillis());
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.filter();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("start=").append(this.start);
        sb.append(", end=").append(this.end);
        sb.append(", timezone=").append(this.timeZone.toString());
        return sb.toString();
    }
    
    @PluginFactory
    public static TimeFilter createFilter(@PluginAttribute("start") final String start, @PluginAttribute("end") final String end, @PluginAttribute("timezone") final String tz, @PluginAttribute("onMatch") final Filter.Result match, @PluginAttribute("onMismatch") final Filter.Result mismatch) {
        final LocalTime startTime = parseTimestamp(start, LocalTime.MIN);
        final LocalTime endTime = parseTimestamp(end, LocalTime.MAX);
        final ZoneId timeZone = (tz == null) ? ZoneId.systemDefault() : ZoneId.of(tz);
        final Filter.Result onMatch = (match == null) ? Filter.Result.NEUTRAL : match;
        final Filter.Result onMismatch = (mismatch == null) ? Filter.Result.DENY : mismatch;
        return new TimeFilter(startTime, endTime, timeZone, onMatch, onMismatch);
    }
    
    private static LocalTime parseTimestamp(final String timestamp, final LocalTime defaultValue) {
        if (timestamp == null) {
            return defaultValue;
        }
        try {
            return LocalTime.parse(timestamp, TimeFilter.FORMATTER);
        }
        catch (Exception e) {
            TimeFilter.LOGGER.warn("Error parsing TimeFilter timestamp value {}", timestamp, e);
            return defaultValue;
        }
    }
    
    static {
        CLOCK = ClockFactory.getClock();
        FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    }
}
