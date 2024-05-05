package club.dortware.client.event.impl;

import club.dortware.client.event.impl.enums.PacketDirection;
import club.dortware.client.event.Event;
import net.minecraft.network.Packet;

public class PacketEvent extends Event {

    private final PacketDirection packetDirection;
    private Packet packet;

    public PacketEvent(PacketDirection packetDirection, Packet packet) {
        this.packetDirection = packetDirection;
        this.packet = packet;
    }

    public PacketDirection getPacketDirection() {
        return packetDirection;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
