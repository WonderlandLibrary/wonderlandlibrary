/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.item;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

public abstract class EntityMinecartContainer
extends EntityMinecart
implements ILockableContainer {
    private ItemStack[] minecartContainerItems = new ItemStack[36];
    private boolean dropContentsWhenDead = true;

    public EntityMinecartContainer(World worldIn) {
        super(worldIn);
    }

    public EntityMinecartContainer(World worldIn, double p_i1717_2_, double p_i1717_4_, double p_i1717_6_) {
        super(worldIn, p_i1717_2_, p_i1717_4_, p_i1717_6_);
    }

    @Override
    public void killMinecart(DamageSource p_94095_1_) {
        super.killMinecart(p_94095_1_);
        if (!this.worldObj.getGameRules().getBoolean("doEntityDrops")) return;
        InventoryHelper.func_180176_a(this.worldObj, this, this);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.minecartContainerItems[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.minecartContainerItems[index] == null) return null;
        if (this.minecartContainerItems[index].stackSize <= count) {
            ItemStack itemstack1 = this.minecartContainerItems[index];
            this.minecartContainerItems[index] = null;
            return itemstack1;
        }
        ItemStack itemstack = this.minecartContainerItems[index].splitStack(count);
        if (this.minecartContainerItems[index].stackSize != 0) return itemstack;
        this.minecartContainerItems[index] = null;
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.minecartContainerItems[index] == null) return null;
        ItemStack itemstack = this.minecartContainerItems[index];
        this.minecartContainerItems[index] = null;
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.minecartContainerItems[index] = stack;
        if (stack == null) return;
        if (stack.stackSize <= this.getInventoryStackLimit()) return;
        stack.stackSize = this.getInventoryStackLimit();
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (this.isDead) {
            return false;
        }
        if (!(player.getDistanceSqToEntity(this) <= 64.0)) return false;
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
    public String getName() {
        if (!this.hasCustomName()) return "container.minecart";
        String string = this.getCustomNameTag();
        return string;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void travelToDimension(int dimensionId) {
        this.dropContentsWhenDead = false;
        super.travelToDimension(dimensionId);
    }

    @Override
    public void setDead() {
        if (this.dropContentsWhenDead) {
            InventoryHelper.func_180176_a(this.worldObj, this, this);
        }
        super.setDead();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        NBTTagList nbttaglist = new NBTTagList();
        int i = 0;
        while (true) {
            if (i >= this.minecartContainerItems.length) {
                tagCompound.setTag("Items", nbttaglist);
                return;
            }
            if (this.minecartContainerItems[i] != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.minecartContainerItems[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
            ++i;
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        NBTTagList nbttaglist = tagCompund.getTagList("Items", 10);
        this.minecartContainerItems = new ItemStack[this.getSizeInventory()];
        int i = 0;
        while (i < nbttaglist.tagCount()) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.minecartContainerItems.length) {
                this.minecartContainerItems[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
            ++i;
        }
    }

    @Override
    public boolean interactFirst(EntityPlayer playerIn) {
        if (this.worldObj.isRemote) return true;
        playerIn.displayGUIChest(this);
        return true;
    }

    @Override
    protected void applyDrag() {
        int i = 15 - Container.calcRedstoneFromInventory(this);
        float f = 0.98f + (float)i * 0.001f;
        this.motionX *= (double)f;
        this.motionY *= 0.0;
        this.motionZ *= (double)f;
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
        return false;
    }

    @Override
    public void setLockCode(LockCode code) {
    }

    @Override
    public LockCode getLockCode() {
        return LockCode.EMPTY_CODE;
    }

    @Override
    public void clear() {
        int i = 0;
        while (i < this.minecartContainerItems.length) {
            this.minecartContainerItems[i] = null;
            ++i;
        }
    }
}

