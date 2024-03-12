// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.network;

import net.minecraft.network.Packet;

public class PacketReceiveEvent extends PacketEvent
{
    public PacketReceiveEvent(final Packet<?> packet) {
        super(packet);
    }
}
