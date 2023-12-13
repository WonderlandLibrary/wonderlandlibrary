package vip.radium.event.impl.player;

import vip.radium.event.Event;

public final class WindowClickEvent implements Event {

    private final int windowId;
    private final int slot;
    private final int hotbarSlot;
    private final int mode;

    public WindowClickEvent(int windowId, int slot, int hotbarSlot, int mode) {
        this.windowId = windowId;
        this.slot = slot;
        this.hotbarSlot = hotbarSlot;
        this.mode = mode;
    }

    public int getWindowId() {
        return windowId;
    }

    public int getSlot() {
        return slot;
    }

    public int getHotbarSlot() {
        return hotbarSlot;
    }

    public int getMode() {
        return mode;
    }
}
