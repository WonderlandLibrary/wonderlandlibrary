/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.minecraft.item.ItemStack;

public final class ItemStackImplKt {
    public static final IItemStack wrap(ItemStack itemStack) {
        boolean bl = false;
        return new ItemStackImpl(itemStack);
    }

    public static final ItemStack unwrap(IItemStack iItemStack) {
        boolean bl = false;
        return ((ItemStackImpl)iItemStack).getWrapped();
    }
}

