package dev.zprestige.prestige.client.event;

import dev.zprestige.prestige.client.Prestige;

public class Event {
    public Phase phase;
    public boolean cancelled;

    public Event(Phase phase) {
        this.phase = phase;
    }

    public Event() {
        this(Phase.NONE);
    }

    public boolean invoke() {
        return Prestige.Companion.getEventBus().invoke(this);
    }

    public Phase getPhase() {
        return this.phase;
    }

    public void setCancelled() {
        this.cancelled = true;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
}
