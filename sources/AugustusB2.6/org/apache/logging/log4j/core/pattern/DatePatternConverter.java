// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Date;
import org.apache.logging.log4j.core.util.datetime.FastDateFormat;
import java.util.TimeZone;
import java.util.Objects;
import org.apache.logging.log4j.core.util.datetime.FixedDateFormat;
import org.apache.logging.log4j.core.time.Instant;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.core.time.MutableInstant;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DatePatternConverter", category = "Converter")
@ConverterKeys({ "d", "date" })
@PerformanceSensitive({ "allocation" })
public final class DatePatternConverter extends LogEventPatternConverter implements ArrayPatternConverter
{
    private static final String UNIX_FORMAT = "UNIX";
    private static final String UNIX_MILLIS_FORMAT = "UNIX_MILLIS";
    private final String[] options;
    private final ThreadLocal<MutableInstant> threadLocalMutableInstant;
    private final ThreadLocal<Formatter> threadLocalFormatter;
    private final AtomicReference<CachedTime> cachedTime;
    private final Formatter formatter;
    
    private DatePatternConverter(final String[] options) {
        super("Date", "date");
        this.threadLocalMutableInstant = new ThreadLocal<MutableInstant>();
        this.threadLocalFormatter = new ThreadLocal<Formatter>();
        this.options = (String[])((options == null) ? null : ((String[])Arrays.copyOf(options, options.length)));
        this.formatter = this.createFormatter(options);
        this.cachedTime = new AtomicReference<CachedTime>(this.fromEpochMillis(System.currentTimeMillis()));
    }
    
    private CachedTime fromEpochMillis(final long epochMillis) {
        final MutableInstant temp = new MutableInstant();
        temp.initFromEpochMilli(epochMillis, 0);
        return new CachedTime(temp);
    }
    
    private Formatter createFormatter(final String[] options) {
        final FixedDateFormat fixedDateFormat = FixedDateFormat.createIfSupported(options);
        if (fixedDateFormat != null) {
            return createFixedFormatter(fixedDateFormat);
        }
        return createNonFixedFormatter(options);
    }
    
    public static DatePatternConverter newInstance(final String[] options) {
        return new DatePatternConverter(options);
    }
    
    private static Formatter createFixedFormatter(final FixedDateFormat fixedDateFormat) {
        return new FixedFormatter(fixedDateFormat);
    }
    
    private static Formatter createNonFixedFormatter(final String[] options) {
        Objects.requireNonNull(options);
        if (options.length == 0) {
            throw new IllegalArgumentException("Options array must have at least one element");
        }
        Objects.requireNonNull(options[0]);
        final String patternOption = options[0];
        if ("UNIX".equals(patternOption)) {
            return new UnixFormatter();
        }
        if ("UNIX_MILLIS".equals(patternOption)) {
            return new UnixMillisFormatter();
        }
        final FixedDateFormat.FixedFormat fixedFormat = FixedDateFormat.FixedFormat.lookup(patternOption);
        final String pattern = (fixedFormat == null) ? patternOption : fixedFormat.getPattern();
        TimeZone tz = null;
        if (options.length > 1 && options[1] != null) {
            tz = TimeZone.getTimeZone(options[1]);
        }
        try {
            final FastDateFormat tempFormat = FastDateFormat.getInstance(pattern, tz);
            return new PatternFormatter(tempFormat);
        }
        catch (IllegalArgumentException e) {
            DatePatternConverter.LOGGER.warn("Could not instantiate FastDateFormat with pattern " + pattern, e);
            return createFixedFormatter(FixedDateFormat.create(FixedDateFormat.FixedFormat.DEFAULT, tz));
        }
    }
    
    public void format(final Date date, final StringBuilder toAppendTo) {
        this.format(date.getTime(), toAppendTo);
    }
    
    @Override
    public void format(final LogEvent event, final StringBuilder output) {
        this.format(event.getInstant(), output);
    }
    
    public void format(final long epochMilli, final StringBuilder output) {
        final MutableInstant instant = this.getMutableInstant();
        instant.initFromEpochMilli(epochMilli, 0);
        this.format(instant, output);
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
    
    public void format(final Instant instant, final StringBuilder output) {
        if (Constants.ENABLE_THREADLOCALS) {
            this.formatWithoutAllocation(instant, output);
        }
        else {
            this.formatWithoutThreadLocals(instant, output);
        }
    }
    
    private void formatWithoutAllocation(final Instant instant, final StringBuilder output) {
        this.getThreadLocalFormatter().formatToBuffer(instant, output);
    }
    
    private Formatter getThreadLocalFormatter() {
        Formatter result = this.threadLocalFormatter.get();
        if (result == null) {
            result = this.createFormatter(this.options);
            this.threadLocalFormatter.set(result);
        }
        return result;
    }
    
    private void formatWithoutThreadLocals(final Instant instant, final StringBuilder output) {
        CachedTime cached = this.cachedTime.get();
        if (instant.getEpochSecond() != cached.epochSecond || instant.getNanoOfSecond() != cached.nanoOfSecond) {
            final CachedTime newTime = new CachedTime(instant);
            if (this.cachedTime.compareAndSet(cached, newTime)) {
                cached = newTime;
            }
            else {
                cached = this.cachedTime.get();
            }
        }
        output.append(cached.formatted);
    }
    
    @Override
    public void format(final Object obj, final StringBuilder output) {
        if (obj instanceof Date) {
            this.format((Date)obj, output);
        }
        super.format(obj, output);
    }
    
    @Override
    public void format(final StringBuilder toAppendTo, final Object... objects) {
        for (final Object obj : objects) {
            if (obj instanceof Date) {
                this.format(obj, toAppendTo);
                break;
            }
        }
    }
    
    public String getPattern() {
        return this.formatter.toPattern();
    }
    
    private abstract static class Formatter
    {
        long previousTime;
        int nanos;
        
        abstract String format(final Instant instant);
        
        abstract void formatToBuffer(final Instant instant, final StringBuilder destination);
        
        public String toPattern() {
            return null;
        }
    }
    
    private static final class PatternFormatter extends Formatter
    {
        private final FastDateFormat fastDateFormat;
        private final StringBuilder cachedBuffer;
        
        PatternFormatter(final FastDateFormat fastDateFormat) {
            this.cachedBuffer = new StringBuilder(64);
            this.fastDateFormat = fastDateFormat;
        }
        
        @Override
        String format(final Instant instant) {
            return this.fastDateFormat.format(instant.getEpochMillisecond());
        }
        
        @Override
        void formatToBuffer(final Instant instant, final StringBuilder destination) {
            final long timeMillis = instant.getEpochMillisecond();
            if (this.previousTime != timeMillis) {
                this.cachedBuffer.setLength(0);
                this.fastDateFormat.format(timeMillis, this.cachedBuffer);
            }
            destination.append((CharSequence)this.cachedBuffer);
        }
        
        @Override
        public String toPattern() {
            return this.fastDateFormat.getPattern();
        }
    }
    
    private static final class FixedFormatter extends Formatter
    {
        private final FixedDateFormat fixedDateFormat;
        private final char[] cachedBuffer;
        private int length;
        
        FixedFormatter(final FixedDateFormat fixedDateFormat) {
            this.cachedBuffer = new char[70];
            this.length = 0;
            this.fixedDateFormat = fixedDateFormat;
        }
        
        @Override
        String format(final Instant instant) {
            return this.fixedDateFormat.formatInstant(instant);
        }
        
        @Override
        void formatToBuffer(final Instant instant, final StringBuilder destination) {
            final long epochSecond = instant.getEpochSecond();
            final int nanoOfSecond = instant.getNanoOfSecond();
            if (!this.fixedDateFormat.isEquivalent(this.previousTime, this.nanos, epochSecond, nanoOfSecond)) {
                this.length = this.fixedDateFormat.formatInstant(instant, this.cachedBuffer, 0);
                this.previousTime = epochSecond;
                this.nanos = nanoOfSecond;
            }
            destination.append(this.cachedBuffer, 0, this.length);
        }
        
        @Override
        public String toPattern() {
            return this.fixedDateFormat.getFormat();
        }
    }
    
    private static final class UnixFormatter extends Formatter
    {
        @Override
        String format(final Instant instant) {
            return Long.toString(instant.getEpochSecond());
        }
        
        @Override
        void formatToBuffer(final Instant instant, final StringBuilder destination) {
            destination.append(instant.getEpochSecond());
        }
    }
    
    private static final class UnixMillisFormatter extends Formatter
    {
        @Override
        String format(final Instant instant) {
            return Long.toString(instant.getEpochMillisecond());
        }
        
        @Override
        void formatToBuffer(final Instant instant, final StringBuilder destination) {
            destination.append(instant.getEpochMillisecond());
        }
    }
    
    private final class CachedTime
    {
        public long epochSecond;
        public int nanoOfSecond;
        public String formatted;
        
        public CachedTime(final Instant instant) {
            this.epochSecond = instant.getEpochSecond();
            this.nanoOfSecond = instant.getNanoOfSecond();
            this.formatted = DatePatternConverter.this.formatter.format(instant);
        }
    }
}
