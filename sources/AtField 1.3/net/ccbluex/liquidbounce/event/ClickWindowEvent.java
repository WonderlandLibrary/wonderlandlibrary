/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.event.CancellableEvent;

public final class ClickWindowEvent
extends CancellableEvent {
    private final int mouseButtonClicked;
    private final int slotId;
    private final int mode;
    private final int windowId;

    public ClickWindowEvent(int n, int n2, int n3, int n4) {
        this.windowId = n;
        this.slotId = n2;
        this.mouseButtonClicked = n3;
        this.mode = n4;
    }

    public final int getSlotId() {
        return this.slotId;
    }

    public final int getMode() {
        return this.mode;
    }

    public final int getWindowId() {
        return this.windowId;
    }

    public final int getMouseButtonClicked() {
        return this.mouseButtonClicked;
    }
}

