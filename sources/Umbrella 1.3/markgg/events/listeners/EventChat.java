/*
 * Decompiled with CFR 0.150.
 */
package markgg.events.listeners;

import markgg.events.Event;

public class EventChat
extends Event<EventChat> {
    public String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EventChat(String message) {
        this.message = message;
    }
}

