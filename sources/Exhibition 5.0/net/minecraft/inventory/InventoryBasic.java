// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import com.google.common.collect.Lists;
import net.minecraft.util.IChatComponent;
import java.util.List;
import net.minecraft.item.ItemStack;

public class InventoryBasic implements IInventory
{
    private String inventoryTitle;
    private int slotsCount;
    private ItemStack[] inventoryContents;
    private List field_70480_d;
    private boolean hasCustomName;
    private static final String __OBFID = "CL_00001514";
    
    public InventoryBasic(final String p_i1561_1_, final boolean p_i1561_2_, final int p_i1561_3_) {
        this.inventoryTitle = p_i1561_1_;
        this.hasCustomName = p_i1561_2_;
        this.slotsCount = p_i1561_3_;
        this.inventoryContents = new ItemStack[p_i1561_3_];
    }
    
    public InventoryBasic(final IChatComponent p_i45902_1_, final int p_i45902_2_) {
        this(p_i45902_1_.getUnformattedText(), true, p_i45902_2_);
    }
    
    public void func_110134_a(final IInvBasic p_110134_1_) {
        if (this.field_70480_d == null) {
            this.field_70480_d = Lists.newArrayList();
        }
        this.field_70480_d.add(p_110134_1_);
    }
    
    public void func_110132_b(final IInvBasic p_110132_1_) {
        this.field_70480_d.remove(p_110132_1_);
    }
    
    @Override
    public ItemStack getStackInSlot(final int slotIn) {
        return (slotIn >= 0 && slotIn < this.inventoryContents.length) ? this.inventoryContents[slotIn] : null;
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.inventoryContents[index] == null) {
            return null;
        }
        if (this.inventoryContents[index].stackSize <= count) {
            final ItemStack var3 = this.inventoryContents[index];
            this.inventoryContents[index] = null;
            this.markDirty();
            return var3;
        }
        final ItemStack var3 = this.inventoryContents[index].splitStack(count);
        if (this.inventoryContents[index].stackSize == 0) {
            this.inventoryContents[index] = null;
        }
        this.markDirty();
        return var3;
    }
    
    public ItemStack func_174894_a(final ItemStack p_174894_1_) {
        final ItemStack var2 = p_174894_1_.copy();
        for (int var3 = 0; var3 < this.slotsCount; ++var3) {
            final ItemStack var4 = this.getStackInSlot(var3);
            if (var4 == null) {
                this.setInventorySlotContents(var3, var2);
                this.markDirty();
                return null;
            }
            if (ItemStack.areItemsEqual(var4, var2)) {
                final int var5 = Math.min(this.getInventoryStackLimit(), var4.getMaxStackSize());
                final int var6 = Math.min(var2.stackSize, var5 - var4.stackSize);
                if (var6 > 0) {
                    final ItemStack itemStack = var4;
                    itemStack.stackSize += var6;
                    final ItemStack itemStack2 = var2;
                    itemStack2.stackSize -= var6;
                    if (var2.stackSize <= 0) {
                        this.markDirty();
                        return null;
                    }
                }
            }
        }
        if (var2.stackSize != p_174894_1_.stackSize) {
            this.markDirty();
        }
        return var2;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(final int index) {
        if (this.inventoryContents[index] != null) {
            final ItemStack var2 = this.inventoryContents[index];
            this.inventoryContents[index] = null;
            return var2;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.inventoryContents[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }
    
    @Override
    public int getSizeInventory() {
        return this.slotsCount;
    }
    
    @Override
    public String getName() {
        return this.inventoryTitle;
    }
    
    @Override
    public boolean hasCustomName() {
        return this.hasCustomName;
    }
    
    public void func_110133_a(final String p_110133_1_) {
        this.hasCustomName = true;
        this.inventoryTitle = p_110133_1_;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void markDirty() {
        if (this.field_70480_d != null) {
            for (int var1 = 0; var1 < this.field_70480_d.size(); ++var1) {
                this.field_70480_d.get(var1).onInventoryChanged(this);
            }
        }
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer playerIn) {
        return true;
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
        for (int var1 = 0; var1 < this.inventoryContents.length; ++var1) {
            this.inventoryContents[var1] = null;
        }
    }
}
