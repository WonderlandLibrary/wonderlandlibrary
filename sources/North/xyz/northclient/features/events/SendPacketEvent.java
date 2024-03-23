package xyz.northclient.features.events;

import xyz.northclient.features.Event;
import net.minecraft.network.Packet;
public class SendPacketEvent extends Event {
    public Packet packet;

    public SendPacketEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }
}
