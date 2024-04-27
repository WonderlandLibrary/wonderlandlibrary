/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.EventState;

public final class MotionEvent
extends Event {
    private boolean onGround;
    private final EventState eventState;

    public final EventState getEventState() {
        return this.eventState;
    }

    public final void setOnGround(boolean bl) {
        this.onGround = bl;
    }

    public MotionEvent(EventState eventState, boolean bl) {
        this.eventState = eventState;
        this.onGround = bl;
    }

    public final boolean isPre() {
        return this.eventState == EventState.PRE;
    }

    public final boolean getOnGround() {
        return this.onGround;
    }
}

