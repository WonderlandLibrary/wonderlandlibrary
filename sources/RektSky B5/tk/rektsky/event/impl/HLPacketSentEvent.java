/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event.impl;

import net.minecraft.network.Packet;
import tk.rektsky.event.Event;

public class HLPacketSentEvent
extends Event {
    private Packet<?> packet;
    private boolean canceled;

    public HLPacketSentEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}

