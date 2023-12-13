/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event.impl.hitentity;

import tk.rektsky.event.Event;

public class PreHitEntityEvent
extends Event {
    private boolean cancelled;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public PreHitEntityEvent setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        return this;
    }
}

