/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import java.util.List;
import me.Tengoku.Terror.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class RotationUtil {
    public static float getYawChange(double d, double d2) {
        Minecraft.getMinecraft();
        double d3 = d - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double d4 = d2 - Minecraft.thePlayer.posZ;
        double d5 = d4 < 0.0 && d3 < 0.0 ? 90.0 + Math.toDegrees(Math.atan(d4 / d3)) : (d4 < 0.0 && d3 > 0.0 ? -90.0 + Math.toDegrees(Math.atan(d4 / d3)) : Math.toDegrees(-Math.atan(d3 / d4)));
        Minecraft.getMinecraft();
        return MathHelper.wrapAngleTo180_float(-(Minecraft.thePlayer.rotationYaw - (float)d5));
    }

    public static float getNewAngle(float f) {
        if ((f %= 360.0f) >= 180.0f) {
            f -= 360.0f;
        }
        if (f < -180.0f) {
            f += 360.0f;
        }
        return f;
    }

    public static float[] getBowAngles(Entity entity) {
        double d = entity.posX - entity.lastTickPosX;
        double d2 = entity.posZ - entity.lastTickPosZ;
        Minecraft.getMinecraft();
        double d3 = Minecraft.thePlayer.getDistanceToEntity(entity);
        d3 -= d3 % 0.8;
        double d4 = 1.0;
        double d5 = 1.0;
        boolean bl = entity.isSprinting();
        d4 = d3 / 0.8 * d * (bl ? 1.25 : 1.0);
        d5 = d3 / 0.8 * d2 * (bl ? 1.25 : 1.0);
        double d6 = entity.posX + d4;
        Minecraft.getMinecraft();
        double d7 = d6 - Minecraft.thePlayer.posX;
        double d8 = entity.posZ + d5;
        Minecraft.getMinecraft();
        double d9 = d8 - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        double d10 = Minecraft.thePlayer.posY;
        Minecraft.getMinecraft();
        double d11 = d10 + (double)Minecraft.thePlayer.getEyeHeight() - (entity.posY + (double)entity.getEyeHeight());
        Minecraft.getMinecraft();
        double d12 = Minecraft.thePlayer.getDistanceToEntity(entity);
        float f = (float)Math.toDegrees(Math.atan2(d9, d7)) - 90.0f;
        float f2 = (float)Math.toDegrees(Math.atan2(d11, d12));
        return new float[]{f, f2};
    }

    public static float[] getAverageRotations(List<EntityLivingBase> list) {
        double d = 0.0;
        double d2 = 0.0;
        double d3 = 0.0;
        for (Entity entity : list) {
            d += entity.posX;
            d2 += entity.boundingBox.maxY - 2.0;
            d3 += entity.posZ;
        }
        return new float[]{RotationUtil.getRotationFromPosition(d /= (double)list.size(), d3 /= (double)list.size(), d2 /= (double)list.size())[0], RotationUtil.getRotationFromPosition(d, d3, d2)[1]};
    }

    public static float getDistanceBetweenAngles(float f, float f2) {
        float f3 = Math.abs(f - f2) % 360.0f;
        if (f3 > 180.0f) {
            f3 = 360.0f - f3;
        }
        return f3;
    }

    public static float[] getRotationsEntity(EntityLivingBase entityLivingBase) {
        Minecraft.getMinecraft();
        return Minecraft.thePlayer.isMoving() ? RotationUtil.getRotations(entityLivingBase.posX + MathUtils.randomNumber(0.03, -0.03), entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() - 0.4 + MathUtils.randomNumber(0.07, -0.07), entityLivingBase.posZ + MathUtils.randomNumber(0.03, -0.03)) : RotationUtil.getRotations(entityLivingBase.posX, entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() - 0.4, entityLivingBase.posZ);
    }

    public static float[] getRotationFromPosition(double d, double d2, double d3) {
        Minecraft.getMinecraft();
        double d4 = d - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double d5 = d2 - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        double d6 = d3 - Minecraft.thePlayer.posY - 1.2;
        double d7 = MathHelper.sqrt_double(d4 * d4 + d5 * d5);
        float f = (float)(Math.atan2(d5, d4) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d6 * 2.0, d7) * 180.0 / Math.PI));
        return new float[]{f, f2};
    }

    public static float getTrajAngleSolutionLow(float f, float f2, float f3) {
        float f4 = 0.006f;
        float f5 = f3 * f3 * f3 * f3 - f4 * (f4 * (f * f) + 2.0f * f2 * (f3 * f3));
        return (float)Math.toDegrees(Math.atan(((double)(f3 * f3) - Math.sqrt(f5)) / (double)(f4 * f)));
    }

    public static float getPitchChange(Entity entity, double d) {
        double d2 = entity.posX;
        Minecraft.getMinecraft();
        double d3 = d2 - Minecraft.thePlayer.posX;
        double d4 = entity.posZ;
        Minecraft.getMinecraft();
        double d5 = d4 - Minecraft.thePlayer.posZ;
        double d6 = d - 2.2 + (double)entity.getEyeHeight();
        Minecraft.getMinecraft();
        double d7 = d6 - Minecraft.thePlayer.posY;
        double d8 = MathHelper.sqrt_double(d3 * d3 + d5 * d5);
        double d9 = -Math.toDegrees(Math.atan(d7 / d8));
        Minecraft.getMinecraft();
        return -MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)d9) - 2.5f;
    }

    public static float[] getRotations(double d, double d2, double d3) {
        Minecraft.getMinecraft();
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        double d4 = d - entityPlayerSP.posX;
        double d5 = d2 - (entityPlayerSP.posY + (double)entityPlayerSP.getEyeHeight());
        double d6 = d3 - entityPlayerSP.posZ;
        double d7 = MathHelper.sqrt_double(d4 * d4 + d6 * d6);
        float f = (float)(Math.atan2(d6, d4) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d5, d7) * 180.0 / Math.PI));
        return new float[]{f, f2};
    }
}

