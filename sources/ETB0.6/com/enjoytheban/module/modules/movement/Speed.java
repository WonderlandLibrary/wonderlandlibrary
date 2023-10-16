package com.enjoytheban.module.modules.movement;

import java.awt.Color;
import java.util.List;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.Type;
import com.enjoytheban.api.events.world.EventMove;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.TimerUtil;
import com.enjoytheban.utils.math.MathUtil;

import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * Module for speed with multiple modes
 *
 * @author Purity
 */

public class Speed extends Module {

	private Mode<Enum> mode = new Mode("Mode", "mode", SpeedMode.values(), SpeedMode.HypixelHop);

	private int stage;
	private double movementSpeed;
	private double distance;

	public Speed() {
		super("Speed", new String[] { "zoom" }, ModuleType.Movement);
		setColor(new Color(99, 248, 91).getRGB());
		addValues(mode);
	}

	private TimerUtil timer = new TimerUtil();

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0f;
		if (mode.getValue() == SpeedMode.Area51) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
		}
	}

	private boolean canZoom() {
		return mc.thePlayer.moving() && mc.thePlayer.onGround;
	}

	// Simple guardian speed and the value assigning of distance
	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		if(Helper.onServer("enjoytheban")) {
			mode.setValue(SpeedMode.Bhop);
		}
		setSuffix(mode.getValue());
		if (mode.getValue() == SpeedMode.Sloth && canZoom()) {
			if (mc.thePlayer.moving()) {
				boolean under = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX, 1.6, mc.thePlayer.motionZ))
						.isEmpty();
				if (mc.thePlayer.ticksExisted % 2 != 0 && under) {
					e.y = e.y + 0.42;
				}
				mc.thePlayer.motionY = -10;
				if (mc.thePlayer.onGround) {
					mc.thePlayer
							.setSpeed(mc.thePlayer.getSpeed() * (mc.thePlayer.ticksExisted % 2 == 0 ? 4.0f : 0.28f));
				}
			}
		} else if (mode.getValue() == SpeedMode.Onground && canZoom()) {
			switch (this.stage) {
			case 1:
				e.setY(e.getY() + 0.4D);
				e.setOnground(false);

				this.stage += 1;
				break;
			case 2:
				e.setY(e.getY() + 0.4D);
				e.setOnground(false);
				this.stage += 1;
				break;
			default:
				this.stage = 1;
				break;

			// this.momentum = ((float) EntityHelper.getNormalMovementSpeed());
			}
		} else if (mode.getValue() == SpeedMode.Janitor && e.getType() == Type.PRE && canZoom()) {
		} else if (this.mode.getValue() == SpeedMode.AGC) {
			double speed = 0.2;
			double x = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
			double z = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
			double n = mc.thePlayer.movementInput.moveForward * speed * x;
			double xOff = n + mc.thePlayer.movementInput.moveStrafe * speed * z;
			double n2 = mc.thePlayer.movementInput.moveForward * speed * z;
			double zOff = n2 - mc.thePlayer.movementInput.moveStrafe * 0.5f * x;

			mc.thePlayer.setAIMoveSpeed(mc.thePlayer.getAIMoveSpeed());
			if (mc.thePlayer.onGround) {
				if (mc.thePlayer.moving()) {
					mc.thePlayer.motionY = 0.2;
				}
			} else {
				if (mc.thePlayer.motionY <= -0.1f) {
					double cock = 10;
					mc.thePlayer.setPosition(mc.thePlayer.posX + xOff * cock, mc.thePlayer.posY,
							mc.thePlayer.posZ + zOff * cock);
					mc.thePlayer.motionY -= 0.001f;
				}
			}
		} else if (mode.getValue() != SpeedMode.OldGuardian && mode.getValue() != SpeedMode.GuardianYport) {
			double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			distance = Math.sqrt(xDist * xDist + zDist * zDist);
		} else if (mode.getValue() == SpeedMode.GuardianYport) {
			if (mc.thePlayer.moving())
				mc.timer.timerSpeed = 1.6f;
			else
				mc.timer.timerSpeed = 1.0f;
			if (mc.thePlayer.moving() && mc.thePlayer.onGround) {
				// jump
				mc.thePlayer.motionY = 0.12;
				mc.thePlayer.motionY -= 0.04;
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ, mc.thePlayer.onGround));
				// Set the player speed, this for some reason works on guardian
				mc.thePlayer.setSpeed(0.7);
			} else {
				// basically making it strafe midair
				mc.thePlayer.setSpeed(Math.sqrt(
						mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
			}
		} else {
			if (mc.thePlayer.moving() && mc.thePlayer.onGround) {
				// jump
				mc.thePlayer.motionY = 0.4;
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ, mc.thePlayer.onGround));
				// Set the player speed, this for some reason works on guardian
				mc.thePlayer.setSpeed(1.75);
			} else {
				// basically making it strafe midair
				mc.thePlayer.setSpeed(Math.sqrt(
						mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
			}
		}
	}

	// Hypixel Mode
	@EventHandler
	private void onMove(EventMove e) {
		if (this.mode.getValue() == SpeedMode.HypixelHop) {
			if (canZoom() && stage == 1) {
				movementSpeed = 1.56 * MathUtil.getBaseMovementSpeed() - 0.01D;
				mc.timer.timerSpeed = 1.15f;
			} else if (canZoom() && stage == 2) {
				e.setY(mc.thePlayer.motionY = 0.3999D);
				movementSpeed *= 1.58;
				mc.timer.timerSpeed = 1.2f;
			} else if (stage == 3) {
				double difference = 0.66D * (distance - MathUtil.getBaseMovementSpeed());
				movementSpeed = distance - difference;
				mc.timer.timerSpeed = 1.1f;
			} else {
				List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D));
				if (collidingList.size() > 0 || mc.thePlayer.isCollidedVertically && stage > 0) {
					stage = (mc.thePlayer.moving()) ? 1 : 0;
				}
				movementSpeed = distance - distance / 159.0D;
			}
			mc.thePlayer.setMoveSpeed(e, movementSpeed = Math.max(movementSpeed, MathUtil.getBaseMovementSpeed()));
			if (mc.thePlayer.moving()) {
				stage += 1;
			}
		} else if (mode.getValue() == SpeedMode.Area51) {
			if (mc.thePlayer.moving()) {
				if (mc.getMinecraft().thePlayer.motionY <= -0) {
					mc.getMinecraft().thePlayer.motionY *= 1.5;
				}
				mc.thePlayer.onGround = true;
				mc.timer.timerSpeed = 0.33f;
				mc.thePlayer.setSpeed(4.0f);
			} else {
				mc.thePlayer.motionX = 0.0D;
				mc.thePlayer.motionZ = 0.0D;
			}
		} else if (this.mode.getValue() == SpeedMode.Onground && canZoom()) {
			switch (this.stage) {
			case 1:
				mc.timer.timerSpeed = 1.22f;
				this.movementSpeed = 1.89 * MathUtil.getBaseMovementSpeed() - 0.01D;
				distance++;
				if (distance == 1) {
					e.setY(e.getY() + 0.000008);
				} else if (distance == 2) {
					e.setY(e.getY() - 0.000008);
					distance = 0;
				}
				break;
			case 2:
				this.movementSpeed = 1.2 * MathUtil.getBaseMovementSpeed() - 0.01D;
				break;
			default:
				this.movementSpeed = ((float) MathUtil.getBaseMovementSpeed());
			}
			mc.thePlayer.setMoveSpeed(e, movementSpeed = Math.max(movementSpeed, MathUtil.getBaseMovementSpeed()));
			this.stage += 1;
		} else if (this.mode.getValue() == SpeedMode.Janitor && canZoom()) {
			mc.thePlayer.setSpeed((mc.thePlayer.ticksExisted % 2 != 0 ? 0 : 2));

		} else if (mode.getValue() == SpeedMode.Mineplex) {
			mc.timer.timerSpeed = 1.1f;
			if (canZoom() && stage == 1) {
				movementSpeed = 0.58;
			} else if (canZoom() && stage == 2) {
				e.setY(mc.thePlayer.motionY = 0.3);
				movementSpeed = 0.64;
			} else if (stage == 3) {
				double difference = 0.66D * (distance - MathUtil.getBaseMovementSpeed());
				movementSpeed = distance - difference;
			} else {
				List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D));
				if (collidingList.size() > 0 || mc.thePlayer.isCollidedVertically && stage > 0) {
					stage = (mc.thePlayer.moving()) ? 1 : 0;
				}
				movementSpeed = distance - distance / 159.0D;
			}
			mc.thePlayer.setMoveSpeed(e, movementSpeed = Math.max(movementSpeed, MathUtil.getBaseMovementSpeed()));
			if (mc.thePlayer.moving()) {
				stage += 1;
			}
		} else if (mode.getValue() == SpeedMode.Bhop) {
			mc.timer.timerSpeed = 1.07f;
			if (canZoom() && stage == 1) {
				movementSpeed = 2.55 * MathUtil.getBaseMovementSpeed() - 0.01D;
			} else if (canZoom() && stage == 2) {
				e.setY(mc.thePlayer.motionY = 0.3999D);
				movementSpeed *= 2.1;
			} else if (stage == 3) {
				double difference = 0.66D * (distance - MathUtil.getBaseMovementSpeed());
				movementSpeed = distance - difference;
			} else {
				List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D));
				if (collidingList.size() > 0 || mc.thePlayer.isCollidedVertically && stage > 0) {
					stage = (mc.thePlayer.moving()) ? 1 : 0;
				}
				movementSpeed = distance - distance / 159.0D;
			}
			mc.thePlayer.setMoveSpeed(e, movementSpeed = Math.max(movementSpeed, MathUtil.getBaseMovementSpeed()));
			if (mc.thePlayer.moving()) {
				stage += 1;
			}
		} else if (mode.getValue() == SpeedMode.Guardian) {
			if (mc.thePlayer.moving()) {
				if (mc.thePlayer.onGround) {
					for (int i = 0; i < 20; i++) {
						mc.thePlayer.sendQueue
								.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
										mc.thePlayer.posY + 1.0E-9, mc.thePlayer.posZ, mc.thePlayer.onGround));
					}
					mc.thePlayer.setSpeed(1.4f);
					e.y = mc.thePlayer.motionY = 0.4D;
				} else {
					mc.thePlayer.setSpeed((float) Math.sqrt(
							mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
				}
			} else {
				mc.thePlayer.motionX = 0.0D;
				mc.thePlayer.motionZ = 0.0D;
			}
		}
	}

	enum SpeedMode {
		Bhop, HypixelHop, Onground, OldGuardian, Guardian, GuardianYport, Mineplex, AGC, Janitor, Sloth, Area51
	}
}