/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.pattern;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ArrayPatternConverter;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

@Plugin(name="DatePatternConverter", category="Converter")
@ConverterKeys(value={"d", "date"})
public final class DatePatternConverter
extends LogEventPatternConverter
implements ArrayPatternConverter {
    private static final String ABSOLUTE_FORMAT = "ABSOLUTE";
    private static final String COMPACT_FORMAT = "COMPACT";
    private static final String ABSOLUTE_TIME_PATTERN = "HH:mm:ss,SSS";
    private static final String DATE_AND_TIME_FORMAT = "DATE";
    private static final String DATE_AND_TIME_PATTERN = "dd MMM yyyy HH:mm:ss,SSS";
    private static final String ISO8601_FORMAT = "ISO8601";
    private static final String ISO8601_BASIC_FORMAT = "ISO8601_BASIC";
    private static final String ISO8601_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
    private static final String ISO8601_BASIC_PATTERN = "yyyyMMdd HHmmss,SSS";
    private static final String COMPACT_PATTERN = "yyyyMMddHHmmssSSS";
    private String cachedDate;
    private long lastTimestamp;
    private final SimpleDateFormat simpleFormat;

    private DatePatternConverter(String[] options) {
        super("Date", "date");
        SimpleDateFormat tempFormat;
        String patternOption = options == null || options.length == 0 ? null : options[0];
        String pattern = patternOption == null || patternOption.equalsIgnoreCase(ISO8601_FORMAT) ? ISO8601_PATTERN : (patternOption.equalsIgnoreCase(ISO8601_BASIC_FORMAT) ? ISO8601_BASIC_PATTERN : (patternOption.equalsIgnoreCase(ABSOLUTE_FORMAT) ? ABSOLUTE_TIME_PATTERN : (patternOption.equalsIgnoreCase(DATE_AND_TIME_FORMAT) ? DATE_AND_TIME_PATTERN : (patternOption.equalsIgnoreCase(COMPACT_FORMAT) ? COMPACT_PATTERN : patternOption))));
        try {
            tempFormat = new SimpleDateFormat(pattern);
        }
        catch (IllegalArgumentException e) {
            LOGGER.warn("Could not instantiate SimpleDateFormat with pattern " + patternOption, (Throwable)e);
            tempFormat = new SimpleDateFormat(ISO8601_PATTERN);
        }
        if (options != null && options.length > 1) {
            TimeZone tz = TimeZone.getTimeZone(options[1]);
            tempFormat.setTimeZone(tz);
        }
        this.simpleFormat = tempFormat;
    }

    public static DatePatternConverter newInstance(String[] options) {
        return new DatePatternConverter(options);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void format(LogEvent event, StringBuilder output) {
        long timestamp = event.getMillis();
        DatePatternConverter datePatternConverter = this;
        synchronized (datePatternConverter) {
            if (timestamp != this.lastTimestamp) {
                this.lastTimestamp = timestamp;
                this.cachedDate = this.simpleFormat.format(timestamp);
            }
        }
        output.append(this.cachedDate);
    }

    @Override
    public void format(StringBuilder toAppendTo, Object ... objects) {
        for (Object obj : objects) {
            if (!(obj instanceof Date)) continue;
            this.format(obj, toAppendTo);
            break;
        }
    }

    @Override
    public void format(Object obj, StringBuilder output) {
        if (obj instanceof Date) {
            this.format((Date)obj, output);
        }
        super.format(obj, output);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void format(Date date, StringBuilder toAppendTo) {
        DatePatternConverter datePatternConverter = this;
        synchronized (datePatternConverter) {
            toAppendTo.append(this.simpleFormat.format(date.getTime()));
        }
    }

    public String getPattern() {
        return this.simpleFormat.toPattern();
    }
}

