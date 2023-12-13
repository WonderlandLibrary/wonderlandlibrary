/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.event;

import me.arithmo.event.EventSystem;

public abstract class Event {
    protected boolean cancelled;

    public void fire() {
        this.cancelled = false;
        EventSystem.fire(this);
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
}

