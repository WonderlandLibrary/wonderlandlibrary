package dev.monsoon.module.implementation.combat;

import java.util.ArrayList;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Random;

import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.*;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.setting.impl.BooleanSetting;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.module.setting.impl.NumberSetting;
import dev.monsoon.util.entity.MovementUtil;
import dev.monsoon.util.entity.PlayerUtils;
import dev.monsoon.util.misc.MathUtils;
import dev.monsoon.util.misc.ServerUtil;
import dev.monsoon.util.misc.Timer;
import dev.monsoon.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.client.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import net.minecraft.item.ItemSword;
import dev.monsoon.module.enums.Category;

public class Killaura extends Module {

	public Random random = new Random();

	public int aa1 = 0;
	public double aa2 = 0;

	public int bb1 = 0;
	public double bb2 = 0;

	public Timer timer = new Timer();
	public NumberSetting aps = new NumberSetting("APS", 10, 1, 20, 0.5, this);
	public NumberSetting range = new NumberSetting("Range", 4, 1, 6, 0.1, this);
	public BooleanSetting disableOnDeath = new BooleanSetting("Disable on death", true, this);
	public ModeSetting rotation = new ModeSetting("Rotation", this, "NCP", "None", "Basic", "NCP", "Redesky");
	public BooleanSetting autoBlock = new BooleanSetting("Autoblock", true, this);
	public boolean blocking;
	public int hit;
	public EntityLivingBase target;
	double redeskypercent = 0.3F;
	List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
	private final String[] strings = new String[]{"1st Killer - ", "1st Place - ", "You died! Want to play again? Click here!", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - "};

	public Killaura() {
		super("Killaura", Keyboard.KEY_NONE, Category.COMBAT);
		this.addSettings(aps, range,rotation, disableOnDeath, autoBlock);
	}

	public void onEnable() {
		targets.clear();
		blocking = false;
		redeskypercent = 0.3F;
		//NotificationManager.show(new Notification(NotificationType.ENABLE, name, name + " was enabled.", 1));
	}

	public void onDisable() {
		targets.clear();
		blocking = false;
	}

	public void onEvent(Event event) {
		if(event instanceof EventMotion) {
			if(event.isPre()) {
				for (Object entityToCast : mc.theWorld.loadedEntityList) {
					if (entityToCast instanceof EntityLivingBase) {

						targets = (List<EntityLivingBase>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());

						if (!ServerUtil.isMineplex()) {
							targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < range.getValue() && !entity.isInvisible() && !entity.isInvisibleToPlayer(mc.thePlayer) && entity != mc.thePlayer && entity.getHealth() > 0 && !entity.isDead).collect(Collectors.toList());
						} else {
							targets = targets.stream().filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < range.getValue() && !entity.isInvisible() && !entity.isInvisibleToPlayer(mc.thePlayer) && entity != mc.thePlayer && !entity.isDead).collect(Collectors.toList());
						}


						if (!targets.isEmpty()) {
							if(shouldAttack(targets.get(0))) {
								target = targets.get(0);
								if (target != null && !rotation.is("None")) {
									((EventMotion) event).setYaw(getRotationsEntity(target)[0] + random.nextInt(12) - 6);
									((EventMotion) event).setPitch(getRotationsEntity(target)[1] + random.nextInt(12) - 6);
								}
							}
						}
					}
				}
			} else if(event.isPost()) {
				if(target != null) {
					if(timer.hasTimeElapsed(((long) (1000 / aps.getValue())), false) && shouldAttack(target)) {
						attack(target);
					}
				}
			}
		}
		if(event instanceof EventRenderPlayer) {
			if(target.getDistanceToEntity(mc.thePlayer) < range.getValue() + 0.5f && target != mc.thePlayer && !target.isDead) {
				((EventRenderPlayer) event).setYaw(getRotations(target)[0]);
				((EventRenderPlayer) event).setPitch(getRotations(target)[1]);
			}
		}
		if(event instanceof EventUpdate) {
			this.setSuffix("R " + (int) range.getValue() + " | APS " + (int)aps.getValue());
			if(target != null && autoBlock.isEnabled() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && target.getDistanceToEntity(mc.thePlayer) < range.getValue() + 1f && target != mc.thePlayer && !target.isDead) {
				mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 7);
			}
			if(disableOnDeath.isEnabled() && mc.thePlayer.getHealth() == 0) {
				this.toggle();
			}
		}
		if(event instanceof EventRender3D) {
			if(target != null && !target.isDead && target.getDistanceToEntity(mc.thePlayer) < range.getValue() + 0.5f && target != mc.thePlayer) {
				if (target.onGround) {
					targetESPBox(target, 0, 1, 0);
				} else {
					targetESPBox(target, 1, 0, 0);
				}
			}
		}
		if (event instanceof EventPacket) {
		}
	}
	private void attack(EntityLivingBase entityLivingBase) {
		for(int i = 0; i < 2; i++)
			mc.thePlayer.onCriticalHit(target);

		mc.thePlayer.swingItem();
		mc.thePlayer.sendQueue.addToSendQueue(
				new C02PacketUseEntity(entityLivingBase,
						C02PacketUseEntity.Action.ATTACK));
	}

	private boolean shouldAttack(EntityLivingBase e) {
		if(ServerUtil.isHypixel() && PlayerUtils.isOnSameTeam(e)) return false;
		if(e.getHealth() <= 0f) return false;
		if(e.isDead) return false;
		if(mc.thePlayer.getDistanceToEntity(e) > range.getValue()) return false;
		if(e instanceof EntityArmorStand) return false;

		else return true;
	}

	public float[] getRotationsEntity(final EntityLivingBase entity) {
		if (MovementUtil.isMoving()) {
			return getRotations(entity.posX + MathUtils.randomNumber(0.03, -0.03), entity.posY + entity.getEyeHeight() - 0.4D + MathUtils.randomNumber(0.07, -0.07), entity.posZ + MathUtils.randomNumber(0.03, -0.03));
		}
		return getRotations(entity.posX, entity.posY + entity.getEyeHeight() - 0.4D, entity.posZ);
	}

	public float[] getRotations(double posX, double posY, double posZ) {
		final EntityPlayerSP player = mc.thePlayer;
		double x = posX - player.posX;
		double y = posY - (player.posY + player.getEyeHeight());
		double z = posZ - player.posZ;

		double dist = MathHelper.sqrt_double(x * x + z * z);
		float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) (-(Math.atan2(y, dist) * 180.0D / Math.PI));
		return new float[]{yaw, pitch};
	}

	public float[] getRotations(Entity e) {
		double deltaX = e.posX + (e.posX - e.lastTickPosX) - mc.thePlayer.posX,
				deltaY = e.posY - 3.5 + e.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - mc.thePlayer.posZ,
				distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

		float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ)),
				pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

		if(deltaX < 0 && deltaZ < 0) {
			yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}else if(deltaX > 0 && deltaZ < 0) {
			yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
		}
		return new float[] {yaw, pitch};
	}

	public static void targetESPBox(Entity entity, int r, int g, int b)
	{
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		Minecraft.getMinecraft().getRenderManager();
		RenderUtil.drawAxisAlignedBBFilled(
				new AxisAlignedBB(
						entity.boundingBox.minX
								- 0.05
								- entity.posX
								+ (entity.posX - Minecraft.getMinecraft()
								.getRenderManager().renderPosX),
						entity.boundingBox.minY
								- entity.posY
								+ (entity.posY - Minecraft.getMinecraft()
								.getRenderManager().renderPosY),
						entity.boundingBox.minZ
								- 0.05
								- entity.posZ
								+ (entity.posZ - Minecraft.getMinecraft()
								.getRenderManager().renderPosZ),
						entity.boundingBox.maxX
								+ 0.05
								- entity.posX
								+ (entity.posX - Minecraft.getMinecraft()
								.getRenderManager().renderPosX),
						entity.boundingBox.maxY
								+ 0.1
								- entity.posY
								+ (entity.posY - Minecraft.getMinecraft()
								.getRenderManager().renderPosY),
						entity.boundingBox.maxZ
								+ 0.05
								- entity.posZ
								+ (entity.posZ - Minecraft.getMinecraft()
								.getRenderManager().renderPosZ)),r,g,b,true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}

}
