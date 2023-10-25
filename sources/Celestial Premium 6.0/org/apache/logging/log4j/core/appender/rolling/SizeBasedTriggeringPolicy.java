/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="SizeBasedTriggeringPolicy", category="Core", printObject=true)
public class SizeBasedTriggeringPolicy
implements TriggeringPolicy {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    private static final long KB = 1024L;
    private static final long MB = 0x100000L;
    private static final long GB = 0x40000000L;
    private static final long MAX_FILE_SIZE = 0xA00000L;
    private static final Pattern VALUE_PATTERN = Pattern.compile("([0-9]+([\\.,][0-9]+)?)\\s*(|K|M|G)B?", 2);
    private final long maxFileSize;
    private RollingFileManager manager;

    protected SizeBasedTriggeringPolicy() {
        this.maxFileSize = 0xA00000L;
    }

    protected SizeBasedTriggeringPolicy(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    @Override
    public void initialize(RollingFileManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean isTriggeringEvent(LogEvent event) {
        return this.manager.getFileSize() > this.maxFileSize;
    }

    public String toString() {
        return "SizeBasedTriggeringPolicy(size=" + this.maxFileSize + ")";
    }

    @PluginFactory
    public static SizeBasedTriggeringPolicy createPolicy(@PluginAttribute(value="size") String size) {
        long maxSize = size == null ? 0xA00000L : SizeBasedTriggeringPolicy.valueOf(size);
        return new SizeBasedTriggeringPolicy(maxSize);
    }

    private static long valueOf(String string) {
        Matcher matcher = VALUE_PATTERN.matcher(string);
        if (matcher.matches()) {
            try {
                long value = NumberFormat.getNumberInstance(Locale.getDefault()).parse(matcher.group(1)).longValue();
                String units = matcher.group(3);
                if (units.equalsIgnoreCase("")) {
                    return value;
                }
                if (units.equalsIgnoreCase("K")) {
                    return value * 1024L;
                }
                if (units.equalsIgnoreCase("M")) {
                    return value * 0x100000L;
                }
                if (units.equalsIgnoreCase("G")) {
                    return value * 0x40000000L;
                }
                LOGGER.error("Units not recognized: " + string);
                return 0xA00000L;
            }
            catch (ParseException e) {
                LOGGER.error("Unable to parse numeric part: " + string, (Throwable)e);
                return 0xA00000L;
            }
        }
        LOGGER.error("Unable to parse bytes: " + string);
        return 0xA00000L;
    }
}

