package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.CancellableEvent;

public class ClickWindowEvent extends CancellableEvent {
    private int windowId;
    private int slotId;
    private int mouseButtonClicked;
    private int mode;

    public int getWindowId() {
        return this.windowId;
    }

    public int getSlotId() {
        return this.slotId;
    }

    public int getMouseButtonClicked() {
        return this.mouseButtonClicked;
    }

    public int getMode() {
        return this.mode;
    }

    public ClickWindowEvent(int windowId, int slotId, int mouseButtonClicked, int mode) {
        this.windowId = windowId;
        this.slotId = slotId;
        this.mouseButtonClicked = mouseButtonClicked;
        this.mode = mode;
    }
}
