package wtf.evolution.event.events.impl;

import net.minecraft.network.Packet;
import wtf.evolution.event.events.Event;
import wtf.evolution.event.events.callables.EventCancellable;

public class EventPacket extends EventCancellable implements Event {

    private Packet packet;

    public EventPacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }



}
