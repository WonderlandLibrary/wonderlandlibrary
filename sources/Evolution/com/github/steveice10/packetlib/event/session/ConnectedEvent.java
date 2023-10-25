/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.event.session;

import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.SessionEvent;
import com.github.steveice10.packetlib.event.session.SessionListener;

public class ConnectedEvent
implements SessionEvent {
    private Session session;

    public ConnectedEvent(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return this.session;
    }

    @Override
    public void call(SessionListener listener) {
        listener.connected(this);
    }
}

