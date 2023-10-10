package tech.atani.client.listener.event;

import tech.atani.client.listener.handling.EventHandling;

public class Event {
    private boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public <T extends Event> T publishItself() {
        EventHandling.getInstance().publishEvent(this);
        return (T) this;
    }

}
