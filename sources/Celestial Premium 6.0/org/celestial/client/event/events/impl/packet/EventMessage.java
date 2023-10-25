/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.packet;

import org.celestial.client.event.events.Event;
import org.celestial.client.event.events.callables.EventCancellable;

public class EventMessage
extends EventCancellable
implements Event {
    public String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

