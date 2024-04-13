package dev.tenacity.util.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DecimalFormat;

public final class MathUtil {

    public static final DecimalFormat DF_0 = new DecimalFormat("0");
    public static final DecimalFormat DF_1 = new DecimalFormat("0.0");
    public static final DecimalFormat DF_2 = new DecimalFormat("0.00");

    public static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private MathUtil() {
    }

    public static int getRandomInRange(final int min, final int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static double[] yawPos(final double value) {
        return yawPos(Minecraft.getMinecraft().thePlayer.rotationYaw * MathHelper.deg2Rad, value);
    }

    public static double[] yawPos(final float yaw, final double value) {
        return new double[]{-MathHelper.sin(yaw) * value, MathHelper.cos(yaw) * value};
    }

    public static float getRandomInRange(final float min, final float max) {
        return SECURE_RANDOM.nextFloat() * (max - min) + min;
    }

    public static double getRandomInRange(final double min, final double max) {
        return min == max ? min : SECURE_RANDOM.nextDouble() * (max - min) + min;
    }

    public static double lerp(final double old, final double newVal, final double amount) {
        return (1.0 - amount) * old + amount * newVal;
    }

    public static Double interpolate(final double oldValue, final double newValue, final double interpolationValue){
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float interpolateFloat(final float oldValue, final float newValue, final double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).floatValue();
    }

    public static int interpolateInt(final int oldValue, final int newValue, final double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
    }

    public static float calculateGaussianValue(final float x, final float sigma) {
        final double output = 1.0 / Math.sqrt(2.0 * Math.PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }

    public static float getRandomFloat(final float max, final float min) {
        return SECURE_RANDOM.nextFloat() * (max - min) + min;
    }

    public static int getNumberOfDecimalPlace(final double value) {
        final BigDecimal bigDecimal = new BigDecimal(value);
        return Math.max(0, bigDecimal.stripTrailingZeros().scale());
    }

    public static double clamp(double value, final double min, final double max) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }

    public static double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }

    public static int nextInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


}
