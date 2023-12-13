/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.callables.EventCancellable;
import net.minecraft.network.Packet;

public class EventSendPacket
extends EventCancellable {
    private final Packet packet;
    private final boolean sending;

    public EventSendPacket(Packet packet, boolean sending) {
        this.packet = packet;
        this.sending = sending;
    }

    public boolean isSending() {
        return this.sending;
    }

    public boolean isRecieving() {
        return !this.sending;
    }

    public Packet getPacket() {
        return this.packet;
    }
}

