// 
// Decompiled by Procyon v0.5.36
// 

package org.joml.sampling;

class Math extends org.joml.Math
{
    static final double PI = 3.141592653589793;
    static final double PI2 = 6.283185307179586;
    static final double PIHalf = 1.5707963267948966;
    private static final double ONE_OVER_PI = 0.3183098861837907;
    private static final double s5;
    private static final double s4;
    private static final double s3;
    private static final double s2;
    private static final double s1;
    
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
    
    static {
        s5 = Double.longBitsToDouble(4523227044276562163L);
        s4 = Double.longBitsToDouble(-4671934770969572232L);
        s3 = Double.longBitsToDouble(4575957211482072852L);
        s2 = Double.longBitsToDouble(-4628199223918090387L);
        s1 = Double.longBitsToDouble(4607182418589157889L);
    }
}
