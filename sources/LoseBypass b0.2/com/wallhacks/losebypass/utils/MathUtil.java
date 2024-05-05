/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class MathUtil {
    public static float calculateGaussianValue(float x, float s) {
        double output = 1.0 / Math.sqrt(Math.PI * 2 * (double)(s * s));
        return (float)(output * Math.exp((double)(-(x * x)) / (2.0 * (double)(s * s))));
    }

    public static float clamp(float num, float min, float max) {
        if (!(num < min)) return Math.min(num, max);
        return min;
    }

    public static int clamp(int num, int min, int max) {
        if (num >= min) return Math.min(num, max);
        return min;
    }

    public static double clamp(double num, double min, double max) {
        if (!(num < min)) return Math.min(num, max);
        return min;
    }

    public static double lerp(double current, double target, double lerp) {
        current -= (current - target) * MathUtil.clamp(lerp, 0.0, 1.0);
        return current;
    }

    public static float wrapDegrees(float value) {
        if ((value %= 360.0f) >= 180.0f) {
            value -= 360.0f;
        }
        if (!(value < -180.0f)) return value;
        value += 360.0f;
        return value;
    }

    public static double wrapDegrees(double value) {
        if ((value %= 360.0) >= 180.0) {
            value -= 360.0;
        }
        if (!(value < -180.0)) return value;
        value += 360.0;
        return value;
    }

    public static int wrapDegrees(int angle) {
        if ((angle %= 360) >= 180) {
            angle -= 360;
        }
        if (angle >= -180) return angle;
        angle += 360;
        return angle;
    }

    public static Vec3 getCenter(AxisAlignedBB alignedBB) {
        double centerX = alignedBB.minX + (alignedBB.maxX - alignedBB.minX) / 2.0;
        double centerZ = alignedBB.minZ + (alignedBB.maxZ - alignedBB.minZ) / 2.0;
        double centerY = alignedBB.minY + (alignedBB.maxY - alignedBB.minY) / 2.0;
        return new Vec3(centerX, centerY, centerZ);
    }

    public static Vec3 getEyesPos(EntityPlayer player) {
        return new Vec3(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
    }

    public static double roundAvoid(double value, int places) {
        double scale = Math.pow(10.0, places);
        return (double)Math.round(value * scale) / scale;
    }
}

