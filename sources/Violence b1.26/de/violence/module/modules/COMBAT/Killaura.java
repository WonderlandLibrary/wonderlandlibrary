package de.violence.module.modules.COMBAT;

import de.violence.friend.FriendManager;
import de.violence.gui.VSetting;
import de.violence.irc.IRCPlayer;
import de.violence.module.Module;
import de.violence.module.modules.OTHER.AntiBots;
import de.violence.module.modules.OTHER.Teams;
import de.violence.module.modules.PLAYER.AutoArmor;
import de.violence.module.ui.Category;
import de.violence.ui.BlickWinkel3D;
import de.violence.ui.Location3D;
import de.violence.ui.Timings;
import de.violence.utils.RotationHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class Killaura extends Module {
	private VSetting sSwitchDelay = new VSetting("Switch Delay", this, 1.0D, 500.0D, 1.0D, true);
	private VSetting sPreHitRange = new VSetting("Pre Hit Range Extension", this, 0.1D, 3.0D, 0.1D, false);
	private VSetting sRange = new VSetting("Range", this, 1.0D, 6.0D, 1.0D, false);
	private VSetting sCPS = new VSetting("CPS", this, 1.0D, 20.0D, 1.0D, true);
	private VSetting sLookSleep = new VSetting("Look Sleep", this, 50.0D, 500.0D, 50.0D, true);
	private VSetting sFailureDelay = new VSetting("Failure Delay", this, 50.0D, 2050.0D, 50.0D, true);
	private VSetting sYawDist = new VSetting("Yaw Dist", this, 0.0D, 360.0D, 0.0D, false);
	private VSetting sAccellerateSpeed = new VSetting("Rot. Accellerate Speed", this, 1.0D, 50.0D, 1.0D, false);
	private VSetting bAccellerate = new VSetting("Accellerate Rotations", this, false,
			Arrays.asList(new String[] { "Rot. Accellerate Speed-Killaura" }));
	private VSetting mMode = new VSetting("Mode", this, Arrays.asList(new String[] { "Nearest", "Switch", "Fov" }),
			"Nearest");
	private VSetting bESP = new VSetting("ESP", this, false);
	private VSetting bSlowdownMovement = new VSetting("Slow Movement", this, false,
			Arrays.asList(new String[] { "Yaw Dist-Killaura" }));
	private VSetting bFailure = new VSetting("Failure", this, false,
			Arrays.asList(new String[] { "Failure Delay-Killaura" }));
	private VSetting bRaycast = new VSetting("Raycast", this, false);
	private VSetting bAnimals = new VSetting("Animals / Mobs", this, false);
	private VSetting bFixEquals = new VSetting("Fix Same-Rotations", this, false);
	private VSetting bFixEqualsClickSpeed = new VSetting("Fix Same-Clickspeed", this, false);
	private VSetting bFixHeuristic = new VSetting("Fix Heuristic", this, false,
			Arrays.asList(new String[] { "Look Sleep-Killaura" }));
	private VSetting bBlockhit = new VSetting("Blockhit", this, false);
	private VSetting bdoublesTapp = new VSetting("Doubles Tapp", this, false);
	private VSetting bSmoothAim = new VSetting("Smooth Aim", this, false);
	private VSetting bPreHit = new VSetting("Pre hit", this, false,
			Arrays.asList(new String[] { "Pre Hit Range Extension-Killaura" }));
	private VSetting bPredict = new VSetting("Predict next move", this, false);
	private VSetting bBetterhits = new VSetting("Betterhits", this, false);
	private VSetting bAutoblock = new VSetting("Autoblock", this, false);
	private Timings delayTimings = new Timings();
	private Timings itemTimings = new Timings();
	private Timings failureTimings = new Timings();
	private long startLooking;
	private Entity lastEntity;
	private long lastLook;
	private long lastSwitch;
	private boolean isBlocking;
	private float lastYaw;
	private float lastPitch;
	public static boolean shouldAction;
	private ArrayList switchList = new ArrayList();
	private Entity currentEntity;
	private float rotatesNeeded;

	public Killaura() {
		super("Killaura", Category.COMBAT);
	}

	public void onDisable() {
		this.lastEntity = null;
		this.startLooking = 0L;
		this.switchList.clear();
		shouldAction = false;
		this.setUnblock();
		super.onDisable();
	}

	public void onUpdate() {
		this.nameAddon = this.mMode.getActiveMode();
		if (this.mc.thePlayer.ticksExisted >= 40 && this.mc.thePlayer.getHealth() > 0.0F) {
			if (Module.getByName("AutoArmor").isToggled() && AutoArmor.openedInventory) {
				this.mc.thePlayer.sendQueue
						.addToSendQueue(new C0DPacketCloseWindow(this.mc.thePlayer.inventoryContainer.windowId));
				AutoArmor.openedInventory = false;
			}

			Entity e = this.getNearest();
			String var2;
			switch ((var2 = this.mMode.getActiveMode()).hashCode()) {
			case -1805606060:
				if (var2.equals("Switch")) {
					e = this.getSwitchEntity();
				}
				break;
			case 70829:
				if (var2.equals("Fov")) {
					e = this.getFovNearest();
				}
			}

			this.currentEntity = e;
			if (e == null) {
				this.startLooking = 0L;
				this.lastEntity = null;
				this.setUnblock();
				this.isBlocking = false;
				ItemRenderer.isBlocking = false;
				if ((double) (System.currentTimeMillis() - this.lastLook) < 320.0D
						&& this.bSlowdownMovement.isToggled()) {
					this.slowdownMovement((Entity) null, true);
				}

				shouldAction = false;
			} else {
				if (this.isBlocking && this.mc.thePlayer.getCurrentEquippedItem() == null
						|| this.mc.thePlayer.getCurrentEquippedItem() != null
								&& !(this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword)) {
					ItemRenderer.isBlocking = false;
				}

				if (((EntityLivingBase) e).deathTime > 0 || e.isInvisible()) {
					return;
				}

				boolean s = false;
				Entity playerEntity = e;
				if (!this.findRaycasts(e).isEmpty() && this.bRaycast.isToggled()) {
					e = (Entity) this.findRaycasts(e).get(0);
					s = true;
				}

				if (this.bSlowdownMovement.isToggled()) {
					this.slowdownMovement(e, false);
				}

				boolean a = (double) (System.currentTimeMillis() - this.startLooking) >= this.sLookSleep.getCurrent();
				if (!this.bFixHeuristic.isToggled()) {
					a = true;
				}

				if (this.startLooking == 0L || this.lastEntity == null) {
					a = false;
				}

				boolean hasToWait = false;
				if (this.lastEntity != null && this.lastEntity != e) {
					hasToWait = true;
				}

				if ((double) (System.currentTimeMillis() - this.lastSwitch) > this.sSwitchDelay.getCurrent()) {
					hasToWait = false;
				}

				if (!hasToWait) {
					shouldAction = true;
					if (this.sendLooks(e,
							(double) (System.currentTimeMillis() - this.startLooking) < this.sLookSleep.getCurrent())) {
						if (Module.getByName("Criticals").isToggled()) {
							this.handleCriticals(playerEntity);
						}

						if (this.delayReached() && (a || s)) {
							this.hitObject(e);
						}
					}
				} else {
					RotationHandler.server_yaw = this.getRotations(this.lastEntity)[0];
					RotationHandler.server_pitch = this.getRotations(this.lastEntity)[1];
				}
			}

			super.onUpdate();
		}
	}

	public void onFrameRender() {
		if (this.bESP.isToggled() && this.currentEntity != null && !this.currentEntity.isInvisible()) {
			double tarMotionX = this.currentEntity.posX - this.currentEntity.prevPosX;
			double tarMotionZ = this.currentEntity.posZ - this.currentEntity.prevPosZ;
			double var10000 = this.currentEntity.posX + tarMotionX;
			Minecraft.getMinecraft().getRenderManager();
			double x = var10000 - RenderManager.renderPosX;
			var10000 = this.currentEntity.posY + 1.0D;
			Minecraft.getMinecraft().getRenderManager();
			double y = var10000 - RenderManager.renderPosY;
			var10000 = this.currentEntity.posZ + tarMotionZ;
			Minecraft.getMinecraft().getRenderManager();
			double z = var10000 - RenderManager.renderPosZ;
			GL11.glEnable(3042);
			GL11.glLineWidth(3.0F);
			if (((EntityLivingBase) this.currentEntity).hurtTime <= 0) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			} else {
				GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
			}

			GL11.glDisable(3553);
			GL11.glDisable(2929);
			GL11.glDepthMask(false);
			double sin = Math.sin((double) System.currentTimeMillis() / 500.0D) * 50.0D;
			double xA = sin / 100.0D;
			double zA = sin / 100.0D;
			double yA = sin / 100.0D;
			if (((EntityLivingBase) this.currentEntity).hurtTime <= 0) {
				RenderGlobal
						.func_181561_a(new AxisAlignedBB(x - xA, y - yA - 0.1D, z - xA, x + zA, y + yA + 0.1D, z + zA));
			} else {
				RenderGlobal.func_181561_a(
						new AxisAlignedBB(x - 0.2D, y - yA - 0.1D, z - 0.2D, x + 0.2D, y + yA + 0.2D, z + 0.2D));
			}

			GL11.glEnable(3553);
			GL11.glEnable(2929);
			GL11.glDepthMask(true);
			GL11.glDisable(3042);
		}

		super.onFrameRender();
	}

	private void betterHit(Entity e) {
		RotationHandler.server_pitch += 15.0F;
		this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
				C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.mc.thePlayer.getPosition(), EnumFacing.DOWN));
		this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, new Vec3(e.posX, e.posY, e.posZ)));
	}

	private void hitObject(Entity e) {
		if (!this.hitFailure() || !this.bFailure.isToggled()) {
			if (this.isBlocking) {
				this.setUnblock();
			}

			if (this.bBetterhits.isToggled() && Math.random() > 0.4D) {
				this.betterHit(e);
			}

			this.swingItem();
			this.sendAttackPacket(e);
			if (this.bBlockhit.isToggled()
					&& (double) this.mc.thePlayer.getDistanceToEntity(e) <= this.sRange.getCurrent() - 1.5D
					|| this.bAutoblock.isToggled() && this.shouldBlock(e)) {
				this.setBlocking();
			}

			if (Module.getByName("Criticals").isToggled()) {
				this.takeDownCritPackets();
			}

			this.switchList.remove(e);
			this.resetDelay();
		}
	}

	private boolean shouldBlock(Entity e) {
		if (this.mc.thePlayer.getHealth() <= 10.0F) {
			return true;
		} else if (!this.mc.thePlayer.onGround && this.mc.thePlayer.hurtTime > 0) {
			return true;
		} else if (Math.abs(e.lastTickPosX - e.posX) <= 3.5D && Math.abs(e.lastTickPosZ - e.posZ) <= 3.5D) {
			double tarMotionX = e.posX - e.prevPosX;
			double tarMotionY = e.posY - e.prevPosY;
			double tarMotionZ = e.posZ - e.prevPosZ;
			return tarMotionX <= 0.2D && tarMotionZ <= 0.2D && tarMotionY > 1.0D;
		} else {
			return true;
		}
	}

	private int getRotateDirection(Entity fromEntity, Entity e) {
		boolean direction = true;
		float yaw1 = MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw);
		if (fromEntity != null) {
			yaw1 = this.getRotations(fromEntity)[0];
		}

		float yaw2 = this.getRotations(e)[0];
		double distX = Math.abs(this.mc.thePlayer.posX - e.posX);
		double distZ = Math.abs(this.mc.thePlayer.posZ - e.posZ);
		double differenz = (double) Math.abs(yaw2 - yaw1);
		float yaw = MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw);
		yaw = (float) ((double) yaw * (distX + distZ));
		boolean behind = (double) yaw < 0.0D;
		int direction1;
		if (behind) {
			direction1 = differenz > 180.0D ? 1 : 0;
		} else {
			direction1 = differenz > 180.0D ? 0 : 1;
		}

		return direction1;
	}

	private void slowdownMovement(Entity e, boolean backwards) {
		double distance;
		if (backwards) {
			distance = 1.25D;
			this.mc.thePlayer.motionX /= distance;
			this.mc.thePlayer.motionZ /= distance;
		} else {
			distance = (double) Math
					.abs(MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw) - this.getRotations(e)[0]);
			if (distance >= this.sYawDist.getMax() / this.sYawDist.getCurrent() * 10.0D) {
				double motion = 1.25D;
				this.mc.thePlayer.motionX /= motion;
				this.mc.thePlayer.motionZ /= motion;
			}

		}
	}

	private void resetDelay() {
		this.delayTimings.resetTimings();
	}

	private boolean hitFailure() {
		if (this.failureTimings.hasReached((long) this.sFailureDelay.getCurrent())) {
			this.failureTimings.resetTimings();
			return true;
		} else {
			return false;
		}
	}

	private void resetFailure() {
		this.failureTimings.resetTimings();
	}

	private void setBlocking() {
		if (!this.mc.thePlayer.isInWater()) {
			if (this.mc.thePlayer.getCurrentEquippedItem() != null
					&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
				this.mc.thePlayer.motionX /= 1.15D;
				this.mc.thePlayer.motionZ /= 1.15D;
				C08PacketPlayerBlockPlacement block = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
						this.mc.thePlayer.getCurrentEquippedItem(), 0.0F, 0.0F, 0.0F);
				this.mc.thePlayer.sendQueue.addToSendQueue(block);
				this.isBlocking = true;
				ItemRenderer.isBlocking = true;
			}

		}
	}

	private void setUnblock() {
		if (this.mc.thePlayer.getCurrentEquippedItem() != null
				&& this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
			C07PacketPlayerDigging unblock = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
					new BlockPos(0, 0, 0), EnumFacing.DOWN);
			this.mc.thePlayer.sendQueue.addToSendQueue(unblock);
			this.isBlocking = false;
		}

	}

	private void swingItem() {
		if (this.itemTimings.hasReached(200L)) {
			this.mc.thePlayer.swingItem();
			this.itemTimings.resetTimings();
		} else {
			this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
		}

	}

	private void sendAttackPacket(Entity e) {
		this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
	}

	private void handleCriticals(Entity e) {
		String mode = VSetting.getByName("Mode", Module.getByName("Criticals")).getActiveMode();
		if (mode.equalsIgnoreCase("Crack")) {
			for (int i = 0; i < (int) VSetting.getByName("Crack Size", Module.getByName("Criticals"))
					.getCurrent(); ++i) {
				this.mc.thePlayer.onCriticalHit(e);
			}

			this.mc.thePlayer.onEnchantmentCritical(e);
		} else if (mode.equalsIgnoreCase("Legit Jump")) {
			if (this.mc.thePlayer.onGround) {
				this.mc.thePlayer.jump();
			}
		} else if (mode.equalsIgnoreCase("Minihop")) {
			if (this.mc.thePlayer.onGround) {
				this.mc.thePlayer.motionY = 0.05D;
			}
		} else if (mode.equalsIgnoreCase("Packet")) {
			this.mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
							this.mc.thePlayer.getEntityBoundingBox().minY + 0.04D, this.mc.thePlayer.posZ, false));
			this.mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
							this.mc.thePlayer.getEntityBoundingBox().minY + 0.1D, this.mc.thePlayer.posZ, false));
			this.mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
							this.mc.thePlayer.getEntityBoundingBox().minY + 0.17D, this.mc.thePlayer.posZ, false));
		}

	}

	private void takeDownCritPackets() {
		String mode = VSetting.getByName("Mode", Module.getByName("Criticals")).getActiveMode();
		if (mode.equalsIgnoreCase("Packet")) {
			this.mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
							this.mc.thePlayer.getEntityBoundingBox().minY + 0.17D, this.mc.thePlayer.posZ, false));
			this.mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
							this.mc.thePlayer.getEntityBoundingBox().minY + 0.1D, this.mc.thePlayer.posZ, false));
			this.mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
							this.mc.thePlayer.getEntityBoundingBox().minY + 0.04D, this.mc.thePlayer.posZ, false));
			this.mc.thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
							this.mc.thePlayer.getEntityBoundingBox().minY, this.mc.thePlayer.posZ, true));
		}

	}

	private boolean isInRange(Entity e) {
		Location3D startLoc = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.6D,
				this.mc.thePlayer.posZ);
		Location3D endLoc = new Location3D(e.posX, e.posY + (double) e.getEyeHeight() - 1.2D, e.posZ);
		double range = this.sRange.getCurrent();
		if (e instanceof EntityLivingBase && this.bPreHit.isToggled() && ((EntityLivingBase) e).isSwingInProgress) {
			range += this.sPreHitRange.getCurrent();
		}

		return startLoc.distance(endLoc) <= range;
	}

	private float[] getRotations(Entity e) {
		double x = e.posX;
		double y = e.posY;
		double z = e.posZ;
		double fX = this.mc.thePlayer.posX;
		double fY = this.mc.thePlayer.posY;
		double fZ = this.mc.thePlayer.posZ;
		double tarMotionX = e.posX - e.prevPosX;
		double tarMotionY = e.posY - e.prevPosY;
		double tarMotionZ = e.posZ - e.prevPosZ;
		double pX = this.mc.thePlayer.motionX;
		double pY = this.mc.thePlayer.motionY;
		double pZ = this.mc.thePlayer.motionZ;
		if (!this.bPredict.isToggled()) {
			tarMotionX = 0.0D;
			tarMotionY = 0.0D;
			tarMotionZ = 0.0D;
			pX = 0.0D;
			pY = 0.0D;
			pZ = 0.0D;
		}

		Location3D startLoc = new Location3D(fX, fY + 1.1D, fZ);
		startLoc.add(pX, pY, pZ);
		Location3D endLoc = new Location3D(x, y + (double) e.getEyeHeight() - 1.2D, z);
		endLoc.add(tarMotionX, tarMotionY, tarMotionZ);
		BlickWinkel3D blickWinkel3D = new BlickWinkel3D(startLoc, endLoc);
		return new float[] { (float) blickWinkel3D.getYaw(), (float) blickWinkel3D.getPitch() };
	}

	private boolean sendLooks(Entity e, boolean confidense) {
		boolean another = false;
		if (this.lastEntity == null && !e.isInvisible()) {
			this.rotatesNeeded = Math
					.abs(MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw) - this.getRotations(e)[0]);
			this.lastEntity = e;
			this.startLooking = System.currentTimeMillis();
		} else if (this.lastEntity != null && this.lastEntity != e && !e.isInvisible()) {
			another = true;
			this.rotatesNeeded = Math.abs(this.getRotations(e)[0] - this.getRotations(this.lastEntity)[0]);
			this.lastEntity = e;
			this.startLooking = System.currentTimeMillis();
		}

		int direction = this.getRotateDirection((Entity) null, e);
		if (another) {
			direction = this.getRotateDirection(this.lastEntity, e);
		}

		if (this.rotatesNeeded > 0.0F) {
			this.rotatesNeeded -= (float) this.sAccellerateSpeed.getCurrent();
		}

		if (String.valueOf(this.rotatesNeeded).contains("E")) {
			this.rotatesNeeded = 0.0F;
		}

		float yaw = this.getRotations(e)[0];
		float pitch = this.getRotations(e)[1];
		if (another) {
			yaw = this.getRotations(this.lastEntity)[0];
			pitch = this.getRotations(this.lastEntity)[1];
		}

		if (this.bAccellerate.isToggled() && !e.isInvisible() && this.lastEntity != null
				&& !this.lastEntity.isInvisible() && this.rotatesNeeded > 0.0F) {
			if (direction == 0) {
				yaw -= this.rotatesNeeded;
			} else {
				yaw += this.rotatesNeeded;
			}
		}

		if (this.bFixEquals.isToggled() && this.rotatesNeeded == 0.0F) {
			double a = Math.sin((double) System.currentTimeMillis() / 500.0D) * 15.0D;
			pitch = (float) ((double) pitch + a);
		}

		float a1 = yaw;
		float b = pitch;
		if (this.bSmoothAim.isToggled()) {
			float distYaw = this.lastYaw - yaw;
			float distPitch = this.lastPitch - pitch;
			yaw -= distYaw;
			pitch -= distPitch;
		}

		RotationHandler.server_pitch = pitch;
		RotationHandler.server_yaw = yaw;
		this.lastYaw = a1;
		this.lastPitch = b;
		if (this.rotatesNeeded > 0.0F && this.bAccellerate.isToggled()) {
			this.lastSwitch = System.currentTimeMillis();
			return false;
		} else {
			this.lastLook = System.currentTimeMillis();
			return (double) Math.abs(this.lastYaw - yaw) <= 15.0D && (double) Math.abs(this.lastPitch - pitch) <= 15.0D
					|| !this.bSmoothAim.isToggled();
		}
	}

	private List<Entity> getList() {
		final ArrayList<Entity> arrayList = new ArrayList<Entity>();
		for (final Object e2 : this.mc.theWorld.loadedEntityList) {
			Entity e = (Entity) e2;
			if (e != null && e instanceof EntityLivingBase) {
				arrayList.add(e);
			}
		}
		arrayList.removeIf(new Predicate<Entity>() {
			@Override
			public boolean test(final Entity e) {
				return !Killaura.this.isInRange(e)
						|| (!Killaura.this.bAnimals.isToggled() && !(e instanceof EntityPlayer))
						|| ((EntityLivingBase) e).getHealth() <= 1.0f || e == Killaura.this.mc.thePlayer
						|| (Module.getByName("ClientFriends").isToggled()
								&& FriendManager.getAliasOf(e.getName()) != null)
						|| (Module.getByName("Teams").isToggled() && Teams.isInTeam((EntityLivingBase) e))
						|| (Module.getByName("AntiBots").isToggled() && !AntiBots.canHit(e))
						|| (Module.getByName("ClientFriends").isToggled()
								&& VSetting.getByName("Irc-Friends", Module.getByName("ClientFriends")).isToggled()
								&& IRCPlayer.ignToPlayer.containsKey(e.getName()))
						|| e.isInvisible();
			}
		});
		return arrayList;
	}

	private List findRaycasts(Entity e) {
		ArrayList arrayList = new ArrayList();
		Iterator var4 = this.mc.theWorld.loadedEntityList.iterator();

		while (var4.hasNext()) {
			Entity rs = (Entity) var4.next();
			if ((double) rs.getDistanceToEntity(e) <= 0.5D && rs.isInvisible() && rs != e) {
				arrayList.add(rs);
			}
		}

		return arrayList;
	}

	private Entity getNearest() {
		Entity e = null;
		double n = this.sRange.getCurrent();
		Iterator var5 = this.getList().iterator();

		while (var5.hasNext()) {
			Entity available = (Entity) var5.next();
			if (e == null) {
				e = available;
				n = (double) this.mc.thePlayer.getDistanceToEntity(available);
			}

			if (n > (double) this.mc.thePlayer.getDistanceToEntity(available)) {
				e = available;
				n = (double) this.mc.thePlayer.getDistanceToEntity(available);
			}
		}

		return e;
	}

	private Entity getFovNearest() {
		Entity e = null;
		float fov = 360.0F;
		Iterator var4 = this.getList().iterator();

		while (var4.hasNext()) {
			Entity available = (Entity) var4.next();
			float yaw = Math.abs(
					MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw) - this.getRotations(available)[0]);
			if (e == null) {
				e = available;
				fov = yaw;
			}

			if (fov > yaw) {
				e = available;
				fov = yaw;
			}
		}

		return e;
	}

	private Entity getSwitchEntity() {
		Iterator var2 = ((ArrayList) this.switchList.clone()).iterator();

		while (var2.hasNext()) {
			Entity inList = (Entity) var2.next();
			if (!this.isInRange(inList)) {
				this.switchList.remove(inList);
			}

			if (inList instanceof EntityLivingBase && ((EntityLivingBase) inList).getHealth() <= 1.0F) {
				this.switchList.remove(inList);
			}
		}

		if (this.switchList.isEmpty()) {
			this.switchList = (ArrayList) this.getList();
		}

		if (this.switchList.isEmpty()) {
			return this.getNearest();
		} else {
			return (Entity) this.switchList.get(0);
		}
	}

	private boolean delayReached() {
		return this.bFixEqualsClickSpeed.isToggled()
				? this.delayTimings.hasReached(this.cpsToMs() + (long) (new Random()).nextInt(70))
				: this.delayTimings.hasReached(this.cpsToMs());
	}

	private long cpsToMs() {
		return (long) (this.sCPS.getMax() / this.sCPS.getCurrent() * 40.0D);
	}
}
