// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.util.Locale;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.text.NumberFormat;

public final class Options
{
    public static final boolean DEBUG;
    public static final boolean NO_UNSAFE;
    public static final boolean FORCE_UNSAFE;
    public static final boolean FASTMATH;
    public static final boolean SIN_LOOKUP;
    public static final int SIN_LOOKUP_BITS;
    public static final boolean useNumberFormat;
    public static final boolean USE_MATH_FMA;
    public static final int numberFormatDecimals;
    public static final NumberFormat NUMBER_FORMAT;
    
    private Options() {
    }
    
    private static NumberFormat decimalFormat() {
        NumberFormat df;
        if (Options.useNumberFormat) {
            final char[] prec = new char[Options.numberFormatDecimals];
            Arrays.fill(prec, '0');
            df = new DecimalFormat(" 0." + new String(prec) + "E0;-");
        }
        else {
            df = NumberFormat.getNumberInstance(Locale.ENGLISH);
            df.setGroupingUsed(false);
        }
        return df;
    }
    
    private static boolean hasOption(final String v) {
        return v != null && (v.trim().length() == 0 || Boolean.valueOf(v));
    }
    
    static {
        DEBUG = hasOption(System.getProperty("joml.debug", "false"));
        NO_UNSAFE = hasOption(System.getProperty("joml.nounsafe", "false"));
        FORCE_UNSAFE = hasOption(System.getProperty("joml.forceUnsafe", "false"));
        FASTMATH = hasOption(System.getProperty("joml.fastmath", "false"));
        SIN_LOOKUP = hasOption(System.getProperty("joml.sinLookup", "false"));
        SIN_LOOKUP_BITS = Integer.parseInt(System.getProperty("joml.sinLookup.bits", "14"));
        useNumberFormat = hasOption(System.getProperty("joml.format", "true"));
        USE_MATH_FMA = hasOption(System.getProperty("joml.useMathFma", "false"));
        numberFormatDecimals = Integer.parseInt(System.getProperty("joml.format.decimals", "3"));
        NUMBER_FORMAT = decimalFormat();
    }
}
