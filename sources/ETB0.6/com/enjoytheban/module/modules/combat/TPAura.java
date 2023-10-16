package com.enjoytheban.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.events.world.EventTick;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.TimerUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

/*
 * Created by Jutting on Oct 12, 2018
 */

public class TPAura extends Module {

	private int ticks;
	private List<EntityLivingBase> loaded = new ArrayList<>();
	private EntityLivingBase target;
	private int tpdelay;
	public boolean criticals;
	private TimerUtil timer = new TimerUtil();

	public TPAura() {
		super("TPAura", new String[] { "tpaura" }, ModuleType.Combat);
	}

	@EventHandler
	public void onUpdate(EventTick event) {
		this.setColor(new Color(255,50,70).getRGB());
		++this.ticks;
		++this.tpdelay;
		if (this.ticks >= 20 - this.speed()) {
			this.ticks = 0;
			for (Object object : mc.theWorld.loadedEntityList) {
				if (object instanceof EntityLivingBase) {
					EntityLivingBase entity = (EntityLivingBase) object;
					if (entity instanceof EntityPlayerSP) {
						continue;
					}
					if (mc.thePlayer.getDistanceToEntity(entity) > 10.0f) {
						continue;
					}
					if (entity.isEntityAlive()) {
						if (this.tpdelay >= 4) {
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
									entity.posX, entity.posY, entity.posZ, false));
						}
						if (mc.thePlayer.getDistanceToEntity(entity) < 10.0f) {
							attack(entity);
						}
					}
				}
			}
		}
	}

	public void attack(EntityLivingBase entity) {
		this.attack(entity, false);
	}

	public void attack(EntityLivingBase entity, boolean crit) {
		mc.thePlayer.swingItem();
		float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), entity.getCreatureAttribute());
		boolean vanillaCrit = mc.thePlayer.fallDistance > 0.0f && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder()
				&& !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness)
				&& mc.thePlayer.ridingEntity == null;
		mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
		if (crit || vanillaCrit) {
			mc.thePlayer.onCriticalHit(entity);
		}
		if (sharpLevel > 0.0f) {
			mc.thePlayer.onEnchantmentCritical(entity);
		}
	}

	private int speed() {
		return 8;
	}

}
