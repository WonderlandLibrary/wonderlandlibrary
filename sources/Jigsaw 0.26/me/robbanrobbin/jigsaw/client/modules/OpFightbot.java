package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.TeleportResult;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.potion.Potion;
import net.minecraft.util.Vec3;

public class OpFightbot extends Module {
	
	WaitTimer timer = new WaitTimer();
	
	WaitTimer timerAttack = new WaitTimer();
	
	private static EntityLivingBase en;

	public OpFightbot() {
		super("OP-Fightbot", Keyboard.KEY_NONE, Category.COMBAT, "A fightbot for HvH without anticheat");
	}
	
	@Override
	public void onToggle() {
		en = null;
		AuraUtils.targets.clear();
		super.onToggle();
	}

	@Override
	public void onUpdate() {
		
		if(!timerAttack.hasTimeElapsed(1000 / 12, true)) {
			return;
		}
		
		en = Utils.getClosestEntity(300f);
		
		if(en == null) {
			return;
		}
		
		if(mc.thePlayer.getDistanceToEntity(en) < 14) {
			ArrayList<EntityLivingBase> ens;
			ens = Utils.getClosestEntities(10f);
			AuraUtils.targets = ens;
			AutoBlock.stopBlock();
			for (EntityLivingBase ent : ens) {
				mc.thePlayer.swingItem();
				sendPacket(new C02PacketUseEntity(ent, Action.ATTACK));
				float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(),
						ent.getCreatureAttribute());
				boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
						&& (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
						&& (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
				if ((Jigsaw.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
					mc.thePlayer.onCriticalHit(ent);
				}
				if (sharpLevel > 0.0F) {
					mc.thePlayer.onEnchantmentCritical(ent);
				}
			}
			AutoBlock.startBlock();
			mc.thePlayer.motionY = 0;
		}
		
		if(!timer.hasTimeElapsed(1000 / 2, true)) {
			return;
		}
		
		if(mc.thePlayer.getDistanceToEntity(en) < 2) {
			return;
		}
		
		TeleportResult result = Utils.pathFinderTeleportTo(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), new Vec3(en.posX, en.posY, en.posZ));
		
		if(result.foundPath) {
			
			mc.thePlayer.setPosition(en.posX, en.posY, en.posZ);
			mc.thePlayer.motionY = 0;
			
			ArrayList<EntityLivingBase> ens;
			ens = Utils.getClosestEntities(10f);
			AuraUtils.targets = ens;
			AutoBlock.stopBlock();
			for (EntityLivingBase ent : ens) {
				mc.thePlayer.swingItem();
				sendPacket(new C02PacketUseEntity(ent, Action.ATTACK));
				float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(),
						ent.getCreatureAttribute());
				boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
						&& (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
						&& (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
				if ((Jigsaw.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
					mc.thePlayer.onCriticalHit(ent);
				}
				if (sharpLevel > 0.0F) {
					mc.thePlayer.onEnchantmentCritical(ent);
				}
			}
			AutoBlock.startBlock();
			
		}
		
		super.onUpdate();
	}
	
	public void attackEntity(EntityLivingBase en) {
		AutoBlock.stopBlock();
		mc.thePlayer.swingItem();
		sendPacket(new C02PacketUseEntity(en, Action.ATTACK));
		AutoBlock.startBlock();
		float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(),
				en.getCreatureAttribute());
		boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
				&& (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
				&& (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
		if ((Jigsaw.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
			mc.thePlayer.onCriticalHit(en);
		}
		if (sharpLevel > 0.0F) {
			mc.thePlayer.onEnchantmentCritical(en);
		}
	}

	public static boolean doBlock() {
		return en != null;
	}
	
}
