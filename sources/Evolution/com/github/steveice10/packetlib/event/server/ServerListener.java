/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.event.server;

import com.github.steveice10.packetlib.event.server.ServerBoundEvent;
import com.github.steveice10.packetlib.event.server.ServerClosedEvent;
import com.github.steveice10.packetlib.event.server.ServerClosingEvent;
import com.github.steveice10.packetlib.event.server.SessionAddedEvent;
import com.github.steveice10.packetlib.event.server.SessionRemovedEvent;

public interface ServerListener {
    public void serverBound(ServerBoundEvent var1);

    public void serverClosing(ServerClosingEvent var1);

    public void serverClosed(ServerClosedEvent var1);

    public void sessionAdded(SessionAddedEvent var1);

    public void sessionRemoved(SessionRemovedEvent var1);
}

