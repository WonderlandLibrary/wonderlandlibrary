package com.enjoytheban.module.modules.player;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPacketRecieve;
import com.enjoytheban.api.events.world.EventPacketSend;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

/**
 * A simple antikb that works on hypixel
 * 
 * @author Purity
 */

public class AntiVelocity extends Module {

	private Numbers<Double> percentage = new Numbers("Percentage", "percentage", 0.0, 0.0, 100.0, 5.0);

	public AntiVelocity() {
		super("Velocity", new String[] { "antivelocity", "antiknockback", "antikb" }, ModuleType.Player);
		addValues(percentage);
		setColor(new Color(191,191,191).getRGB());
	}

	// Method for AntiVelocity
	@EventHandler
	private void onPacket(EventPacketRecieve e) {
		// if the packet is S12PacketEntityVelocity or S27PacketExplosion (so it works
		// on hypixel)
		if (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion) {
			// cancel the event so the player doesn't take kb
			if (percentage.getValue().equals(0.0)) {
				e.setCancelled(true);
			} else {
				//motion shit
				S12PacketEntityVelocity packet = (S12PacketEntityVelocity) e.getPacket();
				packet.field_149415_b = (int) (percentage.getValue() / 100);
				packet.field_149416_c = (int) (percentage.getValue() / 100);
				packet.field_149414_d = (int) (percentage.getValue() / 100);
			}
		}
	}
}