/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.event.events;

import baritone.api.event.events.type.Cancellable;

public final class ChatEvent
extends Cancellable {
    private final String message;

    public ChatEvent(String message) {
        this.message = message;
    }

    public final String getMessage() {
        return this.message;
    }
}

