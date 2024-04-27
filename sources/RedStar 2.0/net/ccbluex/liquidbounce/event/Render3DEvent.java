package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.Event;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\u000020B\r0¢R0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "partialTicks", "", "(F)V", "getPartialTicks", "()F", "Pride"})
public final class Render3DEvent
extends Event {
    private final float partialTicks;

    public final float getPartialTicks() {
        return this.partialTicks;
    }

    public Render3DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
