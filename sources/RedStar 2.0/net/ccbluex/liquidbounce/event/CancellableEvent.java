package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.Event;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\b\n\n\u0000\b\u000020B¢J0\bR020@BX¢\b\n\u0000\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/event/CancellableEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "()V", "<set-?>", "", "isCancelled", "()Z", "cancelEvent", "", "Pride"})
public class CancellableEvent
extends Event {
    private boolean isCancelled;

    public final boolean isCancelled() {
        return this.isCancelled;
    }

    public final void cancelEvent() {
        this.isCancelled = true;
    }
}
