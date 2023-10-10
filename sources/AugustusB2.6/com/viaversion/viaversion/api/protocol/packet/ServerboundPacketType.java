// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.protocol.packet;

public interface ServerboundPacketType extends PacketType
{
    default Direction direction() {
        return Direction.SERVERBOUND;
    }
}
