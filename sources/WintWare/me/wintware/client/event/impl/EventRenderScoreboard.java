/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.Event;
import me.wintware.client.event.types.EventType;

public class EventRenderScoreboard
implements Event {
    EventType state;

    public EventRenderScoreboard(EventType state) {
        this.state = state;
    }

    public EventType getState() {
        return this.state;
    }

    public boolean isPre() {
        return this.state == EventType.PRE;
    }

    public void setState(EventType state) {
        this.state = state;
    }
}

