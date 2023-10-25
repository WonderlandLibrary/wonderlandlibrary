package wtf.evolution.event.events.callables;

import wtf.evolution.event.events.Cancellable;
import wtf.evolution.event.events.Event;

public abstract class EventCancellable implements Event, Cancellable {

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

}
