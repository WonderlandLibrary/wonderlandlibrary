/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event.events;

import me.Tengoku.Terror.event.Event;

public class EventKey
extends Event<EventKey> {
    private int key;

    public int getKey() {
        return this.key;
    }

    public EventKey(int n) {
        this.key = n;
    }
}

