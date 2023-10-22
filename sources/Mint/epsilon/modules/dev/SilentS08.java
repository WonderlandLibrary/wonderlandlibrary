package epsilon.modules.dev;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.packet.EventReceivePacket;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class SilentS08 extends Module{

	public BooleanSetting debug = new BooleanSetting ("Debug", false);
	
	public SilentS08() {
		super("SilentS08", Keyboard.KEY_NONE, Category.DEV, "Makes lagbacks silent");
		this.addSettings(debug);
	}
	
	public void onEvent(Event e) {
	
		if(e instanceof EventReceivePacket && mc.getNetHandler().doneLoadingTerrain) {

			Packet p = e.getPacket();
			
			if(p instanceof S08PacketPlayerPosLook) {

    			final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) p;

    			if(debug.isEnabled())
    				Epsilon.addChatMessage("Received to S08");
    			
    			e.setCancelled();
    			
    			mc.getNetHandler().sendPacketNoEvent(new C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, mc.thePlayer.onGround));
    			
    			if(debug.isEnabled())
    				Epsilon.addChatMessage("Responded to S08");
				
			}
			
		}
		
	}

}
