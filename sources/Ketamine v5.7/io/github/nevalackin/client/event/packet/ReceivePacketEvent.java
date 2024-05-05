package io.github.nevalackin.client.event.packet;

import net.minecraft.network.Packet;

public final class ReceivePacketEvent extends PacketEvent {
    public ReceivePacketEvent(Packet<?> packet) {
        super(packet);
    }
}
