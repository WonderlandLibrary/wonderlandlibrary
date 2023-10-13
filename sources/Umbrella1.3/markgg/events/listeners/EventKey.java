/*
 * Decompiled with CFR 0.150.
 */
package markgg.events.listeners;

import markgg.events.Event;

public class EventKey
extends Event<EventKey> {
    public int code;

    public EventKey(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

