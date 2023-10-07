package dev.monsoon.event.listeners;

import dev.monsoon.event.Event;
import net.minecraft.network.Packet;

public class EventPacket<T> extends Event<EventPacket> {
    
	public static Packet packet;
    public boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public EventPacket(Packet packet) {
        this.packet = packet;
    }

    public static Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}