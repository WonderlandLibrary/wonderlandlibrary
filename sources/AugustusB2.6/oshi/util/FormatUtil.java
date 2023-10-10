// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util;

import java.util.concurrent.TimeUnit;
import java.math.BigInteger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class FormatUtil
{
    private static final long KIBI = 1024L;
    private static final long MEBI = 1048576L;
    private static final long GIBI = 1073741824L;
    private static final long TEBI = 1099511627776L;
    private static final long PEBI = 1125899906842624L;
    private static final long EXBI = 1152921504606846976L;
    private static final long KILO = 1000L;
    private static final long MEGA = 1000000L;
    private static final long GIGA = 1000000000L;
    private static final long TERA = 1000000000000L;
    private static final long PETA = 1000000000000000L;
    private static final long EXA = 1000000000000000000L;
    private static final BigInteger TWOS_COMPLEMENT_REF;
    public static final String HEX_ERROR = "0x%08X";
    
    private FormatUtil() {
    }
    
    public static String formatBytes(final long bytes) {
        if (bytes == 1L) {
            return String.format("%d byte", bytes);
        }
        if (bytes < 1024L) {
            return String.format("%d bytes", bytes);
        }
        if (bytes < 1048576L) {
            return formatUnits(bytes, 1024L, "KiB");
        }
        if (bytes < 1073741824L) {
            return formatUnits(bytes, 1048576L, "MiB");
        }
        if (bytes < 1099511627776L) {
            return formatUnits(bytes, 1073741824L, "GiB");
        }
        if (bytes < 1125899906842624L) {
            return formatUnits(bytes, 1099511627776L, "TiB");
        }
        if (bytes < 1152921504606846976L) {
            return formatUnits(bytes, 1125899906842624L, "PiB");
        }
        return formatUnits(bytes, 1152921504606846976L, "EiB");
    }
    
    private static String formatUnits(final long value, final long prefix, final String unit) {
        if (value % prefix == 0L) {
            return String.format("%d %s", value / prefix, unit);
        }
        return String.format("%.1f %s", value / (double)prefix, unit);
    }
    
    public static String formatBytesDecimal(final long bytes) {
        if (bytes == 1L) {
            return String.format("%d byte", bytes);
        }
        if (bytes < 1000L) {
            return String.format("%d bytes", bytes);
        }
        return formatValue(bytes, "B");
    }
    
    public static String formatHertz(final long hertz) {
        return formatValue(hertz, "Hz");
    }
    
    public static String formatValue(final long value, final String unit) {
        if (value < 1000L) {
            return String.format("%d %s", value, unit).trim();
        }
        if (value < 1000000L) {
            return formatUnits(value, 1000L, "K" + unit);
        }
        if (value < 1000000000L) {
            return formatUnits(value, 1000000L, "M" + unit);
        }
        if (value < 1000000000000L) {
            return formatUnits(value, 1000000000L, "G" + unit);
        }
        if (value < 1000000000000000L) {
            return formatUnits(value, 1000000000000L, "T" + unit);
        }
        if (value < 1000000000000000000L) {
            return formatUnits(value, 1000000000000000L, "P" + unit);
        }
        return formatUnits(value, 1000000000000000000L, "E" + unit);
    }
    
    public static String formatElapsedSecs(final long secs) {
        long eTime = secs;
        final long days = TimeUnit.SECONDS.toDays(eTime);
        eTime -= TimeUnit.DAYS.toSeconds(days);
        final long hr = TimeUnit.SECONDS.toHours(eTime);
        eTime -= TimeUnit.HOURS.toSeconds(hr);
        final long min = TimeUnit.SECONDS.toMinutes(eTime);
        final long sec;
        eTime = (sec = eTime - TimeUnit.MINUTES.toSeconds(min));
        return String.format("%d days, %02d:%02d:%02d", days, hr, min, sec);
    }
    
    public static long getUnsignedInt(final int x) {
        return (long)x & 0xFFFFFFFFL;
    }
    
    public static String toUnsignedString(final int i) {
        if (i >= 0) {
            return Integer.toString(i);
        }
        return Long.toString(getUnsignedInt(i));
    }
    
    public static String toUnsignedString(final long l) {
        if (l >= 0L) {
            return Long.toString(l);
        }
        return BigInteger.valueOf(l).add(FormatUtil.TWOS_COMPLEMENT_REF).toString();
    }
    
    public static String formatError(final int errorCode) {
        return String.format("0x%08X", errorCode);
    }
    
    public static int roundToInt(final double x) {
        return (int)Math.round(x);
    }
    
    static {
        TWOS_COMPLEMENT_REF = BigInteger.ONE.shiftLeft(64);
    }
}
