package me.aquavit.liquidsense.event;

public class CancellableEvent extends Event {
    private boolean isCancelled;

    public final boolean isCancelled() {
        return this.isCancelled;
    }

    public final void cancelEvent() {
        this.isCancelled = true;
    }
}
