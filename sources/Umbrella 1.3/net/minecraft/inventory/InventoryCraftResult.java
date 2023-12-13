/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00001760";

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return this.stackResult[0];
    }

    @Override
    public String getName() {
        return "Result";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.stackResult[0] != null) {
            ItemStack var3 = this.stackResult[0];
            this.stackResult[0] = null;
            return var3;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.stackResult[0] != null) {
            ItemStack var2 = this.stackResult[0];
            this.stackResult[0] = null;
            return var2;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.stackResult[0] = stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer playerIn) {
    }

    @Override
    public void closeInventory(EntityPlayer playerIn) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clearInventory() {
        for (int var1 = 0; var1 < this.stackResult.length; ++var1) {
            this.stackResult[var1] = null;
        }
    }
}

