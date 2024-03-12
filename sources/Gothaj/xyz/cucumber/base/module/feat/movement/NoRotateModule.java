package xyz.cucumber.base.module.feat.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventMove;
import xyz.cucumber.base.events.ext.EventReceivePacket;
import xyz.cucumber.base.events.ext.EventSendPacket;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;

@ModuleInfo(category = Category.MOVEMENT, description = "Automatically stops server from rotating you", name = "No Rotate", key = Keyboard.KEY_NONE)
public class NoRotateModule extends Mod {
	
	@EventListener
	public void onReceivePacket(EventReceivePacket e) {
		if(e.getPacket() instanceof S08PacketPlayerPosLook) {
			S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)e.getPacket();
			packet.yaw = mc.thePlayer.rotationYaw;
			packet.pitch = mc.thePlayer.rotationPitch;
		}
	}
}
