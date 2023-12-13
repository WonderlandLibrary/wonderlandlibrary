package dev.rise.dorttesting.network.packet.packets.client.player;

import dev.rise.dorttesting.network.packet.MappedPacket;
import dev.rise.dorttesting.network.packet.mapping.PacketMapping;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class ClientPlayerPosition implements MappedPacket {
    @Override
    public PacketMapping getMapping() {
        return new PacketMapping(0x04, 0x04);
    }

    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {

    }

    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {

    }

    @Override
    public void processPacket(final INetHandler handler) {

    }
}
