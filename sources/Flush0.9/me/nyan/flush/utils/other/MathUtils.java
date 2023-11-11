package me.nyan.flush.utils.other;

import net.minecraft.util.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtils {
    private static final Random random = new Random();

    public static double getRandomNumber(double max, double min) {
        return random.nextDouble() * (max - min) + min;
    }

    public static int getRandomNumber(int max, int min) {
        return Math.round(random.nextFloat() * (max - min) + min);
    }

    public static double round(double value, int scale) {
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static double snapToStep(double value, double valueStep) {
        if (valueStep == 0) {
            return value;
        }
        double fix = 100000000000D;
        return Math.round(Math.round(value / valueStep) * valueStep * fix) / fix;
    }

    public static float toRadians(float angdeg) {
        return angdeg / 180F * MathHelper.PI;
    }

    public static float toDegrees(float angrad) {
        return angrad * 180F / MathHelper.PI;
    }

    public static float clamp(float num, float min, float max) {
        return Math.max(Math.min(num, max), min);
    }

    public static int clamp(int num, int min, int max) {
        return Math.max(Math.min(num, max), min);
    }
}
