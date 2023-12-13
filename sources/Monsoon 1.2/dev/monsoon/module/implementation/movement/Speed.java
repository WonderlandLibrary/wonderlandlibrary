package dev.monsoon.module.implementation.movement;

import dev.monsoon.util.entity.MovementUtil;
import dev.monsoon.util.entity.SpeedUtil;
import org.lwjgl.input.Keyboard;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventMotion;
import dev.monsoon.event.listeners.EventUpdate;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.BooleanSetting;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.misc.Timer;
import dev.monsoon.module.enums.Category;

public class Speed extends Module {
	
	Timer timer = new Timer();
	
	boolean isWalking = false;
	public ModeSetting mode = new ModeSetting("Mode", this, "Hypixel", "Vanilla", "NCP", "Redesky", "Hypixel","Verus","Mineplex", "Lowhop");
	public NumberSetting speed = new NumberSetting("Speed", 0.5, 0.1, 5, 0.01, this);
	public BooleanSetting jump = new BooleanSetting("Jump", true, this);
	public Speed() {
		super("Speed", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.addSettings(mode, jump, speed);
		this.disableOnLagback = true;
	}
	
	
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		mc.timer.timerSpeed = 1F;
	}
	
	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			if(e.isPre()) {
				float speedmultiplier = 1;
				if(mode.is("Mineplex")) {
					if(mc.thePlayer.onGround) {
						SpeedUtil.setSpeed(0F);
						if(mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
							mc.thePlayer.motionY = 0.4F;
							if(speedmultiplier <= 1.08) {

							} else {
								mc.thePlayer.isAirBorne = true;
							}
						} else {
							mc.timer.timerSpeed = 1F;
							speedmultiplier = 1;
						}
					} else {
						if(!mc.thePlayer.isAirBorne) {
							speedmultiplier += 0.005F;
						}
						if(mc.thePlayer.motionY < 0.05 && mc.thePlayer.motionY > 0) {
							if(mc.thePlayer.isSprinting()) {
								SpeedUtil.setSpeed(0.5F);
								mc.thePlayer.motionX *= 1.4F;
								mc.thePlayer.motionZ *= 1.4F;
							} else {
								SpeedUtil.setSpeed(0.35F);
							}

						} else {
							if(mc.thePlayer.isSprinting() && mc.gameSettings.keyBindForward.isKeyDown() && !mc.gameSettings.keyBindLeft.isKeyDown() && !mc.gameSettings.keyBindBack.isKeyDown() && !mc.gameSettings.keyBindRight.isKeyDown()) {
								if(mc.thePlayer.motionY > 0) {
									SpeedUtil.setSpeed((float) (0.5F * (speedmultiplier * 1.1)));
									mc.thePlayer.motionX *= 0.03F + speedmultiplier;
									mc.thePlayer.motionZ *= 0.03F + speedmultiplier;
								} else {
									SpeedUtil.setSpeed(0.4F);
									mc.thePlayer.motionX *= 1.15F;
									mc.thePlayer.motionZ *= 1.15F;
								}

							} else {
								SpeedUtil.setSpeed(0.35F);
							}
						}

					}
				}
			}
		}
		if(e instanceof EventUpdate) {
			this.setSuffix(mode.getValueName());
			if(e.isPre()) {
				if(jump.isEnabled() && !mode.is("Mineplex") && !mode.is("Lowhop")) {
					if(isWalking && mc.thePlayer.onGround) {
						if(timer.hasTimeElapsed(100, true)) {
							mc.thePlayer.jump();
						}
					}
				}

				if(mode.is("Lowhop")) {
					if(!mc.thePlayer.isUsingItem()) {
						mc.timer.timerSpeed = 1.3f;
					}
					if(mc.thePlayer.onGround && MovementUtil.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
						mc.thePlayer.motionY = 0.12f;
					}
					if((mc.thePlayer.isCollidedHorizontally || mc.gameSettings.keyBindJump.isKeyDown()) && mc.thePlayer.onGround && MovementUtil.isMoving()) {
						mc.thePlayer.jump();
					}
					SpeedUtil.setSpeed((float) speed.getValue());
				}

				if(mode.is("Hypixel")) {
					mc.timer.timerSpeed = 1.17f;
					if(mc.thePlayer.onGround) {
						SpeedUtil.setSpeed(0.24F);
					} else {
						SpeedUtil.setSpeed(0.27386F);
					}
					if(mc.thePlayer.onGround && MovementUtil.isMoving()) {
						mc.thePlayer.jump();
					}
				}
				if(mc.thePlayer.motionX > 0.14 || mc.thePlayer.motionX < -0.14 || mc.thePlayer.motionZ > 0.14 || mc.thePlayer.motionZ < -0.14) {
					isWalking = true;
				} else {
					isWalking = false;
				}
				if(mode.is("Redesky")) {
					mc.timer.timerSpeed = 1.75f;
					if(mc.thePlayer.isAirBorne) {
						SpeedUtil.setSpeed(0.4f);
						mc.timer.timerSpeed = 1.0f;
					}
				}
					if(mode.is("Vanilla")) {
						mc.timer.timerSpeed = 1F;
						SpeedUtil.setSpeed((float) speed.getValue());
					}
					/*if(mode.is("Mineplex")) {
						if(mc.thePlayer.isAirBorne) {
							SpeedModifier.setSpeed(0.32F);
							mc.timer.timerSpeed = 0.87f;
						}
						if(mc.thePlayer.onGround) {
							if(AutoJump.isEnabled() && MovementUtil.isMoving()) {
								mc.thePlayer.jump();
								//mc.thePlayer.motionY *= 1.03f;
							}
							SpeedModifier.setSpeed(0.32F);
						}
					}*/
					if(mode.is("Legit bhop")) {
							mc.timer.timerSpeed = 1F;
							if(mc.thePlayer.onGround) {
								mc.thePlayer.jump();
							}
							if(mc.thePlayer.isDead || mc.thePlayer.getHealth() <= 0) {
								enabled = false;
								mc.timer.timerSpeed = 1F;
					}
				}
						
					if(mode.is("NCP")) {
						if(mc.thePlayer.onGround) {
							if(mc.thePlayer.isSprinting()) {
								SpeedUtil.setSpeed(0.15F);
							} else {
								SpeedUtil.setSpeed(0.175F);
							}
						} else {
							SpeedUtil.setSpeed(0.28F);
						}
					}
					if(mode.is("Verus")) {
						mc.timer.timerSpeed = 1F;
						if(isWalking && mc.thePlayer.onGround) {
								if(timer.hasTimeElapsed(100, true)) {
									mc.thePlayer.jump();
								}
							}
						if(mc.thePlayer.isSprinting()) {
							if(mc.thePlayer.onGround) {
							
								
								if(mc.thePlayer.moveForward > 0) {
									SpeedUtil.setSpeed(0.19F);
								} else {
									SpeedUtil.setSpeed(0.14F);
								}
							} else {
								if(mc.thePlayer.moveForward > 0) {
									SpeedUtil.setSpeed(0.295F);
								} else {
									SpeedUtil.setSpeed(0.29F);
								}
						
							}
							
						} else {
						if(mc.thePlayer.onGround) {
							
							//mc.thePlayer.jump();
								if(mc.thePlayer.moveForward > 0) {
									SpeedUtil.setSpeed(0.16F);
								} else {
									SpeedUtil.setSpeed(0.14F);
								}
							} else {
								if(mc.thePlayer.moveForward > 0) {
									SpeedUtil.setSpeed(0.25F);
								} else {
									SpeedUtil.setSpeed(0.2F);
								}
						
							}
					}
				}
			}
		}
	}
}