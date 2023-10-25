/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event.events;

import me.Tengoku.Terror.event.Event;
import net.minecraft.network.Packet;

public class EventPacket
extends Event<EventPacket> {
    private boolean incoming;
    public Packet packet;

    public EventPacket(Packet packet, boolean bl) {
        this.packet = packet;
        this.incoming = bl;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public boolean isRecieving() {
        return this.incoming;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public boolean isSending() {
        return !this.incoming;
    }
}

