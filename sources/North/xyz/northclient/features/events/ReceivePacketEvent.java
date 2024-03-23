package xyz.northclient.features.events;

import xyz.northclient.features.Event;
import net.minecraft.network.Packet;
public class ReceivePacketEvent extends Event {
    public Packet packet;

    public ReceivePacketEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }
}
