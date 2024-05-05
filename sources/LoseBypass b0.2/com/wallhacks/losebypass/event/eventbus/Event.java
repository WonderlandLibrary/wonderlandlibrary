/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.event.eventbus;

public class Event {
    private boolean isCancelled = false;

    public final boolean isCancelled() {
        return this.isCancelled;
    }

    public final void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public final void cancel() {
        this.isCancelled = true;
    }
}

