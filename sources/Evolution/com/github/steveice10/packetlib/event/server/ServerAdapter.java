/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.event.server;

import com.github.steveice10.packetlib.event.server.ServerBoundEvent;
import com.github.steveice10.packetlib.event.server.ServerClosedEvent;
import com.github.steveice10.packetlib.event.server.ServerClosingEvent;
import com.github.steveice10.packetlib.event.server.ServerListener;
import com.github.steveice10.packetlib.event.server.SessionAddedEvent;
import com.github.steveice10.packetlib.event.server.SessionRemovedEvent;

public class ServerAdapter
implements ServerListener {
    @Override
    public void serverBound(ServerBoundEvent event) {
    }

    @Override
    public void serverClosing(ServerClosingEvent event) {
    }

    @Override
    public void serverClosed(ServerClosedEvent event) {
    }

    @Override
    public void sessionAdded(SessionAddedEvent event) {
    }

    @Override
    public void sessionRemoved(SessionRemovedEvent event) {
    }
}

