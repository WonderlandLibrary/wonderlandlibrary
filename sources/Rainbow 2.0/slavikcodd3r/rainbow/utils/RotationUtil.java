package slavikcodd3r.rainbow.utils;

import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class RotationUtil
{
    public static float[] faceEntity(final Entity p_70625_1_, final float p_70625_2_, final float p_70625_3_) {
        final double var4 = p_70625_1_.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double var5 = p_70625_1_.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double var7;
        if (p_70625_1_ instanceof EntityLivingBase) {
            final EntityLivingBase var6 = (EntityLivingBase)p_70625_1_;
            var7 = var6.posY + var6.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
        }
        else {
            var7 = (p_70625_1_.getEntityBoundingBox().minY + p_70625_1_.getEntityBoundingBox().maxY) / 2.0 - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
        }
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float yaw = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(var7, var8) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static float[] getRotations(final Entity entityIn) {
        final double x = entityIn.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double z = entityIn.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double result;
        if (entityIn instanceof EntityLivingBase) {
            final EntityLivingBase entity = (EntityLivingBase)entityIn;
            result = entity.posY + entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
        }
        else {
            result = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
        }
        final double var141 = MathHelper.sqrt_double(x * x + z * z);
        final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(result, var141) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static float[] faceTileEntity(final TileEntityChest p_70625_1_, final float p_70625_2_, final float p_70625_3_) {
        final double var4 = p_70625_1_.getPos().getX() - Minecraft.getMinecraft().thePlayer.posX + 0.25;
        final double var5 = p_70625_1_.getPos().getZ() - Minecraft.getMinecraft().thePlayer.posZ + 0.25;
        final TileEntityChest var6 = p_70625_1_;
        final double var7 = var6.getPos().getY() + 0.5 - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() + 0.22;
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float yaw = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(var7, var8) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public void faceEntityHard(final Entity p_70625_1_, final float p_70625_2_, final float p_70625_3_) {
        final double var4 = p_70625_1_.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double var5 = p_70625_1_.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double var7;
        if (p_70625_1_ instanceof EntityLivingBase) {
            final EntityLivingBase var6 = (EntityLivingBase)p_70625_1_;
            var7 = var6.posY + var6.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
        }
        else {
            var7 = (p_70625_1_.getEntityBoundingBox().minY + p_70625_1_.getEntityBoundingBox().maxY) / 2.0 - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();
        }
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var9 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float var10 = (float)(-(Math.atan2(var7, var8) * 180.0 / 3.141592653589793));
        Minecraft.getMinecraft().thePlayer.rotationPitch = updateRotation(Minecraft.getMinecraft().thePlayer.rotationPitch, var10, p_70625_3_);
        Minecraft.getMinecraft().thePlayer.rotationYaw = updateRotation(Minecraft.getMinecraft().thePlayer.rotationYaw, var9, p_70625_2_);
    }
    
    public static float updateRotation(final float p_70663_1_, final float p_70663_2_, final float p_70663_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < -p_70663_3_) {
            var4 = -p_70663_3_;
        }
        return p_70663_1_ + var4;
    }
}
