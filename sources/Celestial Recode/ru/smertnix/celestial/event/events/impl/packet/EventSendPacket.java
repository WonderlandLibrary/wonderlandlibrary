package ru.smertnix.celestial.event.events.impl.packet;

import net.minecraft.network.Packet;
import ru.smertnix.celestial.event.events.callables.EventCancellable;

public class EventSendPacket extends EventCancellable {

    private final Packet<?> packet;

    public EventSendPacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }
}
