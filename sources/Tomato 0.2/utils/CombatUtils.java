/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.utils;

import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class CombatUtils {
    public static float yaw;
    public static float tyaw;
    public static float pitch;
    public static float tpitch;
    public static float blockYaw;
    public static float blockPitch;

    public static void faceTileEntity(TileEntity target, float p_70625_2_, float p_70625_3_) {
        double var4 = (double)target.getPos().getX() + 0.5 - Wrapper.mc.thePlayer.posX;
        double var5 = (double)target.getPos().getZ() + 0.5 - Wrapper.mc.thePlayer.posZ;
        double var6 = (double)target.getPos().getY() - (Wrapper.mc.thePlayer.posY + 1.0);
        double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        float var8 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        float var9 = (float)(- Math.atan2(var6, var7) * 180.0 / 3.141592653589793);
        tpitch = CombatUtils.changeRotation(Wrapper.mc.thePlayer.rotationPitch, var9, p_70625_3_);
        tyaw = CombatUtils.changeRotation(Wrapper.mc.thePlayer.rotationYaw, var8, p_70625_2_);
        Wrapper.mc.thePlayer.rotationPitch = tpitch;
        Wrapper.mc.thePlayer.rotationYaw = tyaw;
    }

    public static void faceBlock(BlockPos block, float p_70625_2_, float p_70625_3_) {
        double var4 = (double)block.getX() - Wrapper.mc.thePlayer.posX;
        double var5 = (double)block.getZ() - Wrapper.mc.thePlayer.posZ;
        double var6 = (double)(block.getY() + block.getY()) - 6.0 - (Wrapper.mc.thePlayer.posY + (double)Wrapper.mc.thePlayer.getEyeHeight());
        double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        float var8 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        float var9 = (float)(- Math.atan2(var6, var7) * 180.0 / 3.141592653589793);
        blockPitch = CombatUtils.changeRotation(Wrapper.mc.thePlayer.rotationPitch, var9, p_70625_3_);
        blockYaw = CombatUtils.changeRotation(Wrapper.mc.thePlayer.rotationYaw, var8, p_70625_2_);
    }

    public static double angleDifference(double a2, double b2) {
        return ((a2 - b2) % 360.0 + 540.0) % 360.0 - 180.0;
    }

    public static float[] faceTarget(Entity target, float p_70625_2_, float p_70625_3_) {
        double var6;
        double var4 = target.posX - Wrapper.getPlayer().posX;
        double var8 = target.posZ - Wrapper.getPlayer().posZ;
        if (target instanceof EntityLivingBase) {
            EntityLivingBase var10 = (EntityLivingBase)target;
            var6 = var10.posY + (double)var10.getEyeHeight() - (Wrapper.getPlayer().posY + (double)Wrapper.getPlayer().getEyeHeight());
        } else {
            var6 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (Wrapper.getPlayer().posY + (double)Wrapper.getPlayer().getEyeHeight());
        }
        double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float)(Math.atan2(var8, var4) * 180.0 / 3.141592653589793) - 90.0f;
        float var13 = (float)(- Math.atan2(var6 - (double)(target instanceof EntityPlayer ? 0.5f : 0.0f), var14) * 180.0 / 3.141592653589793);
        float pitch = CombatUtils.changeRotation(Wrapper.getPlayer().rotationPitch, var13, p_70625_3_);
        float yaw = CombatUtils.changeRotation(Wrapper.getPlayer().rotationYaw, var12, p_70625_2_);
        return new float[]{yaw, pitch};
    }

    public static float changeRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < - p_70663_3_) {
            var4 = - p_70663_3_;
        }
        return p_70663_1_ + var4;
    }

    public boolean isCloser(Entity now, Entity first, float error) {
        if (first.getClass().isAssignableFrom(now.getClass())) {
            if (this.getDistanceToEntity(Minecraft.getMinecraft().thePlayer, now) < this.getDistanceToEntity(Minecraft.getMinecraft().thePlayer, first)) {
                return true;
            }
            return false;
        }
        if (this.getDistanceToEntity(Minecraft.getMinecraft().thePlayer, now) < this.getDistanceToEntity(Minecraft.getMinecraft().thePlayer, first) + error) {
            return true;
        }
        return false;
    }

    public float getDistanceToEntity(Entity from, Entity to2) {
        return from.getDistanceToEntity(to2);
    }

    public static float getDistanceBetweenAngles(float par1, float par2) {
        float angle = Math.abs(par1 - par2) % 360.0f;
        if (angle > 180.0f) {
            angle = 360.0f - angle;
        }
        return angle;
    }

    public static float[] getRotations(Entity ent) {
        double x2 = ent.posX;
        double z2 = ent.posZ;
        double y2 = ent.boundingBox.maxY - 4.0;
        return CombatUtils.getRotationFromPosition(x2, z2, y2);
    }

    public static float angleDifference(float a2, float b2) {
        return ((a2 - b2) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }

    public static float[] getRotationFromPosition(double x2, double z2, double y2) {
        double xDiff = x2 - Minecraft.getMinecraft().thePlayer.posX;
        double zDiff = z2 - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y2 - Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight();
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(- Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }
}

