/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemBlock;
import net.ccbluex.liquidbounce.injection.backend.BlockImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public final class ItemBlockImpl
extends ItemImpl<ItemBlock>
implements IItemBlock {
    @Override
    public IBlock getBlock() {
        return new BlockImpl(((ItemBlock)this.getWrapped()).func_179223_d());
    }

    @Override
    public String getUnlocalizedName() {
        return ((ItemBlock)this.getWrapped()).func_77658_a();
    }

    public ItemBlockImpl(ItemBlock wrapped) {
        super((Item)wrapped);
    }
}

