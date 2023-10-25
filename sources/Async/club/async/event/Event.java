package club.async.event;

import club.async.Async;

import java.util.ConcurrentModificationException;

public class Event {

    /*
    the cancelled state
    */
    private boolean cancelled;

    /* Sets current cancelled state
     * @param cancelled The cancelled value
     */
    public final void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * @return the cancelled state
     */
    public final boolean isCancelled() {
        return this.cancelled;
    }
    /* Registers all events in Object
     * @param o Object to register
     */
    public static void register(final Object o) { Async.INSTANCE.getPubSub().subscribe(o); }

    /* Unregisters all events in Object
     * @param o Object to unregister
     */
    public static void unregister(final Object o) { Async.INSTANCE.getPubSub().unsubscribe(o); }

    /**
     * @param event Event to fire
     */
    public static void fire(final Event event) {
        try {
            Async.INSTANCE.getPubSub().post(event).dispatch();
        } catch (ConcurrentModificationException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void fire() {
        try {
            Async.INSTANCE.getPubSub().post(this).dispatch();
        } catch (ConcurrentModificationException exception) {
            System.out.println("Event fire error: " + exception.getMessage());
        }
    }

}
