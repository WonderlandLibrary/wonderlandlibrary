/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityLockable;

public class TileEntityDispenser
extends TileEntityLockable
implements IInventory {
    private static final Random RNG = new Random();
    private ItemStack[] stacks = new ItemStack[9];
    protected String customName;

    public void setCustomName(String string) {
        this.customName = string;
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        NBTTagList nBTTagList = new NBTTagList();
        int n = 0;
        while (n < this.stacks.length) {
            if (this.stacks[n] != null) {
                NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
                nBTTagCompound2.setByte("Slot", (byte)n);
                this.stacks[n].writeToNBT(nBTTagCompound2);
                nBTTagList.appendTag(nBTTagCompound2);
            }
            ++n;
        }
        nBTTagCompound.setTag("Items", nBTTagList);
        if (this.hasCustomName()) {
            nBTTagCompound.setString("CustomName", this.customName);
        }
    }

    @Override
    public void clear() {
        int n = 0;
        while (n < this.stacks.length) {
            this.stacks[n] = null;
            ++n;
        }
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public String getGuiID() {
        return "minecraft:dispenser";
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        if (this.stacks[n] != null) {
            if (this.stacks[n].stackSize <= n2) {
                ItemStack itemStack = this.stacks[n];
                this.stacks[n] = null;
                this.markDirty();
                return itemStack;
            }
            ItemStack itemStack = this.stacks[n].splitStack(n2);
            if (this.stacks[n].stackSize == 0) {
                this.stacks[n] = null;
            }
            this.markDirty();
            return itemStack;
        }
        return null;
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerDispenser(inventoryPlayer, this);
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Items", 10);
        this.stacks = new ItemStack[this.getSizeInventory()];
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(n);
            int n2 = nBTTagCompound2.getByte("Slot") & 0xFF;
            if (n2 >= 0 && n2 < this.stacks.length) {
                this.stacks[n2] = ItemStack.loadItemStackFromNBT(nBTTagCompound2);
            }
            ++n;
        }
        if (nBTTagCompound.hasKey("CustomName", 8)) {
            this.customName = nBTTagCompound.getString("CustomName");
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        if (this.stacks[n] != null) {
            ItemStack itemStack = this.stacks[n];
            this.stacks[n] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        return this.stacks[n];
    }

    public int getDispenseSlot() {
        int n = -1;
        int n2 = 1;
        int n3 = 0;
        while (n3 < this.stacks.length) {
            if (this.stacks[n3] != null && RNG.nextInt(n2++) == 0) {
                n = n3;
            }
            ++n3;
        }
        return n;
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return true;
    }

    @Override
    public int getField(int n) {
        return 0;
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        this.stacks[n] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : entityPlayer.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.dispenser";
    }

    public int addItemStack(ItemStack itemStack) {
        int n = 0;
        while (n < this.stacks.length) {
            if (this.stacks[n] == null || this.stacks[n].getItem() == null) {
                this.setInventorySlotContents(n, itemStack);
                return n;
            }
            ++n;
        }
        return -1;
    }

    @Override
    public void setField(int n, int n2) {
    }
}

