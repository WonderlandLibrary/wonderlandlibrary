package net.minecraft.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.handshake.client.C00Handshake;

static final class EnumConnectionState.1
extends EnumConnectionState {
    EnumConnectionState.1(int protocolId) {
        super(string, n, protocolId, null);
        this.registerPacket(EnumPacketDirection.SERVERBOUND, C00Handshake.class);
    }
}
