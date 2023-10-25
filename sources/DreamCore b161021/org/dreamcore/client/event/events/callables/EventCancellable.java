package org.dreamcore.client.event.events.callables;

import org.dreamcore.client.event.events.Cancellable;
import org.dreamcore.client.event.events.Event;

public abstract class EventCancellable implements Event, Cancellable {

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean state) {
        cancelled = state;
    }

}
