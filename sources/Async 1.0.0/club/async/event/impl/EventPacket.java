package club.async.event.impl;

import club.async.event.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event {

    private final Packet<?> packet;

    public EventPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
