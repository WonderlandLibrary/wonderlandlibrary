package com.enjoytheban.module.modules.player;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.Type;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Simple zoot that negates bad potion effects and fire
 * @author purity
 *
 */

public class Zoot extends Module {

	public Zoot() {
		super("Zoot", new String[] { "Firion", "antipotion", "antifire" }, ModuleType.Player);
		setColor(new Color(208,203,229).getRGB());
	}

	@EventHandler
	private void onUpdate(EventPreUpdate e) {
		//loop all the potions
		for (Potion potion : Potion.potionTypes) {
			PotionEffect effect;
			//set effect to a variable + bunch of checks
			if (e.getType() == Type.PRE && potion != null && ((effect = mc.thePlayer.getActivePotionEffect(potion)) != null && potion.isBadEffect()
					|| mc.thePlayer.isBurning() && !mc.thePlayer.isInWater() && mc.thePlayer.onGround)) {
				//burning send 20 packets, bad potion send the duration ammount / 20
				for (int i = 0; mc.thePlayer.isBurning() ? i < 20 : i < effect.getDuration() / 20; i++) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				}
			}
		}
	}
}
