/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.util.Collections;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

public final class NativeMath
extends ScriptObject {
    private static PropertyMap $nasgenmap$;
    public static final double E = Math.E;
    public static final double LN10 = 2.302585092994046;
    public static final double LN2 = 0.6931471805599453;
    public static final double LOG2E = 1.4426950408889634;
    public static final double LOG10E = 0.4342944819032518;
    public static final double PI = Math.PI;
    public static final double SQRT1_2 = 0.7071067811865476;
    public static final double SQRT2 = 1.4142135623730951;

    private NativeMath() {
        throw new UnsupportedOperationException();
    }

    public static double abs(Object self, Object x) {
        return Math.abs(JSType.toNumber(x));
    }

    public static int abs(Object self, int x) {
        return Math.abs(x);
    }

    public static long abs(Object self, long x) {
        return Math.abs(x);
    }

    public static double abs(Object self, double x) {
        return Math.abs(x);
    }

    public static double acos(Object self, Object x) {
        return Math.acos(JSType.toNumber(x));
    }

    public static double acos(Object self, double x) {
        return Math.acos(x);
    }

    public static double asin(Object self, Object x) {
        return Math.asin(JSType.toNumber(x));
    }

    public static double asin(Object self, double x) {
        return Math.asin(x);
    }

    public static double atan(Object self, Object x) {
        return Math.atan(JSType.toNumber(x));
    }

    public static double atan(Object self, double x) {
        return Math.atan(x);
    }

    public static double atan2(Object self, Object y, Object x) {
        return Math.atan2(JSType.toNumber(y), JSType.toNumber(x));
    }

    public static double atan2(Object self, double y, double x) {
        return Math.atan2(y, x);
    }

    public static double ceil(Object self, Object x) {
        return Math.ceil(JSType.toNumber(x));
    }

    public static int ceil(Object self, int x) {
        return x;
    }

    public static long ceil(Object self, long x) {
        return x;
    }

    public static double ceil(Object self, double x) {
        return Math.ceil(x);
    }

    public static double cos(Object self, Object x) {
        return Math.cos(JSType.toNumber(x));
    }

    public static double cos(Object self, double x) {
        return Math.cos(x);
    }

    public static double exp(Object self, Object x) {
        return Math.exp(JSType.toNumber(x));
    }

    public static double floor(Object self, Object x) {
        return Math.floor(JSType.toNumber(x));
    }

    public static int floor(Object self, int x) {
        return x;
    }

    public static long floor(Object self, long x) {
        return x;
    }

    public static double floor(Object self, double x) {
        return Math.floor(x);
    }

    public static double log(Object self, Object x) {
        return Math.log(JSType.toNumber(x));
    }

    public static double log(Object self, double x) {
        return Math.log(x);
    }

    public static double max(Object self, Object ... args2) {
        switch (args2.length) {
            case 0: {
                return Double.NEGATIVE_INFINITY;
            }
            case 1: {
                return JSType.toNumber(args2[0]);
            }
        }
        double res = JSType.toNumber(args2[0]);
        for (int i = 1; i < args2.length; ++i) {
            res = Math.max(res, JSType.toNumber(args2[i]));
        }
        return res;
    }

    public static double max(Object self) {
        return Double.NEGATIVE_INFINITY;
    }

    public static int max(Object self, int x, int y) {
        return Math.max(x, y);
    }

    public static long max(Object self, long x, long y) {
        return Math.max(x, y);
    }

    public static double max(Object self, double x, double y) {
        return Math.max(x, y);
    }

    public static double max(Object self, Object x, Object y) {
        return Math.max(JSType.toNumber(x), JSType.toNumber(y));
    }

    public static double min(Object self, Object ... args2) {
        switch (args2.length) {
            case 0: {
                return Double.POSITIVE_INFINITY;
            }
            case 1: {
                return JSType.toNumber(args2[0]);
            }
        }
        double res = JSType.toNumber(args2[0]);
        for (int i = 1; i < args2.length; ++i) {
            res = Math.min(res, JSType.toNumber(args2[i]));
        }
        return res;
    }

    public static double min(Object self) {
        return Double.POSITIVE_INFINITY;
    }

    public static int min(Object self, int x, int y) {
        return Math.min(x, y);
    }

    public static long min(Object self, long x, long y) {
        return Math.min(x, y);
    }

    public static double min(Object self, double x, double y) {
        return Math.min(x, y);
    }

    public static double min(Object self, Object x, Object y) {
        return Math.min(JSType.toNumber(x), JSType.toNumber(y));
    }

    public static double pow(Object self, Object x, Object y) {
        return Math.pow(JSType.toNumber(x), JSType.toNumber(y));
    }

    public static double pow(Object self, double x, double y) {
        return Math.pow(x, y);
    }

    public static double random(Object self) {
        return Math.random();
    }

    public static double round(Object self, Object x) {
        double d = JSType.toNumber(x);
        if (Math.getExponent(d) >= 52) {
            return d;
        }
        return Math.copySign(Math.floor(d + 0.5), d);
    }

    public static double sin(Object self, Object x) {
        return Math.sin(JSType.toNumber(x));
    }

    public static double sin(Object self, double x) {
        return Math.sin(x);
    }

    public static double sqrt(Object self, Object x) {
        return Math.sqrt(JSType.toNumber(x));
    }

    public static double sqrt(Object self, double x) {
        return Math.sqrt(x);
    }

    public static double tan(Object self, Object x) {
        return Math.tan(JSType.toNumber(x));
    }

    public static double tan(Object self, double x) {
        return Math.tan(x);
    }

    static {
        NativeMath.$clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}

