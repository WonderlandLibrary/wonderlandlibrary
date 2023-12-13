package wtf.monsoon.api.util.entity;

import java.util.ArrayList;
import java.util.HashSet;

import wtf.monsoon.api.util.misc.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RotationUtils {

	
	private static Minecraft mc = Minecraft.getMinecraft();

	public static float[] getRotations(EntityLivingBase ent) {
		double x = ent.posX;
		double y = ent.posY + ent.getEyeHeight() / 2.0f;
		double z = ent.posZ;
		return RotationUtils.getRotationFromPosition(x, y, z);
	}
	
	public static Vec3 getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }

	public static float[] doScaffoldRotations(Vec3 vec) {
		double diffX = vec.xCoord - mc.thePlayer.posX;
		double diffY = vec.yCoord - (mc.thePlayer.boundingBox.minY);
		double diffZ = vec.zCoord - mc.thePlayer.posZ;
		//double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)));
		float pitch = (float) -Math.toDegrees(Math.atan2(diffY, dist));
		return new float[] {
				mc.thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw),
				mc.thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch) };
	}

	public static float[] getBowAngles(Entity entity) {
		double xDelta = entity.posX - entity.lastTickPosX;
		double zDelta = entity.posZ - entity.lastTickPosZ;
		double d = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) % 0.8;
		boolean sprint = entity.isSprinting();
		double xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
		double zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
		double x = entity.posX + xMulti - Minecraft.getMinecraft().thePlayer.posX;
		double z = entity.posZ + zMulti - Minecraft.getMinecraft().thePlayer.posZ;
		double y = Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight()
				- (entity.posY + entity.getEyeHeight());
		double dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
		float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0f;
		float pitch = (float) Math.toDegrees(Math.atan2(y, dist));
		return new float[] { yaw, pitch };
	}

	public static float[] getRotationFromPosition(double x, double y, double z) {
		double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
		double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
		double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;

		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
		return new float[] { yaw, pitch };
	}
	
	 public static float[] getRotationsEntity(final EntityLivingBase entity) {
	        if (MovementUtil.isMoving()) {
	            return getRotations2(entity.posX + MathUtils.randomNumber(0.03, -0.03), entity.posY + entity.getEyeHeight() - 0.4D + MathUtils.randomNumber(0.07, -0.07), entity.posZ + MathUtils.randomNumber(0.03, -0.03));
	        }
	        return getRotations2(entity.posX, entity.posY + entity.getEyeHeight() - 0.4D, entity.posZ);
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

	private static MovingObjectPosition tracePath(final World world, final float x, final float y, final float z, final float tx, final float ty, final float tz, final float borderSize, final HashSet<Entity> excluded) {
		Vec3 startVec = new Vec3(x, y, z);
		Vec3 endVec = new Vec3(tx, ty, tz);
		final float minX = (x < tx) ? x : tx;
		final float minY = (y < ty) ? y : ty;
		final float minZ = (z < tz) ? z : tz;
		final float maxX = (x > tx) ? x : tx;
		final float maxY = (y > ty) ? y : ty;
		final float maxZ = (z > tz) ? z : tz;
		final AxisAlignedBB bb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ).expand(borderSize, borderSize, borderSize);
		final ArrayList<Entity> allEntities = (ArrayList<Entity>) world.getEntitiesWithinAABBExcludingEntity(null, bb);
		MovingObjectPosition blockHit = world.rayTraceBlocks(startVec, endVec);
		startVec = new Vec3(x, y, z);
		endVec = new Vec3(tx, ty, tz);
		Entity closestHitEntity = null;
		float closestHit = Float.POSITIVE_INFINITY;
		float currentHit;
		for (final Entity ent : allEntities) {
			if (ent.canBeCollidedWith() && !excluded.contains(ent)) {
				final float entBorder = ent.getCollisionBorderSize();
				AxisAlignedBB entityBb = ent.getEntityBoundingBox();
				if (entityBb == null) {
					continue;
				}
				entityBb = entityBb.expand(entBorder, entBorder, entBorder);
				final MovingObjectPosition intercept = entityBb.calculateIntercept(startVec, endVec);
				if (intercept == null) {
					continue;
				}
				currentHit = (float) intercept.hitVec.distanceTo(startVec);
				if (currentHit >= closestHit && currentHit != 0.0f) {
					continue;
				}
				closestHit = currentHit;
				closestHitEntity = ent;
			}
		}
		if (closestHitEntity != null) {
			blockHit = new MovingObjectPosition(closestHitEntity);
		}
		return blockHit;
	}

	private static MovingObjectPosition tracePathD(final World w, final double posX, final double posY, final double posZ, final double v, final double v1, final double v2, final float borderSize, final HashSet<Entity> exclude) {
		return tracePath(w, (float) posX, (float) posY, (float) posZ, (float) v, (float) v1, (float) v2, borderSize, exclude);
	}

	public static MovingObjectPosition rayCast(final EntityPlayerSP player, final double x, final double y, final double z) {
		final HashSet<Entity> excluded = new HashSet<>();
		excluded.add(player);
		return tracePathD(player.worldObj, player.posX, player.posY + player.getEyeHeight(), player.posZ, x, y, z, 1.0f, excluded);
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
	 
	 private static float updateRotation(float currentRotation, float intendedRotation, float increment) {
	        float f = MathHelper.wrapAngleTo180_float(intendedRotation - currentRotation);

	        if (f > increment)
	            f = increment;

	        if (f < -increment)
	            f = -increment;

	        return currentRotation + f;
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

	public static float getDistanceToEntity(EntityLivingBase entityLivingBase) {
		return mc.thePlayer.getDistanceToEntity(entityLivingBase);
	}
	 
	 public static float[] getRotations2(double posX, double posY, double posZ) {
	        final EntityPlayerSP player = mc.thePlayer;
	        double x = posX - player.posX;
	        double y = posY - (player.posY + player.getEyeHeight());
	        double z = posZ - player.posZ;

	        double dist = MathHelper.sqrt_double(x * x + z * z);
	        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
	        float pitch = (float) (-(Math.atan2(y, dist) * 180.0D / Math.PI));
	        return new float[]{yaw, pitch};
	    }
}
