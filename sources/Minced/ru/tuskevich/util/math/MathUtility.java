// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.math;

import java.math.RoundingMode;
import java.math.BigDecimal;

public class MathUtility
{
    public static BigDecimal round(final float f, final int times) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(times, RoundingMode.HALF_UP);
        return bd;
    }
    
    public static float map(final float value, final float istart, final float istop, final float ostart, final float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }
    
    public static int intRandom(final int max, final int min) {
        return (int)(Math.random() * (max - min)) + min;
    }
    
    public static float interpolate(final float current, final float old, final float scale) {
        return current + (old - current) * clamp(scale, 0.0f, 1.0f);
    }
    
    public static float interpolateFloat(final float oldValue, final float newValue, final double interpolationValue) {
        return interpolate(oldValue, newValue, (float)interpolationValue);
    }
    
    public static float lerp(final float current, final float old, final float scale) {
        return current + (old - current) * clamp(scale, 0.0f, 1.0f);
    }
    
    public static int interpolateInt(final int oldValue, final int newValue, final double interpolationValue) {
        return (int)interpolate((float)oldValue, (float)newValue, (float)interpolationValue);
    }
    
    public static double lerp(final double current, final double old, final double scale) {
        return current + (old - current) * clamp((float)scale, 0.0f, 1.0f);
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static double interpolate(final double current, final double old, final double scale) {
        return old + (current - old) * scale;
    }
    
    public static float interpolate(final float current, final float old, final double scale) {
        return (float)interpolate(current, (double)old, scale);
    }
    
    public static int interpolate(final int current, final int old, final double scale) {
        return (int)interpolate(current, (double)old, scale);
    }
    
    public static int getCenter(final int width, final int rectWidth) {
        return width / 2 - rectWidth / 2;
    }
    
    public static double round(final double num, final double increment) {
        final double v = Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
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
    
    public static double clamp(double val, final double min, final double max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }
}
