/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.List;
import net.ccbluex.liquidbounce.api.minecraft.entity.player.IInventoryPlayer;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.util.IWrappedArray;
import net.ccbluex.liquidbounce.api.util.WrappedListArrayAdapter;
import net.ccbluex.liquidbounce.injection.backend.InventoryPlayerImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class InventoryPlayerImpl
implements IInventoryPlayer {
    private final InventoryPlayer wrapped;

    @Override
    public IItemStack getStackInSlot(int n) {
        IItemStack iItemStack;
        ItemStack itemStack = this.wrapped.func_70301_a(n);
        if (itemStack != null) {
            ItemStack itemStack2 = itemStack;
            boolean bl = false;
            iItemStack = new ItemStackImpl(itemStack2);
        } else {
            iItemStack = null;
        }
        return iItemStack;
    }

    @Override
    public IWrappedArray getMainInventory() {
        return new WrappedListArrayAdapter((List)this.wrapped.field_70462_a, mainInventory.1.INSTANCE, mainInventory.2.INSTANCE);
    }

    @Override
    public int getCurrentItem() {
        return this.wrapped.field_70461_c;
    }

    public InventoryPlayerImpl(InventoryPlayer inventoryPlayer) {
        this.wrapped = inventoryPlayer;
    }

    @Override
    public IItemStack armorItemInSlot(int n) {
        IItemStack iItemStack;
        ItemStack itemStack = this.wrapped.func_70440_f(3 - n);
        if (itemStack != null) {
            ItemStack itemStack2 = itemStack;
            boolean bl = false;
            iItemStack = new ItemStackImpl(itemStack2);
        } else {
            iItemStack = null;
        }
        return iItemStack;
    }

    public final InventoryPlayer getWrapped() {
        return this.wrapped;
    }

    @Override
    public IWrappedArray getArmorInventory() {
        return new WrappedListArrayAdapter((List)this.wrapped.field_70460_b, armorInventory.1.INSTANCE, armorInventory.2.INSTANCE);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof InventoryPlayerImpl && ((InventoryPlayerImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public void setCurrentItem(int n) {
        this.wrapped.field_70461_c = n;
    }

    @Override
    public IItemStack getCurrentItemInHand() {
        IItemStack iItemStack;
        ItemStack itemStack = this.wrapped.func_70448_g();
        if (itemStack != null) {
            ItemStack itemStack2 = itemStack;
            boolean bl = false;
            iItemStack = new ItemStackImpl(itemStack2);
        } else {
            iItemStack = null;
        }
        return iItemStack;
    }

    @Override
    public IWrappedArray getOffHandInventory() {
        return new WrappedListArrayAdapter((List)this.wrapped.field_184439_c, offHandInventory.1.INSTANCE, offHandInventory.2.INSTANCE);
    }
}

