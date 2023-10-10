/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.event.events;

import com.wallhacks.losebypass.event.eventbus.Event;

public class InputEvent
extends Event {
    private final int key;

    public InputEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}

