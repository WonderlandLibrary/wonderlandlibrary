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
import org.apache.logging.log4j.status.StatusLogger;

public final class FileSize {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final long KB = 1024L;
    private static final long MB = 0x100000L;
    private static final long GB = 0x40000000L;
    private static final Pattern VALUE_PATTERN = Pattern.compile("([0-9]+([\\.,][0-9]+)?)\\s*(|K|M|G)B?", 2);

    private FileSize() {
    }

    public static long parse(String string, long defaultValue) {
        Matcher matcher = VALUE_PATTERN.matcher(string);
        if (matcher.matches()) {
            try {
                long value = NumberFormat.getNumberInstance(Locale.ROOT).parse(matcher.group(1)).longValue();
                String units = matcher.group(3);
                if (units.isEmpty()) {
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
                LOGGER.error("FileSize units not recognized: " + string);
                return defaultValue;
            }
            catch (ParseException e) {
                LOGGER.error("FileSize unable to parse numeric part: " + string, (Throwable)e);
                return defaultValue;
            }
        }
        LOGGER.error("FileSize unable to parse bytes: " + string);
        return defaultValue;
    }
}

