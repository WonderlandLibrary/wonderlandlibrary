/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBucket
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemBucket;
import net.ccbluex.liquidbounce.injection.backend.BlockImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;

public final class ItemBucketImpl
extends ItemImpl
implements IItemBucket {
    @Override
    public IBlock isFull() {
        return new BlockImpl(((ItemBucket)this.getWrapped()).field_77876_a);
    }

    public ItemBucketImpl(ItemBucket itemBucket) {
        super((Item)itemBucket);
    }
}

