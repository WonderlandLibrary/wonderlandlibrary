/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class InventoryCrafting
implements IInventory {
    private final ItemStack[] stackList;
    private final int inventoryWidth;
    private final int field_174924_c;
    private final Container eventHandler;
    private static final String __OBFID = "CL_00001743";

    public InventoryCrafting(Container p_i1807_1_, int p_i1807_2_, int p_i1807_3_) {
        int var4 = p_i1807_2_ * p_i1807_3_;
        this.stackList = new ItemStack[var4];
        this.eventHandler = p_i1807_1_;
        this.inventoryWidth = p_i1807_2_;
        this.field_174924_c = p_i1807_3_;
    }

    @Override
    public int getSizeInventory() {
        return this.stackList.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return slotIn >= this.getSizeInventory() ? null : this.stackList[slotIn];
    }

    public ItemStack getStackInRowAndColumn(int p_70463_1_, int p_70463_2_) {
        return p_70463_1_ >= 0 && p_70463_1_ < this.inventoryWidth && p_70463_2_ >= 0 && p_70463_2_ <= this.field_174924_c ? this.getStackInSlot(p_70463_1_ + p_70463_2_ * this.inventoryWidth) : null;
    }

    @Override
    public String getName() {
        return "container.crafting";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.stackList[index] != null) {
            ItemStack var2 = this.stackList[index];
            this.stackList[index] = null;
            return var2;
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.stackList[index] != null) {
            if (this.stackList[index].stackSize <= count) {
                ItemStack var3 = this.stackList[index];
                this.stackList[index] = null;
                this.eventHandler.onCraftMatrixChanged(this);
                return var3;
            }
            ItemStack var3 = this.stackList[index].splitStack(count);
            if (this.stackList[index].stackSize == 0) {
                this.stackList[index] = null;
            }
            this.eventHandler.onCraftMatrixChanged(this);
            return var3;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.stackList[index] = stack;
        this.eventHandler.onCraftMatrixChanged(this);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
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
        for (int var1 = 0; var1 < this.stackList.length; ++var1) {
            this.stackList[var1] = null;
        }
    }

    public int func_174923_h() {
        return this.field_174924_c;
    }

    public int func_174922_i() {
        return this.inventoryWidth;
    }
}

