package me.robbanrobbin.jigsaw.client.modules;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.MathHelper;

public class Speed extends Module {

	private double moveSpeedVanilla;
	private double speed;
	private int stage;
	private double moveSpeed;
	private double lastDist;
	private WaitTimer timer = new WaitTimer();

	@Override
	public ModSetting[] getModSettings() {
		// airAgilitySpeedSlider

//		Slider airAgilitySpeedSlider = new BasicSlider("Vanilla Speed Factor", ClientSettings.VanillaspeedFactor, 10,
//				150, 0.0, ValueDisplay.DECIMAL);
//
//		airAgilitySpeedSlider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.VanillaspeedFactor = slider.getValue();
//
//			}
//		});
		SliderSetting airAgilitySpeedSlider = new SliderSetting("Vanilla Speed", "VanillaspeedFactor", 10, 150, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { airAgilitySpeedSlider }; 
	}

	public Speed() {
		super("Speed", Keyboard.KEY_M, Category.MOVEMENT, "WROOOOOM");
	}

	@Override
	public void onToggle() {
		timer.reset();
		this.moveSpeed = getBaseMoveSpeed();
		speed = 1;
		this.stage = 2;
		Utils.resetMcTimerTPS();
		super.onToggle();
	}

	@Override
	public void onPreMotion(PreMotionEvent event) {
		if (Jigsaw.getModuleByName("Flight").isToggled()) {
			return;
		}
		if (this.currentMode.equals("Vanilla")) {
			MovementInput movementInput = mc.player.movementInput;
			this.moveSpeedVanilla = ClientSettings.VanillaspeedFactor / 20;
			float forward = movementInput.moveForward;
			float strafe = movementInput.moveStrafe;
			float yaw = Minecraft.getMinecraft().player.rotationYaw;
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
			event.x = (forward * this.moveSpeedVanilla * mx + strafe * this.moveSpeedVanilla * mz);
			event.z = (forward * this.moveSpeedVanilla * mz - strafe * this.moveSpeedVanilla * mx);
			super.onPreMotion(event);
		}
		if (this.currentMode.equals("OldNCP")) {
			if (mc.player.isInWater() || mc.player.isInLava()) {
				return;
			}
			MovementInput movementInput = mc.player.movementInput;
			float forward = movementInput.moveForward;
			float strafe = movementInput.moveStrafe;
			float yaw = Minecraft.getMinecraft().player.rotationYaw;
			if ((this.stage == 1) && ((mc.player.moveForward != 0.0F) || (mc.player.moveStrafing != 0.0F))) {
				this.stage = 2;
				this.moveSpeed = (1.38D * getBaseMoveSpeed() - 0.01D);
			} else if (this.stage == 2) {
				this.stage = 3;
				event.y = 0.399399995803833D;
				this.moveSpeed *= 2.149D;
			} else if (this.stage == 3) {
				this.stage = 4;
				double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
				this.moveSpeed = (this.lastDist - difference);
				if (((forward != 0.0F) || (strafe != 0.0F)) && !mc.gameSettings.keyBindJump.pressed) {
					event.y = -0.4;
					// mc.player.motionY = -1;
					stage = 2;
				}
			} else {
				if ((mc.world.getCollisionBoxes(mc.player,
						mc.player.boundingBox.offset(0.0D, mc.player.motionY, 0.0D)).size() > 0)
						|| (mc.player.isCollidedVertically)) {
					this.stage = 1;
				}
				this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
			}

			this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
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
		
		
		
	}
	
	@Override
	public void onLivingUpdate() {
		
		super.onLivingUpdate();
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		if(currentMode.equals("NewNCP-1")) {
			MovementInput input = mc.player.movementInput;
			if (mc.player.isInWater() || mc.player.isCollidedHorizontally || mc.player.movementInput.sneak
					|| input.moveForward <= 0) {
                return;
            }
            mc.gameSettings.keyBindJump.pressed = false;
//            mc.timer.timerSpeed = 1.095F; //TODO fix
            if (mc.player.onGround) {
                mc.player.motionY = 0.399;
                float f = mc.player.rotationYaw * 0.017453292F;
                mc.player.motionX -= (double) (MathHelper.sin(f) * 0.089);
                mc.player.motionZ += (double) (MathHelper.cos(f) * 0.089);
            } else {
            	if (mc.player.fallDistance <= 1) {
                    mc.player.motionY = -1;
                }
                return;
            }
		}
		super.onUpdate(event);
	}

	@Override
	public String[] getModes() {
		return new String[] { "Vanilla", "NewNCP-1", "OldNCP" };
	}

	public void onLateUpdate() {
		double xDist = mc.player.posX - mc.player.prevPosX;
		double zDist = mc.player.posZ - mc.player.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
		super.onLateUpdate();
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.player.isPotionActive(MobEffects.SPEED)) {
			int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
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
