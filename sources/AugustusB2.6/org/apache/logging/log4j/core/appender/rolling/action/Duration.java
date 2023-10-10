// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import java.util.regex.Matcher;
import java.util.Objects;
import java.util.regex.Pattern;
import java.io.Serializable;

public class Duration implements Serializable, Comparable<Duration>
{
    private static final long serialVersionUID = -3756810052716342061L;
    public static final Duration ZERO;
    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_DAY = 86400;
    private static final Pattern PATTERN;
    private final long seconds;
    
    private Duration(final long seconds) {
        this.seconds = seconds;
    }
    
    public static Duration parse(final CharSequence text) {
        Objects.requireNonNull(text, "text");
        final Matcher matcher = Duration.PATTERN.matcher(text);
        if (matcher.matches() && !"T".equals(matcher.group(2))) {
            final String dayMatch = matcher.group(1);
            final String hourMatch = matcher.group(3);
            final String minuteMatch = matcher.group(4);
            final String secondMatch = matcher.group(5);
            if (dayMatch != null || hourMatch != null || minuteMatch != null || secondMatch != null) {
                final long daysAsSecs = parseNumber(text, dayMatch, 86400, "days");
                final long hoursAsSecs = parseNumber(text, hourMatch, 3600, "hours");
                final long minsAsSecs = parseNumber(text, minuteMatch, 60, "minutes");
                final long seconds = parseNumber(text, secondMatch, 1, "seconds");
                try {
                    return create(daysAsSecs, hoursAsSecs, minsAsSecs, seconds);
                }
                catch (ArithmeticException ex) {
                    throw new IllegalArgumentException("Text cannot be parsed to a Duration (overflow) " + (Object)text, ex);
                }
            }
        }
        throw new IllegalArgumentException("Text cannot be parsed to a Duration: " + (Object)text);
    }
    
    private static long parseNumber(final CharSequence text, final String parsed, final int multiplier, final String errorText) {
        if (parsed == null) {
            return 0L;
        }
        try {
            final long val = Long.parseLong(parsed);
            return val * multiplier;
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Text cannot be parsed to a Duration: " + errorText + " (in " + (Object)text + ")", ex);
        }
    }
    
    private static Duration create(final long daysAsSecs, final long hoursAsSecs, final long minsAsSecs, final long secs) {
        return create(daysAsSecs + hoursAsSecs + minsAsSecs + secs);
    }
    
    private static Duration create(final long seconds) {
        if (seconds == 0L) {
            return Duration.ZERO;
        }
        return new Duration(seconds);
    }
    
    public long toMillis() {
        return this.seconds * 1000L;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Duration)) {
            return false;
        }
        final Duration other = (Duration)obj;
        return other.seconds == this.seconds;
    }
    
    @Override
    public int hashCode() {
        return (int)(this.seconds ^ this.seconds >>> 32);
    }
    
    @Override
    public String toString() {
        if (this == Duration.ZERO) {
            return "PT0S";
        }
        final long days = this.seconds / 86400L;
        final long hours = this.seconds % 86400L / 3600L;
        final int minutes = (int)(this.seconds % 3600L / 60L);
        final int secs = (int)(this.seconds % 60L);
        final StringBuilder buf = new StringBuilder(24);
        buf.append("P");
        if (days != 0L) {
            buf.append(days).append('D');
        }
        if ((hours | (long)minutes | (long)secs) != 0x0L) {
            buf.append('T');
        }
        if (hours != 0L) {
            buf.append(hours).append('H');
        }
        if (minutes != 0) {
            buf.append(minutes).append('M');
        }
        if (secs == 0 && buf.length() > 0) {
            return buf.toString();
        }
        buf.append(secs).append('S');
        return buf.toString();
    }
    
    @Override
    public int compareTo(final Duration other) {
        return Long.signum(this.toMillis() - other.toMillis());
    }
    
    static {
        ZERO = new Duration(0L);
        PATTERN = Pattern.compile("P?(?:([0-9]+)D)?(T?(?:([0-9]+)H)?(?:([0-9]+)M)?(?:([0-9]+)?S)?)?", 2);
    }
}
