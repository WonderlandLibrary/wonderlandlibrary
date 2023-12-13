/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event;

import me.wintware.client.event.Event;

public abstract class EventStoppable
implements Event {
    private boolean stopped;

    protected EventStoppable() {
    }

    public void stop() {
        this.stopped = true;
    }

    public boolean isStopped() {
        return this.stopped;
    }
}

