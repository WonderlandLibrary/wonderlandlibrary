package Velo.impl.Modules.player;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventReceivePacket;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Module {
	
	public NoRotate() {
		super("NoRotate", "NoRotate", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	public void onEventReceivePacket(EventReceivePacket event) {
		if(event.getPacket() instanceof S08PacketPlayerPosLook) {
			S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook) event.getPacket();
			s08.yaw = mc.thePlayer.rotationYaw;
			s08.pitch = mc.thePlayer.rotationPitch;
		}
	}
}
