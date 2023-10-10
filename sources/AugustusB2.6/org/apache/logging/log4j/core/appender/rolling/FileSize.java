// 
// Decompiled by Procyon v0.5.36
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
    private static final Pattern VALUE_PATTERN;
    
    private FileSize() {
    }
    
    public static long parse(final String string, final long defaultValue) {
        final Matcher matcher = FileSize.VALUE_PATTERN.matcher(string);
        if (matcher.matches()) {
            try {
                final double value = NumberFormat.getNumberInstance(Locale.ROOT).parse(matcher.group(1)).doubleValue();
                final String units = matcher.group(3);
                if (units.isEmpty()) {
                    return (long)value;
                }
                if (units.equalsIgnoreCase("K")) {
                    return (long)(value * 1024.0);
                }
                if (units.equalsIgnoreCase("M")) {
                    return (long)(value * 1048576.0);
                }
                if (units.equalsIgnoreCase("G")) {
                    return (long)(value * 1.073741824E9);
                }
                FileSize.LOGGER.error("FileSize units not recognized: " + string);
                return defaultValue;
            }
            catch (ParseException e) {
                FileSize.LOGGER.error("FileSize unable to parse numeric part: " + string, e);
                return defaultValue;
            }
        }
        FileSize.LOGGER.error("FileSize unable to parse bytes: " + string);
        return defaultValue;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        VALUE_PATTERN = Pattern.compile("([0-9]+([\\.,][0-9]+)?)\\s*(|K|M|G)B?", 2);
    }
}
