/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util.datetime;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.time.Instant;
import org.apache.logging.log4j.core.util.datetime.FastDateFormat;

public class FixedDateFormat {
    private static final char NONE = '\u0000';
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
    private final int[] dstOffsets = new int[25];
    private char[] cachedDate;
    private int dateLength;
    static int[] TABLE = new int[]{100000, 10000, 1000, 100, 10, 1};

    FixedDateFormat(FixedFormat fixedFormat, TimeZone tz) {
        this(fixedFormat, tz, fixedFormat.getSecondFractionDigits());
    }

    FixedDateFormat(FixedFormat fixedFormat, TimeZone tz, int secondFractionDigits) {
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

    public static FixedDateFormat createIfSupported(String ... options) {
        if (options == null || options.length == 0 || options[0] == null) {
            return new FixedDateFormat(FixedFormat.DEFAULT, TimeZone.getDefault());
        }
        TimeZone tz = options.length > 1 ? (options[1] != null ? TimeZone.getTimeZone(options[1]) : TimeZone.getDefault()) : TimeZone.getDefault();
        String option0 = options[0];
        FixedFormat withNanos = FixedFormat.lookupIgnoringNanos(option0);
        if (withNanos != null) {
            int[] nanoRange = FixedFormat.nanoRange(option0);
            int nanoStart = nanoRange[0];
            int nanoEnd = nanoRange[1];
            int secondFractionDigits = nanoEnd - nanoStart;
            return new FixedDateFormat(withNanos, tz, secondFractionDigits);
        }
        FixedFormat type = FixedFormat.lookup(option0);
        return type == null ? null : new FixedDateFormat(type, tz);
    }

    public static FixedDateFormat create(FixedFormat format) {
        return new FixedDateFormat(format, TimeZone.getDefault());
    }

    public static FixedDateFormat create(FixedFormat format, TimeZone tz) {
        return new FixedDateFormat(format, tz != null ? tz : TimeZone.getDefault());
    }

    public String getFormat() {
        return this.fixedFormat.getPattern();
    }

    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public long millisSinceMidnight(long currentTime) {
        if (currentTime >= this.midnightTomorrow || currentTime < this.midnightToday) {
            this.updateMidnightMillis(currentTime);
        }
        return currentTime - this.midnightToday;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void updateMidnightMillis(long now) {
        if (now >= this.midnightTomorrow || now < this.midnightToday) {
            FixedDateFormat fixedDateFormat = this;
            synchronized (fixedDateFormat) {
                this.updateCachedDate(now);
                this.midnightToday = this.calcMidnightMillis(now, 0);
                this.midnightTomorrow = this.calcMidnightMillis(now, 1);
                this.updateDaylightSavingTime();
            }
        }
    }

    private long calcMidnightMillis(long time, int addDays) {
        Calendar cal = Calendar.getInstance(this.timeZone);
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
        int ONE_HOUR = (int)TimeUnit.HOURS.toMillis(1L);
        if (this.timeZone.getOffset(this.midnightToday) != this.timeZone.getOffset(this.midnightToday + (long)(23 * ONE_HOUR))) {
            int i;
            for (i = 0; i < this.dstOffsets.length; ++i) {
                long time = this.midnightToday + (long)(i * ONE_HOUR);
                this.dstOffsets[i] = this.timeZone.getOffset(time) - this.timeZone.getRawOffset();
            }
            if (this.dstOffsets[0] > this.dstOffsets[23]) {
                i = this.dstOffsets.length - 1;
                while (i >= 0) {
                    int n = i--;
                    this.dstOffsets[n] = this.dstOffsets[n] - this.dstOffsets[0];
                }
            }
        }
    }

    private void updateCachedDate(long now) {
        if (this.fastDateFormat != null) {
            StringBuilder result = this.fastDateFormat.format(now, new StringBuilder());
            this.cachedDate = result.toString().toCharArray();
            this.dateLength = result.length();
        }
    }

    public String formatInstant(Instant instant) {
        char[] result = new char[this.length << 1];
        int written = this.formatInstant(instant, result, 0);
        return new String(result, 0, written);
    }

    public int formatInstant(Instant instant, char[] buffer, int startPos) {
        long epochMillisecond = instant.getEpochMillisecond();
        int result = this.format(epochMillisecond, buffer, startPos);
        int pos = this.formatNanoOfMillisecond(instant.getNanoOfMillisecond(), buffer, startPos + (result -= this.digitsLessThanThree()));
        return this.writeTimeZone(epochMillisecond, buffer, pos);
    }

    private int digitsLessThanThree() {
        return Math.max(0, FixedFormat.MILLI_FRACTION_DIGITS - this.secondFractionDigits);
    }

    public String format(long epochMillis) {
        char[] result = new char[this.length << 1];
        int written = this.format(epochMillis, result, 0);
        return new String(result, 0, written);
    }

    public int format(long epochMillis, char[] buffer, int startPos) {
        int ms = (int)this.millisSinceMidnight(epochMillis);
        this.writeDate(buffer, startPos);
        int pos = this.writeTime(ms, buffer, startPos + this.dateLength);
        return pos - startPos;
    }

    private void writeDate(char[] buffer, int startPos) {
        if (this.cachedDate != null) {
            System.arraycopy(this.cachedDate, 0, buffer, startPos, this.dateLength);
        }
    }

    private int writeTime(int ms, char[] buffer, int pos) {
        int hourOfDay = ms / 3600000;
        int hours = hourOfDay + this.daylightSavingTime(hourOfDay) / 3600000;
        int minutes = (ms -= 3600000 * hourOfDay) / 60000;
        int seconds = (ms -= 60000 * minutes) / 1000;
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
        temp = (ms -= 1000 * seconds) / 100;
        buffer[pos++] = (char)(temp + 48);
        ms -= 100 * temp;
        temp = ms / 10;
        buffer[pos++] = (char)(temp + 48);
        buffer[pos++] = (char)((ms -= 10 * temp) + 48);
        return pos;
    }

    private int writeTimeZone(long epochMillis, char[] buffer, int pos) {
        if (this.fixedTimeZoneFormat != null) {
            pos = this.fixedTimeZoneFormat.write(this.timeZone.getOffset(epochMillis), buffer, pos);
        }
        return pos;
    }

    private int formatNanoOfMillisecond(int nanoOfMillisecond, char[] buffer, int pos) {
        int remain = nanoOfMillisecond;
        for (int i = 0; i < this.secondFractionDigits - FixedFormat.MILLI_FRACTION_DIGITS; ++i) {
            int divisor = TABLE[i];
            int temp = remain / divisor;
            buffer[pos++] = (char)(temp + 48);
            remain -= divisor * temp;
        }
        return pos;
    }

    private int daylightSavingTime(int hourOfDay) {
        return hourOfDay > 23 ? this.dstOffsets[23] : this.dstOffsets[hourOfDay];
    }

    public static enum FixedTimeZoneFormat {
        HH('\u0000', false, 3),
        HHMM('\u0000', true, 5),
        HHCMM(':', true, 6);

        private final char timeSeparatorChar;
        private final int timeSeparatorCharLen;
        private final boolean useMinutes;
        private final int length;

        private FixedTimeZoneFormat() {
            this('\u0000', true, 4);
        }

        private FixedTimeZoneFormat(char timeSeparatorChar, boolean minutes, int length) {
            this.timeSeparatorChar = timeSeparatorChar;
            this.timeSeparatorCharLen = timeSeparatorChar != '\u0000' ? 1 : 0;
            this.useMinutes = minutes;
            this.length = length;
        }

        public int getLength() {
            return this.length;
        }

        private int write(int offset, char[] buffer, int pos) {
            buffer[pos++] = offset < 0 ? 45 : 43;
            int absOffset = Math.abs(offset);
            int hours = absOffset / 3600000;
            int ms = absOffset - 3600000 * hours;
            int temp = hours / 10;
            buffer[pos++] = (char)(temp + 48);
            buffer[pos++] = (char)(hours - 10 * temp + 48);
            if (this.useMinutes) {
                buffer[pos] = this.timeSeparatorChar;
                pos += this.timeSeparatorCharLen;
                int minutes = ms / 60000;
                ms -= 60000 * minutes;
                temp = minutes / 10;
                buffer[pos++] = (char)(temp + 48);
                buffer[pos++] = (char)(minutes - 10 * temp + 48);
            }
            return pos;
        }
    }

    public static enum FixedFormat {
        ABSOLUTE("HH:mm:ss,SSS", null, 0, ':', 1, ',', 1, 3, null),
        ABSOLUTE_MICROS("HH:mm:ss,nnnnnn", null, 0, ':', 1, ',', 1, 6, null),
        ABSOLUTE_NANOS("HH:mm:ss,nnnnnnnnn", null, 0, ':', 1, ',', 1, 9, null),
        ABSOLUTE_PERIOD("HH:mm:ss.SSS", null, 0, ':', 1, '.', 1, 3, null),
        COMPACT("yyyyMMddHHmmssSSS", "yyyyMMdd", 0, ' ', 0, ' ', 0, 3, null),
        DATE("dd MMM yyyy HH:mm:ss,SSS", "dd MMM yyyy ", 0, ':', 1, ',', 1, 3, null),
        DATE_PERIOD("dd MMM yyyy HH:mm:ss.SSS", "dd MMM yyyy ", 0, ':', 1, '.', 1, 3, null),
        DEFAULT("yyyy-MM-dd HH:mm:ss,SSS", "yyyy-MM-dd ", 0, ':', 1, ',', 1, 3, null),
        DEFAULT_MICROS("yyyy-MM-dd HH:mm:ss,nnnnnn", "yyyy-MM-dd ", 0, ':', 1, ',', 1, 6, null),
        DEFAULT_NANOS("yyyy-MM-dd HH:mm:ss,nnnnnnnnn", "yyyy-MM-dd ", 0, ':', 1, ',', 1, 9, null),
        DEFAULT_PERIOD("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd ", 0, ':', 1, '.', 1, 3, null),
        ISO8601_BASIC("yyyyMMdd'T'HHmmss,SSS", "yyyyMMdd'T'", 2, ' ', 0, ',', 1, 3, null),
        ISO8601_BASIC_PERIOD("yyyyMMdd'T'HHmmss.SSS", "yyyyMMdd'T'", 2, ' ', 0, '.', 1, 3, null),
        ISO8601("yyyy-MM-dd'T'HH:mm:ss,SSS", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, null),
        ISO8601_OFFSET_DATE_TIME_HH("yyyy-MM-dd'T'HH:mm:ss,SSSX", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, FixedTimeZoneFormat.HH),
        ISO8601_OFFSET_DATE_TIME_HHMM("yyyy-MM-dd'T'HH:mm:ss,SSSXX", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, FixedTimeZoneFormat.HHMM),
        ISO8601_OFFSET_DATE_TIME_HHCMM("yyyy-MM-dd'T'HH:mm:ss,SSSXXX", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, FixedTimeZoneFormat.HHCMM),
        ISO8601_PERIOD("yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'", 2, ':', 1, '.', 1, 3, null),
        ISO8601_PERIOD_MICROS("yyyy-MM-dd'T'HH:mm:ss.nnnnnn", "yyyy-MM-dd'T'", 2, ':', 1, '.', 1, 6, null);

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

        private FixedFormat(String pattern, String datePattern, int escapeCount, char timeSeparator, int timeSepLength, char millisSeparator, int millisSepLength, int secondFractionDigits, FixedTimeZoneFormat timeZoneFormat) {
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

        public static FixedFormat lookup(String nameOrPattern) {
            for (FixedFormat type : FixedFormat.values()) {
                if (!type.name().equals(nameOrPattern) && !type.getPattern().equals(nameOrPattern)) continue;
                return type;
            }
            return null;
        }

        static FixedFormat lookupIgnoringNanos(String pattern) {
            int[] nanoRange = FixedFormat.nanoRange(pattern);
            int nanoStart = nanoRange[0];
            int nanoEnd = nanoRange[1];
            if (nanoStart > 0) {
                String subPattern = pattern.substring(0, nanoStart) + DEFAULT_SECOND_FRACTION_PATTERN + pattern.substring(nanoEnd, pattern.length());
                for (FixedFormat type : FixedFormat.values()) {
                    if (!type.getPattern().equals(subPattern)) continue;
                    return type;
                }
            }
            return null;
        }

        private static int[] nanoRange(String pattern) {
            int indexStart = pattern.indexOf(110);
            int indexEnd = -1;
            if (indexStart >= 0) {
                indexEnd = pattern.indexOf(90, indexStart);
                indexEnd = indexEnd < 0 ? pattern.indexOf(88, indexStart) : indexEnd;
                indexEnd = indexEnd < 0 ? pattern.length() : indexEnd;
                for (int i = indexStart + 1; i < indexEnd; ++i) {
                    if (pattern.charAt(i) == 'n') continue;
                    return EMPTY_RANGE;
                }
            }
            return new int[]{indexStart, indexEnd};
        }

        public int getLength() {
            return this.pattern.length() - this.escapeCount;
        }

        public int getDatePatternLength() {
            return this.getDatePattern() == null ? 0 : this.getDatePattern().length() - this.escapeCount;
        }

        public FastDateFormat getFastDateFormat() {
            return this.getFastDateFormat(null);
        }

        public FastDateFormat getFastDateFormat(TimeZone tz) {
            return this.getDatePattern() == null ? null : FastDateFormat.getInstance(this.getDatePattern(), tz);
        }

        public int getSecondFractionDigits() {
            return this.secondFractionDigits;
        }

        public FixedTimeZoneFormat getFixedTimeZoneFormat() {
            return this.fixedTimeZoneFormat;
        }

        static {
            MILLI_FRACTION_DIGITS = DEFAULT_SECOND_FRACTION_PATTERN.length();
            EMPTY_RANGE = new int[]{-1, -1};
        }
    }
}

