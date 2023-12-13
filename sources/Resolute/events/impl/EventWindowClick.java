// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import vip.Resolute.events.Event;

public class EventWindowClick extends Event<EventWindowClick>
{
    private final int windowId;
    private final int slot;
    private final int hotbarSlot;
    private final int mode;
    
    public EventWindowClick(final int windowId, final int slot, final int hotbarSlot, final int mode) {
        this.windowId = windowId;
        this.slot = slot;
        this.hotbarSlot = hotbarSlot;
        this.mode = mode;
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public int getHotbarSlot() {
        return this.hotbarSlot;
    }
    
    public int getMode() {
        return this.mode;
    }
}
