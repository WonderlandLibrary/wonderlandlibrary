// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import net.minecraft.util.MathHelper;

public class MathUtils
{
    public static final float PI = 3.1415927f;
    public static final float PI2 = 6.2831855f;
    public static final float PId2 = 1.5707964f;
    private static final float[] ASIN_TABLE;
    
    public static float asin(final float value) {
        return MathUtils.ASIN_TABLE[(int)((value + 1.0f) * 32767.5) & 0xFFFF];
    }
    
    public static float acos(final float value) {
        return 1.5707964f - MathUtils.ASIN_TABLE[(int)((value + 1.0f) * 32767.5) & 0xFFFF];
    }
    
    public static int getAverage(final int[] vals) {
        if (vals.length <= 0) {
            return 0;
        }
        final int i = getSum(vals);
        final int j = i / vals.length;
        return j;
    }
    
    public static int getSum(final int[] vals) {
        if (vals.length <= 0) {
            return 0;
        }
        int i = 0;
        for (int j = 0; j < vals.length; ++j) {
            final int k = vals[j];
            i += k;
        }
        return i;
    }
    
    public static int roundDownToPowerOfTwo(final int val) {
        final int i = MathHelper.roundUpToPowerOfTwo(val);
        return (val == i) ? i : (i / 2);
    }
    
    public static boolean equalsDelta(final float f1, final float f2, final float delta) {
        return Math.abs(f1 - f2) <= delta;
    }
    
    public static float toDeg(final float angle) {
        return angle * 180.0f / MathHelper.PI;
    }
    
    public static float toRad(final float angle) {
        return angle / 180.0f * MathHelper.PI;
    }
    
    public static float roundToFloat(final double d) {
        return (float)(Math.round(d * 1.0E8) / 1.0E8);
    }
    
    static {
        ASIN_TABLE = new float[65536];
        for (int i = 0; i < 65536; ++i) {
            MathUtils.ASIN_TABLE[i] = (float)Math.asin(i / 32767.5 - 1.0);
        }
        for (int j = -1; j < 2; ++j) {
            MathUtils.ASIN_TABLE[(int)((j + 1.0) * 32767.5) & 0xFFFF] = (float)Math.asin(j);
        }
    }
}
