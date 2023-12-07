package Velo.api.Util.Other.other;

import java.math.RoundingMode;
import java.util.Random;

import com.ibm.icu.math.BigDecimal;

import Velo.api.Util.Other.MovementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Rotations;
import net.minecraft.util.Vec3;

public class RotationUtil {
	static Minecraft mc = Minecraft.getMinecraft();
    private static int keepLength;
    
    private static Random random = new Random();
    private static double x = random.nextDouble();
    private static double y = random.nextDouble();
    private static double z = random.nextDouble();

    public static EntityLivingBase targetRotation;
	public static float yaw1 = 0, yaw2 = 0, yaw3 = 0, yaw4 = 0, yaw5 = 0, yaw6 = 0;
	public static float pitch1 = 0, pitch2 = 0, pitch3 = 0, pitch4 = 0, pitch5 = 0,  pitch6 = 0;
	public static transient float lastYaw = 0, lastPitch = 0, lastRandX = 0, lastRandY = 0, lastRandZ = 0;
	public static transient BlockPos offsets = BlockPos.ORIGIN,lastBlockPos = null;
	public static transient EnumFacing lastFacing = null;
	
    public static Vec3 getRandomCenter(AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.8 * Math.random(), bb.minY + (bb.maxY - bb.minY) * 1 * Math.random(), bb.minZ + (bb.maxZ - bb.minZ) * 0.8 * Math.random());
    }
    
    public static float[] getAnotherKaRotation(final Vec3 vec) {
        final Vec3 eyesPos = new Vec3(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight(), Minecraft.getMinecraft().thePlayer.posZ);
        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
    }
    
    public static float[] getRotations6969(final Entity entity, final float prevYaw, final float prevPitch, final float aimSpeed) {
        final EntityPlayerSP player = mc.thePlayer;
        final double xDist = entity.posX - player.posX;
        final double zDist = entity.posZ - player.posZ;
        final AxisAlignedBB entityBB = entity.getEntityBoundingBox().expand(0.10000000149011612, 0.10000000149011612, 0.10000000149011612);
        final double playerEyePos = player.posY + player.getEyeHeight();
        final double yDist = (playerEyePos > entityBB.maxY) ? (entityBB.maxY - playerEyePos) : ((playerEyePos < entityBB.minY) ? (entityBB.minY - playerEyePos) : 0.0);
        final double fDist = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);
        final float yaw = interpolateRotation(prevYaw, (float)(StrictMath.atan2(zDist, xDist) * 57.29577951308232) - 90.0f, aimSpeed);
        final float pitch = interpolateRotation(prevPitch, (float)(-(StrictMath.atan2(yDist, fDist) * 57.29577951308232)), aimSpeed);
        return new float[] { yaw, pitch };
    }
    
    private static float interpolateRotation(final float prev, final float now, final float maxTurn) {
        float var4 = MathHelper.wrapAngleTo180_float(now - prev);
        if (var4 > maxTurn) {
            var4 = maxTurn;
        }
        if (var4 < -maxTurn) {
            var4 = -maxTurn;
        }
        return prev + var4;
    }
    
    public static float[] getLegitRotations(Entity entity) {
    	double x = entity.posX - mc.thePlayer.prevPosX;
    	double y = entity.posY - mc.thePlayer.prevPosY;
    	double z = entity.posZ - mc.thePlayer.prevPosZ;
    	double xz = Math.sqrt(x * x + z * z);
        final float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final float pitch = (float) (-Math.toDegrees(Math.atan2(y, xz)));
        yaw1 = yaw5;
        yaw5 = yaw4;
        yaw4 = yaw3;
        yaw3 = yaw2;
        yaw2 = yaw;
        return new float[]{MathHelper.wrapAngleTo180_float(yaw1), MathHelper.wrapAngleTo180_float(pitch)};
    }
    
    
    public static double randomNumber(double max, double min) {
        return (Math.random() * (max - min)) + min;
    }


    public static double square(double motionX) {
        motionX *= motionX;
        return motionX;
    }

    public static double round(double num, double increment) {
        if (increment < 0) {
            throw new IllegalArgumentException();
        }
        java.math.BigDecimal bd = new  java.math.BigDecimal(num);
        bd = bd.setScale((int) increment, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    
    public static void setTargetRotation(final EntityLivingBase target,  float f) {
    	 final float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0f;
         final float pitch = (float) (-Math.toDegrees(Math.atan2(y, x + z)));
        if(Double.isNaN(yaw6) || Double.isNaN(pitch6)
                || pitch6 > 90 || pitch6 < -90)
            return;

        targetRotation = target;
       keepLength = (int) f;
    }
    public static float[] getRotationsEntity(final EntityLivingBase entity) {
  
        if (MovementUtil.isMoving()) {
            return getRotations(entity.posX + randomNumber(0.03, -0.03), entity.posY + entity.getEyeHeight() - 0.4D + randomNumber(0.07, -0.07), entity.posZ + randomNumber(0.03, -0.03));
        }
        return getRotations(entity.posX, entity.posY + entity.getEyeHeight() - 0.4D, entity.posZ);
    }
    
    public static float[] getNeededRotations(EntityLivingBase entityIn) {
        double d0 = entityIn.posX - mc.thePlayer.posX;
        double d1 = entityIn.posZ - mc.thePlayer.posZ;
        double d2 = entityIn.posY + entityIn.getEyeHeight() - (mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight());

        double d3 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
        float f = (float) (MathHelper.func_181159_b(d1, d0) * 180.0D / Math.PI) - 90.0F;
        float f1 = (float) (-(MathHelper.func_181159_b(d2, d3) * 180.0D / Math.PI));
        return new float[]{f, f1};
    }
    
    public static float getAngleChange(EntityLivingBase entityIn) {
        float yaw = getNeededRotations(entityIn)[0];
        float pitch = getNeededRotations(entityIn)[1];
        float playerYaw = mc.thePlayer.rotationYaw;
        float playerPitch = mc.thePlayer.rotationPitch;
        if (playerYaw < 0)
            playerYaw += 360;
        if (playerPitch < 0)
            playerPitch += 360;
        if (yaw < 0)
            yaw += 360;
        if (pitch < 0)
            pitch += 360;
        float yawChange = Math.max(playerYaw, yaw) - Math.min(playerYaw, yaw);
        float pitchChange = Math.max(playerPitch, pitch) - Math.min(playerPitch, pitch);
        return yawChange + pitchChange;
    }

    public static float[] getRotations(double posX, double posY, double posZ) {
        final EntityPlayerSP player = mc.thePlayer;
        double x = posX - player.posX;
        double y = posY - (player.posY + player.getEyeHeight());
        double z = posZ - player.posZ;

        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-(Math.atan2(y, dist) * 180.0D / Math.PI));
        return new float[]{yaw, pitch};
    }
    
public static float[] getRotationsHypixel(BlockPos paramBlockPos, EnumFacing paramEnumFacing, boolean rands) {
		
		if (rands) {
			
//			lastRandX = RandomUtils.nextFloat(0, 1f);
//			lastRandY = RandomUtils.nextFloat(0.4f, 0.8f);
//			lastRandZ = RandomUtils.nextFloat(0, 1f);
//			lastRandX = 0.5f;
//			lastRandY = 0.5f;
//			lastRandZ = 0.5f;
			
		}
		
		if (lastRandX > 2) {
			lastRandX -= 2;
		}

		if (lastRandZ > 2) {
			lastRandZ -= 2;
		}
		
		if (lastRandX < 0) {
			lastRandX = 0;
		}

		if (lastRandZ < 0) {
			lastRandZ = 0;
		}
		
		double offsetX = 0, offsetZ = 0;

		offsetX = (double) paramEnumFacing.getFrontOffsetX() / 4.0D;
		offsetZ = (double) paramEnumFacing.getFrontOffsetZ() / 4.0D;

		if (paramEnumFacing.getFrontOffsetX() == 0 && paramEnumFacing.getFrontOffsetZ() == -1) {
			offsetZ = 0.25;
		} else if (paramEnumFacing.getFrontOffsetX() == -1 && paramEnumFacing.getFrontOffsetZ() == 0) {
			offsetX = 0.25;
		}

		lastBlockPos = paramBlockPos;
		lastFacing = paramEnumFacing;
		
		double randX = lastRandX;
		double randY = lastRandY;
		double randZ = lastRandZ;

		if (randX >= 1) {
			randX = 1 - randX;
		}

		if (randZ >= 2) {
			randZ = 1 - randZ;
		}

		if (offsetX != 0) {
			randX = 0;
		}
		if (offsetZ != 0) {
			randZ = 0;
		}

		double d1 = (double) paramBlockPos.getX() - mc.thePlayer.posX + offsetX + randX;
		double d2 = (double) paramBlockPos.getZ() - mc.thePlayer.posZ + offsetZ + randZ;
		double d3 = mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight() - ((double) paramBlockPos.getY() + randY);
		double d4 = (double) MathHelper.sqrt_double(d1 * d1 + d2 * d2);
		float f1 = (float) (Math.atan2(d2, d1) * 180.0D / 3.141592653589793D) - 90.0F;
		float f2 = (float) (Math.atan2(d3, d4) * 180.0D / 3.141592653589793D);

//		f1 = MathHelper.wrapAngleTo180_float(f1);
//		f2 = MathHelper.wrapAngleTo180_float(f2);

		if (f2 > 90)
			f2 = 90;

		if (f2 < -90)
			f2 = -90;

//        mc.thePlayer.rotationYaw = f1;
//        mc.thePlayer.rotationPitch = f2;

//		return new float[] { mc.thePlayer.rotationYaw - 170, sprint.isEnabled() ? 60 : 70 };
		return new float[] {f1, f2};

	}

public static float[] getRotations(EntityLivingBase entityIn, float speed) {
    float yaw = updateRotation(mc.thePlayer.rotationYaw,
            getNeededRotations(entityIn)[0],
            speed);
    float pitch = updateRotation(mc.thePlayer.rotationPitch,
            getNeededRotations(entityIn)[1],
            speed);
    return new float[]{yaw, pitch};
}

private static float updateRotation(float currentRotation, float intendedRotation, float increment) {
    float f = MathHelper.wrapAngleTo180_float(intendedRotation - currentRotation);

    if (f > increment)
        f = increment;

    if (f < -increment)
        f = -increment;

    return currentRotation + f;
}



}
