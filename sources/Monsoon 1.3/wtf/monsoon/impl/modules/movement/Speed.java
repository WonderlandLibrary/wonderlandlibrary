package wtf.monsoon.impl.modules.movement;

import net.minecraft.block.*;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraft.init.Blocks;
import org.lwjgl.input.Keyboard;

import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventPreMotion;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.BooleanSetting;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.util.entity.MovementUtil;
import wtf.monsoon.api.util.entity.SpeedUtil;
import wtf.monsoon.api.util.misc.Timer;
import wtf.monsoon.api.Wrapper;


public class Speed extends Module {
	
	Timer timer = new Timer();
	
	boolean isWalking = false;
	public ModeSetting mode = new ModeSetting("Mode", this, "Hypixel", "Vanilla", "NCP", "SafeHop", "Redesky", "Hypixel","Verus","Mineplex", "Lowhop");
	public NumberSetting speed = new NumberSetting("Speed", 0.5, 0.1, 5, 0.01, this);
	public BooleanSetting jump = new BooleanSetting("Jump", true, this);
	public Speed() {
		super("Speed", "Move Faster", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.addSettings(mode, jump, speed);
		this.disableOnLagback = true;
	}
	
	
	
	public void onEnable() {
		super.onEnable();
	}
	
	public void onDisable() {
		super.onDisable();
		Wrapper.mc.timer.timerSpeed = 1F;
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotion e) {

		float speedmultiplier = 1;
		if(mode.is("Mineplex")) {
			if(Wrapper.mc.thePlayer.onGround) {
				SpeedUtil.setSpeed(0F);
				if(Wrapper.mc.gameSettings.keyBindForward.isKeyDown() || Wrapper.mc.gameSettings.keyBindLeft.isKeyDown() || Wrapper.mc.gameSettings.keyBindBack.isKeyDown() || Wrapper.mc.gameSettings.keyBindRight.isKeyDown()) {
					Wrapper.mc.thePlayer.motionY = 0.4F;
					if(speedmultiplier <= 1.08) {

					} else {
						Wrapper.mc.thePlayer.isAirBorne = true;
					}
				} else {
					Wrapper.mc.timer.timerSpeed = 1F;
					speedmultiplier = 1;
				}
			} else {
				if(!Wrapper.mc.thePlayer.isAirBorne) {
					speedmultiplier += 0.005F;
				}
				if(Wrapper.mc.thePlayer.motionY < 0.05 && Wrapper.mc.thePlayer.motionY > 0) {
					if(Wrapper.mc.thePlayer.isSprinting()) {
						SpeedUtil.setSpeed(0.5F);
						Wrapper.mc.thePlayer.motionX *= 1.4F;
						Wrapper.mc.thePlayer.motionZ *= 1.4F;
					} else {
						SpeedUtil.setSpeed(0.45F);
					}

				} else {
					if(Wrapper.mc.thePlayer.isSprinting() && Wrapper.mc.gameSettings.keyBindForward.isKeyDown() && !Wrapper.mc.gameSettings.keyBindLeft.isKeyDown() && !Wrapper.mc.gameSettings.keyBindBack.isKeyDown() && !Wrapper.mc.gameSettings.keyBindRight.isKeyDown()) {
						if(Wrapper.mc.thePlayer.motionY > 0) {
							SpeedUtil.setSpeed((float) (0.5F * (speedmultiplier * 1.1)));
							Wrapper.mc.thePlayer.motionX *= 0.06F + speedmultiplier;
							Wrapper.mc.thePlayer.motionZ *= 0.06F + speedmultiplier;
						} else {
							SpeedUtil.setSpeed(0.4F);
							Wrapper.mc.thePlayer.motionX *= 1.35F;
							Wrapper.mc.thePlayer.motionZ *= 1.35F;
						}

					} else {
						SpeedUtil.setSpeed(0.35F);
					}
				}

			}
		}
	
	}
	
	@EventTarget
	public void onUpdate(EventUpdate e) {

		this.setSuffix(mode.getValueName());

		if(jump.isEnabled() && !mode.is("Mineplex") && !mode.is("Lowhop")) {
			if(isWalking && Wrapper.mc.thePlayer.onGround) {
				if(timer.hasTimeElapsed(100, true)) {
					Wrapper.mc.thePlayer.jump();
				}
			}
		}

		if(mode.is("SafeHop")) {
			/*if(!(mc.theWorld.getBlockState(mc.thePlayer.getPosition().add(0,-1,0)).getBlock() instanceof BlockStairs) && !(mc.theWorld.getBlockState(mc.thePlayer.getPosition().add(0,-1,0)).getBlock() instanceof BlockSlab)) {
				if (mc.thePlayer.onGround && MovementUtil.isMoving()) {
					mc.gameSettings.keyBindJump.pressed = true;
					SpeedUtil.setSpeed(0.261f);
				} else {
					SpeedUtil.setSpeed(0.258f);
				}
			}*/
			mc.gameSettings.keyBindJump.pressed = true;
		}

		if(mode.is("Lowhop")) {
			if(!Wrapper.mc.thePlayer.isUsingItem()) {
				//Wrapper.mc.timer.timerSpeed = 1.3f;
			}
			if(Wrapper.mc.thePlayer.onGround && MovementUtil.isMoving() && !Wrapper.mc.thePlayer.isCollidedHorizontally) {
				Wrapper.mc.thePlayer.motionY = 0.12f;
			}
			if((Wrapper.mc.thePlayer.isCollidedHorizontally || Wrapper.mc.gameSettings.keyBindJump.isKeyDown()) && Wrapper.mc.thePlayer.onGround && MovementUtil.isMoving()) {
				Wrapper.mc.thePlayer.jump();
			}
			SpeedUtil.setSpeed((float) speed.getValue());
		}

		if(mode.is("Hypixel")) {
			if(Wrapper.mc.thePlayer.onGround) {
				SpeedUtil.setSpeed(0.23F);
			} else {
				SpeedUtil.setSpeed(0.260786F);
			}
			if(Wrapper.mc.thePlayer.onGround && MovementUtil.isMoving()) {
				Wrapper.mc.thePlayer.jump();
			}
		}
		if(Wrapper.mc.thePlayer.motionX > 0.14 || Wrapper.mc.thePlayer.motionX < -0.14 || Wrapper.mc.thePlayer.motionZ > 0.14 || Wrapper.mc.thePlayer.motionZ < -0.14) {
			isWalking = true;
		} else {
			isWalking = false;
		}
		if(mode.is("Redesky")) {
			Wrapper.mc.timer.timerSpeed = 1.75f;
			if(Wrapper.mc.thePlayer.isAirBorne) {
				SpeedUtil.setSpeed(0.4f);
				Wrapper.mc.timer.timerSpeed = 1.0f;
			}
		}
			if(mode.is("Vanilla")) {
				Wrapper.mc.timer.timerSpeed = 1F;
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
			if(mode.is("NCP")) {
				if(Wrapper.mc.thePlayer.onGround) {
					if(Wrapper.mc.thePlayer.isSprinting()) {
						SpeedUtil.setSpeed(0.15F);
					} else {
						SpeedUtil.setSpeed(0.175F);
					}
				} else {
					SpeedUtil.setSpeed(0.28F);
				}
			}
			if(mode.is("Verus")) {
				Wrapper.mc.timer.timerSpeed = 1F;
				if(isWalking && Wrapper.mc.thePlayer.onGround) {
						if(timer.hasTimeElapsed(100, true)) {
							Wrapper.mc.thePlayer.jump();
						}
					}
				if(Wrapper.mc.thePlayer.isSprinting()) {
					if(Wrapper.mc.thePlayer.onGround) {
					
						
						if(Wrapper.mc.thePlayer.moveForward > 0) {
							SpeedUtil.setSpeed(0.19F);
						} else {
							SpeedUtil.setSpeed(0.14F);
						}
					} else {
						if(Wrapper.mc.thePlayer.moveForward > 0) {
							SpeedUtil.setSpeed(0.295F);
						} else {
							SpeedUtil.setSpeed(0.29F);
						}
				
					}
					
				} else {
				if(Wrapper.mc.thePlayer.onGround) {
					
					//mc.thePlayer.jump();
						if(Wrapper.mc.thePlayer.moveForward > 0) {
							SpeedUtil.setSpeed(0.16F);
						} else {
							SpeedUtil.setSpeed(0.14F);
						}
					} else {
						if(Wrapper.mc.thePlayer.moveForward > 0) {
							SpeedUtil.setSpeed(0.25F);
						} else {
							SpeedUtil.setSpeed(0.2F);
						}
				
					}
			}
		}
		
	}
}