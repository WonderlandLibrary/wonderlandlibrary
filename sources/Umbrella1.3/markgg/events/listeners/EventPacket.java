/*
 * Decompiled with CFR 0.150.
 */
package markgg.events.listeners;

import markgg.events.Event;
import net.minecraft.network.Packet;

public class EventPacket<T>
extends Event<EventPacket> {
    public static Packet packet;
    public boolean cancelled;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public EventPacket(Packet packet) {
        EventPacket.packet = packet;
    }

    public static Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        EventPacket.packet = packet;
    }
}

