/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class InventoryCrafting
implements IInventory {
    private final int inventoryHeight;
    private final int inventoryWidth;
    private final Container eventHandler;
    private final ItemStack[] stackList;

    @Override
    public ItemStack removeStackFromSlot(int n) {
        if (this.stackList[n] != null) {
            ItemStack itemStack = this.stackList[n];
            this.stackList[n] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        if (this.stackList[n] != null) {
            if (this.stackList[n].stackSize <= n2) {
                ItemStack itemStack = this.stackList[n];
                this.stackList[n] = null;
                this.eventHandler.onCraftMatrixChanged(this);
                return itemStack;
            }
            ItemStack itemStack = this.stackList[n].splitStack(n2);
            if (this.stackList[n].stackSize == 0) {
                this.stackList[n] = null;
            }
            this.eventHandler.onCraftMatrixChanged(this);
            return itemStack;
        }
        return null;
    }

    public ItemStack getStackInRowAndColumn(int n, int n2) {
        return n >= 0 && n < this.inventoryWidth && n2 >= 0 && n2 <= this.inventoryHeight ? this.getStackInSlot(n + n2 * this.inventoryWidth) : null;
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        return n >= this.getSizeInventory() ? null : this.stackList[n];
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public InventoryCrafting(Container container, int n, int n2) {
        int n3 = n * n2;
        this.stackList = new ItemStack[n3];
        this.eventHandler = container;
        this.inventoryWidth = n;
        this.inventoryHeight = n2;
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        this.stackList[n] = itemStack;
        this.eventHandler.onCraftMatrixChanged(this);
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public int getField(int n) {
        return 0;
    }

    public int getHeight() {
        return this.inventoryHeight;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public void setField(int n, int n2) {
    }

    @Override
    public void clear() {
        int n = 0;
        while (n < this.stackList.length) {
            this.stackList[n] = null;
            ++n;
        }
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public String getName() {
        return "container.crafting";
    }

    @Override
    public int getSizeInventory() {
        return this.stackList.length;
    }

    public int getWidth() {
        return this.inventoryWidth;
    }
}

