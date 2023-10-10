// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_12to1_11_1.storage;

public class ItemTransaction
{
    private final short windowId;
    private final short slotId;
    private final short actionId;
    
    public ItemTransaction(final short windowId, final short slotId, final short actionId) {
        this.windowId = windowId;
        this.slotId = slotId;
        this.actionId = actionId;
    }
    
    public short getWindowId() {
        return this.windowId;
    }
    
    public short getSlotId() {
        return this.slotId;
    }
    
    public short getActionId() {
        return this.actionId;
    }
    
    @Override
    public String toString() {
        return "ItemTransaction{windowId=" + this.windowId + ", slotId=" + this.slotId + ", actionId=" + this.actionId + '}';
    }
}
