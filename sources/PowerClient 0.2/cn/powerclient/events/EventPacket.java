/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.network.Packet;

public class EventPacket
implements Event {
    public Packet packet;
    private boolean cancelled;

    public EventPacket(Packet p2) {
        this.packet = p2;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setCancelled(boolean state) {
        this.cancelled = state;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setPacket(Packet p2) {
        this.packet = p2;
    }
}

