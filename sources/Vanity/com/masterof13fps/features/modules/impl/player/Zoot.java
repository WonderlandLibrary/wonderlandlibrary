package com.masterof13fps.features.modules.impl.player;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventMotion;
import com.masterof13fps.manager.eventmanager.impl.EventPacket;
import com.masterof13fps.manager.settingsmanager.Setting;

import com.masterof13fps.features.modules.Category;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleInfo(name = "Zoot", category = Category.PLAYER, description = "Prevents you from bad effects and getting burned")
public class Zoot extends Module {

	public Setting potion = new Setting("Potion", this, true);
	public Setting breath = new Setting("Breath", this, true);
	public Setting firionFreeze = new Setting("Firion Freeze", this, true);

	private final Potion[] potions = { Potion.poison, Potion.blindness, Potion.moveSlowdown, Potion.hunger };


	private boolean isStandingStill() {
		return (Math.abs(mc.thePlayer.motionX) <= 0.01D) && (Math.abs(mc.thePlayer.motionZ) <= 0.01D)
				&& (Math.abs(mc.thePlayer.motionY) <= 0.1D) && (Math.abs(mc.thePlayer.motionY) >= -0.1D);
	}

	@Override
	public void onToggle() {

	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if(event instanceof EventMotion) {
			if(((EventMotion) event).getType() == EventMotion.Type.PRE) {
				Potion[] arrayOfPotion;
				int j = (arrayOfPotion = potions).length;
				for (int i = 0; i < j; i++) {
					Potion potion = arrayOfPotion[i];
					if (mc.thePlayer.getActivePotionEffect(potion) != null) {
						PotionEffect effect = mc.thePlayer.getActivePotionEffect(potion);
						for (int index = 0; index < effect.getDuration() / 20; index++) {
							mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
						}
					}
				}
				if ((mc.thePlayer.isBurning()) && (!mc.thePlayer.isPotionActive(Potion.fireResistance))
						&& (!mc.thePlayer.isImmuneToFire()) && (mc.thePlayer.onGround)
						&& (!mc.thePlayer.isInWater()) && (!mc.thePlayer.isInsideOfMaterial(Material.lava))
						&& (!mc.thePlayer.isInsideOfMaterial(Material.fire))) {
					for (int index = 0; index < 20; index++) {
						mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
					}
				}
			}
		}
		if(event instanceof EventPacket) {
			if (((((EventPacket) event).getPacket() instanceof C03PacketPlayer)) && (isStandingStill()) && (!mc.thePlayer.isUsingItem())) {
				if (potion.isToggled()) {
					event.setCancelled(true);
					return;
				}
				if ((breath.isToggled()) && (mc.thePlayer.isCollidedVertically)
						&& (((mc.thePlayer.isInsideOfMaterial(Material.lava)) && (!mc.thePlayer.isBurning()))
						|| (mc.thePlayer.isInsideOfMaterial(Material.water)))) {
					event.setCancelled(true);
				}
				if ((firionFreeze.isToggled()) && (mc.thePlayer.isBurning()) && (!mc.thePlayer.isInWater())) {
					event.setCancelled(true);
				}
			}
		}
	}
}