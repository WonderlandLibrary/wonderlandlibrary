package me.aidanmees.trivia.client.modules.Misc;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C00PacketKeepAlive;

public class DialUpPing extends Module {

	int tries = 0;

	public DialUpPing() {
		super("Dial-Up Ping", Keyboard.KEY_NONE, Category.HIDDEN, "This derps your ping");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {
		
		super.onUpdate();
	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		if (packet instanceof C00PacketKeepAlive) {
			
				packet.cancel();
			
		}
		super.onPacketSent(packet);
	}

}
