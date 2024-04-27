package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.Event;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\u000020B\r0¢R0X¢\n\u0000\b\"\b¨\b"}, d2={"Lnet/ccbluex/liquidbounce/event/StepEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "stepHeight", "", "(F)V", "getStepHeight", "()F", "setStepHeight", "Pride"})
public final class StepEvent
extends Event {
    private float stepHeight;

    public final float getStepHeight() {
        return this.stepHeight;
    }

    public final void setStepHeight(float f) {
        this.stepHeight = f;
    }

    public StepEvent(float stepHeight) {
        this.stepHeight = stepHeight;
    }
}
