package dev.monsoon.module.implementation.combat;

import dev.monsoon.Monsoon;
import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventMotion;
import dev.monsoon.event.listeners.EventMove;
import dev.monsoon.event.listeners.EventRender3D;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.enums.Category;
import dev.monsoon.module.setting.impl.BooleanSetting;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.misc.Timer;
import dev.monsoon.util.render.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TargetStrafe extends Module {
	public static int direction = 1;
	public static boolean canMove;
	public double movespeed2;

	public Timer timer = new Timer();

	public BooleanSetting onlySpeed = new BooleanSetting("Only Spped", false, this);
	public BooleanSetting allowFly = new BooleanSetting("Allow Fly", false, this);
	public NumberSetting distance = new NumberSetting("Distance", 2, 1, 6, 0.5, this);

	public TargetStrafe() {
		super("TargetStrafe", Keyboard.KEY_NONE, Category.COMBAT);
		this.addSettings(distance,onlySpeed,allowFly);
	}
	
	public void onEnable() {

	}
	
	public void onDisable() {

	}
	
	public void onEvent(Event e) {
		if(e instanceof EventMove) {
			if(mc.gameSettings.keyBindJump.isKeyDown()) {
				if (Monsoon.manager.killAura.isEnabled()) {
					Killaura aura = Monsoon.manager.killAura;
					if (aura.target == null) {
						return;
					}
					float[] rotations = getRotationsEntity(aura.target);
					movespeed2 = getBaseMoveSpeed();
					if (aura.target.getDistanceToEntity(mc.thePlayer) < distance.getValue()) {
						if (!allowFly.isEnabled()) {
							if (Monsoon.manager.fly.isEnabled()) {
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
							if (Monsoon.manager.speed.isEnabled()) {
								if (mc.gameSettings.keyBindRight.isPressed()) {
									direction = -1;
								}
								if (mc.gameSettings.keyBindLeft.isPressed()) {
									direction = 1;
								}
								canMove = true;
								if (Monsoon.manager.speed.isEnabled()) {
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
						if (Monsoon.manager.speed.isEnabled()) {
							if (direction == -1 || direction == 1) {
								//mc.gameSettings.keyBindJump.pressed = true;
							}
						}

						if (canMove) {
							strafe((EventMove) e, movespeed2, rotations[0], direction, 0.0D);
						}
					} else {
						if (mc.gameSettings.keyBindJump.isKeyDown() && aura.target.getDistanceToEntity(mc.thePlayer) < aura.range.getValue()) {
							strafe((EventMove) e, movespeed2, rotations[0], direction, 1.0D);
						}
						canMove = false;
					}
				} else {
					canMove = false;
				}
			}
		}
		if(e instanceof EventRender3D) {
			if(mc.gameSettings.keyBindJump.isKeyDown()) {
				if (canMove) {
					drawCircle(Monsoon.manager.killAura.target, mc.timer.elapsedPartialTicks, distance.getValue());
				}
			}
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


	private void drawCircle(Entity entity, float partialTicks, double rad) {
		glPushMatrix();
		glDisable(GL_TEXTURE_2D);
		RenderUtil.startSmooth();
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		glLineWidth(1.5f);
		glBegin(GL_LINE_STRIP);

		final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
		final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
		final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

		final float r = ((float) 1 / 255) * Color.WHITE.getRed();
		final float g = ((float) 1 / 255) * Color.WHITE.getGreen();
		final float b = ((float) 1 / 255) * Color.WHITE.getBlue();

		final double pix2 = Math.PI * 2.0D;

		for (int i = 0; i <= 90; ++i) {
			glColor3f(r, g, b);
			glVertex3d(x + rad * Math.cos(i * pix2 / 45.0), y, z + rad * Math.sin(i * pix2 / 45.0));
		}

		glEnd();
		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
		RenderUtil.endSmooth();
		glEnable(GL_TEXTURE_2D);
		glPopMatrix();
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
