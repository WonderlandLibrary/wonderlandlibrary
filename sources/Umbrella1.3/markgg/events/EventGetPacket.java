/*
 * Decompiled with CFR 0.150.
 */
package markgg.events;

import markgg.events.Event;
import net.minecraft.network.Packet;

public class EventGetPacket
extends Event<EventGetPacket> {
    Packet packet;

    public EventGetPacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }
}

