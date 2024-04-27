package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.CancellableEvent;
import me.aquavit.liquidsense.event.EventType;
import net.minecraft.network.Packet;

public class PacketEvent extends CancellableEvent {
    private Packet<?> packet;
    private EventType eventType;

    public Packet<?> getPacket() {
        return this.packet;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public PacketEvent(Packet<?> packet, EventType eventType) {
        this.packet = packet;
        this.eventType = eventType;
    }
}
