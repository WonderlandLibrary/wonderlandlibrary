package Velo.devtesting;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class BlockUtil {
	
	public BlockPos blockPos;
	public EnumFacing enumFacing;
	public float keepRotationYaw = 0;
	public float keepRotationPitch = 0;
	
	//Animations for the scaffold to make it look cooler xd
	
	public float animationYaw = 0;
	public float animationPitch = 0;
	
	
	public BlockUtil(BlockPos blockPos, EnumFacing enumFacing) {
		this.blockPos = blockPos;
		this.enumFacing = enumFacing;
	}
	
    private float[] getDirectionToEntity(Entity var0) {
        return new float[]{getYaw(var0) + Minecraft.getMinecraft().thePlayer.rotationYaw, getPitch(var0) + Minecraft.getMinecraft().thePlayer.rotationPitch};
    }
    
    public float getYaw(Entity var0) {
        double var1 = var0.posX - Minecraft.getMinecraft().thePlayer.posX;
        double var3 = var0.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double var5;

        if (var3 < 0.0D && var1 < 0.0D) {
            var5 = 90.0D + Math.toDegrees(Math.atan(var3 / var1));
        } else if (var3 < 0.0D && var1 > 0.0D) {
            var5 = -90.0D + Math.toDegrees(Math.atan(var3 / var1));
        } else {
            var5 = Math.toDegrees(-Math.atan(var1 / var3));
        }

        return MathHelper.wrapAngleTo180_float(-(Minecraft.getMinecraft().thePlayer.rotationYaw - (float) var5));
    }

    public float getPitch(Entity var0) {
        double var1 = var0.posX - Minecraft.getMinecraft().thePlayer.posX;
        double var3 = var0.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double var5 = var0.posY - 1.6D + (double) var0.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
        double var7 = (double) MathHelper.sqrt_double(var1 * var1 + var3 * var3);
        double var9 = -Math.toDegrees(Math.atan(var5 / var7));
        return -MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch - (float) var9);
    }
}
