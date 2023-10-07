// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util;

import net.minecraft.util.MathHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityUtil implements MinecraftUtil
{
    public static void faceEntity(final EntityLivingBase entity) {
        final float[] rotations = getRotationsNeeded(entity);
        if (rotations != null) {
            EntityUtil.mc.thePlayer.rotationYaw = rotations[0];
            EntityUtil.mc.thePlayer.rotationPitch = rotations[1] + 1.0f;
        }
    }
    
    public static void faceEntitySilently(final Entity entity) {
        final float[] rotations = getRotationsNeeded(entity);
        if (rotations != null) {
            final float yaw = rotations[0];
            final float pitch = rotations[1] + 1.0f;
            EntityUtil.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, EntityUtil.mc.thePlayer.onGround));
        }
    }
    
    public static float getDistanceFromMouse(final Entity entity) {
        final float[] neededRotations = getRotationsNeeded(entity);
        if (neededRotations != null) {
            final float neededYaw = EntityUtil.mc.thePlayer.rotationYaw - neededRotations[0];
            final float neededPitch = EntityUtil.mc.thePlayer.rotationPitch - neededRotations[1];
            final float distanceFromMouse = MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch * neededPitch);
            return distanceFromMouse;
        }
        return -1.0f;
    }
    
    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffX = entity.posX - EntityUtil.mc.thePlayer.posX;
        final double diffZ = entity.posZ - EntityUtil.mc.thePlayer.posZ;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase elb = (EntityLivingBase)entity;
            diffY = elb.posY + elb.getEyeHeight() - (EntityUtil.mc.thePlayer.posY + EntityUtil.mc.thePlayer.getEyeHeight());
        }
        else {
            diffY = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / 2.0 - (EntityUtil.mc.thePlayer.posY + EntityUtil.mc.thePlayer.getEyeHeight());
        }
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { EntityUtil.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - EntityUtil.mc.thePlayer.rotationYaw), EntityUtil.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - EntityUtil.mc.thePlayer.rotationPitch) };
    }
}
