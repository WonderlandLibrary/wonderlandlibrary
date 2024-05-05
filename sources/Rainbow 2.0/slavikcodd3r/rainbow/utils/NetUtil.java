package slavikcodd3r.rainbow.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class NetUtil
{
	
    public NetUtil() {
        super();
    }
    
    public static void sendPacketNoEvents(final Packet packet) {
    	Minecraft mc = Minecraft.getMinecraft();
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }
    
    public static void sendPacket(final Packet packet) {
    	Minecraft mc = Minecraft.getMinecraft();
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}
