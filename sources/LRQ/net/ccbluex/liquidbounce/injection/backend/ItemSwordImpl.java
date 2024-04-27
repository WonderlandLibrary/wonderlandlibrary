/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemSword
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.item.IItemSword;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public final class ItemSwordImpl
extends ItemImpl<ItemSword>
implements IItemSword {
    @Override
    public float getDamageVsEntity() {
        return ((ItemSword)this.getWrapped()).func_150931_i();
    }

    public ItemSwordImpl(ItemSword wrapped) {
        super((Item)wrapped);
    }
}

