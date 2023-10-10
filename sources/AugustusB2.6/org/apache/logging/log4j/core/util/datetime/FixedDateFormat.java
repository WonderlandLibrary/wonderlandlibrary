// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util.datetime;

import org.apache.logging.log4j.core.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class FixedDateFormat
{
    private static final char NONE = '\0';
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
    private final int[] dstOffsets;
    private char[] cachedDate;
    private int dateLength;
    static int[] TABLE;
    
    FixedDateFormat(final FixedFormat fixedFormat, final TimeZone tz) {
        this(fixedFormat, tz, fixedFormat.getSecondFractionDigits());
    }
    
    FixedDateFormat(final FixedFormat fixedFormat, final TimeZone tz, final int secondFractionDigits) {
        this.dstOffsets = new int[25];
        this.fixedFormat = Objects.requireNonNull(fixedFormat);
        this.timeZone = Objects.requireNonNull(tz);
        this.timeSeparatorChar = fixedFormat.timeSeparatorChar;
        this.timeSeparatorLength = fixedFormat.timeSeparatorLength;
        this.millisSeparatorChar = fixedFormat.millisSeparatorChar;
        this.millisSeparatorLength = fixedFormat.millisSeparatorLength;
        this.fixedTimeZoneFormat = fixedFormat.fixedTimeZoneFormat;
        this.length = fixedFormat.getLength();
        this.secondFractionDigits = Math.max(1, Math.min(9, secondFractionDigits));
        this.fastDateFormat = fixedFormat.getFastDateFormat(tz);
    }
    
    public static FixedDateFormat createIfSupported(final String... options) {
        if (options == null || options.length == 0 || options[0] == null) {
            return new FixedDateFormat(FixedFormat.DEFAULT, TimeZone.getDefault());
        }
        TimeZone tz;
        if (options.length > 1) {
            if (options[1] != null) {
                tz = TimeZone.getTimeZone(options[1]);
            }
            else {
                tz = TimeZone.getDefault();
            }
        }
        else {
            tz = TimeZone.getDefault();
        }
        final String option0 = options[0];
        final FixedFormat withoutNanos = FixedFormat.lookupIgnoringNanos(option0);
        if (withoutNanos != null) {
            final int[] nanoRange = nanoRange(option0);
            final int nanoStart = nanoRange[0];
            final int nanoEnd = nanoRange[1];
            final int secondFractionDigits = nanoEnd - nanoStart;
            return new FixedDateFormat(withoutNanos, tz, secondFractionDigits);
        }
        final FixedFormat type = FixedFormat.lookup(option0);
        return (type == null) ? null : new FixedDateFormat(type, tz);
    }
    
    public static FixedDateFormat create(final FixedFormat format) {
        return new FixedDateFormat(format, TimeZone.getDefault());
    }
    
    public static FixedDateFormat create(final FixedFormat format, final TimeZone tz) {
        return new FixedDateFormat(format, (tz != null) ? tz : TimeZone.getDefault());
    }
    
    public String getFormat() {
        return this.fixedFormat.getPattern();
    }
    
    public TimeZone getTimeZone() {
        return this.timeZone;
    }
    
    public long millisSinceMidnight(final long currentTime) {
        if (currentTime >= this.midnightTomorrow || currentTime < this.midnightToday) {
            this.updateMidnightMillis(currentTime);
        }
        return currentTime - this.midnightToday;
    }
    
    private void updateMidnightMillis(final long now) {
        if (now >= this.midnightTomorrow || now < this.midnightToday) {
            synchronized (this) {
                this.updateCachedDate(now);
                this.midnightToday = this.calcMidnightMillis(now, 0);
                this.midnightTomorrow = this.calcMidnightMillis(now, 1);
                this.updateDaylightSavingTime();
            }
        }
    }
    
    private long calcMidnightMillis(final long time, final int addDays) {
        final Calendar cal = Calendar.getInstance(this.timeZone);
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
        final int ONE_HOUR = (int)TimeUnit.HOURS.toMillis(1L);
        if (this.timeZone.getOffset(this.midnightToday) != this.timeZone.getOffset(this.midnightToday + 23 * ONE_HOUR)) {
            for (int i = 0; i < this.dstOffsets.length; ++i) {
                final long time = this.midnightToday + i * ONE_HOUR;
                this.dstOffsets[i] = this.timeZone.getOffset(time) - this.timeZone.getRawOffset();
            }
            if (this.dstOffsets[0] > this.dstOffsets[23]) {
                for (int i = this.dstOffsets.length - 1; i >= 0; --i) {
                    final int[] dstOffsets = this.dstOffsets;
                    final int n = i;
                    dstOffsets[n] -= this.dstOffsets[0];
                }
            }
        }
    }
    
    private void updateCachedDate(final long now) {
        if (this.fastDateFormat != null) {
            final StringBuilder result = this.fastDateFormat.format(now, new StringBuilder());
            this.cachedDate = result.toString().toCharArray();
            this.dateLength = result.length();
        }
    }
    
    public String formatInstant(final Instant instant) {
        final char[] result = new char[this.length << 1];
        final int written = this.formatInstant(instant, result, 0);
        return new String(result, 0, written);
    }
    
    public int formatInstant(final Instant instant, final char[] buffer, final int startPos) {
        final long epochMillisecond = instant.getEpochMillisecond();
        int result = this.format(epochMillisecond, buffer, startPos);
        result -= this.digitsLessThanThree();
        final int pos = this.formatNanoOfMillisecond(instant.getNanoOfMillisecond(), buffer, startPos + result);
        return this.writeTimeZone(epochMillisecond, buffer, pos);
    }
    
    private int digitsLessThanThree() {
        return Math.max(0, FixedFormat.MILLI_FRACTION_DIGITS - this.secondFractionDigits);
    }
    
    public String format(final long epochMillis) {
        final char[] result = new char[this.length << 1];
        final int written = this.format(epochMillis, result, 0);
        return new String(result, 0, written);
    }
    
    public int format(final long epochMillis, final char[] buffer, final int startPos) {
        final int ms = (int)this.millisSinceMidnight(epochMillis);
        this.writeDate(buffer, startPos);
        final int pos = this.writeTime(ms, buffer, startPos + this.dateLength);
        return pos - startPos;
    }
    
    private void writeDate(final char[] buffer, final int startPos) {
        if (this.cachedDate != null) {
            System.arraycopy(this.cachedDate, 0, buffer, startPos, this.dateLength);
        }
    }
    
    private int writeTime(int ms, final char[] buffer, int pos) {
        final int hourOfDay = ms / 3600000;
        final int hours = hourOfDay + this.daylightSavingTime(hourOfDay) / 3600000;
        ms -= 3600000 * hourOfDay;
        final int minutes = ms / 60000;
        ms -= 60000 * minutes;
        final int seconds = ms / 1000;
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
    
    private int writeTimeZone(final long epochMillis, final char[] buffer, int pos) {
        if (this.fixedTimeZoneFormat != null) {
            pos = this.fixedTimeZoneFormat.write(this.timeZone.getOffset(epochMillis), buffer, pos);
        }
        return pos;
    }
    
    private int formatNanoOfMillisecond(final int nanoOfMillisecond, final char[] buffer, int pos) {
        int remain = nanoOfMillisecond;
        for (int i = 0; i < this.secondFractionDigits - FixedFormat.MILLI_FRACTION_DIGITS; ++i) {
            final int divisor = FixedDateFormat.TABLE[i];
            final int temp = remain / divisor;
            buffer[pos++] = (char)(temp + 48);
            remain -= divisor * temp;
        }
        return pos;
    }
    
    private int daylightSavingTime(final int hourOfDay) {
        return (hourOfDay > 23) ? this.dstOffsets[23] : this.dstOffsets[hourOfDay];
    }
    
    public boolean isEquivalent(final long oldEpochSecond, final int oldNanoOfSecond, final long epochSecond, final int nanoOfSecond) {
        if (oldEpochSecond != epochSecond) {
            return false;
        }
        if (this.secondFractionDigits <= 3) {
            return oldNanoOfSecond / 1000000L == nanoOfSecond / 1000000L;
        }
        return oldNanoOfSecond == nanoOfSecond;
    }
    
    static {
        FixedDateFormat.TABLE = new int[] { 100000, 10000, 1000, 100, 10, 1 };
    }
    
    public enum FixedFormat
    {
        ABSOLUTE("HH:mm:ss,SSS", (String)null, 0, ':', 1, ',', 1, 3, (FixedTimeZoneFormat)null), 
        ABSOLUTE_MICROS("HH:mm:ss,nnnnnn", (String)null, 0, ':', 1, ',', 1, 6, (FixedTimeZoneFormat)null), 
        ABSOLUTE_NANOS("HH:mm:ss,nnnnnnnnn", (String)null, 0, ':', 1, ',', 1, 9, (FixedTimeZoneFormat)null), 
        ABSOLUTE_PERIOD("HH:mm:ss.SSS", (String)null, 0, ':', 1, '.', 1, 3, (FixedTimeZoneFormat)null), 
        COMPACT("yyyyMMddHHmmssSSS", "yyyyMMdd", 0, ' ', 0, ' ', 0, 3, (FixedTimeZoneFormat)null), 
        DATE("dd MMM yyyy HH:mm:ss,SSS", "dd MMM yyyy ", 0, ':', 1, ',', 1, 3, (FixedTimeZoneFormat)null), 
        DATE_PERIOD("dd MMM yyyy HH:mm:ss.SSS", "dd MMM yyyy ", 0, ':', 1, '.', 1, 3, (FixedTimeZoneFormat)null), 
        DEFAULT("yyyy-MM-dd HH:mm:ss,SSS", "yyyy-MM-dd ", 0, ':', 1, ',', 1, 3, (FixedTimeZoneFormat)null), 
        DEFAULT_MICROS("yyyy-MM-dd HH:mm:ss,nnnnnn", "yyyy-MM-dd ", 0, ':', 1, ',', 1, 6, (FixedTimeZoneFormat)null), 
        DEFAULT_NANOS("yyyy-MM-dd HH:mm:ss,nnnnnnnnn", "yyyy-MM-dd ", 0, ':', 1, ',', 1, 9, (FixedTimeZoneFormat)null), 
        DEFAULT_PERIOD("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd ", 0, ':', 1, '.', 1, 3, (FixedTimeZoneFormat)null), 
        ISO8601_BASIC("yyyyMMdd'T'HHmmss,SSS", "yyyyMMdd'T'", 2, ' ', 0, ',', 1, 3, (FixedTimeZoneFormat)null), 
        ISO8601_BASIC_PERIOD("yyyyMMdd'T'HHmmss.SSS", "yyyyMMdd'T'", 2, ' ', 0, '.', 1, 3, (FixedTimeZoneFormat)null), 
        ISO8601("yyyy-MM-dd'T'HH:mm:ss,SSS", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, (FixedTimeZoneFormat)null), 
        ISO8601_OFFSET_DATE_TIME_HH("yyyy-MM-dd'T'HH:mm:ss,SSSX", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, FixedTimeZoneFormat.HH), 
        ISO8601_OFFSET_DATE_TIME_HHMM("yyyy-MM-dd'T'HH:mm:ss,SSSXX", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, FixedTimeZoneFormat.HHMM), 
        ISO8601_OFFSET_DATE_TIME_HHCMM("yyyy-MM-dd'T'HH:mm:ss,SSSXXX", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, FixedTimeZoneFormat.HHCMM), 
        ISO8601_PERIOD("yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'", 2, ':', 1, '.', 1, 3, (FixedTimeZoneFormat)null), 
        ISO8601_PERIOD_MICROS("yyyy-MM-dd'T'HH:mm:ss.nnnnnn", "yyyy-MM-dd'T'", 2, ':', 1, '.', 1, 6, (FixedTimeZoneFormat)null), 
        US_MONTH_DAY_YEAR2_TIME("dd/MM/yy HH:mm:ss.SSS", "dd/MM/yy ", 0, ':', 1, '.', 1, 3, (FixedTimeZoneFormat)null), 
        US_MONTH_DAY_YEAR4_TIME("dd/MM/yyyy HH:mm:ss.SSS", "dd/MM/yyyy ", 0, ':', 1, '.', 1, 3, (FixedTimeZoneFormat)null);
        
        private static final String DEFAULT_SECOND_FRACTION_PATTERN = "SSS";
        private static final int MILLI_FRACTION_DIGITS;
        private static final char SECOND_FRACTION_PATTERN = 'n';
        private final String pattern;
        private final String datePattern;
        private final int escapeCount;
        private final char timeSeparatorChar;
        private final int timeSeparatorLength;
        private final char millisSeparatorChar;
        private final int millisSeparatorLength;
        private final int secondFractionDigits;
        private final FixedTimeZoneFormat fixedTimeZoneFormat;
        private static final int[] EMPTY_RANGE;
        
        private FixedFormat(final String pattern, final String datePattern, final int escapeCount, final char timeSeparator, final int timeSepLength, final char millisSeparator, final int millisSepLength, final int secondFractionDigits, final FixedTimeZoneFormat timeZoneFormat) {
            this.timeSeparatorChar = timeSeparator;
            this.timeSeparatorLength = timeSepLength;
            this.millisSeparatorChar = millisSeparator;
            this.millisSeparatorLength = millisSepLength;
            this.pattern = Objects.requireNonNull(pattern);
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
        
        public static FixedFormat lookup(final String nameOrPattern) {
            for (final FixedFormat type : values()) {
                if (type.name().equals(nameOrPattern) || type.getPattern().equals(nameOrPattern)) {
                    return type;
                }
            }
            return null;
        }
        
        static FixedFormat lookupIgnoringNanos(final String pattern) {
            final int[] nanoRange = nanoRange(pattern);
            final int nanoStart = nanoRange[0];
            final int nanoEnd = nanoRange[1];
            if (nanoStart > 0) {
                final String subPattern = pattern.substring(0, nanoStart) + "SSS" + pattern.substring(nanoEnd, pattern.length());
                for (final FixedFormat type : values()) {
                    if (type.getPattern().equals(subPattern)) {
                        return type;
                    }
                }
            }
            return null;
        }
        
        private static int[] nanoRange(final String pattern) {
            final int indexStart = pattern.indexOf(110);
            int indexEnd = -1;
            if (indexStart >= 0) {
                indexEnd = pattern.indexOf(90, indexStart);
                indexEnd = ((indexEnd < 0) ? pattern.indexOf(88, indexStart) : indexEnd);
                indexEnd = ((indexEnd < 0) ? pattern.length() : indexEnd);
                for (int i = indexStart + 1; i < indexEnd; ++i) {
                    if (pattern.charAt(i) != 'n') {
                        return FixedFormat.EMPTY_RANGE;
                    }
                }
            }
            return new int[] { indexStart, indexEnd };
        }
        
        public int getLength() {
            return this.pattern.length() - this.escapeCount;
        }
        
        public int getDatePatternLength() {
            return (this.getDatePattern() == null) ? 0 : (this.getDatePattern().length() - this.escapeCount);
        }
        
        public FastDateFormat getFastDateFormat() {
            return this.getFastDateFormat(null);
        }
        
        public FastDateFormat getFastDateFormat(final TimeZone tz) {
            return (this.getDatePattern() == null) ? null : FastDateFormat.getInstance(this.getDatePattern(), tz);
        }
        
        public int getSecondFractionDigits() {
            return this.secondFractionDigits;
        }
        
        public FixedTimeZoneFormat getFixedTimeZoneFormat() {
            return this.fixedTimeZoneFormat;
        }
        
        static {
            MILLI_FRACTION_DIGITS = "SSS".length();
            EMPTY_RANGE = new int[] { -1, -1 };
        }
    }
    
    public enum FixedTimeZoneFormat
    {
        HH('\0', false, 3), 
        HHMM('\0', true, 5), 
        HHCMM(':', true, 6);
        
        private final char timeSeparatorChar;
        private final int timeSeparatorCharLen;
        private final boolean useMinutes;
        private final int length;
        
        private FixedTimeZoneFormat() {
            this('\0', true, 4);
        }
        
        private FixedTimeZoneFormat(final char timeSeparatorChar, final boolean minutes, final int length) {
            this.timeSeparatorChar = timeSeparatorChar;
            this.timeSeparatorCharLen = ((timeSeparatorChar != '\0') ? 1 : 0);
            this.useMinutes = minutes;
            this.length = length;
        }
        
        public int getLength() {
            return this.length;
        }
        
        private int write(final int offset, final char[] buffer, int pos) {
            buffer[pos++] = ((offset < 0) ? '-' : '+');
            final int absOffset = Math.abs(offset);
            final int hours = absOffset / 3600000;
            int ms = absOffset - 3600000 * hours;
            int temp = hours / 10;
            buffer[pos++] = (char)(temp + 48);
            buffer[pos++] = (char)(hours - 10 * temp + 48);
            if (this.useMinutes) {
                buffer[pos] = this.timeSeparatorChar;
                pos += this.timeSeparatorCharLen;
                final int minutes = ms / 60000;
                ms -= 60000 * minutes;
                temp = minutes / 10;
                buffer[pos++] = (char)(temp + 48);
                buffer[pos++] = (char)(minutes - 10 * temp + 48);
            }
            return pos;
        }
    }
}
