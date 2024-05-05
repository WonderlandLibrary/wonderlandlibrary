package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
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

    public EntityMinecartContainer(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public void killMinecart(DamageSource source) {
        super.killMinecart(source);
        if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
            InventoryHelper.dropInventoryItems((World)this.worldObj, (Entity)this, (IInventory)this);
        }
    }

    public ItemStack getStackInSlot(int index) {
        return this.minecartContainerItems[index];
    }

    public ItemStack decrStackSize(int index, int count) {
        if (this.minecartContainerItems[index] != null) {
            if (this.minecartContainerItems[index].stackSize <= count) {
                ItemStack itemstack1 = this.minecartContainerItems[index];
                this.minecartContainerItems[index] = null;
                return itemstack1;
            }
            ItemStack itemstack = this.minecartContainerItems[index].splitStack(count);
            if (this.minecartContainerItems[index].stackSize == 0) {
                this.minecartContainerItems[index] = null;
            }
            return itemstack;
        }
        return null;
    }

    public ItemStack removeStackFromSlot(int index) {
        if (this.minecartContainerItems[index] != null) {
            ItemStack itemstack = this.minecartContainerItems[index];
            this.minecartContainerItems[index] = null;
            return itemstack;
        }
        return null;
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        this.minecartContainerItems[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    public void markDirty() {
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.isDead ? false : player.getDistanceSqToEntity((Entity)this) <= 64.0;
    }

    public void openInventory(EntityPlayer player) {
    }

    public void closeInventory(EntityPlayer player) {
    }

    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : "container.minecart";
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public void travelToDimension(int dimensionId) {
        this.dropContentsWhenDead = false;
        super.travelToDimension(dimensionId);
    }

    public void setDead() {
        if (this.dropContentsWhenDead) {
            InventoryHelper.dropInventoryItems((World)this.worldObj, (Entity)this, (IInventory)this);
        }
        super.setDead();
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.minecartContainerItems.length; ++i) {
            if (this.minecartContainerItems[i] == null) continue;
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setByte("Slot", (byte)i);
            this.minecartContainerItems[i].writeToNBT(nbttagcompound);
            nbttaglist.appendTag((NBTBase)nbttagcompound);
        }
        tagCompound.setTag("Items", (NBTBase)nbttaglist);
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        NBTTagList nbttaglist = tagCompund.getTagList("Items", 10);
        this.minecartContainerItems = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 0xFF;
            if (j < 0 || j >= this.minecartContainerItems.length) continue;
            this.minecartContainerItems[j] = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbttagcompound);
        }
    }

    public boolean interactFirst(EntityPlayer playerIn) {
        if (!this.worldObj.isRemote) {
            playerIn.displayGUIChest((IInventory)this);
        }
        return true;
    }

    protected void applyDrag() {
        int i = 15 - Container.calcRedstoneFromInventory((IInventory)this);
        float f = 0.98f + (float)i * 0.001f;
        this.motionX *= (double)f;
        this.motionY *= 0.0;
        this.motionZ *= (double)f;
    }

    public int getField(int id) {
        return 0;
    }

    public void setField(int id, int value) {
    }

    public int getFieldCount() {
        return 0;
    }

    public boolean isLocked() {
        return false;
    }

    public void setLockCode(LockCode code) {
    }

    public LockCode getLockCode() {
        return LockCode.EMPTY_CODE;
    }

    public void clear() {
        for (int i = 0; i < this.minecartContainerItems.length; ++i) {
            this.minecartContainerItems[i] = null;
        }
    }
}
