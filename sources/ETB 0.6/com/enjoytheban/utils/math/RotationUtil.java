package com.enjoytheban.utils.math;

import java.util.Random;

import com.enjoytheban.utils.Helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

/**
 * A utility class for rotation related things
 * 
 * @author Purity
 */

public class RotationUtil {

	// Pitch
	public static float pitch() {
		return Helper.mc.thePlayer.rotationPitch;
	}

	public static void pitch(float pitch) {
		Helper.mc.thePlayer.rotationPitch = pitch;
	}

	// Yaw
	public static float yaw() {
		return Helper.mc.thePlayer.rotationYaw;
	}

	public static void yaw(float yaw) {
		Helper.mc.thePlayer.rotationYaw = yaw;
	}

	public static float[] faceTarget(Entity target, float p_706252, float p_706253, boolean miss) {
		float yaw, pitch;
		double var4 = target.posX - Helper.mc.thePlayer.posX;
		double var8 = target.posZ - Helper.mc.thePlayer.posZ;
		double var6;
		if (target instanceof EntityLivingBase) {
			EntityLivingBase var10 = (EntityLivingBase) target;
			var6 = var10.posY + (double) var10.getEyeHeight()
					- (Helper.mc.thePlayer.posY + (double) Helper.mc.thePlayer.getEyeHeight());
		} else {
			var6 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0D
					- (Helper.mc.thePlayer.posY + (double) Helper.mc.thePlayer.getEyeHeight());
		}
		Random rnd = new Random();
		double var14 = (double) MathHelper.sqrt_double(var4 * var4 + var8 * var8);
		float var12 = (float) (Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
		float var13 = (float) (-(Math.atan2(var6 - (target instanceof EntityPlayer ? 0.25 : 0), var14) * 180.0D
				/ Math.PI));
		pitch = changeRotation(Helper.mc.thePlayer.rotationPitch, var13, p_706253);
		yaw = changeRotation(Helper.mc.thePlayer.rotationYaw, var12, p_706252);
		return new float[] { yaw, pitch };
	}

	public static float changeRotation(float p_706631, float p_706632, float p_706633) {
		float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
		if (var4 > p_706633)
			var4 = p_706633;
		if (var4 < -p_706633)
			var4 = -p_706633;
		return p_706631 + var4;
	}

	public static double[] getRotationToEntity(Entity entity) {
        double pX = Helper.mc.thePlayer.posX;
        double pY = Helper.mc.thePlayer.posY + (Helper.mc.thePlayer.getEyeHeight());
        double pZ = Helper.mc.thePlayer.posZ;

        double eX = entity.posX;
        double eY = entity.posY + (entity.height/2);
        double eZ = entity.posZ;

        double dX = pX - eX;
        double dY = pY - eY;
        double dZ = pZ - eZ;
        double dH = Math.sqrt(Math.pow(dX, 2) + Math.pow(dZ, 2));

        double yaw = (Math.toDegrees(Math.atan2(dZ, dX)) + 90);
        double pitch = (Math.toDegrees(Math.atan2(dH, dY)));

        return new double[]{yaw, 90 - pitch};
    }
	
	// Get an entities yaw & pitch
	public static float[] getRotations(Entity entity) {
		// if there isnt an entity fuck off
		if (Helper.mc.breakTheGame)
			return new float[] { 0, -90f };
		if (entity == null) {
			return null;
		}

		// variables, probably should of set the y but :shrug:
		double diffX = entity.posX - Helper.mc.thePlayer.posX;
		double diffZ = entity.posZ - Helper.mc.thePlayer.posZ;
		double diffY;

		// if the entity is something actually living so we're not rotating to fucking
		// experience orbs
		if (entity instanceof EntityLivingBase) {
			// crate a shorthand variable for entitylivingbase
			EntityLivingBase elb = (EntityLivingBase) entity;
			// just bullshit maths
			diffY = elb.posY + (elb.getEyeHeight() - 0.4)
					- (Helper.mc.thePlayer.posY + Helper.mc.thePlayer.getEyeHeight());
		} else {
			diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0
					- (Helper.mc.thePlayer.posY + Helper.mc.thePlayer.getEyeHeight());
		}

		// more bullshit maths
		double dist = (double) MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
		return new float[] { yaw, pitch };
	}

	// more gay maths tbh
	public static float getDistanceBetweenAngles(float angle1, float angle2) {
		// just settings angle3 to gay maths
		float angle3 = Math.abs(angle1 - angle2) % 360.0f;
		// if angle3 is above 180 then set it to 0
		if (angle3 > 180.0f) {
			angle3 = 0.0f;
		}
		return angle3;
	}

	// these are some rotation methods that will be used in our scaffold
	public static float[] grabBlockRotations(BlockPos pos) {
		return RotationUtil
				.getVecRotation(
						Helper.mc.thePlayer.getPositionVector().addVector(0.0,
								(double) Helper.mc.thePlayer.getEyeHeight(), 0.0),
						new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
	}

	public static float[] getVecRotation(Vec3 position) {
		return RotationUtil.getVecRotation(Helper.mc.thePlayer.getPositionVector().addVector(0.0,
				(double) Helper.mc.thePlayer.getEyeHeight(), 0.0), position);
	}

	public static float[] getVecRotation(Vec3 origin, Vec3 position) {
		Vec3 difference = position.subtract(origin);
		double distance = difference.flat().lengthVector();
		float yaw = (float) Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f;
		float pitch = (float) (-Math.toDegrees(Math.atan2(difference.yCoord, distance)));
		return new float[] { yaw, pitch };
	}

	// returns our direction, used in direction hud
	public static int wrapAngleToDirection(final float yaw, final int zones) {
		int angle = (int) (yaw + 360 / (2 * zones) + 0.5) % 360;
		if (angle < 0) {
			angle += 360;
		}
		return angle / (360 / zones);
	}
}