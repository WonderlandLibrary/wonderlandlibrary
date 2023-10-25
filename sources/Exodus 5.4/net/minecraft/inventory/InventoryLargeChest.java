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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public class InventoryLargeChest
implements ILockableContainer {
    private ILockableContainer upperChest;
    private ILockableContainer lowerChest;
    private String name;

    @Override
    public String getName() {
        return this.upperChest.hasCustomName() ? this.upperChest.getName() : (this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.name);
    }

    public InventoryLargeChest(String string, ILockableContainer iLockableContainer, ILockableContainer iLockableContainer2) {
        this.name = string;
        if (iLockableContainer == null) {
            iLockableContainer = iLockableContainer2;
        }
        if (iLockableContainer2 == null) {
            iLockableContainer2 = iLockableContainer;
        }
        this.upperChest = iLockableContainer;
        this.lowerChest = iLockableContainer2;
        if (iLockableContainer.isLocked()) {
            iLockableContainer2.setLockCode(iLockableContainer.getLockCode());
        } else if (iLockableContainer2.isLocked()) {
            iLockableContainer.setLockCode(iLockableContainer2.getLockCode());
        }
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
        this.upperChest.closeInventory(entityPlayer);
        this.lowerChest.closeInventory(entityPlayer);
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        if (n >= this.upperChest.getSizeInventory()) {
            this.lowerChest.setInventorySlotContents(n - this.upperChest.getSizeInventory(), itemStack);
        } else {
            this.upperChest.setInventorySlotContents(n, itemStack);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return this.upperChest.getInventoryStackLimit();
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        return n >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(n - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(n);
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        return n >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(n - this.upperChest.getSizeInventory(), n2) : this.upperChest.decrStackSize(n, n2);
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public int getField(int n) {
        return 0;
    }

    @Override
    public int getSizeInventory() {
        return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
    }

    @Override
    public void clear() {
        this.upperChest.clear();
        this.lowerChest.clear();
    }

    public boolean isPartOfLargeChest(IInventory iInventory) {
        return this.upperChest == iInventory || this.lowerChest == iInventory;
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        return n >= this.upperChest.getSizeInventory() ? this.lowerChest.removeStackFromSlot(n - this.upperChest.getSizeInventory()) : this.upperChest.removeStackFromSlot(n);
    }

    @Override
    public LockCode getLockCode() {
        return this.upperChest.getLockCode();
    }

    @Override
    public void setField(int n, int n2) {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return this.upperChest.isUseableByPlayer(entityPlayer) && this.lowerChest.isUseableByPlayer(entityPlayer);
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
    public void markDirty() {
        this.upperChest.markDirty();
        this.lowerChest.markDirty();
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerChest(inventoryPlayer, this, entityPlayer);
    }

    @Override
    public String getGuiID() {
        return this.upperChest.getGuiID();
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
        this.upperChest.openInventory(entityPlayer);
        this.lowerChest.openInventory(entityPlayer);
    }

    @Override
    public boolean isLocked() {
        return this.upperChest.isLocked() || this.lowerChest.isLocked();
    }

    @Override
    public void setLockCode(LockCode lockCode) {
        this.upperChest.setLockCode(lockCode);
        this.lowerChest.setLockCode(lockCode);
    }

    @Override
    public boolean hasCustomName() {
        return this.upperChest.hasCustomName() || this.lowerChest.hasCustomName();
    }
}

