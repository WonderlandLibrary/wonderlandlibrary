package com.masterof13fps.manager.eventmanager;

public class Event {

    boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean canceled) {
        this.cancelled = canceled;
    }
}
