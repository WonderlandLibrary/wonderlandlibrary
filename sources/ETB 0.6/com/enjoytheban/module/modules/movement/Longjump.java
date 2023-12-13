package com.enjoytheban.module.modules.movement;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.Type;
import com.enjoytheban.api.events.world.EventMove;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.math.MathUtil;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

/**
 * Module for Longjump
 * 
 * @author Purity
 */

public class Longjump extends Module {

	private Mode<JumpMode> mode = new Mode("Mode", "mode", JumpMode.values(), JumpMode.NCP);

	// bunch of variables
	private int stage;
	private double moveSpeed;
	private double lastDist;

	public Longjump() {
		super("LongJump", new String[] { "lj", "jumpman", "jump" }, ModuleType.Movement);
		addValues(mode);
		setColor(new Color(76, 67, 216).getRGB());
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0f;
		if(mode.getValue() == JumpMode.Area51) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
		}
		if (mc.thePlayer != null) {
			this.moveSpeed = this.getBaseMoveSpeed();
		}
		this.lastDist = 0.0D;
		stage = 0;
	}

	// Simple OldGuardian longjump
	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		setSuffix(mode.getValue());
		if (mode.getValue() == JumpMode.OldGuardian) {
			if (mc.thePlayer.moving() && mc.thePlayer.onGround) {
				// jump
				mc.thePlayer.motionY = 0.44;
				// Set the player speed, this for some reason works on OldGuardian
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ, mc.thePlayer.onGround));
				mc.thePlayer.setSpeed(7.0);
			} else {
				// basically making it strafe midair
				mc.thePlayer.setSpeed(Math.sqrt(
						mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
			}

		} else if (mode.getValue() == JumpMode.Area51) {
			if (mc.thePlayer.moving()) {
				mc.timer.timerSpeed = 0.33f;
				if (mc.thePlayer.onGround) {
					mc.thePlayer.setSpeed(5.0f);
					mc.thePlayer.motionY = 0.455f;
				} else {
					mc.thePlayer.setSpeed(7.0f);
				}
			} else {
				mc.timer.timerSpeed = 0.33f;
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
		} else if (mode.getValue() == JumpMode.Janitor && e.getType() == Type.PRE && mc.thePlayer.moving()
				&& mc.thePlayer.onGround) {
			e.setY(e.getY() + ((mc.thePlayer.ticksExisted % 2 == 0) ? MathUtil.getHighestOffset(0.1) : 0.0));
		} else {
			if (e.getType() == Type.PRE) {
				final double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
				final double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
				this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
			}
		}
	}

	// now for ncp
	@EventHandler
	private void onMove(EventMove e) {
		if (mode.getValue() == JumpMode.NCP) {
			if (mc.thePlayer.moveStrafing <= 0.0f && mc.thePlayer.moveForward <= 0.0f) {
				this.stage = 1;
			}

			if (this.stage == 1 && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
				this.stage = 2;
				this.moveSpeed = 3 * getBaseMoveSpeed() - 0.01;
			} else if (this.stage == 2) {
				this.stage = 3;
				mc.thePlayer.motionY = 0.424;
				e.y = 0.424;
				this.moveSpeed *= 2.149802;
			} else if (this.stage == 3) {
				this.stage = 4;
				double difference = 0.66 * (this.lastDist - getBaseMoveSpeed());
				this.moveSpeed = this.lastDist - difference;
			} else {
				if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0
						|| mc.thePlayer.isCollidedVertically) {
					this.stage = 1;
				}
				this.moveSpeed = this.lastDist - this.lastDist / 159.0;
			}
			this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
			MovementInput movementInput = mc.thePlayer.movementInput;
			float forward = movementInput.moveForward;
			float strafe = movementInput.moveStrafe;
			float yaw = mc.thePlayer.rotationYaw;
			if (forward == 0.0f && strafe == 0.0f) {
				e.x = 0.0;
				e.z = 0.0;
			} else if (forward != 0.0f) {
				if (strafe >= 1.0f) {
					yaw += ((forward > 0.0f) ? -45 : 45);
					strafe = 0.0f;
				} else if (strafe <= -1.0f) {
					yaw += ((forward > 0.0f) ? 45 : -45);
					strafe = 0.0f;
				}
				if (forward > 0.0f) {
					forward = 1.0f;
				} else if (forward < 0.0f) {
					forward = -1.0f;
				}
			}
			double mx = Math.cos(Math.toRadians((double) (yaw + 90.0f)));
			double mz = Math.sin(Math.toRadians((double) (yaw + 90.0f)));
			e.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
			e.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
			if (forward == 0.0f && strafe == 0.0f) {
				e.x = 0.0;
				e.z = 0.0;
			} else if (forward != 0.0f) {
				if (strafe >= 1.0f) {
					yaw += ((forward > 0.0f) ? -45 : 45);
					strafe = 0.0f;
				} else if (strafe <= -1.0f) {
					yaw += ((forward > 0.0f) ? 45 : -45);
					strafe = 0.0f;
				}
				if (forward > 0.0f) {
					forward = 1.0f;
				} else if (forward < 0.0f) {
					forward = -1.0f;
				}
			}
		} else if (this.mode.getValue() == JumpMode.Janitor && mc.thePlayer.moving()) {
			this.moveSpeed = MathUtil.getBaseMovementSpeed() * ((mc.thePlayer.ticksExisted % 2 != 0) ? 5 : 6);
			double x = -(Math.sin(mc.thePlayer.getDirection()) * moveSpeed);
			double z = Math.cos(mc.thePlayer.getDirection()) * moveSpeed;
			e.setX(x);
			e.setZ(z);
			if (mc.thePlayer.onGround)
				e.setY(mc.thePlayer.motionY = 0.3);
		} else if (this.mode.getValue() == JumpMode.Guardian && mc.thePlayer.moving()) {
			if (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) {
				if (mc.thePlayer.onGround) {
					for (int i = 0; i < 20; i++) {
						mc.thePlayer.sendQueue
								.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
										mc.thePlayer.posY + 1.0E-9, mc.thePlayer.posZ, mc.thePlayer.onGround));
					}
					e.y = mc.thePlayer.motionY = 0.4D;
					mc.thePlayer.setSpeed(8f);
				}
			} else {
				mc.thePlayer.motionX = 0.0D;
				mc.thePlayer.motionZ = 0.0D;
			}
		}
	}

	// base movespeed
	double getBaseMoveSpeed() {
		double baseSpeed = 0.2873;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
		}
		return baseSpeed;
	}

	enum JumpMode {
		NCP, OldGuardian, Guardian, Janitor, Area51
	}
}