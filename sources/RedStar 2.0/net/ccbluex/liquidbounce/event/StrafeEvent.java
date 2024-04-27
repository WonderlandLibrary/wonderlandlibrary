package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.CancellableEvent;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\b\u000020B000¢R0¢\b\n\u0000\b\bR0¢\b\n\u0000\b\t\bR0¢\b\n\u0000\b\n\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/StrafeEvent;", "Lnet/ccbluex/liquidbounce/event/CancellableEvent;", "strafe", "", "forward", "friction", "(FFF)V", "getForward", "()F", "getFriction", "getStrafe", "Pride"})
public final class StrafeEvent
extends CancellableEvent {
    private final float strafe;
    private final float forward;
    private final float friction;

    public final float getStrafe() {
        return this.strafe;
    }

    public final float getForward() {
        return this.forward;
    }

    public final float getFriction() {
        return this.friction;
    }

    public StrafeEvent(float strafe, float forward, float friction) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
    }
}
