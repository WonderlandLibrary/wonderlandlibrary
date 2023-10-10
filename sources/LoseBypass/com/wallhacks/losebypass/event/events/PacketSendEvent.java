/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.event.events;

import com.wallhacks.losebypass.event.eventbus.Event;
import net.minecraft.network.Packet;

public class PacketSendEvent
extends Event {
    final Packet<?> p;

    public PacketSendEvent(Packet<?> p) {
        this.p = p;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T)this.p;
    }
}

