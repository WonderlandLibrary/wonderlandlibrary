package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.Event;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\t\u000020B00¢R0X¢\n\u0000\b\"\b\b\tR0X¢\n\u0000\b\n\"\b\t¨\f"}, d2={"Lnet/ccbluex/liquidbounce/event/SlowDownEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "strafe", "", "forward", "(FF)V", "getForward", "()F", "setForward", "(F)V", "getStrafe", "setStrafe", "Pride"})
public final class SlowDownEvent
extends Event {
    private float strafe;
    private float forward;

    public final float getStrafe() {
        return this.strafe;
    }

    public final void setStrafe(float f) {
        this.strafe = f;
    }

    public final float getForward() {
        return this.forward;
    }

    public final void setForward(float f) {
        this.forward = f;
    }

    public SlowDownEvent(float strafe, float forward) {
        this.strafe = strafe;
        this.forward = forward;
    }
}
