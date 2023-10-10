/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public class InventoryLargeChest
implements ILockableContainer {
    private String name;
    private ILockableContainer upperChest;
    private ILockableContainer lowerChest;

    public InventoryLargeChest(String nameIn, ILockableContainer upperChestIn, ILockableContainer lowerChestIn) {
        this.name = nameIn;
        if (upperChestIn == null) {
            upperChestIn = lowerChestIn;
        }
        if (lowerChestIn == null) {
            lowerChestIn = upperChestIn;
        }
        this.upperChest = upperChestIn;
        this.lowerChest = lowerChestIn;
        if (upperChestIn.isLocked()) {
            lowerChestIn.setLockCode(upperChestIn.getLockCode());
            return;
        }
        if (!lowerChestIn.isLocked()) return;
        upperChestIn.setLockCode(lowerChestIn.getLockCode());
    }

    @Override
    public int getSizeInventory() {
        return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
    }

    public boolean isPartOfLargeChest(IInventory inventoryIn) {
        if (this.upperChest == inventoryIn) return true;
        if (this.lowerChest == inventoryIn) return true;
        return false;
    }

    @Override
    public String getName() {
        String string;
        if (this.upperChest.hasCustomName()) {
            string = this.upperChest.getName();
            return string;
        }
        if (this.lowerChest.hasCustomName()) {
            string = this.lowerChest.getName();
            return string;
        }
        string = this.name;
        return string;
    }

    @Override
    public boolean hasCustomName() {
        if (this.upperChest.hasCustomName()) return true;
        if (this.lowerChest.hasCustomName()) return true;
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
    public ItemStack getStackInSlot(int index) {
        ItemStack itemStack;
        if (index >= this.upperChest.getSizeInventory()) {
            itemStack = this.lowerChest.getStackInSlot(index - this.upperChest.getSizeInventory());
            return itemStack;
        }
        itemStack = this.upperChest.getStackInSlot(index);
        return itemStack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemStack;
        if (index >= this.upperChest.getSizeInventory()) {
            itemStack = this.lowerChest.decrStackSize(index - this.upperChest.getSizeInventory(), count);
            return itemStack;
        }
        itemStack = this.upperChest.decrStackSize(index, count);
        return itemStack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemStack;
        if (index >= this.upperChest.getSizeInventory()) {
            itemStack = this.lowerChest.removeStackFromSlot(index - this.upperChest.getSizeInventory());
            return itemStack;
        }
        itemStack = this.upperChest.removeStackFromSlot(index);
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index >= this.upperChest.getSizeInventory()) {
            this.lowerChest.setInventorySlotContents(index - this.upperChest.getSizeInventory(), stack);
            return;
        }
        this.upperChest.setInventorySlotContents(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return this.upperChest.getInventoryStackLimit();
    }

    @Override
    public void markDirty() {
        this.upperChest.markDirty();
        this.lowerChest.markDirty();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (!this.upperChest.isUseableByPlayer(player)) return false;
        if (!this.lowerChest.isUseableByPlayer(player)) return false;
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        this.upperChest.openInventory(player);
        this.lowerChest.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        this.upperChest.closeInventory(player);
        this.lowerChest.closeInventory(player);
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
    public boolean isLocked() {
        if (this.upperChest.isLocked()) return true;
        if (this.lowerChest.isLocked()) return true;
        return false;
    }

    @Override
    public void setLockCode(LockCode code) {
        this.upperChest.setLockCode(code);
        this.lowerChest.setLockCode(code);
    }

    @Override
    public LockCode getLockCode() {
        return this.upperChest.getLockCode();
    }

    @Override
    public String getGuiID() {
        return this.upperChest.getGuiID();
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, this, playerIn);
    }

    @Override
    public void clear() {
        this.upperChest.clear();
        this.lowerChest.clear();
    }
}

