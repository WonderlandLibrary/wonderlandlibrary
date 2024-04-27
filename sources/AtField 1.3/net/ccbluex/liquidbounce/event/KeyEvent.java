/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.Event;

public final class KeyEvent
extends Event {
    private final int key;

    public KeyEvent(int n) {
        this.key = n;
    }

    public final int getKey() {
        return this.key;
    }
}

