/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.event.server;

import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.event.server.ServerEvent;
import com.github.steveice10.packetlib.event.server.ServerListener;

public class ServerClosedEvent
implements ServerEvent {
    private Server server;

    public ServerClosedEvent(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return this.server;
    }

    @Override
    public void call(ServerListener listener) {
        listener.serverClosed(this);
    }
}

