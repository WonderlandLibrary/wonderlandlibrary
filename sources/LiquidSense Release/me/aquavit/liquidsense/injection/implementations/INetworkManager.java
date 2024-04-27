package me.aquavit.liquidsense.injection.implementations;

import net.minecraft.network.Packet;

public interface INetworkManager {
    void sendPacketNoEvent(Packet<?> p);
}
