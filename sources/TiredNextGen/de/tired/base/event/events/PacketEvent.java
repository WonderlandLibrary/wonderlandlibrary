package de.tired.base.event.events;

import de.tired.base.event.Event;
import net.minecraft.network.Packet;

public class PacketEvent extends Event {

    boolean incoming;
    Packet packet;

    public PacketEvent(Packet packet, boolean incoming) {
        this.incoming = incoming;
        this.packet = packet;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
