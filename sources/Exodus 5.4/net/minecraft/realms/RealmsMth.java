/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package net.minecraft.realms;

import java.util.Random;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;

public class RealmsMth {
    public static float sin(float f) {
        return MathHelper.sin(f);
    }

    public static double nextDouble(Random random, double d, double d2) {
        return MathHelper.getRandomDoubleInRange(random, d, d2);
    }

    public static int floor(float f) {
        return MathHelper.floor_float(f);
    }

    public static float wrapDegrees(float f) {
        return MathHelper.wrapAngleTo180_float(f);
    }

    public static int abs(int n) {
        return MathHelper.abs_int(n);
    }

    public static boolean isEmpty(String string) {
        return StringUtils.isEmpty((CharSequence)string);
    }

    public static float sqrt(double d) {
        return MathHelper.sqrt_double(d);
    }

    public static double getDouble(String string, double d, double d2) {
        return MathHelper.parseDoubleWithDefaultAndMax(string, d, d2);
    }

    public static int clamp(int n, int n2, int n3) {
        return MathHelper.clamp_int(n, n2, n3);
    }

    public static float nextFloat(Random random, float f, float f2) {
        return MathHelper.randomFloatClamp(random, f, f2);
    }

    public static int nextInt(Random random, int n, int n2) {
        return MathHelper.getRandomIntegerInRange(random, n, n2);
    }

    public static int floor(double d) {
        return MathHelper.floor_double(d);
    }

    public static int smallestEncompassingPowerOfTwo(int n) {
        return MathHelper.roundUpToPowerOfTwo(n);
    }

    public static int ceil(double d) {
        return MathHelper.ceiling_double_int(d);
    }

    public static double wrapDegrees(double d) {
        return MathHelper.wrapAngleTo180_double(d);
    }

    public static int getInt(String string, int n) {
        return MathHelper.parseIntWithDefault(string, n);
    }

    public static double clampedLerp(double d, double d2, double d3) {
        return MathHelper.denormalizeClamp(d, d2, d3);
    }

    public static long lfloor(double d) {
        return MathHelper.floor_double_long(d);
    }

    public static double absMax(double d, double d2) {
        return MathHelper.abs_max(d, d2);
    }

    public static int intFloorDiv(int n, int n2) {
        return MathHelper.bucketInt(n, n2);
    }

    public static int roundUp(int n, int n2) {
        return MathHelper.func_154354_b(n, n2);
    }

    public static float clamp(float f, float f2, float f3) {
        return MathHelper.clamp_float(f, f2, f3);
    }

    public static float cos(float f) {
        return MathHelper.cos(f);
    }

    public static double average(long[] lArray) {
        return MathHelper.average(lArray);
    }

    public static int fastFloor(double d) {
        return MathHelper.truncateDoubleToInt(d);
    }

    public static int absFloor(double d) {
        return MathHelper.func_154353_e(d);
    }

    public static int ceil(float f) {
        return MathHelper.ceiling_float_int(f);
    }

    public static double clamp(double d, double d2, double d3) {
        return MathHelper.clamp_double(d, d2, d3);
    }

    public static int log2(int n) {
        return MathHelper.calculateLogBaseTwo(n);
    }

    public static int getInt(String string, int n, int n2) {
        return MathHelper.parseIntWithDefaultAndMax(string, n, n2);
    }

    public static float sqrt(float f) {
        return MathHelper.sqrt_float(f);
    }

    public static double getDouble(String string, double d) {
        return MathHelper.parseDoubleWithDefault(string, d);
    }

    public static float abs(float f) {
        return MathHelper.abs(f);
    }
}

