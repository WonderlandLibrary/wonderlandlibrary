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
    public static final ItemStack unwrap(IItemStack $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((ItemStackImpl)$this$unwrap).getWrapped();
    }

    public static final IItemStack wrap(ItemStack $this$wrap) {
        int $i$f$wrap = 0;
        return new ItemStackImpl($this$wrap);
    }
}

