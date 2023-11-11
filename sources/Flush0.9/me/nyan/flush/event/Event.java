package me.nyan.flush.event;

import java.lang.reflect.InvocationTargetException;

public abstract class Event {
    private boolean cancelled;
    private State state;

    public Event() {
        state = State.PRE;
    }

    private static void call(final Event event) {
        final ArrayHelper<Data> dataList = EventManager.get(event.getClass());

        if (dataList != null) {
            for (final Data data : dataList) {
                try {
                    data.target.invoke(data.source, event);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public <T extends Event> T call() {
        cancelled = false;
        call(this);
        return (T) this;
    }

    public State getState() {
        return state;
    }

    public <T extends Event> T setState(State state) {
        this.state = state;
        return (T) this;
    }

    public boolean isPre() {
        return state.equals(State.PRE);
    }

    public boolean isPost() {
        return state.equals(State.POST);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        setCancelled(true);
    }

    public enum State {
        PRE,
        POST
    }
}
