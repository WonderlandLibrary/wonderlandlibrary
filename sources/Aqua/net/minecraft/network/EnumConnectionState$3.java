package net.minecraft.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;

static final class EnumConnectionState.3
extends EnumConnectionState {
    EnumConnectionState.3(int protocolId) {
        super(string, n, protocolId, null);
        this.registerPacket(EnumPacketDirection.SERVERBOUND, C00PacketServerQuery.class);
        this.registerPacket(EnumPacketDirection.CLIENTBOUND, S00PacketServerInfo.class);
        this.registerPacket(EnumPacketDirection.SERVERBOUND, C01PacketPing.class);
        this.registerPacket(EnumPacketDirection.CLIENTBOUND, S01PacketPong.class);
    }
}
