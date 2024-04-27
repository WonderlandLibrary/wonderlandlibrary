/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.CancellableEvent;

public final class PacketEvent
extends CancellableEvent {
    private final IPacket packet;

    public final IPacket getPacket() {
        return this.packet;
    }

    public PacketEvent(IPacket packet) {
        this.packet = packet;
    }
}

