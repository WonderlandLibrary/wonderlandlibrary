package com.initial.utils.movement;

import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;

public final class RotationUtils
{
    private static final Minecraft mc;
    
    public static float[] getRotsByPos(final double posX, final double posY, final double posZ) {
        final EntityPlayerSP player = RotationUtils.mc.thePlayer;
        final double x = posX - player.posX;
        final double y = posY - player.posY + player.getEyeHeight();
        final double z = posZ - player.posZ;
        final double dist = MathHelper.sqrt_double(x * x + z * z);
        final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static Vec3 getVectorToEntity(final Entity e) {
        final float[] rots = getRotsByPos(e.posX, e.posY, e.posZ);
        final float yaw = rots[0];
        final float pitch = rots[1];
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3(f2 * f3, f4, f * f3);
    }
    
    public static float getYawToEntity(final Entity entity) {
        final EntityPlayerSP player = RotationUtils.mc.thePlayer;
        return getYawBetween(player.rotationYaw, player.posX, player.posZ, entity.posX, entity.posZ);
    }
    
    public static float getYawBetween(final float yaw, final double srcX, final double srcZ, final double destX, final double destZ) {
        final double xDist = destX - srcX;
        final double zDist = destZ - srcZ;
        final float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
        return yaw + MathHelper.wrapAngleTo180_float(var1 - yaw);
    }
    
    public static float[] getRotationsWithDir(final EntityLivingBase entityIn, final float hSpeed, final float vSpeed, final float oldYaw, final float oldPitch) {
        final float yaw = updateRotation(oldYaw, getNeededRotations(entityIn)[0], hSpeed);
        final float pitch = updateRotation(oldPitch, getNeededRotations(entityIn)[1], vSpeed);
        return new float[] { yaw, pitch };
    }
    
    public static float[] getRotations(final EntityLivingBase entityIn, final float speed, final float oldYaw, final float oldPitch) {
        final float yaw = updateRotation(oldYaw, getNeededRotations(entityIn)[0], speed);
        final float pitch = updateRotation(oldPitch, getNeededRotations(entityIn)[1], speed);
        return new float[] { yaw, pitch };
    }
    
    private static float updateRotation(final float currentRotation, final float intendedRotation, final float increment) {
        float f = MathHelper.wrapAngleTo180_float(intendedRotation - currentRotation);
        if (f > increment) {
            f = increment;
        }
        if (f < -increment) {
            f = -increment;
        }
        return currentRotation + f;
    }
    
    public static float[] getNeededRotations(final EntityLivingBase entityIn) {
        final double d0 = entityIn.posX - RotationUtils.mc.thePlayer.posX;
        final double d2 = entityIn.posZ - RotationUtils.mc.thePlayer.posZ;
        final double d3 = entityIn.posY + entityIn.getEyeHeight() - RotationUtils.mc.thePlayer.getEntityBoundingBox().minY + RotationUtils.mc.thePlayer.getEyeHeight();
        final double d4 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        final float f = (float)(MathHelper.func_181159_b(d2, d0) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(-(MathHelper.func_181159_b(d3, d4) * 180.0 / 3.141592653589793));
        return new float[] { f, f2 };
    }
    
    public static float[] getRotationsWithDir(final double x, final double y, final double z, final float hSpeed, final float vSpeed, final float oldYaw, final float oldPitch) {
        final float yaw = updateRotation2(oldYaw, getNeededRotations(x, y, z)[0], hSpeed);
        final float pitch = updateRotation2(oldPitch, getNeededRotations(x, y, z)[1], vSpeed);
        return new float[] { yaw, pitch };
    }
    
    private static float updateRotation2(final float currentRotation, final float intendedRotation, final float increment) {
        float f = MathHelper.wrapAngleTo180_float(intendedRotation - currentRotation);
        if (f > increment) {
            f = increment;
        }
        if (f < -increment) {
            f = -increment;
        }
        return currentRotation + f;
    }
    
    public static float[] getNeededRotations(final double x, final double y, final double z) {
        final double d0 = x - RotationUtils.mc.thePlayer.posX;
        final double d2 = z - RotationUtils.mc.thePlayer.posZ;
        final double d3 = y - RotationUtils.mc.thePlayer.getEntityBoundingBox().minY + RotationUtils.mc.thePlayer.getEyeHeight();
        final double d4 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        final float f = (float)(MathHelper.func_181159_b(d2, d0) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(-(MathHelper.func_181159_b(d3, d4) * 180.0 / 3.141592653589793));
        return new float[] { f, f2 };
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
