package cc.novoline.events.events.callables;

import cc.novoline.events.events.Cancellable;
import cc.novoline.events.events.Event;

/**
 * Abstract example implementation of the Cancellable interface.
 *
 * @author DarkMagician6
 * @since August 27, 2013
 */
public abstract class CancellableEvent implements Event, Cancellable {

    private boolean cancelled;

    protected CancellableEvent() {
    }

    /**
     * @see Cancellable#isCancelled()
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * @see Cancellable#setCancelled(boolean)
     */
    @Override
    public void setCancelled(boolean state) {
        this.cancelled = state;
    }

}
