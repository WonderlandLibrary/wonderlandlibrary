package me.robbanrobbin.jigsaw.client.modules;

import java.math.BigDecimal;
import java.math.RoundingMode;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;

public class AntiWeb extends Module {
	public static double speedFactor = 20.0D;
	private double moveSpeedVanilla;
	private double speed;
	private int stage;
	private double moveSpeed;
	private double lastDist;
	private WaitTimer timer = new WaitTimer();

	public AntiWeb() {
		super("AntiWeb", 0, Category.MOVEMENT, "Allows you to move faster in webs.");
	}

	public void onToggle() {
		this.timer.reset();
		this.moveSpeed = getBaseMoveSpeed();
		this.speed = 1.0D;
		this.stage = 0;
		super.onToggle();
	}

	public void onPreMotion(PreMotionEvent event) {
		if (this.currentMode.equals("NCP")) {
			if (mc.player.isInWeb) {
				MovementInput movementInput = mc.player.movementInput;
				float forward = movementInput.moveForward;
				float strafe = movementInput.moveStrafe;
				float yaw = Minecraft.getMinecraft().player.rotationYaw;
				if ((this.stage == 1) && ((mc.player.moveForward != 0.0F) || (mc.player.moveStrafing != 0.0F))) {
					this.stage = 2;
					this.moveSpeed = (1.38D * getBaseMoveSpeed() - 0.01D);
				} else if (this.stage == 2) {
					this.stage = 3;
					if (mc.player.onGround) {
						event.y = 0.399399995803833D;
						mc.player.motionY = 0.399399995803833D;
					}
					this.moveSpeed *= 2.149D;
				} else if (this.stage == 3) {
					this.stage = 4;
					double difference = 0.66D * (this.lastDist - getBaseMoveSpeed());
					this.moveSpeed = (this.lastDist - difference);
					if (((forward != 0.0F) || (strafe != 0.0F)) && (!mc.gameSettings.keyBindJump.pressed)) {
						event.y = -0.5D;
						this.stage = 2;
						if ((mc.world.getCollisionBoxes(mc.player, mc.player.boundingBox.offset(0.0D, mc.player.motionY, 0.0D))
								.size() > 0) || (mc.player.isCollidedVertically)) {
							this.stage = 1;
						}
					}
				} else {
					if ((mc.world.getCollisionBoxes(mc.player, mc.player.boundingBox.offset(0.0D, mc.player.motionY, 0.0D)).size() > 0)
							|| (mc.player.isCollidedVertically)) {
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
		}
	}

	public void onDisable() {
		super.onDisable();
	}

	public void onEnable() {
		super.onEnable();
	}

	public void onUpdate() {
		if (this.currentMode.equals("Vanilla")) {
			mc.player.isInWeb = false;

		} else if (this.currentMode.equals("NCP")) {
			if (mc.player.isInWeb) {
				// mc.player.onGround = true;
			}
		}
		super.onUpdate();
	}

	public String[] getModes() {
		return new String[] { "Vanilla", "NCP" };
	}

	public void onLateUpdate() {
		double xDist = mc.player.posX - mc.player.prevPosX;
		double zDist = mc.player.posZ - mc.player.prevPosZ;
		this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
		super.onLateUpdate();
	}

	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if(mc == null) {
			mc = Minecraft.getMinecraft();
		}
		if(mc.player == null) {
			return baseSpeed;
		}
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
