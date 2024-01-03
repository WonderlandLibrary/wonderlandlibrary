package de.theBest.MythicX.utils;

import de.theBest.MythicX.MythicX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import java.util.Random;

public class RotationUtil {
    public static float yaw;
    public static float renderYawOffset;
    public static float pitch;
    public static boolean RotationInUse;
    public static float friction = 0;
    public static float strafe = 0;
    public static float forward = 0;
    public static float f1 = 0;
    public static float f2 = 0;

    public static float[] basicRotation(final EntityPlayerSP player, final EntityLivingBase target) {
        final double posX =  target.posX - player.posX ;
        final double posY =  target.posY + target.getEyeHeight() - (player.posY  + player.getEyeHeight() + 0.5);
        final double posZ =  target.posZ - player.posZ;
        final double var14 = MathHelper.sqrt_double(posX * posX + posZ * posZ);
        float yaw = (float) (Math.atan2(posZ, posX) * 180 / Math.PI) - 90;
        float pitch = (float) -(Math.atan2(posY, var14) * 180 / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static float[] Intave(EntityPlayerSP player, EntityLivingBase target) {
        float yawRandom = (float)MathHelper.getRandomDoubleInRange(new Random(), -0.2D, 0.2D);
        float pitchRandom = (float)MathHelper.getRandomDoubleInRange(new Random(), -0.02D, 0.02D);
        double posX = MythicX.setmgr.getSettingByName("AuraRandom").getValBoolean() ? (target.posX - player.posX + yawRandom) : (target.posX - player.posX);
        double posY = MythicX.setmgr.getSettingByName("AuraClampPitch").getValBoolean() ? (MathHelper.clamp_double(player.posY + player.getEyeHeight(), (target.getEntityBoundingBox()).minY, (target.getEntityBoundingBox()).maxY) - player.posY + player.getEyeHeight()) : (MythicX.setmgr.getSettingByName("AuraRandom").getValBoolean() ? (target.posY + target.getEyeHeight() - player.posY + player.getEyeHeight() - pitchRandom) : (target.posY + target.getEyeHeight() - player.posY + player.getEyeHeight()));
        double posZ = MythicX.setmgr.getSettingByName("AuraRandom").getValBoolean() ? (target.posZ - player.posZ - yawRandom) : (target.posZ - player.posZ);
        double var14 = MathHelper.sqrt_double(posX * posX + posZ * posZ);
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float)-(Math.atan2(posY, var14) * 180.0D / Math.PI);
        if (MythicX.setmgr.getSettingByName("AuraMouseSensiFix").getValBoolean()) {
            float f2 = (Minecraft.getMinecraft()).gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f3 = f2 * f2 * f2 * 1.2F;
            yaw -= yaw % f3;
            pitch -= pitch % f3 * f2;
        }
        return new float[] { yaw, pitch };
    }

    public static boolean isInRange(double before, float after, float max) {
        while (before > 360.0D)
            before -= 360.0D;
        while (before < 0.0D)
            before += 360.0D;
        while (after > 360.0F)
            after -= 360.0F;
        while (after < 0.0F)
            after += 360.0F;
        if (before > after) {
            if ((before - after > 180.0D) && (360.0D - before - after <= max))
                return true;
            return before - after <= max;
        } else {
            if ((after - before > 180.0D) && (360.0F - after - before <= max))
                return true;
            return after - before <= max;
        }
    }

    public static boolean setPitch(float p, float speed) {
        if (p > 90.0F) {
            p = 90.0F;
        } else if (p < -90.0F) {
            p = -90.0F;
        }
        if (Math.abs(pitch - p) <= speed || speed >= 360.0F) {
            pitch = p;
            return false;
        }
        if (p < pitch) {
            pitch -= speed;
        } else {
            pitch += speed;
        }
        return true;
    }

    public static void setRotation(float y, float p) {
        if (p > 90.0F) {
            p = 90.0F;
        } else if (p < -90.0F) {
            p = -90.0F;
        }
        yaw = y;
        pitch = p;
        RotationInUse = true;
    }

    public static boolean setYaw(float y, float speed) {
        setRotation(yaw, pitch);
        if (speed >= 360.0F) {
            yaw = y;
            return true;
        }
        if (isInRange(yaw, y, speed) || speed >= 360.0F) {
            yaw = y;
            return true;
        }
        if (getRotation(yaw, y) < 0) {
            yaw -= speed;
        } else {
            yaw += speed;
        }
        return false;
    }

    public static int getRotation(double before, float after) {
        while (before > 360.0D)
            before -= 360.0D;
        while (before < 0.0D)
            before += 360.0D;
        while (after > 360.0F)
            after -= 360.0F;
        while (after < 0.0F)
            after += 360.0F;
        if (before > after) {
            if (before - after > 180.0D)
                return 1;
            return -1;
        }
        if (after - before > 180.0D)
            return -1;
        return 1;
    }

    public static float updateRotation(float current, float calc, float maxDelta) {
        float f = MathHelper.wrapAngleTo180_float(calc - current);
        if (f > maxDelta) {
            f = maxDelta;
        }

        if (f < -maxDelta) {
            f = -maxDelta;
        }

        return current + f;
    }
}
