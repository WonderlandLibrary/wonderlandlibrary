package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.PacketEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class AntiRotate extends Module {

	public AntiRotate() {
		super("AntiRotate", Keyboard.KEY_NONE, Category.MOVEMENT, "Prevents the server from controlling your rotation.");
	}

	@Override
	public void onPacketRecieved(PacketEvent event) {
		Packet packetIn = event.getPacket();
		if(mc.player != null) {
			if (packetIn instanceof SPacketPlayerPosLook) {
				SPacketPlayerPosLook packet = (SPacketPlayerPosLook) packetIn;
				packet.yaw = mc.player.rotationYawHead;
				packet.pitch = mc.player.rotationPitch;
				packetIn = packet;
			}
		}
		super.onPacketRecieved(event);
	}

}
