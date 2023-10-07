// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.item;

import net.minecraft.world.LockCode;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ILockableContainer;

public abstract class EntityMinecartContainer extends EntityMinecart implements ILockableContainer
{
    private ItemStack[] minecartContainerItems;
    private boolean dropContentsWhenDead;
    private static final String __OBFID = "CL_00001674";
    
    public EntityMinecartContainer(final World worldIn) {
        super(worldIn);
        this.minecartContainerItems = new ItemStack[36];
        this.dropContentsWhenDead = true;
    }
    
    public EntityMinecartContainer(final World worldIn, final double p_i1717_2_, final double p_i1717_4_, final double p_i1717_6_) {
        super(worldIn, p_i1717_2_, p_i1717_4_, p_i1717_6_);
        this.minecartContainerItems = new ItemStack[36];
        this.dropContentsWhenDead = true;
    }
    
    @Override
    public void killMinecart(final DamageSource p_94095_1_) {
        super.killMinecart(p_94095_1_);
        InventoryHelper.func_180176_a(this.worldObj, this, this);
    }
    
    @Override
    public ItemStack getStackInSlot(final int slotIn) {
        return this.minecartContainerItems[slotIn];
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.minecartContainerItems[index] == null) {
            return null;
        }
        if (this.minecartContainerItems[index].stackSize <= count) {
            final ItemStack var3 = this.minecartContainerItems[index];
            this.minecartContainerItems[index] = null;
            return var3;
        }
        final ItemStack var3 = this.minecartContainerItems[index].splitStack(count);
        if (this.minecartContainerItems[index].stackSize == 0) {
            this.minecartContainerItems[index] = null;
        }
        return var3;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(final int index) {
        if (this.minecartContainerItems[index] != null) {
            final ItemStack var2 = this.minecartContainerItems[index];
            this.minecartContainerItems[index] = null;
            return var2;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.minecartContainerItems[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }
    
    @Override
    public void markDirty() {
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer playerIn) {
        return !this.isDead && playerIn.getDistanceSqToEntity(this) <= 64.0;
    }
    
    @Override
    public void openInventory(final EntityPlayer playerIn) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer playerIn) {
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return true;
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.getCustomNameTag() : "container.minecart";
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void travelToDimension(final int dimensionId) {
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
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        final NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.minecartContainerItems.length; ++var3) {
            if (this.minecartContainerItems[var3] != null) {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.minecartContainerItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        tagCompound.setTag("Items", var2);
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        final NBTTagList var2 = tagCompund.getTagList("Items", 10);
        this.minecartContainerItems = new ItemStack[this.getSizeInventory()];
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final int var5 = var4.getByte("Slot") & 0xFF;
            if (var5 >= 0 && var5 < this.minecartContainerItems.length) {
                this.minecartContainerItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }
    
    @Override
    public boolean interactFirst(final EntityPlayer playerIn) {
        if (!this.worldObj.isRemote) {
            playerIn.displayGUIChest(this);
        }
        return true;
    }
    
    @Override
    protected void applyDrag() {
        final int var1 = 15 - Container.calcRedstoneFromInventory(this);
        final float var2 = 0.98f + var1 * 0.001f;
        this.motionX *= var2;
        this.motionY *= 0.0;
        this.motionZ *= var2;
    }
    
    @Override
    public int getField(final int id) {
        return 0;
    }
    
    @Override
    public void setField(final int id, final int value) {
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
    public void setLockCode(final LockCode code) {
    }
    
    @Override
    public LockCode getLockCode() {
        return LockCode.EMPTY_CODE;
    }
    
    @Override
    public void clearInventory() {
        for (int var1 = 0; var1 < this.minecartContainerItems.length; ++var1) {
            this.minecartContainerItems[var1] = null;
        }
    }
}
