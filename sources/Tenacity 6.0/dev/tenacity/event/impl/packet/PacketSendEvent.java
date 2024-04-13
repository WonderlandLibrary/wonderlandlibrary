package dev.tenacity.event.impl.packet;

import dev.tenacity.event.Event;
import net.minecraft.network.Packet;

public final class PacketSendEvent extends Event {

    private Packet<?> packet;

    public PacketSendEvent(final Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(final Packet<?> packet) {
        this.packet = packet;
    }

}
