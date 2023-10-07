// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.storage;

import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.connection.StorableObject;

public class PlayerLastCursorItem implements StorableObject
{
    private Item lastCursorItem;
    
    public Item getLastCursorItem() {
        return copyItem(this.lastCursorItem);
    }
    
    public void setLastCursorItem(final Item item) {
        this.lastCursorItem = copyItem(item);
    }
    
    public void setLastCursorItem(final Item item, final int amount) {
        (this.lastCursorItem = copyItem(item)).setAmount(amount);
    }
    
    public boolean isSet() {
        return this.lastCursorItem != null;
    }
    
    private static Item copyItem(final Item item) {
        if (item == null) {
            return null;
        }
        final Item copy = new DataItem(item);
        copy.setTag((copy.tag() == null) ? null : copy.tag().clone());
        return copy;
    }
}
