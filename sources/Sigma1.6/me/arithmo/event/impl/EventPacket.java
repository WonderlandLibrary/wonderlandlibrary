/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.event.impl;

import me.arithmo.event.Event;
import net.minecraft.network.Packet;

public class EventPacket
extends Event {
    private Packet packet;
    private boolean outgoing;

    public void fire(Packet packet, boolean outgoing) {
        this.packet = packet;
        this.outgoing = outgoing;
        super.fire();
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public boolean isOutgoing() {
        return this.outgoing;
    }

    public boolean isIncoming() {
        return !this.outgoing;
    }
}

