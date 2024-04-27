/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.EventState;

public final class MotionEvent
extends Event {
    private final EventState eventState;

    public final EventState getEventState() {
        return this.eventState;
    }

    public MotionEvent(EventState eventState) {
        this.eventState = eventState;
    }
}

