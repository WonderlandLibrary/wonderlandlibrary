package com.enjoytheban.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.enjoytheban.Client;
import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPacketSend;
import com.enjoytheban.api.events.world.EventPostUpdate;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.management.FriendManager;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.module.modules.movement.Speed;
import com.enjoytheban.utils.Helper;
import com.enjoytheban.utils.TimerUtil;
import com.enjoytheban.utils.math.MathUtil;
import com.enjoytheban.utils.math.RotationUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * A somewhat decent killaura lol
 *
 * @author Purity
 */

public class Killaura extends Module {

	// a timer for cps bullshit
	private TimerUtil timer = new TimerUtil();

	private Entity target;
	private List targets = new ArrayList(0);
	private int index;

	// Faithful
	private int xd;
	private int tpdelay;

	// make our module values
	private Numbers<Double> aps = new Numbers("APS", "APS", 10.0, 1.0, 20.0, 0.5);
	private Numbers<Double> reach = new Numbers("Reach", "reach", 4.5, 1.0, 6.0, 0.1);
	private Option<Boolean> blocking = new Option("Autoblock", "autoblock", true);
	private Option<Boolean> players = new Option("Players", "players", true);
	private Option<Boolean> animals = new Option("Animals", "animals", true);
	private Option<Boolean> mobs = new Option("Mobs", "mobs", false);
	private Option<Boolean> invis = new Option("Invisibles", "invisibles", false);
	// private Option<Boolean> tpaura = new Option("TPAura", "TPAura", false);
	private Option<Boolean> god = new Option("Velt God Mode", "Velt God Mode", false);

	private Mode<Enum> mode = new Mode("Mode", "mode", AuraMode.values(), AuraMode.Switch);
	// boolean for our blocking listener
	private boolean isBlocking;

	private Comparator<Entity> angleComparator = Comparator.comparingDouble(e2 -> RotationUtil.getRotations(e2)[0]);

	public Killaura() {
		super("KillAura", new String[] { "ka", "aura", "killa" }, ModuleType.Combat);
		setColor(new Color(226, 54, 30).getRGB());
		addValues(aps, reach, blocking, players, animals, mobs, invis, god, mode);
	}

	@Override
	public void onDisable() {
		targets.clear();
		if (blocking.getValue() && this.canBlock() && mc.thePlayer.isBlocking()) {
			this.stopAutoBlockHypixel();
		}
	}

	@Override
	public void onEnable() {
		this.target = null;
		this.index = 0;
		this.xd = 0;
	}

	// can the player block?
	private boolean canBlock() {
		return mc.thePlayer.getCurrentEquippedItem() != null
				&& mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
	}

	// start stop autoblock methods
	private void startAutoBlockHypixel() {
		if (Helper.onServer("hypixel")) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
			if (mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem())) {
				mc.getItemRenderer().resetEquippedProgress2();
			}
		}
	}

	private void stopAutoBlockHypixel() {
		if (Helper.onServer("hypixel")) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
			mc.playerController.onStoppedUsingItem(mc.thePlayer);

		}
	}

	private void startAutoBlock() {
		if (!Helper.onServer("hypixel")) {
			mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
		}
	}

	private void stopAutoBlock() {
		if (!Helper.onServer("hypixel")) {
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
					C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		}
	}

	private boolean shouldAttack() {
		return this.timer.hasReached(1000 / (aps.getValue() + MathUtil.randomDouble(0, 5)));
	}

	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		setSuffix(mode.getValue());
		this.targets = this.loadTargets();
		this.targets.sort(this.angleComparator);

		if (this.target != null && this.target instanceof EntityPlayer || this.target instanceof EntityMob
				|| this.target instanceof EntityAnimal) {
			this.target = null;
		}

		if (mc.thePlayer.ticksExisted % 50 == 0 && this.targets.size() > 1) {
			++this.index;
		}
		if (!this.targets.isEmpty()) {
			if (this.index >= this.targets.size())
				this.index = 0;

			this.target = (Entity) this.targets.get(this.index);
			if (Helper.onServer("invaded") || Helper.onServer("minemen") || Helper.onServer("faithful")) {
			} else {
				event.setYaw(RotationUtil.faceTarget(target, 1000, 1000, false)[0]);
				if (!AutoHeal.currentlyPotting)
					event.setPitch(RotationUtil.faceTarget(target, 1000, 1000, false)[1]);
			}

			if (blocking.getValue() && this.canBlock() && isBlocking) {
				this.stopAutoBlock();
				if (god.getValue()) {
					event.y += 13.8;
				}
			} else {
				if (god.getValue()) {
					event.y += 4.5;
					event.setOnground(true);
				}
			}
		} else {
			if (blocking.getValue() && this.canBlock() && mc.thePlayer.isBlocking()) {
				this.stopAutoBlockHypixel();
			}
		}
	}

	private void swap(int slot, int hotbarNum) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
	}

	@EventHandler
	private void onUpdatePost(EventPostUpdate e) {
		if(Helper.onServer("enjoytheban")) {
			reach.setValue(4.0d);
			aps.setValue(12.0d);
			god.setValue(false);
		}
		if (!Helper.onServer("faithful")) {
			if (god.getValue()) {
				Speed speed = (Speed) Client.instance.getModuleManager().getModuleByClass(Speed.class);
				if (speed.isEnabled()) {
					speed.setEnabled(false);
				}
			}
			if (this.target != null) {
				double angle = Math.toRadians(target.rotationYaw - 90.0f + 360.0f) % 360.0f;
				/*
				 * if(tpaura.getValue()) mc.thePlayer.setPosition(target.posX + Math.cos(angle)
				 * * 2, target.posY + 0.8, target.posZ + Math.sin(angle) * 2);
				 */
				if (shouldAttack()) {
					Criticals crits = (Criticals) Client.instance.getModuleManager().getModuleByClass(Criticals.class);
					if (crits.isEnabled()) {
						crits.hypixelCrit();
					}
					if (crits.isEnabled())
						crits.packetCrit();
					if (mode.getValue() == AuraMode.Switch) {
						this.attack();
					} else {
						this.swap(9, mc.thePlayer.inventory.currentItem);
						this.attack();
						this.attack();
						this.attack();
						crits.offsetCrit();
						this.swap(9, mc.thePlayer.inventory.currentItem);
						this.attack();
						this.attack();
						crits.offsetCrit();
					}
					if (!mc.thePlayer.isBlocking() && canBlock() && blocking.getValue()) {
						startAutoBlockHypixel();
					}
					this.timer.reset();
				}
				if (canBlock() && blocking.getValue() && !mc.thePlayer.isBlocking()) {
					startAutoBlock();
				}
			}
		}
	}

	private List<Entity> loadTargets() {
		return (List<Entity>) mc.theWorld.loadedEntityList.stream().filter(
				e -> mc.thePlayer.getDistanceToEntity((Entity) e) <= reach.getValue() && this.qualifies((Entity) e))
				.collect(Collectors.toList());
	}

	private boolean qualifies(Entity e) {
		if (e == mc.thePlayer)
			return false;
		AntiBot ab = (AntiBot) Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
		if (ab.isServerBot(e))
			return false;
		if (!e.isEntityAlive())
			return false;
		if (FriendManager.isFriend(e.getName()))
			return false;
		if (e instanceof EntityPlayer && players.getValue())
			return true;
		if (e instanceof EntityMob && mobs.getValue())
			return true;
		if (e instanceof EntityAnimal && animals.getValue())
			return true;
		if (e.isInvisible() && !invis.getValue())
			return true;
		return false;
	}

	private void attack() {
		if (blocking.getValue() && this.canBlock() && mc.thePlayer.isBlocking() && qualifies(target)) {
			this.stopAutoBlockHypixel();
		}
		if (Helper.onServer("invaded"))
			mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
		else
			mc.thePlayer.swingItem();
		mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));
	}

	@EventHandler
	private void blockinglistener(EventPacketSend packet) {
		if (packet.getPacket() instanceof C07PacketPlayerDigging) {
			C07PacketPlayerDigging packetPlayerDigging = (C07PacketPlayerDigging) packet.getPacket();
			if (packetPlayerDigging.func_180762_c().equals(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)) {
				isBlocking = false;

			}
		}
		if (packet.getPacket() instanceof C08PacketPlayerBlockPlacement) {
			C08PacketPlayerBlockPlacement blockPlacement = (C08PacketPlayerBlockPlacement) packet.getPacket();

			if (blockPlacement.getStack() != null && blockPlacement.getStack().getItem() instanceof ItemSword
					&& blockPlacement.func_179724_a().equals(new BlockPos(-1, -1, -1))) {
				isBlocking = true;
			}
		}
	}

	public void mmcAttack(final EntityLivingBase entity) {
		this.mmcAttack(entity, false);
	}

	public void mmcAttack(final EntityLivingBase entity, final boolean crit) {
		mc.thePlayer.swingItem();
		final float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(),
				entity.getCreatureAttribute());
		final boolean vanillaCrit = mc.thePlayer.fallDistance > 0.0f && !mc.thePlayer.onGround
				&& !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater()
				&& !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null;
		mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
		if (crit || vanillaCrit) {
			mc.thePlayer.onCriticalHit(entity);
		}
		if (sharpLevel > 0.0f) {
			mc.thePlayer.onEnchantmentCritical(entity);
		}
	}

	enum AuraMode {
		Switch, Tick
	}
}