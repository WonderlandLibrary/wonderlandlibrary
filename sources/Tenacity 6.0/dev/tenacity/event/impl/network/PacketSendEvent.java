// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.network;

import net.minecraft.network.Packet;

public class PacketSendEvent extends PacketEvent
{
    public PacketSendEvent(final Packet<?> packet) {
        super(packet);
    }
}
