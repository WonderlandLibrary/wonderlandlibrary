package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class AntiHunger extends Module {
	
	public AntiHunger() {
		super("AntiHunger", Keyboard.KEY_NONE, Category.PLAYER, "Note: Requires you to stand still! Makes you less hungry and makes potions stay longer. Also enables you to stay in water for longer. It disables natural regen.");
	}
	
	@Override
	public void onPacketSent(PacketEvent event) {
		Packet packet = event.getPacket();
		if(packet instanceof CPacketPlayer
				&& !mc.player.isHandActive()) {
			event.cancel();
		}
		super.onPacketSent(event);
	}
	
}
