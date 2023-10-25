/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event.events;

import me.Tengoku.Terror.event.Event;

public class Event3D
extends Event<Event3D> {
    public float partialTicks;

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public Event3D(float f) {
        this.partialTicks = f;
    }
}

