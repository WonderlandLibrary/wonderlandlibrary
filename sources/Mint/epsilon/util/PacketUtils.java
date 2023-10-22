package epsilon.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;

public class PacketUtils {

	private static final NetHandlerPlayClient mc = Minecraft.getMinecraft().getNetHandler();
	
	public static void sendPacket(Packet p) {
		mc.addToSendQueue(p);
	}
	
	public static void sendPacketNoEvent(Packet p) {
		mc.sendPacketNoEvent(p);
	}
	
	
}
