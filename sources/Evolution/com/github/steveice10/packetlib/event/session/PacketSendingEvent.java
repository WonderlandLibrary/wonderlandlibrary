/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.event.session;

import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.SessionEvent;
import com.github.steveice10.packetlib.event.session.SessionListener;
import com.github.steveice10.packetlib.packet.Packet;

public class PacketSendingEvent
implements SessionEvent {
    private Session session;
    private Packet packet;
    private boolean cancelled = false;

    public PacketSendingEvent(Session session, Packet packet) {
        this.session = session;
        this.packet = packet;
    }

    public Session getSession() {
        return this.session;
    }

    public <T extends Packet> T getPacket() {
        try {
            return (T)this.packet;
        }
        catch (ClassCastException e) {
            throw new IllegalStateException("Tried to get packet as the wrong type. Actual type: " + this.packet.getClass().getName());
        }
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public void call(SessionListener listener) {
        listener.packetSending(this);
    }
}

