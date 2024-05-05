package graphite.impl.event;

public class Event {
    private boolean cancelled;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}