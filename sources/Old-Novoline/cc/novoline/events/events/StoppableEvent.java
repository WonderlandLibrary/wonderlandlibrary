package cc.novoline.events.events;

/**
 * The most basic form of an stoppable Event.
 * Stoppable events are called separate from other events and the calling of methods is stopped
 * as soon as the EventStoppable is stopped.
 *
 * @author DarkMagician6
 * @since 26-9-13
 */
public abstract class StoppableEvent implements Event {

    private boolean stopped;

    /**
     * No need for the constructor to be public.
     */
    protected StoppableEvent() {
    }

    /**
     * Sets the stopped state to true.
     */
    public void stop() {
        this.stopped = true;
    }

    /**
     * Checks the stopped boolean.
     *
     * @return True if the EventStoppable is stopped.
     */
    public boolean isStopped() {
        return this.stopped;
    }

}
