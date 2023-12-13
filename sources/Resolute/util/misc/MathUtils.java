// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.misc;

import java.math.MathContext;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Random;

public class MathUtils
{
    private static Random rng;
    
    public static double randomNumber(final double max, final double min) {
        return Math.random() * (max - min) + min;
    }
    
    public static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static final double toPercentage(final double current, final double max) {
        return current / max;
    }
    
    public static double distance(final float x, final float y, final float x1, final float y1) {
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }
    
    public static double clamp(final double value, final double minimum, final double maximum) {
        return (value > maximum) ? maximum : ((value < minimum) ? minimum : value);
    }
    
    public static int getRandom(final int floor, final int cap) {
        return floor + getRNG().nextInt(cap - floor + 1);
    }
    
    public static Random getRNG() {
        return MathUtils.rng;
    }
    
    public static double square(double motionX) {
        motionX *= motionX;
        return motionX;
    }
    
    public static double roundToDecimalPlace(final double value, final double inc) {
        final double halfOfInc = inc / 2.0;
        final double floored = Math.floor(value / inc) * inc;
        if (value >= floored + halfOfInc) {
            return new BigDecimal(Math.ceil(value / inc) * inc, MathContext.DECIMAL64).stripTrailingZeros().doubleValue();
        }
        return new BigDecimal(floored, MathContext.DECIMAL64).stripTrailingZeros().doubleValue();
    }
    
    public static double getIncremental(final double val, final double inc) {
        final double one = 1.0 / inc;
        return Math.round(val * one) / one;
    }
    
    public static double preciseRound(final double value, final double precision) {
        final double scale = Math.pow(10.0, precision);
        return Math.round(value * scale) / scale;
    }
    
    public static double round(final double num, final double increment) {
        if (increment < 0.0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale((int)increment, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static float[] constrainAngle(final float[] vector) {
        vector[0] %= 360.0f;
        vector[1] %= 360.0f;
        while (vector[0] <= -180.0f) {
            vector[0] += 360.0f;
        }
        while (vector[1] <= -180.0f) {
            vector[1] += 360.0f;
        }
        while (vector[0] > 180.0f) {
            vector[0] -= 360.0f;
        }
        while (vector[1] > 180.0f) {
            vector[1] -= 360.0f;
        }
        return vector;
    }
    
    public static double roundWithPrecision(final double val, final int precision) {
        final int scale = (int)Math.pow(10.0, precision);
        return Math.round(val * scale) / (double)scale;
    }
    
    public static float randomFloatValue() {
        return (float)getRandomInRange(2.96219E-7, 9.13303E-6);
    }
    
    public static double getRandomInRange(final double min, final double max) {
        final Random random = new Random();
        final double range = max - min;
        final double scaled = random.nextDouble() * range;
        final double shifted = scaled + min;
        return shifted;
    }
    
    static {
        MathUtils.rng = new Random();
    }
}
