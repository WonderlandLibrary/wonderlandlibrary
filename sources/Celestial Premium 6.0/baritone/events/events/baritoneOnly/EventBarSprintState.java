/*
 * Decompiled with CFR 0.150.
 */
package baritone.events.events.baritoneOnly;

import org.celestial.client.event.events.Event;
import org.celestial.client.event.events.callables.EventCancellable;

public class EventBarSprintState
extends EventCancellable
implements Event {
    public boolean state;

    public EventBarSprintState(boolean stateIn) {
        this.state = stateIn;
    }
}

