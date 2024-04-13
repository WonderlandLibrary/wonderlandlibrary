package net.smoothboot.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;

public class PacketUtil {

    public static void sendPacket(Packet<?> packet) {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.getNetworkHandler().sendPacket(packet);
    }
}
