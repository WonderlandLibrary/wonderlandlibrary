package epsilon.modules.dev;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.packet.EventReceivePacket;
import epsilon.modules.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class PacketDebugger extends Module {

	public PacketDebugger() {
		super("PacketDebugger", Keyboard.KEY_NONE, Category.DEV, "Debugs CT and KA");
	}

	public void onEvent(Event e) {
		if(e instanceof EventReceivePacket && mc.thePlayer !=null) {
			Packet p = e.getPacket();
			
			
			if(p instanceof S32PacketConfirmTransaction) {
				S32PacketConfirmTransaction trannie = ((S32PacketConfirmTransaction) p);
				
				
				Epsilon.addChatMessage("Transaction "+trannie.field_148892_b);
			}
			
			if(p instanceof S00PacketKeepAlive) {
				S00PacketKeepAlive keepAlive = ((S00PacketKeepAlive) p);
				
				
				Epsilon.addChatMessage("KeepAlive " + keepAlive.func_149134_c());
				
				
			}
			
			
			
		}
	}
	
}
