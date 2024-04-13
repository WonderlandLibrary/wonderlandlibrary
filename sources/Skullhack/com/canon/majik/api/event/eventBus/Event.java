package com.canon.majik.api.event.eventBus;

public class Event {
    private boolean cancelled;
    private final Phase phase;

    public Event() {
        this.phase = Phase.NONE;
    }

    public Event(Phase phase) {
        this.phase = phase;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public Phase getPhase() {
        return phase;
    }

    public final void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
