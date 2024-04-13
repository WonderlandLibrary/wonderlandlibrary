package dev.tenacity.event.impl.network;

import dev.tenacity.event.Event;
import net.minecraft.network.Packet;


public class PacketSendEvent extends Event {

    private Packet<?> packet;

    public PacketSendEvent(Packet<?> packet) {
        this.packet = packet;
    }


    public Packet<?> getPacket() {
        return packet;
    }


    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

}
