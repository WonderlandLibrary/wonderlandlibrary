/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class InventoryCraftResult
implements IInventory {
    private ItemStack[] stackResult = new ItemStack[1];

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void setField(int n, int n2) {
    }

    @Override
    public void markDirty() {
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return true;
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        if (this.stackResult[0] != null) {
            ItemStack itemStack = this.stackResult[0];
            this.stackResult[0] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        if (this.stackResult[0] != null) {
            ItemStack itemStack = this.stackResult[0];
            this.stackResult[0] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void clear() {
        int n = 0;
        while (n < this.stackResult.length) {
            this.stackResult[n] = null;
            ++n;
        }
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        this.stackResult[0] = itemStack;
    }

    @Override
    public String getName() {
        return "Result";
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        return this.stackResult[0];
    }

    @Override
    public int getField(int n) {
        return 0;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }
}

