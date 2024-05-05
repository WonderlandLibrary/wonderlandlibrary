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

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.stacks[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.stacks[index] == null) return null;
        if (this.stacks[index].stackSize <= count) {
            ItemStack itemstack1 = this.stacks[index];
            this.stacks[index] = null;
            this.markDirty();
            return itemstack1;
        }
        ItemStack itemstack = this.stacks[index].splitStack(count);
        if (this.stacks[index].stackSize == 0) {
            this.stacks[index] = null;
        }
        this.markDirty();
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.stacks[index] == null) return null;
        ItemStack itemstack = this.stacks[index];
        this.stacks[index] = null;
        return itemstack;
    }

    public int getDispenseSlot() {
        int i = -1;
        int j = 1;
        int k = 0;
        while (k < this.stacks.length) {
            if (this.stacks[k] != null && RNG.nextInt(j++) == 0) {
                i = k;
            }
            ++k;
        }
        return i;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.stacks[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    public int addItemStack(ItemStack stack) {
        int i = 0;
        while (i < this.stacks.length) {
            if (this.stacks[i] == null || this.stacks[i].getItem() == null) {
                this.setInventorySlotContents(i, stack);
                return i;
            }
            ++i;
        }
        return -1;
    }

    @Override
    public String getName() {
        if (!this.hasCustomName()) return "container.dispenser";
        String string = this.customName;
        return string;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public boolean hasCustomName() {
        if (this.customName == null) return false;
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.stacks = new ItemStack[this.getSizeInventory()];
        int i = 0;
        while (true) {
            if (i >= nbttaglist.tagCount()) {
                if (!compound.hasKey("CustomName", 8)) return;
                this.customName = compound.getString("CustomName");
                return;
            }
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.stacks.length) {
                this.stacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
            ++i;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList nbttaglist = new NBTTagList();
        int i = 0;
        while (true) {
            if (i >= this.stacks.length) {
                compound.setTag("Items", nbttaglist);
                if (!this.hasCustomName()) return;
                compound.setString("CustomName", this.customName);
                return;
            }
            if (this.stacks[i] != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.stacks[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
            ++i;
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (this.worldObj.getTileEntity(this.pos) != this) {
            return false;
        }
        if (!(player.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0)) return false;
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
    public String getGuiID() {
        return "minecraft:dispenser";
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerDispenser(playerInventory, this);
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
        while (i < this.stacks.length) {
            this.stacks[i] = null;
            ++i;
        }
    }
}

