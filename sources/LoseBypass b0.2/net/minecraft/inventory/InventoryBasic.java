/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class InventoryBasic
implements IInventory {
    private String inventoryTitle;
    private int slotsCount;
    private ItemStack[] inventoryContents;
    private List<IInvBasic> field_70480_d;
    private boolean hasCustomName;

    public InventoryBasic(String title, boolean customName, int slotCount) {
        this.inventoryTitle = title;
        this.hasCustomName = customName;
        this.slotsCount = slotCount;
        this.inventoryContents = new ItemStack[slotCount];
    }

    public InventoryBasic(IChatComponent title, int slotCount) {
        this(title.getUnformattedText(), true, slotCount);
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
    public ItemStack getStackInSlot(int index) {
        if (index < 0) return null;
        if (index >= this.inventoryContents.length) return null;
        ItemStack itemStack = this.inventoryContents[index];
        return itemStack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.inventoryContents[index] == null) return null;
        if (this.inventoryContents[index].stackSize <= count) {
            ItemStack itemstack1 = this.inventoryContents[index];
            this.inventoryContents[index] = null;
            this.markDirty();
            return itemstack1;
        }
        ItemStack itemstack = this.inventoryContents[index].splitStack(count);
        if (this.inventoryContents[index].stackSize == 0) {
            this.inventoryContents[index] = null;
        }
        this.markDirty();
        return itemstack;
    }

    public ItemStack func_174894_a(ItemStack stack) {
        ItemStack itemstack = stack.copy();
        int i = 0;
        while (true) {
            int j;
            int k;
            if (i >= this.slotsCount) {
                if (itemstack.stackSize == stack.stackSize) return itemstack;
                this.markDirty();
                return itemstack;
            }
            ItemStack itemstack1 = this.getStackInSlot(i);
            if (itemstack1 == null) {
                this.setInventorySlotContents(i, itemstack);
                this.markDirty();
                return null;
            }
            if (ItemStack.areItemsEqual(itemstack1, itemstack) && (k = Math.min(itemstack.stackSize, (j = Math.min(this.getInventoryStackLimit(), itemstack1.getMaxStackSize())) - itemstack1.stackSize)) > 0) {
                itemstack1.stackSize += k;
                itemstack.stackSize -= k;
                if (itemstack.stackSize <= 0) {
                    this.markDirty();
                    return null;
                }
            }
            ++i;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.inventoryContents[index] == null) return null;
        ItemStack itemstack = this.inventoryContents[index];
        this.inventoryContents[index] = null;
        return itemstack;
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

    public void setCustomName(String inventoryTitleIn) {
        this.hasCustomName = true;
        this.inventoryTitle = inventoryTitleIn;
    }

    @Override
    public IChatComponent getDisplayName() {
        ChatComponentStyle chatComponentStyle;
        if (this.hasCustomName()) {
            chatComponentStyle = new ChatComponentText(this.getName());
            return chatComponentStyle;
        }
        chatComponentStyle = new ChatComponentTranslation(this.getName(), new Object[0]);
        return chatComponentStyle;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        if (this.field_70480_d == null) return;
        int i = 0;
        while (i < this.field_70480_d.size()) {
            this.field_70480_d.get(i).onInventoryChanged(this);
            ++i;
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
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
        while (i < this.inventoryContents.length) {
            this.inventoryContents[i] = null;
            ++i;
        }
    }
}

