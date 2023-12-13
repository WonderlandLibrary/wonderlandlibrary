package io.github.nevalackin.client.event.player;

import io.github.nevalackin.client.event.Event;

public final class WindowClickEvent implements Event {

    private final int windowId;
    private final int slot;
    private final int hotBarSlot;
    private final int mode;

    public WindowClickEvent(int windowId, int slot, int hotBarSlot, int mode) {
        this.windowId = windowId;
        this.slot = slot;
        this.hotBarSlot = hotBarSlot;
        this.mode = mode;
    }

    public int getWindowId() {
        return windowId;
    }

    public int getSlot() {
        return slot;
    }

    public int getHotBarSlot() {
        return hotBarSlot;
    }

    public int getMode() {
        return mode;
    }

}
