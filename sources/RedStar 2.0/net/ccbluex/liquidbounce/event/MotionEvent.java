package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.Event;
import net.ccbluex.liquidbounce.event.EventState;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\u0000\n\n\b\t\u000020B00¢J\r0R0¢\b\n\u0000\b\bR0X¢\n\u0000\b\t\n\"\b\f¨"}, d2={"Lnet/ccbluex/liquidbounce/event/MotionEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "eventState", "Lnet/ccbluex/liquidbounce/event/EventState;", "onGround", "", "(Lnet/ccbluex/liquidbounce/event/EventState;Z)V", "getEventState", "()Lnet/ccbluex/liquidbounce/event/EventState;", "getOnGround", "()Z", "setOnGround", "(Z)V", "isPre", "Pride"})
public final class MotionEvent
extends Event {
    @NotNull
    private final EventState eventState;
    private boolean onGround;

    public final boolean isPre() {
        return this.eventState == EventState.PRE;
    }

    @NotNull
    public final EventState getEventState() {
        return this.eventState;
    }

    public final boolean getOnGround() {
        return this.onGround;
    }

    public final void setOnGround(boolean bl) {
        this.onGround = bl;
    }

    public MotionEvent(@NotNull EventState eventState, boolean onGround) {
        Intrinsics.checkParameterIsNotNull((Object)eventState, "eventState");
        this.eventState = eventState;
        this.onGround = onGround;
    }
}
