/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.event.session;

import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectingEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSendingEvent;
import com.github.steveice10.packetlib.event.session.PacketSentEvent;

public interface SessionListener {
    public void packetReceived(PacketReceivedEvent var1);

    public void packetSending(PacketSendingEvent var1);

    public void packetSent(PacketSentEvent var1);

    public void connected(ConnectedEvent var1);

    public void disconnecting(DisconnectingEvent var1);

    public void disconnected(DisconnectedEvent var1);
}

