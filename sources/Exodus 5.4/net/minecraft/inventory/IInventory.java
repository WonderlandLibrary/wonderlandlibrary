/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

public interface IInventory
extends IWorldNameable {
    public void clear();

    public ItemStack decrStackSize(int var1, int var2);

    public ItemStack getStackInSlot(int var1);

    public int getSizeInventory();

    public boolean isUseableByPlayer(EntityPlayer var1);

    public void closeInventory(EntityPlayer var1);

    public void markDirty();

    public int getFieldCount();

    public void setField(int var1, int var2);

    public ItemStack removeStackFromSlot(int var1);

    public void setInventorySlotContents(int var1, ItemStack var2);

    public int getField(int var1);

    public boolean isItemValidForSlot(int var1, ItemStack var2);

    public int getInventoryStackLimit();

    public void openInventory(EntityPlayer var1);
}

