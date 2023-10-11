package markgg.event.impl;

import markgg.event.Event;
import net.minecraft.network.Packet;

public class PacketEvent extends Event {

    public static Packet packet;

    public PacketEvent(Packet packet) {
        this.packet = packet;
    }

    public static Packet getPacket() {
        return packet;
    }
}
