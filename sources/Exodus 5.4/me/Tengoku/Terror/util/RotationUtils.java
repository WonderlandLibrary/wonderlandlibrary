/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class RotationUtils {
    protected static Minecraft mc = Minecraft.getMinecraft();

    public static float[] getRotations(Entity entity) {
        double d;
        double d2 = entity.posX - Minecraft.thePlayer.posX;
        double d3 = entity.posZ - Minecraft.thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            d = entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() - Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
        } else {
            d = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
        }
        double d4 = MathHelper.sqrt_double(d2 * d2 + d3 * d3);
        float f = (float)(Math.atan2(d3, d2) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d - (entity instanceof EntityPlayer ? 0.25 : 0.0), d4) * 180.0 / Math.PI));
        float f3 = RotationUtils.changeRotation(Minecraft.thePlayer.rotationPitch, f2);
        float f4 = RotationUtils.changeRotation(Minecraft.thePlayer.rotationYaw, f);
        return new float[]{f4, f3};
    }

    public static float getYawToEntity(Entity entity, boolean bl) {
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        double d = (bl ? entity.prevPosX : entity.posX) - (bl ? entityPlayerSP.prevPosX : entityPlayerSP.posX);
        double d2 = (bl ? entity.prevPosZ : entity.posZ) - (bl ? entityPlayerSP.prevPosZ : entityPlayerSP.posZ);
        float f = bl ? Minecraft.thePlayer.prevRotationYaw : Minecraft.thePlayer.rotationYaw;
        float f2 = (float)(Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        return f + MathHelper.wrapAngleTo180_float(f2 - f);
    }

    public static float changeRotation(float f, float f2) {
        float f3 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f3 > 1000.0f) {
            f3 = 1000.0f;
        }
        if (f3 < -1000.0f) {
            f3 = -1000.0f;
        }
        return f + f3;
    }

    public static float[] faceTarget(Entity entity, float f, float f2, boolean bl) {
        double d;
        Object object;
        double d2 = entity.posX - Minecraft.thePlayer.posX;
        double d3 = entity.posZ - Minecraft.thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            object = (EntityLivingBase)entity;
            d = ((EntityLivingBase)object).posY + (double)((Entity)object).getEyeHeight() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        } else {
            d = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        }
        object = new Random();
        double d4 = MathHelper.sqrt_double(d2 * d2 + d3 * d3);
        float f3 = (float)(Math.atan2(d3, d2) * 180.0 / Math.PI) - 90.0f;
        float f4 = (float)(-(Math.atan2(d - (entity instanceof EntityPlayer ? 0.25 : 0.0), d4) * 180.0 / Math.PI));
        float f5 = RotationUtils.changeRotation(Minecraft.thePlayer.rotationPitch, f4, f2);
        float f6 = RotationUtils.changeRotation(Minecraft.thePlayer.rotationYaw, f3, f);
        return new float[]{f6, f5};
    }

    public static float roundTo360(float f) {
        float f2 = f;
        while (f2 > 360.0f) {
            f2 -= 360.0f;
        }
        return f2;
    }

    public static float getYawChange(float f, double d, double d2) {
        Minecraft.getMinecraft();
        double d3 = d - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double d4 = d2 - Minecraft.thePlayer.posZ;
        double d5 = 0.0;
        if (d4 < 0.0 && d3 < 0.0) {
            if (d3 != 0.0) {
                d5 = 90.0 + Math.toDegrees(Math.atan(d4 / d3));
            }
        } else if (d4 < 0.0 && d3 > 0.0) {
            if (d3 != 0.0) {
                d5 = -90.0 + Math.toDegrees(Math.atan(d4 / d3));
            }
        } else if (d4 != 0.0) {
            d5 = Math.toDegrees(-Math.atan(d3 / d4));
        }
        return MathHelper.wrapAngleTo180_float(-(f - (float)d5));
    }

    public static float changeRotation(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4;
    }
}

