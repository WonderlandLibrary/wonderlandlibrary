/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event.events;

import me.Tengoku.Terror.event.Event;

public class EventChat
extends Event<EventChat> {
    public String message;

    public void setMessage(String string) {
        this.message = string;
    }

    public EventChat(String string) {
        this.message = string;
    }

    public String getMessage() {
        return this.message;
    }
}

