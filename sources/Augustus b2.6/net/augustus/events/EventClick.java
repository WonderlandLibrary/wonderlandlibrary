// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

public class EventClick extends Event
{
    private boolean shouldRightClick;
    private int slot;
    
    public EventClick(final int slot) {
        this.slot = slot;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public void setSlot(final int slot) {
        this.slot = slot;
    }
    
    public boolean isShouldRightClick() {
        return this.shouldRightClick;
    }
    
    public void setShouldRightClick(final boolean shouldRightClick) {
        this.shouldRightClick = shouldRightClick;
    }
}
