/*
 * Decompiled with CFR 0.152.
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
    private List<IInvBasic> field_70480_d;
    private String inventoryTitle;
    private boolean hasCustomName;
    private int slotsCount;
    private ItemStack[] inventoryContents;

    @Override
    public String getName() {
        return this.inventoryTitle;
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    public void func_110132_b(IInvBasic iInvBasic) {
        this.field_70480_d.remove(iInvBasic);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public void setInventorySlotContents(int n, ItemStack itemStack) {
        this.inventoryContents[n] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    public InventoryBasic(String string, boolean bl, int n) {
        this.inventoryTitle = string;
        this.hasCustomName = bl;
        this.slotsCount = n;
        this.inventoryContents = new ItemStack[n];
    }

    @Override
    public void openInventory(EntityPlayer entityPlayer) {
    }

    public InventoryBasic(IChatComponent iChatComponent, int n) {
        this(iChatComponent.getUnformattedText(), true, n);
    }

    @Override
    public void clear() {
        int n = 0;
        while (n < this.inventoryContents.length) {
            this.inventoryContents[n] = null;
            ++n;
        }
    }

    @Override
    public boolean isItemValidForSlot(int n, ItemStack itemStack) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return this.slotsCount;
    }

    public ItemStack func_174894_a(ItemStack itemStack) {
        ItemStack itemStack2 = itemStack.copy();
        int n = 0;
        while (n < this.slotsCount) {
            int n2;
            int n3;
            ItemStack itemStack3 = this.getStackInSlot(n);
            if (itemStack3 == null) {
                this.setInventorySlotContents(n, itemStack2);
                this.markDirty();
                return null;
            }
            if (ItemStack.areItemsEqual(itemStack3, itemStack2) && (n3 = Math.min(itemStack2.stackSize, (n2 = Math.min(this.getInventoryStackLimit(), itemStack3.getMaxStackSize())) - itemStack3.stackSize)) > 0) {
                itemStack3.stackSize += n3;
                itemStack2.stackSize -= n3;
                if (itemStack2.stackSize <= 0) {
                    this.markDirty();
                    return null;
                }
            }
            ++n;
        }
        if (itemStack2.stackSize != itemStack.stackSize) {
            this.markDirty();
        }
        return itemStack2;
    }

    @Override
    public ItemStack getStackInSlot(int n) {
        return n >= 0 && n < this.inventoryContents.length ? this.inventoryContents[n] : null;
    }

    @Override
    public void markDirty() {
        if (this.field_70480_d != null) {
            int n = 0;
            while (n < this.field_70480_d.size()) {
                this.field_70480_d.get(n).onInventoryChanged(this);
                ++n;
            }
        }
    }

    public void func_110134_a(IInvBasic iInvBasic) {
        if (this.field_70480_d == null) {
            this.field_70480_d = Lists.newArrayList();
        }
        this.field_70480_d.add(iInvBasic);
    }

    @Override
    public ItemStack decrStackSize(int n, int n2) {
        if (this.inventoryContents[n] != null) {
            if (this.inventoryContents[n].stackSize <= n2) {
                ItemStack itemStack = this.inventoryContents[n];
                this.inventoryContents[n] = null;
                this.markDirty();
                return itemStack;
            }
            ItemStack itemStack = this.inventoryContents[n].splitStack(n2);
            if (this.inventoryContents[n].stackSize == 0) {
                this.inventoryContents[n] = null;
            }
            this.markDirty();
            return itemStack;
        }
        return null;
    }

    public void setCustomName(String string) {
        this.hasCustomName = true;
        this.inventoryTitle = string;
    }

    @Override
    public ItemStack removeStackFromSlot(int n) {
        if (this.inventoryContents[n] != null) {
            ItemStack itemStack = this.inventoryContents[n];
            this.inventoryContents[n] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
    }

    @Override
    public boolean hasCustomName() {
        return this.hasCustomName;
    }

    @Override
    public void setField(int n, int n2) {
    }

    @Override
    public int getField(int n) {
        return 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
}

