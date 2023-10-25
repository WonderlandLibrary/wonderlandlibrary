/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class BlockUtil {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static float getYaw(Entity entity) {
        double d = entity.posX - Minecraft.thePlayer.posX;
        double d2 = entity.posZ - Minecraft.thePlayer.posZ;
        double d3 = d2 < 0.0 && d < 0.0 ? 90.0 + Math.toDegrees(Math.atan(d2 / d)) : (d2 < 0.0 && d > 0.0 ? -90.0 + Math.toDegrees(Math.atan(d2 / d)) : Math.toDegrees(-Math.atan(d / d2)));
        return MathHelper.wrapAngleTo180_float(-(Minecraft.thePlayer.rotationYaw - (float)d3));
    }

    public static float getPitch(Entity entity) {
        double d = entity.posX - Minecraft.thePlayer.posX;
        double d2 = entity.posZ - Minecraft.thePlayer.posZ;
        double d3 = entity.posY - 1.6 + (double)entity.getEyeHeight() - Minecraft.thePlayer.posY;
        double d4 = MathHelper.sqrt_double(d * d + d2 * d2);
        double d5 = -Math.toDegrees(Math.atan(d3 / d4));
        return -MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)d5);
    }

    public static float[] getDirectionToBlock(int n, int n2, int n3, EnumFacing enumFacing) {
        EntityEgg entityEgg = new EntityEgg(Minecraft.theWorld);
        entityEgg.posX = (double)n + 0.5;
        entityEgg.posY = (double)n2 + 0.5;
        entityEgg.posZ = (double)n3 + 0.5;
        entityEgg.posX += (double)enumFacing.getDirectionVec().getX() * 0.25;
        entityEgg.posY += (double)enumFacing.getDirectionVec().getX() * 0.25;
        entityEgg.posZ += (double)enumFacing.getDirectionVec().getX() * 0.25;
        return BlockUtil.getDirectionToEntity(entityEgg);
    }

    public static float[] getDirectionToEntity(Entity entity) {
        return new float[]{BlockUtil.getYaw(entity) + Minecraft.thePlayer.rotationYaw, BlockUtil.getPitch(entity) + Minecraft.thePlayer.rotationPitch};
    }

    public static float[] getFacingRotations(BlockPos blockPos, EnumFacing enumFacing) {
        EntityXPOrb entityXPOrb = new EntityXPOrb(Minecraft.theWorld);
        entityXPOrb.posX = (double)blockPos.getX() + 0.5;
        entityXPOrb.posY = (double)blockPos.getY() + 0.5;
        entityXPOrb.posZ = (double)blockPos.getZ() + 0.5;
        entityXPOrb.posX += (double)enumFacing.getDirectionVec().getX() * (double)0.1f;
        entityXPOrb.posZ += (double)enumFacing.getDirectionVec().getZ() * (double)0.1f;
        entityXPOrb.posY += 0.5;
        return BlockUtil.getRotationsNeeded(entityXPOrb);
    }

    public static float[] getBlockRotations(double d, double d2, double d3) {
        double d4 = d - Minecraft.thePlayer.posX + 0.5;
        double d5 = d3 - Minecraft.thePlayer.posZ + 0.5;
        double d6 = d2 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() - 1.0);
        double d7 = MathHelper.sqrt_double(d4 * d4 + d5 * d5);
        float f = (float)(Math.atan2(d5, d4) * 180.0 / Math.PI) - 90.0f;
        return new float[]{f, (float)(-Math.atan2(d6, d7) * 180.0 / Math.PI)};
    }

    public static float[] getRotationNeededForBlock(EntityPlayer entityPlayer, BlockPos blockPos) {
        double d = (double)blockPos.getX() - entityPlayer.posX;
        double d2 = (double)blockPos.getY() + 0.5 - (entityPlayer.posY + (double)entityPlayer.getEyeHeight());
        double d3 = (double)blockPos.getZ() - entityPlayer.posZ;
        double d4 = Math.sqrt(d * d * d3 * d3);
        float f = (float)(Math.atan2(d3, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d2, d4) * 180.0 / Math.PI));
        return new float[]{f, f2};
    }

    private static float[] getRotationsNeeded(Entity entity) {
        double d = entity.posX - Minecraft.thePlayer.posX;
        double d2 = entity.posY - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()) - 20.0;
        double d3 = entity.posZ - Minecraft.thePlayer.posZ;
        double d4 = MathHelper.sqrt_float((float)(d * d + d3 * d3));
        float f = (float)(Math.atan2(d3, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d2, d4) * 180.0 / Math.PI));
        return new float[]{f, f2};
    }
}

