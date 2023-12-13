package me.aidanmees.trivia.client.modules.Movement;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.WaitTimer;
import me.aidanmees.trivia.client.events.PreMotionEvent;
import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;

public class NoSlowdown extends Module {
   public static boolean noslowing = false;
	public static double speedFactor = 20.0D;
	private double moveSpeedVanilla;
	private double speed;
	private int stage;
	private double moveSpeed;
	private double lastDist;
	private WaitTimer timer = new WaitTimer();
	public NoSlowdown() {
		super("NoSlowdown", Keyboard.KEY_NONE, Category.MOVEMENT, "When using items, you don't get slowed down.");
	}

	@Override
	public void onDisable() {
		noslowing = false;
		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}
	

	@Override
	public void onPreMotion(PreMotionEvent event) {
		if (mc.thePlayer.isInWeb) {
			MovementInput movementInput = mc.thePlayer.movementInput;
			float forward = movementInput.moveForward;
			float strafe = movementInput.moveStrafe;
			float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
			if ((this.stage == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
				this.stage = 2;
				this.moveSpeed = (1.38D * getBaseMoveSpeed() - 0.01D);
			} else if (this.stage == 2) {
				this.stage = 3;
				if (mc.thePlayer.onGround) {
					event.y = 0.399399995803833D;
					mc.thePlayer.motionY = 0.399399995803833D;
				}
				this.moveSpeed *= 2.149D;
			} else if (this.stage == 3) {
				this.stage = 4;
				double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
				this.moveSpeed = (this.lastDist - difference);
				if (((forward != 0.0F) || (strafe != 0.0F)) && (!mc.gameSettings.keyBindJump.pressed)) {
					event.y = -0.5D;
					this.stage = 2;
					if ((mc.theWorld
							.getCollidingBoundingBoxes(mc.thePlayer,
									mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D), true)
							.size() > 0) || (mc.thePlayer.isCollidedVertically)) {
						this.stage = 1;
					}
				}
			} else {
				if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D), true).size() > 0)
						|| (mc.thePlayer.isCollidedVertically)) {
					this.stage = 1;
				}
				this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
			}
			this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed()) / 1.1;
			if ((forward == 0.0F) && (strafe == 0.0F)) {
				event.x = 0.0D;
				event.z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}
				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			double mx = Math.cos(Math.toRadians(yaw + 90.0F));
			double mz = Math.sin(Math.toRadians(yaw + 90.0F));
			event.x = (forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz);
			event.z = (forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx);
			super.onPreMotion(event);
		}
		 if(mc.thePlayer.isBlocking() && mc.thePlayer.isMovingXZ()) {
	            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0,0,0), EnumFacing.UP));
		 }
	    
		super.onPreMotion(event);
	}
	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if(mc == null) {
			mc = Minecraft.getMinecraft();
		}
		if(mc.thePlayer == null) {
			return baseSpeed;
		}
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	

	

}
