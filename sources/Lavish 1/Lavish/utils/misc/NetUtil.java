// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class NetUtil
{
    public static void sendPacketNoEvents(final Packet packet) {
        final Minecraft mc = Minecraft.getMinecraft();
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }
}
