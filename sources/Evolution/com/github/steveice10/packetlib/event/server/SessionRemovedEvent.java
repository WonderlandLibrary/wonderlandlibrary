/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.event.server;

import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.server.ServerEvent;
import com.github.steveice10.packetlib.event.server.ServerListener;

public class SessionRemovedEvent
implements ServerEvent {
    private Server server;
    private Session session;

    public SessionRemovedEvent(Server server, Session session) {
        this.server = server;
        this.session = session;
    }

    public Server getServer() {
        return this.server;
    }

    public Session getSession() {
        return this.session;
    }

    @Override
    public void call(ServerListener listener) {
        listener.sessionRemoved(this);
    }
}

