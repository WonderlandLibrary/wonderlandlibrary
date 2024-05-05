package club.dortware.client.util.impl.networking;

import club.dortware.client.util.Util;
import net.minecraft.network.Packet;

public class PacketUtil implements Util {

    public static void sendPacket(Packet packet) {
        mc.getNetHandler().addToSendQueue(packet);
    }

    public static void sendPacketNoEvent(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

}
