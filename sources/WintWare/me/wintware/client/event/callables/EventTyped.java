/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.callables;

import me.wintware.client.event.Event;
import me.wintware.client.event.Typed;

public abstract class EventTyped
implements Event,
Typed {
    private final byte type;

    protected EventTyped(byte eventType) {
        this.type = eventType;
    }

    @Override
    public byte getType() {
        return this.type;
    }
}

