/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.event.events;

import com.wallhacks.losebypass.event.eventbus.Event;
import net.minecraft.network.Packet;

public class PacketReceiveEvent
extends Event {
    final Packet<?> p;

    public PacketReceiveEvent(Packet<?> p) {
        this.p = p;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T)this.p;
    }
}

