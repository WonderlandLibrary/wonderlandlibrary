package io.github.raze.events.collection.network;

import io.github.raze.events.system.Event;
import net.minecraft.network.Packet;

public class EventPacketReceive extends Event {

    public Packet packet;

    public EventPacketReceive(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

}
