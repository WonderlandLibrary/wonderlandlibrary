/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.input;

import org.celestial.client.event.events.callables.EventCancellable;

public class EventMouse
extends EventCancellable {
    public int key;

    public EventMouse(int key) {
        this.key = key;
    }
}

