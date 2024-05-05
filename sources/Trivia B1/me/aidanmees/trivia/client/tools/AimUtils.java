package me.aidanmees.trivia.client.tools;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class AimUtils {
	private static Minecraft mc = Minecraft.getMinecraft();

	
	public static void getRotationDifference(float[] rotations) {
	}

	public static void entityFaceEntity(EntityPlayer e, Entity theEntity) {
		double xDistance = theEntity.posX - mc.thePlayer.posX;
		double zDistance = theEntity.posZ - mc.thePlayer.posZ;
		double yDistance = theEntity.posY + (theEntity.getEyeHeight() - 0.1D) / 1.4D - mc.thePlayer.posY
				- mc.thePlayer.getEyeHeight() / 1.4D;
		double angleHelper = MathHelper.sqrt_double(xDistance * xDistance + zDistance * zDistance);

		float newYaw = (float) Math.toDegrees(-Math.atan(xDistance / zDistance));
		float newPitch = (float) -Math.toDegrees(Math.atan(yDistance / angleHelper));
		if ((zDistance < 0.0D) && (xDistance < 0.0D)) {
			newYaw = (float) (90.0D + Math.toDegrees(Math.atan(zDistance / xDistance)));
		} else if ((zDistance < 0.0D) && (xDistance > 0.0D)) {
			newYaw = (float) (-90.0D + Math.toDegrees(Math.atan(zDistance / xDistance)));
		}
		mc.thePlayer.rotationYaw = newYaw;
		mc.thePlayer.rotationPitch = newPitch;
		mc.thePlayer.rotationYawHead = newYaw;
	}

	public static float[] getRotations(Entity theEntity) {
		double xDistance = theEntity.posX - mc.thePlayer.posX;
		double zDistance = theEntity.posZ - mc.thePlayer.posZ;
		double yDistance = theEntity.posY + (theEntity.getEyeHeight() - 0.1D) / 1.4D - mc.thePlayer.posY
				- mc.thePlayer.getEyeHeight() / 1.4D;
		double angleHelper = MathHelper.sqrt_double(xDistance * xDistance + zDistance * zDistance);

		float newYaw = (float) Math.toDegrees(-Math.atan(xDistance / zDistance));
		float newPitch = (float) -Math.toDegrees(Math.atan(yDistance / angleHelper));
		if ((zDistance < 0.0D) && (xDistance < 0.0D)) {
			newYaw = (float) (90.0D + Math.toDegrees(Math.atan(zDistance / xDistance)));
		} else if ((zDistance < 0.0D) && (xDistance > 0.0D)) {
			newYaw = (float) (-90.0D + Math.toDegrees(Math.atan(zDistance / xDistance)));
		}
		return new float[] { newYaw, newPitch };
	}

	public static float[] getRotations(Location location) {
		double xDistance = location.getX() - mc.thePlayer.posX;
		double zDistance = location.getZ() - mc.thePlayer.posZ;
		double yDistance = location.getY() + (location.getY() - 0.1D) / 1.4D - mc.thePlayer.posY
				- mc.thePlayer.getEyeHeight() / 1.4D;
		double angleHelper = MathHelper.sqrt_double(xDistance * xDistance + zDistance * zDistance);

		float newYaw = (float) Math.toDegrees(-Math.atan(xDistance / zDistance));
		float newPitch = (float) -Math.toDegrees(Math.atan(yDistance / angleHelper));
		if ((zDistance < 0.0D) && (xDistance < 0.0D)) {
			newYaw = (float) (90.0D + Math.toDegrees(Math.atan(zDistance / xDistance)));
		} else if ((zDistance < 0.0D) && (xDistance > 0.0D)) {
			newYaw = (float) (-90.0D + Math.toDegrees(Math.atan(zDistance / xDistance)));
		}
		return new float[] { newYaw, newPitch };
	}

	public static float getYawChangeToEntity(Entity entity) {
		double deltaX = entity.posX - mc.thePlayer.posX;
		double deltaZ = entity.posZ - mc.thePlayer.posZ;
		double yawToEntity;
		if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
			yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else {
			if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
				yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
			} else {
				yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
			}
		}
		return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float) yawToEntity));
	}

	public static float getPitchChangeToEntity(Entity entity) {
		double deltaX = entity.posX - mc.thePlayer.posX;
		double deltaZ = entity.posZ - mc.thePlayer.posZ;
		double deltaY = entity.posY - 1.6D + entity.getEyeHeight() - 0.4D - mc.thePlayer.posY;
		double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);

		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));

		return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float) pitchToEntity);
	}

	public static float[] getAngles(Entity e) {
		return new float[] { getYawChangeToEntity(e) + mc.thePlayer.rotationYaw,
				getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch };
	}

	public static float[] getBlockRotations(int x, int y, int z, EnumFacing facing) {
		Minecraft mc = Minecraft.getMinecraft();
		Entity temp = new EntitySnowball(mc.theWorld);
		temp.posX = (x + 0.5D);
		temp.posY = (y + 0.5D);
		temp.posZ = (z + 0.5D);
		return getAngles(temp);
	}

	public static float[] getRotationFromPosition(double x, double z, double y) {
		double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
		double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
		double yDiff = y - Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight();

		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
		return new float[] { yaw, pitch };
	}
	
	public static String decrypted;

}