/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;

@Plugin(name="TimeFilter", category="Core", elementType="filter", printObject=true)
public final class TimeFilter
extends AbstractFilter {
    private static final long HOUR_MS = 3600000L;
    private static final long MINUTE_MS = 60000L;
    private static final long SECOND_MS = 1000L;
    private final long start;
    private final long end;
    private final TimeZone timezone;

    private TimeFilter(long start, long end, TimeZone tz, Filter.Result onMatch, Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.start = start;
        this.end = end;
        this.timezone = tz;
    }

    @Override
    public Filter.Result filter(LogEvent event) {
        Calendar calendar = Calendar.getInstance(this.timezone);
        calendar.setTimeInMillis(event.getMillis());
        long apparentOffset = (long)calendar.get(11) * 3600000L + (long)calendar.get(12) * 60000L + (long)calendar.get(13) * 1000L + (long)calendar.get(14);
        return apparentOffset >= this.start && apparentOffset < this.end ? this.onMatch : this.onMismatch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("start=").append(this.start);
        sb.append(", end=").append(this.end);
        sb.append(", timezone=").append(this.timezone.toString());
        return sb.toString();
    }

    @PluginFactory
    public static TimeFilter createFilter(@PluginAttribute(value="start") String start, @PluginAttribute(value="end") String end, @PluginAttribute(value="timezone") String tz, @PluginAttribute(value="onMatch") String match, @PluginAttribute(value="onMismatch") String mismatch) {
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        long s = 0L;
        if (start != null) {
            stf.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                s = stf.parse(start).getTime();
            }
            catch (ParseException ex) {
                LOGGER.warn("Error parsing start value " + start, (Throwable)ex);
            }
        }
        long e = Long.MAX_VALUE;
        if (end != null) {
            stf.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                e = stf.parse(end).getTime();
            }
            catch (ParseException ex) {
                LOGGER.warn("Error parsing start value " + end, (Throwable)ex);
            }
        }
        TimeZone timezone = tz == null ? TimeZone.getDefault() : TimeZone.getTimeZone(tz);
        Filter.Result onMatch = Filter.Result.toResult(match, Filter.Result.NEUTRAL);
        Filter.Result onMismatch = Filter.Result.toResult(mismatch, Filter.Result.DENY);
        return new TimeFilter(s, e, timezone, onMatch, onMismatch);
    }
}

