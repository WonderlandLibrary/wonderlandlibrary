package wtf.monsoon.impl.modules.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.event.EventTarget;
import wtf.monsoon.api.event.impl.EventPostMotion;
import wtf.monsoon.api.event.impl.EventPreMotion;
import wtf.monsoon.api.event.impl.EventRender3D;
import wtf.monsoon.api.event.impl.EventRenderPlayer;
import wtf.monsoon.api.event.impl.EventUpdate;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.BooleanSetting;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.setting.impl.NumberSetting;
import wtf.monsoon.api.util.entity.MovementUtil;
import wtf.monsoon.api.util.entity.PlayerUtils;
import wtf.monsoon.api.util.misc.MathUtils;
import wtf.monsoon.api.util.misc.ServerUtil;
import wtf.monsoon.api.util.misc.Timer;
import wtf.monsoon.api.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import wtf.monsoon.api.Wrapper;

public class Aura extends Module {

	public Random random = new Random();

	public int aa1 = 0;
	public double aa2 = 0;

	public int bb1 = 0;
	public double bb2 = 0;

	public Timer timer = new Timer();
	public NumberSetting aps = new NumberSetting("APS", 10, 1, 20, 0.5, this);
	public NumberSetting range = new NumberSetting("Range", 4, 1, 6, 0.1, this);
	public BooleanSetting disableOnDeath = new BooleanSetting("Disable on death", true, this);
	public BooleanSetting render = new BooleanSetting("Render", true, this);
	public ModeSetting rotations = new ModeSetting("Rotation", this, "NCP", "None", "Basic", "NCP", "Redesky");
	public ModeSetting attackMode = new ModeSetting("Attack Mode", this, "Pre", "Pre", "Post");
	public ModeSetting blockMode = new ModeSetting("Block Mode", this, "Fake", "Fake", "HvH", "Vanilla", "Control");

	public BooleanSetting players = new BooleanSetting("Attack Players", true, this);
	public BooleanSetting animals = new BooleanSetting("Attack Animals", true, this);
	public BooleanSetting mobs = new BooleanSetting("Attack Monsters", true, this);
	public BooleanSetting walls = new BooleanSetting("Through walls", true, this);

	public boolean blocking;
	public int hit;
	public EntityLivingBase target;
	double redeskypercent = 0.3F;
	List<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
	List<Entity> targets = new ArrayList<Entity>();
	private final String[] strings = new String[]{"1st Killer - ", "1st Place - ", "You died! Want to play again? Click here!", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - "};

	public Aura() {
		super("Aura", "Kill entities around you", Keyboard.KEY_NONE, Category.COMBAT);
		this.addSettings(aps, range,rotations,attackMode,disableOnDeath,render,blockMode,players, animals, mobs,walls);
	}

	public void onEnable() {
		super.onEnable();
		targets.clear();
		blocking = false;
		redeskypercent = 0.3F;
		//NotificationManager.show(new Notification(NotificationType.ENABLE, name, name + " was enabled.", 1));
	}

	public void onDisable() {
		super.onDisable();
		targets.clear();
		blocking = false;
	}
	
	@EventTarget
	public void onPreMotion(EventPreMotion event) {
		List<EntityLivingBase> multiEntities = getMultipleTargets(12, range.getValue(), players.isEnabled(), animals.isEnabled(), walls.isEnabled(), mobs.isEnabled());
		if (multiEntities.isEmpty())
			return;

		target = multiEntities.get(0);

		Rotation rotation = getRotationsRandom(target);
		if (!rotations.is("None") && !rotations.is("Redesky")) {
			event.setYaw(rotation.getRotationYaw());
			event.setPitch(rotation.getRotationPitch());
		}
		if(attackMode.is("Pre")) {
			if (timer.hasTimeElapsed((long) (1000L / aps.getValue()), true)) {
				attack(target);
			}
		}
		if(disableOnDeath.isEnabled() && mc.thePlayer.getHealth() < 1) {
			this.toggle();
		}
	}
	
	@EventTarget
	public void onPostMotion(EventPostMotion event) {
		if(attackMode.is("Post")) {
			if (target != null) {
				if (timer.hasTimeElapsed(((long) (1000 / aps.getValue())), false) && shouldAttack(target)) {
					attack(target);
				}
			}
		}
	}
	
	@EventTarget
	public void onRenderPlayer(EventRenderPlayer event) {
		if(target.getDistanceToEntity(Wrapper.mc.thePlayer) < range.getValue() + 0.5f && target != Wrapper.mc.thePlayer && !target.isDead) {
			Rotation rotation = getRotationsRandom(target);
			event.setYaw(rotation.getRotationYaw());
			event.setPitch(rotation.getRotationPitch());
		}
	}
	
	@EventTarget
	public void onMotion(EventUpdate event) {
		this.setSuffix("R " + range.getValue() + " | APS " + aps.getValue());
		if(target != null && blockMode.is("Fake") && Wrapper.mc.thePlayer.getHeldItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && target.getDistanceToEntity(Wrapper.mc.thePlayer) < range.getValue() + 1f && target != Wrapper.mc.thePlayer && !target.isDead) {
			mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 7);
		} else if(target != null && blockMode.is("Vanilla") && Wrapper.mc.thePlayer.getHeldItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && target.getDistanceToEntity(Wrapper.mc.thePlayer) < range.getValue() + 1f && target != Wrapper.mc.thePlayer && !target.isDead) {
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
		}
		if(disableOnDeath.isEnabled() && Wrapper.mc.thePlayer.getHealth() == 0) {
			this.toggle();
		}
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		if(render.isEnabled()) {
			if (target != null && !target.isDead && target.getDistanceToEntity(Wrapper.mc.thePlayer) < range.getValue() + 0.5f && target != Wrapper.mc.thePlayer) {
				if (target.onGround) {
					targetESPBox(target, 0, 1, 0);
				} else {
					targetESPBox(target, 1, 0, 0);
				}
			}
		}
	}
	
	private void attack(EntityLivingBase entityLivingBase) {

		Wrapper.mc.thePlayer.swingItem();
		Wrapper.mc.thePlayer.sendQueue.addToSendQueue(
				new C02PacketUseEntity(entityLivingBase,
						C02PacketUseEntity.Action.ATTACK));

		if(target != null && blockMode.is("Control") && Wrapper.mc.thePlayer.getHeldItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && target.getDistanceToEntity(Wrapper.mc.thePlayer) < range.getValue() + 1f && target != Wrapper.mc.thePlayer && !target.isDead) {
			mc.gameSettings.keyBindUseItem.pressed = true;
		} else if(target != null && blockMode.is("HvH") && Wrapper.mc.thePlayer.getHeldItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() != null && Wrapper.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && target.getDistanceToEntity(Wrapper.mc.thePlayer) < range.getValue() + 1f && target != Wrapper.mc.thePlayer && !target.isDead) {
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
		}
	}

	private boolean shouldAttack(EntityLivingBase e) {
		if(ServerUtil.isHypixel() && PlayerUtils.isOnSameTeam(e)) return false;
		if(e.getHealth() <= 0f) return false;
		if(e.isDead) return false;
		if(Wrapper.mc.thePlayer.getDistanceToEntity(e) > range.getValue()) return false;
		if(e instanceof EntityArmorStand) return false;

		else return true;
	}

	public Rotation getRotationsRandom(EntityLivingBase entity) {
		ThreadLocalRandom threadLocalRandom =  ThreadLocalRandom.current();
		double randomXZ = threadLocalRandom.nextDouble(-0.08, 0.08);
		double randomY = threadLocalRandom.nextDouble(-0.125, 0.125);
		double x = entity.posX + randomXZ;
		double y = entity.posY + (entity.getEyeHeight() / 2.05) + randomY;
		double z = entity.posZ + randomXZ;
		return attemptFacePosition(x, y, z);
	}

	public Rotation attemptFacePosition(double x, double y, double z) {
		double xDiff = x - mc.thePlayer.posX;
		double yDiff = y - mc.thePlayer.posY - 1.2;
		double zDiff = z - mc.thePlayer.posZ;

		double dist = Math.hypot(xDiff, zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180 / Math.PI) - 90;
		float pitch = (float) -(Math.atan2(yDiff, dist) * 180 / Math.PI);
		return new Rotation(yaw, pitch);
	}


	public float[] getRotations(Entity e) {
		double deltaX = e.posX + (e.posX - e.lastTickPosX) - Wrapper.mc.thePlayer.posX,
				deltaY = e.posY - 3.5 + e.getEyeHeight() - Wrapper.mc.thePlayer.posY + Wrapper.mc.thePlayer.getEyeHeight(),
				deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - Wrapper.mc.thePlayer.posZ,
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

	public static List<EntityLivingBase> getMultipleTargets(int max, double range, boolean players, boolean animals, boolean walls, boolean mobs) {
		List<EntityLivingBase> list = new ArrayList<>();
		for (Entity entity : mc.theWorld.loadedEntityList) {
			if (!(entity instanceof EntityLivingBase))
				continue;
			EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			if (mc.thePlayer.getDistanceToEntity(entityLivingBase) > range
					|| (entityLivingBase.canEntityBeSeen(mc.thePlayer) && !walls)
					|| entityLivingBase.isDead
					|| entityLivingBase.getHealth() == 0
					|| list.size() >= max)
				continue;
			if (entityLivingBase instanceof EntityPlayer && !players) continue;
			if (entityLivingBase instanceof EntityAnimal && !animals) continue;
			if (entityLivingBase instanceof EntityMob && !mobs) continue;
			if (entityLivingBase == mc.thePlayer) continue;
			list.add(entityLivingBase);
			if (entityLivingBase.getDisplayName().getUnformattedText().contains(":") || entityLivingBase.getDisplayName().getUnformattedText().contains("!")) list.remove(entityLivingBase);
		}
		return list;
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

	static final class Rotation {

		private final float rotationYaw;

		private final float rotationPitch;

		/**
		 * Constructs a {@code Rotation} instance.
		 * @param rotationYaw - The yaw
		 * @param rotationPitch - The pitch
		 */
		public Rotation(float rotationYaw, float rotationPitch) {
			this.rotationYaw = rotationYaw;
			this.rotationPitch = rotationPitch;
		}

		public float getRotationYaw() {
			return rotationYaw;
		}

		public float getRotationPitch() {
			return rotationPitch;
		}
	}


}
