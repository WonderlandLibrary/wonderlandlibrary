// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import java.util.Random;
import net.minecraft.inventory.IInventory;

public class TileEntityDispenser extends TileEntityLockable implements IInventory
{
    private static final Random field_174913_f;
    private ItemStack[] field_146022_i;
    protected String field_146020_a;
    private static final String __OBFID = "CL_00000352";
    
    public TileEntityDispenser() {
        this.field_146022_i = new ItemStack[9];
    }
    
    @Override
    public int getSizeInventory() {
        return 9;
    }
    
    @Override
    public ItemStack getStackInSlot(final int slotIn) {
        return this.field_146022_i[slotIn];
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.field_146022_i[index] == null) {
            return null;
        }
        if (this.field_146022_i[index].stackSize <= count) {
            final ItemStack var3 = this.field_146022_i[index];
            this.field_146022_i[index] = null;
            this.markDirty();
            return var3;
        }
        final ItemStack var3 = this.field_146022_i[index].splitStack(count);
        if (this.field_146022_i[index].stackSize == 0) {
            this.field_146022_i[index] = null;
        }
        this.markDirty();
        return var3;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(final int index) {
        if (this.field_146022_i[index] != null) {
            final ItemStack var2 = this.field_146022_i[index];
            this.field_146022_i[index] = null;
            return var2;
        }
        return null;
    }
    
    public int func_146017_i() {
        int var1 = -1;
        int var2 = 1;
        for (int var3 = 0; var3 < this.field_146022_i.length; ++var3) {
            if (this.field_146022_i[var3] != null && TileEntityDispenser.field_174913_f.nextInt(var2++) == 0) {
                var1 = var3;
            }
        }
        return var1;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.field_146022_i[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }
    
    public int func_146019_a(final ItemStack p_146019_1_) {
        for (int var2 = 0; var2 < this.field_146022_i.length; ++var2) {
            if (this.field_146022_i[var2] == null || this.field_146022_i[var2].getItem() == null) {
                this.setInventorySlotContents(var2, p_146019_1_);
                return var2;
            }
        }
        return -1;
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.field_146020_a : "container.dispenser";
    }
    
    public void func_146018_a(final String p_146018_1_) {
        this.field_146020_a = p_146018_1_;
    }
    
    @Override
    public boolean hasCustomName() {
        return this.field_146020_a != null;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        final NBTTagList var2 = compound.getTagList("Items", 10);
        this.field_146022_i = new ItemStack[this.getSizeInventory()];
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final int var5 = var4.getByte("Slot") & 0xFF;
            if (var5 >= 0 && var5 < this.field_146022_i.length) {
                this.field_146022_i[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
        if (compound.hasKey("CustomName", 8)) {
            this.field_146020_a = compound.getString("CustomName");
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        final NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.field_146022_i.length; ++var3) {
            if (this.field_146022_i[var3] != null) {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.field_146022_i[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        compound.setTag("Items", var2);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.field_146020_a);
        }
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer playerIn) {
        return this.worldObj.getTileEntity(this.pos) == this && playerIn.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
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
    public String getGuiID() {
        return "minecraft:dispenser";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerDispenser(playerInventory, this);
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
    public void clearInventory() {
        for (int var1 = 0; var1 < this.field_146022_i.length; ++var1) {
            this.field_146022_i[var1] = null;
        }
    }
    
    static {
        field_174913_f = new Random();
    }
}
