package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.CancellableEvent;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\b\n\b\n\u000020B%0000¢R0¢\b\n\u0000\b\b\tR0¢\b\n\u0000\b\n\tR0¢\b\n\u0000\b\tR0¢\b\n\u0000\b\f\t¨\r"}, d2={"Lnet/ccbluex/liquidbounce/event/ClickWindowEvent;", "Lnet/ccbluex/liquidbounce/event/CancellableEvent;", "windowId", "", "slotId", "mouseButtonClicked", "mode", "(IIII)V", "getMode", "()I", "getMouseButtonClicked", "getSlotId", "getWindowId", "Pride"})
public final class ClickWindowEvent
extends CancellableEvent {
    private final int windowId;
    private final int slotId;
    private final int mouseButtonClicked;
    private final int mode;

    public final int getWindowId() {
        return this.windowId;
    }

    public final int getSlotId() {
        return this.slotId;
    }

    public final int getMouseButtonClicked() {
        return this.mouseButtonClicked;
    }

    public final int getMode() {
        return this.mode;
    }

    public ClickWindowEvent(int windowId, int slotId, int mouseButtonClicked, int mode) {
        this.windowId = windowId;
        this.slotId = slotId;
        this.mouseButtonClicked = mouseButtonClicked;
        this.mode = mode;
    }
}
