package wtf.monsoon.api.util.entity;

import net.minecraft.client.Minecraft;

public class SpeedUtil {
	public static void setSpeed(float d) {
		double yaw = (float) Minecraft.getMinecraft().thePlayer.rotationYaw;
		boolean isMoving = Minecraft.getMinecraft().thePlayer.moveForward != 0.0F || Minecraft.getMinecraft().thePlayer.moveStrafing != 0.0F;
		boolean isMovingForward = Minecraft.getMinecraft().thePlayer.moveForward > 0.0F;
		boolean isMovingBackward = Minecraft.getMinecraft().thePlayer.moveForward < 0.0F;
		boolean isMovingRight = Minecraft.getMinecraft().thePlayer.moveStrafing > 0.0F;
		boolean isMovingLeft = Minecraft.getMinecraft().thePlayer.moveStrafing < 0.0F;
		boolean isMovingSideways = isMovingLeft || isMovingRight;
		boolean isMovingStraight = isMovingForward || isMovingBackward;
		if (isMoving) {
			if (isMovingForward && !isMovingSideways) {
				yaw += 0.0D;
			} else if (isMovingBackward && !isMovingSideways) {
				yaw += 180.0D;
			} else if (isMovingForward && isMovingLeft) {
				yaw += 45.0D;
			} else if (isMovingForward) {
				yaw -= 45.0D;
			} else if (!isMovingStraight && isMovingLeft) {
				yaw += 90.0D;
			} else if (!isMovingStraight && isMovingRight) {
				yaw -= 90.0D;
			} else if (isMovingBackward && isMovingLeft) {
				yaw += 135.0D;
			} else if (isMovingBackward) {
				yaw -= 135.0D;
			}

			yaw = Math.toRadians(yaw);
			Minecraft.getMinecraft().thePlayer.motionX = -Math.sin(yaw) * (float) d;
			Minecraft.getMinecraft().thePlayer.motionZ = Math.cos(yaw) * (float) d;
		}

	}

	public static void setSpeed(double moveSpeed, float yaw) {
		float forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
		float strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
		if ((double) forward == 0.0D && (double) strafe == 0.0D) {
			Minecraft.getMinecraft().thePlayer.motionX = 0.0D;
			Minecraft.getMinecraft().thePlayer.motionZ = 0.0D;
		} else {
			if ((double) forward != 0.0D) {
				if (strafe > 0.0F) {
					yaw += (float) ((double) forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0F) {
					yaw += (float) ((double) forward > 0.0D ? 45 : -45);
				}

				strafe = 0.0F;
				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}

			double xDist = (double) forward * moveSpeed * Math.cos(Math.toRadians((double) (yaw + 90.0F)))
					+ (double) strafe * moveSpeed * Math.sin(Math.toRadians((double) (yaw + 90.0F)));
			double zDist = (double) forward * moveSpeed * Math.sin(Math.toRadians((double) (yaw + 90.0F)))
					- (double) strafe * moveSpeed * Math.cos(Math.toRadians((double) (yaw + 90.0F)));
			Minecraft.getMinecraft().thePlayer.motionX = xDist;
			Minecraft.getMinecraft().thePlayer.motionZ = zDist;
			Minecraft.getMinecraft().thePlayer.posX = (xDist);
			Minecraft.getMinecraft().thePlayer.posZ = (zDist);
		}

	}
}
