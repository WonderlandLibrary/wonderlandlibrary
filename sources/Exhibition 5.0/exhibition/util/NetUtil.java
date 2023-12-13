// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util;

import net.minecraft.network.Packet;

public class NetUtil implements MinecraftUtil
{
    public static void sendPacketNoEvents(final Packet packet) {
        NetUtil.mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
    }
    
    public static void sendPacket(final Packet packet) {
        NetUtil.mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}
