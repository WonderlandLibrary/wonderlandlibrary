/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class InventoryCrafting
implements IInventory {
    private final ItemStack[] stackList;
    private final int inventoryWidth;
    private final int inventoryHeight;
    private final Container eventHandler;

    public InventoryCrafting(Container eventHandlerIn, int width, int height) {
        int i = width * height;
        this.stackList = new ItemStack[i];
        this.eventHandler = eventHandlerIn;
        this.inventoryWidth = width;
        this.inventoryHeight = height;
    }

    @Override
    public int getSizeInventory() {
        return this.stackList.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index >= this.getSizeInventory()) {
            return null;
        }
        ItemStack itemStack = this.stackList[index];
        return itemStack;
    }

    public ItemStack getStackInRowAndColumn(int row, int column) {
        if (row < 0) return null;
        if (row >= this.inventoryWidth) return null;
        if (column < 0) return null;
        if (column > this.inventoryHeight) return null;
        ItemStack itemStack = this.getStackInSlot(row + column * this.inventoryWidth);
        return itemStack;
    }

    @Override
    public String getName() {
        return "container.crafting";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        ChatComponentStyle chatComponentStyle;
        if (this.hasCustomName()) {
            chatComponentStyle = new ChatComponentText(this.getName());
            return chatComponentStyle;
        }
        chatComponentStyle = new ChatComponentTranslation(this.getName(), new Object[0]);
        return chatComponentStyle;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.stackList[index] == null) return null;
        ItemStack itemstack = this.stackList[index];
        this.stackList[index] = null;
        return itemstack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.stackList[index] == null) return null;
        if (this.stackList[index].stackSize <= count) {
            ItemStack itemstack1 = this.stackList[index];
            this.stackList[index] = null;
            this.eventHandler.onCraftMatrixChanged(this);
            return itemstack1;
        }
        ItemStack itemstack = this.stackList[index].splitStack(count);
        if (this.stackList[index].stackSize == 0) {
            this.stackList[index] = null;
        }
        this.eventHandler.onCraftMatrixChanged(this);
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.stackList[index] = stack;
        this.eventHandler.onCraftMatrixChanged(this);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
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
    public void clear() {
        int i = 0;
        while (i < this.stackList.length) {
            this.stackList[i] = null;
            ++i;
        }
    }

    public int getHeight() {
        return this.inventoryHeight;
    }

    public int getWidth() {
        return this.inventoryWidth;
    }
}

