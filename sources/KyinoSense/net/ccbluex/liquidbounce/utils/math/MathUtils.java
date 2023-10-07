/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.MathHelper
 */
package net.ccbluex.liquidbounce.utils.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.util.MathHelper;

public final class MathUtils
extends MinecraftInstance {
    private static Random random = new Random();

    public static double round(double num, double increment) {
        if (increment < 0.0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale((int)increment, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double roundToHalf(double d) {
        return (double)Math.round(d * 2.0) / 2.0;
    }

    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    public static float clampValue(float value, float floor, float cap) {
        if (value < floor) {
            return floor;
        }
        return Math.min(value, cap);
    }

    public static float toDegree(double x, double z) {
        double n = z - MathUtils.mc.field_71439_g.field_70161_v;
        return (float)(Math.atan2(n, x - MathUtils.mc.field_71439_g.field_70165_t) * 180.0 / Math.PI) - 90.0f;
    }

    public static double randomDouble2(double min, double max) {
        return MathHelper.func_151237_a((double)(min + random.nextDouble() * max), (double)min, (double)max);
    }

    public static double getRandomInRange(double max, double min) {
        return min + (max - min) * random.nextDouble();
    }

    public static double incValue(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round(val * one) / one;
    }

    public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
        return oldValue + (newValue - oldValue) * interpolationValue;
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
        return MathUtils.interpolate(oldValue, newValue, (float)interpolationValue).floatValue();
    }
}

