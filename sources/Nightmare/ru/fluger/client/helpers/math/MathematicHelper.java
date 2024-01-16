// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.math;

import java.math.RoundingMode;
import java.math.BigDecimal;
import ru.fluger.client.helpers.Helper;

public class MathematicHelper implements Helper
{
    public static int middleRandomize(final int max, final int min) {
        return (int)(Math.random() * (max - min)) + min;
    }
    
    public static int spikeRandomize(final int max, final int min) {
        return -min + (int)(Math.random() * (max - -min + 1));
    }
    
    public static double spikeRandomize(final double max, final double min) {
        return -min + Math.random() * (max - -min + 1.0);
    }
    
    public static BigDecimal round(final float f, final int times) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(times, 4);
        return bd;
    }
    
    public static float abs(final float num) {
        return (num < 0.0f) ? (0.0f - num) : num;
    }
    
    public static float nabs(final float num) {
        return abs(num) * -1.0f;
    }
    
    public static double round(final double num, final double increment) {
        final double v = Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static float checkAngle(final float oneVar, final float twoVar, final float threeVar) {
        float f = rk.g(oneVar - twoVar);
        if (f < -threeVar) {
            f = -threeVar;
        }
        if (f >= threeVar) {
            f = threeVar;
        }
        return oneVar - f;
    }
    
    public static float lerp(final float a, final float b, final float f) {
        return a + f * (b - a);
    }
    
    public static float clamp(float val, final float min, final float max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }
    
    public static float randomizeFloat(final float startInclusive, final float endInclusive) {
        if (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0f) {
            return startInclusive;
        }
        return (float)(startInclusive + (endInclusive - startInclusive) * Math.random());
    }
}
