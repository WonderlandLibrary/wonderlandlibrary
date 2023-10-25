/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryBasic
implements IInventory {
    private String inventoryTitle;
    private final int slotsCount;
    private final NonNullList<ItemStack> inventoryContents;
    private List<IInventoryChangedListener> changeListeners;
    private boolean hasCustomName;

    public InventoryBasic(String title, boolean customName, int slotCount) {
        this.inventoryTitle = title;
        this.hasCustomName = customName;
        this.slotsCount = slotCount;
        this.inventoryContents = NonNullList.func_191197_a(slotCount, ItemStack.field_190927_a);
    }

    public InventoryBasic(ITextComponent title, int slotCount) {
        this(title.getUnformattedText(), true, slotCount);
    }

    public void addInventoryChangeListener(IInventoryChangedListener listener) {
        if (this.changeListeners == null) {
            this.changeListeners = Lists.newArrayList();
        }
        this.changeListeners.add(listener);
    }

    public void removeInventoryChangeListener(IInventoryChangedListener listener) {
        this.changeListeners.remove(listener);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= 0 && index < this.inventoryContents.size() ? this.inventoryContents.get(index) : ItemStack.field_190927_a;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventoryContents, index, count);
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
        return itemstack;
    }

    public ItemStack addItem(ItemStack stack) {
        ItemStack itemstack = stack.copy();
        for (int i = 0; i < this.slotsCount; ++i) {
            ItemStack itemstack1 = this.getStackInSlot(i);
            if (itemstack1.isEmpty()) {
                this.setInventorySlotContents(i, itemstack);
                this.markDirty();
                return ItemStack.field_190927_a;
            }
            if (!ItemStack.areItemsEqual(itemstack1, itemstack)) continue;
            int j = Math.min(this.getInventoryStackLimit(), itemstack1.getMaxStackSize());
            int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());
            if (k <= 0) continue;
            itemstack1.func_190917_f(k);
            itemstack.func_190918_g(k);
            if (!itemstack.isEmpty()) continue;
            this.markDirty();
            return ItemStack.field_190927_a;
        }
        if (itemstack.getCount() != stack.getCount()) {
            this.markDirty();
        }
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemstack = this.inventoryContents.get(index);
        if (itemstack.isEmpty()) {
            return ItemStack.field_190927_a;
        }
        this.inventoryContents.set(index, ItemStack.field_190927_a);
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventoryContents.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.func_190920_e(this.getInventoryStackLimit());
        }
        this.markDirty();
    }

    @Override
    public int getSizeInventory() {
        return this.slotsCount;
    }

    @Override
    public boolean func_191420_l() {
        for (ItemStack itemstack : this.inventoryContents) {
            if (itemstack.isEmpty()) continue;
            return false;
        }
        return true;
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
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        if (this.changeListeners != null) {
            for (int i = 0; i < this.changeListeners.size(); ++i) {
                this.changeListeners.get(i).onInventoryChanged(this);
            }
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
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
        this.inventoryContents.clear();
    }
}

