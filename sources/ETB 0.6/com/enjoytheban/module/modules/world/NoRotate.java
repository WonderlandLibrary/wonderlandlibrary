package com.enjoytheban.module.modules.world;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.world.EventPacketSend;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;

/**
 * Stops the server from updating player look position
 * 
 * @author purity
 */

public class NoRotate extends Module {

	public NoRotate() {
		super("NoRotate", new String[] { "rotate" }, ModuleType.World);
		setColor(new Color(17,250,154).getRGB());
	}

	@EventHandler
	private void onPacket(EventPacketSend e) {
		if (e.getPacket() instanceof S08PacketPlayerPosLook) {
			S08PacketPlayerPosLook look = (S08PacketPlayerPosLook) e.getPacket();
			look.field_148936_d = mc.thePlayer.rotationYaw;
			look.field_148937_e = mc.thePlayer.rotationPitch;
		}
	}
}