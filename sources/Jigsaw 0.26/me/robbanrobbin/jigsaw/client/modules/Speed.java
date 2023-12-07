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
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

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
		mc.timer.timerSpeed = 1f;
		super.onToggle();
	}

	@Override
	public void onPreMotion(PreMotionEvent event) {
		if (Jigsaw.getModuleByName("Flight").isToggled()) {
			return;
		}
		if (this.currentMode.equals("Vanilla")) {
			MovementInput movementInput = mc.thePlayer.movementInput;
			this.moveSpeedVanilla = ClientSettings.VanillaspeedFactor / 20;
			float forward = movementInput.moveForward;
			float strafe = movementInput.moveStrafe;
			float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
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
			if (mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) {
				return;
			}
			MovementInput movementInput = mc.thePlayer.movementInput;
			float forward = movementInput.moveForward;
			float strafe = movementInput.moveStrafe;
			float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
			if ((this.stage == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
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
					// mc.thePlayer.motionY = -1;
					stage = 2;
				}
			} else {
				if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D), true).size() > 0)
						|| (mc.thePlayer.isCollidedVertically)) {
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
			if (mc.thePlayer.isInWater() || mc.thePlayer.isCollidedHorizontally || mc.thePlayer.movementInput.sneak
					|| mc.thePlayer.moveForward <= 0) {
                return;
            }
            mc.gameSettings.keyBindJump.pressed = false;
            mc.timer.timerSpeed = 1.095F;
            if (mc.thePlayer.onGround) {
                mc.thePlayer.motionY = 0.399;
                float f = mc.thePlayer.rotationYaw * 0.017453292F;
                mc.thePlayer.motionX -= (double) (MathHelper.sin(f) * 0.089);
                mc.thePlayer.motionZ += (double) (MathHelper.cos(f) * 0.089);
            } else {
            	if (mc.thePlayer.fallDistance <= 1) {
                    mc.thePlayer.motionY = -1;
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
		double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
		double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
		super.onLateUpdate();
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
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
