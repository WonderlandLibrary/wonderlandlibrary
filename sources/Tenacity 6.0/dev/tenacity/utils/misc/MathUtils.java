// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.misc;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import java.security.SecureRandom;
import java.text.DecimalFormat;

public class MathUtils
{
    public static final DecimalFormat DF_0;
    public static final DecimalFormat DF_1;
    public static final DecimalFormat DF_2;
    public static final DecimalFormat DF_1D;
    public static final DecimalFormat DF_2D;
    public static final SecureRandom secureRandom;
    
    public static int getRandomInRange(final int min, final int max) {
        return (int)(Math.random() * (max - min) + min);
    }
    
    public static double[] yawPos(final double value) {
        return yawPos(Minecraft.getMinecraft().thePlayer.rotationYaw * MathHelper.deg2Rad, value);
    }
    
    public static double[] yawPos(final float yaw, final double value) {
        return new double[] { -MathHelper.sin(yaw) * value, MathHelper.cos(yaw) * value };
    }
    
    public static float getRandomInRange(final float min, final float max) {
        final SecureRandom random = new SecureRandom();
        return random.nextFloat() * (max - min) + min;
    }
    
    public static double getRandomInRange(final double min, final double max) {
        final SecureRandom random = new SecureRandom();
        return (min == max) ? min : (random.nextDouble() * (max - min) + min);
    }
    
    public static int getRandomNumberUsingNextInt(final int min, final int max) {
        final Random random = new Random();
        return random.nextInt(max - min) + min;
    }
    
    public static double lerp(final double old, final double newVal, final double amount) {
        return (1.0 - amount) * old + amount * newVal;
    }
    
    public static Double interpolate(final double oldValue, final double newValue, final double interpolationValue) {
        return oldValue + (newValue - oldValue) * interpolationValue;
    }
    
    public static float interpolateFloat(final float oldValue, final float newValue, final double interpolationValue) {
        return interpolate(oldValue, newValue, (float)interpolationValue).floatValue();
    }
    
    public static int interpolateInt(final int oldValue, final int newValue, final double interpolationValue) {
        return interpolate(oldValue, newValue, (float)interpolationValue).intValue();
    }
    
    public static float calculateGaussianValue(final float x, final float sigma) {
        final double output = 1.0 / Math.sqrt(6.283185307179586 * (sigma * sigma));
        return (float)(output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }
    
    public static double roundToHalf(final double d) {
        return Math.round(d * 2.0) / 2.0;
    }
    
    public static double round(final double num, final double increment) {
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale((int)increment, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static String round(final String value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.stripTrailingZeros();
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.toString();
    }
    
    public static float getRandomFloat(final float max, final float min) {
        final SecureRandom random = new SecureRandom();
        return random.nextFloat() * (max - min) + min;
    }
    
    public static int getNumberOfDecimalPlace(final double value) {
        final BigDecimal bigDecimal = new BigDecimal(value);
        return Math.max(0, bigDecimal.stripTrailingZeros().scale());
    }
    
    static {
        DF_0 = new DecimalFormat("0");
        DF_1 = new DecimalFormat("0.0");
        DF_2 = new DecimalFormat("0.00");
        DF_1D = new DecimalFormat("0.#");
        DF_2D = new DecimalFormat("0.##");
        secureRandom = new SecureRandom();
    }
}
