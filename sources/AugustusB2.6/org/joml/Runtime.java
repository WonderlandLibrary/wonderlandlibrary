// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

import java.text.NumberFormat;

public final class Runtime
{
    public static final boolean HAS_floatToRawIntBits;
    public static final boolean HAS_doubleToRawLongBits;
    public static final boolean HAS_Long_rotateLeft;
    public static final boolean HAS_Math_fma;
    private static /* synthetic */ Class class$0;
    private static /* synthetic */ Class class$1;
    private static /* synthetic */ Class class$2;
    private static /* synthetic */ Class class$3;
    
    private static boolean hasMathFma() {
        try {
            ((Runtime.class$0 == null) ? (Runtime.class$0 = class$("java.lang.Math")) : Runtime.class$0).getDeclaredMethod("fma", Float.TYPE, Float.TYPE, Float.TYPE);
            return true;
        }
        catch (NoSuchMethodException e) {
            return false;
        }
    }
    
    private static /* synthetic */ Class class$(final String className) {
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException cause) {
            throw new NoClassDefFoundError().initCause(cause);
        }
    }
    
    private Runtime() {
    }
    
    private static boolean hasFloatToRawIntBits() {
        try {
            ((Runtime.class$1 == null) ? (Runtime.class$1 = class$("java.lang.Float")) : Runtime.class$1).getDeclaredMethod("floatToRawIntBits", Float.TYPE);
            return true;
        }
        catch (NoSuchMethodException e) {
            return false;
        }
    }
    
    private static boolean hasDoubleToRawLongBits() {
        try {
            ((Runtime.class$2 == null) ? (Runtime.class$2 = class$("java.lang.Double")) : Runtime.class$2).getDeclaredMethod("doubleToRawLongBits", Double.TYPE);
            return true;
        }
        catch (NoSuchMethodException e) {
            return false;
        }
    }
    
    private static boolean hasLongRotateLeft() {
        try {
            ((Runtime.class$3 == null) ? (Runtime.class$3 = class$("java.lang.Long")) : Runtime.class$3).getDeclaredMethod("rotateLeft", Long.TYPE, Integer.TYPE);
            return true;
        }
        catch (NoSuchMethodException e) {
            return false;
        }
    }
    
    public static int floatToIntBits(final float flt) {
        if (Runtime.HAS_floatToRawIntBits) {
            return floatToIntBits1_3(flt);
        }
        return floatToIntBits1_2(flt);
    }
    
    private static int floatToIntBits1_3(final float flt) {
        return Float.floatToRawIntBits(flt);
    }
    
    private static int floatToIntBits1_2(final float flt) {
        return Float.floatToIntBits(flt);
    }
    
    public static long doubleToLongBits(final double dbl) {
        if (Runtime.HAS_doubleToRawLongBits) {
            return doubleToLongBits1_3(dbl);
        }
        return doubleToLongBits1_2(dbl);
    }
    
    private static long doubleToLongBits1_3(final double dbl) {
        return Double.doubleToRawLongBits(dbl);
    }
    
    private static long doubleToLongBits1_2(final double dbl) {
        return Double.doubleToLongBits(dbl);
    }
    
    public static String formatNumbers(final String str) {
        final StringBuffer res = new StringBuffer();
        int eIndex = Integer.MIN_VALUE;
        for (int i = 0; i < str.length(); ++i) {
            final char c = str.charAt(i);
            if (c == 'E') {
                eIndex = i;
            }
            else {
                if (c == ' ' && eIndex == i - 1) {
                    res.append('+');
                    continue;
                }
                if (Character.isDigit(c) && eIndex == i - 1) {
                    res.append('+');
                }
            }
            res.append(c);
        }
        return res.toString();
    }
    
    public static String format(final double number, final NumberFormat format) {
        if (Double.isNaN(number)) {
            return padLeft(format, " NaN");
        }
        if (Double.isInfinite(number)) {
            return padLeft(format, (number > 0.0) ? " +Inf" : " -Inf");
        }
        return format.format(number);
    }
    
    private static String padLeft(final NumberFormat format, final String str) {
        final int len = format.format(0.0).length();
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len - str.length() + 1; ++i) {
            sb.append(" ");
        }
        return sb.append(str).toString();
    }
    
    public static boolean equals(final float a, final float b, final float delta) {
        return Float.floatToIntBits(a) == Float.floatToIntBits(b) || Math.abs(a - b) <= delta;
    }
    
    public static boolean equals(final double a, final double b, final double delta) {
        return Double.doubleToLongBits(a) == Double.doubleToLongBits(b) || Math.abs(a - b) <= delta;
    }
    
    static {
        HAS_floatToRawIntBits = hasFloatToRawIntBits();
        HAS_doubleToRawLongBits = hasDoubleToRawLongBits();
        HAS_Long_rotateLeft = hasLongRotateLeft();
        HAS_Math_fma = (Options.USE_MATH_FMA && hasMathFma());
    }
}
