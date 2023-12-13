package me.aidanmees.trivia.client.modules.Misc;

import org.lwjgl.input.Keyboard;

import me.aidanmees.trivia.client.module.state.Category;
import me.aidanmees.trivia.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Module {

	public NoRotate() {
		super("NoRotate", Keyboard.KEY_NONE, Category.MOVEMENT, "Prevents the server from controlling your rotation.");
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
	public void onPacketRecieved(AbstractPacket packetIn) {
		if (packetIn instanceof S08PacketPlayerPosLook && mc.thePlayer.ticksExisted > 5) {
			S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) packetIn;
			packet.yaw = mc.thePlayer.rotationYawHead;
			packet.pitch = mc.thePlayer.rotationPitch;
			packetIn = packet;
		}
		super.onPacketRecieved(packetIn);
	}

}
