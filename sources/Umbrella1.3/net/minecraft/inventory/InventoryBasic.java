/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class InventoryBasic
implements IInventory {
    private String inventoryTitle;
    private int slotsCount;
    private ItemStack[] inventoryContents;
    private List field_70480_d;
    private boolean hasCustomName;
    private static final String __OBFID = "CL_00001514";

    public InventoryBasic(String p_i1561_1_, boolean p_i1561_2_, int p_i1561_3_) {
        this.inventoryTitle = p_i1561_1_;
        this.hasCustomName = p_i1561_2_;
        this.slotsCount = p_i1561_3_;
        this.inventoryContents = new ItemStack[p_i1561_3_];
    }

    public InventoryBasic(IChatComponent p_i45902_1_, int p_i45902_2_) {
        this(p_i45902_1_.getUnformattedText(), true, p_i45902_2_);
    }

    public void func_110134_a(IInvBasic p_110134_1_) {
        if (this.field_70480_d == null) {
            this.field_70480_d = Lists.newArrayList();
        }
        this.field_70480_d.add(p_110134_1_);
    }

    public void func_110132_b(IInvBasic p_110132_1_) {
        this.field_70480_d.remove(p_110132_1_);
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return slotIn >= 0 && slotIn < this.inventoryContents.length ? this.inventoryContents[slotIn] : null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.inventoryContents[index] != null) {
            if (this.inventoryContents[index].stackSize <= count) {
                ItemStack var3 = this.inventoryContents[index];
                this.inventoryContents[index] = null;
                this.markDirty();
                return var3;
            }
            ItemStack var3 = this.inventoryContents[index].splitStack(count);
            if (this.inventoryContents[index].stackSize == 0) {
                this.inventoryContents[index] = null;
            }
            this.markDirty();
            return var3;
        }
        return null;
    }

    public ItemStack func_174894_a(ItemStack p_174894_1_) {
        ItemStack var2 = p_174894_1_.copy();
        for (int var3 = 0; var3 < this.slotsCount; ++var3) {
            int var5;
            int var6;
            ItemStack var4 = this.getStackInSlot(var3);
            if (var4 == null) {
                this.setInventorySlotContents(var3, var2);
                this.markDirty();
                return null;
            }
            if (!ItemStack.areItemsEqual(var4, var2) || (var6 = Math.min(var2.stackSize, (var5 = Math.min(this.getInventoryStackLimit(), var4.getMaxStackSize())) - var4.stackSize)) <= 0) continue;
            var4.stackSize += var6;
            var2.stackSize -= var6;
            if (var2.stackSize > 0) continue;
            this.markDirty();
            return null;
        }
        if (var2.stackSize != p_174894_1_.stackSize) {
            this.markDirty();
        }
        return var2;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.inventoryContents[index] != null) {
            ItemStack var2 = this.inventoryContents[index];
            this.inventoryContents[index] = null;
            return var2;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
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

    public void func_110133_a(String p_110133_1_) {
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
                ((IInvBasic)this.field_70480_d.get(var1)).onInventoryChanged(this);
            }
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer playerIn) {
    }

    @Override
    public void closeInventory(EntityPlayer playerIn) {
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
    public void clearInventory() {
        for (int var1 = 0; var1 < this.inventoryContents.length; ++var1) {
            this.inventoryContents[var1] = null;
        }
    }
}

