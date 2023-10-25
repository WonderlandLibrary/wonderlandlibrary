/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.render;

import org.celestial.client.event.events.Event;
import org.celestial.client.event.types.EventType;

public class EventRenderScoreboard
implements Event {
    private EventType state;

    public EventRenderScoreboard(EventType state) {
        this.state = state;
    }

    public EventType getState() {
        return this.state;
    }

    public void setState(EventType state) {
        this.state = state;
    }

    public boolean isPre() {
        return this.state == EventType.PRE;
    }
}

