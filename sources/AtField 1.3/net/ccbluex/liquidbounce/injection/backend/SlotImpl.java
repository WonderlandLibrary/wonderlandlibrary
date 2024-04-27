/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.inventory.ISlot;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class SlotImpl
implements ISlot {
    private final Slot wrapped;

    public SlotImpl(Slot slot) {
        this.wrapped = slot;
    }

    @Override
    public IItemStack getStack() {
        IItemStack iItemStack;
        ItemStack itemStack = this.wrapped.func_75211_c();
        if (itemStack != null) {
            ItemStack itemStack2 = itemStack;
            boolean bl = false;
            iItemStack = new ItemStackImpl(itemStack2);
        } else {
            iItemStack = null;
        }
        return iItemStack;
    }

    public final Slot getWrapped() {
        return this.wrapped;
    }

    @Override
    public boolean getHasStack() {
        return this.wrapped.func_75216_d();
    }

    @Override
    public int getSlotNumber() {
        return this.wrapped.field_75222_d;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof SlotImpl && ((SlotImpl)object).wrapped.equals(this.wrapped);
    }
}

