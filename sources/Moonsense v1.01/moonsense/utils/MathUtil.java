// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import net.minecraft.util.MathHelper;

public class MathUtil
{
    private static final double[] a;
    private static final double[] b;
    
    static {
        a = new double[65536];
        b = new double[360];
        for (int i = 0; i < 65536; ++i) {
            MathUtil.a[i] = Math.sin(i * 3.141592653589793 * 2.0 / 65536.0);
        }
        for (int i = 0; i < 360; ++i) {
            MathUtil.b[i] = Math.sin(Math.toRadians(i));
        }
    }
    
    public static double getAngle(int nameInt) {
        nameInt %= 360;
        return MathUtil.b[nameInt];
    }
    
    public static double getRightAngle(int nameInt) {
        nameInt += 90;
        nameInt %= 360;
        return MathUtil.b[nameInt];
    }
    
    private static float snapToStep(float value, final float valueStep) {
        if (valueStep > 0.0f) {
            value = valueStep * Math.round(value / valueStep);
        }
        return value;
    }
    
    public static float normalizeValue(final float value, final float valueMin, final float valueMax, final float valueStep) {
        return MathHelper.clamp_float((snapToStepClamp(value, valueMin, valueMax, valueStep) - valueMin) / (valueMax - valueMin), 0.0f, 1.0f);
    }
    
    private static float snapToStepClamp(float value, final float valueMin, final float valueMax, final float valueStep) {
        value = snapToStep(value, valueStep);
        return MathHelper.clamp_float(value, valueMin, valueMax);
    }
    
    public static float denormalizeValue(final float value, final float valueMin, final float valueMax, final float valueStep) {
        return snapToStepClamp(valueMin + (valueMax - valueMin) * MathHelper.clamp_float(value, 0.0f, 1.0f), valueMin, valueMax, valueStep);
    }
    
    public static double lIlIlIlIlIIlIIlIIllIIIIIl(final double d, final double d2, final double d3) {
        return Math.max(d2, Math.min(d, d3));
    }
    
    public static int lIlIlIlIlIIlIIlIIllIIIIIl(final int n, final int n2, final int n3) {
        return Math.max(n2, Math.min(n, n3));
    }
    
    public static int lIlIlIlIlIIlIIlIIllIIIIIl(final double d) {
        final int n = (int)d;
        return (d > n) ? (n + 1) : n;
    }
    
    public static int IlllIIIIIIlllIlIIlllIlIIl(final double d) {
        final int n = (int)d;
        return (d < n) ? (n - 1) : n;
    }
    
    public static float lIlIlIlIlIIlIIlIIllIIIIIl(float f) {
        if ((f %= 360.0f) >= 180.0f) {
            f -= 360.0f;
        }
        if (f < -180.0f) {
            f += 360.0f;
        }
        return f;
    }
    
    public static double lIllIlIIIlIIIIIIIlllIlIll(double d) {
        if ((d %= 360.0) >= 180.0) {
            d -= 360.0;
        }
        if (d < -180.0) {
            d += 360.0;
        }
        return d;
    }
    
    public static double llIlllIIIllllIIlllIllIIIl(final double d) {
        return d - Math.floor(d);
    }
    
    public static float lIlIlIlIlIIlIIlIIllIIIIIl(final float f, final float f2, final float f3) {
        return (f3 >= 1.0f) ? f2 : ((f3 <= 0.0f) ? f : (f + (f2 - f) * f3));
    }
}
