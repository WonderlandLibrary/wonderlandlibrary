/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events;

import org.celestial.client.event.events.Event;

public abstract class EventStoppable
implements Event {
    private boolean stopped;

    public void stop() {
        this.stopped = true;
    }

    public boolean isStopped() {
        return this.stopped;
    }
}

