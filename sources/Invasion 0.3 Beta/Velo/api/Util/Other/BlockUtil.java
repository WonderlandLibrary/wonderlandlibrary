package Velo.api.Util.Other;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class BlockUtil {
    private static Minecraft mc = Minecraft.getMinecraft();
    public double yaw;
    public double pitch;
    
    public BlockUtil(double startX, double startY, double startZ, double endX, double endY, double endZ) {
    	double yawX = this.distance(startX,startZ, endX, endZ);
    	double yawY = endY - startY;
    	double yawDownCount = this.distance(0.0D,  0.0d, yawX, yawY);
    	double finalYaw = -Math.toDegrees(Math.asin(yawY));
    	double pitchX = endX - startX;
    	double pitchY = endZ - startZ;
    	double pitchDownCount = this.distance(0.0D, 0.0D, pitchX, pitchY);
    	double finalPitch = Math.toDegrees(Math.asin(pitchY));
    	finalPitch -= 90.0D;
    	if(startX > endX) {
    		finalPitch *= -1.0D;
    	}
    	this.yaw = finalYaw;
    	this.pitch = finalPitch;
    }
    
    public static void placeHeldItemUnderPlayer() {
        final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1,
                mc.thePlayer.posZ);
        final Vec3 vec = new Vec3(blockPos).addVector(0.4F, 0.4F, 0.4F);
        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, null, blockPos, EnumFacing.UP,
                vec);
    }


    public static float[] getDirectionToBlock(int var0, int var1, int var2, EnumFacing var3) {
        EntityEgg var4 = new EntityEgg(mc.theWorld);
        var4.posX = (double) var0 + 0.5D;
        var4.posY = (double) var1 + 0.5D;
        var4.posZ = (double) var2 + 0.5D;
        var4.posX += (double) var3.getDirectionVec().getX() * 0.25D;
        var4.posY += (double) var3.getDirectionVec().getY() * 0.25D;
        var4.posZ += (double) var3.getDirectionVec().getZ() * 0.25D;
        return getDirectionToEntity(var4);
    }
    
    public static float[] getWatchdogRotations(BlockPos block, EnumFacing face) {
        final double x = block.getX() + 0.5 - mc.thePlayer.posX + face.getFrontOffsetX() / 2.0;
        final double z = block.getZ() + 0.5 - mc.thePlayer.posZ + face.getFrontOffsetZ() / 2.0;
        double y = block.getY() + 0.5;
        final double d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }

    private static float[] getDirectionToEntity(Entity var0) {
        return new float[]{getYaw(var0) + mc.thePlayer.rotationYaw, getPitch(var0) + mc.thePlayer.rotationPitch};
    }

    public static float[] getRotationNeededForBlock(EntityPlayer paramEntityPlayer, BlockPos pos) {
        double d1 = pos.getX() - paramEntityPlayer.posX;
        double d2 = pos.getY() + 0.5 - (paramEntityPlayer.posY + paramEntityPlayer.getEyeHeight());
        double d3 = pos.getZ() - paramEntityPlayer.posZ;
        double d4 = Math.sqrt(d1 * d1 + d3 * d3);
        float f1 = (float) (Math.atan2(d3, d1) * 180.0D / Math.PI) - 90.0F;
        float f2 = (float) -(Math.atan2(d2, d4) * 180.0D / Math.PI);
        return new float[]{f1, f2};
    }

    public static float getYaw(Entity var0) {
        double var1 = var0.posX - mc.thePlayer.posX;
        double var3 = var0.posZ - mc.thePlayer.posZ;
        double var5;

        if (var3 < 0.0D && var1 < 0.0D) {
            var5 = 90.0D + Math.toDegrees(Math.atan(var3 / var1));
        } else if (var3 < 0.0D && var1 > 0.0D) {
            var5 = -90.0D + Math.toDegrees(Math.atan(var3 / var1));
        } else {
            var5 = Math.toDegrees(-Math.atan(var1 / var3));
        }

        return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) var5));
    }
    public double getYaw() {
    	return this.pitch;
    }
    public double getPitch() {
    	return this.yaw;
    }

    public static float getPitch(Entity var0) {
        double var1 = var0.posX - mc.thePlayer.posX;
        double var3 = var0.posZ - mc.thePlayer.posZ;
        double var5 = var0.posY - 1.6D + (double) var0.getEyeHeight() - mc.thePlayer.posY;
        double var7 = (double) MathHelper.sqrt_double(var1 * var1 + var3 * var3);
        double var9 = -Math.toDegrees(Math.atan(var5 / var7));
        return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) var9);
    }
    
    public double distance(double value1, double value2, double x, double y) {
    	double a = Math.abs(value1 - x);
      	double b = Math.abs(value2 - y);
      	double c = Math.abs(a* a + b* b);
      	return c;
    }
}
