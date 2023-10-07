package dev.rise.dorttesting.network.packet;

import dev.rise.dorttesting.network.packet.mapping.PacketMapping;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

public interface MappedPacket extends Packet<INetHandler> {

    PacketMapping getMapping();

}
