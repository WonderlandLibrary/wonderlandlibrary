/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event.impl;

import tk.rektsky.event.Event;

public class KeyPressedEvent
extends Event {
    private int key;

    public KeyPressedEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}

