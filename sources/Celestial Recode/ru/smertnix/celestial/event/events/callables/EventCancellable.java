package ru.smertnix.celestial.event.events.callables;

import ru.smertnix.celestial.event.events.Cancellable;
import ru.smertnix.celestial.event.events.Event;

public abstract class EventCancellable implements Event, Cancellable {

    protected boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean state) {
        cancelled = state;
    }

}
