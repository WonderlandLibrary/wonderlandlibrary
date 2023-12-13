package com.enjoytheban.module.modules.movement;

import java.awt.Color;

import com.enjoytheban.Client;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.Type;
import com.enjoytheban.api.events.world.EventMove;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.TimerUtil;
import com.enjoytheban.utils.math.MathUtil;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * Simple guardian flight
 * 
 * @author Purity
 */

public class Flight extends Module {

	// mode for flight
	public Mode mode = new Mode("Mode", "mode", FlightMode.values(), FlightMode.Guardian);

	private TimerUtil timer;

	private double movementSpeed;

	private int counter;

	private int hypixelCounter;
	private int hypixelCounter2;

	public Flight() {
		super("Flight", new String[] { "fly", "angel" }, ModuleType.Movement);
		// timer
		timer = new TimerUtil();
		setColor(new Color(158, 114, 243).getRGB());
		addValues(mode);
	}

	// if using hypixel fly put you in the air a lil bit so you dont flag when you
	// leave the ground
	@Override
	public void onEnable() {
		if (mode.getValue() == FlightMode.Hypixel || mode.getValue() == FlightMode.HypixelBoost) {
			hypixelCounter = 0;
			hypixelCounter2 = 1000;
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.2, mc.thePlayer.posZ);
		}
	}

	// Set the timer back to 1.0 when disabled
	@Override
	public void onDisable() {
		if(mode.getValue() == FlightMode.Area51) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
		}
		hypixelCounter = 0;
		hypixelCounter2 = 100;
		mc.timer.timerSpeed = 1.0f;
	}

	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		// Basic guardian flight
		setSuffix(mode.getValue());
		if (mode.getValue() == FlightMode.Guardian) {
			// sets the timer speed to 1.7 so it isn't slow as balls
			mc.timer.timerSpeed = 1.7f;

			// If the player is onground, Every 2 ticks increment the players motion y by
			// 0.04
			if (!mc.thePlayer.onGround && mc.thePlayer.ticksExisted % 2 == 0) {
				mc.thePlayer.motionY = 0.04;
			}

			// if jump is pressed inc the players motion y
			if (mc.gameSettings.keyBindJump.getIsKeyPressed()) {
				mc.thePlayer.motionY += 1.0;
			}

			// if sneak is pressed deinc the players motion y
			if (mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
				mc.thePlayer.motionY -= 1.0;
			}
		} else if (mode.getValue() == FlightMode.Vanilla) {
			if (mc.thePlayer.movementInput.jump) {
				mc.thePlayer.motionY = 1.0f;
			} else if (mc.thePlayer.movementInput.sneak) {
				mc.thePlayer.motionY = -1.0f;
			} else {
				mc.thePlayer.motionY = 0;
			}
			if (mc.thePlayer.moving()) {
				mc.thePlayer.setSpeed(1.0f);
			} else {
				mc.thePlayer.setSpeed(0);
			}
		} else if (mode.getValue() == FlightMode.Area51) {
			if (mc.thePlayer.movementInput.jump) {
				mc.thePlayer.motionY = 1;
			} else if (mc.thePlayer.movementInput.sneak) {
				mc.thePlayer.motionY = -1;
			} else {
				mc.thePlayer.motionY = 0;
			}
		}
		// hypix fly
		else if (mode.getValue() == FlightMode.Hypixel && e.getType() == Type.PRE) {
			mc.thePlayer.motionY = 0.0;
			counter++;
			if (counter == 1) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.000008, mc.thePlayer.posZ);
			} else if (counter == 2) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.000008, mc.thePlayer.posZ);
				counter = 0;
			}
			if (timer.hasReached(50)) {
				if (mc.thePlayer.movementInput.jump)
					mc.thePlayer.motionY += 0.5;
				if (mc.thePlayer.movementInput.sneak)
					mc.thePlayer.motionY -= 0.5;
				timer.reset();
			}
		} else if (mode.getValue() == FlightMode.HypixelBoost && e.getType() == Type.PRE) {
			mc.thePlayer.motionY = 0.0;
			counter++;
			if (counter == 1) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.000008, mc.thePlayer.posZ);
			} else if (counter == 2) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.000008, mc.thePlayer.posZ);
				counter = 0;
			}
			if (timer.hasReached(50)) {
				if (mc.thePlayer.movementInput.jump)
					mc.thePlayer.motionY += 0.5;
				if (mc.thePlayer.movementInput.sneak)
					mc.thePlayer.motionY -= 0.5;
				timer.reset();
			}
		} else if (mode.getValue() == FlightMode.OldGuardianLongJumpFly) {
			if (mc.thePlayer.moving()
					&& !Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
				if (mc.thePlayer.isAirBorne) {
					if (mc.thePlayer.ticksExisted % 12 == 0 && mc.theWorld
							.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ))
							.getBlock() instanceof BlockAir) {
						mc.thePlayer.setSpeed(6.5);
						mc.thePlayer.sendQueue
								.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
										mc.thePlayer.posY + 1.0E-9D, mc.thePlayer.posZ, mc.thePlayer.onGround));
						mc.thePlayer.motionY = 0.455;
					} else {
						mc.thePlayer.setSpeed((float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX
								+ mc.thePlayer.motionZ * mc.thePlayer.motionZ));
					}
				} else {
					mc.thePlayer.motionX = 0.0;
					mc.thePlayer.motionZ = 0.0;
				}
				if (mc.thePlayer.movementInput.jump) {
					mc.thePlayer.motionY = 0.85;
				} else if (mc.thePlayer.movementInput.sneak) {
					mc.thePlayer.motionY = -0.85;
				}
			}
		}
	}

	@EventHandler
	private void onMove(EventMove e) {
		if (mc.thePlayer.moving() && mode.getValue() == FlightMode.Hypixel) {
			mc.thePlayer.cameraYaw = 0.099999994f / 1.1f * 0.8f;
			mc.timer.timerSpeed = 1.15f;
			this.movementSpeed = MathUtil.getBaseMovementSpeed();
			double x = -(Math.sin(mc.thePlayer.getDirection()) * movementSpeed);
			double z = Math.cos(mc.thePlayer.getDirection()) * movementSpeed;
			e.setX(x);
			e.setZ(z);
		} else if (mc.thePlayer.moving() && mode.getValue() == FlightMode.HypixelBoost) {
			mc.thePlayer.cameraYaw = 0.099999994f / 1.1f * 0.8f;
			if (hypixelCounter < 5) {
				hypixelCounter++;
				mc.timer.timerSpeed = 2.0f;
			} else {
				mc.timer.timerSpeed = 1.0f;
			}
			if (hypixelCounter2 < 112) {
				hypixelCounter++;
				mc.timer.timerSpeed = 2.0f;
			}
			if (hypixelCounter2 < 160) {
				hypixelCounter2++;
			}
			if (hypixelCounter2 >= 160) {
				hypixelCounter2 = 100;
			}
			this.movementSpeed = MathUtil.getBaseMovementSpeed();
			double x = -(Math.sin(mc.thePlayer.getDirection()) * movementSpeed);
			double z = Math.cos(mc.thePlayer.getDirection()) * movementSpeed;
			e.setX(x);
			e.setZ(z);
		} else if (mode.getValue() == FlightMode.Area51) {
			if (mc.thePlayer.moving()) {
				mc.thePlayer.setSpeed(3.33f);
				mc.timer.timerSpeed = 0.32f;
			} else {
				mc.timer.timerSpeed = 0.88f;
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
				mc.thePlayer.motionY = 0;
			}
		} else if (mode.getValue() == FlightMode.AGC) {
			if (mc.thePlayer.moving()) {
				mc.thePlayer.setSpeed(mc.thePlayer.ticksExisted % 3 == 0 ? 5 : 0);
			}
			if (mc.thePlayer.ticksExisted % 10 == 0 && mc.gameSettings.keyBindJump.pressed) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 5, mc.thePlayer.posZ);
			} else {
				mc.thePlayer.motionY = 0.05;
			}
			if (mc.thePlayer.ticksExisted >= 10 && !mc.gameSettings.keyBindJump.pressed) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.5, mc.thePlayer.posZ);
				mc.thePlayer.ticksExisted = 0;
			}
		} else if (mode.getValue() == FlightMode.GuardianLongJumpFly) {
			if (Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
				e.y = Minecraft.getMinecraft().thePlayer.motionY = 1.5;
			}
			if (Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed) {
				e.y = Minecraft.getMinecraft().thePlayer.motionY = -1.5;
			}
			if (mc.thePlayer.moving() && !Minecraft.getMinecraft().gameSettings.keyBindJump.pressed
					&& !Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed) {

				if (Minecraft.getMinecraft().thePlayer.motionY > -0.41
						&& !Minecraft.getMinecraft().thePlayer.onGround) {
					return;
				}
				for (int i = 0; i < 10; i++) {
					mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY + 1.0E-9, mc.thePlayer.posZ, true));
				}
				mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				e.y = Minecraft.getMinecraft().thePlayer.motionY = 0.455;
				mc.thePlayer.setSpeed(7f);
			}
		} else {
			mc.timer.timerSpeed = 1.0f;
		}
	}

	// basemovespeed, changes with speed pots
	double getBaseMoveSpeed() {
		double baseSpeed = 0.275;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
		}
		return baseSpeed;
	}

	public enum FlightMode {
		Vanilla, Guardian, Hypixel, Area51, HypixelBoost, OldGuardianLongJumpFly, GuardianLongJumpFly, AGC
	}
}