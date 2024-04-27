/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viabackwards.protocol.protocol1_16_4to1_17.storage;

import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.api.minecraft.item.Item;

public class PlayerLastCursorItem
implements StorableObject {
    private Item lastCursorItem;

    public Item getLastCursorItem() {
        return PlayerLastCursorItem.copyItem(this.lastCursorItem);
    }

    public void setLastCursorItem(Item item) {
        this.lastCursorItem = PlayerLastCursorItem.copyItem(item);
    }

    public void setLastCursorItem(Item item, int amount) {
        this.lastCursorItem = PlayerLastCursorItem.copyItem(item);
        this.lastCursorItem.setAmount(amount);
    }

    public boolean isSet() {
        return this.lastCursorItem != null;
    }

    private static Item copyItem(Item item) {
        DataItem copy;
        if (item == null) {
            return null;
        }
        copy.setTag((copy = new DataItem(item)).tag() == null ? null : copy.tag().clone());
        return copy;
    }
}

