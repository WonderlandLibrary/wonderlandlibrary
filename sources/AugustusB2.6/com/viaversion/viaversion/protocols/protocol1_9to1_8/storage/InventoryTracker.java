// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.storage;

import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.HashMap;
import java.util.Map;
import com.viaversion.viaversion.api.connection.StorableObject;

public class InventoryTracker implements StorableObject
{
    private String inventory;
    private final Map<Short, Map<Short, Integer>> windowItemCache;
    private int itemIdInCursor;
    private boolean dragging;
    
    public InventoryTracker() {
        this.windowItemCache = new HashMap<Short, Map<Short, Integer>>();
        this.itemIdInCursor = 0;
        this.dragging = false;
    }
    
    public String getInventory() {
        return this.inventory;
    }
    
    public void setInventory(final String inventory) {
        this.inventory = inventory;
    }
    
    public void resetInventory(final short windowId) {
        if (this.inventory == null) {
            this.itemIdInCursor = 0;
            this.dragging = false;
            if (windowId != 0) {
                this.windowItemCache.remove(windowId);
            }
        }
    }
    
    public int getItemId(final short windowId, final short slot) {
        final Map<Short, Integer> itemMap = this.windowItemCache.get(windowId);
        if (itemMap == null) {
            return 0;
        }
        return itemMap.getOrDefault(slot, 0);
    }
    
    public void setItemId(final short windowId, final short slot, final int itemId) {
        if (windowId == -1 && slot == -1) {
            this.itemIdInCursor = itemId;
        }
        else {
            this.windowItemCache.computeIfAbsent(Short.valueOf(windowId), k -> new HashMap()).put(slot, itemId);
        }
    }
    
    public void handleWindowClick(final UserConnection user, final short windowId, final byte mode, final short hoverSlot, final byte button) {
        final EntityTracker1_9 entityTracker = user.getEntityTracker(Protocol1_9To1_8.class);
        if (hoverSlot == -1) {
            return;
        }
        if (hoverSlot == 45) {
            entityTracker.setSecondHand(null);
            return;
        }
        final boolean isArmorOrResultSlot = (hoverSlot >= 5 && hoverSlot <= 8) || hoverSlot == 0;
        switch (mode) {
            case 0: {
                if (this.itemIdInCursor == 0) {
                    this.itemIdInCursor = this.getItemId(windowId, hoverSlot);
                    this.setItemId(windowId, hoverSlot, 0);
                    break;
                }
                if (hoverSlot == -999) {
                    this.itemIdInCursor = 0;
                    break;
                }
                if (!isArmorOrResultSlot) {
                    final int previousItem = this.getItemId(windowId, hoverSlot);
                    this.setItemId(windowId, hoverSlot, this.itemIdInCursor);
                    this.itemIdInCursor = previousItem;
                    break;
                }
                break;
            }
            case 2: {
                if (!isArmorOrResultSlot) {
                    final short hotkeySlot = (short)(button + 36);
                    final int sourceItem = this.getItemId(windowId, hoverSlot);
                    final int destinationItem = this.getItemId(windowId, hotkeySlot);
                    this.setItemId(windowId, hotkeySlot, sourceItem);
                    this.setItemId(windowId, hoverSlot, destinationItem);
                    break;
                }
                break;
            }
            case 4: {
                final int hoverItem = this.getItemId(windowId, hoverSlot);
                if (hoverItem != 0) {
                    this.setItemId(windowId, hoverSlot, 0);
                    break;
                }
                break;
            }
            case 5: {
                switch (button) {
                    case 0:
                    case 4: {
                        this.dragging = true;
                        break;
                    }
                    case 1:
                    case 5: {
                        if (this.dragging && this.itemIdInCursor != 0 && !isArmorOrResultSlot) {
                            final int previousItem2 = this.getItemId(windowId, hoverSlot);
                            this.setItemId(windowId, hoverSlot, this.itemIdInCursor);
                            this.itemIdInCursor = previousItem2;
                            break;
                        }
                        break;
                    }
                    case 2:
                    case 6: {
                        this.dragging = false;
                        break;
                    }
                }
                break;
            }
        }
        entityTracker.syncShieldWithSword();
    }
}
