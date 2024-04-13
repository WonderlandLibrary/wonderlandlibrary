package com.canon.majik.api.utils.turok.util;

import net.minecraft.util.math.Vec3d;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author SrRina
 * @since 21/10/2020
 */
public class TurokMath {
    public static int clamp(int value, int minimum, int maximum) {
        return value <= minimum ? minimum : value >= maximum ? maximum : value;
    }

    public static double clamp(double value, double minimum, double maximum) {
        return value <= minimum ? minimum : value >= maximum ? maximum : value;
    }

    public static float clamp(float value, float minimum, float maximum) {
        return value <= minimum ? minimum : value >= maximum ? maximum : value;
    }

    public static double round(double vDouble) {
        BigDecimal decimal = new BigDecimal(vDouble);

        decimal = decimal.setScale(2, RoundingMode.HALF_UP);

        return decimal.doubleValue();
    }

    public static Vec3d lerp(Vec3d a, Vec3d b, float ticks) {
        return new Vec3d(
            a.x + (b.x - a.x) * ticks,
            a.y + (b.y - a.y) * ticks,
            a.z + (b.z - a.z) * ticks
        );
    }

    public static float lerp(int a, int b, float ticks) {
        return (a + (b - a) * ticks);
    }

    public static float lerp(float a, float b, float ticks) {
        return (a + (b - a) * ticks);
    }

    public static int normalize(int... value) {
        int normalizedValue = 0;
        int cachedValue = 0;

        for (int values : value) {
            cachedValue = values;

            normalizedValue = values / cachedValue * cachedValue;
        }

        return normalizedValue;
    }

    public static double normalize(double... value) {
        double normalizedValue = 0;
        double cachedValue = 0;

        for (double values : value) {
            cachedValue = values;

            normalizedValue = values / cachedValue * cachedValue;
        }

        return normalizedValue;
    }

    public static float normalize(float... value) {
        float normalizedValue = 0;
        float cachedValue = 0;

        for (float values : value) {
            cachedValue = values;

            normalizedValue = values / cachedValue * cachedValue;
        }

        return normalizedValue;
    }

    public static int ceiling(double value) {
        int valueInt = (int) value;

        return value >= (double) valueInt ? valueInt + 1 : valueInt;
    }

    public static int ceiling(float value) {
        int valueInt = (int) value;

        return value >= (float) valueInt ? valueInt + 1 : valueInt;
    }

    public static double sqrt(double a) {
        return Math.sqrt(a);
    }

    public static float sqrt(float a) {
        return (float) Math.sqrt(a);
    }

    public static int sqrt(int a) {
        return (int) Math.sqrt(a);
    }

    public static int min(int value, int minimum) {
        return value <= minimum ? minimum : value;
    }

    public static float min(float value, float minimum) {
        return value <= minimum ? minimum : value;
    }

    public static double min(double value, double minimum) {
        return value <= minimum ? minimum : value;
    }

    public static int max(int value, int maximum) {
        return value >= maximum ? maximum : value;
    }

    public static double max(double value, double maximum) {
        return value >= maximum ? maximum : value;
    }

    public static float max(float value, float maximum) {
        return value >= maximum ? maximum : value;
    }
}