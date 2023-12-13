/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.Event;
import me.wintware.client.event.callables.EventCancellable;

public class SendMessageEvent
extends EventCancellable
implements Event {
    private final String message;

    public SendMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

