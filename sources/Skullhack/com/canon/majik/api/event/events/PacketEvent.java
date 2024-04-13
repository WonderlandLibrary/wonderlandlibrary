package com.canon.majik.api.event.events;

import com.canon.majik.api.event.eventBus.Event;
import net.minecraft.network.Packet;

public class PacketEvent extends Event {

    private final Packet<?> packet;

    private PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public static class Send extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }
    }
}
