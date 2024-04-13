package dev.tenacity.util.network;

import dev.tenacity.util.Utils;
import net.minecraft.network.Packet;

public class PacketUtils implements Utils {

    public static void sendPacket(Packet<?> packet, boolean silent) {
        if (mc.thePlayer != null) {
        }
    }

    public static void sendPacketNoEvent(Packet packet) {
        sendPacket(packet, true);
    }

    public static void sendPacket(Packet packet) {
        sendPacket(packet, false);
    }

}
