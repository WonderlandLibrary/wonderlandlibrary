/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Utils {
    private static final Random RANDOM = new Random();

    public static float[] getNeededRotations(Vec3 vec3) {
        Minecraft.getMinecraft();
        double d = Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double d2 = Minecraft.thePlayer.posY;
        Minecraft.getMinecraft();
        double d3 = d2 + (double)Minecraft.thePlayer.getEyeHeight();
        Minecraft.getMinecraft();
        Vec3 vec32 = new Vec3(d, d3, Minecraft.thePlayer.posZ);
        double d4 = vec3.xCoord - vec32.xCoord;
        double d5 = vec3.yCoord - vec32.yCoord;
        double d6 = vec3.zCoord - vec32.zCoord;
        double d7 = Math.sqrt(d4 * d4 + d6 * d6);
        float f = (float)Math.toDegrees(Math.atan2(d6, d4)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(d5, d7)));
        return new float[]{MathHelper.wrapAngleTo180_float(f), MathHelper.wrapAngleTo180_float(f2)};
    }

    public static float getDirection() {
        Minecraft minecraft = Minecraft.getMinecraft();
        float f = Minecraft.thePlayer.rotationYaw;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            f += 180.0f;
        }
        float f2 = 1.0f;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            f2 = -0.5f;
        } else if (Minecraft.thePlayer.moveForward > 0.0f) {
            f2 = 0.5f;
        }
        if (Minecraft.thePlayer.moveStrafing > 0.0f) {
            f -= 90.0f * f2;
        }
        if (Minecraft.thePlayer.moveStrafing < 0.0f) {
            f += 90.0f * f2;
        }
        return f *= (float)Math.PI / 180;
    }

    public static int random(int n, int n2) {
        return RANDOM.nextInt(n2 - n) + n;
    }

    public static Vec3 getRandomCenter(AxisAlignedBB axisAlignedBB) {
        return new Vec3(axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * 0.8 * Math.random(), axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * 1.0 * Math.random(), axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * 0.8 * Math.random());
    }
}

