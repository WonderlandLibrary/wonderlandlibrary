/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event.events;

import me.Tengoku.Terror.event.Event;
import net.minecraft.network.Packet;

public class EventSendPacket
extends Event<EventSendPacket> {
    private Packet packet = null;

    public Packet getPacket() {
        return this.packet;
    }

    public EventSendPacket(Packet packet) {
        this.setPacket(packet);
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

