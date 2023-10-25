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

    @Override
    public void setDead() {
        if (this.dropContentsWhenDead) {
            InventoryHelper.func_180176_a(this.worldObj, this, this);
        }
        super.setDead();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        NBTTagList nBTTagList = new NBTTagList();
        int n = 0;
        while (n < this.minecartContainerItems.length) {
            if (this.minecartContainerItems[n] != null) {
                NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
                nBTTagCompound2.setByte("Slot", (byte)n);
                this.minecartContainerItems[n].writeToNBT(nBTTagCompound2);
                nBTTagList.appendTag(nBTTagCompound2);
            }
            ++n;
        }
        nBTTagCompound.setTag("Items", nBTTagList);
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        return this.minecartContainerItems[n];
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public LockCode getLockCode() {
        return LockCode.EMPTY_CODE;
    }

    @Override
    public int getField(int n) {
        return 0;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        if (this.minecartContainerItems[n] != null) {
            if (this.minecartContainerItems[n].stackSize <= n2) {
                ItemStack itemStack = this.minecartContainerItems[n];
                this.minecartContainerItems[n] = null;
                return itemStack;
            }
            ItemStack itemStack = this.minecartContainerItems[n].splitStack(n2);
            if (this.minecartContainerItems[n].stackSize == 0) {
                this.minecartContainerItems[n] = null;
            }
            return itemStack;
        }
        return null;
    }

    @Override
    public void setLockCode(LockCode lockCode) {
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return true;
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : "container.minecart";
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        this.minecartContainerItems[n] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void clear() {
        int n = 0;
        while (n < this.minecartContainerItems.length) {
            this.minecartContainerItems[n] = null;
            ++n;
        }
    }

    @Override
    protected void applyDrag() {
        int n = 15 - Container.calcRedstoneFromInventory(this);
        float f = 0.98f + (float)n * 0.001f;
        this.motionX *= (double)f;
        this.motionY *= 0.0;
        this.motionZ *= (double)f;
    }

    public EntityMinecartContainer(World world) {
        super(world);
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        if (this.minecartContainerItems[n] != null) {
            ItemStack itemStack = this.minecartContainerItems[n];
            this.minecartContainerItems[n] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public void travelToDimension(int n) {
        this.dropContentsWhenDead = false;
        super.travelToDimension(n);
    }

    @Override
    public void setField(int n, int n2) {
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Items", 10);
        this.minecartContainerItems = new ItemStack[this.getSizeInventory()];
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(n);
            int n2 = nBTTagCompound2.getByte("Slot") & 0xFF;
            if (n2 >= 0 && n2 < this.minecartContainerItems.length) {
                this.minecartContainerItems[n2] = ItemStack.loadItemStackFromNBT(nBTTagCompound2);
            }
            ++n;
        }
    }

    @Override
    public boolean interactFirst(EntityPlayer entityPlayer) {
        if (!this.worldObj.isRemote) {
            entityPlayer.displayGUIChest(this);
        }
        return true;
    }

    @Override
    public void killMinecart(DamageSource damageSource) {
        super.killMinecart(damageSource);
        if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
            InventoryHelper.func_180176_a(this.worldObj, this, this);
        }
    }

    public EntityMinecartContainer(World world, double d, double d2, double d3) {
        super(world, d, d2, d3);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return this.isDead ? false : entityPlayer.getDistanceSqToEntity(this) <= 64.0;
    }
}

