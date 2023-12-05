package rip.helium.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class NetUtil {
    static Minecraft mc;

    public static void sendPacketNoEvents(final Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    public static void sendPacket(final Packet packet) {
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}

