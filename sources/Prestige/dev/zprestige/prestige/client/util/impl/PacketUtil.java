package dev.zprestige.prestige.client.util.impl;

import dev.zprestige.prestige.client.util.MC;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;

public class PacketUtil implements MC {

    public static PacketUtil INSTANCE = new PacketUtil();

    public void sendPacket(Packet packet) {
        this.getMc().getNetworkHandler().sendPacket(packet);
    }

    @Override
    public MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }

}
