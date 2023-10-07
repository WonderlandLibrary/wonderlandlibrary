/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.util;

import me.arithmo.util.MinecraftUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class EntityUtil
implements MinecraftUtil {
    public static void faceEntity(EntityLivingBase entity) {
        float[] rotations = EntityUtil.getRotationsNeeded(entity);
        if (rotations != null) {
            EntityUtil.mc.thePlayer.rotationYaw = rotations[0];
            EntityUtil.mc.thePlayer.rotationPitch = rotations[1] + 1.0f;
        }
    }

    public static void faceEntitySilently(Entity entity) {
        float[] rotations = EntityUtil.getRotationsNeeded(entity);
        if (rotations != null) {
            float yaw = rotations[0];
            float pitch = rotations[1] + 1.0f;
            EntityUtil.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, EntityUtil.mc.thePlayer.onGround));
        }
    }

    public static float getDistanceFromMouse(Entity entity) {
        float[] neededRotations = EntityUtil.getRotationsNeeded(entity);
        if (neededRotations != null) {
            float neededYaw = EntityUtil.mc.thePlayer.rotationYaw - neededRotations[0];
            float neededPitch = EntityUtil.mc.thePlayer.rotationPitch - neededRotations[1];
            float distanceFromMouse = MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch * neededPitch);
            return distanceFromMouse;
        }
        return -1.0f;
    }

    public static float[] getRotationsNeeded(Entity entity) {
        double diffY;
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - EntityUtil.mc.thePlayer.posX;
        double diffZ = entity.posZ - EntityUtil.mc.thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + (double)elb.getEyeHeight() - (EntityUtil.mc.thePlayer.posY + (double)EntityUtil.mc.thePlayer.getEyeHeight());
        } else {
            diffY = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0 - (EntityUtil.mc.thePlayer.posY + (double)EntityUtil.mc.thePlayer.getEyeHeight());
        }
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{EntityUtil.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - EntityUtil.mc.thePlayer.rotationYaw), EntityUtil.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - EntityUtil.mc.thePlayer.rotationPitch)};
    }
}

