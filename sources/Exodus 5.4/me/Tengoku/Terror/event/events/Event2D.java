/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event.events;

import me.Tengoku.Terror.event.Event;

public class Event2D
extends Event<Event2D> {
    private float width;
    private float height;

    public Event2D(float f, float f2) {
        this.width = f;
        this.height = f2;
    }

    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }
}

