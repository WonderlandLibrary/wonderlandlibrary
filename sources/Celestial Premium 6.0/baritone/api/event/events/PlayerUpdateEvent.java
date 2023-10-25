/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.event.events;

import baritone.api.event.events.type.EventState;

public final class PlayerUpdateEvent {
    private final EventState state;

    public PlayerUpdateEvent(EventState state) {
        this.state = state;
    }

    public final EventState getState() {
        return this.state;
    }
}

