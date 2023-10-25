package org.dreamcore.client.event.events.impl.packet;

import org.dreamcore.client.event.events.callables.EventCancellable;

public class EventReceiveMessage extends EventCancellable {

    public String message;
    public boolean cancelled;

    public EventReceiveMessage(String chat) {
        message = chat;
    }

    public String getMessage() {
        return message;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}