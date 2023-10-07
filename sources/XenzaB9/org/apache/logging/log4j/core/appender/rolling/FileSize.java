// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.regex.Matcher;
import java.text.ParseException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;

public final class FileSize
{
    private static final Logger LOGGER;
    private static final long KB = 1024L;
    private static final long MB = 1048576L;
    private static final long GB = 1073741824L;
    private static final long TB = 1099511627776L;
    private static final Pattern VALUE_PATTERN;
    
    private FileSize() {
    }
    
    public static long parse(final String string, final long defaultValue) {
        final Matcher matcher = FileSize.VALUE_PATTERN.matcher(string);
        if (matcher.matches()) {
            try {
                final String quantityString = matcher.group(1);
                final double quantity = NumberFormat.getNumberInstance(Locale.ROOT).parse(quantityString).doubleValue();
                final String unit = matcher.group(3);
                if (unit == null || unit.isEmpty()) {
                    return (long)quantity;
                }
                if (unit.equalsIgnoreCase("K")) {
                    return (long)(quantity * 1024.0);
                }
                if (unit.equalsIgnoreCase("M")) {
                    return (long)(quantity * 1048576.0);
                }
                if (unit.equalsIgnoreCase("G")) {
                    return (long)(quantity * 1.073741824E9);
                }
                if (unit.equalsIgnoreCase("T")) {
                    return (long)(quantity * 1.099511627776E12);
                }
                FileSize.LOGGER.error("FileSize units not recognized: " + string);
                return defaultValue;
            }
            catch (final ParseException error) {
                FileSize.LOGGER.error("FileSize unable to parse numeric part: " + string, error);
                return defaultValue;
            }
        }
        FileSize.LOGGER.error("FileSize unable to parse bytes: " + string);
        return defaultValue;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        VALUE_PATTERN = Pattern.compile("([0-9]+([.,][0-9]+)?)\\s*(|K|M|G|T)B?", 2);
    }
}
