// 
// Decompiled by Procyon v0.5.36
// 

package org.joml;

public class Math
{
    public static final double PI = 3.141592653589793;
    static final double PI2 = 6.283185307179586;
    static final float PI_f = 3.1415927f;
    static final float PI2_f = 6.2831855f;
    static final double PIHalf = 1.5707963267948966;
    static final float PIHalf_f = 1.5707964f;
    static final double PI_4 = 0.7853981633974483;
    static final double PI_INV = 0.3183098861837907;
    private static final int lookupBits;
    private static final int lookupTableSize;
    private static final int lookupTableSizeMinus1;
    private static final int lookupTableSizeWithMargin;
    private static final float pi2OverLookupSize;
    private static final float lookupSizeOverPi2;
    private static final float[] sinTable;
    private static final double c1;
    private static final double c2;
    private static final double c3;
    private static final double c4;
    private static final double c5;
    private static final double c6;
    private static final double c7;
    private static final double s5;
    private static final double s4;
    private static final double s3;
    private static final double s2;
    private static final double s1;
    private static final double k1;
    private static final double k2;
    private static final double k3;
    private static final double k4;
    private static final double k5;
    private static final double k6;
    private static final double k7;
    
    static double sin_theagentd_arith(final double x) {
        final double xi = floor((x + 0.7853981633974483) * 0.3183098861837907);
        final double x_ = x - xi * 3.141592653589793;
        final double sign = ((int)xi & 0x1) * -2 + 1;
        final double x2 = x_ * x_;
        double sin = x_;
        double tx = x_ * x2;
        sin += tx * Math.c1;
        tx *= x2;
        sin += tx * Math.c2;
        tx *= x2;
        sin += tx * Math.c3;
        tx *= x2;
        sin += tx * Math.c4;
        tx *= x2;
        sin += tx * Math.c5;
        tx *= x2;
        sin += tx * Math.c6;
        tx *= x2;
        sin += tx * Math.c7;
        return sign * sin;
    }
    
    static double sin_roquen_arith(final double x) {
        final double xi = floor((x + 0.7853981633974483) * 0.3183098861837907);
        double x_ = x - xi * 3.141592653589793;
        final double sign = ((int)xi & 0x1) * -2 + 1;
        final double x2 = x_ * x_;
        x_ *= sign;
        double sin = Math.c7;
        sin = sin * x2 + Math.c6;
        sin = sin * x2 + Math.c5;
        sin = sin * x2 + Math.c4;
        sin = sin * x2 + Math.c3;
        sin = sin * x2 + Math.c2;
        sin = sin * x2 + Math.c1;
        return x_ + x_ * x2 * sin;
    }
    
    static double sin_roquen_9(final double v) {
        final double i = java.lang.Math.rint(v * 0.3183098861837907);
        double x = v - i * 3.141592653589793;
        final double qs = 1 - 2 * ((int)i & 0x1);
        final double x2 = x * x;
        x *= qs;
        double r = Math.s5;
        r = r * x2 + Math.s4;
        r = r * x2 + Math.s3;
        r = r * x2 + Math.s2;
        r = r * x2 + Math.s1;
        return x * r;
    }
    
    static double sin_roquen_newk(final double v) {
        final double i = java.lang.Math.rint(v * 0.3183098861837907);
        double x = v - i * 3.141592653589793;
        final double qs = 1 - 2 * ((int)i & 0x1);
        final double x2 = x * x;
        x *= qs;
        double r = Math.k7;
        r = r * x2 + Math.k6;
        r = r * x2 + Math.k5;
        r = r * x2 + Math.k4;
        r = r * x2 + Math.k3;
        r = r * x2 + Math.k2;
        r = r * x2 + Math.k1;
        return x + x * x2 * r;
    }
    
    static float sin_theagentd_lookup(final float rad) {
        final float index = rad * Math.lookupSizeOverPi2;
        final int ii = (int)java.lang.Math.floor(index);
        final float alpha = index - ii;
        final int i = ii & Math.lookupTableSizeMinus1;
        final float sin1 = Math.sinTable[i];
        final float sin2 = Math.sinTable[i + 1];
        return sin1 + (sin2 - sin1) * alpha;
    }
    
    public static float sin(final float rad) {
        if (!Options.FASTMATH) {
            return (float)java.lang.Math.sin(rad);
        }
        if (Options.SIN_LOOKUP) {
            return sin_theagentd_lookup(rad);
        }
        return (float)sin_roquen_newk(rad);
    }
    
    public static double sin(final double rad) {
        if (!Options.FASTMATH) {
            return java.lang.Math.sin(rad);
        }
        if (Options.SIN_LOOKUP) {
            return sin_theagentd_lookup((float)rad);
        }
        return sin_roquen_newk(rad);
    }
    
    public static float cos(final float rad) {
        if (Options.FASTMATH) {
            return sin(rad + 1.5707964f);
        }
        return (float)java.lang.Math.cos(rad);
    }
    
    public static double cos(final double rad) {
        if (Options.FASTMATH) {
            return sin(rad + 1.5707963267948966);
        }
        return java.lang.Math.cos(rad);
    }
    
    public static float cosFromSin(final float sin, final float angle) {
        if (Options.FASTMATH) {
            return sin(angle + 1.5707964f);
        }
        return cosFromSinInternal(sin, angle);
    }
    
    private static float cosFromSinInternal(final float sin, final float angle) {
        final float cos = sqrt(1.0f - sin * sin);
        final float a = angle + 1.5707964f;
        float b = a - (int)(a / 6.2831855f) * 6.2831855f;
        if (b < 0.0) {
            b += 6.2831855f;
        }
        if (b >= 3.1415927f) {
            return -cos;
        }
        return cos;
    }
    
    public static double cosFromSin(final double sin, final double angle) {
        if (Options.FASTMATH) {
            return sin(angle + 1.5707963267948966);
        }
        final double cos = sqrt(1.0 - sin * sin);
        final double a = angle + 1.5707963267948966;
        double b = a - (int)(a / 6.283185307179586) * 6.283185307179586;
        if (b < 0.0) {
            b += 6.283185307179586;
        }
        if (b >= 3.141592653589793) {
            return -cos;
        }
        return cos;
    }
    
    public static float sqrt(final float r) {
        return (float)java.lang.Math.sqrt(r);
    }
    
    public static double sqrt(final double r) {
        return java.lang.Math.sqrt(r);
    }
    
    public static float invsqrt(final float r) {
        return 1.0f / (float)java.lang.Math.sqrt(r);
    }
    
    public static double invsqrt(final double r) {
        return 1.0 / java.lang.Math.sqrt(r);
    }
    
    public static float tan(final float r) {
        return (float)java.lang.Math.tan(r);
    }
    
    public static double tan(final double r) {
        return java.lang.Math.tan(r);
    }
    
    public static float acos(final float r) {
        return (float)java.lang.Math.acos(r);
    }
    
    public static double acos(final double r) {
        return java.lang.Math.acos(r);
    }
    
    public static float safeAcos(final float v) {
        if (v < -1.0f) {
            return 3.1415927f;
        }
        if (v > 1.0f) {
            return 0.0f;
        }
        return acos(v);
    }
    
    public static double safeAcos(final double v) {
        if (v < -1.0) {
            return 3.141592653589793;
        }
        if (v > 1.0) {
            return 0.0;
        }
        return acos(v);
    }
    
    private static double fastAtan2(final double y, final double x) {
        final double ax = (x >= 0.0) ? x : (-x);
        final double ay = (y >= 0.0) ? y : (-y);
        final double a = min(ax, ay) / max(ax, ay);
        final double s = a * a;
        double r = ((-0.0464964749 * s + 0.15931422) * s - 0.327622764) * s * a + a;
        if (ay > ax) {
            r = 1.57079637 - r;
        }
        if (x < 0.0) {
            r = 3.14159274 - r;
        }
        return (y >= 0.0) ? r : (-r);
    }
    
    public static float atan2(final float y, final float x) {
        return (float)java.lang.Math.atan2(y, x);
    }
    
    public static double atan2(final double y, final double x) {
        if (Options.FASTMATH) {
            return fastAtan2(y, x);
        }
        return java.lang.Math.atan2(y, x);
    }
    
    public static float asin(final float r) {
        return (float)java.lang.Math.asin(r);
    }
    
    public static double asin(final double r) {
        return java.lang.Math.asin(r);
    }
    
    public static float safeAsin(final float r) {
        return (r <= -1.0f) ? -1.5707964f : ((r >= 1.0f) ? 1.5707964f : asin(r));
    }
    
    public static double safeAsin(final double r) {
        return (r <= -1.0) ? -1.5707963267948966 : ((r >= 1.0) ? 1.5707963267948966 : asin(r));
    }
    
    public static float abs(final float r) {
        return java.lang.Math.abs(r);
    }
    
    public static double abs(final double r) {
        return java.lang.Math.abs(r);
    }
    
    static boolean absEqualsOne(final float r) {
        return (Float.floatToRawIntBits(r) & Integer.MAX_VALUE) == 0x3F800000;
    }
    
    static boolean absEqualsOne(final double r) {
        return (Double.doubleToRawLongBits(r) & Long.MAX_VALUE) == 0x3FF0000000000000L;
    }
    
    public static int abs(final int r) {
        return java.lang.Math.abs(r);
    }
    
    public static int max(final int x, final int y) {
        return java.lang.Math.max(x, y);
    }
    
    public static int min(final int x, final int y) {
        return java.lang.Math.min(x, y);
    }
    
    public static double min(final double a, final double b) {
        return (a < b) ? a : b;
    }
    
    public static float min(final float a, final float b) {
        return (a < b) ? a : b;
    }
    
    public static float max(final float a, final float b) {
        return (a > b) ? a : b;
    }
    
    public static double max(final double a, final double b) {
        return (a > b) ? a : b;
    }
    
    public static float clamp(final float a, final float b, final float val) {
        return max(a, min(b, val));
    }
    
    public static double clamp(final double a, final double b, final double val) {
        return max(a, min(b, val));
    }
    
    public static int clamp(final int a, final int b, final int val) {
        return max(a, min(b, val));
    }
    
    public static float toRadians(final float angles) {
        return (float)java.lang.Math.toRadians(angles);
    }
    
    public static double toRadians(final double angles) {
        return java.lang.Math.toRadians(angles);
    }
    
    public static double toDegrees(final double angles) {
        return java.lang.Math.toDegrees(angles);
    }
    
    public static double floor(final double v) {
        return java.lang.Math.floor(v);
    }
    
    public static float floor(final float v) {
        return (float)java.lang.Math.floor(v);
    }
    
    public static double ceil(final double v) {
        return java.lang.Math.ceil(v);
    }
    
    public static float ceil(final float v) {
        return (float)java.lang.Math.ceil(v);
    }
    
    public static long round(final double v) {
        return java.lang.Math.round(v);
    }
    
    public static int round(final float v) {
        return java.lang.Math.round(v);
    }
    
    public static double exp(final double a) {
        return java.lang.Math.exp(a);
    }
    
    public static boolean isFinite(final double d) {
        return abs(d) <= Double.MAX_VALUE;
    }
    
    public static boolean isFinite(final float f) {
        return abs(f) <= Float.MAX_VALUE;
    }
    
    public static float fma(final float a, final float b, final float c) {
        if (Runtime.HAS_Math_fma) {
            return java.lang.Math.fma(a, b, c);
        }
        return a * b + c;
    }
    
    public static double fma(final double a, final double b, final double c) {
        if (Runtime.HAS_Math_fma) {
            return java.lang.Math.fma(a, b, c);
        }
        return a * b + c;
    }
    
    public static int roundUsing(final float v, final int mode) {
        switch (mode) {
            case 0: {
                return (int)v;
            }
            case 1: {
                return (int)java.lang.Math.ceil(v);
            }
            case 2: {
                return (int)java.lang.Math.floor(v);
            }
            case 4: {
                return roundHalfDown(v);
            }
            case 5: {
                return roundHalfUp(v);
            }
            case 3: {
                return roundHalfEven(v);
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    public static int roundUsing(final double v, final int mode) {
        switch (mode) {
            case 0: {
                return (int)v;
            }
            case 1: {
                return (int)java.lang.Math.ceil(v);
            }
            case 2: {
                return (int)java.lang.Math.floor(v);
            }
            case 4: {
                return roundHalfDown(v);
            }
            case 5: {
                return roundHalfUp(v);
            }
            case 3: {
                return roundHalfEven(v);
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }
    
    public static float lerp(final float a, final float b, final float t) {
        return fma(b - a, t, a);
    }
    
    public static double lerp(final double a, final double b, final double t) {
        return fma(b - a, t, a);
    }
    
    public static float biLerp(final float q00, final float q10, final float q01, final float q11, final float tx, final float ty) {
        final float lerpX1 = lerp(q00, q10, tx);
        final float lerpX2 = lerp(q01, q11, tx);
        return lerp(lerpX1, lerpX2, ty);
    }
    
    public static double biLerp(final double q00, final double q10, final double q01, final double q11, final double tx, final double ty) {
        final double lerpX1 = lerp(q00, q10, tx);
        final double lerpX2 = lerp(q01, q11, tx);
        return lerp(lerpX1, lerpX2, ty);
    }
    
    public static float triLerp(final float q000, final float q100, final float q010, final float q110, final float q001, final float q101, final float q011, final float q111, final float tx, final float ty, final float tz) {
        final float x00 = lerp(q000, q100, tx);
        final float x2 = lerp(q010, q110, tx);
        final float x3 = lerp(q001, q101, tx);
        final float x4 = lerp(q011, q111, tx);
        final float y0 = lerp(x00, x2, ty);
        final float y2 = lerp(x3, x4, ty);
        return lerp(y0, y2, tz);
    }
    
    public static double triLerp(final double q000, final double q100, final double q010, final double q110, final double q001, final double q101, final double q011, final double q111, final double tx, final double ty, final double tz) {
        final double x00 = lerp(q000, q100, tx);
        final double x2 = lerp(q010, q110, tx);
        final double x3 = lerp(q001, q101, tx);
        final double x4 = lerp(q011, q111, tx);
        final double y0 = lerp(x00, x2, ty);
        final double y2 = lerp(x3, x4, ty);
        return lerp(y0, y2, tz);
    }
    
    public static int roundHalfEven(final float v) {
        return (int)java.lang.Math.rint(v);
    }
    
    public static int roundHalfDown(final float v) {
        return (v > 0.0f) ? ((int)java.lang.Math.ceil(v - 0.5)) : ((int)java.lang.Math.floor(v + 0.5));
    }
    
    public static int roundHalfUp(final float v) {
        return (v > 0.0f) ? ((int)java.lang.Math.floor(v + 0.5)) : ((int)java.lang.Math.ceil(v - 0.5));
    }
    
    public static int roundHalfEven(final double v) {
        return (int)java.lang.Math.rint(v);
    }
    
    public static int roundHalfDown(final double v) {
        return (v > 0.0) ? ((int)java.lang.Math.ceil(v - 0.5)) : ((int)java.lang.Math.floor(v + 0.5));
    }
    
    public static int roundHalfUp(final double v) {
        return (v > 0.0) ? ((int)java.lang.Math.floor(v + 0.5)) : ((int)java.lang.Math.ceil(v - 0.5));
    }
    
    public static double random() {
        return java.lang.Math.random();
    }
    
    public static double signum(final double v) {
        return java.lang.Math.signum(v);
    }
    
    public static float signum(final float v) {
        return java.lang.Math.signum(v);
    }
    
    public static int signum(final int v) {
        final int r = Integer.signum(v);
        return r;
    }
    
    public static int signum(final long v) {
        final int r = Long.signum(v);
        return r;
    }
    
    static {
        lookupBits = Options.SIN_LOOKUP_BITS;
        lookupTableSize = 1 << Math.lookupBits;
        lookupTableSizeMinus1 = Math.lookupTableSize - 1;
        lookupTableSizeWithMargin = Math.lookupTableSize + 1;
        pi2OverLookupSize = 6.2831855f / Math.lookupTableSize;
        lookupSizeOverPi2 = Math.lookupTableSize / 6.2831855f;
        if (Options.FASTMATH && Options.SIN_LOOKUP) {
            sinTable = new float[Math.lookupTableSizeWithMargin];
            for (int i = 0; i < Math.lookupTableSizeWithMargin; ++i) {
                final double d = i * Math.pi2OverLookupSize;
                Math.sinTable[i] = (float)java.lang.Math.sin(d);
            }
        }
        else {
            sinTable = null;
        }
        c1 = Double.longBitsToDouble(-4628199217061079772L);
        c2 = Double.longBitsToDouble(4575957461383582011L);
        c3 = Double.longBitsToDouble(-4671919876300759001L);
        c4 = Double.longBitsToDouble(4523617214285661942L);
        c5 = Double.longBitsToDouble(-4730215272828025532L);
        c6 = Double.longBitsToDouble(4460272573143870633L);
        c7 = Double.longBitsToDouble(-4797767418267846529L);
        s5 = Double.longBitsToDouble(4523227044276562163L);
        s4 = Double.longBitsToDouble(-4671934770969572232L);
        s3 = Double.longBitsToDouble(4575957211482072852L);
        s2 = Double.longBitsToDouble(-4628199223918090387L);
        s1 = Double.longBitsToDouble(4607182418589157889L);
        k1 = Double.longBitsToDouble(-4628199217061079959L);
        k2 = Double.longBitsToDouble(4575957461383549981L);
        k3 = Double.longBitsToDouble(-4671919876307284301L);
        k4 = Double.longBitsToDouble(4523617213632129738L);
        k5 = Double.longBitsToDouble(-4730215344060517252L);
        k6 = Double.longBitsToDouble(4460268259291226124L);
        k7 = Double.longBitsToDouble(-4798040743777455072L);
    }
}
