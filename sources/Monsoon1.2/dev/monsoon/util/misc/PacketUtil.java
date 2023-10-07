package dev.monsoon.util.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtil {

    public static void sendPacket(Packet p) {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(p);
    }

    public static void sendPacketNoEvent(Packet p) {
        Minecraft.getMinecraft().thePlayer.sendQueue.sendPacketNoEvent(p);
    }

}
