/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.packet;

import org.celestial.client.event.events.callables.EventCancellable;

public class EventReceiveMessage
extends EventCancellable {
    public String message;
    public boolean cancelled;

    public EventReceiveMessage(String chat) {
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

