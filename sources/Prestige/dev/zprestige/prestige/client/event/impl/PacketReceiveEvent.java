package dev.zprestige.prestige.client.event.impl;

import dev.zprestige.prestige.client.event.Event;
import net.minecraft.network.packet.Packet;

public class PacketReceiveEvent extends Event {
    public Packet packet;

    public PacketReceiveEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }
}
