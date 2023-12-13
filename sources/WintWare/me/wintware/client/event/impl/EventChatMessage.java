/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.callables.EventCancellable;

public class EventChatMessage
extends EventCancellable {
    public String message;
    public boolean cancelled;

    public EventChatMessage(String chat) {
        this.message = chat;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}

