/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event.events;

import me.Tengoku.Terror.event.Event;

public class EventSafeWalk
extends Event<EventSafeWalk> {
    boolean walk = false;

    public void setSafeWalk(boolean bl) {
        this.walk = bl;
    }

    public boolean getSafeWalk() {
        return this.walk;
    }

    public EventSafeWalk() {
        this.setSafeWalk(this.walk);
    }
}

