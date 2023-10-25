/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.callables;

import org.celestial.client.event.events.Cancellable;
import org.celestial.client.event.events.Event;

public abstract class EventCancellable
implements Event,
Cancellable {
    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancelled = state;
    }
}

