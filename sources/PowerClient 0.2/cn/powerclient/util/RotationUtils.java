/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class RotationUtils {
    public static float[] getRotations(EntityLivingBase ent) {
        double x2 = ent.posX;
        double z2 = ent.posZ;
        double y2 = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
        return RotationUtils.getRotationFromPosition(x2, z2, y2);
    }

    public static float[] getAverageRotations(List<EntityLivingBase> targetList) {
        double posX = 0.0;
        double posY = 0.0;
        double posZ = 0.0;
        for (Entity ent : targetList) {
            posX += ent.posX;
            posY += ent.boundingBox.maxY - 2.0;
            posZ += ent.posZ;
        }
        return new float[]{RotationUtils.getRotationFromPosition(posX /= (double)targetList.size(), posZ /= (double)targetList.size(), posY /= (double)targetList.size())[0], RotationUtils.getRotationFromPosition(posX, posZ, posY)[1]};
    }

    public static float[] getBowAngles(Entity entity) {
        double xDelta = entity.posX - entity.lastTickPosX;
        double zDelta = entity.posZ - entity.lastTickPosZ;
        Minecraft.getMinecraft();
        double d2 = Minecraft.thePlayer.getDistanceToEntity(entity);
        d2 -= d2 % 0.8;
        double xMulti = 1.0;
        double zMulti = 1.0;
        boolean sprint = entity.isSprinting();
        xMulti = d2 / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
        zMulti = d2 / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
        Minecraft.getMinecraft();
        double x2 = entity.posX + xMulti - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double z2 = entity.posZ + zMulti - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        double y2 = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() - (entity.posY + (double)entity.getEyeHeight());
        Minecraft.getMinecraft();
        double dist = Minecraft.thePlayer.getDistanceToEntity(entity);
        float yaw = (float)Math.toDegrees(Math.atan2(z2, x2)) - 90.0f;
        float pitch = (float)Math.toDegrees(Math.atan2(y2, dist));
        return new float[]{yaw, pitch};
    }

    private static float[] getRotationFromPosition(double x2, double z2, double y2) {
        Minecraft.getMinecraft();
        double xDiff = x2 - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double zDiff = z2 - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        double yDiff = y2 - Minecraft.thePlayer.posY - 0.6;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
        float g2 = 0.006f;
        float sqrt = velocity * velocity * velocity * velocity - g2 * (g2 * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
        return (float)Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt(sqrt)) / (double)(g2 * d3)));
    }

    public static float getYawChange(double posX, double posZ) {
        Minecraft.getMinecraft();
        double deltaX = posX - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double deltaZ = posZ - Minecraft.thePlayer.posZ;
        double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(- Math.atan(deltaX / deltaZ)));
        Minecraft.getMinecraft();
        return MathHelper.wrapAngleTo180_float(- Minecraft.thePlayer.rotationYaw - (float)yawToEntity);
    }

    public static float getPitchChange(Entity entity, double posY) {
        Minecraft.getMinecraft();
        double deltaX = entity.posX - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        double deltaY = posY - 2.2 + (double)entity.getEyeHeight() - Minecraft.thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = - Math.toDegrees(Math.atan(deltaY / distanceXZ));
        Minecraft.getMinecraft();
        return - MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)pitchToEntity) - 2.5f;
    }

    public static float getNewAngle(float angle) {
        if ((angle %= 360.0f) >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle = Math.abs(angle1 - angle2) % 360.0f;
        if (angle > 180.0f) {
            angle = 360.0f - angle;
        }
        return angle;
    }
}

