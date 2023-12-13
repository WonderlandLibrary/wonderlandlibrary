// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.misc;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class TimeUtil
{
    public static String formatMillis(final int millis) {
        final String string = DurationFormatUtils.formatDuration((long)millis, "H:mm:ss", true);
        final String[] parts = string.split(":");
        if (!parts[0].equals("0") && !parts[0].equals("00")) {
            return String.valueOf(parts[0]) + "h " + parts[1] + "m " + parts[2] + "s ";
        }
        if (!parts[1].equals("0") && !parts[1].equals("00")) {
            return String.valueOf(parts[1]) + "m " + parts[2] + "s ";
        }
        return String.valueOf(parts[2]) + "s ";
    }
}
