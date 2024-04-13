package dev.tenacity.event.impl.packet;

import dev.tenacity.event.Event;
import net.minecraft.network.Packet;

public final class PacketReceiveEvent extends Event {

    private final Packet<?> packet;

    public PacketReceiveEvent(final Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

}
