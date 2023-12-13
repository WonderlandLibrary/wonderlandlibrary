package me.robbanrobbin.jigsaw.client.modules;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;

public class BunnyHop extends Module {

	private double moveSpeed;
	
	private int jumps;
	private int stage;
	
	private double lastDist;
	
	private double amplifier;

	public BunnyHop() {
		super("BunnyHop", Keyboard.KEY_NONE, Category.MOVEMENT, "Makes you go faster and it bypasses nocheat.");
	}
	
	@Override
	public ModSetting[] getModSettings() {
		return new ModSetting[]{new SliderSetting("Speed Amplifier", "bunnyHopMaxSpeed", 0.0, 0.13, 0.0, ValueFormat.PERCENT)};
	}

	@Override
	public void onToggle() {
		
		moveSpeed = getBaseMoveSpeed();

		jumps = 0;
		stage = 1;
		
		amplifier = 0;
		
		super.onToggle();
	}

	@Override
	public void onPreMotion(PreMotionEvent event) {
		
		if (mc.player.isInWater() || mc.player.isInLava() || mc.player.isInWeb
				|| Jigsaw.getModuleByName("Flight").isToggled() /*|| Jigsaw.getModuleByName("Scaffold").isToggled() //TODO fix */) {
			jumps = 0;
			this.stage = 1;
			return;
		}
		
		if(mc.player.isCollidedHorizontally) {
			jumps = 0;
		}
		
		MovementInput movementInput = mc.player.movementInput;
		float forward = movementInput.moveForward;
		float strafe = movementInput.moveStrafe;
		float yaw = Minecraft.getMinecraft().player.rotationYaw;
		
		double round = round(mc.player.posY - (int) mc.player.posY, 3);
		
		double amplifierSpeed = Math.min(1.00, ((0.4 * (double)jumps) - 0.7) + 1d);
		
		if(forward != 0 || strafe != 0) {
			if (round == 0.869 || round == 0.15) {
				Entity thePlayer = mc.player;
				
				amplifier = Math.min(ClientSettings.bunnyHopMaxSpeed, (0.03 * (double)jumps) - 0.03);
				if(jumps == 2) {
					amplifier = 0d;
				}
				
				thePlayer.motionY -= amplifier;
			}
		}
		if (jumps != 0 && (this.stage == 1) && ((forward != 0.0F) || (strafe != 0.0F))) {
			this.stage = 2;
			
			this.moveSpeed = getBaseMoveSpeed();
		} else if (this.stage == 2 || (jumps == 0 && ((forward != 0.0F) || (strafe != 0.0F)))) {
			this.stage = 3;
			jumps++;
			
			if(mc.player.onGround) {
				
				mc.player.motionY = 0.399399995803833D;
				event.y = 0.399399995803833D;
				
				
				this.moveSpeed = getBaseMoveSpeed() * 1.98 * amplifierSpeed;
				
			}

		} else if (this.stage == 3) {
			this.stage = 4;
			
			double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
			this.moveSpeed = (this.lastDist - difference);
			
		} else {
			boolean ground = false;
			if ((mc.world.getCollisionBoxes(mc.player,
					mc.player.boundingBox.offset(0.0D,
							-Math.abs((mc.player.motionY * (mc.player.fallDistance > 2 ? 2 : 2))), 0.0D)).size() > 0)) {
				ground = true;
			} else {
				ground = false;
			}
			if (ground || mc.player.onGround) {
				this.stage = 1;
			}
			this.moveSpeed = (this.lastDist - this.lastDist / 159.0D);
		}
		this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
		if ((forward == 0.0F) && (strafe == 0.0F)) {
			event.x = 0.0D;
			event.z = 0.0D;
			jumps = 0;
			amplifier = 0;
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
	
	@Override
	public String getAddonText() {
		return String.valueOf(Math.round(amplifier * 100d) / 100d);
	}
	
}
