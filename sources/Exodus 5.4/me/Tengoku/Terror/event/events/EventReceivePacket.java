/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event.events;

import me.Tengoku.Terror.event.Event;
import net.minecraft.network.Packet;

public class EventReceivePacket
extends Event<EventReceivePacket> {
    private Packet packet;

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public EventReceivePacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }
}

