package io.github.raze.utilities.collection.world;

import io.github.raze.utilities.system.Methods;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPosition;

public class MoveUtil implements Methods {

	public static final double DEGREES_TO_RADIANS = Math.PI / 180.0;

	public static boolean isInLiquid() {
		return mc.thePlayer != null && (mc.thePlayer.isInWater() || mc.thePlayer.isInLava());
	}

	public static boolean isBlocking() {
		return mc.thePlayer != null && (mc.thePlayer.isBlocking() || mc.thePlayer.isEating() || mc.thePlayer.isUsingItem());
	}

	public static double getJumpHeight(double speed) {
		return mc.thePlayer.isPotionActive(Potion.jump) ? (speed + 0.1D * (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1)) : speed;
	}

	public static void strafe() {
		strafe((float) getSpeed());
	}

	public static void strafe(float speed) {
		if (!isMoving())
			return; 
		double yaw = getDirection();
		mc.thePlayer.motionX = -Math.sin(yaw) * speed;
		mc.thePlayer.motionZ = Math.cos(yaw) * speed;
	}

	public static double getDirection() {
		float rotationYaw = mc.thePlayer.rotationYaw;
		if (mc.thePlayer.moveForward < 0.0F)
			rotationYaw += 180.0F; 
		float forward = 1.0F;
		if (mc.thePlayer.moveForward < 0.0F) {
			forward = -0.5F;
		} else if (mc.thePlayer.moveForward > 0.0F) {
			forward = 0.5F;
		} 
		if (mc.thePlayer.moveStrafing > 0.0F)
			rotationYaw -= 90.0F * forward; 
		if (mc.thePlayer.moveStrafing < 0.0F)
			rotationYaw += 90.0F * forward; 
		return Math.toRadians(rotationYaw);
	}

	public static float getSpeedBoost(float times) {
		float boost = (float) ((MoveUtil.getBaseMoveSpeed() - 0.2875F) * times);
		if(0 > boost) {
			boost = 0;
		}

		return boost;
	}

	public static void sendPosition(double x, double y, double z, boolean ground, boolean moving) {
		if (!moving) {
			mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + y, mc.thePlayer.posZ, ground));
		} else {
			mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z, ground));
		} 
	}

	public static Block getBlockAtPos(BlockPosition inBlockPosition) {
		IBlockState s = mc.theWorld.getBlockState(inBlockPosition);
		return s.getBlock();
	}

	public static float getBaseMoveSpeed() {
		float baseSpeed = 0.2875F;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
			baseSpeed *= 1.0F + 0.2F * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		return baseSpeed;
	}

	public static boolean isMoving() {
		return !(mc.thePlayer.movementInput.moveForward == 0.0F && mc.thePlayer.movementInput.moveStrafe == 0.0F);
	}

	public static double defaultMoveSpeed() {
		return mc.thePlayer.isSprinting() ? 0.28700000047683716D : 0.22300000488758087D;
	}

	public static double getLastDistance() {
		return Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ);
	}

	public static boolean isOnGround(double height) {
		return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty();
	}

	public static double jumpHeight() {
		if (mc.thePlayer.isPotionActive(Potion.jump))
			return 0.419999986886978D + 0.1D * (mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1); 
		return 0.419999986886978D;
	}

	public static double getJumpBoostModifier(double baseJumpHeight) {
		if (mc.thePlayer.isPotionActive(Potion.jump)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
			baseJumpHeight += ((amplifier + 1) * 0.1F);
		} 
		return baseJumpHeight;
	}

	public static double predictedMotion(double motion, int ticks) {
		if (ticks == 0) return motion;
		double predicted = motion;

		for (int i = 0; i < ticks; i++) {
			predicted = (predicted - 0.08) * 0.98F;
		}

		return predicted;
	}

	public static double[] getMotion(double speed, float strafe, float forward, float yaw) {
		final float movementSpeed = (float) speed;

		final float sinYaw = MathHelper.sin((float) (yaw * DEGREES_TO_RADIANS));
		final float cosYaw = MathHelper.cos((float) (yaw * DEGREES_TO_RADIANS));

		final double motionX = strafe * movementSpeed * cosYaw - forward * movementSpeed * sinYaw;
		final double motionZ = forward * movementSpeed * cosYaw + strafe * movementSpeed * cosYaw;

		return new double[]{motionX, motionZ};
	}

	public static void setSpeed(double moveSpeed, float yaw, double strafe, double forward) {
		if (forward != 0.0D) {
			if (strafe > 0.0D) {
				yaw += ((forward > 0.0D) ? -45 : 45);
			} else if (strafe < 0.0D) {
				yaw += ((forward > 0.0D) ? 45 : -45);
			} 
			strafe = 0.0D;
			if (forward > 0.0D) {
				forward = 1.0D;
			} else if (forward < 0.0D) {
				forward = -1.0D;
			} 
		} 
		if (strafe > 0.0D) {
			strafe = 1.0D;
		} else if (strafe < 0.0D) {
			strafe = -1.0D;
		} 
		double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
		double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
		mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
		mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
	}

	public static double getSpeed() {
		return Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
	}

	public static boolean isMovingWithKeys() {
		return !Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
	}

	public static void setSpeed(double moveSpeed) {
		setSpeed(moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
	}

	public double getTickDist() {
		double xDist = mc.thePlayer.posX - mc.thePlayer.lastTickPosX;
		double zDist = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ;
		return Math.sqrt(Math.pow(xDist, 2.0D) + Math.pow(zDist, 2.0D));
	}

	public static void stop() {
		mc.thePlayer.motionX = mc.thePlayer.motionZ = 0.0D;
	}

	public static double getJumpMotion(float motionY) {
		Potion potion = Potion.jump;
		if (mc.thePlayer.isPotionActive(potion)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(potion).getAmplifier();
			motionY += (amplifier + 1) * 0.1F;
		} 
		return motionY;
	}

	public static boolean validMotionY(Entity entity, float minMotion, float maxMotion) {
		return entity.motionY > minMotion && entity.motionY < maxMotion;
	}

}
