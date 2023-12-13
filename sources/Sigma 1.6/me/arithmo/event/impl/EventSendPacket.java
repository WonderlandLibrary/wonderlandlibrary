/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.event.impl;

import me.arithmo.event.Event;
import net.minecraft.network.Packet;

public class EventSendPacket
extends Event {
    private Packet packet;
    private boolean pre;

    public void fire(boolean state, Packet packet) {
        this.pre = state;
        this.packet = packet;
        super.fire();
    }

    public Packet getPacket() {
        return this.packet;
    }

    public boolean isPre() {
        return this.pre;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public void setState(boolean state) {
        this.pre = state;
    }
}

