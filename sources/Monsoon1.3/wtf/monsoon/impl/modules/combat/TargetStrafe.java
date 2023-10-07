package wtf.monsoon.impl.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;
import wtf.monsoon.Monsoon;
import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventMove;
import wtf.monsoon.api.event.impl.EventRender3D;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.BooleanSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.util.misc.Timer;
import wtf.monsoon.api.util.render.ColorUtil;
import wtf.monsoon.api.util.render.RenderUtil;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;

public class TargetStrafe extends Module {
	public static int direction = 1;
	public static boolean canMove;
	public double movespeed2;

	public Timer timer = new Timer();

	public BooleanSetting onlySpeed = new BooleanSetting("Only Speed", false, this);
	public BooleanSetting allowFly = new BooleanSetting("Allow Fly", false, this);
	public NumberSetting distance = new NumberSetting("Range", 2, 1, 6, 0.5, this);

	public TargetStrafe() {
		super("TargetStrafe", "Automatically strafe your opponents.", Keyboard.KEY_NONE, Category.COMBAT);
		this.addSettings(distance,onlySpeed,allowFly);
	}
	
	public void onEnable() {
		super.onEnable();
	}
	
	public void onDisable() {
		super.onDisable();
	}

	@EventTarget
	public void onMove(EventMove e) {
		Aura aura = Monsoon.INSTANCE.manager.killAura;
		if(mc.gameSettings.keyBindJump.isKeyDown() && Monsoon.INSTANCE.manager.killAura.isEnabled() && !aura.target.isDead) {
			if (aura.target == null) {
				return;
			}
			float[] rotations = getRotationsEntity(aura.target);
			movespeed2 = getBaseMoveSpeed();
			if (aura.target.getDistanceToEntity(mc.thePlayer) < distance.getValue()) {
				if (!allowFly.isEnabled()) {
					if (Monsoon.INSTANCE.manager.fly.isEnabled()) {
						return;
					} else {
						if (mc.gameSettings.keyBindRight.isPressed()) {
							direction = -1;
						}
						if (mc.gameSettings.keyBindLeft.isPressed()) {
							direction = 1;
						}
						canMove = true;
					}
				}

				if (!aura.target.isDead)
					canMove = false;

				/*
				 * Speed Only Value
				 */

				if (onlySpeed.isEnabled()) {
					if (Monsoon.INSTANCE.manager.speed.isEnabled()) {
						if (mc.gameSettings.keyBindRight.isPressed()) {
							direction = -1;
						}
						if (mc.gameSettings.keyBindLeft.isPressed()) {
							direction = 1;
						}
						canMove = true;
						if (Monsoon.INSTANCE.manager.speed.isEnabled()) {
							if (direction == -1 || direction == 1) {
								//mc.gameSettings.keyBindJump.pressed = true;
							}
						}
					} else {
						return;
					}
				}

				/*
				 * Normal
				 */

				if (mc.gameSettings.keyBindRight.isPressed()) {
					direction = -1;
				}
				if (mc.gameSettings.keyBindLeft.isPressed()) {
					direction = 1;
				}

				canMove = true;
				if (Monsoon.INSTANCE.manager.speed.isEnabled()) {
					if (direction == -1 || direction == 1) {
						//mc.gameSettings.keyBindJump.pressed = true;
					}
				}

				if (canMove) {
					strafe(e, movespeed2, rotations[0], direction, 0.0D);
				}
			} else {
				if (mc.gameSettings.keyBindJump.isKeyDown() && aura.target.getDistanceToEntity(mc.thePlayer) < aura.range.getValue()) {
					strafe(e, movespeed2, rotations[0], direction, 1.0D);
				}
				canMove = false;
			}
		}
	}

	@EventTarget
	public void onRender3d(EventRender3D e) {
		Aura aura = Monsoon.INSTANCE.manager.killAura;
		if (aura.target != null && aura.target.getDistanceToEntity(mc.thePlayer) < aura.range.getValue() && aura.isEnabled() && !aura.target.isDead) {
			RenderUtil.drawCircle(aura.target, e.getPartialTicks(), distance.getValue());
		}
	}


	public double getBaseMoveSpeed() {
		double baseSpeed = 0.2873;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
		}
		return baseSpeed;
	}


	public static void strafe(EventMove event, double movespeed, float yaw, double strafe, double forward) {
		double fow = forward;
		double stra = strafe;
		float ya = yaw;
		if (fow != 0.0D) {
			if (strafe > 0.0D) {
				ya += ((fow > 0.0D) ? -45 : 45);
			} else if (strafe < 0.0D) {
				ya += ((fow > 0.0D) ? 45 : -45);
			}
			stra = 0.0D;
			if (fow > 0.0D) {
				fow = 1.0D;
			} else if (fow < 0.0D) {
				fow = -1.0D;
			}
		}
		if (stra > 1.0D) {
			stra = 1.0D;
		} else if (stra < 0.0D) {
			stra = -1.0D;
		}
		double mx = Math.cos(Math.toRadians((ya + 90.0F)));
		double mz = Math.sin(Math.toRadians((ya + 90.0F)));
		event.setX(fow * movespeed * mx + stra * movespeed * mz);
		event.setZ(fow * movespeed * mz - stra * movespeed * mx);
	}

	public float[] getRotationsEntity(EntityLivingBase entity) {
		return getRotations(entity.posX + randomNumber(0.03D, -0.03D), entity.posY + entity.getEyeHeight() - 0.4D + randomNumber(0.07D, -0.07D), entity.posZ + randomNumber(0.03D, -0.03D));
	}

	public static double randomNumber(double max, double min) {
		return Math.random() * (max - min) + min;
	}

	public float[] getRotations(double posX, double posY, double posZ) {
		EntityLivingBase player = mc.thePlayer;
		double x = posX - player.posX;
		double y = posY - player.posY + player.getEyeHeight();
		double z = posZ - player.posZ;
		double dist = Math.sqrt(x * x + z * z);
		float yaw = (float)(Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float)-(Math.atan2(y, dist) * 180.0D / Math.PI);
		return new float[] { yaw, pitch };
	}

	public static int novoline(int delay) {
		double novolineState = Math.ceil((System.currentTimeMillis() + delay) / 10.0);
		novolineState %= 360;
		return Color.getHSBColor((float) (novolineState / 180.0f), 0.3f, 1.0f).getRGB();
	}

	public static int rainbow(int delay) {
		double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
		rainbowState %= 360;
		return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}
	
	
}
