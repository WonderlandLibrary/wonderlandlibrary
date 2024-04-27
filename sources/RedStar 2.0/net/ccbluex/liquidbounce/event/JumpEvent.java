package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.CancellableEvent;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\u000020B\r0¢R0X¢\n\u0000\b\"\b¨\b"}, d2={"Lnet/ccbluex/liquidbounce/event/JumpEvent;", "Lnet/ccbluex/liquidbounce/event/CancellableEvent;", "motion", "", "(F)V", "getMotion", "()F", "setMotion", "Pride"})
public final class JumpEvent
extends CancellableEvent {
    private float motion;

    public final float getMotion() {
        return this.motion;
    }

    public final void setMotion(float f) {
        this.motion = f;
    }

    public JumpEvent(float motion) {
        this.motion = motion;
    }
}
