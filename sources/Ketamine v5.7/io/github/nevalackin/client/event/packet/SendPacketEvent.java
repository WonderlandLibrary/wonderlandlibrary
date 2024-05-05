package io.github.nevalackin.client.event.packet;

import net.minecraft.network.Packet;

public final class SendPacketEvent extends PacketEvent {
    public SendPacketEvent(Packet<?> packet) {
        super(packet);
    }
}
